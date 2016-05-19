package com.mwronski.users

import cats.Comonad
import com.mwronski.users.context.UserServiceSimpleCompiler
import com.mwronski.users.services.UserService

object UserDemo {

  def main(args: Array[String]) {
    println("====> Registering user using: UserServiceInterpreter")
    demoRegister()
  }

  private def demoRegister(): Unit = {
    println("Registering user")
    val registered = UserUseCases
      .register("m-wrona", "Mike Wrona")
      .foldMap(UserServiceSimpleCompiler)
    println(s"====> User registered: $registered")
    println
    println("Trying to register the same user for the 2nd time")
    val registered2 = UserUseCases
      .register("m-wrona", "Mike Wrona")
      .foldMap(UserServiceSimpleCompiler)
    println(s"====> The same user registered twice: $registered2")
  }

}
