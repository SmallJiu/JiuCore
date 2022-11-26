package cat.jiu.core.api.values.commands;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cat.jiu.core.api.values.Values;
import cat.jiu.core.types.ValueStateType;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseCommand;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandValueReload extends BaseCommand.CommandNormal {
	protected final String valueID;
	protected final String valueName;
	protected final Logger log;

	public CommandValueReload(String modid, String valueID) {
		super("reload", modid, 2);
		this.valueID = valueID;
		this.valueName = Values.getValueName(valueID);
		this.log = LogManager.getLogger(valueID);
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/ * " + this.valueID + " reload";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		server.getPlayerProfileCache().save();
		ValueStateType type = Values.loadFromFile();
		
		if(type == ValueStateType.SUCCESS) {
			this.log.log(Level.INFO, "Reload Success");
			sender.sendMessage(JiuUtils.other.createTextComponent("command.value.reload.success.info", TextFormatting.GREEN));
		}else if(type == ValueStateType.FILE_NOT_FOUND) {
			this.log.log(Level.ERROR, "Reload Fail, file not found");
			sender.sendMessage(JiuUtils.other.createTextComponent("command.value.reload.not_found_file.info", TextFormatting.RED));
		}else {
			this.log.log(Level.ERROR, "Reload Fail, see the log");
			sender.sendMessage(JiuUtils.other.createTextComponent("command.value.reload.unknown", TextFormatting.RED));
		}
	}
}
