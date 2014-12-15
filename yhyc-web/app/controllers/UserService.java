package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class UserService extends Controller {

	static Form<Register> regform = Form.form(Register.class);

	public static Result register() {
		return ok(views.html.register.render(regform));
	}

	public static Result createuser() {
		Form<Register> form = regform.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(views.html.register.render(form));
		} else {
			User user = new User();
			user.email = form.get().email;
			user.name = form.get().name;
			user.password = form.get().password;
			User.save(user);
			flash("success", "welcome you " + user.email);
			return redirect(routes.UserService.register());
		}

	}
	
	public static Result show(){
		return ok(views.html.user.render(User.findByEmail(session().get("email"))));
	}

	public static class Register {

		public String email;
		public String password;
		public String name;
		public String tel;

		public String validate() {
			if (User.findByEmail(email) != null) {
				return "This email is already used!";
			}
			return null;
		}

	}

}
