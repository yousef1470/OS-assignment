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
    public void cd(String[] args) throws IOException {
        if (args.length == 0) {
            currentPath = System.getProperty("user.home");
        } else if (args.length == 1) {
            if (args[0].equals("..")) {
                File parentDir = new File(currentPath).getParentFile();
                if (parentDir != null) {
                    currentPath = parentDir.getCanonicalPath();
                }
            } else {
                File newDir = new File(currentPath, args[0]);
                if (newDir.exists() && newDir.isDirectory()) {
                    currentPath = newDir.getCanonicalPath();
                } else {
                    System.out.println("Directory not found: " + args[0]);
                }
            }
        } else {
            System.out.println("Invalid usage.");
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



    public static void main(String[] args) throws IOException {



    }
}
