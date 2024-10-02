/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.service;

import com.codehouse.contants.Constant;
import com.codehouse.util.WordPressUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FeatureImageService {
    public static void updateFeatureImageId(String mediaJsonFilePath, String myMediaJsonFilePath, String postJsonFilePath)  {
        Map<Integer, Integer> mediaMap = WordPressUtils.mapMediaId(
                mediaJsonFilePath,
                myMediaJsonFilePath
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArray = null;
        try {
            jsonArray = objectMapper.readTree(new File(postJsonFilePath));
        } catch (IOException e) {
            System.err.println("Error occurred while reading post json file");
            e.printStackTrace();
            return;
        }

        System.out.println("----> Media Updation Started.....");

        if (jsonArray.isArray()) {
            for (JsonNode jsonNode : jsonArray) {
                JsonNode mediaNode = jsonNode.get("featured_media");
                if (mediaNode != null && mediaNode.isInt()) {
                    int oldValue = mediaNode.asInt();
                    if (mediaMap.containsKey(oldValue)) {
                        JsonNode newValue = objectMapper.valueToTree(mediaMap.getOrDefault(oldValue, 0));
                        ((ObjectNode) jsonNode).replace("featured_media", newValue);
                    }
                }
            }

            try {
                objectMapper.writeValue(new File(postJsonFilePath), jsonArray);
            } catch (IOException e) {
                System.err.println("Error occurred while writing post json file");
                e.printStackTrace();
                return;
            }

            System.out.println("------> Media updated successfully in the file.");
        }
    }
}
