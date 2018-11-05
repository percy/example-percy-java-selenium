package io.percy.examplepercyjavaselenium;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(App::handleRequest);
        System.out.println(String.format("Starting server, listening on port %d", PORT));
        server.start();
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
            response = App.class.getClassLoader().getResourceAsStream(resourcePath).readAllBytes();
            responseCode = 202;
        }

        exchange.sendResponseHeaders(responseCode, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
