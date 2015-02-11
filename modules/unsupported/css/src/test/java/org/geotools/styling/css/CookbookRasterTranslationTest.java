package org.geotools.styling.css;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

public class CookbookRasterTranslationTest extends AbstractIntegrationTest {

    public CookbookRasterTranslationTest(String name, File file) {
        super(name, file, true);
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        List<Object[]> result = new ArrayList<>();
        File root = new File("./src/test/resources/css/cookbook/raster");
        for (File file : root.listFiles()) {
            if (file.getName().endsWith(".css")) {
                result.add(new Object[] { file.getName(), file });
            }
        }

        return result;
    }
}
