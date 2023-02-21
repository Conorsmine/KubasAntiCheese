package net.conorsmine.com.kubaanticheese.cmd;

import net.conorsmine.com.kubaanticheese.KubaAntiCheese;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListCommand implements CommandExecutor {

    private final KubaAntiCheese pl;

    public ListCommand(KubaAntiCheese pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("AntiCheat.list")) { pl.sendNonPermMessage(sender); return false; }

        pl.getRemover().printBlacklisted(sender);
        return true;
    }
}
