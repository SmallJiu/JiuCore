package cat.jiu.core.command;

import cat.jiu.core.util.base.BaseCommand;

public class CommandJiuCore extends BaseCommand.BaseTree {
    public CommandJiuCore() {
        super("jc");
        this.addSubCommand(new CommandInfo());
    }
}
