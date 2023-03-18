package cat.jiu.core.commands;

import java.util.ArrayList;
import java.util.List;

import cat.jiu.core.events.game.InFluidCraftingEvent;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseCommand;
import cat.jiu.core.util.crafting.InFluidCrafting;
import cat.jiu.core.util.crafting.InFluidCrafting.InFluidCraftingRecipeType;
import cat.jiu.core.util.crafting.Recipes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

import net.minecraftforge.common.MinecraftForge;

public class CommandInFluidCrafting extends BaseCommand.CommandTree{
	public CommandInFluidCrafting() {
		super(new ICommand[] { new Add(), new Remove(), new RemoveAll(), new Reload() }, "influid", "jc", false, 2);
	}
	
	private static class Add extends BaseCommand.CommandTree{
		public Add() {
			super(new ICommand[] { new CacheAdd(),new ForeverAdd() }, "add", "jc", false, 2);
		}
		
		private static class CacheAdd extends BaseCommand.CommandNormal {
			public CacheAdd() {super("c", "jc", 2);}
			@SuppressWarnings("deprecation")
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if(args.length == 4) {
					IBlockState fluid = Blocks.WATER.getDefaultState();
					
					String[] fluidText = JiuUtils.other.custemSplitString(args[0], "@");
					if(fluidText.length == 1) {
						fluid = getBlockByText(sender, fluidText[0]).getDefaultState();
					}else {
						fluid = getBlockByText(sender, fluidText[0]).getStateFromMeta(parseInt(fluidText[1]));
					}
					
					String[] inputText = JiuUtils.other.custemSplitString(args[1], "@");
					ItemStack input = new ItemStack(getItemByText(sender, inputText[0]), parseInt(inputText[1]), parseInt(inputText[2]));
					boolean canConsumeFluid = parseBoolean(args[2]);
					
					List<ItemStack> outputs = new ArrayList<ItemStack>();
					
					String[] outputText0 = JiuUtils.other.custemSplitString(args[3], "#");
					for(String outText0 : outputText0) {
						String[] outputText1 = JiuUtils.other.custemSplitString(outText0, "@");
						
						ItemStack out = new ItemStack(getItemByText(sender, outputText1[0]), parseInt(outputText1[1]), parseInt(outputText1[2]));
						
						outputs.add(out);
					}
					
					if(InFluidCrafting.getRecipesMap().isEmpty()) {
						JiuUtils.entity.sendMessage(sender, "UseFluid: < "+ fluid + " >");
						JiuUtils.entity.sendMessage(sender, "InputItem: < "+ input + " >");
						JiuUtils.entity.sendMessage(sender, "OutputItems: < "+ outputs + " >");
						JiuUtils.entity.sendMessage(sender, "CanConsumeFluid: < "+ canConsumeFluid + " >");
						JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.warn.info", TextFormatting.RED);
						
						InFluidCrafting.addInFluidCrafting(fluid, input, outputs, canConsumeFluid);
					}
					
					for(int i : InFluidCrafting.getRecipesMap().keySet()) {
						InFluidCraftingRecipeType type = InFluidCrafting.getRecipes().get(InFluidCrafting.getRecipesMap().get(i));
						
						if(!JiuUtils.other.containItemStackKey(InFluidCrafting.getRecipes(), input, false)) {
							if(!JiuUtils.item.equalsStack(type.getInput(), input)) {
								JiuUtils.entity.sendMessage(sender, "UseFluid: < "+ fluid + " >");
								JiuUtils.entity.sendMessage(sender, "InputItem: < "+ input + " >");
								JiuUtils.entity.sendMessage(sender, "OutputItems: < "+ outputs + " >");
								JiuUtils.entity.sendMessage(sender, "CanConsumeFluid: < "+ canConsumeFluid + " >");
								JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.warn.info", TextFormatting.RED);
								
								InFluidCrafting.addInFluidCrafting(fluid, input, outputs, canConsumeFluid);
								break;
							}else {
								JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.error.info", TextFormatting.GREEN);
							}
						}else {
							JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.error.info", TextFormatting.GREEN);
							break;
						}
					}
				}else if(args.length == 2) {
					String[] inputText = JiuUtils.other.custemSplitString(args[0], "@");
					ItemStack input = new ItemStack(getItemByText(sender, inputText[0]), parseInt(inputText[1]), parseInt(inputText[2]));
					List<ItemStack> outputs = new ArrayList<ItemStack>();
					
					String[] outputText0 = JiuUtils.other.custemSplitString(args[1], "#");
					for(String outText0 : outputText0) {
						String[] outputText1 = JiuUtils.other.custemSplitString(outText0, "@");
						
						ItemStack out = new ItemStack(getItemByText(sender, outputText1[0]), parseInt(outputText1[1]), parseInt(outputText1[2]));
						
						outputs.add(out);
					}
					
					for(int i : InFluidCrafting.getRecipesMap().keySet()) {
						InFluidCraftingRecipeType type = InFluidCrafting.getRecipes().get(InFluidCrafting.getRecipesMap().get(i));
						
						if(!JiuUtils.other.containItemStackKey(InFluidCrafting.getRecipes(), input, false)) {
							if(!JiuUtils.item.equalsStack(type.getInput(), input)) {
								JiuUtils.entity.sendMessage(sender, "UseFluid: < "+ Blocks.WATER.getDefaultState() + " >");
								JiuUtils.entity.sendMessage(sender, "InputItem: < "+ input + " >");
								JiuUtils.entity.sendMessage(sender, "OutputItems: < "+ outputs + " >");
								JiuUtils.entity.sendMessage(sender, "CanConsumeFluid: < false >");
								JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.warn.info", TextFormatting.RED);
								
								InFluidCrafting.addInFluidCrafting(Blocks.WATER.getDefaultState(), input, outputs, false);
								break;
							}else {
								JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.error.info", TextFormatting.GREEN);
							}
						}else {
							JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.error.info", TextFormatting.GREEN);
							break;
						}
					}
				}else {
					JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.0.info");
					JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.add.1.info");
				}
				
//				JiuUtils.entity.sendMessage(sender, "commands size: "+ args.length);
//				JiuUtils.entity.sendMessage(sender, "recipe size: "+ InFluidCrafting.getRecipesMap().size());
			}
			@Override
			public String getUsage(ICommandSender sender) {
				return "command." + this.modid + ".craft.influid.add." + this.getName() +".info";
			}
		}
		
