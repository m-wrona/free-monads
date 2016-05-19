package com.mwronski.users.services

import com.mwronski.users.model.User
import com.mwronski.users.model.User._

import scalaz.Free

sealed trait UserService[A]

final case class SaveUserService(user: UserLogin, name: UserName) extends UserService[Boolean]

final case class GetUserService(user: UserLogin) extends UserService[User]

object UserService {

  def save(user: User): Free[UserService, Option[Boolean]] = {
    if (!user.login.isEmpty) {
      Free.liftF(SaveUserService(user.login, user.name)).map(Some(_))
    } else {
      Free.pure(None)
    }
  }

  def get(user: UserLogin): Free[UserService, Option[User]] = {
    if (!user.isEmpty) {
      Free.liftF(GetUserService(user)).map(Some(_))
    } else {
      Free.pure(None)
    }
  }

}