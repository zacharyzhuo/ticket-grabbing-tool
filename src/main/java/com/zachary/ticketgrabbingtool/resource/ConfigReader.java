package com.zachary.ticketgrabbingtool.resource;

import java.util.ResourceBundle;

public class ConfigReader {

    private static ConfigReader reader;
    private ResourceBundle resourceBundle;
    private ExternalConfigProvider externalConfigProvider;

    private ConfigReader() {
        // resource 底下的 .properties 檔
        resourceBundle = ResourceBundle.getBundle("application");
    }

    static {
        reader = new ConfigReader();

        // 讀 application.properties 中的 config.provider 變數
        String provider = reader.resourceBundle.getString("config.provider");

        if (provider.equals("local")) {
            reader.setExternalConfigProvider(new LocalFileConfigProvider());
        }
    }

    public static String getString(String key) {
        if (reader.resourceBundle.containsKey(key)) {
            return reader.resourceBundle.getString(key);
        }
        return null;
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

    public static String getStringFromExternalProvider(String key) {
        if (reader.externalConfigProvider != null) {
            return reader.externalConfigProvider.getString(key);
        }
        return null;
    }

    public void setExternalConfigProvider(ExternalConfigProvider externalConfigProvider) {
        this.externalConfigProvider = externalConfigProvider;
    }

}
