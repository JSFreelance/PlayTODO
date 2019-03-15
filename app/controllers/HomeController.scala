package controllers

import javax.inject._
import play.api.mvc._
import models.Task
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import scala.collection.mutable.ListBuffer
import services.dbService
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject() (dbService: dbService) extends Controller
{

  val taskForm = Form(
    mapping(
      "id" -> optional(number),
      "text" -> nonEmptyText
    )(Task.apply)(Task.unapply)
  )

  def index = Action.async
  {
    dbService.all().map { case (tasks) => Ok(views.html.index(taskForm, tasks.to[ListBuffer])) }
  }

  def insertTask = Action(parse.form(taskForm, onErrors = (formWithErrors: Form[Task]) => {
    Redirect(routes.HomeController.index())
  })) { implicit request =>
    val taskData = request.body
    val newTask = Task(None, taskData.text)

    dbService.insert(newTask)
    Redirect(routes.HomeController.index())
  }

  def updateTask = Action { implicit request =>
    val taskData = request.body.asFormUrlEncoded
    val id = taskData.get("id")(0).toInt
    val text = taskData.get("text")(0)
    val taskUpdated = Task(Some(id), text)

    dbService.update(id, taskUpdated)
    Redirect(routes.HomeController.index())
  }

  def deleteTask(id: Int) = Action.async {

    //val taskData = request.body.asFormUrlEncoded
    //val id = taskData.get("id")(0).toInt

    dbService.delete(id)

    dbService.all().map { case (tasks) => Ok(views.html.index(taskForm, tasks.to[ListBuffer])) }
  }

}
