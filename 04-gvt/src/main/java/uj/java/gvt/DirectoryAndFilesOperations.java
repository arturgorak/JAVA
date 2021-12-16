package uj.java.gvt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static uj.java.gvt.CallErrors.underlyingSystemProblemError;
import static uj.java.gvt.Gvt.*;

public class DirectoryAndFilesOperations {
    public static void copyAllFilesFromDirectory(Path sourceFile, Path destFile){
        File directoryPath = new File(sourceFile.toString());
        String[] contents = directoryPath.list();

        if(contents != null && contents.length > 0){

            for (String content : contents) {

                Path fileToAdd = sourceFile.resolve(content);
                Path newFilePath = destFile.resolve(content);

                try {
                    Files.copy(fileToAdd, newFilePath);
                } catch (IOException e) {
                    underlyingSystemProblemError(e);
                }
            }
        }
    }
    public static void createNewDirectory(Path pathToDirectory){
        try {
            Files.createDirectory(pathToDirectory);
        } catch (IOException e) {
            underlyingSystemProblemError(e);
        }
    }

    public static void addFile(Path sourcePath, Path destPath){
        try {
            Files.deleteIfExists(destPath);
        } catch (IOException e) {
            underlyingSystemProblemError(e);
        }

        copyFile(sourcePath, destPath);
    }

    public static void copyFile(Path source, Path dest){
        try {
            Files.copy(source, dest);
        } catch (IOException e) {
            underlyingSystemProblemError(e);
        }
    }

    public static void createNewVersionDirectory(FileDescription fileDescription){
        Path previousDirectory = versionsDirectory.resolve(String.valueOf(lastVersion));

        highestVersion += 1;
        lastVersion = highestVersion;

        Path newVersionDirectory = versionsDirectory.resolve(String.valueOf(lastVersion));

        createNewDirectory(newVersionDirectory);
        copyAllFilesFromDirectory(previousDirectory, newVersionDirectory);

        Path pathToCommittedFile = newVersionDirectory.resolve(new File(fileDescription.filePath()).getName());
        Path pathToReplacement = Paths.get(fileDescription.filePath());

        addFile(pathToReplacement, pathToCommittedFile);
    }
}
