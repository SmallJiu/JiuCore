package cat.jiu.core.command;

import cat.jiu.core.CoreMain;
import cat.jiu.core.api.IData;
import cat.jiu.core.util.DataUtils;
import cat.jiu.core.util.DevMessageEvent;
import cat.jiu.core.util.JsonUtils;
import cat.jiu.core.util.SideProxy;
import cat.jiu.core.util.base.BaseCommand;
import cat.jiu.core.util.client.config.GuiConfig;
import cat.jiu.core.util.element.data.JsonData;
import cat.jiu.core.util.element.data.NBTData;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.server.command.ModIdArgument;

import java.io.File;

public class CommandJiuCore extends BaseCommand.BaseTree {
    public CommandJiuCore() {
        super("jc");
        this
                .addSubCommand(new BaseCommand.Builder("test")
                        .level(0)
                        .run(ctx->{
                            if (false) {
                                try {
                                    IData.IMapData<?> data = DataUtils.toData(new ItemStack(Items.DIAMOND, 9), JsonData.map());
                                    JsonUtils.toJsonFileThrow("C:/test.json", data.getData(), true);
                                    IData.IMapData<CompoundTag> nbtMap = data.transfer(NBTData.map());
                                    NbtIo.write(nbtMap.getData(), new File("C:/test.nbt"));
                                    CoreMain.LOGGER.info(DevMessageEvent.DEV, "nbtMap: {}", nbtMap.getData());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .build())

                .addSubCommand(new BaseCommand.BaseTree("info", 0)
                        .addSubCommand(new BaseCommand.BaseTree("jar", 0)
                                .addSubCommand(new BaseCommand.Builder("modid")
                                        .level(0)
                                        .argument((cmd, node)->node.then(Commands.argument("modid", ModIdArgument.modIdArgument()).executes(cmd)))
                                        .execute((server, sender, args, ctx) -> {
                                            String modid = ctx.getArgument("modid", String.class);
                                            ModFileInfo mod = FMLLoader.getLoadingModList().getModFileById(modid);
                                            if (mod!=null) {
                                                sender.sendSystemMessage(Component.literal("Mod: " + modid + ", File: " + mod.getFile().getFileName() + "."));
                                            }else {
                                                sender.sendSystemMessage(Component.literal("Not found mod."));
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .build())
                                .addSubCommand(new BaseCommand.Builder("package")
                                        .level(0)
                                        .argument((cmd, node)->node.then(Commands.argument("package", StringArgumentType.string()).executes(cmd)))
                                        .execute((server, sender, args, ctx) -> {
                                            String mod_package = ctx.getArgument("package", String.class);
                                            for (ModInfo mod : FMLLoader.getLoadingModList().getMods()) {
                                                for (String aPackage : mod.getOwningFile().getFile().getSecureJar().getPackages()) {
                                                    if (aPackage.contains(mod_package)) {
                                                        sender.sendSystemMessage(Component.literal("Mod: " + mod.getModId() + ", File: " + mod.getOwningFile().getFile().getFileName() + "."));
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                }
                                            }
                                            sender.sendSystemMessage(Component.literal("Not found mod."));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .build())

                        ).addSubCommand(new BaseCommand.Builder("te")
                                .level(0)
                                .argument((cmd, node)->node.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(cmd)))
                                .execute((server, sender, args, ctx) -> {
                                    BlockEntity te = ctx.getSource().getLevel().getBlockEntity(BlockPosArgument.getBlockPos(ctx, "pos"));
                                    if (te != null) {
                                        sender.sendSystemMessage(Component.literal("Block entity class: " + te.getClass()));
                                        sender.sendSystemMessage(Component.literal("Block entity data: " + te.serializeNBT()));
                                    }else {
                                        sender.sendSystemMessage(Component.literal("Not found Block Entity."));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .build()
                        )
                );
    }
}
