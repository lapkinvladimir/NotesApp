package com.example.NotesApp.extra;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    public static void createFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void writeToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("An error occurred while writing.");
        }
    }
}
