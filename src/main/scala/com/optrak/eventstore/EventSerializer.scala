package com.optrak.eventstore

import akka.persistence.eventstore.EventStoreSerializer
import akka.persistence.eventstore.snapshot.EventStoreSnapshotStore.SnapshotEvent.Snapshot
import akka.serialization.Serializer
import eventstore.ContentType
import eventstore.util.DefaultFormats
import java.nio.charset.Charset
import java.nio.ByteBuffer
import muster._
import muster.ast.AstNode


import org.joda.time.DateTime


case class StackEvt (
    val item: String,
    val timestamp: DateTime,
    val remainingItems: List[String]
)

class JsonEventSerializer extends EventStoreSerializer {
  import muster.codec.json4s._
  import muster.codec.json4s.api._
  import org.json4s._
  import org.json4s.native.JsonMethods._
  import JsonEventSerializer._
  import com.optrak.eventstore.StackEvent

  def identifier = Identifier

  def includeManifest = true

  def fromBinary(bytes: Array[Byte], manifestOpt: Option[Class[_]]) = {
    implicit val manifest = manifestOpt match {
      case Some(x) => Manifest.classType[AnyRef](x)
      case None    => Manifest.AnyRef
    }

    Json4sCodec.as[StackEvt](parse(new String(bytes, UTF8)))
  }



  def toBinary(o: AnyRef) = o.asJValue.toString.getBytes(UTF8)

  def contentType = ContentType.Json
}

object JsonEventSerializer {
  val UTF8 = Charset.forName("UTF-8")
  val Identifier: Int = ByteBuffer.wrap("json4s".getBytes(UTF8)).getInt

  implicit def dateTimeToString(d: DateTime): String = d.toString
  implicit def parseDateTime(s: String): String = DateTime.parse(s)
  implicit object dateTimeConsumer extends Consumer[DateTime] {
    def consume(node: AstNode[_]): DateTime = node match {
      case obj: ObjectNode =>
        val addressesConsumer = implicitly[Consumer[Seq[Address]]]
        Person(
          obj.readIntField("id"),
          obj.readStringField("name"),
          addressesConsumer.consume(obj.readArrayField("addresses"))
        )
      case n => throw new MappingException(s"Can't convert a ${n.getClass} to a Person")
    }
  }
  //implicit val formats: Formats = DefaultFormats + SnapshotSerializer

  /*object SnapshotSerializer extends Serializer[Snapshot] {

    def deserialize(implicit format: Formats) = {
      case (TypeInfo(SnapshotClass, _), JObject(List(JField("data", JString(x))))) => Snapshot(x)
    }

    def serialize(implicit format: Formats) = {
      case Snapshot(x: String) => JObject("data" -> JString(x))
    }
  }*/

}
