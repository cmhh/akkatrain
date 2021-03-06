package org.cmhh

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.{ ActorRef, Behavior }
import java.io.File

object Coordinator {
  import messages._

  def apply(): Behavior[CoordinatorCommand] = Behaviors.setup { context => 
    val consumer = context.spawn(Consumer(), "consumer")
    val producer = context.spawn(Producer(), "producer")

    Behaviors.receiveMessage { message => {
      message match {
        case RequestSendImages(n) =>
          producer ! SendImages(consumer, n)
        case RequestAccuracy => 
          consumer ! SendAccuracy(context.self)
        case RequestClassifier(f) =>
          consumer ! SendClassifier(context.self, f)
        case Accuracy(accuracy, n) => 
          println(f"accuracy: %%1.4f, training images seen: %%05d".format(accuracy, n))
        case Classifier(n, f) =>
          n.save(new File(f))
        case Stop =>
          consumer ! Stop
          producer ! Stop
      }
      Behaviors.same
    }}
  }
}