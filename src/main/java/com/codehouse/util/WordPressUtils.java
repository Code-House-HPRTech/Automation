/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WordPressUtils {
    /**
     * Create a map with key as category id and value as category name
     *
     * @return
     * @
     */
    public static Map<Integer, String> createCategoryTagMapWithIdName(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Integer, String> map = new HashMap<>();

        List<JsonNode> myCategoryList = null;
        try {
            myCategoryList = objectMapper.readValue(new File(filePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));
        } catch (IOException e) {
            System.err.println("Error occurred while reading my category list");
            e.printStackTrace();
            return new HashMap<Integer, String>();
        }

        for (JsonNode element1 : myCategoryList) {
            String name = element1.get("name").asText();
            int id = element1.get("id").asInt();
            map.put(id, name);
        }
        return map;
    }

    /**
     * Create a map with remoteMediaId and myMediaId
     * So that I can replace remote media id with my media id
     *
     * @return
     * @
     */
    public static Map<Integer, Integer> mapMediaId(String mediaJsonFilePath, String myMediaJsonFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Integer, Integer> mediaIdMap = new HashMap<>();

        // Load and parse JSON files
        List<JsonNode> remoteMediaList = null;
        try {
            remoteMediaList = objectMapper.readValue(
                    new File(mediaJsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class)
            );
        } catch (IOException e) {
            System.err.println("Error occurred while reading remote media list");
            e.printStackTrace();
            return new HashMap<>();
        }

        List<JsonNode> myMediaList = null;
        try {
            myMediaList = objectMapper.readValue(
                    new File(myMediaJsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class)
            );
        } catch (IOException e) {
            System.err.println("Error occurred while reading my media list");
            e.printStackTrace();
            return new HashMap<>();
        }

        // Create a HashMap to store myMediaName -> myMediaId mapping
        Map<String, Integer> myMediaMap = new HashMap<>();

        for (JsonNode element2 : myMediaList) {
            int myMediaId = element2.get("id").asInt();
            String myMediaFullName = element2.get("guid").get("rendered").asText();
            String myMediaName = myMediaFullName.substring(myMediaFullName.lastIndexOf("/") + 1);
            myMediaMap.put(myMediaName.toLowerCase(), myMediaId); // Store names in lowercase for case-insensitive comparison
        }

        // Compare remote media names with myMediaMap
        for (JsonNode element1 : remoteMediaList) {
            int remoteMediaId = element1.get("id").asInt();
            String remoteMediaFullName = element1.get("guid").get("rendered").asText();
            String remoteMediaName = remoteMediaFullName.substring(remoteMediaFullName.lastIndexOf("/") + 1);

            Integer myMediaId = myMediaMap.get(remoteMediaName.toLowerCase()); // Case-insensitive lookup
            if (myMediaId != null) {
                mediaIdMap.put(remoteMediaId, myMediaId);
            } else {
                mediaIdMap.put(remoteMediaId, 0);
            }
        }

        System.out.println(mediaIdMap.size());
        return mediaIdMap;
    }

    /**
     * @param mediaJsonFilePath
     * @return
     * @
     */
    public static Map<Integer, String> getRemoteMediaMap(String mediaJsonFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Load and parse JSON files
        List<JsonNode> remoteMediaList = null;
        try {
            remoteMediaList = objectMapper.readValue(
                    new File(mediaJsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class)
            );
        } catch (IOException e) {
            System.err.println("Error occurred while reading remote media list");
            e.printStackTrace();
            return new HashMap<>();
        }

        // Create a HashMap to store myMediaName -> myMediaId mapping
        Map<Integer, String> mediaMap = new HashMap<>();

        for (JsonNode element : remoteMediaList) {
            mediaMap.put(element.get("id").asInt(), element.get("guid").get("rendered").asText());
        }
        return mediaMap;
    }

    /**
     * Create a new json file which contains only new media that we need to download
     *
     * @param mediaJsonFilePath
     * @param myMediaJsonFile
     * @param requiredMediaJsonFile
     */
    public static void collectOnlyRequiredMediaJson(String mediaJsonFilePath, String myMediaJsonFile, String requiredMediaJsonFile) {
        Utils.deleteFile(requiredMediaJsonFile);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read existing.json and store names in a Set for fast lookup
            List<Map<String, JsonNode>> existingItems
                    = objectMapper.readValue(new File(myMediaJsonFile), new TypeReference<List<Map<String, JsonNode>>>() {
            });
            Set<String> existingNames = new HashSet<>();
            for (Map<String, JsonNode> item : existingItems) {
                existingNames.add(item.get("guid").get("rendered").asText().split("uploads", 2)[1].split("/", 4)[3]);
            }

            // Read new.json and filter items that are not in existingNames
            List<Map<String, JsonNode>> newItems = objectMapper.readValue(new File(mediaJsonFilePath), new TypeReference<List<Map<String, JsonNode>>>() {
            });
            List<Map<String, JsonNode>> requiredNewItems = newItems.stream()
                    .filter(item -> !existingNames.contains(item.get("guid").get("rendered").asText().split("uploads", 2)[1].split("/", 4)[3]))
                    .toList();

            // Write the filtered items to requirednew.json
            objectMapper.writeValue(new File(requiredMediaJsonFile), requiredNewItems);

            System.out.println("Filtered items written to requirednew.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}