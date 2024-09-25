package com.mychessapp.core.common.collections.list

/**
 * Copy the list with updates on selected fields.
 *
 * You can update the field on index i by passing vararg: i to newVal
 * where newVal will be the value after the update
 *
 * The updates are applied in order so if there are a few updates to the same field
 * the last one will be the final one
 *
 * @param updates the vararg updates to be made to the copied list
 *
 * @throws IndexOutOfBoundsException for updates outside of list indices
 */
fun <T> List<T>.copy(
  vararg updates: Pair<Int, T>
): List<T> =
  toMutableList().apply {
    updates.forEach(::update)
  }

fun <T> List<T>.copy(
  updates: List<Pair<Int, T>>
): List<T> =
  toMutableList().apply {
    updates.forEach(::update)
  }

fun <T> MutableList<T>.update(update: Pair<Int, T>) =
  apply {
    update.let { (index, newValue) -> set(index, newValue) }
  }
