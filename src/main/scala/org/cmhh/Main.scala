package org.cmhh

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.LoggerOps
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  import messages._

  val system = ActorSystem(Coordinator(), "akkatrain")

  val t = new Thread {
    override def run(): Unit = {
      (1 to 120).foreach(i => {
        system ! RequestSendImages(500)
        Thread.sleep(3000)
        system ! RequestAccuracy
      })
    }
  }

  println()
  system ! RequestAccuracy
  Thread.sleep(1000)
  t.start()
  Thread.sleep(3000 * 121)
  system ! Stop
  system.terminate()
}