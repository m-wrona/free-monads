package com.mwronski.users

import com.mwronski.users.model.User
import com.mwronski.users.model.User.UserName
import com.mwronski.users.services.UserService

import scalaz.Free

object UserRegistration {

  def register(user: UserName, name: UserName): Free[UserService, Boolean] =
    UserService.get(user).map(_ match {
      case Some(user) => {
        println(s"User already exists: $user")
        false
      }

      case None => UserService.save(user)
    })
  

  def main(args: Array[String]) {
    println("Registering user")
    register("m-wrona", "Mike Wrona")
      .foldMap(UserServiceInterpreter)
      .foreach(result => println(s"User registered: $result"))
  }

}
