// header start
package org.geotools.tutorial.datastore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

import com.csvreader.CsvReader;

public class CSVDataStore extends ContentDataStore {
// header end
    
    // constructor start
    File file;
    
    public CSVDataStore( File file ){
        this.file = file;
    }
    // constructor end
    
    /**
     * Allow read access to file; for our package visibile "friends".
     * Please close the reader when done.
     * @return CsvReader for file
     */
    CsvReader read() throws IOException {
        Reader reader = new FileReader(file);
        CsvReader csvReader = new CsvReader(reader);
        csvReader.close();
        return csvReader;
    }

    // createTypeNames start
    protected List<Name> createTypeNames() throws IOException {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        
        Name typeName = new NameImpl( name );
        return Collections.singletonList(typeName);
    }
    // createTypeNames end
    

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new CSVFeatureSource(entry, Query.ALL);
    }

}
