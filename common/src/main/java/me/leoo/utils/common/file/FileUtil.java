package me.leoo.utils.common.file;

import lombok.experimental.UtilityClass;
import me.leoo.utils.common.compatibility.SoftwareManager;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class FileUtil {

    public boolean exists(String name) {
        return new File(name).exists();
    }

    public boolean exists(String directory, String name) {
        return new File(directory, name).exists();
    }

    public File generateFile(String directory, String name) {
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

    public File generateFolder(String name) {
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
