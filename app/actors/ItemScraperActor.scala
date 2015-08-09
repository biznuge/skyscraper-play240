package actors

import akka.actor.{ Actor }

class MongoWorker extends Actor {

  def receive = {

    case scrape => {
      //Logger.info("HERE WE ARE in MongoWorker RECEIVE")
    }
  }
}

class ItemScraperActor() extends Actor {

  def receive = {
    case scrape => {

      //Logger.info("HERE WE ARE in RECEIVE")
      // Was initially going to use Actor to run scheduled scrape of the feed
      // but due to the mongodb conn not being in any way serializable.
      // instead moved the scrap functionality into the controller
      // which uses core cache to drop the hits against the feed.

    }
  }

}
