package example

import example.domain.generated.User
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._
import scalikejdbc._

object UsersController extends Controller {

  private[this] implicit val userWrites: OWrites[User] = (
    (__ \ "id").write[Int] and
    (__ \ "name").write[String] and
    (__ \ "created_at").write[DateTime]
  )(Function.unlift(User.unapply))


  val index = Action{
    val users = DB.localTx{ implicit session =>
      User.findAll()
    }
    val json = JsArray(users.map(Json.toJson[User]))
    Ok(json)
  }

  private[this] final val result = "result"

  def get(name: String) = Action {
    DB.localTx { implicit session =>
      User.findBy(sqls.eq(User.u.name, name))
    } match {
      case Some(user) =>
        Ok(Json.obj(result -> Json.toJson(user)))
      case None =>
        NotFound(Json.obj(result -> (name + " not fould")))
    }
  }

  def put(name: String) = Action {
    val user = DB.localTx { implicit session =>
      User.create(name, DateTime.now)
    }
    Created(Json.toJson(user))
  }

  def delete(name: String) = Action{
    DB.localTx { implicit session =>
      withSQL {
        scalikejdbc.delete.from(User).where.eq(User.column.name, name)
      }.update.apply()
    } match {
      case 1 =>
        Ok(Json.obj(result -> "deleted"))
      case _ =>
        NotFound(Json.obj(result -> "not found"))
    }
  }

}
