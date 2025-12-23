@file:Suppress("unused")

package com.mobile.chessapp.backend.game.boardUtils

interface Matrix2D<T> : Iterable<T> {
  val rows: Int
  val cols: Int

  operator fun get(idx: Int): T
  operator fun get(row: Int, col: Int): T

  fun getOrNull(idx: Int): T?
  fun getOrNull(row: Int, col: Int): T?

  fun rows(): List<List<T>>
  fun cols(): List<List<T>>

  fun isEmpty(): Boolean
  fun prettyString(): String

  fun isSquare(): Boolean = rows == cols

  val size get() = rows * cols
  val indices get() = 0..<size

  val colIndices get() = 0..<cols
  val rowIndices get() = 0..<rows
}

interface MutableMatrix2D<T> : Matrix2D<T> {
  operator fun set(row: Int, col: Int, value: T): T
  fun addRow(row: Iterable<T>)
  fun addCol(col: Iterable<T>)
}

@ConsistentCopyVisibility
data class Matrix2DImpl<T> private constructor(
  override var rows: Int,
  override var cols: Int,
  private var entries: MutableList<T>,
) : MutableMatrix2D<T> {

  init {
    check(entries.size == rows * cols) { "Entries size must match matrix size" }
  }

  override fun get(idx: Int): T {
    return entries[idx]
  }

  override fun get(row: Int, col: Int): T {
    return entries[row * cols + col]
  }

  override fun getOrNull(idx: Int): T? {
    return entries.getOrNull(idx)
  }

  override fun getOrNull(row: Int, col: Int): T? {
    return entries.getOrNull(row * cols + col)
  }

  override fun set(row: Int, col: Int, value: T): T {
    entries[row * cols + col] = value
    return value
  }

  override fun addRow(row: Iterable<T>) {
    val newRowSize = row.count()
    check(newRowSize == cols || isEmpty()) {
      "Rows must be equal size! Expected: $cols, actual: $newRowSize"
    }
    entries += row
    rows += 1
    if (isEmpty()) cols = newRowSize
  }

  override fun addCol(col: Iterable<T>) {
    val newColSize = col.count()
    check(newColSize == rows || isEmpty()) {
      "Cols must be equal size! Expected: $rows, actual: $newColSize"
    }
    val res = mutableListOf<T>()
    col.reversed().forEachIndexed { row, entry ->
      for (col in colIndices) res += this[row, col]
      res += entry
    }
    entries = res
    cols += 1
    if (isEmpty()) rows = newColSize
  }

  override fun iterator(): Iterator<T> {
    return entries.iterator()
  }

  override fun rows(): List<List<T>> {
    return this.chunked(cols)
  }

  override fun cols(): List<List<T>> {
    val rows = this.rows()
    return colIndices.map { col -> rows.map { it[col] } }
  }

  override fun isEmpty(): Boolean {
    return entries.isEmpty()
  }

  override fun prettyString(): String {
    val strMatrix = map { it.toString() }.toMutableMatrix2D(rows, cols)
    val widthPerCol = strMatrix.cols().map { it.maxOf(String::length) }
    return strMatrix
      .mapIndexed { _, col, entry -> entry.padStart(widthPerCol[col]) }
      .rows()
      .joinToString("\n") { "|${it.joinToString(", ")}|" }
  }

  override fun toString(): String {
    return "Matrix2D(rows=$rows, cols=$cols, entries=$entries)"
  }

  companion object {
    fun <T> fromMutableList(rows: Int, cols: Int, list: MutableList<T>) =
      Matrix2DImpl(rows, cols, list)

    fun <T> fromIterable(rows: Int, cols: Int, iterable: Iterable<T>) =
      Matrix2DImpl(rows, cols, iterable.toMutableList())
  }
}

fun <T> emptyMatrix2D(): Matrix2D<T> = emptyMatrix2D()
fun <T> emptyMutableMatrix2D(): MutableMatrix2D<T> =
  Matrix2DImpl.fromMutableList(0, 0, mutableListOf())

inline fun <T> Matrix2D(size: Int, init: (idx: Int) -> T): Matrix2D<T> =
  MutableMatrix2D(size, size, init)

inline fun <T> MutableMatrix2D(size: Int, init: (idx: Int) -> T): MutableMatrix2D<T> =
  MutableMatrix2D(size, size, init)

inline fun <T> Matrix2D(rows: Int, cols: Int, init: (idx: Int) -> T): Matrix2D<T> =
  MutableMatrix2D(rows, cols, init)

inline fun <T> MutableMatrix2D(rows: Int, cols: Int, init: (idx: Int) -> T): MutableMatrix2D<T> {
  val res = mutableListOf<T>()
  for (idx in 0..<(rows * cols)) {
    res += init(idx)
  }
  return Matrix2DImpl.fromIterable(rows, cols, res)
}

inline fun <T> Matrix2D(size: Int, init: (row: Int, col: Int) -> T): Matrix2D<T> =
  MutableMatrix2D(size, size, init)

inline fun <T> MutableMatrix2D(size: Int, init: (row: Int, col: Int) -> T): MutableMatrix2D<T> =
  MutableMatrix2D(size, size, init)

inline fun <T> Matrix2D(rows: Int, cols: Int, init: (row: Int, col: Int) -> T): Matrix2D<T> =
  MutableMatrix2D(rows, cols, init)

inline fun <T> MutableMatrix2D(
  rows: Int,
  cols: Int,
  init: (row: Int, col: Int) -> T,
): MutableMatrix2D<T> {
  val res = mutableListOf<T>()
  for (row in 0..<rows) for (col in 0..<cols) {
    res += init(row, col)
  }
  return Matrix2DImpl.fromIterable(rows, cols, res)
}

