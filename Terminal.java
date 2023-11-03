import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class Terminal {
    Parser parse;
    String currentPath = new java.io.File(".").getCanonicalPath();

    public Terminal() throws IOException {
    }

    public void echo(String[] arg){
        if(arg.length>0)
            System.out.println(parse.args[0]);
    }
    public String pwd(){
        return currentPath;

    }
    public void cd(String[] args) {
        String[] pathParts = path.split("[,;]"); // Split the input by comma or semicolon

        for (String part : pathParts) {
            if (part.equals("..")) {
                // Change the current directory to the parent directory
                File currentDir = new File(current_dir);
                current_dir = currentDir.getParent();
            } else {
                // Change the current directory to the specified path
                File newDir = new File(part);
                if (newDir.isAbsolute()) {
                    current_dir = newDir.getAbsolutePath();
                } else {
                    current_dir = new File(current_dir, part).getAbsolutePath();
                }
            }
        }
    }
    public void mkdir(String[] args) {
        if (args.length == 0) {
            System.out.println("NO.args to make directory.");
        } else {
            for (String arg : args) {
                if (arg.endsWith(File.separator)) {
                    new File(arg);
                } else {
                    new File(System.getProperty("user.dir"), arg);
                }
            }
        }
    }

    public void rmdir(String directory) {
        File dir = new File(current_dir,directory);

        if (dir.exists()) {
            if (dir.isDirectory()) {
                String[] subFiles = dir.list();
                if (subFiles != null && subFiles.length == 0) {
                    if (dir.delete()) {
                        System.out.println("Directory removed: " + dir.getAbsolutePath());
                    } else {
                        System.out.println("Failed to remove directory: " + dir.getAbsolutePath());
                    }
                } else {
                    System.out.println("Directory is not empty: " + dir.getAbsolutePath());
                }
            } else {
                System.out.println("Not a directory: " + dir.getAbsolutePath());
            }
        } else {
            System.out.println("Directory does not exist: " + dir.getAbsolutePath());
        }
    }

    public void ls() {
        File currentDir = new File(System.getProperty("user.dir"));
        if (currentDir.exists() && currentDir.isDirectory()) {
            File[] files = currentDir.listFiles();
            if (files != null && files.length > 0) {
                Arrays.sort(files);
                for (File file : files) {
                    System.out.println(file.getName());
                }
                } else {
                    System.out.println("The current directory is empty.");
                }
            }
        else {
                System.out.println("Failed to list the contents of the current directory.");
        }
    }

    public void ls_r() {
        File currentDir = new File(System.getProperty("user.dir"));
        if (currentDir.exists() && currentDir.isDirectory()) {
            File[] files = currentDir.listFiles();
            if (files != null && files.length > 0) {
                for (int i = files.length - 1; i >= 0; i--) {
                    System.out.println(files[i].getName());
                }
            } else {
                System.out.println("The current directory is empty.");
            }
        } else {
            System.out.println("Failed to list the contents of the current directory.");
        }
    }

    public void rm(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: rm <file_name>");
            return;
        }
        String fileName = args[0];
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("File removed : " + fileName);
            } else {
                System.out.println("Failed to remove this file: " + fileName);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

    public void touch(String filePath) {
        File file = new File(current_dir, filePath);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists or creation failed: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Failed to create the file: " + e.getMessage());
        }
    }


    public void chooseCommandAction(String input) {
        parser.parse(input);
        String commandName = parser.getCommandName();
        String[] args = parser.getArgs();
        String argument;

        switch (commandName) {
            case "pwd":
                System.out.println(pwd());
                break;
            case "cd":
                argument = args.length > 0 ? args[0] : "";
                cd(argument);
                break;
            case "ls":
                argument = args.length > 0 ? args[0] : "";
                if (argument.equals("-r")) {
                    lsReverse();
                } else {
                    ls();
                }
                break;
            case "mkdir":
                if (args.length > 0) {
                    mkdir(args);
                } else {
                    System.out.println("No directory name specified.");
                }
                break;
            case "rmdir":
                if (args.length > 0) {
                    rmdir(args[0]);
                } else {
                    System.out.println("No directory specified.");
                }
                break;
            case "touch":
                if (args.length > 0) {
                    touch(args[0]);
                } else {
                    System.out.println("No file path specified.");
                }
                break;
            case "rm":
                if (args.length > 0) {
                    rm(args[0]);
                } else {
                    System.out.println("No file path specified.");
                }
                break;
            case "exit":
                break;
            default:
                System.out.println("Command not found: " + commandName);
        }
    }



    public static void main(String[] args) throws IOException {
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);
        terminal.current_dir = System.getProperty("user.dir");

        while (true) {
            System.out.print(terminal.current_dir + "> ");
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            } else {
                terminal.chooseCommandAction(input);
            }
        }

        scanner.close();
    }
    }
}
