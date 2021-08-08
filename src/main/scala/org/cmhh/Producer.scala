package org.cmhh

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.Behavior

object Producer {
  import messages._

  private val images = MnistIterator(MnistType.TRAIN)

  def apply(): Behavior[ProducerCommand] = 
    Behaviors.receive[ProducerCommand]{ (context, message) => 
      message match {
        case SendImages(r, n) => 
          def loop(i: Int): Behavior[ProducerCommand] = 
            if (!images.hasNext) {
              println("All images sent.  Shutting down.")
              Behaviors.stopped
            } else if (i == n) {
              Behaviors.same
            } else {
              r ! Image(images.next())
              loop(i + 1)
            }
          loop(0)
        case Stop => 
          println("Producer is dead!")
          Behaviors.stopped
      }
    }
}