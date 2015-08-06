package models

import java.util.UUID

import org.joda.time.DateTime
import play.Logger
import play.api.libs.json.Json
import reactivemongo.bson._

case class Item(
  //uuid: Option[UUID],
  //uuid: UUID,
  title: String,
  description: String,
  link: String,
  //pubDate: Option[DateTime],
  pubDate: DateTime,
  category: String,
  image: String)

object Item {
  implicit val ItemFormat = Json.format[Item]

  /*implicit object ItemIdentity extends Identity[Item, UUID] {
    val name = "uuid"
    //def of(entity: Item): Option[UUID] = entity.uuid
    //def set(entity: Item, id: UUID): Item = entity.copy(uuid = Option(id))
    //def clear(entity: Item): Item = entity.copy(uuid = None)
    //def next: UUID = UUID.randomUUID()
    def of(entity: Item): UUID = entity.uuid
    def set(entity: Item, id: UUID): Item = entity.copy(uuid = id)
    def clear(entity: Item): Item = entity.copy(uuid = null)
    def next: UUID = UUID.randomUUID()
  }*/

  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    def read(time: BSONDateTime) = new DateTime(time.value)
    def write(jdtime: DateTime) = BSONDateTime(jdtime.getMillis)
  }
}

object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val itemFormat = Json.format[Item]
}

/*object BSONMap {
  implicit def MapReader[V](implicit vr: BSONDocumentReader[V]): BSONDocumentReader[Map[String, V]] = new BSONDocumentReader[Map[String, V]] {
    def read(bson: BSONDocument): Map[String, V] = {
      val elements = bson.elements.map { tuple =>
        // assume that all values in the document are BSONDocuments
        tuple._1 -> vr.read(tuple._2.seeAsTry[BSONDocument].get)
      }
      elements.toMap
    }
  }

  implicit def MapWriter[V](implicit vw: BSONDocumentWriter[V]): BSONDocumentWriter[Map[String, V]] = new BSONDocumentWriter[Map[String, V]] {
    def write(map: Map[String, V]): BSONDocument = {
      val elements = map.toStream.map { tuple =>
        tuple._1 -> vw.write(tuple._2)
      }
      BSONDocument(elements)
    }
  }

}*/

