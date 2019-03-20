const fs = require("fs");
const { spawn } = require("child_process");
const platform = require("os").platform();

// We need to change the path / command based on the platform they're using
const NPM_CMD = /^win/.test(platform) ? `npm.cmd` : `npm`
const MVN_CMD = /^win/.test(platform) ? `mvn.cmd` : `mvn`
const PERCY_CMD = /^win/.test(platform)
      ? `${process.cwd()}\\node_modules\\.bin\\percy.cmd`
      : `${process.cwd()}/node_modules/.bin/percy`

/**
 * Run tests by calling the percy executable and passing
 * the right mvn executable
 *
 */
function runTests() {
  const tests = spawn(PERCY_CMD, ["exec", `-- ${MVN_CMD} test`], {
    stdio: "inherit",
    windowsVerbatimArguments: true,
  })

  tests.on("close", () => {
    console.log("Tests completed!");
  });

  // Otherwise errors are given a 0 exit code
  tests.on("exit", code => {
    code !== 0 ? process.exit(code) : "";
  });
}

// If the dependencies aren't there, then lets install them for
if (!fs.existsSync(PERCY_CMD)) {
  const install = spawn(NPM_CMD, ["install"], {
    stdio: "inherit",
    windowsVerbatimArguments: true
  });

  install.on("close", () => {
    console.log(`Dependencies installed!`);
    runTests();
  });
} else {
  runTests();
}
