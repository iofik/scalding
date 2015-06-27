package com.twitter.scalding.serialization

import com.twitter.scalding._
import com.twitter.scalding.thrift.macros.impl.ScroogeInternalOrderedSerializationImpl

import scala.language.experimental.{ macros => smacros }

/**
 * RequiredBinaryComparators provide comparators (or Ordering in Scala) that are capable of comparing keys in their
 * serialized form reducing the amount of time spent in serialization/deserialization.  These comparators are implemented
 * using Scala macros, and currently provide binary comparators for primitives, strings, Options, tuples, collections, case classes
 * and Scrooge objects.
 */
trait RequiredBinaryComparators extends Job {

  implicit def ordSer[T]: OrderedSerialization[T] = macro ScroogeInternalOrderedSerializationImpl[T]

  override def config =
    super.config + (Config.ScaldingRequireOrderedSerialization -> "true")
}

object RequiredBinaryComparators {

  implicit def orderedSerialization[T]: OrderedSerialization[T] = macro ScroogeInternalOrderedSerializationImpl[T]
}

/**
 * Use this for an ExecutionApp.
 */
trait RequiredBinaryComparatorsExecutionApp[K] extends ExecutionApp {
  implicit def ordSer[T]: OrderedSerialization[T] = macro ScroogeInternalOrderedSerializationImpl[T]

  override def config(inputArgs: Array[String]): (Config, Mode) = {
    val (conf, m) = super.config(inputArgs)
    (conf.setRequireOrderedSerialization(true), m)
  }
}
