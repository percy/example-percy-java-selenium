package io.percy.examplepercyjavaselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.percy.selenium.Percy;

/**
 * Unit test for example App.
 */
public class AppTest {
    private static final String TEST_URL = "http://localhost:8000";
    private static ExecutorService serverExecutor;
    private static HttpServer server;
    private static WebDriver driver;
    private static Percy percy;

    @BeforeEach
    public void startAppAndOpenBrowser() throws IOException {
        // Disable browser logs from being logged to stdout
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
        // Create a threadpool with 1 thread and run our server on it.
        serverExecutor = Executors.newFixedThreadPool(1);
        server = App.startServer(serverExecutor);
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        percy = new Percy(driver);
    }

    @AfterEach
    public void closeBrowser() {
        // Close our test browser.
        driver.quit();
        // Shutdown our server and make sure the threadpool also terminates.
        server.stop(1);
        serverExecutor.shutdownNow();
    }

    @Test
    public void loadsHomePage() {
        driver.get(TEST_URL);
        WebElement element = driver.findElement(By.className("todoapp"));
        assertNotNull(element);

        // Take a Percy snapshot.
        percy.snapshot("Home Page");
    }

    @Test
    public void acceptsANewTodo() {
        driver.get(TEST_URL);

        // We start with zero todos.
        List<WebElement> todoEls = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(0, todoEls.size());

        // Add a todo in the browser.
        WebElement newTodoEl = driver.findElement(By.className("new-todo"));
        newTodoEl.sendKeys("A new fancy todo!");
        newTodoEl.sendKeys(Keys.RETURN);

        // Now we should have 1 todo.
        todoEls = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(1, todoEls.size());

        // Take a Percy snapshot specifying browser widths.
        percy.snapshot("One todo", Arrays.asList(768, 992, 1200));
    }

    @Test
    public void letsYouCheckOffATodo() {
        driver.get(TEST_URL);

        WebElement newTodoEl = driver.findElement(By.className("new-todo"));
        newTodoEl.sendKeys("A new todo to check off");
        newTodoEl.sendKeys(Keys.RETURN);

        WebElement todoCountEl = driver.findElement(By.className("todo-count"));
        assertEquals("1 item left", todoCountEl.getText());

        driver.findElement(By.cssSelector("input.toggle")).click();

        todoCountEl = driver.findElement(By.className("todo-count"));
        assertEquals("0 items left", todoCountEl.getText());

        // Take a Percy snapshot specifying a minimum height.
        percy.snapshot("Checked off todo", null, 2000);
    }
}