inline fun <T> buildMatrix2D(
  size: Int,
  builderAction: MutableMatrix2D<T?>.() -> Unit,
): Matrix2D<T?> =
  buildMatrix2D(size, size, builderAction)

inline fun <T> buildMatrix2D(
  rows: Int,
  cols: Int,
  builderAction: MutableMatrix2D<T?>.() -> Unit,
): Matrix2D<T?> =
  MutableList<T?>(rows * cols) { null }
    .toMutableMatrix2D(rows, cols)
    .apply { builderAction() }

inline fun <T> buildMatrix2D(builderAction: MutableMatrix2D<T>.() -> Unit): Matrix2D<T> =
  mutableListOf<T>().toMutableMatrix2D(0).apply { builderAction() }

fun <T> Matrix2D<T>.toMutableMatrix(): MutableMatrix2D<T> =
  Matrix2DImpl.fromIterable(rows, cols, this)

fun <T> MutableMatrix2D<T>.toMatrix(): Matrix2D<T> = Matrix2DImpl.fromIterable(rows, cols, this)

fun <T> Iterable<T>.toMatrix2D(size: Int): Matrix2D<T> = this.toMutableMatrix2D(size, size)
fun <T> Iterable<T>.toMatrix2D(rows: Int, cols: Int): Matrix2D<T> =
  this.toMutableMatrix2D(rows, cols)

fun <T> Iterable<T>.toMutableMatrix2D(size: Int): MutableMatrix2D<T> =
  this.toMutableMatrix2D(size, size)

fun <T> Iterable<T>.toMutableMatrix2D(rows: Int, cols: Int): MutableMatrix2D<T> =
  Matrix2DImpl.fromIterable(rows, cols, this)

fun <T> Iterable<Iterable<T>>.toMatrix2D(): Matrix2D<T> = this.toMutableMatrix2D()
fun <T> Iterable<Iterable<T>>.toMutableMatrix2D(): MutableMatrix2D<T> {
  val rows = count()
  val cols = firstOrNull()?.count() ?: 0
  return Matrix2DImpl.fromIterable(rows, cols, this.flatten())
}

fun <T> Matrix2D<T>.copy(): Matrix2D<T> = this.toMutableMatrix2D(rows, cols)
fun <T> MutableMatrix2D<T>.copy(): MutableMatrix2D<T> = this.toMutableMatrix2D(rows, cols)

inline fun <T> Matrix2D<T>.forEachIndexed(action: (row: Int, col: Int, value: T) -> Unit) {
  var idx = 0
  for (row in 0..<rows) for (col in 0..<cols) {
    action(row, col, this[idx++])
  }
}

inline fun <T> Matrix2D<T>.onEachIndexed(action: (row: Int, col: Int, value: T) -> Unit) {
  forEachIndexed(action)
}

inline fun <T> Matrix2D<T>.filterIndexed(predicate: (row: Int, col: Int, value: T) -> Boolean): List<T> =
  buildList {
    for (row in 0..<rows) for (col in 0..<cols) {
      if (predicate(row, col, this[row])) {
        add(this@filterIndexed[row, col])
      }
    }
  }

inline fun <T, R> Matrix2D<T>.mapIndexed(transform: (row: Int, col: Int, value: T) -> R): Matrix2D<R> {
  val res = mutableListOf<R>()
  for (row in rowIndices) for (col in colIndices) {
    res += transform(row, col, this[row, col])
  }
  return Matrix2DImpl.fromMutableList(rows, cols, res)
}

fun <T> Matrix2D<T>.transpose(): Matrix2D<T> {
  val res = mutableListOf<T>()
  for (col in colIndices) for (row in rowIndices) {
    res += this[row, col]
  }
  return Matrix2DImpl.fromMutableList(cols, rows, res)
}

fun <T> Matrix2D<T>.flipHorizontal(): Matrix2D<T> {
  val res = mutableListOf<T>()
  for (row in rowIndices) for (col in colIndices) {
    res += this[row, cols - col - 1]
  }
  return Matrix2DImpl.fromMutableList(rows, cols, res)
}

fun <T> Matrix2D<T>.flipVertical(): Matrix2D<T> {
  val res = mutableListOf<T>()
  for (row in rowIndices) for (col in colIndices) {
    res += this[rows - row - 1, col]
  }
  return Matrix2DImpl.fromMutableList(rows, cols, res)
}

fun <T> Matrix2D<T>.rotate90(): Matrix2D<T> {
  val res = mutableListOf<T>()
  for (col in colIndices) for (row in rowIndices) {
    res += this[rows - row - 1, col]
  }
  return Matrix2DImpl.fromMutableList(cols, rows, res)
}

fun <T> Matrix2D<T>.rotate180(): Matrix2D<T> {
  val res = mutableListOf<T>()
  for (row in rowIndices) for (col in colIndices) {
    res += this[rows - row - 1, cols - col - 1]
  }
  return Matrix2DImpl.fromMutableList(rows, cols, res)
}

fun <T> Matrix2D<T>.rotate270(): Matrix2D<T> {
  val res = mutableListOf<T>()
  for (col in colIndices) for (row in rowIndices) {
    res += this[row, cols - col - 1]
  }
  return Matrix2DImpl.fromMutableList(cols, rows, res)
}

fun main() {
  val l = mutableListOf("1", "2", "3", "4", "5", "6")
  val m = mutableListOf("1", "2", "3", "4", "5", "6").toMutableMatrix2D(2, 3)
  println(m.toString())
}