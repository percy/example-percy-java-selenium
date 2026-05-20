# Advanced Percy + Selenium-Java example — STUB

**Status:** Phase 1 stub. `matrix.yml` is populated based on `io.percy:percy-java-selenium` research. Test code in `src/test/java/io/percy/examplepercyjavaselenium/AdvancedTest.java` is **not yet written**.

See the basic example at the repo root. See [`matrix.yml`](./matrix.yml) for the planned matrix-row coverage.

## What this example will cover

Each `@Test` will exercise one row of the matrix using the SDK's typed overloads and the `Map<String, Object> options` overload. Includes `createRegion` static helper for regions.

The Java SDK exposes both typed `snapshot(name, widths, minHeight, enableJavaScript, ...)` overloads AND `snapshot(name, Map<String, Object> options)`. The advanced example exercises both shapes to demonstrate the API surface.

## Run locally (once tests are written)

```bash
cd advanced
# CLI managed separately via npm
npm install -g @percy/cli
export PERCY_TOKEN="<your project token>"      # do NOT commit
npx percy exec -- mvn test
```

## Coverage matrix

Source of truth: [`matrix.yml`](./matrix.yml).

> Phase 1 stub: most rows are currently `Planned`. Basic example (`AppTest.java`) already exercises `widths` and `minHeight`.
