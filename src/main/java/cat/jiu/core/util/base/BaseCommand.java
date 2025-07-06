package cat.jiu.core.util.base;

import cat.jiu.core.api.ICommand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import net.minecraftforge.jarjar.nio.util.LambdaExceptionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaseCommand {
    public static class BaseTree implements ICommand {
        protected final Map<String, ICommand> commandMap = new HashMap<>();
        protected final Map<String, ICommand> aliasMap = new HashMap<>();
        protected final String name;
        protected List<String> alias;
        protected int level = 4;
        public BaseTree(String name) {
            this.name = name;
        }
        public BaseTree(String name, int level) {
            this.name = name;
            this.level = level;
        }
        public BaseTree(CommandDispatcher<CommandSourceStack> dispatcher, String name, Consumer<CommandDispatcher<CommandSourceStack>> init) {
            this.name = name;
            if (init!=null) init.accept(dispatcher);
            this.register(dispatcher);
        }

        @Override
        public LiteralCommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> dispatcher) {
            LiteralArgumentBuilder<CommandSourceStack> s;
            if(!this.commandMap.isEmpty() || !this.aliasMap.isEmpty()){
                LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.getName()).requires(this::checkPermission);
                // TODO 注册子命令
                this.commandMap.forEach((k,v) -> builder.then(v.register(dispatcher)).requires(v::checkPermission).executes(v));
                this.aliasMap.forEach((k,v) -> builder.then(v.register(dispatcher)).requires(v::checkPermission).executes(v));
                s = builder;
            }else {
                // TODO 注册空命令树
                s = Commands.literal(this.getName())
                        .requires(this::checkPermission)
                        .executes(this);
            }
            LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(s);

            // TODO 注册别名
            if(this.getAliases()!=null && !this.getAliases().isEmpty()){
                for (String alias : this.getAliases()) {
                    dispatcher.register(Commands.literal(alias).redirect(cmd));
                }
            }
            return cmd;
        }

        public boolean hasSubCommand(String name){
            return this.commandMap.containsKey(name) || this.aliasMap.containsKey(name);
        }

        public ICommand getSubCommand(String name){
            if(this.commandMap.containsKey(name)){
                return this.commandMap.get(name);
            }
            if(this.aliasMap.containsKey(name)){
                return this.aliasMap.get(name);
            }
            return null;
        }
        public <T extends ICommand> BaseTree addSubCommand(T cmd){
            if(this.canAddSubCommand(cmd)){
                this.commandMap.put(cmd.getName(), cmd);
                List<String> alias = cmd.getAliases();
                if(alias!=null && !alias.isEmpty()){
                    alias.forEach(alia-> {
                        if(!this.hasSubCommand(alia)){
                            this.aliasMap.put(alia, cmd);
                        }
                    });
                }
            }
            return this;
        }
        protected <T extends ICommand> boolean canAddSubCommand(T cmd){
            return true;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public List<String> getAliases() {
            return this.alias;
        }
        protected BaseTree addAliases(String name){
            if(this.alias==null) this.alias = new ArrayList<>();
            this.alias.add(name);
            return this;
        }

        @Override
        public boolean checkPermission(CommandSourceStack source) {
            return source.hasPermission(this.getRequiredPermissionLevel());
        }

        public int getRequiredPermissionLevel() {
            return this.level;
        }

        @Override
        public int execute(MinecraftServer server, CommandSource sender, String[] args, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
            return ICommand.SINGLE_SUCCESS;
        }
    }

    public static abstract class Base implements ICommand {
        protected final String name;
        protected final int executeLevel;
        protected List<String> alias;
        public Base(String name, int executeLevel) {
            this.name = name;
            this.executeLevel = executeLevel;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public List<String> getAliases() {
            return this.alias;
        }
        public Base addAliases(String name){
            if(this.alias==null) this.alias = new ArrayList<>();
            this.alias.add(name);
            return this;
        }

        @Override
        public boolean checkPermission(CommandSourceStack source) {
            return source.hasPermission(this.executeLevel);
        }
    }

    public static class Builder {
        protected final String name;
        protected int level = 1;
        protected String[] alias;
        protected Execute execute;
        protected LambdaExceptionUtils.Function_WithExceptions<CommandContext<CommandSourceStack>, Integer, CommandSyntaxException> cmd;
        protected Function<LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> argumentBuilder;
        public Builder(String name) {
            this.name = name;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }
        public Builder execute(Execute execute) {
            this.execute = execute;
            return this;
        }
        public Builder run(LambdaExceptionUtils.Function_WithExceptions<CommandContext<CommandSourceStack>, Integer, CommandSyntaxException> cmd) {
            this.cmd = cmd;
            return this;
        }

        public Builder argument(Function<LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> argumentBuilder) {
            this.argumentBuilder = argumentBuilder;
            return this;
        }

        public Builder alias(String... alias) {
            this.alias = alias;
            return this;
        }

        public Base build() {
            return new Base(this.name, this.level) {
                {
                    if (Builder.this.alias != null) {
                        for (String name : Builder.this.alias) {
                            this.addAliases(name);
                        }
                    }
                }

                @Override
                public int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
                    return Builder.this.cmd != null ? Builder.this.cmd.apply(ctx) : super.run(ctx);
                }

                @Override
                public int execute(MinecraftServer server, CommandSource sender, String[] args, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
                    if (Builder.this.execute != null) {
                        return Builder.this.execute.execute(server, sender, args, ctx);
                    }
                    throw new SimpleCommandExceptionType(Component.literal("Not implement the command, report the mod authors")).create();
                }

                @Override
                public LiteralArgumentBuilder<CommandSourceStack> apply(LiteralArgumentBuilder<CommandSourceStack> node) {
                    return Builder.this.argumentBuilder !=null ? Builder.this.argumentBuilder.apply(node) : super.apply(node);
                }
            };
        }

        @FunctionalInterface
        public interface Execute {
            int execute(MinecraftServer server, CommandSource sender, String[] args, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException;
        }
    }

    public static class CommandArgumentType<T> implements ArgumentType<T> {
        public final List<T> list;
        public final LambdaExceptionUtils.Function_WithExceptions<String, T, CommandSyntaxException> getter;
        public final Function<T, String> toString;

        public CommandArgumentType(LambdaExceptionUtils.Function_WithExceptions<String, T, CommandSyntaxException> getter, Function<T, String> toString, List<T> list) {
            this.list = list;
            this.getter = getter;
            this.toString = toString;
        }
        public CommandArgumentType(LambdaExceptionUtils.Function_WithExceptions<String, T, CommandSyntaxException> getter, Function<T, String> toString, T... list) {
            this.list = new ArrayList<>();
            Collections.addAll(this.list, list);
            this.getter = getter;
            this.toString = toString;
        }

        @Override
        public T parse(StringReader reader) throws CommandSyntaxException {
            int i = reader.getCursor();
            while(reader.canRead() && reader.peek() != ' ') {
                reader.skip();
            }

            String s = reader.getString().substring(i, reader.getCursor());
            return this.getter.apply(s);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(this.list.stream().map(this.toString).toList(), builder);
        }
    }
}
