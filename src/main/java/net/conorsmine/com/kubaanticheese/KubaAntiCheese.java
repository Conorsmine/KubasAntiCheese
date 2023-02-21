package net.conorsmine.com.kubaanticheese;

import net.conorsmine.com.kubaanticheese.cmd.ReloadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class KubaAntiCheese extends JavaPlugin {

    public final String prefix = "§6[§5AntiCheese§6]§r";
    private final EnchantRemover remover = new EnchantRemover(this);
    private FileConfiguration conf;

    @Override
    public void onEnable() {
        setupFiles();
        remover.initEnchantIDs();
        remover.startChecker();
        this.getCommand("reloadAntiCheat").setExecutor(new ReloadCommand(this));
        getLogger().info(String.format("%s §aWas enabled successfully.", prefix));
    }

    @Override
    public void onDisable() {
        remover.getTask().cancel();
    }

    public void setupFiles() {
        saveDefaultConfig();
        conf = getConfig();
    }

    public void sendNonPermMessage(CommandSender sender) {
        sender.sendMessage(String.format("%s §cYou do not have permission to use this command!§r", prefix));
    }

    public FileConfiguration getConf() {
        return conf;
    }

    public EnchantRemover getRemover() {
        return remover;
    }

    public String getPrefix() {
        return prefix;
    }
}
