package cat.jiu.core.capability;

import cat.jiu.core.api.IJiuEnergyStorage;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityJiuEnergy {
	@CapabilityInject(IJiuEnergyStorage.class)
    public static Capability<IJiuEnergyStorage> ENERGY = null;
	static {
		CapabilityManager.INSTANCE.register(IJiuEnergyStorage.class, new IStorage<IJiuEnergyStorage>() {
			@Override
			public NBTBase writeNBT(Capability<IJiuEnergyStorage> capability, IJiuEnergyStorage instance, EnumFacing side) {
				return instance.writeTo(NBTTagCompound.class);
			}

			@Override
			public void readNBT(Capability<IJiuEnergyStorage> capability, IJiuEnergyStorage instance, EnumFacing side, NBTBase nbt) {
				if (nbt instanceof NBTTagString) {
					instance.setEnergy(JiuUtils.big_integer.create(((NBTTagString)nbt).getString()));
				}else if (nbt instanceof NBTTagCompound){
					instance.readFrom(nbt);
				}
			}
		}, () -> new JiuEnergyStorage(10000));
	}
}
