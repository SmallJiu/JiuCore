package cat.jiu.core.util.base;

import cat.jiu.core.api.ICommand;

import cat.jiu.core.api.Lambdas;
import cat.jiu.core.util.ArrayUtils;
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

import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseCommand {
    public static Builder builder(String name) {
        return new Builder(name);
    }
    public static BaseTree tree(String name) {
        return new BaseTree(name);
    }
    public static BaseTree tree(String name, int level) {
        return new BaseTree(name, level);
    }
    public static class BaseTree implements ICommand {
        protected final Map<String, ICommand> commandMap = new HashMap<>();
        protected final Map<String, ICommand> aliasMap = new HashMap<>();
        protected final String name;
        protected List<String> alias;
        protected int level = 0;
        public BaseTree(String name) {
            this.name = name;
        }
        public BaseTree(String name, int level) {
            this.name = name;
            this.level = level;
        }
        public BaseTree(RegisterCommandsEvent event, String name, Consumer<CommandDispatcher<CommandSourceStack>> init) {
            this.name = name;
            if (init!=null) init.accept(event.getDispatcher());
            this.register(event);
        }

        @Override
        public LiteralCommandNode<CommandSourceStack> register(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            LiteralArgumentBuilder<CommandSourceStack> s;
            if(!this.commandMap.isEmpty() || !this.aliasMap.isEmpty()){
                LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.getName()).requires(this::checkPermission);
                // TODO 注册子命令
                this.commandMap.forEach((k,v) -> builder.then(v.register(event)).requires(v::checkPermission).executes(v));
                this.aliasMap.forEach((k,v) -> builder.then(v.register(event)).requires(v::checkPermission).executes(v));
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
        protected String[] alias = ArrayUtils.EMPTY_STRING_ARRAY;
        protected Lambdas.Function4_WithException<MinecraftServer, CommandSource, String[], CommandContext<CommandSourceStack>, Integer, CommandSyntaxException> execute;
        protected Lambdas.Function_WithException<CommandContext<CommandSourceStack>, Integer, CommandSyntaxException> run;
        protected Lambdas.Function3<ICommand, RegisterCommandsEvent, LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> argument_event;
        protected Function<LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> argument_builder;
        public Builder(String name) {
            this.name = name;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }
        public Builder setNotNeedPermission() {
            return this.level(0);
        }
        public Builder execute(Lambdas.Function4_WithException<MinecraftServer, CommandSource, String[], CommandContext<CommandSourceStack>, Integer, CommandSyntaxException> execute) {
            this.execute = execute;
            return this;
        }
        public Builder run(Lambdas.Function_WithException<CommandContext<CommandSourceStack>, Integer, CommandSyntaxException> cmd) {
            this.run = cmd;
            return this;
        }

        public Builder argument(Function<LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> argument) {
            this.argument_builder = argument;
            return this;
        }
        public Builder argument(BiFunction<ICommand, LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> argument) {
            this.argument_event = (cmd, event, node) -> argument.apply(cmd, node);
            return this;
        }
        public Builder argument(Lambdas.Function3<ICommand, RegisterCommandsEvent, LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>> argument) {
            this.argument_event = argument;
            return this;
        }

        public Builder alias(String... alias) {
            this.alias = alias;
            return this;
        }

        public Base build() {
            return new Base(this.name, this.level) {
                {
                    for (String name : Builder.this.alias) {
                        this.addAliases(name);
                    }
                }

                @Override
                public int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
                    return Builder.this.run != null ? Builder.this.run.apply(ctx) : super.run(ctx);
                }

                @Override
                public int execute(MinecraftServer server, CommandSource sender, String[] args, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
                    if (Builder.this.execute != null) {
                        return Builder.this.execute.apply(server, sender, args, ctx);
                    }
                    throw new SimpleCommandExceptionType(Component.literal("The command is not implement, report the mod authors")).create();
                }

                @Override
                public LiteralArgumentBuilder<CommandSourceStack> apply(RegisterCommandsEvent event, LiteralArgumentBuilder<CommandSourceStack> node) {
                    return Builder.this.argument_event != null ? Builder.this.argument_event.apply(this, event, node) : super.apply(node);
                }

                @Override
                public LiteralArgumentBuilder<CommandSourceStack> apply(LiteralArgumentBuilder<CommandSourceStack> node) {
                    return Builder.this.argument_builder != null ? Builder.this.argument_builder.apply(node) : super.apply(node);
                }
            };
        }
    }

    public static class CommandArgumentType<T> implements ArgumentType<T> {
        public final Function<CommandContext<?>, Iterable<String>> suggestions;
        public final Lambdas.Function_WithException<StringReader, T, CommandSyntaxException> getter;
        public final Function<T, String> toString;

        protected CommandArgumentType(Lambdas.Function_WithException<StringReader, T, CommandSyntaxException> getter, Function<T, String> toString, Function<CommandContext<?>, Iterable<String>> suggestions) {
            this.getter = getter;
            this.toString = toString;
            this.suggestions = suggestions;
        }
        protected CommandArgumentType(Lambdas.Function_WithException<String      , T, CommandSyntaxException> getter, Function<T, String> toString, Function<CommandContext<?>, Iterable<String>> suggestions, int $) {
            this.suggestions = suggestions;
            this.getter = reader-> getter.apply(this.parseString(reader));
            this.toString = toString;
        }
        protected CommandArgumentType(Lambdas.Function_WithException<String, T, CommandSyntaxException> getter, Function<T, String> toString, List<T> suggestions) {
            this.suggestions = ctx-> suggestions.stream().map(toString).collect(Collectors.toList());
            this.getter = reader-> getter.apply(this.parseString(reader));
            this.toString = toString;
        }
        @SafeVarargs
        protected CommandArgumentType(Lambdas.Function_WithException<String, T, CommandSyntaxException> getter, Function<T, String> toString, T... suggestions) {
            this.suggestions = ctx-> Arrays.stream(suggestions).map(toString).collect(Collectors.toList());
            this.getter = reader-> getter.apply(this.parseString(reader));
            this.toString = toString;
        }

        @Override
        public T parse(StringReader reader) throws CommandSyntaxException {
            return this.getter.apply(reader);
        }
        public String parseString(StringReader reader) {
            int i = reader.getCursor();
            while(reader.canRead() && reader.peek() != ' ') {
                reader.skip();
            }
            return reader.getString().substring(i, reader.getCursor());
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(this.suggestions.apply(context), builder);
        }
    }
}
