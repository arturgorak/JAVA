package uj.java.gvt;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

import static uj.java.gvt.CallErrors.*;
import static uj.java.gvt.DirectoryAndFilesOperations.*;
import static uj.java.gvt.WriteReadVersions.*;


public class Gvt {
    static ArrayList<GvtVersion> gvtVersions = new ArrayList<>();
    static Path path = Paths.get("");
    static Path mainDirectory = path.resolve(".gvt");
    static Path versionsDirectory = mainDirectory.resolve("versions");
    static Path savesDirectory = mainDirectory.resolve("saves");
    static String versionsPath = savesDirectory.resolve("versions.txt").toString();
    static String lastVersionPath = savesDirectory.resolve("lastVersion.txt").toString();
    static long lastVersion = -1;
    static long highestVersion = -1;

    public static void main(String... args){

        specifyCommandError(args);
        switch (args[0]) {
            case "init" -> init();
            case "add" -> add(args);
            case "detach" -> detach(args);
            case "checkout" -> checkout(args);
            case "commit" -> commit(args);
            case "history" -> history(args);
            case "version" -> version(args);
            default -> {
                System.out.println("Unknown command " + args[0] + ".");
                System.exit(1);
            }
        }
    }


    static void init(){
        isInitiatedError();

        lastVersion = 0;
        highestVersion = 0;

        createNewDirectory(mainDirectory);

        Path thisVersionDirectory = versionsDirectory.resolve(String.valueOf(lastVersion));

        createNewDirectory(versionsDirectory);
        createNewDirectory(thisVersionDirectory);
        createNewDirectory(savesDirectory);

        gvtVersions.add(new GvtVersion(0, "GVT initialized.", new ArrayList<>(), new ArrayList<>()));

        writeLastVersion();
        writeVersions();
        System.out.println("Current directory initialized successfully.");

    }

    static void add(String[] input){

        isNotInitiatedError();
        specifyFileError("add", input);

        readVersions();
        readLastVersion();

        FileDescription fileDescription = hatchMessage(input, "-m");

        fileNotFoundError(fileDescription.filePath());


        for (GvtVersion ver: gvtVersions) {
            if (ver.number() == lastVersion){
                if(ver.addedFiles().contains(fileDescription.filePath())){
                    System.out.println("File " + fileDescription.filePath() + " already added.");
                } else {
                    ArrayList<String> tempListAdded = new ArrayList<>(ver.addedFiles());
                    ArrayList<String> tempListCommitted = new ArrayList<>(ver.committedFiles());
                    String fileName = new File(fileDescription.filePath()).getName();

                    tempListAdded.add(fileDescription.filePath());

                    createNewVersionDirectory(fileDescription);

                    String newMessage = "Added file: " + fileDescription.filePath();

                    if(fileDescription.message() != null){
                        newMessage += "\n" + fileDescription.message();
                    }

                    GvtVersion tmp = new GvtVersion(highestVersion, newMessage, tempListAdded, tempListCommitted);
                    gvtVersions.add(tmp);

                    System.out.println("File " + fileName + " added successfully.");

                    writeVersions();
                    writeLastVersion();
                }
                return;


            }
        }

    }

    static FileDescription hatchMessage(String[] input, String flag){
        String first = null;
        String second = null;
        if (input.length > 1) {
            first = input[1];
        }
        for (int i = 1; i < input.length; i++){
            if (input[i].equals(flag)){
                second = input[i + 1];
                break;
            }
        }

        return new FileDescription(first, second);
    }

    static void history(String[] input){
        isNotInitiatedError();
        readLastVersion();
        readVersions();

        String howManyVersions = hatchMessage(input, "-last").message();
        if (howManyVersions != null){
            long lastNVersions = Long.parseLong(howManyVersions);
            long firstVersion = highestVersion - lastNVersions;
            for(GvtVersion ver: gvtVersions){
                if(ver.number() > firstVersion){
                   System.out.println(ver.number() + ": " + printFirstLine(ver.message()));
                }
            }
        } else {
            for(GvtVersion ver: gvtVersions){
                System.out.println(ver.number() + ": " + printFirstLine(ver.message()));
            }
        }
    }

    static void version(String[] input){
        isNotInitiatedError();
        readVersions();
        readLastVersion();

        long number = invalidVersionNumberError("version", input);

        for (GvtVersion ver: gvtVersions){
            if(ver.number() == number){
                System.out.println("Version: " + number);
                System.out.print(ver.message());
                return;
            }
        }

        System.out.println("Invalid version number: " + number + ".");

    }

