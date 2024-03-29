package cat.jiu.core.util.base;

import java.util.List;

import cat.jiu.core.exception.Log4jBugException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

public class BaseCommand {
	public abstract static class CommandNormal extends CommandBase {
		protected final String name;
		protected final int level;
		protected final String modid;
		
		public CommandNormal(String name, String modid) {
			this(name, modid, 0);
		}
		
		public CommandNormal(String name, String modid, int level) {
			this.name = name;
			this.modid = modid;
			this.level = level;
		}
		
		@Override
		public String getName(){
			return this.name;
		}
		
		@Override
		public String getUsage(ICommandSender sender) {
			return "command." + this.modid + "." + this.getName() +".info";
		}
		
		@Override
		public int getRequiredPermissionLevel() {
			return this.level;
		}
		
		@Override
		public abstract void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
	}
	
	public static class CommandTree extends CommandTreeBase {
		protected final String name;
		protected final String modid;
		protected final boolean canAddCommand;
		protected final int level;
		protected final ICommand[] commands;
		
		public CommandTree(List<ICommand> commands, String name, String modid, boolean canAddComman, int level) {
			this(commands.toArray(new ICommand[commands.size()]), name, modid, canAddComman, level);
		}
		
		public CommandTree(ICommand[] commands, String name, String modid, boolean canAddComman, int level) {
			this.commands = commands;
			this.name = name;
			this.modid = modid;
			this.canAddCommand = canAddComman;
			this.level = level;
			
			for(ICommand cmd : commands){
				if(cmd != null){
					super.addSubcommand(cmd);
				}
			}
		}
		
		public ICommand[] getCommands() {
			return this.commands;
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		@Override
		public String getUsage(ICommandSender sender) {
			return "command." + this.modid + "." + this.getName() +".info";
		}
		
		@Override
		public void addSubcommand(ICommand command) {
			if(!this.canAddCommand){
				if(command.getClass().getPackage() != this.getClass().getPackage()) {
					throw new Log4jBugException("Don't add sub-commands to /" + this.name + ", create your owner command !");
				}
			}else {
				super.addSubcommand(command);
			}
		}
		
		@Override
		public int getRequiredPermissionLevel() {
			return this.level;
		}
	}
}
