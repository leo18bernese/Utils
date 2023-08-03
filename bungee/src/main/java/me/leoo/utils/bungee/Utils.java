package me.leoo.utils.bungee;

import me.leoo.utils.bungee.software.Software;
import me.leoo.utils.common.compatibility.SoftwareUtils;
import net.md_5.bungee.api.plugin.Plugin;

public class Utils extends Plugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        SoftwareUtils.setInstance(new Software());
    }

    public static Plugin get() {
        /*if (plugin == null) {
            plugin = JavaPlugin.getProvidingPlugin(Utils.class);
        }*/
        return plugin;
    }
}
