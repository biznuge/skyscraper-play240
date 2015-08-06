package services

import javax.inject.Inject

import models.Item
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.Logger
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{ BSONDateTime, BSONDocument }

object Scraper {

  def scrape() = {

    /*val xml = scala.xml.XML.load("http://www.skysports.com/feeds/11095/news.xml")

    val items = xml \ "channel" \ "item"

    items.foreach { n =>
      val title = (n \\ "title").text
      val description = (n \\ "description").text
      val link = (n \\ "link").text
      val pubDate = (n \\ "pubDate").text
      val category = (n \\ "category").text
      val image = (n \\ "enclosure" \ "@url").text
      //println(s"$day, $date, Low: $low")

      Logger.info(
        "item: %s, %s, %s, %s, %s, %s",
        title, description, link, pubDate, category, image
      )

    }

    Logger.info("items", items)*/

    /*val driver = new MongoDriver
    val conn = driver.connection(List("localhost27017"))

    Logger.info("AFTER we got our connection")

    def xml = scala.xml.XML.load("http://www.skysports.com/feeds/11095/news.xml")

    val items = xml \ "channel" \ "item"

    items.foreach { n =>
      val title = (n \\ "title").text
      val description = (n \\ "description").text
      val link = (n \\ "link").text
      val pubDate = (n \\ "pubDate").text
      val category = (n \\ "category").text
      val image = (n \\ "enclosure" \ "@url").text

      // BST explodes the DateTimeFormat parse at the z
      // stupid Bangladesh Standard Time.
      // "Sat, 01 Aug 2015 08:00:00 BST"
      val dateTimeStr = pubDate.replace(" BST", " GMT")

      var dateTimeObj = DateTime.parse(dateTimeStr, DateTimeFormat.forPattern("EEE, dd MMM y HH:mm:ss z"))

      val item = Item(
        //UUID.randomUUID(),
        title,
        description,
        link,
        dateTimeObj,
        category,
        image
      )

      val bsonDocument =
        BSONDocument(
          //"uuid" -> item.uuid,
          "title" -> item.title,
          "description" -> item.description,
          "link" -> item.link,
          "pubDate" -> BSONDateTime(item.pubDate.getMillis),
          "category" -> item.category,
          "image" -> item.image
        )

      Logger.info("AFTER bsonDoc creation")

      //val driver = new MongoDriver
      //val connection = driver.connection(List("localhost:27017"))

      // Gets a reference to the database "plugin"
      val db = conn("skyscraper")

      // Gets a reference to the collection "acoll"
      // By default, you get a BSONCollection.
      //lazy val session = database.createSession()

      val collection = db[BSONCollection]("items")

      val future = collection.insert(bsonDocument)

    }*/

  }

}

/*abstract class Scraper() {
  def scrape() = {

  }
}

@Singleton
class SimpleScraper extends Scraper {

  override def scrape() = {

    val xml = scala.xml.XML.load("http://www.skysports.com/feeds/11095/news.xml")

    val items = xml \ "channel" \ "item"

    items.foreach { n =>
      val title = (n \\ "title").text
      val description = (n \\ "description").text
      val link = (n \\ "link").text
      val pubDate = (n \\ "pubDate").text
      val category = (n \\ "category").text
      val image = (n \\ "enclosure" \ "@url").text
      //println(s"$day, $date, Low: $low")

      Logger.info(
        "item: %s, %s, %s, %s, %s, %s",
        title, description, link, pubDate, category, image
      )

    }

    Logger.info("items", items)

  }

}*/
