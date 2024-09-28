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

    public static void updateCategoryAndTag(String type, String categoryJsonFilePath, String postJsonFilePath) throws IOException {
        Map<Integer, String> categoryMap = WordPressUtils.createCategoryTagMapWithIdName(categoryJsonFilePath);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonArray = objectMapper.readTree(new File(postJsonFilePath));

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

            objectMapper.writeValue(new File(postJsonFilePath), postJsonArray);

            System.out.println("-----> Updated Successfully In The Post Json File.");
        }
    }
}
