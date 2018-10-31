package org.geotools.brewer.styling.builder;

import static org.junit.Assert.*;

import java.util.logging.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.logging.Logging;
import org.geotools.xml.styling.SLDTransformer;
import org.opengis.filter.FilterFactory2;

public abstract class AbstractStyleTest {

    static final Logger LOGGER = Logging.getLogger(AbstractStyleTest.class);

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    protected void print(Object styleObject) {
        try {
            SLDTransformer tx = new SLDTransformer();
            tx.setIndentation(2);
            LOGGER.info(tx.transform(styleObject));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while printing the style", e);
        }
    }

    protected void assertSimpleStyle(StyleCollector collector) {
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(1, collector.rules.size());
        assertEquals(1, collector.symbolizers.size());
        assertEquals(1, collector.styles.size());
        assertTrue(collector.layers.size() == 0 || collector.layers.size() == 1);
    }
}
