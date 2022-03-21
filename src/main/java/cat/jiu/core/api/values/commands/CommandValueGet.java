package cat.jiu.core.api.values.commands;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import cat.jiu.core.api.values.Values;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseCommand;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandValueGet extends BaseCommand.CommandNormal {
	protected final String valueID;
	protected final String valueName;
	protected final Logger log;
	public CommandValueGet(String modid, String valueID) {
		super("get", modid, 0);
		this.valueID = valueID;
		this.valueName = Values.getValueName(valueID);
		this.log = LogManager.getLogger(JiuUtils.other.upperCaseToFirstLetter(valueID));
	}
	
	@Override
	public List<String> getAliases() {
		return Lists.newArrayList("look", "other");
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/ * " + this.valueID + " <get/look/other> <player>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		server.getPlayerProfileCache().save();
		server.getPlayerProfileCache().load();
		if(args.length == 0) {
			if(sender instanceof EntityPlayer) {
				UUID uid = ((EntityPlayer) sender).getUniqueID();
				BigInteger value = Values.get(this.valueID, uid);
				if(sender instanceof Entity) {
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.get.success.info", TextFormatting.GREEN, sender.getName(), value, this.valueName));
				}else {
					this.log.log(Level.INFO, sender.getName() + " has " + (value + " " + this.valueName));
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
			BigInteger value = Values.get(this.valueID, uid);
			if(sender instanceof Entity) {
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.get.success.info", TextFormatting.GREEN, name, value, this.valueName));
			}else {
				this.log.log(Level.INFO, name + " has " + (value + " " + this.valueName));
			}
		}
	}
}
