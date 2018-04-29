package org.geotools.data.aggregate;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.geotools.data.DefaultRepository;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.NameImpl;
import org.junit.Before;
import org.opengis.feature.type.Name;

/** @source $URL$ */
public abstract class AbstractAggregatingStoreTest {
    DefaultRepository repository = new DefaultRepository();
    protected PropertyDataStore store1;
    protected PropertyDataStore store2;
    protected PropertyDataStore store3;
    protected PropertyDataStore store4;

    @Before
    public void setup() throws IOException {
        String base = "./src/test/resources/org/geotools/data/aggregate/";

        store1 = new PropertyDataStore(new File(base + "store1"));
        store1.setNamespaceURI(null);
        repository.register("store1", store1);

        store2 = new PropertyDataStore(new File(base + "store2"));
        store2.setNamespaceURI(null);
        repository.register("store2", store2);

        store3 = new PropertyDataStore(new File(base + "store3"));
        store3.setNamespaceURI(null);
        repository.register(new NameImpl("gt", "store3"), store3);

        store4 = new PropertyDataStore(new File(base + "store4"));
        store4.setNamespaceURI(null);
        repository.register("store4", store4);
    }

    protected void assertSingleSourceType(
            String typeName, Name storeName, AggregateTypeConfiguration config) {
        List<SourceType> sourceTypes = config.getSourceTypes(storeName);
        assertEquals(1, sourceTypes.size());
        SourceType st = sourceTypes.get(0);
        assertEquals(typeName, st.getTypeName());
        assertEquals(storeName, st.getStoreName());
    }
}
