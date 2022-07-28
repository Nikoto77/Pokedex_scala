package com.pokemon.db

import monix.eval.Task
import scala.collection.mutable.LinkedHashMap

class PokemonDB(pokemons: List[Pokemon]) {

  def getPokemon(name: String): Option[Pokemon] =
    pokemons.find(pokemon => pokemon.name == name)

  def getAllPokemonsByType(pokemonType: String): List[Pokemon] =
    pokemons.filter(pokemon => pokemon.type1 == pokemonType)

  def countByType(pokemonType: String): Int =
    pokemons.count(pokemon => pokemon.type1 == pokemonType)
  
  def matchByKeyword(keyword: String): List[Pokemon] =
    pokemons.filter(pokemon => pokemon.name.contains(keyword))

}

object PokemonDB {
  
  private def readCSV(csvFile: String): Task[List[List[String]]] =
    Task(
      scala.io.Source.fromFile(csvFile)
        .getLines()
        .drop(1)
        .map(line => line.split(",").toList)
        .toList
    )

  private def parsePokemons(lines: List[List[String]]): List[Pokemon] =
    lines.map(parsePokemon)

  private def parsePokemon(attributes: List[String]): Pokemon =
    Pokemon(
      id = attributes(0).toInt,
      name = attributes(1),
      type1 = attributes(2),
      type2 = attributes(3),
      total = attributes(4).toInt,
      hp = attributes(5).toInt,
      attack = attributes(6).toInt,
      defense = attributes(7).toInt,
      spAttack = attributes(8).toInt,
      spDefense = attributes(9).toInt,
      speed = attributes(10).toInt,
      generation = attributes(11).toInt,
      legendary = attributes(12).toBoolean
    )

  def importCSV(csvFile: String): Task[PokemonDB] =
    readCSV(csvFile)
      .map(parsePokemons)
      .map(pokemons => new PokemonDB(pokemons))

}