import org.flywaydb.core.Flyway

object FlywayMigrate {
  def main(args: Array[String]): Unit = {
    val schema = System.getProperty("flyway.schema")
    val url = System.getProperty("flyway.url")
    val user = System.getProperty("flyway.user")
    val password = System.getProperty("flyway.password")
    val location = System.getProperty("flyway.location")

    val flyway = Flyway
      .configure()
      .schemas(schema)
      .dataSource(
        url,
        user,
        password
      )
      .locations(location)
      .load()

    args.headOption match {
      case Some("migrate") =>
        flyway.migrate()
      case Some("clean") =>
        flyway.clean()
      case _ =>
        sys.error("invalid arguments " + args.mkString(", "))
    }
  }
}
