package cat.jiu.core.capability;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cat.jiu.core.api.IJiuFluidStorage;
import cat.jiu.core.api.annotation.Unfinish;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

@Unfinish
@Deprecated
public class JiuFluidStorage implements IJiuFluidStorage, ICapabilityProvider {
	private final Map<String, FluidType> fluids = Maps.newHashMap();

	public JiuFluidStorage() {
		
	}

	public boolean addFluid(FluidStack fluid, int maxStorage) {
		if(this.fluids.containsKey(fluid.getFluid().getName())) {
			this.fluids.get(fluid.getFluid().getName()).setMaxStorage(maxStorage);
			return true;
		}
		return this.fluids.put(fluid.getFluid().getName(), new FluidType(fluid, maxStorage)) != null;
	}

	@Override
	public BigInteger receiveFluidWithBigInteger(String fluidName, BigInteger maxReceive, boolean simulate) {
		return null;
	}

	@Override
	public BigInteger receiveFluidWithBigInteger(BigInteger maxReceive, boolean simulate) {
		return null;
	}

	@Override
	public FluidStack extractFluidWithBigInteger(String fluidName, BigInteger maxExtract, boolean simulate) {
		return null;
	}

	@Override
	public FluidStack extractFluidWithBigInteger(BigInteger maxExtract, boolean simulate) {
		return null;
	}

	@Override
	public int getFluidStored(String fluidName) {
		return this.fluids.get(fluidName).stack.amount;
	}

	@Override
	public int getMaxFluidStored(String fluidName) {
		return this.fluids.get(fluidName).maxStorage;
	}

	@Override
	public int getFluidStored() {
		return 0;
	}

	@Override
	public int getMaxFluidStored() {
		return 0;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return false;
	}

	@Override
	public IFluidTankProperties[] toFluidProperties() {
		List<IFluidTankProperties> l = Lists.newArrayList();
		for(Entry<String, FluidType> f : this.fluids.entrySet()) {
			l.add(f.getValue().toPropertie());
		}
		return l.toArray(new FluidTankProperties[0]);
	}

	public static class BigFluidStack extends FluidStack {
		private BigInteger bigamount;
		public int amount = this.getAmount();

		public BigFluidStack(Fluid fluid, int amount) {
			super(fluid, amount);
			this.bigamount = BigInteger.valueOf(amount);
		}

		public BigFluidStack(Fluid fluid, int amount, NBTTagCompound tag) {
			super(fluid, amount, tag);
			this.bigamount = BigInteger.valueOf(amount);
		}

		public BigFluidStack(FluidStack stack, int amount) {
			super(stack, amount);
			this.bigamount = BigInteger.valueOf(amount);
		}

		private int getAmount() {
			return this.bigamount.intValue();
		}

		public BigInteger getBigAmount() {
			return this.bigamount;
		}

		public static BigFluidStack loadFluidStackFromNBT(NBTTagCompound nbt) {
			if(nbt == null) {
				return null;
			}
			if(!nbt.hasKey("FluidName", Constants.NBT.TAG_STRING)) {
				return null;
			}

			String fluidName = nbt.getString("FluidName");
			if(FluidRegistry.getFluid(fluidName) == null) {
				return null;
			}
			BigFluidStack stack = new BigFluidStack(FluidRegistry.getFluid(fluidName), nbt.getInteger("Amount"));

			if(nbt.hasKey("Tag")) {
				stack.tag = nbt.getCompoundTag("Tag");
			}
			return stack;
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			nbt.setString("FluidName", FluidRegistry.getFluidName(getFluid()));
			nbt.setString("Amount", this.bigamount.toString());
			if(tag != null) {
				nbt.setTag("Tag", tag);
			}
			return nbt;
		}
	}

	public class FluidType {
		private FluidStack stack;
		private int maxStorage;

		public FluidType(FluidStack stack, int maxStorage) {
			this.stack = stack;
			this.maxStorage = maxStorage;
		}

		public void setMaxStorage(int maxStorage) {
			this.maxStorage = maxStorage;
		}

		public boolean canExtract() {
			return this.stack.amount > 0;
		}

		public boolean canReceive() {
			return this.stack.amount < this.maxStorage;
		}

		public IFluidTankProperties toPropertie() {
			return new FluidTankProperties(this.stack, this.stack.amount);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing side) {
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing side) {
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.toFluidHandler());
		}
		return null;
	}
}
