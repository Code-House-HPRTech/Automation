/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.service;

import com.codehouse.util.WordPressUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.util.*;

public class CategoryAndTagService {

    public static void updateCategoryAndTag(String type, String categoryJsonFilePath, String postJsonFilePath) {
        Map<Integer, String> categoryMap = WordPressUtils.createCategoryTagMapWithIdName(categoryJsonFilePath);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonArray = null;
        try {
            postJsonArray = objectMapper.readTree(new File(postJsonFilePath));
        } catch (IOException e) {
            System.out.println("Error occurred while reading json file");
            e.printStackTrace();
            return;
        }

        if (postJsonArray.isArray()) {
            ArrayNode postJsonarrayNode = (ArrayNode) postJsonArray;
            for (JsonNode jsonNode : postJsonarrayNode) {
                JsonNode node = jsonNode.get(type);
                if (node != null && node.isArray()) {
                    ArrayNode categoriesArray = (ArrayNode) node;
                    for (int i = 0; i < categoriesArray.size(); i++) {
                        JsonNode element = categoriesArray.get(i);
                        if (element.isInt()) {
                            int idVal = element.asInt();
                            if (categoryMap.containsKey(idVal)) {
                                categoriesArray.set(i, categoryMap.getOrDefault(idVal, ""));
                            }
                        }
                    }
                }
            }

            try {
                objectMapper.writeValue(new File(postJsonFilePath), postJsonArray);
            } catch (IOException e) {
                System.err.println("Error occurred while writing post json file");
                e.printStackTrace();
                return;
            }

            System.out.println("-----> Updated Successfully In The Post Json File.");
        }
    }
}
