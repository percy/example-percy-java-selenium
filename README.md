# example-percy-java-selenium
Example app used by the [Percy Java Selenium tutorial](https://docs.percy.io/docs/java-selenium-testing-tutorial) demonstrating Percy's Java Selenium integration.

This example app is an HTTP server that serves a fork of the [TodoMVC](https://github.com/tastejs/todomvc)
[Vanilla-ES6](https://github.com/tastejs/todomvc/tree/master/examples/vanilla-es6)
(forked at commit
[c78ae12a1834a11da6236c64a0c0fb06b20b7c51](https://github.com/tastejs/todomvc/tree/c78ae12a1834a11da6236c64a0c0fb06b20b7c51)).

It requires Java 8 and Maven >3.6.

The Selenium tests use ChromeDriver, which you need to install separately for your system.

On Mac OS, you can use Homebrew:
```bash
$ brew tap homebrew/cask && brew cask install chromedriver
```

On Windows, you can use Chocolatey:

```bash
$ choco install chromedriver
```

For other systems (or installation alternatives), see:
https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver

## Building and running the app

To compile and build a jar containing our app:
```bash
$ mvn package
```

To run the server:
```bash
$ java -cp target/example-percy-java-selenium-1.0-SNAPSHOT.jar io.percy.examplepercyjavaselenium.App
```

Then visit http://localhost:8000 to see the app in action.

To run the tests:
```bash
$ mvn test
```


## Running the tests

To run Percy snapshots, first set the `PERCY_TOKEN` environment variable, and then run:
```bash
$ npm install
$ npm run test
```
