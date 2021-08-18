package dev.atedeg.ecscala.util.immutable

import dev.atedeg.ecscala.util.BaseMapTests

import scala.collection.{Map, MapFactory}

class IterableMapTests extends BaseMapTests[IterableMap] {

  override def name: String = "An immutable iterable map"

  override def mapFactory(): MapFactory[IterableMap] = IterableMap

  override def add[K, V](map: IterableMap[K, V], elem: (K, V)): IterableMap[K, V] = map + elem

  override def remove[K, V](map: IterableMap[K, V], key: K): IterableMap[K, V] = map - key
}
