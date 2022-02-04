package cat.jiu.core.api;

import java.math.BigInteger;

import net.minecraftforge.energy.IEnergyStorage;

public interface IJiuEnergyStorage extends IEnergyStorage {
	BigInteger receiveEnergyWithBigInteger(BigInteger maxReceive, boolean simulate);
    BigInteger extractEnergyWithBigInteger(BigInteger maxExtract, boolean simulate);
    BigInteger getEnergyStoredWithBigInteger();
    BigInteger getMaxEnergyStoredWithBigInteger();
    boolean canExtract();/** {@link IEnergyStorage#canExtract()} */
    boolean canReceive();/** {@link IEnergyStorage#canReceive()} */
    
	    default int receiveEnergyWithInt(int maxReceive, boolean simulate) {
	    	return this.receiveEnergyWithBigInteger(BigInteger.valueOf(maxReceive), simulate).intValue();
	    }
	    default int extractEnergyWithInt(int maxExtract, boolean simulate) {
	    	return this.extractEnergyWithBigInteger(BigInteger.valueOf(maxExtract), simulate).intValue();
	    }
	    default int getEnergyStoredWithInt() {
	    	return this.getEnergyStoredWithBigInteger().intValue();
	    }
	    default int getMaxEnergyStoredWithInt() {
	    	return this.getMaxEnergyStoredWithBigInteger().intValue();
	    }
	    default int inputEnergyWithInt(int maxReceive, boolean simulate) {
	    	return this.receiveEnergyWithInt(maxReceive, simulate);
	    }
	    default int outputEnergyWithInt(int maxExtract, boolean simulate) {
	    	return this.extractEnergyWithInt(maxExtract, simulate);
	    }
	    
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
	    
	    /**
	     * {@link IEnergyStorage#receiveEnergy(int, boolean)}
	     */
	    default int receiveEnergy(int maxReceive, boolean simulate) {
	    	return this.receiveEnergyWithInt(maxReceive, simulate);
	    }
	    /**
	     * {@link IEnergyStorage#extractEnergy(int, boolean)}
	     */
	    default int extractEnergy(int maxExtract, boolean simulate) {
	    	return this.extractEnergyWithInt(maxExtract, simulate);
	    }
	    /**
	     * {@link IEnergyStorage#getEnergyStored()}
	     */
	    default int getEnergyStored() {
	    	return this.getEnergyStoredWithInt();
	    }
	    /**
	     * {@link IEnergyStorage#getMaxEnergyStored()}
	     */
	    default int getMaxEnergyStored() {
	    	return this.getMaxEnergyStoredWithInt();
	    }
}
