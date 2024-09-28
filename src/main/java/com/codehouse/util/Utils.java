/**
 * @Author - Prakash Jangir
 * @Date - 18/09/2024
 */

package com.codehouse.util;

import com.codehouse.contants.Constant;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.cert.X509Certificate;

public class Utils {

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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            int i = 1;
            while (true) {
                System.out.println("------>>> Page: " + i);

                String response = getResponseFromUrl(
                        siteUrl
                                + Constant.WORDPRESS_URL_POSTFIX
                                + type
                                + "?per_page=100&page=" + i
                                + filterColumn
                );

                if (response != null
                        && (!(response.equalsIgnoreCase("rest_post_invalid_page_number")
                        || response.equalsIgnoreCase("[]")))) {
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
     * @throws IOException
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
     * @throws IOException
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
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);

            System.out.println("File deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download image from internet and save to local dir
     *
     * @param imageUrl
     * @throws IOException
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
}
