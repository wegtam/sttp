package sttp.client.examples

object GetAndParseJsonAkkaHttpJson4s extends App {
  import scala.concurrent.Future

  import sttp.client._
  import sttp.client.akkahttp._
  import sttp.client.json4s._

  import scala.concurrent.ExecutionContext.Implicits.global

  case class HttpBinResponse(origin: String, headers: Map[String, String])

  implicit val serialization = org.json4s.native.Serialization
  val request = basicRequest
    .get(uri"https://httpbin.org/get")
    .response(asJson[HttpBinResponse])

  implicit val backend: SttpBackend[Future, Nothing, NothingT] = AkkaHttpBackend()
  val response: Future[Response[Either[ResponseError[Exception], HttpBinResponse]]] =
    request.send()

  for {
    r <- response
  } {
    println(s"Got response code: ${r.code}")
    println(r.body)
    backend.close()
  }
}
