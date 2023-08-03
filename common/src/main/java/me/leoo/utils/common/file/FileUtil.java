package me.leoo.utils.common.file;

import me.leoo.utils.common.compatibility.SoftwareUtils;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static final SoftwareUtils utils = SoftwareUtils.getInstance();

    public static File generateFile(String name, String dir) {
        File folder = new File(dir);

        if (!folder.exists()) {
            utils.info("Creating " + folder.getPath());
            if (!folder.mkdirs()) {
                utils.severe("Could not create " + folder.getPath());
                return null;
            }
        }

        File file = new File(folder, name);

        if (!file.exists()) {
            utils.info("Creating " + file.getPath());
            try {
                if (!file.createNewFile()) {
                    utils.severe("Could not create " + file.getPath());
                    return null;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return file;
    }
}
