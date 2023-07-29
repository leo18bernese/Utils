package me.leoo.utils.file;

import me.leoo.utils.Utils;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File generateFile(String name, String dir) {
        File folder = new File(dir);

        if (!folder.exists()) {
            Utils.get().getLogger().info("Creating " + folder.getPath());
            if (!folder.mkdirs()) {
                Utils.get().getLogger().severe("Could not create " + folder.getPath());
                return null;
            }
        }

        File file = new File(dir, name);

        if (!file.exists()) {
            Utils.get().getLogger().info("Creating " + file.getPath());
            try {
                if (!file.createNewFile()) {
                    Utils.get().getLogger().severe("Could not create " + file.getPath());
                    return null;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return file;
    }
}
