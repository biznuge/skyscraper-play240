
import actors.ItemScraperActor
import akka.actor.Props
import play.api.{ Application, GlobalSettings, Logger }
import play.libs.Akka

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit

/**
 * Set up general configuration for the application. In particular we set up the Spring application context.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("STARTING UP SKYSCRAPER....")

    val myActor = Akka.system.actorOf(Props[ItemScraperActor], name = "myactor")
    Akka.system.scheduler.schedule(FiniteDuration.apply(0, TimeUnit.SECONDS), FiniteDuration.apply(1, TimeUnit.MINUTES), myActor, "transactions")

  }

  override def onStop(app: Application) {
    Logger.info("SHUTTING DOWN SKYSCRAPER....")
  }

}