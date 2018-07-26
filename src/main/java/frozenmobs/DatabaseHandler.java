package frozenmobs;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import net.md_5.bungee.api.ChatColor;

public class DatabaseHandler {
	
	private MongoClient client;
	private DB mcserverdb;
	private DBCollection animals;
	
	public boolean connect(String inputString, String dbString, String collectionString) {
		MongoClientURI uri = new MongoClientURI(inputString); 
		
		try {
			client = new MongoClient(uri);
		} catch (UnknownHostException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to connect to db");
			return false;
		}
		
		mcserverdb = client.getDB(dbString);
		
		animals = mcserverdb.getCollection(collectionString);
		return true;
	}
	
	public void storeAnimal(Entity entity) {
		Location loc = entity.getLocation();
		
		DBObject obj = new BasicDBObject("uuid", entity.getUniqueId());
		obj.put("type", entity.getType().toString());
		obj.put("world", loc.getWorld().toString());
		obj.put("posx", loc.getX());
		obj.put("posy", loc.getY());
		obj.put("posz", loc.getZ());
		obj.put("posyaw", loc.getYaw());
		obj.put("pospitch", loc.getPitch());
		
		animals.insert(obj);
	}

	public boolean checkType(UUID uuid, String type) {
		DBObject s = new BasicDBObject("uuid", uuid);
		
		DBObject found = animals.findOne(s);
		if(found == null) {
			return false;
		}
		String truetype = (String) found.get("type");
		
		if (type.equals(truetype)) {
			return true;
		}else {
			return false;
		}
	}
	
	public void removeAnimal(Entity entity) {
		UUID uuid = entity.getUniqueId();
		
		DBObject s = new BasicDBObject("uuid", uuid);
		
		DBObject found = animals.findOne(s);
		if(found == null) {
			System.out.println(ChatColor.RED + "Entity not found");
			return;
		}
		animals.remove(found);
	}
	
	public List<UUID> getUUIDs() {
		
		DBCursor creatures = animals.find();
		List<UUID> uuid = new ArrayList<UUID>();
		
		for (DBObject creature: creatures) {
			uuid.add((UUID) creature.get("uuid"));
		}
		
		return uuid;
	}
	
	public void readAnimal(Entity entity) {
		UUID uuid = entity.getUniqueId();
		
		DBObject s = new BasicDBObject("uuid", uuid);
		
		DBObject found = animals.findOne(s);
		if(found == null) {
			System.out.println(ChatColor.RED + "Entity not found");
			return;
		}
		System.out.println(ChatColor.GREEN + "Entity found");
		// Do something
	}
	
	public void updateAnimalLocation(Entity entity, Location newloc) {
		UUID uuid = entity.getUniqueId();

		DBObject s = new BasicDBObject("uuid", uuid);
		
		DBObject found = animals.findOne(s);
		if(found == null) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Entity not found");
			return;
		}
		
		BasicDBObject set = new BasicDBObject("$set", s);
		set.append("$set", new BasicDBObject("posx", newloc.getX()));
		animals.update(found, set);
		set = new BasicDBObject("$set", s);
		set.append("$set", new BasicDBObject("posy", newloc.getY()));
		animals.update(found, set);
		set = new BasicDBObject("$set", s);
		set.append("$set", new BasicDBObject("posz", newloc.getZ()));
		animals.update(found, set);
		set = new BasicDBObject("$set", s);
		set.append("$set", new BasicDBObject("posyaw", newloc.getYaw()));
		animals.update(found, set);
		set = new BasicDBObject("$set", s);
		set.append("$set", new BasicDBObject("pospitch", newloc.getPitch()));
		animals.update(found, set);
		
	}
}
