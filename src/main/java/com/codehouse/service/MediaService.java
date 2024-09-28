/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.service;

import com.codehouse.contants.Constant;
import com.codehouse.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaService {
    public static void downloadMedia(String mediaJsonFilePath) throws IOException {
        Utils.disableSSLValidation();

        // Read JSON array from file and convert it to a List of objects
        ObjectMapper objectMapper = new ObjectMapper();
        List<JsonNode> mediaList = objectMapper.readValue(new File(mediaJsonFilePath), objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));

        System.out.println("------- Media Downloading Started ------");

        int i = 0;
        for (JsonNode obj : mediaList) {
            try {
                String imageUrl = obj.get("guid").get("rendered").asText();
                Utils.downloadImage(imageUrl);

                System.out.println(++i + ": Image downloaded successfully: " + imageUrl);
            } catch (IOException e) {
                System.err.println(++i + ": Error downloading image: " + e.getMessage());
            }
        }
        System.out.println("------- All Media Downloaded Successfully ------");
    }

    public static void downloadRequiredMediaByPost(String postJsonFilePath, String mediaJsonFilePath, String siteUrl) throws IOException {
        Utils.disableSSLValidation();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonArray = objectMapper.readTree(new File(postJsonFilePath));

        if (postJsonArray.isArray()) {
            ArrayNode postJsonarrayNode = (ArrayNode) postJsonArray;

            List<String> featureIdStrings = new ArrayList<>();
            StringBuilder idsBuilder = new StringBuilder();
            int count = 0;

            for (JsonNode jsonNode : postJsonarrayNode) {
                if (jsonNode.has("featured_media")) {
                    String featureId = jsonNode.get("featured_media").asText();

                    if (count < 10) {
                        if (!idsBuilder.isEmpty()) {
                            idsBuilder.append(",");
                        }
                        idsBuilder.append(featureId);
                        count++;
                    }

                    if (count == 10) {
                        featureIdStrings.add(idsBuilder.toString());
                        idsBuilder = new StringBuilder(); // Reset the builder for next group
                        count = 0; // Reset count for next group
                    }
                }
            }

            // Add any remaining IDs that didn't make up a full group of 10
            if (count > 0) {
                featureIdStrings.add(idsBuilder.toString());
            }

            boolean isFirstWrite = true;
            for (String idGrp : featureIdStrings) {
                Utils.downloadSpecificMediaData("media", mediaJsonFilePath,
                        "&_fields=id,guid&include=" + idGrp, siteUrl, isFirstWrite);
                isFirstWrite = false;
            }
            // Correct Json File Formatting
            // After the loop, add the closing bracket if necessary
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(mediaJsonFilePath, true))) {
                writer.write("]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}