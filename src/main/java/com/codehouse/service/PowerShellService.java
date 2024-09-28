package com.codehouse.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PowerShellService {

    public static void replaceStringWithMyStringInCsv(String csvFolderPath, String searchString, String replaceString) {
        // Define the PowerShell command to execute
        String command
                = String.format("""
                Get-ChildItem *.csv | ForEach-Object {
                    (Get-Content $_.FullName) -replace '%s', '%s' | Set-Content $_.FullName
                }""", searchString, replaceString);

        try {
            // Create a ProcessBuilder instance and set the working directory and command
            ProcessBuilder builder = new ProcessBuilder("powershell.exe", "-Command", command);
            builder.directory(new java.io.File(csvFolderPath));

            System.out.println("---> Replacing: " + searchString + " with: " + replaceString);

            // Start the process
            Process process = builder.start();

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete and check the exit value
            int exitCode = process.waitFor();
            System.out.println("Exited with code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}