package cat.jiu.core.api.values.commands;

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

public class CommandValueRemove extends BaseCommand.CommandNormal {
	protected final String valueID;
	protected final String valueName;
	protected final Logger log;

	public CommandValueRemove(String modid, String valueID) {
		super("remove", modid, 2);
		this.valueID = valueID;
		this.valueName = Values.getValueName(valueID);
		this.log = LogManager.getLogger(JiuUtils.other.upperCaseToFirstLetter(valueID));
	}

	@Override
	public List<String> getAliases() {
		return Lists.newArrayList("reset", "purge");
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/ * " + this.valueID + " <remove/reset/purge> <player>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		server.getPlayerProfileCache().save();
		server.getPlayerProfileCache().load();
		if(args.length < 1 || args.length == 0) {
			if(sender instanceof EntityPlayer) {
				UUID uid = JiuUtils.entity.getUUID(sender.getName());
				if(uid == null) {
					uid = ((EntityPlayer) sender).getUniqueID();
				}
				ValueStateType type = Values.remove(this.valueID, uid, 0);

				if(type == ValueStateType.SUCCESS) {
					this.log.log(Level.INFO, "Remove Success, " + sender.getName() + " has 0 " + this.valueName);
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.remove.success.info", TextFormatting.GREEN, sender.getName(), this.valueName, this.valueName));
				}else if(type == ValueStateType.NOT_FOUND_VALUE) {
					this.log.log(Level.INFO, "Remove: not found valueID");
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.value_not_found.info", TextFormatting.RED, this.valueName));
				}else if(type == ValueStateType.UNABLE_WRITE) {
					this.log.log(Level.INFO, "Remove: unable write but success");
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.remove.success.info", TextFormatting.GREEN, sender.getName(), this.valueName, this.valueName));
					sender.sendMessage(JiuUtils.other.createTextComponent("command.value.unable_write.info", TextFormatting.RED));
				}
				return;
			}else {
				throw new CommandException(this.getUsage(sender));
			}
		}

		String name = args[0];

		UUID uid = JiuUtils.entity.hasNameOrUUID(name) ? JiuUtils.entity.getUUID(name) : getPlayer(server, sender, name).getUniqueID();
		ValueStateType type = Values.remove(this.valueID, uid, 0);

		switch(type) {
			case SUCCESS:
				this.log.info("Remove Success, " + name + " has 0 " + this.valueName);
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.remove.success.info", TextFormatting.GREEN, name, this.valueName, this.valueName));
				break;
			case NOT_FOUND_VALUE:
				this.log.error("Remove: not found valueID");
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.value_not_found.info", TextFormatting.RED, this.valueName));
				break;
			case UNABLE_WRITE:
				this.log.error("Remove: unable write but success");
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.remove.success.info", TextFormatting.GREEN, name, this.valueName, this.valueName));
				sender.sendMessage(JiuUtils.other.createTextComponent("command.value.unable_write.info", TextFormatting.RED));
				break;
			case Initialization:
				this.log.error("Name can not be: Initialization");
				sender.sendMessage(JiuUtils.other.createTextComponent("Name can not be: Initialization", TextFormatting.RED));
				break;
			default:
				this.log.error("Unknown State");
				sender.sendMessage(JiuUtils.other.createTextComponent("Unknown State", TextFormatting.RED));
				break;
		}
	}
}
