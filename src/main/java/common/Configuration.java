/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Ngan Do
 */
public class Configuration {

    private static final String FILE_CONFIG = "/conf/development.config.properties";
//    private static final String FILE_CONFIG = "/conf/production.config.properties";

    private final static Properties PROPERTY = new Properties();

    public static String APP_DOMAIN = "";
    public static String STATIC_ADMIN_DOMAIN = "";
    public static String STATIC_CLIENT_DOMAIN = "";
    public static String UPLOAD_DIR = "";
    public static int SHIPPING_FEE = 20000;

    static {
        readConfig();

        APP_DOMAIN = getProperty("app_domain");
        STATIC_ADMIN_DOMAIN = getProperty("app_domain") + "/static/admin";
        STATIC_CLIENT_DOMAIN = getProperty("app_domain") + "/static/client";
    }

    /**
     * get property with key
     *
     * @param key
     * @return value of key
     */
    public static String getProperty(String key) {
        return PROPERTY.getProperty(key);
    }

    /**
     * read file .properties
     */
    private static void readConfig() {
        InputStream inputStream = null;
        try {
            String currentDir = System.getProperty("user.dir");
            inputStream = new FileInputStream(currentDir + FILE_CONFIG);
            PROPERTY.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close objects
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
