package com.sksamuel.avro4s

import scala.annotation.StaticAnnotation

case class AvroAlias(override val alias: String) extends AvroAliasable

trait AvroAliasable extends AvroFieldReflection {
  val alias: String
}

case class AvroDoc(override val doc: String) extends AvroDocumentable

trait AvroDocumentable extends AvroFieldReflection {
  val doc: String
}

/**
  * [[AvroFixed]] overrides the schema type for a field or a value class
  * so that the schema is set to org.apache.avro.Schema.Type.FIXED
  * rather than whatever the default would be.
  *
  * This annotation can be used in the following ways:
  *
  * - On a field, eg   class `Foo(@AvroField(10) name: String)`
  * which results in the field `name` having schema type FIXED with
  * a size of 10.
  *
  * - On a value type, eg `@AvroField(7)   class Foo(name: String) extends AnyVal`
  * which results in all usages of the value type having schema
  * FIXED with a size of 7 rather than the default.
  */
case class AvroFixed(override val size: Int) extends AvroFixable

trait AvroFixable extends AvroFieldReflection {
  val size: Int
}


/**
  * [[AvroName]] allows the name used by Avro to be different
  * from what is defined in code.
  *
  * For example, if a   class defines a field z, such as
  * `  class Foo(z: String)` then normally this will be
  * serialized as an entry 'z' in the Avro Record.
  *
  * However, if the field is annotated such as
  * `  class Foo(@AvroName("x") z: String)` then the entry
  * in the Avro Record will be for 'x'.
  *
  * Similarly for deserialization, if a field is annotated then
  * the name that is looked up in the avro record will be the
  * annotated name and not the field name in Scala.
  *
  * The second example is with classes. If a class is annotated
  * with @AvroName then the name used in the record schema
  * will not be the classname but the annotated value.
  *
  * This will also have an effect on serialization.
  * For example, when decoding records into an Either, the
  * decoder must decide if the value is a Left or a Right.
  * It usually does this by comparing the name in the record
  * to the classnames of the either types, but when annotated,
  * it will compare the name in the record to the annotated value.
  *
  */
case class AvroName(override val name: String) extends AvroNameable

sealed trait AvroNameable extends AvroFieldReflection {
  val name: String
}


case class AvroNamespace(override val namespace: String) extends AvroNamespaceable

sealed trait AvroNamespaceable extends AvroFieldReflection {
  val namespace: String
}

case class AvroProp(override val key: String, override val value:String) extends AvroProperty

case class AvroProp2(override val doc: String) extends AvroNameable with AvroDocumentable {
  override val name: String = "static"
}

trait AvroProperty extends AvroFieldReflection{
  val key: String
  val value: String
}


/**
  * This annotation is used to disable generics in the encoding
  * of a record's name.
  *
  * Normally, the record name for a generic type is the name of the
  * raw type, plus the actual type parameters. For example, a class Foo
  * with type parameters Int and Boolean, would have a generated name of
  * `Foo__Int_Boolean`
  *
  * When this annotation is present on a type, the name used in the
  * schema will simply be the raw type, eg `Foo`.
  */
case class AvroErasedName() extends AvroFieldReflection{

}


trait AvroFieldReflection extends StaticAnnotation {
  private def getClassFields(clazz: Class[_]): Map[String, AnyRef] = {
    val fields = clazz.getDeclaredFields.map(field => {
      field setAccessible true
      field.getName -> field.get(this)
    }).toMap
    if(clazz.getSuperclass != null){
      fields ++ getClassFields(clazz.getSuperclass)
    }
    fields
  }

  def getAllFields = getClassFields(this.getClass)
}
