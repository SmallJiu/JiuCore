package cat.jiu.core.commands;

import cat.jiu.core.util.base.BaseCommand;
import net.minecraft.command.ICommand;

public class CommandJiuCore extends BaseCommand.CommandTree {
	public CommandJiuCore(String name, boolean canAddCommand, boolean checkPermission, int level) {
		super(new ICommand[] {
				new BaseCommand.CommandTree(
						new ICommand[] {
								new CommandInFluidCrafting()
						}, "craft", "jc", checkPermission, checkPermission, level
					)
				}, 
				name, "jc", canAddCommand, checkPermission, level);
	}
}
