package com.mwronski.users.model

import com.mwronski.users.model.User.{UserLogin, UserName}

object User {

  type UserLogin = String

  type UserName = String

}

final case class User(login: UserLogin, name: UserName)