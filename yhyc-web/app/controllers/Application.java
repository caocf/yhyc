package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

public class Application extends Controller {

	static Form<Login> loginform = Form.form(Login.class);

	@Security.Authenticated(Secured.class)
	public static Result index() {
		return ok(index.render(Messages.get("website.title")));
	}

	public static Result login() {
		return ok(views.html.login.render(loginform));
	}

	public static Result authenticate() {
		Form<Login> form = loginform.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(views.html.login.render(form));
		} else {
			session().clear();
			String email = form.get().email;
			session("email", email);
			return redirect(routes.Application.index());
		}
	}

	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.login());
	}

	public static class Login {

		public String email;
		public String password;

		public String validate() {
			if (User.authenticate(email, password) == null) {
				return "Invalid email or password";
			}
			return null;
		}

	}

}
