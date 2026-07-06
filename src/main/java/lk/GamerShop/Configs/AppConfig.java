package lk.GamerShop.Configs;

import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
public AppConfig() {

    packages("lk.GamerShop.Controllers");
    register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);
    packages("lk.GamerShop.Filters");
}

}
