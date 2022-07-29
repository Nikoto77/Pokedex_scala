

import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods

import scala.io.Source

object PokeAPI {

  val API_BASE_URL = "https://pokeapi.co/api/v2/"

  def pokedexApiEndpoint(): String = s"${API_BASE_URL}/pokedex/1/"

  def abilityApiEndpointGhost(): String = s"${API_BASE_URL}/ability"
  def abilityApiEndpoint(limit: Int): String = s"${API_BASE_URL}/ability/?limit=${limit}&offset=0"

  def moveApiEndpointGhost(): String = s"${API_BASE_URL}/move"
  def moveApiEndpoint(limit: Int): String = s"${API_BASE_URL}/move/?limit=${limit}&offset=0"

  def typeApiEndpointGhost(): String = s"${API_BASE_URL}/type"
  def typeApiEndpoint(limit: Int): String = s"${API_BASE_URL}/type/?limit=${limit}&offset=0"

  def getPokedex(): List[Pokedex] = {
    implicit val formats = DefaultFormats
    val jsonResponse = Source.fromURL(pokedexApiEndpoint(), "utf-8").mkString
    val parsedJson = JsonMethods.parse(jsonResponse).map(v => v.camelizeKeys)
    val pokedex = (parsedJson \ "pokemon").extract[List[Pokedex]]
    pokedex
  }


  def getAbilities(): List[Ability] = {
    implicit val formats = DefaultFormats
    val ghost = Source.fromURL(abilityApiEndpointGhost(), "utf-8").mkString
    val totalCount = (JsonMethods.parse(ghost) \ "meta" \ "total_count").extract[Int]
    val jsonResponse = Source.fromURL(abilityApiEndpoint(totalCount), "utf-8").mkString
    val parsedJson = JsonMethods.parse(jsonResponse).map(v => v.camelizeKeys)
    val abilities = (parsedJson \ "objects").extract[List[Ability]]
    abilities
  }

  def getMoves(): List[Move] = {
    implicit val formats = DefaultFormats
    val ghost = Source.fromURL(moveApiEndpointGhost(), "utf-8").mkString
    val totalCount = (JsonMethods.parse(ghost) \ "meta" \ "total_count").extract[Int]
    val jsonResponse = Source.fromURL(moveApiEndpoint(totalCount), "utf-8").mkString
    val parsedJson = JsonMethods.parse(jsonResponse).map(v => v.camelizeKeys)
    val moves = (parsedJson \ "objects").extract[List[Move]]
    moves
  }

  def getTypes(): List[Type] = {
    implicit val formats = DefaultFormats
    val ghost = Source.fromURL(typeApiEndpointGhost(), "utf-8").mkString
    val totalCount = (JsonMethods.parse(ghost) \ "meta" \ "total_count").extract[Int]
    val jsonResponse = Source.fromURL(typeApiEndpoint(totalCount), "utf-8").mkString
    val parsedJson = JsonMethods.parse(jsonResponse).map(v => v.camelizeKeys)
    val types = (parsedJson \ "objects").extract[List[Type]]
    types
  }
}