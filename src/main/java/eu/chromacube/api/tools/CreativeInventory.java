package eu.chromacube.api.tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
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
public class CreativeInventory {

    private Inventory inventory;
    private JavaPlugin javaPlugin;
    private static Map<Player, CreativeInventoryData> creativeinventory;
    private static Map<Class<? extends ICreativeInventory>, ICreativeInventory> creativeinventorys;
    private CreativeInventoryData creativeInventoryData;


    public CreativeInventory(){}

    public CreativeInventory(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        creativeinventory = new HashMap<>();
        creativeinventorys = new HashMap<>();
    }

    public void init(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CreativeListeners(), javaPlugin);
    }

    /**
     * Create the inventory with a name and size.
     * @param name
     * @param size
     */
    public CreativeInventory(String name, int size) {
        inventory = Bukkit.createInventory(null, size, name);
        creativeInventoryData = new CreativeInventoryData();
    }

    /**
     * Adds the item to the inventory.
     * @param itemStack
     * @return
     */
    public CreativeInventory addItem(ItemStack itemStack){
        inventory.addItem(itemStack);
        return this;
    }

    /**
     * Adds the item to the inventory and gives it an action.
     * @param itemStack
     * @param eventConsumer
     * @return
     */
    public CreativeInventory addItem(ItemStack itemStack, Consumer<InventoryClickEvent> eventConsumer){
        inventory.addItem(itemStack);
        creativeInventoryData.getItemEventconsumer().put(itemStack, eventConsumer);
        return this;
    }

    /**
     * Adds the item to the inventory in a specific slot.
     * @param slot
     * @param itemStack
     * @return
     */
    public CreativeInventory setItem(int slot, ItemStack itemStack){
        inventory.setItem(slot, itemStack);
        return this;
    }

    /**
     * Adds the item to the inventory in a specific slot and gives it an action.
     * @param slot
     * @param itemStack
     * @param eventConsumer
     * @return
     */
    public CreativeInventory setItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> eventConsumer){
        inventory.setItem(slot, itemStack);
        creativeInventoryData.getItemEventconsumer().put(itemStack, eventConsumer);
        return this;
    }

    /**
     * Place a horizontal line of item in the inventory.
     * @param itemStack
     * @param slotfrom
     * @param slotto
     * @return
     */
    public CreativeInventory setHorizontalLine(ItemStack itemStack, int slotfrom, int slotto) {
        for (int i = slotfrom; i <= slotto; i++) {
            inventory.setItem(i, itemStack);
        }
        return this;
    }

    /**
     * Place a vertical line of item in the inventory.
     * @param itemStack
     * @param slotfrom
     * @param slotto
     * @return
     */
    public CreativeInventory setVerticalLine(ItemStack itemStack, int slotfrom, int slotto) {
        for (int i = slotfrom; i <= slotto; i += 9) {
            inventory.setItem(i, itemStack);
        }
        return this;
    }

    /**
     * Defined an action has a slot in the inventory
     * @param slot
     * @param eventConsumer
     * @return
     */
    public CreativeInventory setAction(int slot, Consumer<InventoryClickEvent> eventConsumer){
        creativeInventoryData.getSloteventconsumer().put(slot, eventConsumer);
        return this;
    }

    /**
     * Open gui for the player
     * @param player
     */

    public void open(Player player){
        player.openInventory(inventory);
        creativeinventory.put(player, creativeInventoryData);
    }

    /**
     *
     * @param player
     * @param gClass
     */
    public void open(Player player, Class<? extends ICreativeInventory> gClass){
        if(!creativeinventorys.containsKey(gClass)){
            System.err.print("Please register your class "+gClass.toString());
            return;
        }
        ICreativeInventory iCreativeInventory = creativeinventorys.get(gClass);
        CreativeInventory inv = new CreativeInventory(iCreativeInventory.name(), iCreativeInventory.size());
        iCreativeInventory.contents(inv);
        inv.open(player);

    }

    /**
     * Close gui for the player
     * @param player
     */
    public void close(Player player){
        player.closeInventory();
    }

    /**
     * Register GUi
     * @param iCreativeInventory
     */

    public void registerMenu(ICreativeInventory iCreativeInventory){
        creativeinventorys.put(iCreativeInventory.getClass(), iCreativeInventory);
    }

    private class CreativeListeners implements Listener {
        @EventHandler
        public void onclick(InventoryClickEvent event){
            Player player = (Player) event.getWhoClicked();
            ItemStack itemStack = event.getCurrentItem();
            int slot = event.getSlot();
            if(!creativeinventory.containsKey(player)) return;

            event.setCancelled(true);

            CreativeInventoryData inventoryData = creativeinventory.get(player);
            if(inventoryData.getItemEventconsumer().containsKey(itemStack)){
                inventoryData.getItemEventconsumer().get(itemStack).accept(event);
                return;
            }
            if(inventoryData.getSloteventconsumer().containsKey(slot)){
                inventoryData.getSloteventconsumer().get(slot).accept(event);
            }

        }


        @EventHandler
        public void onclose(InventoryCloseEvent event){
            Player player = (Player) event.getPlayer();
            creativeinventory.remove(player);
        }

        @EventHandler
        public void ondisable(PluginDisableEvent event){
            creativeinventory.forEach((key, value) -> key.closeInventory());
        }
    }

    private class CreativeInventoryData{

        private Map<ItemStack, Consumer<InventoryClickEvent>> itemeventconsumer;
        private Map<Integer, Consumer<InventoryClickEvent>> sloteventconsumer;

        public CreativeInventoryData() {
            this.itemeventconsumer = new HashMap<>();
            this.sloteventconsumer = new HashMap<>();
        }

        public Map<ItemStack, Consumer<InventoryClickEvent>> getItemEventconsumer() {
            return itemeventconsumer;
        }

        public Map<Integer, Consumer<InventoryClickEvent>> getSloteventconsumer() {
            return sloteventconsumer;
        }
    }

    public interface ICreativeInventory{
        public abstract String name();
        public abstract int size();
        public abstract void contents(CreativeInventory inv);
    }

}
