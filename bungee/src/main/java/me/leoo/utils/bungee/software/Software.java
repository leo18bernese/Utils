package me.leoo.utils.bungee.software;

import me.leoo.utils.bungee.chat.CC;
import me.leoo.utils.common.compatibility.SoftwareUtils;
import net.md_5.bungee.api.ProxyServer;

public class Software extends SoftwareUtils {

    @Override
    public void info(String text) {
        ProxyServer.getInstance().getLogger().info(text);
    }

    @Override
    public void warning(String text) {
        ProxyServer.getInstance().getLogger().warning(text);
    }

    @Override
    public void severe(String text) {
        ProxyServer.getInstance().getLogger().severe(text);
    }

    @Override
    public String color(String text) {
        return CC.color(text);
    }
}
