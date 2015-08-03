package controllers

import play.api.mvc.{ Action, Controller }

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection

object Application extends Controller {
  def index = Action {
    Ok(views.html.index("Hello Play Framework"))
  }
}