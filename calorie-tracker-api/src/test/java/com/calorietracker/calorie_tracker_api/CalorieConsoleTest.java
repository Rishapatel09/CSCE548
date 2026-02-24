package com.calorietracker.calorie_tracker_api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Console test client for Calorie Tracker API.
 * Fixes HTTP 400 crashing by reading error stream and printing server response.
 */
public class CalorieConsoleTest {

    // Change if your server uses /api prefix, etc.
    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) throws Exception {
        System.out.println("=== STARTING TEST ===");

        // 1) Try GET /users (if implemented)
        System.out.println("\n--- GET /users ---");
        doPrint(get("/users"));

        // 2) Try POST /users (most likely where you're failing)
        // Adjust JSON keys to match your backend (name/email are common).
        String userJson = """
                {
                  "name": "Console User",
                  "email": "console_user_%d@example.com"
                }
                """.formatted(System.currentTimeMillis());

        System.out.println("\n--- POST /users ---");
        doPrint(post("/users", userJson));

        System.out.println("\n=== DONE ===");
    }

    /* ----------------------------
     * HTTP helpers
     * ---------------------------- */

    private static HttpResult get(String path) throws IOException {
        return request("GET", path, null);
    }

    private static HttpResult post(String path, String jsonBody) throws IOException {
        return request("POST", path, jsonBody);
    }

    private static HttpResult put(String path, String jsonBody) throws IOException {
        return request("PUT", path, jsonBody);
    }

    private static HttpResult delete(String path) throws IOException {
        return request("DELETE", path, null);
    }

    private static HttpResult request(String method, String path, String jsonBody) throws IOException {
        String urlStr = BASE_URL + path;
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod(method);

        // Always ask for JSON back
        conn.setRequestProperty("Accept", "application/json");

        // Send JSON body when present
        if (jsonBody != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            byte[] payload = jsonBody.getBytes(StandardCharsets.UTF_8);
            conn.setFixedLengthStreamingMode(payload.length);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload);
                os.flush();
            }
        }

        int code = conn.getResponseCode();
        String body = readResponseBody(conn, code);

        return new HttpResult(method, urlStr, code, body);
    }

    /**
     * IMPORTANT FIX:
     * - If response is 2xx -> conn.getInputStream()
     * - If response is 4xx/5xx -> conn.getErrorStream()
     */
    private static String readResponseBody(HttpURLConnection conn, int code) throws IOException {
        InputStream stream = (code >= 200 && code < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        if (stream == null) return "(no response body)";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            return sb.toString().trim();
        }
    }

    private static void doPrint(HttpResult r) {
        System.out.println(r.method + " " + r.url);
        System.out.println("HTTP " + r.statusCode);
        if (r.body == null || r.body.isBlank()) {
            System.out.println("(empty body)");
        } else {
            System.out.println(r.body);
        }

        // Very helpful hint for your situation:
        if (r.statusCode == 400) {
            System.out.println("\n[Hint] 400 usually means your JSON fields don't match what the API expects.");
            System.out.println("       Look at the body above for validation/error details.");
        }
    }

    /* ----------------------------
     * Simple result struct
     * ---------------------------- */
    private static class HttpResult {
        final String method;
        final String url;
        final int statusCode;
        final String body;

        HttpResult(String method, String url, int statusCode, String body) {
            this.method = method;
            this.url = url;
            this.statusCode = statusCode;
            this.body = body;
        }
    }
}