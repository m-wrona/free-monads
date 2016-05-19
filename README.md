# free-monads

Sample usage of free monad based on user service registration.

# What is free monad?

Description of free-monad that can be found in Haskell:

```
A free monad generated by a functor is a special case of 

the more general free (algebraic) structure over some underlying structure.
```

***Free monads allow basically to build AST for your business logic and interpret it later.***

That means that you can decide later what kind of services will be really called for some business operations and steps within it.

For instance you can switch usage of sync services to async ones just by changing your AST business tree interpreter.


# User service demo - structure

Demo built for user service compose of following packages:

* model: domain model
 
* services: operations that can be done for users (save, get etc.)

* context: interpreters of business logic

In the main package you can find `UserUseCases` class which contains business logic.

UserDemo class runs sample registration using different interpreters. 

# Interpreters

For demo purposes two interpreters have been implemented.

## UserServiceBasicInterpreter

Basic service interpreter making direct/sync calls to underlying services.

```scala
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
```

Output:

```
Registering user using UserServiceBasicInterpreter
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona
UserService->get: login: m-wrona
UserServiceBasicInterpreter->GetUserService: login: m-wrona, users count: 0
UserService->get: login: m-wrona, found: null
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona, found user with given login: null
UserService->save: user: User(m-wrona,Mike Wrona)
UserServiceBasicInterpreter->SaveUserService: login: m-wrona, name: Mike Wrona
UserService->save: user: User(m-wrona,Mike Wrona), result: true
====> User registered: true

Trying to register the same user for the 2nd time
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona
UserService->get: login: m-wrona
UserServiceBasicInterpreter->GetUserService: login: m-wrona, users count: 1
UserService->get: login: m-wrona, found: User(m-wrona,Mike Wrona)
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona, found user with given login: User(m-wrona,Mike Wrona)
====> The same user registered twice: false
```


## UserServiceFutureInterpreter

Future service interpreter making async calls to underlying services.

```scala
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
```

Output:

```
Registering user using UserServiceFutureInterpreter
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona
UserService->get: login: m-wrona
UserServiceFutureInterpreter->GetUserService: login: m-wrona, users count: 0
UserService->get: login: m-wrona, found: null
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona, found user with given login: null
UserService->save: user: User(m-wrona,Mike Wrona)
UserServiceFutureInterpreter->SaveUserService: login: m-wrona, name: Mike Wrona
UserService->save: user: User(m-wrona,Mike Wrona), result: true
====> User registered: true

Trying to register the same user for the 2nd time
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona
UserService->get: login: m-wrona
UserServiceFutureInterpreter->GetUserService: login: m-wrona, users count: 1
UserService->get: login: m-wrona, found: User(m-wrona,Mike Wrona)
UserUseCases->UserRegistration - login: m-wrona, name: Mike Wrona, found user with given login: User(m-wrona,Mike Wrona)
====> The same user registered twice: false
```

# Articles

* [Free monads in cats](https://github.com/typelevel/cats/blob/master/docs/src/main/tut/freemonad.md)

* [Free monads are simple](http://underscore.io/blog/posts/2015/04/14/free-monads-are-simple.html)

* [Haskell - Free structure](https://wiki.haskell.org/Free_structure)

* [Why free monads matter](http://www.haskellforall.com/2012/06/you-could-have-invented-free-monads.html)

* [Functors, Applicative Functors and Monoids](http://learnyouahaskell.com/functors-applicative-functors-and-monoids)