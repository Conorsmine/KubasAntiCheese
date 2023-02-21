package net.conorsmine.com.kubaanticheese.cmd;

import net.conorsmine.com.kubaanticheese.KubaAntiCheese;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

public class RemoveCommand implements CommandExecutor {

    private final KubaAntiCheese pl;
    private static final String BLACKLISTED_PATH = "blacklist";

    public RemoveCommand(KubaAntiCheese pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("AntiCheat.remove")) { pl.sendNonPermMessage(sender); return false; }
        if (args.length < 2) { sendUsageMsg(sender); return false; }


        removeItem(args[1].toLowerCase(Locale.ROOT));
        sender.sendMessage(String.format("%s §7Removed \"§b%s§7\" from the blacklist.", pl.getPrefix(), args[1]));
        sender.sendMessage(String.format("%s §7Use §3/kb reload §7to apply the changes.", pl.getPrefix()));
        sender.sendMessage(String.format("%s §7Use §3/kb list §7to view all currently blacklisted items.", pl.getPrefix()));
        return true;
    }

    private void removeItem(String arg) {
        final List<String> stringList = pl.getConf().getStringList(BLACKLISTED_PATH);
        stringList.removeIf(s -> s.toLowerCase(Locale.ROOT).equals(arg));
        pl.getConf().set(BLACKLISTED_PATH, stringList);
        pl.saveConfig();
    }

    private void sendUsageMsg(CommandSender sender) {
        sender.sendMessage(String.format("%s §7Usages for the §6[§5Remove§6] §7command:§r", pl.getPrefix()));
        sender.sendMessage(String.format("%s §b/kb remove §3<name>§r", pl.getPrefix()));
        sender.sendMessage(String.format("%s §7Note that \"§3<name>§7\" also has to include the data!§r", pl.getPrefix()));
    }
}
