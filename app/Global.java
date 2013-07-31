import com.avaje.ebean.Ebean;
import models.Transformer;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import play.libs.Yaml;
import scala.concurrent.duration.Duration;
import support.bulkImport.ImportMangerSystem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Global extends GlobalSettings {


    public void onStart(Application app) {
        // get a status update every 10 seconds
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(10, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        ImportMangerSystem mgr = ImportMangerSystem.getInstance();
                        mgr.reportOnAllSuperVisors();
                    }
                }, Akka.system().dispatcher());

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