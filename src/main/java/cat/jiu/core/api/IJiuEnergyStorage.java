package cat.jiu.core.api;

import java.math.BigInteger;

import net.minecraftforge.fml.common.Optional;

public interface IJiuEnergyStorage  {
	BigInteger receiveEnergyWithBigInteger(BigInteger maxReceive, boolean simulate);
    BigInteger extractEnergyWithBigInteger(BigInteger maxExtract, boolean simulate);
    BigInteger getEnergyStoredWithBigInteger();
    BigInteger getMaxEnergyStoredWithBigInteger();
    boolean canExtract();
    boolean canReceive();
    IJiuEnergyStorage copy();
	    
	    default long receiveEnergyWithLong(long maxReceive, boolean simulate) {
	    	return this.receiveEnergyWithBigInteger(BigInteger.valueOf(maxReceive), simulate).longValue();
	    };
	    default long extractEnergyWithLong(long maxExtract, boolean simulate) {
	    	return this.extractEnergyWithBigInteger(BigInteger.valueOf(maxExtract), simulate).longValue();
	    };
	    default long getEnergyStoredWithLong() {
	    	return this.getEnergyStoredWithBigInteger().longValue();
	    }
	    default long getMaxEnergyStoredWithLong() {
	    	return this.getMaxEnergyStoredWithBigInteger().longValue();
	    }
	    default long inputEnergyWithLong(long maxReceive, boolean simulate) {
	    	return this.receiveEnergyWithLong(maxReceive, simulate);
	    }
	    default long outputEnergyWithLong(long maxExtract, boolean simulate) {
	    	return this.extractEnergyWithLong(maxExtract, simulate);
	    }
	    
	    default BigInteger inputEnergyWithBigInteger(BigInteger maxReceive, boolean simulate) {
	    	return this.receiveEnergyWithBigInteger(maxReceive, simulate);
	    }
	    default BigInteger outputEnergyWithBigInteger(BigInteger maxExtract, boolean simulate) {
	    	return this.extractEnergyWithBigInteger(maxExtract, simulate);
	    }
	    default String toStringInfo() {
	    	return "MaxEnergy:" + this.getMaxEnergyStoredWithBigInteger()
	    		 + ", Energy:" + this.getEnergyStoredWithBigInteger();
	    }
	    
	    @Optional.Method(modid = "redstoneflux")
	    default cofh.redstoneflux.api.IEnergyStorage toRFStorage() {
	    	final IJiuEnergyStorage storage = this;
	    	return new cofh.redstoneflux.api.IEnergyStorage() {
	    		public int receiveEnergy(int maxReceive, boolean simulate) {return storage.receiveEnergyWithBigInteger(BigInteger.valueOf(maxReceive), simulate).intValue();}
				public int extractEnergy(int maxExtract, boolean simulate) {return storage.extractEnergyWithBigInteger(BigInteger.valueOf(maxExtract), simulate).intValue();}
				public int getEnergyStored() {return storage.getEnergyStoredWithBigInteger().intValue();}
				public int getMaxEnergyStored() {return storage.getMaxEnergyStoredWithBigInteger().intValue();}
	    	};
	    }
	    
	    default net.minecraftforge.energy.IEnergyStorage toFEStorage() {
	    	final IJiuEnergyStorage storage = this;
	    	return new net.minecraftforge.energy.IEnergyStorage() {
				public int receiveEnergy(int maxReceive, boolean simulate) {return storage.receiveEnergyWithBigInteger(BigInteger.valueOf(maxReceive), simulate).intValue();}
				public int extractEnergy(int maxExtract, boolean simulate) {return storage.extractEnergyWithBigInteger(BigInteger.valueOf(maxExtract), simulate).intValue();}
				public int getEnergyStored() {return storage.getEnergyStoredWithBigInteger().intValue();}
				public int getMaxEnergyStored() {return storage.getMaxEnergyStoredWithBigInteger().intValue();}
				public boolean canExtract() {return storage.canExtract();}
				public boolean canReceive() {return storage.canReceive();}
	    	};
	    }
}
