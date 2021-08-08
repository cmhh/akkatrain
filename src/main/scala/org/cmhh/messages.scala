package org.cmhh

import akka.actor.typed.ActorRef

object messages {
  sealed trait CoordinatorCommand 
  case object RequestAccuracy extends CoordinatorCommand
  case class RequestSendImages(n: Int) extends CoordinatorCommand
  case class RequestClassifier(f: String) extends CoordinatorCommand
  case object Stop extends CoordinatorCommand with ProducerCommand with ConsumerCommand
  case class Accuracy(value: Double, n: Int) extends CoordinatorCommand
  case class Classifier(value: Array[Byte], f: String)  extends CoordinatorCommand
  
  sealed trait ProducerCommand
  case class SendImages(sendTo: ActorRef[ConsumerCommand], n: Int) extends ProducerCommand

  sealed trait ConsumerCommand
  case class Image(image: MnistRecord) extends ConsumerCommand
  case class SendAccuracy(replyTo: ActorRef[Accuracy]) extends ConsumerCommand
  case class SendClassifier(replyTo: ActorRef[Classifier], f: String) extends ConsumerCommand 
}