package com.mychessapp.core.common.scope_functions

inline fun <reified T: Any> Any.takeIfIsInstance(): T? = this as? T
