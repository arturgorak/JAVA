package uj.java.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static uj.java.gvt.Gvt.lastVersion;
import static uj.java.gvt.Gvt.mainDirectory;

public class CallErrors {
    static void isInitiatedError(){
        if (Files.exists(mainDirectory)) {
            System.out.println("Current directory is already initialized.");
            System.exit(10);
        }
    }

    static void isNotInitiatedError(){
        if (!Files.isDirectory(mainDirectory)) {
            System.out.println("Current directory is not initialized. Please use \"init\" command to initialize.");
            System.exit(-2);
        }
    }

    static void specifyFileError(String typeOfMethod, String[] input){

        if (input.length == 1){
            System.out.println("Please specify file to " + typeOfMethod + ".");
            switch (typeOfMethod) {
                case "add" -> System.exit(20);
                case "detach" -> System.exit(30);
                case "commit" -> System.exit(50);
            }
        }
    }

    static void specifyCommandError(String[] input) {
        if (input.length == 0) {
            System.out.println("Please specify command.");
            System.exit(1);
        }
    }

    static void fileNotFoundError(String input) {
        if (Files.notExists(Paths.get("").resolve(input))) {
            System.out.println("File " + input + " not found.");
            System.exit(21);
        }
    }

    static void underlyingSystemProblemError(IOException e){
        System.out.println("Underlying system problem. See ERR for details.");
        e.printStackTrace();
        System.exit(-3);
    }

    static long invalidVersionNumberError(String typeOfMethod, String[] input){
        long result = lastVersion;
        if (input.length > 1){
            try {
                result = Long.parseLong(input[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid version number: " + input[1] + ".");

                if(typeOfMethod.equals("version")){
                    System.exit(60);
                } else if (typeOfMethod.equals("checkout")){
                    System.exit(64);
                }
            }
        }
        return result;
    }

    static void fileDoesNotExistError(String filePath){
        if (Files.notExists(Paths.get("").resolve(filePath))) {
            System.out.println("File " + filePath + " does not exist.");
            System.exit(51);
        }
    }
}