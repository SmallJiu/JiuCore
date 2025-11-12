package cat.jiu.core.util;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.element.data.NBTData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataUtils {
    public static List<ItemStack> toStack(IData.IListData<?> data) {
        List<ItemStack> list = new ArrayList<>();
        data.foreach((index, stackData) -> list.add(toStack(stackData.getAsMap())));
        return list;
    }
    public static ItemStack toStack(IData.IMapData<?> data) {
        if (data.containsKey("name") && !data.containsKey("id")) {
            data.putData("id", data.getString("name"));
        }
        if (data.containsKey("count") && !data.containsKey("Count")) {
            data.putData("Count", data.getInt("count"));
        }
        if (data.containsKey("nbt") && !data.containsKey("tag")) {
            data.putData("tag", data.getData("nbt", data.emptyMap()));
        }
        return decode(ItemStack.CODEC, data.getData() instanceof CompoundTag ? data : data.transfer(NBTData.map())).orElse(ItemStack.EMPTY);
    }

    public static IData.IListData<?> toData(List<ItemStack> stacks, IData.IListData<?> data) {
        stacks.forEach(stack -> data.putData(toData(stack, data.newMap())));
        return data;
    }
    public static IData.IMapData<?> toData(ItemStack stack, IData.IMapData<?> data) {
        ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).result().ifPresent(e->{
            CompoundTag tag = (CompoundTag) e;
            tag.putString("name", tag.getString("id"));
            tag.putInt("count", stack.getCount());
            tag.putInt("damage", stack.getDamageValue());
            NBTData.map(tag).transfer(data);
        });
        return data;
    }

    public static <T> Optional<T> decode(Codec<T> codec, IData.IMapData<?> data) {
        if (data.getData() instanceof CompoundTag) {
            return codec.parse(NbtOps.INSTANCE,  (CompoundTag) data.getData()).result();
        }else if (data.getData() instanceof JsonObject) {
            return codec.parse(JsonOps.INSTANCE, (JsonElement) data.getData()).result();
        }
        return Optional.empty();
    }

    public static <T> IData.IMapData<?> encode(Codec<T> codec, T in, IData.IMapData<?> data) {
        codec.encodeStart(NbtOps.INSTANCE, in).result().ifPresent(e->
                NBTData.map((CompoundTag) e).transfer(data)
        );
        return data;
    }

    public static <T> T getValue(IForgeRegistry<T> registry, IData.IMapData<?> data, String key) {
        return registry.getValue(data.getLocation(key));
    }
    public static <T> T getValue(IForgeRegistry<T> registry, IData.IMapData<?> data, String key, ResourceLocation fallback) {
        return registry.getValue(data.getLocation(key, fallback));
    }

    @Nullable
    public static MobEffectInstance loadMobEffect(IData.IMapData<?> data) {
        int i = data.getByte("Id") & 0xFF;
        MobEffect mobeffect = MobEffect.byId(i);
        try {
            mobeffect = getValue(ForgeRegistries.MOB_EFFECTS, data, "forge:id");
        } catch (ResourceLocationException ignored) {}

        return mobeffect == null ? null : loadSpecifiedEffect(mobeffect, data);
    }

    private static MobEffectInstance loadSpecifiedEffect(MobEffect effect, IData.IMapData<?> data) {
        int amplifier = data.getByte("Amplifier");
        int duration = data.getInt("Duration");
        boolean ambient = data.getBoolean("Ambient");
        boolean showParticles = data.getBoolean("ShowParticles", true);
        boolean showIcon = data.getBoolean("ShowIcon", showParticles);
        MobEffectInstance hiddenEffect = !data.containsKey("HiddenEffect") ? null : loadSpecifiedEffect(effect, data.getMap("HiddenEffect"));
        Optional<MobEffectInstance.FactorData> factorData = !data.containsKey("FactorCalculationData") ? Optional.empty() : decode(MobEffectInstance.FactorData.CODEC, data.getMap("FactorCalculationData"));

        MobEffectInstance instance = new MobEffectInstance(effect, duration, Math.max(0, amplifier), ambient, showParticles, showIcon, hiddenEffect, factorData);

        if (data.containsKey("CurativeItems")) {
            instance.setCurativeItems(toStack(data.getList("CurativeItems", IData.IMapData.class)));
        }
        return instance;
    }
}
