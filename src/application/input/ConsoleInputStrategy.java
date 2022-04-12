package application.input;

import application.exceptions.ClosingAppException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleInputStrategy implements InputStrategy{
    private final Scanner scanner;

    {
        scanner = new Scanner(System.in);
    }

    @Override
    public InputStrategyType type() {
        return InputStrategyType.CONSOLE;
    }


    @Override
    public String getInput() {
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e){
            throw new ClosingAppException("System.in has been closed!");
        }
    }
}
