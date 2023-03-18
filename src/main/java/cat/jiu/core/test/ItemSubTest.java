package cat.jiu.core.test;

import java.math.BigInteger;
import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.events.item.ItemInPlayerEvent;
import cat.jiu.core.events.item.ItemInWorldEvent;
import cat.jiu.core.capability.CapabilityJiuEnergy;
import cat.jiu.core.capability.EnergyUtils;
import cat.jiu.core.capability.JiuEnergyStorage;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseItem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSubTest extends BaseItem.Normal {
	public final String energyName = "JiuE";
	public ItemSubTest() {
		super(JiuCore.MODID, "test_item", JiuCore.CORE, true);
		this.setMaxMetadata(16);
		MinecraftForge.EVENT_BUS.register(this);
		Init.ITEMS.add(this);
	}
	
	boolean fly = false;
	
	@SideOnly(Side.CLIENT)
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.capabilities.setFlySpeed(0.09F);
		
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItemMainhand());
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItemMainhand();
		NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setBoolean("TestEnergy", !nbt.getBoolean("TestEnergy"));
		stack.setTagCompound(nbt);
		
		String[] i = new String[] {"St", "ri", "ng"};
		JiuUtils.nbt.setItemNBT(stack, "TestStringArray", i);
		
		JiuUtils.nbt.setItemNBT(stack, "TestBigInt", new BigInteger("1"));
		
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		EnergyUtils util = new EnergyUtils();
		NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
		tooltip.add("JiuE: " + util.getFEEnergy(stack) + "/" +  Long.MAX_VALUE + " | " + nbt.getBoolean("TestEnergy"));
		tooltip.add(nbt.getInteger("WorldM") + "/" + nbt.getInteger("WorldS") + "/" + nbt.getInteger("WorldTick"));
		tooltip.add("NBTValue: " + nbt.toString());
		tooltip.add("========");
		for(String i : JiuUtils.nbt.getItemNBTStringArray(stack, "TestStringArray")) {
			tooltip.add(i);
		}
//		tooltip.add("========");
//		for(int i : JiuUtils.nbt.getItemNBTIntArray(stack, "TestIntArray")) {
//			tooltip.add(i+"");
//		}
//		JiuUtils.nbt.addItemNBT(stack, "TestBigInt", JiuUtils.nbt.getItemNBTBigInteger(stack, "TestBigInt"));
		tooltip.add("========");
		if(stack.hasCapability(CapabilityJiuEnergy.ENERGY, null)) {
			tooltip.add(stack.getCapability(CapabilityJiuEnergy.ENERGY, null).toStringInfo());
		}
	}
	
	private void addEnergy(EnergyUtils util, ItemStack stack) {
		util.inputFEEnergy(stack, 1);
	}
	
	@SubscribeEvent
	public void onItemInPlayerInventoryTick(ItemInPlayerEvent.InInventory event) {
		if(event.stack.getItem() == this) {
			NBTTagCompound nbt = event.stack.getTagCompound() != null ? event.stack.getTagCompound() : new NBTTagCompound();
			if(nbt.getBoolean("TestEnergy")) {
				this.addEnergy(JiuUtils.energy, event.stack);
					
				JiuUtils.entity.sendMessage(event.getEntityPlayer(), JiuUtils.energy.getFEEnergy(event.stack) +  "/" +  Long.MAX_VALUE + ": Inv", TextFormatting.GREEN);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemInPlayerHandTick(ItemInPlayerEvent.InHand event) {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = event.stack;
		if(stack.getItem() == this) {
			NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
			if(nbt.getBoolean("TestEnergy")) {
				this.addEnergy(JiuUtils.energy, stack);
				JiuUtils.entity.sendMessage(player, JiuUtils.energy.getFEEnergy(stack)+ "/" +  Long.MAX_VALUE +" :MainHand", TextFormatting.GREEN);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemInWorldTick(ItemInWorldEvent.InWorld event) {
		EntityItem item = event.item;
		World world = item.getEntityWorld();
		BlockPos pos = item.getPosition();
		if(item.getItem().getItem() == this) {
			NBTTagCompound nbt = item.getItem().getTagCompound() != null ? item.getItem().getTagCompound() : new NBTTagCompound();
			if(nbt.getBoolean("TestEnergy")) {
				if(JiuUtils.item.equalsStack(item.getItem(), new ItemStack(this), false, false)) {
					this.addEnergy(JiuUtils.energy, item.getItem());
					JiuUtils.entity.sendMessageToAllPlayer(world, JiuUtils.energy.getFEEnergy(item.getItem())+": World", TextFormatting.GREEN);
				}
			}else {
				if(item.getItem().getItem() == this || item.getItem().getItem() == Item.getItemFromBlock(Init.TestBlock)) {
					if(!JiuUtils.entity.isEntityInFluid(world, pos)) {
						
						nbt.setInteger("WorldTick", nbt.getInteger("WorldTick") + 1);
						if(nbt.getInteger("WorldTick") >= 20) {
							nbt.setInteger("WorldTick", 0);
							nbt.setInteger("WorldS", nbt.getInteger("WorldS")+1);
						}
						if(nbt.getInteger("WorldS") >= 60) {
							nbt.setInteger("WorldS", 0);
							nbt.setInteger("WorldM", nbt.getInteger("WorldM")+1);
						}
						
						nbt.setInteger("WorldTick", nbt.getInteger("WorldTick"));
						nbt.setInteger("WorldS", nbt.getInteger("WorldS"));
						nbt.setInteger("WorldM", nbt.getInteger("WorldM"));
						
						item.getItem().setTagCompound(nbt);
						
						NBTTagCompound enbt = item.getItem().getTagCompound();
						
						if(enbt.getInteger("WorldTick") >= 10) {
							if(enbt.getInteger("WorldS") >= 1) {
								if(item.getItem().getMetadata() == 65535) {
									JiuUtils.item.spawnAsEntity(world, pos, new ItemStack(Init.TestBlock, 1, 0));
								}else {
									ItemStack i = new ItemStack(item.getItem().getItem(), item.getItem().getCount(), item.getItem().getMetadata() + 1);
									NBTTagCompound tnbt = nbt.copy();
									tnbt.setInteger("WorldTick", 0);
									tnbt.setInteger("WorldS", 0);
									i.setTagCompound(tnbt);
									BlockPos tpos = item.getPosition();
									JiuUtils.item.spawnAsEntity(world, pos, i);
									item.setPosition(tpos.getX(), tpos.getY(), tpos.getZ());
								}
								
								JiuUtils.entity.sendMessageToAllPlayer(world, "In World: Meta: " + item.getItem().getMetadata() + " Y: " + item.getPosition().getY(), TextFormatting.GREEN);
								item.getItem().setCount(0);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		String energy = JiuUtils.nbt.getItemNBTString(stack, "Energy");
		if(energy.isEmpty()) energy = "0";
		return new JiuEnergyStorage(BigInteger.valueOf(Long.MAX_VALUE), BigInteger.valueOf(10), BigInteger.valueOf(10), new BigInteger(energy));
	}
}
