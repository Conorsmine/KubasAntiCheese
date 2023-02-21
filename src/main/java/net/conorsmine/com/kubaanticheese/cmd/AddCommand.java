package net.conorsmine.com.kubaanticheese.cmd;

import net.conorsmine.com.kubaanticheese.KubaAntiCheese;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AddCommand implements CommandExecutor {

    private final KubaAntiCheese pl;
    private static final String BLACKLISTED_PATH = "blacklist";

    public AddCommand(KubaAntiCheese pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("AntiCheat.add")) { pl.sendNonPermMessage(sender); return false; }
        if (args.length == 1 && !(sender instanceof Player)) { sendUsageMsg(sender); return false; }


        if (args.length > 1) addItemToBlacklist(args);
        else if (sender instanceof Player) {
            if (!handlePlayer((Player) sender))
                return false;
        }

        pl.saveConfig();
        String materialName = (args.length > 1) ? args[1] : ((Player) sender).getInventory().getItemInMainHand().getType().name();
        sender.sendMessage(String.format("%s §aAdded \"§b%s§a\" to the blacklisted items.§r", pl.getPrefix(), materialName));
        sender.sendMessage(String.format("%s §7Use §b/kb reload §7to reload the plugin and apply the changes.§r", pl.getPrefix()));
        return true;
    }

    private void addItemToBlacklist(String[] args) {
        List<String> blacklisted = pl.getConf().getStringList(BLACKLISTED_PATH);

        if (args.length == 2) blacklisted.add(args[1]);
        else blacklisted.add(String.format("%s#%s", args[1], args[2]));

        pl.getConf().set(BLACKLISTED_PATH, blacklisted);
    }

    private boolean handlePlayer(Player p) {
        final ItemStack item = p.getInventory().getItemInMainHand();
        List<String> blacklisted = pl.getConf().getStringList(BLACKLISTED_PATH);

        if (item != null && item.getType() != Material.AIR) {
            blacklisted.add(item.getType().name());
            pl.getConf().set(BLACKLISTED_PATH, blacklisted);
            return true;
        }

        p.sendMessage(String.format("%s §7Hold an item in hand to use this command without arguments.§r", pl.getPrefix()));
        return false;
    }

    private void sendUsageMsg(CommandSender sender) {
        sender.sendMessage(String.format("%s §7Usages for the §6[§5Add§6] §7command:§r", pl.getPrefix()));
        sender.sendMessage("%s §b/kb add §3<MaterialName> <Durability>§r");
    }
}
