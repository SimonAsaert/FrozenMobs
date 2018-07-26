package frozenmobs.events;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Husk;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import frozenmobs.Main;
import frozenmobs.conversations.Move;
import frozenmobs.conversations.Rotate;
import frozenmobs.inventory.CustomInventory;


public class EventClass implements Listener {

	private Main plugin = Main.getPlugin(Main.class);

	HashMap<String, Boolean> delay = new HashMap<String, Boolean>();
	HashMap<Player, Entity> entiplay = new HashMap<Player, Entity>();

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (plugin.checkEntity(entity.getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityFire(EntityCombustEvent event) {
		Entity entity = event.getEntity();
		if (plugin.checkEntity(entity.getUniqueId())){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		
		if (player.getInventory().getItemInMainHand().equals(new ItemStack(Material.STICK))) {
			if((!delay.containsKey(player.toString()))){
				
				delay.put(player.toString(), true);
				Location loc = event.getClickedBlock().getLocation().clone();
				loc.add(0.5, 1, 0.5);
				Cow cow = (Cow) loc.getWorld().spawnEntity(loc, EntityType.COW);
				cow.setAI(false);
				
				plugin.addToDb(cow);
				
				new BukkitRunnable() {
					public void run() {
						delay.remove(player.toString());
					}
				}.runTaskLater(this.plugin, 5);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		String prefixMain = (ChatColor.DARK_BLUE + "FrozenMob GUI");
		String prefixEntity = (ChatColor.DARK_BLUE + "FrozenMob type GUI");
		Player player = (Player) event.getWhoClicked();
		Inventory open = event.getClickedInventory();
		
		if (open == null) {
			return;
		}else if (open.getName().equals(prefixMain)) {
			event.setCancelled(true);
			ItemStack clicked = event.getCurrentItem();
			Entity entity = entiplay.get(player);
			
			if (clicked == null) {
				return;
			}else if(clicked.getItemMeta() == null) {
				return;
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Remove Entity")) {
				plugin.removeEntity(entity);
				player.closeInventory();
				entity.remove();
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Move Entity")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				Move posconv = new Move();
				posconv.inputData(player, entity);
				Conversation conv = cf.withFirstPrompt(posconv).withLocalEcho(true).buildConversation(player);
				conv.begin();
			}else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Rotate Entity")){
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				Rotate rotconv = new Rotate();
				rotconv.inputData(player, entity);
				Conversation conv = cf.withFirstPrompt(rotconv).withLocalEcho(true).buildConversation(player);
				conv.begin();
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Change Type")) {
				player.closeInventory();
				CustomInventory i = new CustomInventory();
				i.entityInventoryMenu(player,entity);
			}

		}else if (open.getName().equals(prefixEntity)) {
			event.setCancelled(true);
			ItemStack clicked = event.getCurrentItem();
			Entity entity = entiplay.get(player);
			
			if (clicked == null) {
				return;
			}else if(clicked.getItemMeta() == null) {
				return;
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Bat")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "BAT")) {
					Bat newEntity = (Bat)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.BAT);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Chicken")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "CHICKEN")) {
					Chicken newEntity = (Chicken)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.CHICKEN);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Cow")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "COW")) {
					Cow newEntity = (Cow)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.COW);
					newEntity.setAI(false);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Donkey")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "DONKEY")) {
					Donkey newEntity = (Donkey)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.DONKEY);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Horse")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "HORSE")) {
					Horse newEntity = (Horse)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.HORSE);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Iron Golem")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "IRON_GOLEM")) {
					IronGolem newEntity = (IronGolem)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.IRON_GOLEM);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Llama")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "LLAMA")) {
					Llama newEntity = (Llama)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.LLAMA);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Mooshroom")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "MUSHROOM_COW")) {
					MushroomCow newEntity = (MushroomCow)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.MUSHROOM_COW);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Mule")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "MULE")) {
					Mule newEntity = (Mule)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.MULE);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Ocelot")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "OCELOT")) {
					Ocelot newEntity = (Ocelot)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.OCELOT);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Parrot")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "PARROT")) {
					Parrot newEntity = (Parrot)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.PARROT);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Pig")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "PIG")) {
					Pig newEntity = (Pig)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.PIG);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Rabbit")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "RABBIT")) {
					Rabbit newEntity = (Rabbit)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.RABBIT);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Sheep")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "SHEEP")) {
					Sheep newEntity = (Sheep)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SHEEP);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Skeleton Horse")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "COW")) {
					SkeletonHorse newEntity = (SkeletonHorse)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SKELETON_HORSE);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Squid")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "SQUID")) {
					Squid newEntity = (Squid)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SQUID);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Villager")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "VILLAGER")) {
					Villager newEntity = (Villager)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.VILLAGER);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Blaze")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "BLAZE")) {
					Blaze newEntity = (Blaze)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.BLAZE);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Cave Spider")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "CAVE_SPIDER")) {
					CaveSpider newEntity = (CaveSpider)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.CAVE_SPIDER);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Creeper")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "CREEPER")) {
					Creeper newEntity = (Creeper)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.CREEPER);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Elder Guardian")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "VILLAGER")) {
					ElderGuardian newEntity = (ElderGuardian)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.ELDER_GUARDIAN);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Enderman")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "ENDERMAN")) {
					Enderman newEntity = (Enderman)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.ENDERMAN);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Endermite")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "ENDERMITE")) {
					Endermite newEntity = (Endermite)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.ENDERMITE);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Evoker")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "ENVOKER")) {
					Evoker newEntity = (Evoker)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.EVOKER);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Ghast")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "GHAST")) {
					Ghast newEntity = (Ghast)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.GHAST);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Guardian")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "GUARDIAN")) {
					Guardian newEntity = (Guardian)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.GUARDIAN);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Husk")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "HUSK")) {
					Husk newEntity = (Husk)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.HUSK);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Magma Cube")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "MAGMA_CUBE")) {
					MagmaCube newEntity = (MagmaCube)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.MAGMA_CUBE);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Shulker")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "SHULKER")) {
					Shulker newEntity = (Shulker)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SHULKER);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Silverfish")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "SILVERFISH")) {
					Silverfish newEntity = (Silverfish)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SILVERFISH);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Skeleton")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "SKELETON")) {
					Skeleton newEntity = (Skeleton)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SKELETON);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Spider")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "SPIDER")) {
					Spider newEntity = (Spider)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SPIDER);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Slime")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "SLIME")) {
					Slime newEntity = (Slime)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SLIME);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Stray")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "STRAY")) {
					Stray newEntity = (Stray)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.STRAY);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Vex")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "VEX")) {
					Vex newEntity = (Vex)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.VEX);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Vindicator")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "VINDICATOR")) {
					Vindicator newEntity = (Vindicator)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.VINDICATOR);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Witch")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "WITCH")) {
					Witch newEntity = (Witch)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.WITCH);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Wither Skeleton")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "WITHER_SKELETON")) {
					WitherSkeleton newEntity = (WitherSkeleton)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.WITHER_SKELETON);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Zombie")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "ZOMBIE")) {
					Zombie newEntity = (Zombie)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Zombie Villager")) {
				if(!plugin.checkEntityType(entity.getUniqueId(), "ZOMBIE_VILLAGER")) {
					ZombieVillager newEntity = (ZombieVillager)entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE_VILLAGER);
					newEntity.setAI(false);
					newEntity.setInvulnerable(true);
					plugin.changeEntityType(newEntity, entity);
					entity.remove();
				}else {
					player.sendMessage(ChatColor.GOLD + "Mob type already this type");
				}
			}else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Return to main menu")) {
				player.closeInventory();
				CustomInventory i = new CustomInventory();
				i.mainInventoryMenu(player, entity);
			}
		}
	}


	@EventHandler
	public void onEntityInteract(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		
		if (plugin.checkEntity(event.getRightClicked().getUniqueId())) {
			event.setCancelled(true);
			
			entiplay.put(player, event.getRightClicked());
			CustomInventory i = new CustomInventory();
			i.mainInventoryMenu(player, event.getRightClicked());
		}
	}
}
