The presence of the folder "app-schema-cache" triggers automatic configuration
of the AppSchemaCache used by AppSchemaValidationDemo.

If you are looking for downloaded schemas, they should be in the copy of this
folder in target/test-classes/test-data/validator-demo/app-schema-cache
because file-to-be-validated.xml is loaded from the classpath before the
source path.

See AppSchemaValidatorDemo for more.