package org.geotools.data.property;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.logging.Logging;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * FeatureReader access to the contents of a PropertyFile.
 * 
 * @author Jody Garnett
 * @version 8.0.0
 * @since 2.0.0
 */
public class PropertyFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.property");

    PropertyAttributeReader reader;

    /**
     * Creates a new PropertyFeatureReader object.
     * 
     * @param directory Directory containing property file
     * @param typeName TypeName indicating file to read
     * 
     * @throws IOException
     */
    public PropertyFeatureReader(File directory, String typeName) throws IOException {
        File file = new File(directory, typeName + ".properties");
        reader = new PropertyAttributeReader(file);
    }

    /**
     * Access to schema description.
     * 
     * @return SimpleFeatureType describing attribtues
     */
    public SimpleFeatureType getFeatureType() {
        return reader.type;
    }

    /**
     * Access the next feature (if available).
     * 
     * @return SimpleFeature read from property file
     * 
     * @throws IOException
     * @throws IllegalAttributeException
     * @throws NoSuchElementException
     */
    public SimpleFeature next() throws IOException, IllegalAttributeException,
            NoSuchElementException {
        reader.next();

        SimpleFeatureType type = reader.type;
        String fid = reader.getFeatureID();
        Object[] values = new Object[reader.getAttributeCount()];

        for (int i = 0; i < reader.getAttributeCount(); i++) {
            try {
                values[i] = reader.read(i);
            } catch (RuntimeException e) {
                values[i] = null;
            } catch (IOException e) {
                throw e;
            }
        }
        return SimpleFeatureBuilder.build(type, values, fid);
    }

    /**
     * Check if additional contents are available.
     * 
     * @return <code>true</code> if additional contents are available
     * 
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    /**
     * Close the FeatureReader when not in use.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        if (reader == null) {
            LOGGER.warning("Stream seems to be already closed.");
        } else {
            reader.close();
        }
        reader = null;
    }
}
