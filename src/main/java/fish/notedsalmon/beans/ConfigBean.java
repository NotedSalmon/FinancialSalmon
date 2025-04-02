package fish.notedsalmon.beans;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigBean {

    @Inject
    @ConfigProperty(name = "app.name", defaultValue = "UnknownApp")
    private String appName;

    @Inject
    @ConfigProperty(name = "database.url")
    private String databaseUrl;

    public String getAppName() {
        return appName;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }
}
