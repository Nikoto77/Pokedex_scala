
package com.pokemon

import okhttp3.{OkHttpClient, Request}

import scala.util.control.NonFatal

object Pokedex {

object Console {
  def readline(): Task[String] =
    Task(scala.io.StdIn.readLine())

  def write[A](obj: A): Task[Unit] =
    Task(print(obj.toString))

  def writeLine[A](obj: A): Task[Unit] =
    Task(println(obj.toString))

  def writeList[A](list: List[A]): Task[Unit] =
    Task.sequence(
      list.map(writeLine) // List[Task[Unit]]
      // List[Task[Unit]] => Task[List[Unit]] => Task[Unit]
    )
    .map(_ => ())
}

}