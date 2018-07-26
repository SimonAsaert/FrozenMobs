package frozenmobs.conversations;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import frozenmobs.Main;
import frozenmobs.inventory.CustomInventory;

public class Rotate extends StringPrompt {
	private Main plugin = Main.getPlugin(Main.class);
	
	private Entity entity;
	private Player player;
	
	public Prompt acceptInput(ConversationContext con, String message) {
		boolean flag = true;
		try{
			Float.parseFloat(message);
		}catch(NumberFormatException e){
			flag = false;
		}
		if (flag == true) {
			float rotate = Float.parseFloat(message);
			Location loc = entity.getLocation().clone();
			loc.setYaw(loc.getYaw() + rotate);
			
			entity.teleport(loc);
			
			plugin.updateEntityLocation(entity, loc);
			
			CustomInventory i = new CustomInventory();
			i.mainInventoryMenu(player, entity);
			
			
		}
		
		return null;
	}
	
	public void inputData(Player playerIn,Entity entityIn) {
		entity = entityIn;
		player = playerIn;
	}
	
	public String getPromptText(ConversationContext arg0) {
		String string = (ChatColor.GOLD + "Input the delay between each particle in ticks. Minimum 1 ticks delay.");
		
		return string;
	}
}
