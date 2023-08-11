package me.leoo.utils.common.file;

import me.leoo.utils.common.compatibility.SoftwareManager;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File generateFile(String name, String directory) {
        File file = new File(generateFolder(directory), name);

        if (!file.exists()) {
            SoftwareManager.getUtils().info("Creating file " + file.getPath());
            try {
                if (!file.createNewFile()) {
                    SoftwareManager.getUtils().severe("Could not create " + file.getPath());
                    return null;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return file;
    }

    public static File generateFolder(String name) {
        File folder = new File(name);

        if (!folder.exists()) {
            SoftwareManager.getUtils().info("Creating folder " + folder.getPath());
            if (!folder.mkdirs()) {
                SoftwareManager.getUtils().severe("Could not create " + folder.getPath());
                return null;
            }
        }

        return folder;
    }
}
