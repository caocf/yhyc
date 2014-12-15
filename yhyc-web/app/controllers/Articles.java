package controllers;

import java.util.Date;

import models.Article;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class Articles extends Controller {

	static Form<Article> form = Form.form(Article.class);

	@Security.Authenticated(Secured.class)
	public static Result index() {

		return ok(views.html.articles.render(Article.findAll(), "", "none"));
	}

	public static Result list() {

		return ok(views.html.articles.render(Article.find(request().getQueryString("q"), request().getQueryString("sort")), request().getQueryString("q"), request().getQueryString("sort")));
	}

	public static Result showCreationForm() {
		Logger.info(request().username());
		return ok(views.html.articles_edit.render(null, form));
	}

	public static Result create() {
		Form<Article> myform = form.bindFromRequest();
		if (form.hasErrors()) {
			return redirect(routes.Articles.index());
		} else {
			Article a = new Article();
			a.title = myform.get().title;
			a.content = myform.get().content;
			a.pub = myform.get().pub;
			a.pdt = myform.get().pdt;
			a.crt = new Date();
			Article.save(a);
			flash("success", "Success to add an article");
			return redirect(routes.Articles.index());
		}
	}

	public static Result showEditForm(String id) {
		Form<Article> myform = form.fill(Article.findById(id));
		return ok(views.html.articles_edit.render(id, myform));
	}

	public static Result edit(String id) {
		Form<Article> myform = form.bindFromRequest();
		if (form.hasErrors()) {
			return redirect(routes.Articles.index());
		} else {
			Logger.info(myform.toString());
			Article a = new Article();
			a.title = myform.get().title;
			a.pub = myform.get().pub;
			a.content = myform.get().content;
			a.crt = new Date();
			Article.update(id, a);
			flash("success", "Success to edit an article");
			return ok(views.html.articles_edit.render(id, form));
		}
	}

	public static Result delete(String id) {
		Article.removeById(id);
		return redirect(routes.Articles.index());
	}

}
