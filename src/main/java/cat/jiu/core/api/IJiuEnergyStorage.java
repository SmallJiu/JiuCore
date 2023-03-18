package cat.jiu.core.api;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

import cat.jiu.core.api.handler.ISerializable;
import cat.jiu.core.capability.CapabilityJiuEnergy;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.sql.SQLValues;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

public interface IJiuEnergyStorage extends ISerializable, ICapabilityProvider {
	BigInteger receiveEnergyWithBigInteger(BigInteger maxReceive, boolean simulate);
    BigInteger extractEnergyWithBigInteger(BigInteger maxExtract, boolean simulate);
    
    BigInteger getEnergyStoredWithBigInteger();
    BigInteger getMaxEnergyStoredWithBigInteger();
    BigInteger getMaxReceiveWithBigInteger();
    BigInteger getMaxExtractWithBigInteger();
    
    void setEnergy(BigInteger energy);
    void setMaxEnergy(BigInteger max);
    void setMaxReceive(BigInteger max);
    void setMaxExtract(BigInteger max);
    
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
	    
	    default BigInteger extractEnergy(IEnergyStorage capability, long maxExtract, boolean simulate) {
	    	return this.extractEnergy(capability, BigInteger.valueOf(maxExtract), simulate);
	    }
	    default BigInteger extractEnergy(IEnergyStorage capability, BigInteger maxExtract, boolean simulate) {
	    	int[] extracts = JiuUtils.other.perseInt(maxExtract);
	    	BigInteger extract = BigInteger.ZERO;
	    	for(int i = 0; i < extracts.length; i++) {
	    		extract = extract.add(BigInteger.valueOf(capability.receiveEnergy(this.extractEnergyWithBigInteger(BigInteger.valueOf(extracts[i]), simulate).intValue(), simulate)));
			}
	    	return extract;
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
	    
	    
	    @Override
	    default void read(JsonObject json) {
	    	if(json!=null) {
	    		this.setEnergy(JiuUtils.big_integer.create(json.get("Energy").getAsString()));
	    		this.setMaxEnergy(JiuUtils.big_integer.create(json.get("MaxEnergyStored").getAsString()));
	    		this.setMaxReceive(JiuUtils.big_integer.create(json.get("MaxOutput").getAsString()));
	    		this.setMaxExtract(JiuUtils.big_integer.create(json.get("MaxInput").getAsString()));
	    	}
	    }
	    @Override
	    default JsonObject write(JsonObject json) {
	    	if(json==null) json = new JsonObject();
	    	json.addProperty("Energy", this.getEnergyStoredWithBigInteger().toString());
	    	json.addProperty("MaxEnergyStored", this.getMaxEnergyStoredWithBigInteger().toString());
	    	json.addProperty("MaxOutput", this.getMaxExtractWithBigInteger().toString());
	    	json.addProperty("MaxInput", this.getMaxReceiveWithBigInteger().toString());
	    	return json;
	    }
	    
	    @Override
	    default void read(NBTTagCompound nbt) {
	    	if(nbt!=null) {
	    		this.setEnergy(JiuUtils.big_integer.create(nbt.getString("Energy")));
	    		this.setMaxEnergy(JiuUtils.big_integer.create(nbt.getString("MaxEnergyStored")));
	    		this.setMaxReceive(JiuUtils.big_integer.create(nbt.getString("MaxOutput")));
	    		this.setMaxExtract(JiuUtils.big_integer.create(nbt.getString("MaxInput")));
	    	}
	    }
	    @Override
	    default NBTTagCompound write(NBTTagCompound nbt) {
	    	if(nbt==null) nbt = new NBTTagCompound();
	    	nbt.setString("Energy", this.getEnergyStoredWithBigInteger().toString());
			nbt.setString("MaxEnergyStored", this.getMaxEnergyStoredWithBigInteger().toString());
			nbt.setString("MaxOutput", this.getMaxExtractWithBigInteger().toString());
			nbt.setString("MaxInput", this.getMaxReceiveWithBigInteger().toString());
	    	return nbt;
	    }
	    
	    @Override
	    default void read(ResultSet result) throws SQLException {
	    	if(result!=null) {
	    		this.setEnergy(JiuUtils.big_integer.create(result.getString("Energy")));
	    		this.setMaxEnergy(JiuUtils.big_integer.create(result.getString("MaxEnergyStored")));
	    		this.setMaxReceive(JiuUtils.big_integer.create(result.getString("MaxOutput")));
	    		this.setMaxExtract(JiuUtils.big_integer.create(result.getString("MaxInput")));
	    	}
	    }
	    @Override
	    default SQLValues write(SQLValues value) {
	    	if(value==null) value = new SQLValues();
	    	value.put("Energy", this.getEnergyStoredWithBigInteger().toString());
	    	value.put("MaxEnergyStored", this.getMaxEnergyStoredWithBigInteger().toString());
	    	value.put("MaxOutput", this.getMaxExtractWithBigInteger().toString());
	    	value.put("MaxInput", this.getMaxReceiveWithBigInteger().toString());
	    	return value;
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
	    
	    @Override
	    default boolean hasCapability(Capability<?> capability, EnumFacing facing) {
	    	return capability == CapabilityJiuEnergy.ENERGY;
	    }
	    @Override
	    default <T> T getCapability(Capability<T> capability, EnumFacing facing) {
	    	if(this.hasCapability(capability, facing)) {
	    		return CapabilityJiuEnergy.ENERGY.cast(this);
	    	}
	    	return null;
	    }
}
