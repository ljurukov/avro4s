//package com.sksamuel.avro4s.schema
//
//import com.sksamuel.avro4s.{AvroNamespace, AvroSchema, Encoder, SchemaFor, Decoder}
//import org.scalatest.{Matchers, WordSpec}
//
//class AvroNamespaceTest extends WordSpec with Matchers {
//
//  "@AvroNamespace" should {
//    "support namespace annotations on records" in {
//
//      @AvroNamespace("com.yuval") case class AnnotatedNamespace(s: String)
//
//      val schema = AvroSchema[AnnotatedNamespace]
//      schema.getNamespace shouldBe "com.yuval"
//    }
//
//    "support namespace annotations in nested records" in {
//
//      @AvroNamespace("com.yuval") case class AnnotatedNamespace(s: String, internal: InternalAnnotated)
//      @AvroNamespace("com.yuval.internal") case class InternalAnnotated(i: Int)
//
//      val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/namespace.avsc"))
//      val schema = AvroSchema[AnnotatedNamespace]
//      schema.toString(true) shouldBe expected.toString(true)
//    }
//
//    "support namespace annotations on field" in {
//
//      case class InternalAnnotated(i: Int)
//      @AvroNamespace("com.yuval") case class AnnotatedNamespace(s: String, @AvroNamespace("com.yuval.internal") internal: InternalAnnotated)
//
//      val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/namespace.avsc"))
//      val schema = AvroSchema[AnnotatedNamespace]
//      schema.toString(true) shouldBe expected.toString(true)
//    }
//
//    "favour namespace annotations on field over record" in {
//
//      @AvroNamespace("ignore")
//      case class InternalAnnotated(i: Int)
//
//      @AvroNamespace("com.yuval") case class AnnotatedNamespace(s: String, @AvroNamespace("com.yuval.internal") internal: InternalAnnotated)
//
//      val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/namespace.avsc"))
//      val schema = AvroSchema[AnnotatedNamespace]
//      schema.toString(true) shouldBe expected.toString(true)
//    }
//
//    "support namespace annotations on ADTs at field level" in {
//      val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/namespace_enum.json"))
//      val schema = AvroSchema[Teapot]
//      schema.toString(true) shouldBe expected.toString(true)
//    }
//
//    "support namespace annotations on ADTs at type level" in {
//      val expected = new org.apache.avro.Schema.Parser().parse(getClass.getResourceAsStream("/namespace_enum2.json"))
//      val schema = AvroSchema[Location]
//      schema.toString(true) shouldBe expected.toString(true)
//    }
//
//    "support encoding and decoding with empty namespaces" in {
//      val spaceship = Spaceship(MiserableCosmos(true))
//      val encoded = Encoder[Spaceship].encode(spaceship, SchemaFor[Spaceship].schema)
//      val decoded = Decoder[Spaceship].decode(encoded, SchemaFor[Spaceship].schema)
//      spaceship shouldBe decoded
//    }
//  }
//}
//
//sealed trait Tea
//case object EarlGrey extends Tea
//case object Assam extends Tea
//case object EnglishBreakfast extends Tea
//
//@AvroNamespace("wibble")
//case class Teapot(@AvroNamespace("wobble") tea: Tea)
//
//@AvroNamespace("wibble")
//case class Location(africa: Africa)
//
//@AvroNamespace("wobble")
//sealed trait Africa
//case object Cameroon extends Africa
//case object Comoros extends Africa
//case object Chad extends Africa
//
//@AvroNamespace("")
//case class Spaceship(cosmos: Cosmos)
//@AvroNamespace("")
//sealed trait Cosmos
//@AvroNamespace("")
//case class FunCosmos(amountOfFun: Float) extends Cosmos
//@AvroNamespace("")
//case class MiserableCosmos(isTrulyAwful: Boolean) extends Cosmos
