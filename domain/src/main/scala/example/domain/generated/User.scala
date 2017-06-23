package example.domain.generated

import scalikejdbc._
import java.time.{OffsetDateTime}

case class User(
  id: Int,
  name: String,
  createdAt: OffsetDateTime) {

  def save()(implicit session: DBSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession): Int = User.destroy(this)(session)

}


object User extends SQLSyntaxSupport[User] {

  override val tableName = "users"

  override val columns = Seq("id", "name", "created_at")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = new User(
    id = rs.get(u.id),
    name = rs.get(u.name),
    createdAt = rs.get(u.createdAt)
  )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.id, id)
    }.map(User(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    withSQL(select(sqls.count).from(User as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    withSQL {
      select(sqls.count).from(User as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    createdAt: OffsetDateTime)(implicit session: DBSession): User = {
    val generatedKey = withSQL {
      insert.into(User).namedValues(
        column.name -> name,
        column.createdAt -> createdAt
      )
    }.updateAndReturnGeneratedKey.apply()

    User(
      id = generatedKey.toInt,
      name = name,
      createdAt = createdAt)
  }

  def batchInsert(entities: Seq[User])(implicit session: DBSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'createdAt -> entity.createdAt))
    SQL("""insert into users(
      name,
      created_at
    ) values (
      {name},
      {createdAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: User)(implicit session: DBSession): User = {
    withSQL {
      update(User).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.createdAt -> entity.createdAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession): Int = {
    withSQL { delete.from(User).where.eq(column.id, entity.id) }.update.apply()
  }

}
