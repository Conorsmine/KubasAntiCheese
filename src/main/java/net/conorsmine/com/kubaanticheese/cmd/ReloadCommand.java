package net.conorsmine.com.kubaanticheese.cmd;

import net.conorsmine.com.kubaanticheese.KubaAntiCheese;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final KubaAntiCheese pl;

    public ReloadCommand(KubaAntiCheese pl) {
        this.pl = pl;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("AntiCheat.reload")) {
            pl.sendNonPermMessage(commandSender);
            return false;
        }
        else {
            pl.reloadConfig();
            pl.setupFiles();
            pl.getRemover().reloadRemover(commandSender);
            pl.getRemover().printBlacklisted(commandSender);
            commandSender.sendMessage(String.format("%s §aSuccessfully reloaded the config.", pl.getPrefix()));
            return true;
        }
    }
}