    static String printFirstLine(String input){
        return input.split("\\n")[0];
    }

    //static void updateLists(String nameOfMethod, String newMessage, )

    static void commit(String[] input){
        specifyFileError("commit", input);
        isNotInitiatedError();

        readVersions();
        readLastVersion();

        FileDescription fileDescription = hatchMessage(input, "-m");

        fileDoesNotExistError(fileDescription.filePath());


        for (GvtVersion ver: gvtVersions) {
            if (ver.number() == lastVersion ){
                if (ver.addedFiles().contains(fileDescription.filePath()) && !ver.committedFiles().contains(fileDescription.filePath())){
                    ArrayList<String> tempListAdded = new ArrayList<>(ver.addedFiles());
                    ArrayList<String> tempListCommitted = new ArrayList<>(ver.committedFiles());

                    tempListCommitted.add(fileDescription.filePath());
                    String fileName = new File(fileDescription.filePath()).getName();
                    createNewVersionDirectory(fileDescription);

                    String newMessage = "Committed file: " + fileDescription.filePath();

                    if(fileDescription.message() != null){
                        newMessage += "\n" + fileDescription.message();
                    }

                    GvtVersion tmp = new GvtVersion(lastVersion, newMessage, tempListAdded, tempListCommitted);
                    gvtVersions.add(tmp);

                    System.out.println("File " + fileName + " committed successfully.");

                    writeLastVersion();
                    writeVersions();
                } else {
                    System.out.println("File " + fileDescription.filePath() + " is not added to gvt.");
                }
                return;
            }
        }

    }

    static void detach(String[] input){
        specifyFileError("detach", input);
        isNotInitiatedError();

        readLastVersion();
        readVersions();

        FileDescription fileDescription = hatchMessage(input, "-m");

        for(GvtVersion ver: gvtVersions){
            if(ver.number() == lastVersion){
                String fileName = new File(fileDescription.filePath()).getName();
                if(ver.addedFiles().contains(fileDescription.filePath())){


                    ArrayList<String> tmpAddedList = new ArrayList<>(ver.addedFiles());
                    tmpAddedList.remove(fileDescription.filePath());

                    ArrayList<String> tmpCommittedList = new ArrayList<>(ver.committedFiles());
                    tmpCommittedList.remove(fileDescription.filePath());

                    Path previousDirectory = versionsDirectory.resolve(String.valueOf(lastVersion));

                    highestVersion += 1;
                    lastVersion = highestVersion;

                    Path newVersionDirectory = versionsDirectory.resolve(String.valueOf(lastVersion));

                    createNewDirectory(newVersionDirectory);

                    copyAllFilesFromDirectory(previousDirectory, newVersionDirectory);

                    File myObj = new File(newVersionDirectory + "/" + fileName);
                    if(myObj.delete()){
                        System.out.println("File " + fileDescription.filePath() + " detached successfully.");
                    }

                    String newMessage = "Detached file: " + fileName;
                    if(fileDescription.message() != null){
                        newMessage += "\n" + fileDescription.message();
                    }

                    GvtVersion tmp = new GvtVersion(lastVersion, newMessage, tmpAddedList, tmpCommittedList);
                    gvtVersions.add(tmp);


                    writeVersions();
                    writeLastVersion();
                } else {
                    System.out.println("File "+ fileName +" is not added to gvt.");
                }
                return;
            }
        }
    }

    static void checkout(String[] input){
        isNotInitiatedError();
        readVersions();
        readLastVersion();

        long number = invalidVersionNumberError("checkout", input);

        for(GvtVersion ver: gvtVersions){
            if(ver.number() == number){
                Path actualVersionDirectory = versionsDirectory.resolve(String.valueOf(number));

                for(String addedFile: ver.addedFiles()){

                    String fileName = new File(addedFile).getName();
                    Path sourcePath = actualVersionDirectory.resolve(fileName);
                    Path destPath = Paths.get(addedFile);

                    addFile(sourcePath, destPath);

                }
                lastVersion = number;
                writeLastVersion();
                System.out.println("Version "+ number +" checked out successfully.");
                return;
            }
        }
        System.out.println("Invalid version number: " + input[1] + ".");
        System.exit(40);
    }
}






