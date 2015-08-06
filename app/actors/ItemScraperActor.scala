
package actors

import scala.concurrent.ExecutionContext.Implicits.global

import java.util.UUID
import javax.inject.Inject
import akka.routing.FromConfig
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._
import akka.actor.{ Props, Actor }
import models.Item
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.Logger
import play.modules.reactivemongo.ReactiveMongoHelper
import reactivemongo.api.{ MongoConnection, MongoDriver, BSONSerializationPack }
import reactivemongo.bson.{ BSONDateTime, BSON, BSONDocument }
import services.Scraper

import org.joda.time.DateTime
import reactivemongo.bson._

import scala.concurrent.ExecutionContext

//import reactivemongo.bson.handlers.BSONReader
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONString
/**
 * Created by BizNuge on 02/08/2015.
 */
//@Inject() (scraper: Scraper)

class MongoWorker /*(conn: MongoConnection)*/ extends Actor {

  //val driver = new MongoDriver
  //val connection = driver.connection(List("localhost27017"))

  //override def preStart = {
  //context.actorOf(Props(classOf[MongoWorker], connection).withRouter(FromConfig()))
  //}

  def receive = {

    case scrape => {
      Logger.info("HERE WE ARE in MongoWorker RECEIVE")

      //services.Scraper.scrape()

      Logger.info("AFTER bsonDoc / mongo insertion")

    }
  }
}
/*implicit xc: ExecutionContext = ExecutionContext.Implicits.global*/
class ItemScraperActor() extends Actor {

  //val driver = new MongoDriver
  //val connection = driver.connection(List("localhost27017"))

  //override def preStart = {
  //context.actorOf(Props(classOf[MongoWorker], connection).withRouter(FromConfig()))
  //}

  def receive = {
    case scrape => {
      Logger.info("HERE WE ARE in RECEIVE")

      //controllers.Items.

      /*def xml = scala.xml.XML.load("http://www.skysports.com/feeds/11095/news.xml")

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

        val driver = new MongoDriver
        val connection = driver.connection(List("localhost:27017"))

        // Gets a reference to the database "plugin"
        val db = connection("skyscraper")

        // Gets a reference to the collection "acoll"
        // By default, you get a BSONCollection.
        lazy val session = database.createSession()




        ////////////////val collection = db[BSONCollection]("items")

        ////////////////val future = collection.insert(bsonDocument)





        Logger.info("AFTER bsonDoc / mongo insertion")


      }*/

      Logger.info("STILL HERE...")

    }
  }

  //}
}

/*class TestActor extends Actor {

  def receive = {
    case Tick => //...
  }
}*/

//val testActor = Akka.system.actorOf(Props[TestActor], name = "testActor")

//Akka.system.scheduler.schedule(0.seconds, 30.minutes, testActor, Tick)
