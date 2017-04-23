package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import models.Task
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import scala.collection.mutable.ListBuffer

@Singleton
class HomeController @Inject() extends Controller
{

  var data = new ListBuffer[Task]
  val taskForm = Form(
    mapping(
      "text" -> nonEmptyText
    )(Task.apply)(Task.unapply)
  )

  def index = Action
  {
     Ok(views.html.index(taskForm, data))
  }


  def insertUser = Action(parse.form(taskForm)) { implicit request =>
    val taskData = request.body
    val newTask = Task(taskData.text)
    data += newTask
    Redirect(routes.HomeController.index())
  }


}
