package controllers;

import models.Article;
import play.mvc.Controller;
import play.mvc.Result;

public class Stats extends Controller {
	
	public static Result index() {

		return ok(views.html.stats.render());
	}

}
