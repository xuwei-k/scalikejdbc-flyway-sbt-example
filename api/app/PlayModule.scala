package example

import javax.inject._
import play.api._
import play.api.inject._

class ScalikejdbcPlayModule extends Module {
  def bindings(env: Environment, config: Configuration) =
    Seq(
      bind[PlayInitializer].toSelf.eagerly()
    )
}
