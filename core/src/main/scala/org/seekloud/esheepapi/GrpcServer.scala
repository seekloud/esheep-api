package org.seekloud.esheepapi

import io.grpc.{Server, ServerBuilder, ServerServiceDefinition}
import org.seekloud.esheepapi.pb.hello.{GreeterGrpc, HelloReply, HelloRequest}

import scala.concurrent.{ExecutionContext, Future}
import io.grpc.{Server, ServerBuilder}
import scalapb.grpc.AbstractService

/**
  * User: Taoz
  * Date: 11/18/2018
  * Time: 2:35 PM
  */
class GrpcServer {

  private var server: Option[Server] = None

  def start(service: ServerServiceDefinition, executionContext: ExecutionContext, port: Int): Boolean = {
    server match {
      case None =>
        val s = ServerBuilder.forPort(port).addService(service).build
        s.start()
        server = Some(s)
        true
      case _ => false
    }
  }

  def stop(): Unit = server.foreach(_.shutdown())

  def blockUntilShutdown(): Unit = server.foreach(_.awaitTermination())


}














