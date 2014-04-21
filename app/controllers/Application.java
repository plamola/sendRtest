package controllers;


import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    /**
     * Display the home page.
     */
    public static Result index() {
        return ok(index.render());
    }

}
