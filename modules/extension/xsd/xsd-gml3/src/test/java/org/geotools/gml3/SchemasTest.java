package org.geotools.gml3;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xsd.Schemas;
import org.junit.Test;

public class SchemasTest {

    @Test
    public void testConcurrentParse() throws Exception {
        URL location = SchemasTest.class.getResource("states.xsd");
        final File schemaFile = new File(location.toURI());
        final List<XSDSchemaLocator> locators =
                Arrays.asList(GML.getInstance().createSchemaLocator());

        ExecutorService es = Executors.newFixedThreadPool(32);
        List<Future<Void>> results = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            Future<Void> future =
                    es.submit(
                            () -> {
                                XSDSchema schema =
                                        Schemas.parse(schemaFile.getAbsolutePath(), locators, null);
                                Schemas.dispose(schema);
                                return null;
                            });
            results.add(future);
        }

        // make sure none threw an exception
        for (Future<Void> future : results) {
            future.get();
        }
    }
}
