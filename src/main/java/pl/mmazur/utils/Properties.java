package pl.mmazur.utils;

import java.util.ResourceBundle;

public class Properties {
    public static String getProperty(String propertyName){
        return ResourceBundle.getBundle("application").getString(propertyName);
    }
}
