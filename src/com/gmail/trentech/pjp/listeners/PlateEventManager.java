package com.gmail.trentech.pjp.listeners;

import java.util.HashMap;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.ConfigManager;
import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.Resource;
import com.gmail.trentech.pjp.events.TeleportEvent;
import com.gmail.trentech.pjp.portals.Plate;

import ninja.leaping.configurate.ConfigurationNode;

public class PlateEventManager {

	public static HashMap<Player, Plate> creators = new HashMap<>();

	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Modify event, @First Player player) {
		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			BlockSnapshot block = transaction.getFinal();
			BlockType type = block.getState().getType();
			
			if(!type.equals(BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE) && !type.equals(BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE) 
					&& !type.equals(BlockTypes.STONE_PRESSURE_PLATE) && !type.equals(BlockTypes.WOODEN_PRESSURE_PLATE)){
				return;
			}

			if(!block.getExtendedState().get(Keys.POWERED).isPresent()){
				return;
			}

			if(!block.getExtendedState().get(Keys.POWERED).get()){
				return;
			}

			Location<World> location = block.getLocation().get();		
			String locationName = location.getExtent().getName() + "." + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();

			ConfigurationNode config = new ConfigManager("portals.conf").getConfig();;
			
			if(config.getNode("Plates", locationName, "World").getString() == null){
				return;
			}			
			String worldName = config.getNode("Plates", locationName, "World").getString();
			
			if(!Main.getGame().getServer().getWorld(worldName).isPresent()){
				player.sendMessage(Text.of(TextColors.DARK_RED, Resource.getPrettyName(worldName), " does not exist"));
				return;
			}
			World world = Main.getGame().getServer().getWorld(worldName).get();
			
			int x = world.getSpawnLocation().getBlockX();
			int y = world.getSpawnLocation().getBlockY();
			int z = world.getSpawnLocation().getBlockZ();
			
			if(config.getNode("Plates", locationName, "X").getString() != null && config.getNode("Plates", locationName, "Y").getString() != null && config.getNode("Plates", locationName, "Z").getString() != null){
				x = config.getNode("Plates", locationName, "X").getInt();
				y = config.getNode("Plates", locationName, "Y").getInt();
				z = config.getNode("Plates", locationName, "Z").getInt();
			}

			if(!player.hasPermission("pjp.plate.interact")){
				player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to interact with pressure plate portals"));
				event.setCancelled(true);
				return;
			}
			
			Main.getGame().getEventManager().post(new TeleportEvent(player.getLocation(), world.getLocation(x, y, z), Cause.of(player)));
		}
	}
	
	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Break event, @First Player player) {
		if(creators.containsKey(player)){
			creators.remove(player);
			return;
		}

		ConfigManager configManager = new ConfigManager("portals.conf");
		ConfigurationNode config = configManager.getConfig();
		
		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			Location<World> location = transaction.getFinal().getLocation().get();		
			String locationName = location.getExtent().getName() + "." + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();

			if(config.getNode("Plates", locationName, "World").getString() == null){
				continue;
			}
			
			if(!player.hasPermission("pjp.plate.break")){
				player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to break pressure plate portals"));
				event.setCancelled(true);
			}else{
				config.getNode("Plates", locationName).setValue(null);
				configManager.save();
				player.sendMessage(Text.of(TextColors.DARK_GREEN, "Broke pressure plate portal"));
			}
		}
	}

	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Place event, @First Player player) {
		if(!creators.containsKey(player)){
			return;
		}

		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			BlockType type = transaction.getFinal().getState().getType();

			if(!type.equals(BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE) && !type.equals(BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE) 
					&& !type.equals(BlockTypes.STONE_PRESSURE_PLATE) && !type.equals(BlockTypes.WOODEN_PRESSURE_PLATE)){
				continue;
			}
			
			Location<World> location = transaction.getFinal().getLocation().get();
			
			if(!player.hasPermission("pjp.plate.place")){
	        	player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to place pressure plate portals"));
	        	creators.remove(player);
	        	event.setCancelled(true);
	        	return;
			}
			
			String locationName = location.getExtent().getName() + "." + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();

            ConfigManager configManager = new ConfigManager("portals.conf");
            ConfigurationNode config = configManager.getConfig();

            Plate plate = creators.get(player);
            
            config.getNode("Plates", locationName, "World").setValue(plate.getLocation().getExtent().getName());
            if(!plate.isSpawn()){
                config.getNode("Plates", locationName, "X").setValue(plate.getLocation().getBlockX());
                config.getNode("Plates", locationName, "Y").setValue(plate.getLocation().getBlockY());
                config.getNode("Plates", locationName, "Z").setValue(plate.getLocation().getBlockZ());
            }

            configManager.save();
          
    		if(new ConfigManager().getConfig().getNode("Options", "Show-Particles").getBoolean()){
    			Resource.spawnParticles(location, 1.0, false);
    		}
            
            player.sendMessage(Text.of(TextColors.DARK_GREEN, "New pressure plate portal created"));
            
            creators.remove(player);
		}
	}
}
