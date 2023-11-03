import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Terminal {
    Parser parse;
    String currentPath = new java.io.File(".").getCanonicalPath();
    private String current_dir;

    public Terminal() throws IOException {
    }

    public void echo(String[] arg){
        if(arg.length>0)
            System.out.println(parse.args[0]);
    }
    public String pwd(){
        return current_dir;

    }

    public void cd(String path) {
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
            System.out.println("Usage: mkdir <directory1> [<directory2> ...]");
        } else {
            for (String arg : args) {
                File newDir;
                if (arg.endsWith(File.separator)) {
                    // Argument is a path ending with a directory name
                    newDir = new File(arg);
                } else {
                // Argument is a directory name (create in the current directory)
                    newDir = new File(current_dir, arg);
            }

                if (!newDir.exists() && newDir.mkdirs()) {
                    System.out.println("Created directory: " + newDir.getAbsolutePath());
                } else if (newDir.exists()) {
                    System.out.println("Directory already exists: " + newDir.getAbsolutePath());
                } else {
                System.out.println("Failed to create directory: " + newDir.getAbsolutePath());
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
        File currentDir = new File(current_dir);
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
        File currentDir = new File(current_dir);
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

    public void rm(String fileName) {
        File file = new File(current_dir, fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("File removed: " + file.getAbsolutePath());
            } else {
                System.out.println("Failed to remove the file: " + file.getAbsolutePath());
            }
        } else {
            System.out.println("File does not exist or is not a regular file: " + file.getAbsolutePath());
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
    public void cp(String[] args) {
        if(args.length<2) {
            System.out.println("Not enough arguments.");
        }else{
            String sourcePath = args[0];
            String destinationPath = args[1];
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);
            if (!source.isAbsolute()) {
                source = Paths.get(current_dir, sourcePath);
            }
        
            if (!destination.isAbsolute()) {
                destination = Paths.get(current_dir, destinationPath);
            }
            try {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Error copying the file: " + e.getMessage());
            }
        }
    }
    public void cat(String[] args) {
        if (args.length == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }
        } else if (args.length == 2) {
            try (BufferedReader br1 = new BufferedReader(new FileReader(args[0]));
                 BufferedReader br2 = new BufferedReader(new FileReader(args[1]))) {
                String line;
                while ((line = br1.readLine()) != null) {
                    System.out.println(line);
                }
                while ((line = br2.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading the files: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid number of arguments. Usage: cat <file1> or cat <file1> <file2>");
        }
    }


    public void chooseCommandAction(String input) {
        Parser parser= new Parser();
        parser.parse(input);
        String commandName = parser.getCommandName();
        String[] args = parser.getArgs();
        String argument;

        switch (commandName) {
            case "echo":
                echo(args);
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
                    ls_r();
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
                    System.out.println("No File specified.");
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
            case "cp":
                cp(args);
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

