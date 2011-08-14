package org.geotools.data.aggregate;

import java.io.File;
import java.io.IOException;

import org.geotools.data.DefaultRepository;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.NameImpl;
import org.junit.Before;

public abstract class AbstractAggregatingStoreTest {
    DefaultRepository repository = new DefaultRepository();
    protected PropertyDataStore store1;
    protected PropertyDataStore store2;
    protected PropertyDataStore store3;

    @Before
    public void setup() throws IOException {
        String base = "./src/test/resources/org/geotools/data/aggregate/";

        store1 = new PropertyDataStore(new File(base + "store1"));
        repository.register("store1", store1);

        store2 = new PropertyDataStore(new File(base + "store2"));
        repository.register("store2", store2);

        store3 = new PropertyDataStore(new File(base + "store3"));
        repository.register(new NameImpl("gt", "store3"), store3);
    }

}
