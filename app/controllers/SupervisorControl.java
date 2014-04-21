package controllers;

import models.Transformer;
import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import support.bulkImport.ImportMangerSystem;

public class SupervisorControl extends Controller {

    private static final Form<Transformer> transformerForm = Form.form(Transformer.class);

    public static Result start(Long id) {
        Transformer tr = Transformer.findById(id);
        if (tr != null) {
            ImportMangerSystem mgr = ImportMangerSystem.getInstance();
            mgr.startImportManager(getNumberOfWorkers(),tr);
        } else {
            Logger.error("Transformer with id " + id + " does not exist.");
        }
        return ok();
    }

    public static Result pause(Long id) {
        if (checkIfTransformerExists(id)) {
            Logger.debug("Contoller: Pause/Resume ImportManager for " + id);
            ImportMangerSystem mgr = ImportMangerSystem.getInstance();
            mgr.pauseImportManager(id);
        } else {
            Logger.error("Transformer with id " + id + " does not exist.");
        }
        return ok();
    }

    public static Result stop(Long id) {
        if (checkIfTransformerExists(id)) {
            Logger.debug("Contoller: Stopping ImportManager for " + id);
            ImportMangerSystem mgr = ImportMangerSystem.getInstance();
            mgr.stopImportManager(id);
        } else {
            Logger.error("Transformer with id " + id + " does not exist.");
        }
        return ok();
    }

    public static Result edit(Long id) {
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
            try {
                if (id.longValue() == 0L)  {
                    Transformer.create(transformer);
                } else {
                    Transformer.update(transformer);
                }
            } catch (Exception e) {
                return badRequest(views.html.transformer_newedit.render("Error while saving. " + e.getMessage(), id,  filledForm));
            }
            return redirect(routes.Application.index());
        }
    }

    public static Result delete(Long id) {
        Transformer.delete(id);
        return redirect(routes.Application.index());
    }

    public static Result cloneThisTransformer(Long sourceId) {
        Transformer cloned = Transformer.cloneTransformer(sourceId);
        return redirect(routes.SupervisorControl.edit(cloned.id));
    }

    private static boolean checkIfTransformerExists(Long id) {
        Transformer tr = Transformer.findById(id);
        return (tr != null);
    }

    private static int getNumberOfWorkers() {
        String nrOfWorkers = Play.application().configuration().getString("sendr.nrofworkers");
        try {
            return Integer.parseInt(nrOfWorkers);
        } catch (Exception e) {
            return 8;
        }
    }

}
