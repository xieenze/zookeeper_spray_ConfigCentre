package service

import javax.print.attribute.standard.MediaSize.NA

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  val myRoute =
    //访问localhost:8080
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to
                  <i>spray-routing</i>
                  on
                  <i>spray-can</i>
                  !</h1>
              </body>
            </html>
          }
        }
      }
    } ~
      //波浪号的意思是可以定义另外一个接口
      //访问localhost:8080/ping
      path("ping") {
        get {
          respondWithMediaType(`application/json`) {
            complete {
              "{result: 'ping'}"
            }
          }
        }
      } ~
      //访问localhost:8080/configApi?path=xxx
      path("configApi") {
        parameters('configpath.?) {
          (configpath) =>
            //println(path)
            configpath match {
                case Some(configpath) if configpath=="/FirstZnode" => get {
                  respondWithMediaType(`application/json`) {
                    //从zk获取数据
                    val data:String = new get_zk_data().getdata(configpath)
                    complete {
                      "{result: data="+data+"}"
                    }
                  }
                }
                case _ => get {
                  respondWithMediaType(`application/json`) {
                    complete {
                      "{result: 'data=null'}"
                    }
                  }
                }
            }
      }
  }
}