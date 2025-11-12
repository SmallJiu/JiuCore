package cat.jiu.core.api;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.Arrays;
import java.util.List;

public interface ICommand extends Command<CommandSourceStack>, Lambdas.Function2<RegisterCommandsEvent, LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> {
    String getName();
    List<String> getAliases();
    boolean checkPermission(CommandSourceStack source);

    @Override
    default int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String[] arg = ctx.getInput().split(" ");
        int index = 0;
        for (int i = 0; i < arg.length; i++) {
            if(this.getName().equals(arg[i])){
                index = i+1;
                break;
            }
        }
        return this.execute(ctx.getSource().getServer(), ctx.getSource().source, Arrays.copyOfRange(arg, index, arg.length), ctx);
    }

    int execute(MinecraftServer server, CommandSource sender, String[] args, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException;

    default LiteralCommandNode<CommandSourceStack> register(RegisterCommandsEvent event) {
        LiteralCommandNode<CommandSourceStack> node = event.getDispatcher().register(this.apply(event, Commands.literal(this.getName()).requires(this::checkPermission)));
        List<String> alias = this.getAliases();
        if(alias!=null && !alias.isEmpty()){
            alias.forEach(alia->event.getDispatcher().register(Commands.literal(alia).redirect(node)));
        }
        return node;
    }

    /**
     * @deprecated {@link #register(RegisterCommandsEvent)}
     */
    @Deprecated
    default LiteralCommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(this.apply(Commands.literal(this.getName()).requires(this::checkPermission)));
        List<String> alias = this.getAliases();
        if(alias!=null && !alias.isEmpty()){
            alias.forEach(alia->dispatcher.register(Commands.literal(alia).redirect(node)));
        }
        return node;
    }

    @OnlyIn(Dist.CLIENT)
    default void registerForClient(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(this.apply(Commands.literal(this.getName()).requires(this::checkPermission)));
    }

    @Override
    default LiteralArgumentBuilder<CommandSourceStack> apply(RegisterCommandsEvent event, LiteralArgumentBuilder<CommandSourceStack> node){
        return this.apply(node);
    }
    default LiteralArgumentBuilder<CommandSourceStack> apply(LiteralArgumentBuilder<CommandSourceStack> node){
        return node.executes(this);
    }
}
