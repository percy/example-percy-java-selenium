package io.percy.examplepercyjavaselenium.advanced;

// PER-8195 Phase 1 — java-selenium advanced example.
// Each @Test exercises one row of the Advanced Feature Matrix. See
// ../matrix.yml for the canonical mapping of test name -> matrix row.

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.percy.selenium.Percy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdvancedTest {
  private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT_NUMBER", "8007"));
  private static final String TEST_URL = "http://localhost:" + PORT;
  private static HttpServer server;
  private static ExecutorService serverExecutor;
  private static WebDriver driver;
  private static Percy percy;

  @BeforeAll
  static void start() throws IOException {
    // Serve the parent example repo (../) — index.html + js/ are siblings of advanced/.
    Path root = new File("..").toPath().toAbsolutePath().normalize();
    serverExecutor = Executors.newFixedThreadPool(2);
    server = HttpServer.create(new InetSocketAddress(PORT), 0);
    server.setExecutor(serverExecutor);
    server.createContext("/", (HttpExchange ex) -> {
      String requested = ex.getRequestURI().getPath();
      if (requested.equals("/") || requested.isEmpty()) requested = "/index.html";
      Path file = root.resolve(requested.substring(1)).normalize();
      if (!file.startsWith(root) || !Files.exists(file) || Files.isDirectory(file)) {
        ex.sendResponseHeaders(404, -1);
        return;
      }
      byte[] body = Files.readAllBytes(file);
      ex.sendResponseHeaders(200, body.length);
      try (OutputStream os = ex.getResponseBody()) { os.write(body); }
    });
    server.start();

    ChromeOptions opts = new ChromeOptions();
    opts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
    driver = new ChromeDriver(opts);
    percy = new Percy(driver);
  }

  @AfterAll
  static void stop() {
    if (driver != null) driver.quit();
    if (server != null) server.stop(1);
    if (serverExecutor != null) serverExecutor.shutdownNow();
  }

  @BeforeEach
  void seed() {
    driver.get(TEST_URL);
    driver.findElement(By.className("new-todo")).sendKeys("Walk the dog", Keys.RETURN);
  }

  @Test
  void exercisesWidthsOverload() {
    percy.snapshot("AdvancedTest > exercisesWidths", Arrays.asList(375, 768, 1280, 1920));
  }

  @Test
  void exercisesMinHeightOverload() {
    percy.snapshot("AdvancedTest > exercisesMinHeight", Arrays.asList(1280), 2000);
  }

  @Test
  void exercisesEnableJavaScriptOverload() {
    percy.snapshot("AdvancedTest > exercisesEnableJavaScript", Arrays.asList(1280), 800, true);
  }

  @Test
  void exercisesPercyCssOverload() {
    percy.snapshot(
        "AdvancedTest > exercisesPercyCss",
        Arrays.asList(1280),
        800,
        true,
        ".todo-list li { background: #fffde7 !important; }");
  }

  @Test
  void exercisesScopeOverload() {
    percy.snapshot(
        "AdvancedTest > exercisesScope",
        Arrays.asList(1280),
        800,
        true,
        "",
        ".todoapp");
  }

  // Map-based options overload exercises matrix rows that don't have a typed
  // signature: responsive_snapshot_capture, readiness, labels, testCase,
  // devicePixelRatio, browsers, regions, sync.
  @Test
  void exercisesMapOptionsResponsiveAndReadiness() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("widths", Arrays.asList(375, 1280));
    opts.put("responsiveSnapshotCapture", true);
    Map<String, Object> readiness = new HashMap<>();
    readiness.put("preset", "strict");
    readiness.put("timeoutMs", 5000);
    opts.put("readiness", readiness);
    percy.snapshot("AdvancedTest > exercisesResponsiveAndReadiness", opts);
  }

  @Test
  void exercisesMapOptionsLabelsAndTestCase() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("labels", "smoke,sdk-java-selenium");
    opts.put("testCase", "todomvc-advanced-suite");
    percy.snapshot("AdvancedTest > exercisesLabelsAndTestCase", opts);
  }

  @Test
  void exercisesMapOptionsDevicePixelRatio() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("devicePixelRatio", 2);
    percy.snapshot("AdvancedTest > exercisesDevicePixelRatio", opts);
  }

  @Test
  void exercisesMapOptionsBrowsers() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("browsers", Arrays.asList("chrome", "firefox"));
    percy.snapshot("AdvancedTest > exercisesBrowsers", opts);
  }

  @Test
  void exercisesMapOptionsRegions() {
    Map<String, Object> region = new HashMap<>();
    region.put("algorithm", "ignore");
    Map<String, Object> bbox = new HashMap<>();
    bbox.put("x", 0); bbox.put("y", 0); bbox.put("width", 200); bbox.put("height", 100);
    Map<String, Object> selector = new HashMap<>();
    selector.put("boundingBox", bbox);
    region.put("elementSelector", selector);
    Map<String, Object> opts = new HashMap<>();
    opts.put("regions", Arrays.asList(region));
    percy.snapshot("AdvancedTest > exercisesRegions", opts);
  }

  @Test
  void exercisesMapOptionsSync() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("sync", false);
    percy.snapshot("AdvancedTest > exercisesSync", opts);
  }
}
