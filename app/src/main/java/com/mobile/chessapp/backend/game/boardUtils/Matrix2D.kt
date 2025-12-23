package com.mobile.chessapp.backend.game.boardUtils


interface Matrix2D<T> : Iterable<T> {
  val rows: Int
  val cols: Int
  val size: Int
  operator fun get(idx: Int): T
  operator fun get(row: Int, col: Int): T
  fun getOrNull(idx: Int): T?
  fun getOrNull(row: Int, col: Int): T?
}

interface MutableMatrix2D<T> : Matrix2D<T> {
  operator fun set(row: Int, col: Int, value: T): T
}

class Matrix2DImpl<T> private constructor(
  override val rows: Int,
  override val cols: Int,
  private val entries: MutableList<T>,
) : MutableMatrix2D<T> {

  override val size: Int = rows * cols

  constructor(rows: Int, cols: Int, entries: Iterable<T>) :
      this(rows, cols, entries.toMutableList())

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

  override fun iterator(): Iterator<T> {
    return entries.iterator()
  }

  override fun toString(): String {
    val strEntries = entries.map { it.toString() }
    val maxEntryLength = strEntries.maxOf { it.length }
    return strEntries
      .map { it.padEnd(maxEntryLength) }
      .chunked(cols)
      .joinToString("\n") { row ->
        "|" + row.joinToString(",") + "|"
      }
  }
}

inline fun <reified T> Matrix2D<T>.forEachIndexed(action: (Int, Int, T) -> Unit) {
  var idx = 0
  for (row in 0..<rows) for (col in 0..<cols) {
    action(row, col, this[idx++])
  }
}

inline fun <reified T> Matrix2D<T>.toMutableMatrix(): MutableMatrix2D<T> =
  Matrix2DImpl(rows, cols, this)

inline fun <reified T> Iterable<T>.toMutableMatrix2D(rows: Int, cols: Int): MutableMatrix2D<T> =
  Matrix2DImpl(rows, cols, this)

inline fun <reified T> Iterable<T>.toMatrix2D(rows: Int, cols: Int): Matrix2D<T> =
  Matrix2DImpl(rows, cols, this)


fun main() {
  val l = mutableListOf("1", "2", "3", "4", "5", "6")
  val k = l.set(0, "A")
  val m = Matrix2DImpl(2, 3, mutableListOf("1", "2", "3", "4", "5", "6"))
  m.forEachIndexed { a, b -> }
  m.forEachIndexed { a, b, c -> }
  m.map {  }
  m.map { }
  val arr = arrayOf("1", "2", "3")
  println(m.toString())
}