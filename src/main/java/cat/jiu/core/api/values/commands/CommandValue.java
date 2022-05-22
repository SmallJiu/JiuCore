package cat.jiu.core.api.values.commands;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.util.base.BaseCommand;
import net.minecraft.command.ICommand;

public class CommandValue extends BaseCommand.CommandTree{
	public CommandValue(String name, String modid, String valueName, boolean canAddComman, int level) {
		super(getCMDs(modid, valueName), name, modid, canAddComman, level);
	}
	
	static List<ICommand> getCMDs(String modid, String valueName){
		List<ICommand> list = Lists.newArrayList();
			list.add(new CommandValueAdd(modid, valueName));
			list.add(new CommandValueSubtract(modid, valueName));
			list.add(new CommandValueGet(modid, valueName));
			list.add(new CommandValueReload(modid, valueName));
			list.add(new CommandValueRemove(modid, valueName));
			
		return list;
	}
}
