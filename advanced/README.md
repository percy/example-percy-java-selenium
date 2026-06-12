# Advanced Percy + Selenium-Java example

This directory exercises the full applicable Percy SDK feature surface for `io.percy:percy-java-selenium`. See the basic example at the repo root for the minimum integration.

## What this example covers

A JUnit 5 suite (`src/test/java/io/percy/examplepercyjavaselenium/advanced/AdvancedTest.java`) where each `@Test` exercises one row of the [Percy SDK Advanced Feature Matrix](../../../docs/advanced-example-feature-matrix.md) using the SDK's typed overloads (`snapshot(name, widths, minHeight, enableJavaScript, percyCSS, scope)`) and the `Map<String, Object> options` overload for everything else (responsive, readiness, labels, testCase, devicePixelRatio, browsers, regions, sync).

Global SDK config — readiness preset, default widths, percyCSS, discovery — lives in `.percy.yml`.

## Run locally

```bash
cd advanced
make install                       # installs @percy/cli into node_modules
export PERCY_TOKEN="<your token>"  # do NOT commit this
make test
```

To run without a real token (CI assertion mode):

```bash
make test-advanced-ci   # uses --testing + PERCY_TOKEN=fake_token + captures /test/requests
```

The CI variant asserts every matrix row appears in the captured POST bodies at the local `/test/requests` endpoint. No real Percy build is created.

## Coverage matrix

States: `Covered` / `N/A — <reason>` / `Planned` / `Deprecated`. Source of truth is [`matrix.yml`](./matrix.yml).

| Feature | State | Test |
|---|---|---|
| widths (typed overload) | Covered | `exercisesWidthsOverload` |
| minHeight (typed overload) | Covered | `exercisesMinHeightOverload` |
| enableJavaScript (typed overload) | Covered | `exercisesEnableJavaScriptOverload` |
| percyCSS (typed overload) | Covered | `exercisesPercyCssOverload` |
| scope (typed overload) | Covered | `exercisesScopeOverload` |
| responsiveSnapshotCapture (Map options) | Covered | `exercisesMapOptionsResponsiveAndReadiness` |
| readiness preset (Map options) | Covered | `exercisesMapOptionsResponsiveAndReadiness` |
| labels (Map options) | Covered | `exercisesMapOptionsLabelsAndTestCase` |
| testCase (Map options) | Covered | `exercisesMapOptionsLabelsAndTestCase` |
| devicePixelRatio (Map options) | Covered | `exercisesMapOptionsDevicePixelRatio` |
| browsers override (Map options) | Covered | `exercisesMapOptionsBrowsers` |
| regions (Map options) | Covered | `exercisesMapOptionsRegions` |
| sync mode (Map options) | Covered | `exercisesMapOptionsSync` |
| Map<String, Object> options overload | Covered | seven `exercisesMapOptions*` tests |
| `.percy.yml` global config | Covered | `.percy.yml` consumed at build start |
| environment info reporting | Covered | automatic via SDK client info |
| PERCY_SERVER_ADDRESS via env | Covered | CI advanced job picks up `PERCY_SERVER_ADDRESS` |
| `Percy.createRegion` static helper | Planned | — |
| `domTransformation` (Map options) | Planned | — |
| `discovery` per-snapshot | N/A | discovery is per-build only |
