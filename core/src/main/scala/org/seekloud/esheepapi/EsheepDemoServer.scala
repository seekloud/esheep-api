package org.seekloud.esheepapi

import akka.actor.{Actor, ActorRef, ActorSystem, Cancellable, Props}
import io.grpc.stub.StreamObserver
import io.grpc.{Server, ServerBuilder}
import org.seekloud.esheepapi.pb.api._
import org.seekloud.esheepapi.pb.service.EsheepAgentGrpc
import org.seekloud.esheepapi.pb.service.EsheepAgentGrpc.EsheepAgent

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

/**
  * User: Taoz
  * Date: 11/18/2018
  * Time: 12:01 PM
  */
object EsheepDemoServer {

  def build(worker: ActorRef, port: Int, executionContext: ExecutionContext): Server = {

    val service = new EsheepService(worker)

    ServerBuilder.forPort(port).addService(
      EsheepAgentGrpc.bindService(service, executionContext)
    ).build

  }


  def main2(args: Array[String]): Unit = {

    val system = ActorSystem("mysystem")
    val worker = system.actorOf(Props(new Worker()), "worker1")

    Thread.sleep(10000)
    println("DONE.")


  }


  def main(args: Array[String]): Unit = {

    val executor = concurrent.ExecutionContext.Implicits.global
    val port = 5321

    val system = ActorSystem("mysystem")
    val worker = system.actorOf(Props(new Worker()), "worker1")

    val server = EsheepDemoServer.build(worker, port, executor)
    server.start()
    println(s"Server started at $port")

    sys.addShutdownHook {
      println("JVM SHUT DOWN.")
      server.shutdown()
      println("SHUT DOWN.")
    }

    server.awaitTermination()
    println("DONE.")

  }

}


class EsheepService(worker: ActorRef) extends EsheepAgent {
  override def createRoom(request: CreateRoomReq): Future[CreateRoomRsp] = {
    println(s"createRoom Called by [$request")
    val state = State.init_game
    Future.successful(CreateRoomRsp(errCode = 101, state = state, msg = "ok"))
  }

  override def joinRoom(request: JoinRoomReq): Future[SimpleRsp] = {
    println(s"joinRoom Called by [$request")
    val state = State.in_game
    Future.successful(SimpleRsp(errCode = 102, state = state, msg = "ok"))
  }

  override def leaveRoom(request: Credit): Future[SimpleRsp] = {
    println(s"leaveRoom Called by [$request")
    val state = State.ended
    Future.successful(SimpleRsp(errCode = 103, state = state, msg = "ok"))
  }

  override def actionSpace(request: Credit): Future[ActionSpaceRsp] = {
    println(s"actionSpace Called by [$request")
    val rsp = ActionSpaceRsp()
    Future.successful(rsp)
  }

  override def action(request: ActionReq): Future[ActionRsp] = {
    println(s"action Called by [$request")
    val rsp = ActionRsp()
    Future.successful(rsp)
  }

  override def observation(request: Credit): Future[ObservationRsp] = {
    println(s"action Called by [$request")
    val rsp = ObservationRsp()
    Future.successful(rsp)
  }

  override def inform(request: Credit): Future[InformRsp] = {
    println(s"action Called by [$request")
    val rsp = InformRsp()
    Future.successful(rsp)
  }

  override def systemInfo(request: Credit): Future[SystemInfoRsp] = {
    null
  }

  override def currentFrame(request: Credit): Future[CurrentFrameRsp] = {
    null
  }

  override def observationWithInfo(request: Credit): Future[ObservationWithInfoRsp] = {
    null
  }

  override def reincarnation(request: Credit): Future[SimpleRsp] = {
    null
  }

  override def testStream(request: StreamRq, responseObserver: StreamObserver[StreamRsp]): Unit = {
    worker ! responseObserver
    println("testStream function done.")
  }


}


class Worker extends Actor {

  import concurrent.duration._
  import context.dispatcher

  private var counter = 0
  val receive: Receive = waiting()

  private def waiting(): Receive = {
    case "hi" => println("i got [Hi]")
    case stream: StreamObserver[StreamRsp] =>
      println(s"i got StreamObserver[$stream]")
      val timer = context.system.scheduler.schedule(100 milliseconds, 1 seconds, self, "step")
      println("becoming working.")
      context.become(working(stream, timer))
    case x => println(s"unknown msg [$x] in waiting.")
  }


  private def working(
    outSteam: StreamObserver[StreamRsp],
    timer: Cancellable
  ): Receive = {
    case "step" =>
      counter += 1
      println(s"got step, and send $counter")
      outSteam.onNext(StreamRsp(counter))
      if (counter > 10) {
        self ! "stop"
      }
    case "stop" =>
      timer.cancel()
      outSteam.onCompleted()
      counter = 0
      context.become(waiting())
    case x => println(s"unknown msg [$x] in working.")
  }


}



