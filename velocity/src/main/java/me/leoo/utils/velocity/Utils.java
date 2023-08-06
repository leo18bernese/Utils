package me.leoo.utils.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.leoo.utils.common.compatibility.SoftwareManager;
import me.leoo.utils.common.compatibility.SoftwareUtils;
import me.leoo.utils.velocity.software.Software;
import org.slf4j.Logger;

@Getter
@Plugin(id = "utils-velocity", name = "Utils-Velocity", version = "${project.version}",
        description = "Utils methods for velocity plugins by itz_leoo", authors = {"itz_leoo"})
public class Utils {

    private static Utils plugin;

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public Utils(ProxyServer server, Logger logger) {
        plugin = this;

        this.server = server;
        this.logger = logger;
    }

    /**
     * Initialize utils.
     * Must be executed before running anything related to this plugin.
     */
    public static void initialize(){
        SoftwareManager.setUtils(new Software());
    }

    public static Utils get() {
        return plugin;
    }
}
