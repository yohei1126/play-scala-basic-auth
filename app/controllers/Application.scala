package controllers

import play.api.libs.ws.WSAuthScheme.BASIC
import play.api.libs.ws.WS
import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Application extends Controller with AdminSecure {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def authSuccess = Action.async {
    import play.api.Play.current
    WS.url("http://localhost:9000/adminHome").withAuth("admin", "password", BASIC).get().map(
      response => Ok(response.body)
    )
  }

  def authFail = Action.async {
    import play.api.Play.current
    WS.url("http://localhost:9000/adminHome").withAuth("admin", "passord", BASIC).get().map(
      response => Ok(response.body)
    )
  }

  def adminHome = AdminAction { implicit request =>
    Ok("Welcome back, " + request.userName)
  }
}
