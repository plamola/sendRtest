package controllers;

import models.Transformer;
import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import support.bulkImport.ImportMangerSystem;
import views.html.configurationMain;

/**
 * Created with IntelliJ IDEA.
 * User: matthijs
 * Date: 7/16/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Security.Authenticated(Secured.class)
public class SupervisorControl extends Controller {

    static Form<Transformer> transformerForm = Form.form(Transformer.class);



    public static Result start(long id) {
        Transformer tr = Transformer.findById(id);
        if (tr != null) {
            ImportMangerSystem mgr = ImportMangerSystem.getInstance();
            // TODO move number of workers to config
            mgr.startImportManager(getNumberOfWorkers(),tr);
        } else {
            Logger.error("Transformer with id " + id + " does not exist.");
        }
        return ok();
    }

    public static Result pause(long id) {
        if (checkIfTransformerExists(id)) {
            Logger.debug("Contoller: Pause/Resume ImportManager for " + id);
            ImportMangerSystem mgr = ImportMangerSystem.getInstance();
            mgr.pauseImportManager(id);
        } else {
            Logger.error("Transformer with id " + id + " does not exist.");
        }
        return ok();
    }

    public static Result stop(long id) {
        if (checkIfTransformerExists(id)) {
            Logger.debug("Contoller: Stopping ImportManager for " + id);
            ImportMangerSystem mgr = ImportMangerSystem.getInstance();
            mgr.stopImportManager(id);
        } else {
            Logger.error("Transformer with id " + id + " does not exist.");
        }
        return ok();
    }

    public static Result edit(long id) {
        if (id == 0) {
            return ok(views.html.transformer_newedit.render("New transformer", id, transformerForm.fill(new Transformer())));
        } else {
            return ok(views.html.transformer_newedit.render("Edit transformer", id, transformerForm.fill(Transformer.findById(id))));
        }
    }

    public static Result save(Long id) {
        Form<Transformer> filledForm = transformerForm.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(
                    views.html.transformer_newedit.render("Error while saving", id,  filledForm)
            );
        } else {
            Transformer transformer = filledForm.get();
            if (id == 0)  {
                Transformer.create(transformer);
            }else {
                Transformer.update(transformer);
            }
            return redirect(routes.Application.index());
        }
    }

    public static Result delete(Long id) {
        Transformer.delete(id);
        return redirect(routes.Application.index());
    }

    private static boolean checkIfTransformerExists(long id) {
        Transformer tr = Transformer.findById(id);
        return (tr != null);
    }

    private static int getNumberOfWorkers() {
        String nrOfWorkers = Play.application().configuration().getString("sendr.nrofworkers");
        int wrkrs = 8;
        try {
            wrkrs = Integer.parseInt(nrOfWorkers);
        } catch (Exception e) {
        }
        return wrkrs;
    }


}
