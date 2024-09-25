package com.mychessapp.core.common.collections.matrix

import com.mychessapp.core.common.numbers.zero

class MatrixImpl<T>(
  override val rows: MutableList<MutableList<T>>
) : Matrix<T>, MutableMatrix<T>, Collection<T> {

  override val columns: List<List<T>>
    get() = transposedCache.rows

  override val rowsSize = rows.size
  override val columnsSize = rows.firstOrNull()?.size ?: Int.zero
  override val size = rowsSize * columnsSize

  private var fieldsListCache = rows.flatten().toMutableList()
  private var transposedCache = MutableMatrix(rowsSize, columnsSize) { row, column ->
    get(column, row)
  }

  init {
    if (isValidMatrix().not()) throw IllegalMatrixSizeException()
  }

  override operator fun set(row: Int, column: Int, element: T) {
    rows[row][column] = element.also { updateCache(row, column, element) }
  }

  private fun updateCache(row: Int, column: Int, element: T) {
    transposedCache[column, row] = element
    fieldsListCache[row * columnsSize + column] = element
  }

  private fun isValidMatrix() = isNotEmpty() && columnsAreSameSize()
  private fun columnsAreSameSize() = rows.any { it.size == columnsSize }

  override fun fieldsList(): List<T> = fieldsListCache
  override fun transpose(): Matrix<T> = transposedCache

  override fun copy(): MatrixImpl<T> = fromListOfLists(rows)
  override fun toMatrix(): Matrix<T> = copy()
  override fun toMutableMatrix(): MutableMatrix<T> = copy()

  override fun toString(): String = buildString {
    forEachRow { append(it.toString() + "\n") }
  }

  companion object {
    inline operator fun <T> invoke(
      rowsSize: Int,
      columnsSize: Int,
      initializer: (row: Int, column: Int) -> T
    ) = MutableList(rowsSize) { row ->
      MutableList(columnsSize) { column -> initializer(row, column) }
    }.let(::MatrixImpl)

    inline operator fun <T> invoke(
      size: Int,
      initializer: (row: Int, column: Int) -> T
    ) = invoke(size, size, initializer)

    fun <T> fromListOfLists(listOfLists: List<List<T>>) =
      listOfLists
        .map(List<T>::toMutableList)
        .toMutableList()
        .let(::MatrixImpl)
  }
}

class IllegalMatrixSizeException : IllegalStateException(
  "In Matrix all rows must have same number of columns"
)
