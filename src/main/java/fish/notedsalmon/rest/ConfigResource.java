package fish.notedsalmon.rest;

import fish.notedsalmon.beans.ConfigBean;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/config")
public class ConfigResource {

    @Inject
    private ConfigBean configBean;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getConfigDetails() {
        return "App Name: " + configBean.getAppName() +
                "\nDatabase URL: " + configBean.getDatabaseUrl();
    }
}

