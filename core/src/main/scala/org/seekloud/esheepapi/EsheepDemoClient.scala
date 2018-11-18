package org.seekloud.esheepapi

import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import org.seekloud.esheepapi.pb.api.{Credit, ObservationRsp, SimpleRsp}
import org.seekloud.esheepapi.pb.service.EsheepAgentGrpc
import org.seekloud.esheepapi.pb.service.EsheepAgentGrpc.EsheepAgentStub

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * User: Taoz
  * Date: 11/18/2018
  * Time: 12:02 PM
  */
class EsheepDemoClient(
  host: String,
  port: Int,
  playerId: String,
  apiToken: String
) {

  private[this] val channel: ManagedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build

  private val esheepStub: EsheepAgentStub = EsheepAgentGrpc.stub(channel)

  val credit = Credit(playerId = playerId, apiToken = apiToken)


  def createRoom(): Future[SimpleRsp] = esheepStub.createRoom(credit)

  def observation(): Future[ObservationRsp] = esheepStub.observation(credit)
}



object EsheepDemoClient{


  def main(args: Array[String]): Unit = {
    //import concurrent.ExecutionContext.Implicits.global

    val host = "127.0.0.1"
    val port = 5321
    val playerId = "gogo"
    val apiToken = "lala"

    val client = new EsheepDemoClient(host, port, playerId, apiToken)

    val rsp1 = client.createRoom()

    val rsp2 = client.observation()

    println("--------  begin sleep   ----------------")
    Thread.sleep(10000)
    println("--------  end sleep   ----------------")

    println(rsp1)
    println("------------------------")
    println(rsp2)
    println("client DONE.")

  }
}







