package com.mwronski.users.services

import cats.free.Free
import com.mwronski.users.model.User
import com.mwronski.users.model.User._


sealed trait UserService[A]

object UserService {

  final case class SaveUserService(user: UserLogin, name: UserName) extends UserService[Boolean]

  final case class GetUserService(user: UserLogin) extends UserService[User]

  type UserServiceCall[A] = Free[UserService, A]

  def save(user: User): UserServiceCall[Option[Boolean]] = {
    println(s"UserService->save: user: $user")
    if (!user.login.isEmpty) {
      Free.liftF(SaveUserService(user.login, user.name))
        .map(result => {
          println(s"UserService->save: user: $user, result: $result")
          Some(result)
        })
    } else {
      println(s"UserService->save: empty login - returning no results")
      Free.pure(None)
    }
  }

  def get(login: UserLogin): UserServiceCall[Option[User]] = {
    println(s"UserService->get: login: $login")
    if (!login.isEmpty) {
      Free.liftF(GetUserService(login))
        .map(user => {
          println(s"UserService->get: login: $login, found: $user")
          Some(user)
        })
    } else {
      println(s"UserService->get: empty login - returning no results")
      Free.pure(None)
    }
  }

}