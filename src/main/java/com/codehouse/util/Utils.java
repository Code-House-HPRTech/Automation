/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.util;

import com.codehouse.contants.Constant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.stream.Stream;

public class Utils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Download Json From Internet And Save to Specified File
     *
     * @param type
     * @param path
     * @param filterColumn
     * @param siteUrl
     */
    public static void downloadData(String type, String path, String filterColumn, String siteUrl) {
        // Delete Old File From Directory
        Utils.deleteFile(path);

        System.out.println("-------Downloading Started: " + type.toUpperCase() + "---------");

        File file = new File(path);

        // Create parent directories if they do not exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                System.out.println("Directories created: " + parentDir);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            int i = 1;
            while (true) {
                System.out.println(type + " -> " + i);

                String response = getResponseFromUrl(
                        siteUrl
                                + Constant.WORDPRESS_URL_POSTFIX
                                + type
                                + "?per_page=100&page=" + i
                                + filterColumn
                );

                if (response != null && isJsonArrayAndLengthGreaterThanOne(response)) {
                    if (i == 1) {
                        response = response.substring(0, response.length() - 1);
                    }
                    if (i > 1) {
                        response = response.replaceFirst("\\[", ",");
                        // Remove the last "]"
                        if (response.endsWith("]")) {
                            response = response.substring(0, response.length() - 1);
                        }
                    }
                    writer.write(response);
                } else {
                    writer.write("]");
                    break;
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-------Successfully Downloaded: " + type.toUpperCase() + "---------");
    }


    /**
     * Fetch the data from the internet
     *
     * @param urlString
     * @return
     * @
     */
    private static String getResponseFromUrl(String urlString) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString);

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Read the response content
        if (responseCode == 400) return null;

        StringBuilder responseStringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseStringBuilder.append(line);
            }
        } finally {
            connection.disconnect();
        }
        return responseStringBuilder.toString();
    }

    /**
     * Get HTTP url connection
     *
     * @param urlString
     * @return
     * @
     */
    private static HttpURLConnection getHttpURLConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method to GET
        connection.setRequestMethod("GET");

        // Set the User-Agent header
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        connection.setRequestProperty("Referer", "https://www.google.com");

        return connection;
    }

    /**
     * Delete Any Exising File From Directory
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        Path path = Paths.get(filePath);

        // Check if the file exists
        if (!Files.exists(path)) {
            System.out.println("File does not exist: " + filePath);
            return; // Exit if the file doesn't exist
        }

        try {
            Files.delete(path);
            System.out.println("File deleted successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Unable to delete file: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Download image from internet and save to local dir
     *
     * @param imageUrl
     * @
     */
    public static void downloadImage(String imageUrl, String mediaFolderPath) throws IOException {
        URL url = new URL(imageUrl);

        // Create the folder if it doesn't exist
        Path folder = Path.of(mediaFolderPath);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        Path destinationPath = folder.resolve(fileName);

        try (InputStream in = url.openStream()) {
            // Download and save the image
            Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Disable SSL validation while accessing the site
     */
    public static void disableSSLValidation() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadSpecificMediaData(String type, String path, String filterColumn, String siteUrl, boolean isFirstWrite) {
        System.out.println("-------Downloading Started: " + type.toUpperCase() + "---------");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            String response = getResponseFromUrl(
                    siteUrl
                            + Constant.WORDPRESS_URL_POSTFIX
                            + type
                            + "?per_page=100"
                            + filterColumn
            );

            if (response != null && (!(response.equalsIgnoreCase("rest_post_invalid_page_number") || response.equalsIgnoreCase("[]")))) {
                // If this is the first write, replace the first comma with a [
                if (isFirstWrite) {
                    response = response.replaceFirst("\\[", "[");
                } else {
                    response = response.replaceFirst("\\[", ",");
                }

                if (response.endsWith("]")) {
                    response = response.substring(0, response.length() - 1);
                }
                writer.write(response);
            } else {
                writer.write("]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Decode to Hindi
     *
     * @param input
     * @return
     */
    public static String decodeUnicode(String input) {
        StringBuilder sb = new StringBuilder();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == '\\' && i < input.length() - 1 && input.charAt(i + 1) == 'u') {
                // Start of a Unicode escape sequence
                if (!currentToken.isEmpty()) {
                    sb.append(currentToken);
                    currentToken.setLength(0);
                }

                // Extract the Unicode escape sequence
                String unicodeSequence = input.substring(i + 2, i + 6);

                // Handle Devanagari script
                int codepoint = Integer.parseInt(unicodeSequence, 16);
                sb.append((char) codepoint);

                // Skip the processed characters
                i += 5;
            } else {
                // Regular character or number
                currentToken.append(currentChar);
            }
        }

        if (!currentToken.isEmpty()) {
            sb.append(currentToken);
        }

        return sb.toString();
    }

    public static void deleteFolder(String folderPath) {
        Path pathToDelete = Path.of(folderPath);

        // Check if the folder exists
        if (!Files.exists(pathToDelete)) {
            System.out.println("Folder does not exist: " + folderPath);
            return; // Exit if the folder doesn't exist
        }

        // Using try-with-resources for Files.walk
        try (Stream<Path> paths = Files.walk(pathToDelete)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Unable to delete: " + path);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            System.err.println("Unable to walk the folder: " + folderPath);
            e.printStackTrace();
        }
    }

    public static void createFolder(String folderName) throws IOException {
        Path folder = Path.of(folderName);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
    }

    public static void deleteAndCreateFolder(String folderName) throws IOException {
        deleteFolder(String.format(Constant.WP_DATA_BASE_PATH, folderName));
        createFolder(String.format(Constant.WP_DATA_BASE_PATH, folderName));
    }

    public static boolean isJsonArrayAndLengthGreaterThanOne(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.isArray() && !jsonNode.isEmpty(); // Check if it's an array and size > 1
        } catch (Exception e) {
            System.out.println("Response is invalid");
            return false;
        }
    }

    /**
     *
     */
    public static void consolidateMedia() {
        // Change this to your target folder path
        String sourcePath = ".\\wordpress";
        String destinationPath = ".\\wordpress\\all_media";

        Utils.deleteFolder(destinationPath);

        try {
            // Create destination directory if it doesn't exist
            Path destDir = Paths.get(destinationPath);
            if (!Files.exists(destDir)) {
                Files.createDirectories(destDir);
            }

            // Walk the file tree
            Files.walk(Paths.get(sourcePath))
                    .filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().equalsIgnoreCase("media"))
                    .forEach(mediaDir -> {
                        try {
                            // Copy files from media directory to the destination directory
                            Files.list(mediaDir).forEach(file -> {
                                try {
                                    Files.copy(file, destDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                                } catch (IOException e) {
                                    System.err.println("Error copying file: " + file + " - " + e.getMessage());
                                }
                            });
                        } catch (IOException e) {
                            System.err.println("Error accessing media directory: " + mediaDir + " - " + e.getMessage());
                        }
                    });

            System.out.println("Files copied successfully to " + destinationPath);

        } catch (IOException e) {
            System.err.println("Error walking the file tree: " + e.getMessage());
        }
    }

    public static void consolidateCsv() {
        // Set the source directory and destination directory
        String sourceDir = ".\\wordpress";
        String destinationDir = ".\\wordpress\\all_csv";

        Utils.deleteFolder(destinationDir);

        try {
            Files.createDirectories(Paths.get(destinationDir)); // Create destination directory if it doesn't exist

            // Walk through the directory structure
            Files.walkFileTree(Paths.get(sourceDir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Check if the file is a CSV file
                    if (file.toString().endsWith(".csv")) {
                        // Get parent folder names
                        Path parentFolder = file.getParent();
                        String parentParentFolderName = parentFolder.getParent().getFileName().toString();
                        String actualFileName = file.getFileName().toString();

                        // Create new file name
                        String newFileName = parentParentFolderName + "_" + actualFileName;

                        // Copy the CSV file to the new location with the new name
                        Path destinationPath = Paths.get(destinationDir, newFileName);
                        Files.copy(file, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String sourcePath, String destinationPath) {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        try {
            // Create parent directories for the destination if they don't exist
            if (destination.getParent() != null) {
                Files.createDirectories(destination.getParent());
            }

            // Copy the file, replacing it if it already exists
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully from " + sourcePath + " to " + destinationPath);
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }
}

