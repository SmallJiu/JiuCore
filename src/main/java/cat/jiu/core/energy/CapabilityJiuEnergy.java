package cat.jiu.core.energy;

import java.math.BigInteger;

import cat.jiu.core.api.IJiuEnergyStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityJiuEnergy {
	@CapabilityInject(IJiuEnergyStorage.class)
    public static Capability<IJiuEnergyStorage> ENERGY = null;
	
	public static void register() {
        CapabilityManager.INSTANCE.register(IJiuEnergyStorage.class, new IStorage<IJiuEnergyStorage>() {
			@Override
			public NBTBase writeNBT(Capability<IJiuEnergyStorage> capability, IJiuEnergyStorage instance, EnumFacing side) {
				return new NBTTagString(instance.getEnergyStoredWithBigInteger().toString());
			}
			
			@Override
			public void readNBT(Capability<IJiuEnergyStorage> capability, IJiuEnergyStorage instance, EnumFacing side, NBTBase nbt) {
				if(instance instanceof JiuEnergyStorage) {
					((JiuEnergyStorage) instance).energy = new BigInteger(((NBTTagString)nbt).getString());
				}else {
					 throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
				}
			}
        	
        }, () -> new JiuEnergyStorage(10000));
    }
}
