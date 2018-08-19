package pl.codepride.dailyadvisor.functionaltest.common;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class ConfigManager {

    private static Configurations configs = new Configurations();

    private static Configuration config;

    /**
     * Initial configuration.
     */
    static {
        try {
            config = configs.properties(new File("application.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load string property from test properties.
     * @param name Property name.
     * @return Property value.
     */
    public static String loadStringProperty(String name) {
        return config.getString(name);
    }

    /**
     * Load integer property from test properties.
     * @param name Property name.
     * @return Property value.
     */
    public static int loadIntProperty(String name) {
        return config.getInt(name);
    }
}
