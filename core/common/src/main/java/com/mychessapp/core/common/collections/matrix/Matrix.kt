package com.mychessapp.core.common.collections.matrix

import android.icu.text.Transliterator.Position
import com.mychessapp.core.common.collections.matrix.MatrixImpl.Companion.fromLists

/**
 * Matrix - a List representation of a matrix,
 * get(a)(b) will return an element in 'a' row and 'b' column
 *
 */
interface Matrix<T> : Collection<T> {

  val rows: List<List<T>>
  val columns: List<List<T>>

  val rowsSize: Int
  val columnsSize: Int

  override fun iterator(): Iterator<T> = fieldsList().iterator()

  override fun isEmpty(): Boolean = rows.isEmpty()

  override fun containsAll(elements: Collection<T>) =
    fieldsList().containsAll(elements)

  override fun contains(element: T): Boolean =
    fieldsList().contains(element)

  operator fun get(row: Int, column: Int): T = rows[row][column]

  fun copy(): Matrix<T>
  fun toMutableMatrix(): MutableMatrix<T>

  fun transposed(): Matrix<T>
  fun fieldsList(): List<T>
}

interface MutableMatrix<T> : Matrix<T> {

  fun toMatrix(): Matrix<T>

  operator fun set(row: Int, column: Int, element: T)

  operator fun set(position: Pair<Int, Int>, element: T) {
    set(position.first, position.second, element)
  }

  override fun copy(): MutableMatrix<T>

  override fun transposed(): Matrix<T>
}

inline fun <T> Matrix(
  size: Int,
  initializer: (row: Int, column: Int) -> T,
): Matrix<T> = MutableMatrix(size, initializer)

inline fun <T> Matrix(
  rowsSize: Int,
  columnsSize: Int,
  initializer: (row: Int, column: Int) -> T,
): Matrix<T> = MutableMatrix(rowsSize, columnsSize, initializer)

inline fun <T> MutableMatrix(
  size: Int,
  initializer: (row: Int, column: Int) -> T,
): MutableMatrix<T> = MatrixImpl(
  size = size,
  initializer = initializer
)

inline fun <T> MutableMatrix(
  rowsSize: Int,
  columnsSize: Int,
  initializer: (row: Int, column: Int) -> T,
): MutableMatrix<T> = MatrixImpl(
  rowsSize = rowsSize,
  columnsSize = columnsSize,
  initializer = initializer
)

inline fun <T> Matrix<T>.forEachRow(action: (List<T>) -> Unit) =
  rows.forEach(action)

inline fun <T> Matrix<T>.forEachRowIndexed(
  action: (index: Int, columns: List<T>) -> Unit
) = rows.forEachIndexed(action)

inline fun <T> Matrix<T>.forEachField(action: (T) -> Unit) =
  rows.forEach { it.forEach(action) }

inline fun <T> Matrix<T>.forEachFieldIndexed(
  action: (row: Int, column: Int, field: T) -> Unit,
) = rows.forEachIndexed { row, columns ->
  columns.forEachIndexed { column, field ->
    action(row, column, field)
  }
}

inline fun <T, R> Matrix<T>.mapFields(action: (field: T) -> R): Matrix<R> =
  rows.map { it.map(action) }.toMatrix()

inline fun <T, R> Matrix<T>.mapFieldsIndexed(
  action: (row: Int, column: Int, field: T) -> R,
) = rows.mapIndexed { row, columns ->
  columns.mapIndexed { column, field ->
    action(row, column, field)
  }
}.toMatrix()

fun <T> List<List<T>>.toMatrix(): Matrix<T> = toMutableMatrix()

fun <T> List<List<T>>.toMutableMatrix(): MutableMatrix<T> = fromLists(this)

fun main() {
  val matrix = Matrix(3, 3) { row, column -> row to column }
  println(matrix.toString())
}