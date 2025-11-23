package com.undertale.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveManager {
    private static final String SAVE_FILE_PATH = "save_data.json";
    private Gson gson;

    public SaveManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save(GameStateData data) {
        try (Writer writer = new FileWriter(SAVE_FILE_PATH)) {
            gson.toJson(data, writer);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public GameStateData load() {
        try {
            if (!Files.exists(Paths.get(SAVE_FILE_PATH))) {
                System.out.println("No save file found. Creating new game state.");
                return new GameStateData();
            }

            Reader reader = new FileReader(SAVE_FILE_PATH);
            GameStateData data = gson.fromJson(reader, GameStateData.class);
            reader.close();

            if (data != null) {
                System.out.println("Game loaded successfully!");
                return data;
            } else {
                System.out.println("Save file was empty. Creating new game state.");
                return new GameStateData();
            }
        } catch (IOException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            e.printStackTrace();
            return new GameStateData();
        }
    }

    public boolean saveExists() {
        return Files.exists(Paths.get(SAVE_FILE_PATH));
    }

    public boolean deleteSave() {
        try {
            return Files.deleteIfExists(Paths.get(SAVE_FILE_PATH));
        } catch (IOException e) {
            System.err.println("Failed to delete save file: " + e.getMessage());
            return false;
        }
    }

    public void setSaveFilePath(String path) {
        // Allow custom save file paths if needed
    }
}
