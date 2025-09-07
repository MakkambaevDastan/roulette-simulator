package utils;

import constants.Configurations;

import java.io.File;

public class FileHelper {

    public static File getSettingFile(String fileName) {
        File file = new File(Configurations.SETTING_FILE_DIRECTORY, fileName);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                System.err.println(" ");
            }
        }
        return file;
    }
}