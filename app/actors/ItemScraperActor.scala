package actors

import akka.actor.Actor
import play.Logger

/**
 * Created by BizNuge on 02/08/2015.
 */
class ItemScraperActor extends Actor {
  def receive = {
    case _ => Logger.info("HERE WE ARE in RECEIVE")
  }
  /*def receive() {
    Logger.info("HERE WE ARE in RECEIVE")
  }
  def act(): Unit = {
    Logger.info("HERE WE ARE in ACT")
  }*/
  //override def receive: Receive = ???
}

/*class TestActor extends Actor {

  def receive = {
    case Tick => //...
  }
}*/

//val testActor = Akka.system.actorOf(Props[TestActor], name = "testActor")

//Akka.system.scheduler.schedule(0.seconds, 30.minutes, testActor, Tick)
