import play.api.Application

object Global extends play.api.GlobalSettings {

  override def onStart(app: Application): Unit ={
    scalikejdbc.ConnectionPool.singleton(
      "jdbc:mysql://localhost/test",
      "root",
      ""
    )
  }

  override def onStop(app: Application): Unit ={
    scalikejdbc.ConnectionPool.closeAll()
  }

}
