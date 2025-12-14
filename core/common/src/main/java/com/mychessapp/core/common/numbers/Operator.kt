package com.mychessapp.core.common.numbers

operator fun Number.minus(other: Number): Number {
  return when (this) {
    is Long -> this - other.toLong()
    is Int -> this - other.toInt()
    is Short -> this - other.toShort()
    is Byte -> this - other.toByte()
    is Double -> this - other.toDouble()
    is Float -> this - other.toFloat()
    else -> throw RuntimeException("Unknown numeric type")
  }
}

operator fun Number.plus(other: Number): Number {
  return when (this) {
    is Long -> this + other.toLong()
    is Int -> this + other.toInt()
    is Short -> this + other.toShort()
    is Byte -> this + other.toByte()
    is Double -> this + other.toDouble()
    is Float -> this + other.toFloat()
    else -> throw RuntimeException("Unknown numeric type")
  }
}

operator fun Number.times(other: Number): Number {
  return when (this) {
    is Long -> this * other.toLong()
    is Int -> this * other.toInt()
    is Short -> this * other.toShort()
    is Byte -> this * other.toByte()
    is Double -> this * other.toDouble()
    is Float -> this * other.toFloat()
    else -> throw RuntimeException("Unknown numeric type")
  }
}

operator fun Number.div(other: Number): Number {
  return when (this) {
    is Long -> this / other.toLong()
    is Int -> this / other.toInt()
    is Short -> this / other.toShort()
    is Byte -> this / other.toByte()
    is Double -> this / other.toDouble()
    is Float -> this / other.toFloat()
    else -> throw RuntimeException("Unknown numeric type")
  }
}

operator fun Number.rem(other: Number): Number {
  return when (this) {
    is Long -> this % other.toLong()
    is Int -> this % other.toInt()
    is Short -> this % other.toShort()
    is Byte -> this % other.toByte()
    is Double -> this % other.toDouble()
    is Float -> this % other.toFloat()
    else -> throw RuntimeException("Unknown numeric type")
  }
}
