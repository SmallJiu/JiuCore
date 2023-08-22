package cat.jiu.core.commands;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.values.commands.CommandValue;
import cat.jiu.core.util.base.BaseCommand;

import net.minecraft.command.ICommand;

public class CommandJiuCore extends BaseCommand.CommandTree {
	public static final List<String> unRegisterValue = Lists.newArrayList();
	public CommandJiuCore(String name, boolean canAddCommand, int level) {
		super(getCMDs(canAddCommand, level), name, JiuCore.MODID, canAddCommand, level);
	}
	
	private static List<ICommand> getCMDs(boolean canAddCommand, int level) {
		List<ICommand> cmds = Lists.newArrayList();
		
		cmds.add(new CommandValue("coin", JiuCore.MODID, "coin", canAddCommand, level));
		for (String value : unRegisterValue) {
			cmds.add(new CommandValue(value, JiuCore.MODID, value, canAddCommand, level));
			
		}
		return cmds;
	}
}
