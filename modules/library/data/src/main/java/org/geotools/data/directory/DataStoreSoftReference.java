package org.geotools.data.directory;

import java.lang.ref.SoftReference;

import org.geotools.data.DataStore;
import org.geotools.util.WeakCollectionCleaner;

public class DataStoreSoftReference extends SoftReference<DataStore> {

    public DataStoreSoftReference(DataStore referent) {
        super(referent, WeakCollectionCleaner.DEFAULT.getReferenceQueue());
    }
    
    @Override
    public void clear() {
        DataStore store = get();
        if(store != null)
            store.dispose();
        super.clear();
    }

}
