package controllers

import play.api.mvc._
import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.json.Json
import play.api.Logger

case class SampleObject(id: Long, name:String)

object Application extends Controller {


  implicit val faceBloggerFormat = Json.format[SampleObject]

  def index = Action {

    val key = "hoge-key"

    Cache.set(key, "hoge-value")
    val res = Cache.getOrElse(key)("no")

    Logger.info(s"result = $res")

    Cache.set("object", Json.toJson(SampleObject.apply(0, "hoge")).toString())

    import org.sedis._
    import redis.clients.jedis._
    val pool = new Pool(new JedisPool(new JedisPoolConfig(), "localhost", 6379, 2000))

    pool.withJedisClient { client =>


      Dress.up(client).set("hoge", Json.toJson(SampleObject.apply(0, "hoge")).toString())
      val r = Dress.up(client).get("hoge").getOrElse("")

    Logger.info(s"result = $r")


    // TODO あとでやる
//      Dress.up(client).get("single").get.must(be("foo"))
//      val r: List[String] = Dress.up(client).lrange("test",0,2)·
//      r.size.must(be(2))
//      r.toString.must(be("List(bar, foo)"))
//      val s: List[String] = Dress.up(client).sort("test")
//      s.size.must(be(2))
//      s.toString.must(be("List(bar, foo)"))
      Ok(r)

    }

  }

}