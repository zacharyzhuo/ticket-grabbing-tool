package com.zachary.ticketgrabbingtool.resource;

import java.util.ResourceBundle;

public class ConfigReader {
    private static ConfigReader reader;

    static {
        reader = new ConfigReader();

        String provider = reader.resourceBundle.getString("config.provider");

        if (provider.equals("local")) {
            reader.setExternalConfigProvider(new LocalFileConfigProvider());
        }
//		else if (provider.equals("db")) {
//			reader.setExternalConfigProvider(
//					ApplicationContextProvider.getApplicationContext().getBean(DBConfigProvider.class));
//		}
    }

    public static String getString(CONSTANT key) {
        return getString(key.toString());
    }

    public static int getInteger(CONSTANT key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean getBoolean(CONSTANT key) {
        try {
            return Boolean.parseBoolean(getString(key));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(CONSTANT key, boolean isExternal) {
        return isExternal ? getStringFromExternalProvider(key.toString()) : getString(key.toString());
    }

    public static int getInteger(CONSTANT key, boolean isExternal) {
        try {
            return Integer
                    .parseInt(isExternal ? getStringFromExternalProvider(key.toString()) : getString(key.toString()));
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean getBoolean(CONSTANT key, boolean isExternal) {
        try {
            return Boolean.parseBoolean(
                    isExternal ? getStringFromExternalProvider(key.toString()) : getString(key.toString()));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(String key) {
        if (reader.resourceBundle.containsKey(key)) {
            return reader.resourceBundle.getString(key);
        }

        return null;
    }

    public static String getStringFromExternalProvider(String key) {
        if (reader.externalConfigProvider != null) {
            return reader.externalConfigProvider.getString(key);
        }

        return null;
    }

    private ResourceBundle resourceBundle;
    private ExternalConfigProvider externalConfigProvider;

    private ConfigReader() {
        resourceBundle = ResourceBundle.getBundle("application");
    }

    public void setExternalConfigProvider(ExternalConfigProvider externalConfigProvider) {
        this.externalConfigProvider = externalConfigProvider;
    }
}
