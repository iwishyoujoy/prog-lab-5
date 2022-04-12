package commands.basic;

import application.exceptions.ClosingAppException;
import commands.AbstractCommand;
import commands.CommandParameters;

public class ExitCommand extends AbstractCommand {
    public ExitCommand() {
        super("exit", "close the application without saving");
    }

    @Override
    public void execute(CommandParameters commandParameters) {
        throw new ClosingAppException();
    }
}
