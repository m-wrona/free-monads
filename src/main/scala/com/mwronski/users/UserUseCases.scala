package com.mwronski.users

import cats.free.Free
import com.mwronski.users.model.User
import com.mwronski.users.model.User.UserName
import com.mwronski.users.services.UserOperation

/**
  * Business logic related with users
  */
object UserUseCases {

  def register(login: UserName, name: UserName): Free[UserOperation, Boolean] = {
    println(s"UserUseCases->UserRegistration - login: $login, name: $name")
    UserOperation.get(login)
      .flatMap(_ match {
        case Some(user) => {
          println(s"UserUseCases->UserRegistration - login: $login, name: $name, found user with given login: $user")
          if (user != null) {
            Free.pure(false)
          } else {
            UserOperation.save(new User(login, name))
              .map(_.getOrElse(false))
          }
        }

        case None => {
          println(s"UserUseCases->UserRegistration - login: $login, name: $name : no user results returned")
          Free.pure(false)
        }
      })
  }

}
