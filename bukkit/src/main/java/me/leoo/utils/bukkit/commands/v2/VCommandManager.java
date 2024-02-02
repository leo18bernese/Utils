package me.leoo.utils.bukkit.commands.v2;

import lombok.Getter;
import me.leoo.utils.bukkit.commands.v2.exception.CommandError;
import me.leoo.utils.bukkit.commands.v2.exception.VCommandError;

public class VCommandManager {

    @Getter
    private static VCommandError error;

    public VCommandManager(VCommandError commandError) {
        error = commandError;
    }

    public VCommandManager() {
        error = new CommandError();
    }

    public static void register(VCommandError vCommandError) {
        if (vCommandError == null) {
            error = new CommandError();
        } else {
            error = vCommandError;
        }
    }

    public static void register() {
        register(null);
    }
}
