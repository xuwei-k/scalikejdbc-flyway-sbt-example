package example

import javax.inject._
import play.api._
import play.api.inject._
import scala.concurrent.Future
import scalikejdbc.ConnectionPool

@Singleton
class PlayInitializer @Inject()(
    lifecycle: ApplicationLifecycle
) {

  def onStart(): Unit = {
    ConnectionPool.singleton(
      "jdbc:mysql://localhost/test",
      "root",
      ""
    )
  }

  def onStop(): Unit = {
    ConnectionPool.closeAll()
  }

  lifecycle.addStopHook(() => Future.successful(onStop))
  onStart()
}
