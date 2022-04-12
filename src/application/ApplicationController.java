package application;

import application.exceptions.ClosingAppException;
import application.exceptions.ScriptEndedException;
import application.input.ConsoleInputStrategy;
import application.input.InputManager;
import application.input.InputStrategyType;
import application.input.ScriptInputStrategy;
import collection.CollectionItem;
import collection.CollectionManager;
import collection.data.exceptions.InvalidDataException;
import collection.exceptions.CollectionException;
import commands.Command;
import commands.CommandManager;
import commands.CommandParameters;
import commands.basic.*;
import commands.exceptions.CommandException;
import commands.exceptions.OpenFileException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationController<T extends CollectionItem> {
    public static String path;

    static {
        path = "default.xml";
    }

    private final CollectionManager<T> collectionManager;
    private final CommandManager commandManager;
    private final InputManager inputManager;

    public ApplicationController(CollectionManager<T> collectionManager) {
        this.collectionManager = collectionManager;
        commandManager.add(new HelpCommand(commandManager));
        commandManager.add(new InfoCommand(collectionManager));
        commandManager.add(new ShowCommand(collectionManager));
        commandManager.add(new ClearCommand(collectionManager));
        commandManager.add(new RemoveLastCommand(collectionManager));
        commandManager.add(new ExitCommand());
        commandManager.add(new SortCommand(collectionManager));
        commandManager.add(new RemoveByIdCommand(collectionManager));
        commandManager.add(new AddCommand<>(collectionManager, inputManager));
        commandManager.add(new UpdateCommand<>(collectionManager, inputManager));
        commandManager.add(new RemoveLowerCommand<>(collectionManager, inputManager));
        commandManager.add(new ExecuteScriptCommand(inputManager));
        commandManager.add(new SaveCommand(collectionManager));
    }

    {
        inputManager = new InputManager(new ConsoleInputStrategy());
        commandManager = new CommandManager();
    }
    public void addCommand(Command command){
        commandManager.add(command);
    }
    public void run(String path){
        try {
            if (path != null) openFile(path);
        } catch (OpenFileException e) {
            ConsolePrinter.println(e.getMessage());
        }
        while(true) {
            try {
                ConsolePrinter.request("Enter command: ");
                String[] input = inputManager.getInput().split(" ");
                String commandName = (input.length != 0) ? input[0] : "";
                List<String> param = new ArrayList<>(Arrays.asList(input));
                param.remove(0);
                commandManager.executeCommand(commandName, new CommandParameters(param));
            } catch (CommandException | CollectionException | InvalidDataException | ScriptEndedException e){
                if(inputManager.type()== InputStrategyType.SCRIPT) {
                    inputManager.setInputStrategy(new ConsoleInputStrategy());
                }
                ConsolePrinter.println(e.getMessage());
            } catch (ClosingAppException e){
                ConsolePrinter.println(e.getMessage());
                break;
            }

        }
    }

    public void openFile(String path) {
        ApplicationController.path = path;
        try {
            File inputFile;
            inputFile = new File(path);
            if(!inputFile.canRead()) throw new OpenFileException("xml", "Can't read");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            collectionManager.parse(doc.getDocumentElement());

        } catch (ParserConfigurationException | IOException | InvalidDataException | CollectionException | SAXException e) {
            throw new OpenFileException("xml", e.getMessage());
        }
    }
}
