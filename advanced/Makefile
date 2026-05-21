NPM=node_modules/.bin

$(NPM):
	npm install --no-save @percy/cli@^1.31.13

.PHONY: install clean test test-advanced test-advanced-ci

install: $(NPM)

clean:
	rm -rf target node_modules advanced-requests.json

# Local run against a real PERCY_TOKEN.
test test-advanced: install
	$(NPM)/percy exec -- mvn test

# CI run in --testing mode + capture requests file.
test-advanced-ci: install
	PERCY_TOKEN=fake_token $(NPM)/percy exec --testing -- bash -c '\
		mvn test; \
		ec=$$?; \
		curl -fsS http://localhost:5338/test/requests > advanced-requests.json || true; \
		exit $$ec'
