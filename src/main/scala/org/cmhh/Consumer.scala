package org.cmhh

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.Behavior
import org.nd4j.evaluation.classification.Evaluation

object Consumer {
  import messages._

  private val network = model.cnn()
  private val mnistTest = new MnistDataSetIterator(100, MnistType.TEST)

  def apply(): Behavior[ConsumerCommand] = run(0)

  def run(n: Int): Behavior[ConsumerCommand] = 
    Behaviors.receive{ (context, message) => message match {
      case Image(rec) =>
        val im = rec.toNd4j
        network.fit(im._1, im._2)
        run(n + 1)
      case SendAccuracy(replyTo) =>
        replyTo ! Accuracy(accuracy, n)
        Behaviors.same
      case SendClassifier(replyTo, f) =>
        replyTo ! Classifier(network.clone, f)
        Behaviors.same
      case Stop => 
        Behaviors.stopped {() => 
          println("Consumer is dead!")
        }
    }
  }

  def accuracy: Double = {
    val eval = network.evaluate[Evaluation](mnistTest)
    eval.accuracy
  }
}