/**
  * Created by enxie on 2017/10/18.
  */

import java.net.InetAddress

import akka.actor.ActorSystem
import spray.http.StatusCodes
import spray.routing.SimpleRoutingApp
object webservice extends App with SimpleRoutingApp {

  implicit val system = ActorSystem("pixel_actor")

  lazy val hostname = InetAddress.getLocalHost.getHostName

  val sites = List("0", "3", "77", "15", "71", "2", "101", "186")

  // web server
  //startServer(InetAddress.getLocalHost.getHostAddress, 8080) {
  startServer("localhost", 8080) {
    path("bk") {
      parameters('iid.?, 'tcat.?, 'ev.?, 'meta.?, 'cg.?, 'fm.?, 'crm.?, 'ldt.?, 's.?) { (iid, tcat, ev, meta, cg, fm, crm, ldt, s) =>
        requestContext =>
          (ev, s) match {
            case (Some(_ev), Some(_s)) if _ev.nonEmpty && _ev.toInt <= 6 && sites.contains(_s.toString) =>
              val uri = requestContext.request.uri.toRelative
              //println(s"redirect $uri")
              requestContext.redirect(s"www.baidu.com", StatusCodes.Found)
            case _ =>
              requestContext.redirect("www.google.com", StatusCodes.Found)
          }
      }
    }
  }

}
