package controllers

//import controllers.Application._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.cache.Cached
import play.cache.Cache
import play.modules.reactivemongo.{ ReactiveMongoComponents, ReactiveMongoApi, MongoController }
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{ BSONInteger, BSONDateTime, BSONDocument }
import views.html.helper.form
import scala.collection.mutable
import scala.concurrent.{ Await, Future }
import reactivemongo.api.{ QueryOpts, MongoDriver, Cursor }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import org.slf4j.{ LoggerFactory, Logger }
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import play.api.libs.json._
import play.modules.reactivemongo.json._
import scala.concurrent.duration._

import play.api.libs.json._

import scala.util.{ Success, Failure }

class Items @Inject() (
  val reactiveMongoApi: ReactiveMongoApi,
  cached: Cached)
    extends Controller with MongoController with ReactiveMongoComponents {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Items])

  def collection: JSONCollection = db.collection[JSONCollection]("items")

  import models._

  def toInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case e:
        Exception => None
    }
  }

  def toLong(s: String): Option[Long] = {
    try {
      Some(s.toLong)
    } catch {
      case e:
        Exception => None
    }
  }

  //(pubDate: Int)
  def getMore(pubDate: String) = Action.async { request =>

    var pubDateLong = toLong(pubDate)

    // http://stackoverflow.com/questions/26850964/how-do-write-a-date-range-query-in-mongodb-using-reactive-mongo-in-scala
    // appears to work in the mongo client using.
    // > db.items.find({"pubDate": {"$lt": 1438884060000}})

    val query = BSONDocument(
      "pubDate" -> BSONDocument(

        // Attempting to do this as a BSONDateTime, as assumed since that's the DATATYPE
        // resulted in no results. Using the raw Long here as the comparison value
        // worked. No idea. Works.
        //////"$lte" -> BSONDateTime(pubDateLong.head)

        "$lt" -> pubDateLong.head

      )
    )

    val sortJson = Json.obj("pubDate" -> -1)
    val futureList: scala.concurrent.Future[List[Item]] =
      collection.
        find(query).
        sort(sortJson).
        cursor[Item]().
        collect[List](10)

    val timeout = 10.seconds
    val timeoutFuture = play.api.libs.concurrent.Promise.timeout("Oops", timeout)

    Future.firstCompletedOf(Seq(futureList, timeoutFuture)).map {
      case list: List[Item] => {
        // on successful return the items from the mongo collection
        // are serialised to json and returned to the client.
        Ok(Json.obj("items" -> list))
      }
      // error case.
      case t: String => InternalServerError(t)
    }

  }

  def getItems = Action.async {

    val query = BSONDocument()
    val sortJson = Json.obj("pubDate" -> -1)
    val futureList: scala.concurrent.Future[List[Item]] =
      collection.
        find(query).
        sort(sortJson).
        cursor[Item]().
        collect[List](10)

    val timeout = 10.seconds
    val timeoutFuture = play.api.libs.concurrent.Promise.timeout("Oops", timeout)

    if (Cache.get("scraped") == null) {

      //
      Cache.set("scraped", true, 300 * 1);
      // immediately set the cache object so that no subsequent requests hit this block.
      // not entirely sure about whether this cache class/object is application / session
      // scope, so this is either very efficient or not even slightly...
      //
      // tested with sessions in FF / Chrome and it DOES do an application
      // scope style cache. Cool as tits.
      //
      // this prevents us from
      //  A) making constant requests to the SKY RSS, which would most likely result
      //     in a restriction, since traffic increase would DDOS... :)
      //  B) Roundtrips to the DB can be reduced significantly.
      //
      // Caveat. This method, although it drops resource, will require a single first
      // hit of the controller emthod which would initialise the mongodb recordset
      // with Atom Feed data.

      import reactivemongo.bson._
      val xml = scala.xml.XML.load("http://www.skysports.com/feeds/11095/news.xml")

      val items = xml \ "channel" \ "item"

      items.foreach { n =>

        val title = (n \\ "title").text
        val description = (n \\ "description").text
        val link = (n \\ "link").text
        val pubDate = (n \\ "pubDate").text
        val category = (n \\ "category").text
        val image = (n \\ "enclosure" \ "@url").text

        // BST explodes the DateTimeFormat parse at the z
        // stupid Bangladeshi Standard Time.
        // "Sat, 01 Aug 2015 08:00:00 BST"
        //
        // This technically makes dates incorrect to a certain degree ( a fairly high one
        // for 6 months of the year to be precise).
        //
        val dateTimeStr = pubDate.replace(" BST", " GMT")

        var dateTimeObj = DateTime.parse(dateTimeStr, DateTimeFormat.forPattern("EEE, dd MMM y HH:mm:ss z"))

        val item = Item(
          title,
          description,
          link,
          dateTimeObj,
          category,
          image
        )

        collection.insert(item).map {
          lastError =>
            logger.debug(s"Successfully inserted with LastError: $lastError")
            Created(s"Item Created")
        }

      }

    }

    Future.firstCompletedOf(Seq(futureList, timeoutFuture)).map {
      case list: List[Item] => {
        // on successful return the items from the mongo collection
        // are serialised to json and returned to the client.
        Ok(Json.obj("items" -> list))
      }
      // error case.
      case t: String => InternalServerError(t)
    }

  }

  /*
  {

    if (Cache.get("scraped") == null) {

      //
      Cache.set("scraped", true, 300 * 1);
      // immediately set the cache object so that no subsequent requests hit this block.
      // not entirely sure about whether this cache class/object is application / session
      // scope, so this is either very efficient or not even slightly...
      //
      // tested with sessions in FF / Chrome and it DOES do an application
      // scope style cache. Cool as tits.
      //
      // this prevents us from
      //  A) making constant requests to the SKY RSS, which would most likely result
      //     in a restriction, since traffic increase would DDOS... :)
      //  B) Roundtrips to the DB can be reduced significantly.
      //

      import reactivemongo.bson._

      // { "age": { "$gt": 27 } }
      //val query = BSONDocument("age" -> BSONDocument("$gt" -> 27))

      /*val query = BSONDocument();
      val peopleOlderThanTwentySeven =
        collection.
          find(query).
          cursor[BSONDocument].
          collect[List]()*/

      //  [error] C:\sites\skyscraper9\app\controllers\Items.scala:97: No Json serializer
      //as JsObject found for type reactivemongo.bson.BSONDocument. Try to implement an
      //implicit OWrites or OFormat for this type.

      // result type is Future[List[BSONDocument]]
      /*val peopleOlderThanTwentySeven =
        collection.
          find(query).
          cursor[BSONDocument].
          collect[List]()*/

      val xml = scala.xml.XML.load("http://www.skysports.com/feeds/11095/news.xml")

      val items = xml \ "channel" \ "item"

      items.foreach { n =>

        val title = (n \\ "title").text
        val description = (n \\ "description").text
        val link = (n \\ "link").text
        val pubDate = (n \\ "pubDate").text
        val category = (n \\ "category").text
        val image = (n \\ "enclosure" \ "@url").text

        // BST explodes the DateTimeFormat parse at the z
        // stupid Bangladeshi Standard Time.
        // "Sat, 01 Aug 2015 08:00:00 BST"
        //
        // This technically makes dates incorrect to a certain degree ( a fairly high one
        // for 6 months of the year to be precise).
        //
        val dateTimeStr = pubDate.replace(" BST", " GMT")

        var dateTimeObj = DateTime.parse(dateTimeStr, DateTimeFormat.forPattern("EEE, dd MMM y HH:mm:ss z"))

        val item = Item(
          title,
          description,
          link,
          dateTimeObj,
          category,
          image
        )

        collection.insert(item).map {
          lastError =>
            logger.debug(s"Successfully inserted with LastError: $lastError")
            Created(s"Item Created")
        }

      }

      play.Logger.info("just created a bunch of news... maybe")

    } else {
      play.Logger.info("missed the Atom consumer due to cache.")
    }


  }
  */

  /*Future.successful {
      BadRequest("invalid json")
    }*/

  /*request.body.validate[Item].map {
        item =>
          // `item` is an instance of the case class `models.Item`
          collection.insert(item).map {
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"Item Created")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))*/

  //}

  /*def updateItem(firstName: String, lastName: String) = Action.async(parse.json) {
    request =>
      request.body.validate[Item].map {
        user =>
          // find our user by first name and last name
          val nameSelector = Json.obj("firstName" -> firstName, "lastName" -> lastName)
          collection.update(nameSelector, user).map {
            lastError =>
              logger.debug(s"Successfully updated with LastError: $lastError")
              Created(s"User Updated")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }*/

  /*def findItems(page: Integer, term: String) = Action.async(parse.json) {
    // let's do our query
    val cursor: Cursor[Item] = collection.
      // find all
      find(Json.obj("active" -> true)).
      // sort them by creation date
      sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[Item]

    // gather all the JsObjects in a list
    val futureItemsList: Future[List[Item]] = cursor.collect[List]()

    // transform the list into a JsArray
    val futurePersonsJsonArray: Future[JsArray] = futureItemsList.map { items =>
      Json.arr(items)
    }
    // everything's ok! Let's reply with the array
    futurePersonsJsonArray.map {
      items =>
        Ok(items(0))
    } //.getOrElse(Future.successful(BadRequest("invalid json")))
  }*/

}
