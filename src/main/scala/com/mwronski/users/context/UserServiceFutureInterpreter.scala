package com.mwronski.users.context

import com.mwronski.users.model.User
import com.mwronski.users.model.User.{UserLogin, UserName}
import com.mwronski.users.services.UserOperation
import com.mwronski.users.services.UserOperation._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.concurrent.Future
import cats._

/**
  * Future service interpreter making async calls to underlying services.
  */
object UserServiceFutureInterpreter extends (UserOperation ~> Future) {

  private val users = mutable.HashMap[UserLogin, User]()

  override def apply[A](service: UserOperation[A]): Future[A] = service match {

    case SaveUserOperation(login: UserLogin, name: UserName) => Future {
      println(s"UserServiceFutureInterpreter->SaveUserService: login: $login, name: $name")
      users.put(login, new User(login, name))
      true.asInstanceOf[A]
    }


    case GetUserOperation(login: UserLogin) => Future {
      println(s"UserServiceFutureInterpreter->GetUserService: login: $login, users count: ${users.size}")
      users
        .get(login)
        .orNull
        .asInstanceOf[A]
    }

  }

}
