package controllers

import javax.inject.Inject

import play.api.cache._

import play.api.mvc.{ Action, Controller }

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection

//@Inject() (cache: CacheApi)

class Application @Inject() (cached: Cached) extends Controller {

  //}

  //object Application extends Controller {
  def index = Action {

    /*val myString:String = Cache.getOrElse[String]("mykey") {
      SomeOtherClass.getNewString()
    }*/

    Ok(views.html.index("Hello Play Framework"))
  }
}