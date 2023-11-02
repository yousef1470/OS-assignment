import java.util.Vector;

public class Parser {
    String CommandName;
    String[] args;
    public boolean parse(String input) {
        String[] parts = input.split(" ");
        if (parts.length >= 1) {
            CommandName = parts[0];
            if (parts.length > 1) {
                args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, parts.length - 1);
            } else {
                args = new String[0];
            }
            return true;
        } else {
            return false;
        }
    }

    public String getCommandName() {
        return CommandName;
    }

    public String[] getArgs() {
        return args;
    }
}
