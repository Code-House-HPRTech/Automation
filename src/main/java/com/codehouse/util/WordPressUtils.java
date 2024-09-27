/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordPressUtils {
    /**
     * Create a map with key as category id and value as category name
     *
     * @return
     * @throws IOException
     */
    public static Map<Integer, String> createCategoryTagMapWithIdName(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Integer, String> map = new HashMap<>();

        List<JsonNode> myCategoryList = objectMapper.readValue(new File(filePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));

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
     * @throws IOException
     */
    public static Map<Integer, Integer> mapMediaId(String mediaJsonFilePath, String myMediaJsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Integer, Integer> mediaIdMap = new HashMap<>();

        // Load and parse JSON files
        List<JsonNode> remoteMediaList = objectMapper.readValue(
                new File(mediaJsonFilePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class)
        );

        List<JsonNode> myMediaList = objectMapper.readValue(
                new File(myMediaJsonFilePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class)
        );

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
            }
        }

        System.out.println(mediaIdMap.size());
        return mediaIdMap;
    }
}