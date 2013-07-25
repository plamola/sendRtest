import com.avaje.ebean.Ebean;
import models.Transformer;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {


    public void onStart(Application app) {
        InitialData.insert(app);
    }

    public void onStop(Application app) {

    }


    static class InitialData {

        public static void insert(Application app) {

            if(Ebean.find(User.class).findRowCount() == 0) {
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
                Ebean.save(all.get("users"));
            }

            if(Ebean.find(Transformer.class).findRowCount() == 0) {
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("transformers.yml");
                for(String key : all.keySet()) {
                    Ebean.save(all.get(key));
                }
            }



        }

    }

}