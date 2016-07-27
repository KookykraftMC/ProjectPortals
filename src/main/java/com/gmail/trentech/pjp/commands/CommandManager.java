package com.gmail.trentech.pjp.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.pjp.commands.home.CMDHome;
import com.gmail.trentech.pjp.commands.portal.CMDPortal;
import com.gmail.trentech.pjp.commands.portal.CMDSave;
import com.gmail.trentech.pjp.commands.warp.CMDWarp;

public class CommandManager {
	
	private CommandSpec cmdWarpCreate = CommandSpec.builder()
		    .description(Text.of("Create a new warp point"))
		    .permission("pjp.cmd.warp.create")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("destination"))),
		    		GenericArguments.flags().flag("b")
		    		.valueFlag(GenericArguments.string(Text.of("x,y,z")), "c")
    				.valueFlag(GenericArguments.string(Text.of("direction")), "d")
    				.valueFlag(GenericArguments.string(Text.of("price")), "p").buildWith(GenericArguments.none()))
		    .executor(new com.gmail.trentech.pjp.commands.warp.CMDCreate())
		    .build();
	
	private CommandSpec cmdWarpRemove = CommandSpec.builder()
		    .description(Text.of("Remove an existing warp point"))
		    .permission("pjp.cmd.warp.remove")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new com.gmail.trentech.pjp.commands.warp.CMDRemove())
		    .build();
	
	private CommandSpec cmdWarpPrice = CommandSpec.builder()
		    .description(Text.of("Set price of an existing warp point"))
		    .permission("pjp.cmd.warp.price")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("price"))))
		    .executor(new com.gmail.trentech.pjp.commands.warp.CMDPrice())
		    .build();
	
	private CommandSpec cmdWarpList = CommandSpec.builder()
		    .description(Text.of("List all available warp points"))
		    .permission("pjp.cmd.warp.list")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new com.gmail.trentech.pjp.commands.warp.CMDList())
		    .build();
	
	public CommandSpec cmdWarp = CommandSpec.builder()
		    .description(Text.of("Top level warp command"))
		    .permission("pjp.cmd.warp")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("player"))))
		    .child(cmdWarpCreate, "create", "c")
		    .child(cmdWarpRemove, "remove", "r")
		    .child(cmdWarpList, "list", "l")
		    .child(cmdWarpPrice, "price", "p")
		    .executor(new CMDWarp())
		    .build();
	
	
	private CommandSpec cmdHomeCreate = CommandSpec.builder()
		    .description(Text.of("Create a new home"))
		    .permission("pjp.cmd.home.create")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new com.gmail.trentech.pjp.commands.home.CMDCreate())
		    .build();
	
	private CommandSpec cmdHomeRemove = CommandSpec.builder()
		    .description(Text.of("Remove an existing home"))
		    .permission("pjp.cmd.home.remove")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new com.gmail.trentech.pjp.commands.home.CMDRemove())
		    .build();
	
	private CommandSpec cmdHomeList = CommandSpec.builder()
		    .description(Text.of("List all homes"))
		    .permission("pjp.cmd.home.list")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new com.gmail.trentech.pjp.commands.home.CMDList())
		    .build();

	public CommandSpec cmdHome = CommandSpec.builder()
		    .description(Text.of("Top level home command"))
		    .permission("pjp.cmd.home")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("player"))))
		    .child(cmdHomeCreate, "create", "c")
		    .child(cmdHomeRemove, "remove", "r")
		    .child(cmdHomeList, "list", "l")
		    .executor(new CMDHome())
		    .build();

	private CommandSpec cmdPortalCreate = CommandSpec.builder()
		    .description(Text.of("Create a new portal"))
		    .permission("pjp.cmd.portal.create")
		    .arguments()
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("destination"))), 
		    		GenericArguments.flags().flag("b")
		    		.valueFlag(GenericArguments.string(Text.of("x,y,z")), "c")
    				.valueFlag(GenericArguments.string(Text.of("direction")), "d")
		    		.valueFlag(GenericArguments.string(Text.of("particle[:color]")), "e")
    				.valueFlag(GenericArguments.string(Text.of("price")), "p").buildWith(GenericArguments.none()))
		    .executor(new com.gmail.trentech.pjp.commands.portal.CMDCreate())
		    .build();

	private CommandSpec cmdPortalRemove = CommandSpec.builder()
		    .description(Text.of("Remove an existing portal"))
		    .permission("pjp.cmd.portal.remove")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new com.gmail.trentech.pjp.commands.portal.CMDRemove())
		    .build();
	
	private CommandSpec cmdPortalParticle = CommandSpec.builder()
		    .description(Text.of("Set particle effects of an existing portal"))
		    .permission("pjp.cmd.portal.particle")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("type")))
		    		, GenericArguments.optional(GenericArguments.string(Text.of("color"))))
		    .executor(new com.gmail.trentech.pjp.commands.portal.CMDParticle())
		    .build();
	
	private CommandSpec cmdPortalPrice = CommandSpec.builder()
		    .description(Text.of("Set price of an existing portal"))
		    .permission("pjp.cmd.portal.price")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("price"))))
		    .executor(new com.gmail.trentech.pjp.commands.portal.CMDPrice())
		    .build();
	
	private CommandSpec cmdPortalList = CommandSpec.builder()
		    .description(Text.of("List all portals"))
		    .permission("pjp.cmd.portal.list")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new com.gmail.trentech.pjp.commands.portal.CMDList())
		    .build();

	private CommandSpec cmdSave = CommandSpec.builder()
		    .description(Text.of("Saves generated portal"))
		    .permission("pjp.cmd.portal.create")
		    .executor(new CMDSave())
		    .build();
	
	public CommandSpec cmdPortal = CommandSpec.builder()
		    .description(Text.of("Top level portal command"))
		    .permission("pjp.cmd.portal")
		    .child(cmdPortalCreate, "create", "c")
		    .child(cmdPortalRemove, "remove", "r")
		    .child(cmdPortalParticle, "particle", "p")
		    .child(cmdPortalPrice, "price", "pr")
		    .child(cmdPortalList, "list", "l")
		    .child(cmdSave, "save", "s")
		    .executor(new CMDPortal())
		    .build();
	
	
	public CommandSpec cmdButton = CommandSpec.builder()
		    .description(Text.of("Create a new button portal"))
		    .permission("pjp.cmd.button")
		    .arguments()
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("destination"))), GenericArguments.flags().flag("b")
		    		.valueFlag(GenericArguments.string(Text.of("x,y,z")), "c")
		    		.valueFlag(GenericArguments.string(Text.of("direction")), "d")
		    		.valueFlag(GenericArguments.string(Text.of("price")), "p").buildWith(GenericArguments.none()))
		    .executor(new CMDObj.Button())
		    .build();

	public CommandSpec cmdDoor = CommandSpec.builder()
		    .description(Text.of("Create a new door portal"))
		    .permission("pjp.cmd.door")
		    .arguments()
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("destination"))), GenericArguments.flags().flag("b")
		    		.valueFlag(GenericArguments.string(Text.of("x,y,z")), "c")
		    		.valueFlag(GenericArguments.string(Text.of("direction")), "d")
		    		.valueFlag(GenericArguments.string(Text.of("price")), "p").buildWith(GenericArguments.none()))
		    .executor(new CMDObj.Door())
		    .build();
	
	public CommandSpec cmdLever = CommandSpec.builder()
		    .description(Text.of("Create a new lever portal"))
		    .permission("pjp.cmd.lever")
		    .arguments()
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("destination"))), GenericArguments.flags().flag("b")
		    		.valueFlag(GenericArguments.string(Text.of("x,y,z")), "c")
		    		.valueFlag(GenericArguments.string(Text.of("direction")), "d")
		    		.valueFlag(GenericArguments.string(Text.of("price")), "p").buildWith(GenericArguments.none()))
		    .executor(new CMDObj.Lever())
		    .build();
	
	public CommandSpec cmdPlate = CommandSpec.builder()
		    .description(Text.of("Create a new pressure plate portal"))
		    .permission("pjp.cmd.plate")
		    .arguments()
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("destination"))), GenericArguments.flags().flag("b")
		    		.valueFlag(GenericArguments.string(Text.of("x,y,z")), "c")
		    		.valueFlag(GenericArguments.string(Text.of("direction")), "d")
		    		.valueFlag(GenericArguments.string(Text.of("price")), "p").buildWith(GenericArguments.none()))
		    .executor(new CMDObj.Plate())
		    .build();

	public CommandSpec cmdSign = CommandSpec.builder()
		    .description(Text.of("Create a new sign portal"))
		    .permission("pjp.cmd.sign")
		    .arguments()
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("destination"))), GenericArguments.flags().flag("b")
		    		.valueFlag(GenericArguments.string(Text.of("x,y,z")), "c")
		    		.valueFlag(GenericArguments.string(Text.of("direction")), "d")
		    		.valueFlag(GenericArguments.string(Text.of("price")), "p").buildWith(GenericArguments.none()))
		    .executor(new CMDObj.Sign())
		    .build();
	
	public CommandSpec cmdPJP = CommandSpec.builder()
		    .description(Text.of("Lists all Project Worlds commands"))
		    .permission("pjp.cmd")
		    .executor(new CMDPjp())
		    .build();
}
