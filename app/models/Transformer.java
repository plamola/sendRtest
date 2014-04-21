package models;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

/**
 * This entity contains the Transformer configuration
 */
@Entity
public class Transformer extends Model{

    @Id
    public Long id;

    @Required
    @Constraints.Pattern("[a-zA-Z0-9_]*")
    @Column(unique=true, nullable=false)
    public String name;

    public String category;

    // input properties
    @Required
    public long nrOfRequests;


    // Webservice properties
    @Required
    public String webserviceCharSet = "UTF-8";

    @Required
    public String webserviceURL;

    public String webserviceUser;
    public String webservicePassword;

    @Required
    public int webserviceTimeout = 10000;


    @Required
    @Lob
    public String webserviceTemplate = "<soap></soap>";

    public int version = 1;


    private static final Finder<Long, Transformer> find = new Finder(Long.class, Transformer.class);

    public static List<Transformer> all() {
        return find.where("1=1").orderBy("category ASC, name ASC").findList();
    }

    public static Transformer findById(Long id) {
        return find.byId(id);
    }

    public static List<Long> allIds() {
        List<Transformer> list  = find.where("1=1").orderBy("name ASC").findList();
        List<Long> ids = new ArrayList<Long>();
        for(Transformer tr : list) {
            ids.add(tr.id);
        }
        return ids;
    }

    public static void create(Transformer transformer) {
        transformer.save();
    }

    public static void update(Transformer transformer) {
        Transformer originalTransformer =  Transformer.find.ref(transformer.id);
        copyContents(transformer,originalTransformer);
        originalTransformer.save();
    }


    private static void copyContents(Transformer source, Transformer destination) {
        // Copy everything except the id field
        destination.nrOfRequests = source.nrOfRequests;
        destination.name = source.name;
        destination.category = source.category;
        destination.webserviceCharSet = source.webserviceCharSet;
        destination.webservicePassword = source.webservicePassword;
        destination.webserviceTemplate = source.webserviceTemplate;
        destination.webserviceTimeout = source.webserviceTimeout;
        destination.webserviceURL = source.webserviceURL;
        destination.webserviceUser = source.webserviceUser;
        destination.version = source.version;
    }

    public static Transformer cloneTransformer(Long sourceId) {
        Transformer sourceTransformer = Transformer.find.ref(sourceId);
        Transformer clonedTransformer = new Transformer();
        copyContents(sourceTransformer,clonedTransformer);
        clonedTransformer.name = sourceTransformer.name + "_Clone_of_"+ sourceTransformer.id;
        clonedTransformer.save();
        clonedTransformer.name = sourceTransformer.name + "_CLONE_"+ clonedTransformer.id;
        clonedTransformer.save();
        return clonedTransformer;
    }


    public static void delete(Long id) {
        find.ref(id).delete();
    }


}
