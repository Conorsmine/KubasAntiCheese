package net.conorsmine.com.kubaanticheese;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class EnchantRemover {

    private final KubaAntiCheese pl;
    private final Set<Integer> enchantIdSet = new HashSet<>();
    private String removalMsg;
    private BukkitTask task = null;


    public EnchantRemover(KubaAntiCheese pl) {
        this.pl = pl;
        setRemovalMsg(pl.getConf().getString("EnchRemoveMessage"));
    }

    public void startChecker() {
        enchantIdSet.clear();
        initEnchantIDs();

        if (task != null) task.cancel();
        task = Bukkit.getScheduler().runTaskTimer(pl, this::runChecker, 100L, 100L);
    }

    private void runChecker() {
        List<Player> currentPlayers = new LinkedList<>(Bukkit.getOnlinePlayers());
        for (Player p : currentPlayers) {
            if (!p.isOnline()) continue;

            clearEnchantmentFromPlayer(p);
        }
    }

    private void clearEnchantmentFromPlayer(Player p) {
        for (ItemStack item : p.getInventory()) {
            if (clearEnchantmentFromItem(item))
                sendRemovalMessage(p);
        }
    }

    private boolean clearEnchantmentFromItem(ItemStack item) {
        boolean found = false;
        final ItemMeta it = item.getItemMeta();
        for (int enchId : enchantIdSet) {
            final Enchantment enchantment = new EnchantmentWrapper(enchId).getEnchantment();
            it.removeEnchant(enchantment);

            if (it.getEnchants().containsKey(enchantment))
                found = true;
        }

        item.setItemMeta(it);
        return found;
    }

    private void sendRemovalMessage(Player p) {
        p.sendMessage(String.format(removalMsg, pl.getPrefix()));
    }



    public void initEnchantIDs() {
        enchantIdSet.addAll(pl.getConf().getIntegerList("Enchants"));
    }

    public void setRemovalMsg(String msg) {
        if (msg == null || StringUtils.isBlank(msg))
            msg = "%s &cRemoved illegal enchantments!&r";

        removalMsg = msg.replaceAll("&", "§");
    }

    public BukkitTask getTask() {
        return task;
    }
}
