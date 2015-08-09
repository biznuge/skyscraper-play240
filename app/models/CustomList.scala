package models

import java.util.UUID

import org.joda.time.DateTime
import play.Logger
import play.api.libs.json.Json
import reactivemongo.bson._

case class Item(
  title: String,
  description: String,
  link: String,
  pubDate: DateTime,
  category: String,
  image: String)

object Item {
  implicit val ItemFormat = Json.format[Item]



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


