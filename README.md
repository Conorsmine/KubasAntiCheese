# KubasAntiCheese
A spigot plugin to listen and manage specific violations.  
Currently, the plugin only provides the [EnchantRemover](##EnchantRemover) functionality.

## EnchantRemover
The plugin will remove any specified enchantments, found on items.
The `config.yml` file provides the options to specify certain *echantments*, by their id.  
But also a *blacklist* for items, which should not have their enchantments removed.

Example of a possible `config.yml` file:
```
# Enchant removal:
enchants: [34]
enchRemoveMessage: "%s &cRemoved illegal enchantments!&r"

# Blacklisted materials:
# All materials within this list will not be affected
# by the plugin.
# Use the following syntax to define the "damage" of an item:
#     stone#5 => Andesite
blacklist:
  - stone#5
```
