package eu.chromacube.api.tools;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author _RealAlpha_
 * Discord: _RealAlpha_#1910
 * github: @RealAlphaDEV
 * 04/03/2019
 */
public class CreativeItems {

    private ItemStack is;
    private JavaPlugin javaPlugin;
    private static Map<ItemStack, Consumer<PlayerInteractEvent>> creativeitem;

    public CreativeItem(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        creativeitem = new HashMap<>();
    }

    public CreativeItem(Material m) {
        this(m, 1);
    }

    public CreativeItem(ItemStack is) {
        this.is = is;
    }

    public CreativeItem(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public CreativeItem(Material m, int amount, short meta){
        is = new ItemStack(m, amount, meta);
    }

    public CreativeItem clone() {
        return new CreativeItem(is);
    }

    public CreativeItem setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    public CreativeItem setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    public CreativeItem addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public CreativeItem removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public CreativeItem setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public CreativeItem addEnchant(Enchantment ench, int level, boolean show) {
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, show);
        is.setItemMeta(im);
        return this;
    }

    public CreativeItem setInfinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public CreativeItem setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    public CreativeItem setColor(DyeColor color) {
        if (is.getType() != Material.WOOL && is.getType() != Material.STAINED_GLASS_PANE && is.getType() != Material.STAINED_GLASS && is.getType() != Material.CLAY)
            return this;
        this.is.setDurability(color.getData());
        return this;
    }

    public CreativeItem setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }


    public CreativeItem setCustomHead(String name) {
        ItemMeta headM = this.is.getItemMeta();
        try {
            Field field = headM.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            PropertyMap propertyMap = profile.getProperties();
            byte[] encodedData = name.getBytes();
            propertyMap.put("textures", new Property("textures", new String(encodedData)));
            field.set(headM, profile);
        }
        catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e) {
            e.printStackTrace();
        }
        this.is.setItemMeta(headM);
        return this;
    }

    public CreativeItem setAction(Consumer<PlayerInteractEvent> consumer){
        creativeitem.put(is, consumer);
        return this;
    }

    public void init(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CreativeListeners(), javaPlugin);
    }

    public ItemStack toItemStack() {
        return this.is;
    }



    private class CreativeListeners implements Listener {
        @EventHandler
        public void onclick(PlayerInteractEvent event){
            ItemStack item = event.getItem();
            if(item == null)return;
            if(!creativeitem.containsKey(item))return;
            creativeitem.get(item).accept(event);
        }

        @EventHandler
        public void ondisable(PluginDisableEvent event){
            Bukkit.getOnlinePlayers().forEach(pls -> pls.kickPlayer(ChatColor.RED+"The server restarts"));
        }
    }

}