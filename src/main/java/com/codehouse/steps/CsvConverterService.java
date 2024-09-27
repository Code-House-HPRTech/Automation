/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.steps;

import com.codehouse.contants.Constant;
import com.codehouse.dto.WordpressPost;
import com.codehouse.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class CsvConverterService {

    public static void convertBatchToCsv(String postJsonFilePath, String csvFolderPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<WordpressPost> postDTOList = new ArrayList<>();

        List<JsonNode> myObjects = objectMapper.readValue(
                new File(postJsonFilePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));

        for (JsonNode jsonNode : myObjects) {
            postDTOList.add(WordpressPost.builder()
                    .id(jsonNode.get("id").asInt())
                    .title(jsonNode.get("title").get("rendered").asText())
                    .content(jsonNode.get("content").get("rendered").asText())
                    .excerpt(jsonNode.get("excerpt").get("rendered").asText())
                    .featureImage(jsonNode.get("featured_media").asInt())
                    .postDate(jsonNode.get("date").asText())
                    .slug(jsonNode.get("slug").asText())
                    .status(jsonNode.get("status").asText())
                    .category(jsonNode.get("categories").toString().replace("[", "").replace("]", "").replace("\"", ""))
                    .tag(jsonNode.get("tags").toString().replace("[", "").replace("]", "").replace("\"", ""))
                    .build());
        }

        // Define the size of each chunk
        int chunkSize = 100;

        // Calculate the number of CSV files needed
        int numFiles = (int) Math.ceil((double) postDTOList.size() / chunkSize);

        Path folder = Path.of(csvFolderPath);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        // Iterate through the chunks and write each chunk to a separate CSV file
        for (int i = 0; i < numFiles; i++) {
            int startIndex = i * chunkSize;
            int endIndex = Math.min(startIndex + chunkSize, postDTOList.size());
            List<WordpressPost> chunk = postDTOList.subList(startIndex, endIndex);

            try {
                // Generate a random filename or use some logic to create a unique filename
                String fileName = csvFolderPath + "\\postsCsv_" + (i + 1) + ".csv";
                // Create CSVWriter with FileWriter
                try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))) {
                    JsonNode rootNode = objectMapper.convertValue(chunk, JsonNode.class);
                    csvWriter.writeNext(getHeader(rootNode));
                    writeData(csvWriter, rootNode);
                }
                System.out.println("CSV file " + fileName + " written successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file writing errors
            }
        }
        System.out.println("------->> Conversion successful");
    }

    private static String[] getHeader(JsonNode rootNode) {
        // Assuming all objects in the JSON have the same structure
        JsonNode firstObject = rootNode.isArray() ? rootNode.get(0) : rootNode;
        Iterator<String> fieldNames = firstObject.fieldNames();

        List<String> headerList = StreamSupport.stream(Spliterators.spliteratorUnknownSize(fieldNames, 0), false).toList();

        return headerList.toArray(new String[0]);
    }

    private static void writeData(CSVWriter csvWriter, JsonNode rootNode) {
        if (rootNode.isArray()) {
            for (JsonNode jsonNode : rootNode) {
                String[] data = getData(jsonNode);
                csvWriter.writeNext(data);
            }
        } else {
            String[] data = getData(rootNode);
            csvWriter.writeNext(data);
        }
    }

    private static String[] getData(JsonNode jsonNode) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(jsonNode.elements(), 0), false)
                .map(JsonNode::asText)
                .toArray(String[]::new);
    }
}
