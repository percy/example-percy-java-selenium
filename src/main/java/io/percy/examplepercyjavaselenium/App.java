package io.percy.examplepercyjavaselenium;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Entrypoint to the Percy Java example app: an HTTP server that serves the
 * TodoMVC JavaScript app.
 */
public class App {
    // Server port.
    private static final Integer PORT = 8000;
    // This file path is relative to that classpath root.
    private static final String INDEX_PATH = "index.html";
    // Recognized Mime type map (extension -> mimetype)
    private static final Map<String, String> MIME_MAP = new HashMap<String, String>();
    static {
        MIME_MAP.put("html", "text/html");
        MIME_MAP.put("js", "application/javascript");
        MIME_MAP.put("css", "text/css");
    }

    public static void main(String[] args) throws IOException {
        // Using a null executor will cause the server to run on the current thread, thus
        // blocking execution until the server exits or this process is terminated.
        startServer(null);
    }

    public static HttpServer startServer(Executor executor) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(App::handleRequest);
        System.out.println(String.format("Starting server, listening on port %d", PORT));
        System.out.println("CTRL-C to exit");
        server.setExecutor(executor);
        server.start();
        return server;
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String requestedPath = exchange.getRequestURI().getPath();
        System.out.println("Serving path: " + requestedPath);
        if (requestedPath.equals("/")) {
            serveStaticFile(exchange, INDEX_PATH);
        } else {
            if (requestedPath.startsWith("/")) {
                requestedPath = requestedPath.substring(1);
            }
            serveStaticFile(exchange, requestedPath);
        }
    }

    private static void serveStaticFile(HttpExchange exchange, String resourcePath) throws IOException {
        byte[] response;
        int responseCode;
        InputStream in = App.class.getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            response = "404 - File Not Found".getBytes();
            responseCode = 404;
        } else {
            InputStream stream = App.class.getClassLoader().getResourceAsStream(resourcePath);
            response = new byte[stream.available()];
            stream.read(response);
            responseCode = 200;
        }

        exchange.getResponseHeaders().add("Content-Type", getMimeType(resourcePath));
        exchange.sendResponseHeaders(responseCode, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private static String getMimeType(String resourcePath) {
        int lastDotIndex = resourcePath.lastIndexOf('.');
        String extension = lastDotIndex > 0 ? resourcePath.substring(lastDotIndex + 1) : "";
        return MIME_MAP.getOrDefault(extension, "text/plain");
    }
}
