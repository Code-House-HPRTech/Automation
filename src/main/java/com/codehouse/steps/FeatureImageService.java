/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.steps;

import com.codehouse.contants.Constant;
import com.codehouse.util.WordPressUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FeatureImageService {
    public static void updateFeatureImageId(String mediaJsonFilePath, String myMediaJsonFilePath) throws IOException {
        Map<Integer, Integer> mediaMap = WordPressUtils.mapMediaId(
                mediaJsonFilePath,
                myMediaJsonFilePath
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArray = objectMapper.readTree(new File(Constant.POSTS_JSON_FILE_PATH));

        System.out.println("----> Media Updation Started.....");

        if (jsonArray.isArray()) {
            for (JsonNode jsonNode : jsonArray) {
                JsonNode mediaNode = jsonNode.get("featured_media");
                if (mediaNode != null && mediaNode.isInt()) {
                    int oldValue = mediaNode.asInt();
                    if (mediaMap.containsKey(oldValue)) {
                        JsonNode newValue = objectMapper.valueToTree(mediaMap.get(oldValue));
                        ((ObjectNode) jsonNode).replace("featured_media", newValue);
                    }
                }
            }

            objectMapper.writeValue(new File(Constant.POSTS_JSON_FILE_PATH), jsonArray);

            System.out.println("------> Media updated successfully in the file.");
        }
    }
}