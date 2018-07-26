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

public class Move extends StringPrompt {
		
	Main plugin = Main.getPlugin(Main.class);
	
	Player player;
	Entity entity;
	
	public Prompt acceptInput(ConversationContext con, String message) {
		
		String delims = "[ ]";
		String[] tokens = message.split(delims);
		boolean flag = true;
		if (tokens.length == 3){
			for (int j = 0; j < tokens.length; j++){
				try{
					Double.parseDouble(tokens[j]);
				}catch(NumberFormatException e){
					flag = false;
					break;
				}
			}
		} else {
			flag = false;
		}
		
		if (flag == true) {
			String string = (ChatColor.GOLD + "Formatted correctly.");
			con.getForWhom().sendRawMessage(string);

			Location loc = entity.getLocation().clone();

			loc.setX(Double.parseDouble(tokens[0]) + entity.getLocation().getX());
			loc.setY(Double.parseDouble(tokens[1]) + entity.getLocation().getY());
			loc.setZ(Double.parseDouble(tokens[2]) + entity.getLocation().getZ());

			entity.teleport(loc);
			
			plugin.updateEntityLocation(entity, loc);
			
			CustomInventory i = new CustomInventory();

			i.mainInventoryMenu(player, entity);

		}else {
			String string = (ChatColor.GOLD + "Formatted incorrectly.");
			con.getForWhom().sendRawMessage(string);
		}
		
		
		return null;
	}

	public void inputData(Player playerIn, Entity entityIn) {
		player = playerIn;
		entity = entityIn;
	}
	
	public String getPromptText(ConversationContext arg0) {
		
		String string = (ChatColor.GOLD + "Input position using following format: " + ChatColor.WHITE + "X Y Z" + ChatColor.GOLD + "\nFor Example: " + ChatColor.WHITE + "0 1 -0.5");
		
		return string;
	}

}
