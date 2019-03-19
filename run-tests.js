const fs = require("fs");
const { spawn } = require("child_process");
const platform = require("os").platform();

// We need to change the path based on the platform they're using
const PERCY_AGENT_BINARY_PATH = /^win/.test(platform)
      ? `${process.cwd()}\\node_modules\\.bin\\percy.cmd`
      : `${process.cwd()}/node_modules/.bin/percy`


/**
 * Run tests by running the NPM script in the `package.json`
 *
 */
function runTests() {
  const tests = spawn("npm", ["run", "percy"], {
    stdio: "inherit",
    windowsVerbatimArguments: true,
  })

  tests.on("close", () => {
    console.log('Tests completed!');
  });
}

// If the dependencies aren't there, then lets install them for
if(!fs.existsSync(PERCY_AGENT_BINARY_PATH)) {
  const install = spawn("npm", ["install"], {
    stdio: "inherit",
    windowsVerbatimArguments: true,
  });

  install.on("close", () => {
    console.log(`Dependencies installed!`);
    runTests();
  });
} else {
  runTests();
}