		private static class ForeverAdd extends BaseCommand.CommandNormal {
			public ForeverAdd() {super("f", "jc", 2);}
			
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				
			}
			
			@Override
			public String getUsage(ICommandSender sender) {
				return "command." + this.modid + ".craft.influid.add." + this.getName() +".info";
			}
		}
		
		@Override
		public String getUsage(ICommandSender sender) {
			return "command." + this.modid + ".craft.influid." + this.getName() +".info";
		}
	}
	
	private static class Reload extends BaseCommand.CommandNormal {
		public Reload() {super("reload", "jc", 2);}
		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			InFluidCrafting.removeAllRecipes();
			JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.reload.0.info", TextFormatting.RED);
			
			long time = System.currentTimeMillis();
			
			MinecraftForge.EVENT_BUS.post(new InFluidCraftingEvent(new Recipes(this.modid)));
			
			JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.reload.1.info", TextFormatting.GREEN, "( " + (System.currentTimeMillis() - time)+"ms )");
		}
		
		@Override
		public String getUsage(ICommandSender sender) {
			return "command." + this.modid + ".craft.influid." + this.getName() +".info";
		}
	}
	
	private static class Remove extends BaseCommand.CommandNormal {
		public Remove() {super("remove", "jc", 2);}
		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			if(args.length == 1) {
				String[] outputText = JiuUtils.other.custemSplitString(args[0], "@");
				ItemStack output = new ItemStack(getItemByText(sender, outputText[0]), 1, parseInt(outputText[1]));
				long time = System.currentTimeMillis();
				
				InFluidCrafting.removeCrafting(output);
				JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.remove.success.info", TextFormatting.GREEN, "( " + (System.currentTimeMillis() - time) + "ms )");
			}else if(args.length == 2) {
				Item item = getItemByText(sender, args[0]);
				int meta = parseInt(args[1]);
				ItemStack input = new ItemStack(item, 1, meta);
				long time = System.currentTimeMillis();
				
				InFluidCrafting.removeCrafting(input);
				JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.remove.success.info", TextFormatting.GREEN, "( " + (System.currentTimeMillis() - time) + "ms )");
			}else {
				JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.remove.0.info", TextFormatting.RED);
				JiuUtils.entity.sendMessage(sender, "command.jc.craft.influid.remove.1.info", TextFormatting.RED);
			}
		}
		
		@Override
		public String getUsage(ICommandSender sender) {
			return "command." + this.modid + ".craft.influid." + this.getName() +".info";
		}
	}
	
	private static class RemoveAll extends BaseCommand.CommandNormal {
		public RemoveAll() {super("removeall", "jc", 2);}
		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			if(InFluidCrafting.getRecipes().isEmpty()) {
				JiuUtils.entity.sendMessage(sender, "All recipe has been remove");
			}else {
				long time = System.currentTimeMillis();
				InFluidCrafting.removeAllRecipes();
				JiuUtils.entity.sendMessage(sender, "All recipe remove successful, took( " + (System.currentTimeMillis() - time) + "ms )", TextFormatting.GREEN);
			}
		}
		
		@Override
		public String getUsage(ICommandSender sender) {
			return "command." + this.modid + ".craft.influid." + this.getName() +".info";
		}
	}
}
