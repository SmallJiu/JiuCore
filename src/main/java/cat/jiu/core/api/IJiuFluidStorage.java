package cat.jiu.core.api;

import java.math.BigInteger;

import cat.jiu.core.api.annotation.Unfinish;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

@Unfinish
@Deprecated
public interface IJiuFluidStorage {
	default BigInteger receiveFluidWithBigInteger(FluidStack fluids, boolean simulate) {
		return this.receiveFluidWithBigInteger(fluids.getFluid().getName(), BigInteger.valueOf(fluids.amount),  simulate);
	}
	BigInteger receiveFluidWithBigInteger(String fluidName, BigInteger maxReceive, boolean simulate);
	BigInteger receiveFluidWithBigInteger(BigInteger maxReceive, boolean simulate);
	
	default FluidStack extractFluidWithBigInteger(FluidStack fluids, boolean simulate) {
		return this.extractFluidWithBigInteger(fluids.getFluid().getName(), BigInteger.valueOf(fluids.amount),  simulate);
	}
	FluidStack extractFluidWithBigInteger(String fluidName, BigInteger maxExtract, boolean simulate);
	FluidStack extractFluidWithBigInteger(BigInteger maxExtract, boolean simulate);
    
	int getFluidStored(String fluidName);
	int getMaxFluidStored(String fluidName);
    
	int getFluidStored();
	int getMaxFluidStored();
	
    boolean canExtract();
    boolean canReceive();
    
    	default long receiveFluidWithLong(FluidStack fluids, boolean simulate) {
    		return this.receiveFluidWithBigInteger(fluids.getFluid().getName(), BigInteger.valueOf(fluids.amount), simulate).longValue();
		}
    	default long receiveFluidWithLong(String fluidName, int maxDrain, boolean simulate) {
    		return this.receiveFluidWithBigInteger(fluidName, BigInteger.valueOf(maxDrain), simulate).longValue();
    	}
    	default long receiveFluidWithLong(int maxDrain, boolean simulate) {
    		return this.receiveFluidWithBigInteger(BigInteger.valueOf(maxDrain), simulate).longValue();
    	}
    	
    	default FluidStack extractFluidWithLong(FluidStack fluids, boolean simulate) {
    		return this.extractFluidWithBigInteger(fluids.getFluid().getName(), BigInteger.valueOf(fluids.amount), simulate);
    	}
    	default FluidStack extractFluidWithLong(String fluidName, int maxDrain, boolean simulate) {
    		return this.extractFluidWithBigInteger(fluidName, BigInteger.valueOf(maxDrain), simulate);
    	}
    	default FluidStack extractFluidWithLong(int maxDrain, boolean simulate) {
    		return this.extractFluidWithBigInteger(BigInteger.valueOf(maxDrain), simulate);
    	}
    	
    	IFluidTankProperties[] toFluidProperties();
    	
	    default IFluidHandler toFluidHandler() {
	    	final IJiuFluidStorage storage = this;
	    	return new IFluidHandler() {
				@Override
				public IFluidTankProperties[] getTankProperties() {
					return storage.toFluidProperties();
				}

				@Override
				public int fill(FluidStack fluid, boolean simulate) {
					return (int) storage.receiveFluidWithLong(fluid, simulate);
				}

				@Override
				public FluidStack drain(FluidStack fluid, boolean simulate) {
					return storage.extractFluidWithLong(fluid, simulate);
				}

				@Override
				public FluidStack drain(int maxDrain, boolean simulate) {
					return storage.extractFluidWithLong(maxDrain, simulate);
				}
	    	};
	    }
}
