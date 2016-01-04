package controllers

import play.api._
import play.api.mvc._

class Application extends Controller with AdminSecure {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def adminHome = AdminAction { implicit request =>
    Ok("Welcome back, " + request.userName)
  }
}
