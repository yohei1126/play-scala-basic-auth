package controllers

/**
  * Basic 認証の値の取り扱い
  */
object BasicAuthentication {
  private val basicPefix = "Basic "
  private val AUTHORIZATION_PARAMS = """([^:]+?):(.+)""".r

  /**
    * Base64 エンコードで文字列化された Basic 認証値を、ユーザー名とパスワードに分解して返します
    *
    * @param auth Base64 エンコード文字列
    * @return 名前とパスワード
    */
  private[this] def decode(auth: String): Option[(String, String)] = {
    val d = javax.xml.bind.DatatypeConverter.parseBase64Binary(auth)

    new String(d, "utf-8") match {
      case AUTHORIZATION_PARAMS(username, password) => Some(username, password)
      case _ => None
    }
  }

  /**
    * WWW-Authenticate ヘッダの値から、ユーザー名とパスワードを取り出します。
    *
    * @param authorization WWW-Authenticate ヘッダ値。ヘッダの名前は含まない。
    * @return 名前とパスワード
    */
  def unapply(authorization: String): Option[(String, String)] = authorization match {
    case s if s startsWith basicPefix => decode(s drop basicPefix.length)
    case _ => None
  }
}