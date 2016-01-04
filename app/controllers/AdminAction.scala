package controllers

import play.api.mvc._

trait AdminSecure {
  import play.api.mvc.Results._

  private[this] val accounts = Map("admin" -> "password")

  protected val basicRealm = "(my realm)"

  /** ユーザー名とパスワードが一致するかを返します */
  protected def checkAccount(userName: String, password: String) =
    accounts.get(userName).filter(_ == password).nonEmpty


  /**
    * 認証を確認して実行するアクション
    */
  object AdminAction {
    private val AUTHORIZATION = "authorization"
    private val WWW_AUTHENTICATE = "WWW-Authenticate"

    private def realm = "Basic realm=\"%s\"".format(basicRealm)

    def apply(f: AuthenticatedRequest[AnyContent] => Result): Action[AnyContent] =
      apply(BodyParsers.parse.anyContent)(f)

    def apply[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result) = Action(p) { request =>
      request.headers.get(AUTHORIZATION) match {
        case Some(BasicAuthentication(name, pw)) if checkAccount(name, pw) =>
          f(new AuthenticatedRequest(name, request))
        case _ => Unauthorized("need admin login").withHeaders(WWW_AUTHENTICATE -> realm)
      }
    }

    /** 認証されて実行するリクエスト */
    class AuthenticatedRequest[A] private[controllers]
    (val userName: String, request: Request[A]) extends WrappedRequest(request)
  }
}