package me.leoo.utils.velocity;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Data;
import lombok.Getter;
import me.leoo.utils.common.compatibility.SoftwareManager;
import me.leoo.utils.velocity.software.Software;
import org.slf4j.Logger;

@Data
@Plugin(id = "utils-velocity", name = "Utils-Velocity", version = "${project.version}",
        description = "Utils methods for velocity plugins by itz_leoo", authors = {"itz_leoo"})
public class Utils {

    @Getter
    private static final Utils instance = new Utils();

    private ProxyServer server;
    private Logger logger;

    /**
     * Initialize utils.
     * Must be executed before running anything related to this plugin.
     */
    public static void initialize(ProxyServer proxyServer, Logger proxyLogger) {
        instance.setServer(proxyServer);
        instance.setLogger(proxyLogger);

        SoftwareManager.setUtils(new Software());
    }
}
