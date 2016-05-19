package com.mwronski.users

import com.mwronski.users.services.UserService
import com.mwronski.users.services.SaveUserService
import com.mwronski.users.services.GetUserService
import com.mwronski.users.model.User
import com.mwronski.users.model.User.{UserLogin, UserName}

import scala.collection.mutable
import scala.concurrent.Future
import scalaz.~>

object UserServiceInterpreter extends (UserService ~> Future) {

  private val users = mutable.HashMap[UserLogin, User]()

  override def apply[A](fa: UserService[A]): Future[_] = fa match {

    case SaveUserService(user: UserLogin, name: UserName) => Future {
      users
        .put(user, new User(user, name))
        .map(_ => false)
        .getOrElse(true)
    }

    case GetUserService(user: UserLogin) => Future {
      users.get(user)
    }

  }


}
