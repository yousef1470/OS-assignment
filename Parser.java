import java.util.Scanner;
public class Parser {
    String commandName;
    String[] args;
    
    public boolean parse(String input){
        String[] part = input.split(" ");
        if (part.length > 0) {
            commandName = part[0];
            args = new String[part.length - 1];
            for (int i = 1; i < part.length; i++) {
                args[i - 1] = part[i];
            }
            return true;
        }
        return false;
    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
}
