package com.mwronski.users.services

import cats.free.Free
import com.mwronski.users.model.User
import com.mwronski.users.model.User._

/**
  * Generic  user operation
 *
  * @tparam A type of result
  */
sealed trait UserOperation[A]

object UserOperation {

  final case class SaveUserOperation(user: UserLogin, name: UserName) extends UserOperation[Boolean]

  final case class GetUserOperation(user: UserLogin) extends UserOperation[User]

  type UserOperationResult[A] = Free[UserOperation, A]

  def save(user: User): UserOperationResult[Option[Boolean]] = {
    println(s"UserService->save: user: $user")
    if (!user.login.isEmpty) {
      Free.liftF(SaveUserOperation(user.login, user.name))
        .map(result => {
          println(s"UserService->save: user: $user, result: $result")
          Some(result)
        })
    } else {
      println(s"UserService->save: empty login - returning no results")
      Free.pure(None)
    }
  }

  def get(login: UserLogin): UserOperationResult[Option[User]] = {
    println(s"UserService->get: login: $login")
    if (!login.isEmpty) {
      Free.liftF(GetUserOperation(login))
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