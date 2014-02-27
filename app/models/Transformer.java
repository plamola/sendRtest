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

    public String tags;

    // CVS properties
    @Required
    public String importPath;

    @Required
    public String importFileExtension = ".csv";

    @Required
    public String importFilecontentType = "cp1252";



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


    public String timeStampString = "2013-01-01T00:00:00.000+200";


    private static final Finder<Long, Transformer> find = new Finder(Long.class, Transformer.class);

    public static List<Transformer> all() {
        return find.where("1=1").orderBy("name ASC").findList();
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
        originalTransformer.importFilecontentType = transformer.importFilecontentType;
        originalTransformer.importFileExtension = transformer.importFileExtension;
        originalTransformer.importPath = transformer.importPath;
        originalTransformer.name = transformer.name;
        originalTransformer.tags = transformer.tags;
        originalTransformer.timeStampString = transformer.timeStampString;
        originalTransformer.webserviceCharSet = transformer.webserviceCharSet;
        originalTransformer.webservicePassword = transformer.webservicePassword;
        originalTransformer.webserviceTemplate = transformer.webserviceTemplate;
        originalTransformer.webserviceTimeout = transformer.webserviceTimeout;
        originalTransformer.webserviceURL = transformer.webserviceURL;
        originalTransformer.webserviceUser = transformer.webserviceUser;
        originalTransformer.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }


}
