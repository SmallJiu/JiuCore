package cat.jiu.core.api.values.commands;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import cat.jiu.core.api.values.Values;
import cat.jiu.core.api.values.Values.ValueStateType;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseCommand;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandValueAdd extends BaseCommand.CommandNormal {
	protected final String valueID;
	protected final String valueName;
	protected final Logger log;
	public CommandValueAdd(String modid, String valueID) {
		super("add", modid, 2);
		this.valueID = valueID;
		this.valueName = Values.getValueName(valueID);
		this.log = LogManager.getLogger(JiuUtils.other.upperCaseToFirstLetter(valueID));
	}
	
	@Override
	public List<String> getAliases() {
		return Lists.newArrayList("give");
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/ * " + this.valueID + " <add/give> <player> <value>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		server.getPlayerProfileCache().save();
		
		if(args.length == 0) {
			throw new CommandException(this.getUsage(sender));
		}
		
		if(args.length == 1) {
			if(sender instanceof EntityPlayer) {
				UUID uid = JiuUtils.entity.getUUID(sender.getName());
				if(uid == null) {
					uid = ((EntityPlayer) sender).getUniqueID();
				}
				ValueStateType type = Values.add(this.valueID, uid, new BigInteger(args[0]), 0);
				
				if(type == ValueStateType.SUCCESS) {
					this.log.log(Level.INFO, "Add: success, " + sender.getName() + " has " + (Values.get(valueID, sender.getName()) + " " + this.valueName));
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.add.success.info", TextFormatting.GREEN, args[0], this.valueName, sender.getName(), Values.get(valueID, sender.getName()), this.valueName));
				}else if(type == ValueStateType.NOT_FOUND_VALUE) {
					this.log.log(Level.INFO, "Add: not found valueID");
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.value_not_found.info", TextFormatting.RED, this.valueName));
				}else if(type == ValueStateType.UNABLE_WRITE) {
					this.log.log(Level.INFO, "Add: unable write but add success");
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.add.success.info", TextFormatting.GREEN, args[0], this.valueName, sender.getName(), Values.get(valueID, sender.getName()), this.valueName));
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.unable_write.info", TextFormatting.RED));
				}
				return;
			}else {
				throw new CommandException(this.getUsage(sender));
			}
		}
		
		String name = args[0];
		
		if(name.equals("Initialization")){
			throw new CommandException("Name can not be: Initialization");
		}else {
			UUID uid = JiuUtils.entity.hasNameOrUUID(name) ? JiuUtils.entity.getUUID(name) : getPlayer(server, sender, name).getUniqueID();
			ValueStateType type = Values.add(this.valueID, uid, new BigInteger(args[1]), 0);
			
			if(type == ValueStateType.SUCCESS) {
				this.log.log(Level.INFO, "Add: success, " + name + " has " + (Values.get(valueID, name) + " " + this.valueName));
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.add.success.info", TextFormatting.GREEN, args[1], this.valueName, name, Values.get(valueID, name), this.valueName));
			}else if(type == ValueStateType.NOT_FOUND_VALUE) {
				this.log.log(Level.INFO, "Add: not found valueID");
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.value_not_found.info", TextFormatting.RED, this.valueName));
			}else if(type == ValueStateType.UNABLE_WRITE) {
				this.log.log(Level.INFO, "Add: unable write but add success");
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.add.success.info", TextFormatting.GREEN, args[1], this.valueName, name, Values.get(valueID, name), this.valueName));
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.unable_write.info", TextFormatting.RED));
			}
		}
	}
}
