package com.mwronski.users

import com.mwronski.users.context.{UserServiceBasicInterpreter, UserServiceFutureInterpreter}
import cats.std.all._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object UserDemo {

  def main(args: Array[String]) {
    registerDemoUsingBasicInterpreter()
    println("\n********************************************\n")
    registerDemoUsingFutureInterpreter()
  }

  private def registerDemoUsingFutureInterpreter() = {
    println(s"Registering user using UserServiceFutureInterpreter")
    val registered = Await.result(
      UserUseCases
        .register("m-wrona", "Mike Wrona")
        .foldMap(UserServiceFutureInterpreter),
      500.millis
    )
    println(s"====> User registered: $registered")
    println
    println("Trying to register the same user for the 2nd time")
    val registered2 = Await.result(
      UserUseCases
        .register("m-wrona", "Mike Wrona")
        .foldMap(UserServiceFutureInterpreter),
      500.millis
    )
    println(s"====> The same user registered twice: $registered2")
  }

  private def registerDemoUsingBasicInterpreter() = {
    println(s"Registering user using UserServiceBasicInterpreter")
    val registered = UserUseCases
      .register("m-wrona", "Mike Wrona")
      .foldMap(UserServiceBasicInterpreter)
    println(s"====> User registered: $registered")
    println
    println("Trying to register the same user for the 2nd time")
    val registered2 = UserUseCases
      .register("m-wrona", "Mike Wrona")
      .foldMap(UserServiceBasicInterpreter)
    println(s"====> The same user registered twice: $registered2")
  }

}
