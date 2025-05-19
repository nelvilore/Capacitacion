package com.bdd.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Locale;
import java.util.zip.ZipInputStream;

public class UpdateChromeDriver {

    public static void downloadChromeDriver(String targetDir) {
        System.out.println("Starting ChromeDriver download...");
        System.out.println("Target directory: " + targetDir);
        try {
            String os = detectOS();
            String chromeVersion = getChromeVersion();
            String driverVersion = getChromeDriverDownloadUrl(chromeVersion,os);

            String downloadUrl = buildDownloadUrl(driverVersion, os);
            System.out.println("Downloading from: " + downloadUrl);

            Path targetPath = Paths.get(targetDir, "chromedriver.zip");
            downloadFile(downloadUrl, targetPath);
            unzip(targetPath.toString(), targetDir);
            //Files.delete(targetPath);

            System.out.println("Downloaded and extracted to: " + targetDir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String detectOS() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String arch = System.getProperty("os.arch");

        if (osName.contains("win")) {
            return "win32";
        } else if (osName.contains("mac")) {
            return arch.contains("aarch64") || arch.contains("arm") ? "mac_arm64" : "mac-x64";
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return "linux64";
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + osName);
        }
    }

    private static String getChromeVersion() throws IOException {
        String[] command;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command = new String[]{"reg", "query", "HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon", "/v", "version"};
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            command = new String[]{"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome", "--version"};
        } else {
            command = new String[]{"google-chrome", "--version"};
        }

        Process process = new ProcessBuilder(command).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String version = null;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Chrome")) {
                version = line.replaceAll("[^\\d\\.]", "");
                break;
            } else if (line.contains("version")) {
                version = line.split("    ")[line.split("    ").length - 1].trim();
                break;
            }
        }
        if (version == null) throw new RuntimeException("Could not determine Chrome version");
        System.out.println("Detected Chrome version: " + version);
        return version;
    }

    private static String getChromeDriverDownloadUrl(String version, String os) throws Exception {
        os = os.replaceAll("_", "-");
        String apiUrl = "https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions-with-downloads.json";

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject json = new JSONObject(response.toString());
        JSONObject stableChannel = json.getJSONObject("channels").getJSONObject("Stable");
        JSONArray downloads = stableChannel.getJSONObject("downloads").getJSONArray("chromedriver");
        String driverVersion = "";
        for (int i = 0; i < downloads.length(); i++) {
            JSONObject entry = downloads.getJSONObject(i);
            System.out.println("Entry: " + entry.getString("platform"));
            System.out.println(os);
            if (entry.getString("platform").equals(os)) {
                return entry.getString("url");
            }
        }
        throw new RuntimeException("Could not find download URL for platform: " + version);
    }

    private static String buildDownloadUrl(String driverVersion, String os) {
        String osSuffix;
        switch (os) {
            case "win32":
                osSuffix = "win32";
                break;
            case "mac-x64":
                osSuffix = "mac-x64";
                break;
            case "mac_arm64":
                osSuffix = "mac-arm64";
                break;
            case "linux64":
                osSuffix = "linux64";
                break;
            default:
                throw new IllegalArgumentException("Unknown OS type");
        }
        return String.format("%s",
                driverVersion);
    }

    private static void downloadFile(String urlStr, Path target) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(urlStr).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(target.toFile())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    private static void unzip(String zipFilePath, String destDir) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            var entry = zis.getNextEntry();
            while (entry != null) {
                File newFile = new File(destDir, "chromedriver");
                new File(newFile.getParent()).mkdirs();
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                entry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

}