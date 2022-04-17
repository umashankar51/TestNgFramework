package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    private static Properties envVariables;
    private static Properties testVariables;

    public static boolean loadTestConfig() {
        try(InputStream is = ClassLoader.getSystemClassLoader().getResource("test.properties").openStream()) {
            testVariables = new Properties();
            testVariables.load(is);
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getTestVariable(String propertyName) {
        if(System.getProperty(propertyName) != null){
           return System.getProperty(propertyName);
        }
        if(testVariables.containsKey(propertyName)){
            return String.valueOf(testVariables.get(propertyName));
        }
        return null;
    }

    public static boolean loadEnvConfig() {
        try(InputStream is = ClassLoader.getSystemClassLoader().getResource("configs/"+getTestVariable("env")
                +".properties").openStream()) {
            envVariables = new Properties();
            envVariables.load(is);
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getEnvVariable(String propertyName) {
        if(envVariables.containsKey(propertyName)){
            return String.valueOf(envVariables.get(propertyName));
        }
        return null;
    }
}
