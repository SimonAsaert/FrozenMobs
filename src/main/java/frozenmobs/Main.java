package frozenmobs;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import frozenmobs.events.EventClass;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	private DatabaseHandler dbh;
	private List<UUID> uuidlist;
	private ConfigManager cfgm;
	
	@Override
	public void onEnable() {
		String inputString = loadConfigManager();
		if (inputString == null) {
			this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "You have to use MongoDB to use this plugin in its current state. Disabling the plugin");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		loadDatabaseHandler(inputString);
		getServer().getPluginManager().registerEvents(new EventClass(), this);
		
		getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "FrozenMobs has been enabled");
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private String loadConfigManager() {
		cfgm = new ConfigManager();
		cfgm.setup();
		if(cfgm.returnBool()) return cfgm.returnURI();
		return null;
	}
	
	private void loadDatabaseHandler(String inputString) {
		dbh = new DatabaseHandler();
		dbh.connect(inputString, cfgm.returnDB(), cfgm.returnCollection());
		uuidlist = dbh.getUUIDs();
	}
	
	public void addToDb(Entity entity) {
		dbh.storeAnimal(entity);
		uuidlist.add(entity.getUniqueId());
	}
	
	public boolean checkEntity(UUID uuid) {
		if (uuidlist.contains(uuid)) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean checkEntityType(UUID uuid, String type) {
		return dbh.checkType(uuid, type);
	}
	
	public void updateEntityLocation(Entity entity, Location loc) {
		if(uuidlist.contains(entity.getUniqueId())) {
			dbh.updateAnimalLocation(entity, loc);
		}
	}
	
	public void removeEntity(Entity entity) {
		if (uuidlist.contains(entity.getUniqueId())){
			uuidlist.remove(entity.getUniqueId());
			dbh.removeAnimal(entity);
		}
	}
	
	public void changeEntityType(Entity newEnt, Entity oldEnt) {
		removeEntity(oldEnt);
		addToDb(newEnt);
	}
}
