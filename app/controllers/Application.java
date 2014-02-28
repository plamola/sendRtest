package controllers;


import models.Transformer;
import models.User;
import play.data.Form;

import play.mvc.*;

import views.html.*;

import static play.data.Form.form;

public class Application extends Controller {

    // -- Authentication

    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if(User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    //private static final Form<Transformer> transformerForm = Form.form(Transformer.class);

    /**
     * Display the home page.
     */
    public static Result index() {
        return ok(configurationMain.render());
    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(
                views.html.login.render(form(Login.class))
        );
    }

    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(
                    routes.Application.index()
            );
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Application.login()
        );
    }



}
