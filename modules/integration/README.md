Integration Testing
===================

These integration tests use maven-invoker-plugin to treat the current build as a local repository for writing tests
that require an independent environment.

How integration tests work:

1. `integration-test`: Uses `exec:exec` to run test in independent java environment, configured with its environmental varaibles, and system properties. The output is recorded to a `build.log` file.
2. `verify` goal: Uses bean shell `postbuild.bsh` script to check `build.log` file output.

For more information see [maven-invoker-plugin](https://maven.apache.org/plugins/maven-invoker-plugin/).