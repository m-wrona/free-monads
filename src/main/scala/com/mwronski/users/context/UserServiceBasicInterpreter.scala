package com.mwronski.users.context

import cats.{Id, ~>}
import com.mwronski.users.model.User
import com.mwronski.users.model.User.{UserLogin, UserName}
import com.mwronski.users.services.UserOperation
import com.mwronski.users.services.UserOperation._

import scala.collection.mutable

/**
  * Basic service interpreter making direct/sync calls to underlying services.
  */
object UserServiceBasicInterpreter extends (UserOperation ~> Id) {

  private val users = mutable.HashMap[UserLogin, User]()

  override def apply[A](service: UserOperation[A]): Id[A] = service match {

    case SaveUserOperation(login: UserLogin, name: UserName) => {
      println(s"UserServiceBasicInterpreter->SaveUserService: login: $login, name: $name")
      users.put(login, new User(login, name))
      true.asInstanceOf[A]
    }


    case GetUserOperation(login: UserLogin) => {
      println(s"UserServiceBasicInterpreter->GetUserService: login: $login, users count: ${users.size}")
      users
        .get(login)
        .orNull
        .asInstanceOf[A]
    }

  }

}
