package com.pokemon.db

import scala.collection.immutable.HashMap
import scala.util.control.NonFatal
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import scala.concurrent.duration.Duration

object PokemonCLI:

  def readUserInput(): Task[List[String]] =
    Console.readline().map(_.split(" ").toList)

  def execCmd(pokemonDB: PokemonDB, cmd: List[String]): Task[Unit] =
    cmd match {
      case List("GET", pokemonName) =>
        pokemonDB.getPokemon(pokemonName) match {
          case Some(pokemon) => Console.writeLine(pokemon)
          case None => Console.writeLine(s"Pokemon $pokemonName cannot be found in DB")
        }
      case List("GETALL", pokemonType) =>
        Console.writeList(pokemonDB.getAllPokemonsByType(pokemonType))
      case List("COUNT", pokemonType) =>
        Console.writeLine(pokemonDB.countByType(pokemonType))
      case List("MATCH", keyword) =>
        Console.writeList(pokemonDB.matchByKeyword(keyword))
      case _ => Console.writeLine("command not found")
    }

  def runCLI(pokemonDB: PokemonDB): Task[Unit] =
    val prg = for {
      _ <- Console.write("PokeCLI>")
      cmd <- readUserInput()
      _ <- execCmd(pokemonDB, cmd)
    } yield ()

    prg
      .loopForever
      .onErrorHandleWith {
        case ex: InterruptedException => 
          Console.writeLine("Exiting CLI...")
      }
      .onErrorRestartIf {
        case _: InterruptedException => false
        case NonFatal(ex) => true
      }

  def main(args: Array[String]): Unit =
    val prg = for {
      _ <- Console.writeLine("Parsing CSV...")
      pokemonDB <- PokemonDB.importCSV("src/main/resources/pokemon.csv")
      _ <- Console.writeLine("CSV imported")
      _ <- runCLI(pokemonDB)
    } yield ()

    prg.runSyncUnsafe(timeout = Duration.Inf)