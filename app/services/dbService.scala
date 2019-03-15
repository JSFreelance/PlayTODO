package services

import scala.concurrent.Future
import javax.inject.Inject
import models.Task
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

class dbService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]
{
  import profile.api._

  private val Tasks = TableQuery[TasksTable]
  val setup = DBIO.seq(
    (Tasks.schema).create
  )
  val setupFuture = db.run(setup)

  def all(): Future[Seq[Task]] = db.run(Tasks.result)

  def findById(id: Int): Future[Option[Task]] =
    db.run(Tasks.filter(_.id === id).result.headOption)

  def insert(task: Task): Future[Unit] = db.run(Tasks += task).map { _ => ()}

  def delete(id: Int): Future[Unit] =
    db.run(Tasks.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, task: Task): Future[Unit] = {
    val taskToUpdate: Task = task
    db.run(Tasks.filter(_.id === id).update(taskToUpdate)).map(_ => ())
  }

  private class TasksTable(tag: Tag) extends Table[Task](tag, "TASK"){
    def id   = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def text = column[String]("TEXT")

    def * = (id.?, text) <> (Task.tupled, Task.unapply _)
  }

}
