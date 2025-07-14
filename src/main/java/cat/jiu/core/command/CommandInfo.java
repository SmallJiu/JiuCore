package cat.jiu.core.command;

import cat.jiu.core.util.base.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.neoforge.server.command.ModIdArgument;

public class CommandInfo extends BaseCommand.BaseTree {
    public CommandInfo() {
        super("info", 0);
        this
                .addSubCommand(new BaseCommand.BaseTree("jar", 0)
                        .addSubCommand(new BaseCommand.Builder("modid")
                                .level(0)
                                .argument(node->node.then(Commands.argument("modid", ModIdArgument.modIdArgument())))
                                .execute((server, sender, args, ctx) -> {
                                    String modid = ctx.getArgument("package", String.class);
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
                                .argument(node->node.then(Commands.argument("package", StringArgumentType.string())))
                                .execute((server, sender, args, ctx) -> {
                                    String mod_package = ctx.getArgument("package", String.class);
//                                    for (ModInfo mod : FMLLoader.getLoadingModList().getMods()) {
//                                        for (String aPackage : mod.getOwningFile().getFile().getSecureJar().getManifestSigners()) {
//                                            if (aPackage.contains(mod_package)) {
//                                                sender.sendSystemMessage(Component.literal("Mod: " + mod.getModId() + ", File: " + mod.getOwningFile().getFile().getFileName() + "."));
//                                                return Command.SINGLE_SUCCESS;
//                                            }
//                                        }
//                                    }
                                    sender.sendSystemMessage(Component.literal("Not found mod."));
                                    return Command.SINGLE_SUCCESS;
                                })
                                .build())

                ).addSubCommand(new BaseCommand.Builder("te")
                        .level(0)
                        .argument(node->node.then(Commands.argument("pos", BlockPosArgument.blockPos())))
                        .execute((server, sender, args, ctx) -> {
                            if (sender instanceof Entity) {
                                BlockEntity te = ((Entity)sender).getCommandSenderWorld().getBlockEntity(BlockPosArgument.getBlockPos(ctx, "pos"));
                                if (te != null) {
                                    sender.sendSystemMessage(Component.literal("Block entity class: " + te.getClass()));
//                                    sender.sendSystemMessage(Component.literal("Block entity data: " + te.saveWithFullMetadata()));
                                }else {
                                    sender.sendSystemMessage(Component.literal("Not found Block Entity."));
                                }
                                return Command.SINGLE_SUCCESS;
                            }
                            throw new SimpleCommandExceptionType(Component.translatable("permissions.requires.player")).create();
                        })
                        .build()
        );
    }
}
