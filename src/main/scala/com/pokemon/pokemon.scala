package com.pokemon.db

import scala.collection.immutable.HashMap

case class Pokemon(
  id: Int,
  name: String,
  type1: String,
  type2: String,
  total: Int,
  hp: Int,
  attack: Int,
  defense: Int,
  spAttack: Int,
  spDefense: Int,
  speed: Int,
  generation: Int,
  legendary: Boolean
)
