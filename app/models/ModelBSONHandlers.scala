package models

import java.util.UUID

import org.joda.time.DateTime
import reactivemongo.bson._

/**
 * BSON to/from conversions for model parts
 */
object ModelBSONHandlers {

  implicit object BSONUUIDHandler extends BSONHandler[BSONString, UUID] {
    def read(string: BSONString) = UUID.fromString(string.value)
    def write(uuid: UUID) = BSONString(uuid.toString)
  }

  implicit object ItemBSONReader extends BSONDocumentReader[Item] {
    def read(doc: BSONDocument): Item = Item(
      //doc.getAs[UUID]("uuid").get,
      doc.getAs[String]("title").get,
      doc.getAs[String]("link").get,
      doc.getAs[String]("description").get,
      new DateTime(doc.getAs[BSONDateTime]("pubDate").get),
      //doc.getAs[BSONDateTime]("pubDate").get,
      doc.getAs[String]("category").get,
      doc.getAs[String]("image").get
    )
  }
  implicit object ItemBSONWriter extends BSONDocumentWriter[Item] {
    def write(elem: Item): BSONDocument =
      BSONDocument(
        //"uuid" -> elem.uuid,
        "title" -> elem.title,
        "description" -> elem.description,
        "link" -> elem.link,
        //"pubDate" -> elem.pubDate.map(date => BSONDateTime(date.getMillis)),
        "pubDate" -> BSONDateTime(elem.pubDate.getMillis),
        "category" -> elem.category,
        "image" -> elem.image
      )
  }

}