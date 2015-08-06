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
import scala.concurrent.Future
import reactivemongo.api.{ QueryOpts, MongoDriver, Cursor }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import org.slf4j.{ LoggerFactory, Logger }
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import play.api.libs.json._

/**
 * The Users controllers encapsulates the Rest endpoints and the interaction with the MongoDB, via ReactiveMongo
 * play plugin. This provides a non-blocking driver for mongoDB as well as some useful additions for handling JSon.
 * @see https://github.com/ReactiveMongo/Play-ReactiveMongo
 */
//@Singleton //class Items extends Controller with MongoController {
//class Items extends Controller with MongoController {
//class Items @Inject() (val reactiveMongoApi: ReactiveMongoApi)
class Items @Inject() (
  val reactiveMongoApi: ReactiveMongoApi,
  cached: Cached)
    extends Controller with MongoController with ReactiveMongoComponents {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Items])

  def collection: JSONCollection = db.collection[JSONCollection]("items")

  import models._

  def index = Action {

    play.Logger.info("Hello Play Framework")
    Ok(views.html.index("Hello Play Framework"))

  }

  def scrape = Action {

    //val query = {"IMAGE URL":{$exists:true}}

    /*
    val latestItems =
      collection.
        find(query).
        // sort by lastName
        //sort(BSONDocument("pubDate" -> 1)).
        cursor[BSONDocument].
        collect[List]()*/

    /* this kind of works. parenthesis around the end of the cursro was the key due to the deprecation.*/
    /*val cursor: Cursor[Item] = collection.
      // find all
      find(BSONDocument()).
      // sort them by creation date
      sort(Json.obj("pubDate" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[Item]()
/////////////////////////////////////////////////////

    peopleOlderThanTwentySeven.map { people =>
      for(person <- people) {
        val firstName = person.firstName
        println(s"found $firstName")
      }
    }*/

    // didn't work. Seems to be expecting a JSON
    // document for some unfathomable reason.
    val findBson = BSONDocument("pubDate" ->
      BSONDocument("$eq" -> true)
    )

    // still utterly broken. now coming back with
    // " No Json serializer as JsObject found for type Unit.
    // Try to implement an implicit OWrites or OFormat for this type. "
    //
    // ---> Relational Data Modelling never seemed as easy as it does right now. <---
    val findJson = Json.obj("pubDate" ->
      Json.obj("$eq" -> true)
    )

    // works.
    val sortJson = Json.obj("pubDate" -> -1)

    val sortedItems =
      collection.
        //find(query).
        find(findBson).
        //sort(sortJson).
        cursor[Item]().
        collect[List]()

    sortedItems.map { item =>
      for (item <- item) {
        val title = item.title
        //println(s"found $firstName")
        play.Logger.info(s"found $title")
      }
    }

    //val query = BSONDocument()

    /*collection.find(query)
      .options(QueryOpts(10, 5))
      .sort(BSONDocument("pubDate" -> 1)) // WORKS*/

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
      val query = BSONDocument("age" -> BSONDocument("$gt" -> 27))

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

    Ok("OK")
  }

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
