package frozenmobs;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	private Main plugin = Main.getPlugin(Main.class);
			
	// File and File Configurations here
	private FileConfiguration maincfg;
	private File mainfile;
	/*-------------------------------*/
	
	// Main Config setup
	public boolean setup() {
		//Create plugin folder if doesn't exist.
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		//Main Config setup
		mainfile = new File(plugin.getDataFolder(), "config.yml");
		
		if(!mainfile.exists()) {
			try {
				mainfile.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Config file created");
			}catch(IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "An error has occured creating the main config filefile");
				return false;
			}
		}
		maincfg = YamlConfiguration.loadConfiguration(mainfile);

		createMainConfigValues();
		
		return true;
	}
	
	private void createMainConfigValues() {
		maincfg.addDefault("config.mongodb.use", false);
		maincfg.addDefault("config.mongodb.URI-key", "");
		maincfg.addDefault("config.mongodb.database", "animals");
		maincfg.addDefault("config.mongodb.collection", "animalcollection");
		
		maincfg.options().copyDefaults(true);

		try {
			maincfg.save(mainfile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "unable to add default values to config.");
		}
	}
	
	public void reloadMainConfig() {
		maincfg = YamlConfiguration.loadConfiguration(mainfile);
	}
	
	public boolean returnBool() {
		return maincfg.getBoolean("config.mongodb.use");
	}
	
	public String returnURI() {
		return maincfg.getString("config.mongodb.URI-key");
	}
	
	public String returnDB() {
		return maincfg.getString("config.mongodb.database");
	}
	
	public String returnCollection() {
		return maincfg.getString("config.mongodb.collection");
	}
}
