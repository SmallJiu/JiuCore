package cat.jiu.core.capability;

import cat.jiu.core.JiuCore;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public final class EnergyUtils {
	public static final ResourceLocation AllTeEnergy = new ResourceLocation(JiuCore.MODID + ":textures/gui/energy/te_energy.png");
	public static final ResourceLocation AllDeEnergy = new ResourceLocation(JiuCore.MODID + ":textures/gui/energy/de_energy.png");
	public static final ResourceLocation AllEioEnergy = new ResourceLocation(JiuCore.MODID + ":textures/gui/energy/eio_energy.png");

	public IEnergyStorage getFEStorage(ItemStack stack) {
		if(stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			return stack.getCapability(CapabilityEnergy.ENERGY, null);
		}
		return null;
	}
	
	public IEnergyStorage getFEStorage(TileEntity te, EnumFacing side) {
		if(te == null) {
			return null;
		}
		if(te.hasCapability(CapabilityEnergy.ENERGY, side)) {
			return te.getCapability(CapabilityEnergy.ENERGY, side);
		}
		return null;
	}

	public void inputFEEnergy(ItemStack stack, int energy) {
		if(this.getFEStorage(stack) != null) {
			this.getFEStorage(stack).receiveEnergy(energy, false);
		}
	}
	
	public void inputFEEnergy(TileEntity te, EnumFacing side, int energy) {
		if(this.getFEStorage(te, side) != null) {
			this.getFEStorage(te, side).receiveEnergy(energy, false);
		}
	}
	
	public void outputFEEnergy(ItemStack stack, int energy) {
		if(this.getFEStorage(stack) != null) {
			this.getFEStorage(stack).extractEnergy(energy, false);
		}
	}
	
	public void outputFEEnergy(TileEntity te, EnumFacing side, int energy) {
		if(this.getFEStorage(te, side) != null) {
			this.getFEStorage(te, side).extractEnergy(energy, false);
		}
	}
	
	public boolean canInputFEEnergy(ItemStack stack) {
		if(this.getFEStorage(stack) != null) {
			return this.getFEStorage(stack).canReceive();
		}
		return false;
	}
	
	public boolean canInputFEEnergy(TileEntity te, EnumFacing side) {
		if(this.getFEStorage(te, side) != null) {
			return this.getFEStorage(te, side).canReceive();
		}
		return false;
	}
	
	public boolean canOutputFEEnergy(ItemStack stack) {
		if(this.getFEStorage(stack) != null) {
			return this.getFEStorage(stack).canExtract();
		}
		return false;
	}
	
	public boolean canOutputFEEnergy(TileEntity te, EnumFacing side) {
		if(this.getFEStorage(te, side) != null) {
			return this.getFEStorage(te, side).canExtract();
		}
		return false;
	}
	
	public int getFEEnergy(ItemStack stack) {
		if(this.getFEStorage(stack) != null) {
			return this.getFEStorage(stack).getEnergyStored();
		}
		return 0;
	}
	
	public int getFEEnergy(TileEntity te, EnumFacing side) {
		if(this.getFEStorage(te, side) != null) {
			return this.getFEStorage(te, side).getEnergyStored();
		}
		return 0;
	}
	
	public boolean hasFEStorage(ItemStack stack) {
		return this.getFEStorage(stack) != null;
	}
	
	public boolean hasFEStorage(TileEntity te, EnumFacing side) {
		return this.getFEStorage(te, side) != null;
	}
	
	public boolean hasFEEnergy(ItemStack stack) {
		if(this.getFEStorage(stack) != null) {
			return this.getFEEnergy(stack) > 0;
		}
		return false;
	}
	
	public boolean hasFEEnergy(TileEntity te, EnumFacing side) {
		if(this.getFEStorage(te, side) != null) {
			return this.getFEEnergy(te, side) > 0;
		}
		return false;
	}
	
	public int sendFEEnergyTo(ItemStack stack, int energy, boolean simulate) {
		if(energy < 0) {
			return 0;
		}
		if(this.hasFEStorage(stack)) {
			return this.getFEStorage(stack).receiveEnergy(energy, simulate);
		}
		
		return 0;
	}
	
	public int sendFEEnergyToAll(TileEntity tile, int energy, boolean simulate) {
		if(energy < 0 || tile.getWorld().isRemote) {
			return 0;
		}
		int i = 0;
		for(EnumFacing side : EnumFacing.VALUES) {
			i += this.sendFEEnergyTo(tile, energy - i, side, simulate);
		}
		return i;
	}
	
	public int sendFEEnergyTo(TileEntity tile, int energy, EnumFacing[] sides, boolean simulate) {
		if(energy < 0 || tile.getWorld().isRemote) {
			return 0;
		}
		int i = 0;
		for(EnumFacing side : sides) {
			i += this.sendFEEnergyTo(tile.getWorld(), tile.getPos(), energy, side, simulate);
		}
		return i;
	}
	
	public int sendFEEnergyTo(TileEntity tile, int energy, EnumFacing side, boolean simulate) {
		return this.sendFEEnergyTo(tile.getWorld(), tile.getPos(), energy, side, simulate);
	}
	
	public int sendFEEnergyTo(World world, BlockPos pos, int energy, EnumFacing side, boolean simulate) {
		if(energy < 0 || world.isRemote) {
			return 0;
		}
		TileEntity sendTe = world.getTileEntity(pos.offset(side));
		if(this.hasFEStorage(sendTe, side.getOpposite())) {
			return this.getFEStorage(sendTe, side.getOpposite()).receiveEnergy(energy, false);
		}
		return 0;
	}
}
