package net.conorsmine.com.kubaanticheese.cmd;

import net.conorsmine.com.kubaanticheese.KubaAntiCheese;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/*
* This class acts as the main command for the
* plugin.
* */
public class PluginCommand implements CommandExecutor {

    private final KubaAntiCheese pl;
    private static final Map<String, CommandExecutor> subCommandMap = new HashMap<>();

    public PluginCommand(KubaAntiCheese pl) {
        this.pl = pl;

        subCommandMap.put("add", new AddCommand(pl));
        subCommandMap.put("reload", new ReloadCommand(pl));
        subCommandMap.put("remove", new RemoveCommand(pl));
        subCommandMap.put("list", new ListCommand(pl));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !subCommandMap.containsKey(args[0])) { sendUsageMsg(sender); return false; }

        return subCommandMap.get(args[0]).onCommand(sender, command, label, args);
    }

    private void sendUsageMsg(CommandSender sender) {
        sender.sendMessage(String.format("§7§m        §r %s§7 §m        §r", pl.getPrefix()));
        sender.sendMessage(String.format("%s §7The following commands are available:§r", pl.getPrefix()));

        for (String s : subCommandMap.keySet())
            sender.sendMessage(String.format("%s  §7>>§b%s§r", pl.getPrefix(), s));
    }
}
