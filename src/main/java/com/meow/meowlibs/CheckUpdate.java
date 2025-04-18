package com.meow.meowlibs;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * 更新检查类，用于检查 GitHub 上的最新版本并提示用户更新
 */
public class CheckUpdate {
    private final Logger logger;
    private final PluginDescriptionFile description;
    private static final String LATEST_VERSION_URL = "https://api.github.com/repos/Zhang12334/MeowLibs/releases/latest";
    private static final String DOWNLOAD_URL = "https://github.com/Zhang12334/MeowLibs/releases/latest";

    /**
     * 构造函数，初始化日志、语言管理和插件描述
     * @param logger log记录器
     * @param description 读版本号
     */
    public CheckUpdate(Logger logger, PluginDescriptionFile description) {
        this.logger = logger;
        this.description = description;
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {
        String currentVersion = description.getVersion(); // 获取当前版本号

        try {
            // 使用 GitHub API 获取最新版本信息
            String response = fetchLatestVersion();
            JSONObject json = new JSONObject(response);
            String latestVersion = json.getString("tag_name"); // 获取最新版本号
            String releaseNotes = json.getString("body"); // 获取更新内容

            // 比较版本号
            if (isVersionGreater(latestVersion, currentVersion)) {
                logger.warning("New version available: v" + latestVersion);
                logger.warning("Update content:");
                parseMarkdownAndLog(releaseNotes);
                logger.warning("Download URL for new version: " + DOWNLOAD_URL);
                logger.warning("Old versions may cause problems, please update as soon as possible!");
            } else {
                logger.info("You are currently using the latest version!");
            }
        } catch (Exception e) {
            logger.warning("Update check failed, please check your network status!");
        }
    }

    /**
     * 获取最新版本信息
     * @return GitHub API 的 JSON 响应
     * @throws Exception 如果网络请求失败
     */
    private String fetchLatestVersion() throws Exception {
        URL url = new URI(LATEST_VERSION_URL).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP 响应码: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        return response.toString();
    }

    /**
     * 解析 Markdown 格式的更新日志
     * @param body GitHub release 的更新日志
     */
    private void parseMarkdownAndLog(String body) {
        String[] lines = body.split("\n");
        String lastPrefix = "";

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String output;
            if (line.startsWith("# ")) {
                lastPrefix = "🔹";
                output = lastPrefix + " **" + line.substring(2) + "**";
            } else if (line.equals("---")) {
                lastPrefix = "---";
                output = "---";
            } else {
                lastPrefix = lastPrefix.isEmpty() ? "-" : lastPrefix;
                output = lastPrefix + " " + line;
            }
            logger.warning(output);
        }
    }

    /**
     * 比较版本号（支持 1.1.1 格式）
     * @param latestVersion 最新版本号
     * @param currentVersion 当前版本号
     * @return 如果 latestVersion > currentVersion，返回 true
     */
    private boolean isVersionGreater(String latestVersion, String currentVersion) {
        // 移除可能的 "v" 前缀
        latestVersion = latestVersion.replaceFirst("^v", "");
        currentVersion = currentVersion.replaceFirst("^v", "");

        String[] v1Parts = latestVersion.split("\\.");
        String[] v2Parts = currentVersion.split("\\.");

        for (int i = 0; i < Math.max(v1Parts.length, v2Parts.length); i++) {
            int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            if (v1Part > v2Part) return true;
            if (v1Part < v2Part) return false;
        }
        return false;
    }
}