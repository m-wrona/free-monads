package com.mwronski.users

import cats._
import cats.free.Free
import com.mwronski.users.model.User
import com.mwronski.users.model.User.UserName
import com.mwronski.users.services.UserService

object UserUseCases {

  def register(login: UserName, name: UserName): Free[UserService, Boolean] = {
    println(s"UserRegistration - login: $login, name: $name")
    UserService.get(login).map(_ match {
      case Some(user) => {
        println(s"UserRegistration - login: $login, name: $name, found user with given login: $user")
        if (user != null) {
          false
        } else {
          UserService.save(new User(login, name))
          true
        }
      }

      case None => {
        println(s"UserRegistration - login: $login, name: $name : no user results returned")
        false
      }
    })
  }

}
