package com.mwronski.users.context

import cats.{Id, ~>}
import com.mwronski.users.model.User
import com.mwronski.users.model.User.{UserLogin, UserName}
import com.mwronski.users.services.UserService
import com.mwronski.users.services.UserService._

import scala.collection.mutable

object UserServiceInterpreter extends (UserService ~> Id) {

  private val users = mutable.HashMap[UserLogin, User]()

  override def apply[A](fa: UserService[A]): Id[A] = fa match {

    case SaveUserService(login: UserLogin, name: UserName) => {
      println(s"UserServiceInterpreter->SaveUserService: login: $login, name: $name")
      users.put(login, new User(login, name))
      true.asInstanceOf[A]
    }


    case GetUserService(user: UserLogin) => {
      println(s"UserServiceInterpreter->GetUserService: login: $user, users count: ${users.size}")
      users
        .get(user)
        .orNull
        .asInstanceOf[A]
    }

  }

}
