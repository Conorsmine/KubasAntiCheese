package net.conorsmine.com.kubaanticheese;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class EnchantRemover {

    private final KubaAntiCheese pl;
    private final Set<Enchantment> enchantSet = new HashSet<>();
    private final Map<Material, Short> blacklistedMaterials = new HashMap<>();
    private String removalMsg;
    private BukkitTask task = null;


    public EnchantRemover(KubaAntiCheese pl) {
        this.pl = pl;

        reloadRemover(pl.getServer().getConsoleSender());
    }

    public void startChecker() {
        if (task != null) task.cancel();
        task = Bukkit.getScheduler().runTaskTimer(pl, this::runChecker, 100L, 100L);
    }

    public void stopChecker() {
        if (task == null) return;
        task.cancel();
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
            if (item == null || item.getType() == Material.AIR) continue;
            if (isItemBlacklisted(item)) continue;
            if (clearEnchantmentFromItem(item))
                sendRemovalMessage(p);
        }
    }

    private boolean clearEnchantmentFromItem(ItemStack item) {
        boolean found = false;
        final ItemMeta it = item.getItemMeta();
        for (Enchantment enchantment : enchantSet) {
            it.removeEnchant(enchantment);

            if (it.getEnchants().containsKey(enchantment))
                found = true;
        }

        item.setItemMeta(it);
        return found;
    }

    private boolean isItemBlacklisted(final ItemStack item) {
        if (!blacklistedMaterials.containsKey(item.getType())) return false;
        return (item.getDurability() == blacklistedMaterials.get(item.getType()));
    }

    private void sendRemovalMessage(Player p) {
        p.sendMessage(String.format(removalMsg, pl.getPrefix()));
    }



    public void reloadRemover(CommandSender sender) {
        setEnchantIDs(pl.getConf().getIntegerList("enchants"));
        setBlacklistedMaterials(pl.getConf().getStringList("blacklist"), sender);
        setRemovalMsg(pl.getConf().getString("enchRemoveMessage"));
    }

    private void setEnchantIDs(Collection<Integer> iDs) {
        enchantSet.clear();
        iDs.forEach(id -> enchantSet.add(new EnchantmentWrapper(id)));
    }

    private void setBlacklistedMaterials(Collection<String> materialNames, CommandSender sender) {
        blacklistedMaterials.clear();

        for (String mat : materialNames) {
            if (StringUtils.isBlank(mat)) continue;

            String matName = getMatName(mat);
            short matDurability = getDurability(mat);

            final Material material = Material.getMaterial(matName);
            if (material == null) { sendNonMaterialErr(sender, matName); continue; }
            blacklistedMaterials.put(material, matDurability);
        }
    }

    private String getMatName(String mat) {
        return mat.replaceAll("#\\d*", "").toUpperCase(Locale.ROOT);
    }

    private short getDurability(String mat) {
        final String data = mat.replaceFirst("\\w+#", "");

        if (StringUtils.isBlank(data) || !data.matches("\\d+")) return 0;
        return Short.parseShort(data);
    }

    private void setRemovalMsg(String msg) {
        if (msg == null || StringUtils.isBlank(msg))
            msg = "%s &cRemoved illegal enchantments!&r";

        removalMsg = msg.replaceAll("&", "§");
    }

    public BukkitTask getTask() {
        return task;
    }

    public void printBlacklisted(CommandSender sender) {
        sender.sendMessage(String.format("§7§m--------§r  %s  §7§m--------§r", pl.getPrefix()));
        if (blacklistedMaterials.isEmpty())
            sender.sendMessage(String.format("%s §7No items are blacklisted.", pl.getPrefix()));
        else
            sender.sendMessage(String.format("%s §7The following items will not have their enchants removed:", pl.getPrefix()));

        for (Map.Entry<Material, Short> entry : blacklistedMaterials.entrySet()) {
            if (entry.getValue() == 0)
                sender.sendMessage(String.format("%s    §7>>§b%s", pl.getPrefix(), entry.getKey().name()));
            else
                sender.sendMessage(String.format("%s    §7>>§b%s: §3%s", pl.getPrefix(), entry.getKey().name(), entry.getValue()));
        }
    }

    private void sendNonMaterialErr(CommandSender sender, String materialName) {
        sender.sendMessage(String.format("%s §7\"§b%s§7\" §cis not a valid material!§r", pl.getPrefix(), materialName));
    }
}
