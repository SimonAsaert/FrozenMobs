package frozenmobs.inventory;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import frozenmobs.Main;

public class CustomInventory {

	private Plugin plugin = Main.getPlugin(Main.class);
	

	private ItemStack createItem(ItemStack item, String disName, String loreName) {

		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(ChatColor.WHITE + disName);
		ArrayList<String> itemL = new ArrayList<String>();
		itemL.add(ChatColor.GRAY + "" + ChatColor.ITALIC + loreName);
		itemM.setLore(itemL);
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(itemM);

		return item;
	}

	public void mainInventoryMenu(Player player, Entity entity){
		Inventory i = plugin.getServer().createInventory(null, 9, ChatColor.DARK_BLUE + "FrozenMob GUI");
		
		ItemStack kill = createItem(new ItemStack(Material.SKULL_ITEM, 1, (short) 0), "Remove Entity", "Removes entity");
		ItemStack move = createItem(new ItemStack(Material.STONE, 1), "Move Entity", "Moves entity. Current Pos: " + entity.getLocation().getX() + "," + entity.getLocation().getY() + "," + entity.getLocation().getZ());
		ItemStack rotate = createItem(new ItemStack(Material.WOOD, 1), "Rotate Entity", "Rotates entity. Current Yaw: " + entity.getLocation().getYaw());
		ItemStack type = createItem(new ItemStack(Material.MONSTER_EGGS, 1), "Change Type", "Current type: " + entity.getType().toString()); 
		
		i.setItem(0, move);
		i.setItem(1, rotate);
		i.setItem(2, type);
		i.setItem(8, kill);
		
		player.openInventory(i);
	}
	
	public void entityInventoryMenu(Player player, Entity entity) {
		Inventory i = plugin.getServer().createInventory(null, 45, ChatColor.DARK_BLUE + "FrozenMob type GUI");
		
		ItemStack back = createItem(new ItemStack(Material.BARRIER, 1), "Return to main menu", "");
		// Passive/Neutral mobs
		ItemStack bat = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)3), "Bat", "");
		ItemStack chicken = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)2), "Chicken", "");
		ItemStack cow = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)1), "Cow", "");
		ItemStack donkey = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)4), "Donkey", "");
		ItemStack horse = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)5), "Horse", "");
		ItemStack irongolem = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)16), "Iron Golem", "");
		ItemStack llama = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)17), "Llama", "");
		ItemStack mooshroom = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)6), "Mooshroom", "");
		ItemStack mule = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)7), "Mule", "");
		ItemStack ocelot = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)8), "Ocelot", "");
		ItemStack parrot = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)9), "Parrot", "");
		ItemStack pig = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)10), "Pig", "");
		ItemStack rabbit = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)11), "Rabbit", "");
		ItemStack sheep = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)12), "Sheep", "");
		ItemStack skeletonhorse = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)13), "Skeleton Horse", "");
		ItemStack squid = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)14), "Squid", "");
//		ItemStack villager = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)15), "Villager", "");
		//Monsters
		ItemStack blaze = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)17), "Blaze", "");
		ItemStack cavespider = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)16), "Cave Spider", "");
		ItemStack creeper = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)18), "Creeper", "");
		ItemStack elderguardian = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)19), "Elder Guardian", "");
		ItemStack enderman = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)20), "Enderman", "");
		ItemStack endermite = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)21), "Endermite", "");
		ItemStack envoker = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)22), "Evoker", "");
		ItemStack ghast = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)23), "Ghast", "");
		ItemStack guardian = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)24), "Guardian", "");
		ItemStack husk = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)25), "Husk", "");
		ItemStack magmacube = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)26), "Magma Cube", "");
		ItemStack shulker = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)27), "Shulker", "");
		ItemStack silverfish = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)28), "Silverfish", "");
		ItemStack skeleton = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)29), "Skeleton", "");
		ItemStack spider = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)30), "Spider", "");
		ItemStack slime = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)31), "Slime", "");
		ItemStack stray = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)32), "Stray", "");
		ItemStack vex = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)33), "Vex", "");
		ItemStack vindicator = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)34), "Vindicator", "");
		ItemStack witch = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)35), "Witch", "");
		ItemStack witherskeleton = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)36), "Wither Skeleton", "");
		ItemStack zombie = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)37), "Zombie", "");
		ItemStack zombievillager = createItem(new ItemStack(Material.MONSTER_EGG, 1, (short)38), "Zombie Villager", "");
		
		i.setItem(0,  bat);
		i.setItem(1, chicken);
		i.setItem(2, cow);
		i.setItem(3, donkey);
		i.setItem(4, horse);
		i.setItem(5, irongolem);
		i.setItem(6, llama);
		i.setItem(7, mooshroom);
		i.setItem(8, mule);
		i.setItem(9, ocelot);
		i.setItem(10, parrot);
		i.setItem(11, pig);
		i.setItem(12, rabbit);
		i.setItem(13, sheep);
		i.setItem(14, skeletonhorse);
		i.setItem(15, squid);
//		i.setItem(16, villager);
		
		i.setItem(18, blaze);
		i.setItem(19, cavespider);
		i.setItem(20, creeper);
		i.setItem(21, elderguardian);
		i.setItem(22, enderman);
		i.setItem(23, endermite);
		i.setItem(24, envoker);
		i.setItem(25, ghast);
		i.setItem(26, guardian);
		i.setItem(27, husk);
		i.setItem(27, magmacube);
		i.setItem(28, shulker);
		i.setItem(29, silverfish);
		i.setItem(30, skeleton);
		i.setItem(31, spider);
		i.setItem(32, slime);
		i.setItem(33, stray);
		i.setItem(34, vex);
		i.setItem(35, vindicator);
		i.setItem(36, witch);
		i.setItem(37, witherskeleton);
		i.setItem(38, zombie);
		i.setItem(39, zombievillager);
		
		
		i.setItem(44, back);
		
		player.openInventory(i);
	}
}
