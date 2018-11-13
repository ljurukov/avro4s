package com.sksamuel.avro4s.schema

import com.sksamuel.avro4s.{AvroErasedName, AvroSchema, SchemaFor}
import org.apache.avro.SchemaParseException
import org.scalatest.{FunSuite, Matchers}

class GenericSchemaTest extends FunSuite with Matchers {

  case class Generic[T](t: T)
  case class SameGenericWithDifferentTypeArgs(gi: Generic[Int], gs: Generic[String])

  @AvroErasedName
  case class GenericDisabled[T](t: T)
  case class SameGenericWithDifferentTypeArgsDisabled(gi: GenericDisabled[Int], gs: GenericDisabled[String])

  test("support same generic with different type parameters") {
    val schema = AvroSchema[SameGenericWithDifferentTypeArgs]
    val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/generic.json"))
    schema.toString(true) shouldBe expected.toString(true)
  }

  test("throw error if different type parameters are disabled by @AvroErasedName") {
    intercept[SchemaParseException] {
      val schema = AvroSchema[SameGenericWithDifferentTypeArgsDisabled]
      val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/generic.json"))
      schema.toString(true) shouldBe expected.toString(true)
    }
  }

  test("support top level generic") {
    val schema1 = AvroSchema[Generic[String]]
    val expected1 = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/generic_string.json"))
    schema1.toString(true) shouldBe expected1.toString(true)

    val schema2 = AvroSchema[Generic[Int]]
    val expected2 = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/generic_int.json"))
    schema2.toString(true) shouldBe expected2.toString(true)
  }

  test("generate only raw name if @AvroErasedName is present") {
    val schema = AvroSchema[GenericDisabled[String]]
    val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/generic_disabled.json"))
    schema.toString(true) shouldBe expected.toString(true)
  }
}
