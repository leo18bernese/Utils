package me.leoo.utils.common.file;

import me.leoo.utils.common.compatibility.SoftwareManager;
import sun.plugin2.main.server.Plugin;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File generateFile(String name, String dir) {
        File folder = new File(dir);

        if (!folder.exists()) {
            SoftwareManager.getUtils().info("Creating folder " + folder.getPath());
            if (!folder.mkdirs()) {
                SoftwareManager.getUtils().severe("Could not create " + folder.getPath());
                return null;
            }
        }

        File file = new File(folder, name);

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
}
