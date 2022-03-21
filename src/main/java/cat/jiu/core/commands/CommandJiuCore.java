package cat.jiu.core.commands;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.values.commands.CommandValue;
import cat.jiu.core.util.base.BaseCommand;

import net.minecraft.command.ICommand;

public class CommandJiuCore extends BaseCommand.CommandTree {
	public static final List<String> unRegisterValue = Lists.newArrayList();
	public CommandJiuCore(String name, boolean canAddCommand, boolean checkPermission, int level) {
		super(getOwnerCommands(canAddCommand, checkPermission, level), name, JiuCore.MODID, canAddCommand, checkPermission, level);
	}
	
	private static List<ICommand> getOwnerCommands(boolean canAddCommand, boolean checkPermission, int level) {
		List<ICommand> cmds = Lists.newArrayList();
		
		cmds.add(new BaseCommand.CommandTree(new ICommand[] {
				new CommandInFluidCrafting()		
		}, "craft", JiuCore.MODID, checkPermission, checkPermission, level));
		
		cmds.add(new CommandValue("coin", JiuCore.MODID, "coin", checkPermission, checkPermission, level));
		for (String value : unRegisterValue) {
			cmds.add(new CommandValue(value, JiuCore.MODID, value, checkPermission, checkPermission, level));
			
		}
		return cmds;
	}
}
