package org.geotools.data.efeature;

import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.IDataTypeAdapter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * An Adapter class to be used to extract from -adapt- the argument object to some {@link String}
 * value that would later be used in <code>Condition</code> evaluation.
 * 
 * Clients can subclass it and provide their own implementation
 * 
 * @see {@link Condition}
 */
public abstract class WKTAdapter implements IDataTypeAdapter<String> {

    /**
     * Cached {@link WKTWriter} instance for writing
     */
    public final static WKTWriter WRITER = new WKTWriter();

    /**
     * Cached {@link WKTReader} instance for reading
     */
    public final static WKTReader READER = new WKTReader();

    /**
     * The simplest {@link WKTAdapter} implementation that represents 
     * the case when the object to adapt is a {@link String} object or
     * a {@link Geometry} instance.
     */
    public static final WKTAdapter DEFAULT = new WKTAdapter() {
        @Override
        public String toWKT(Object object) {
            try {
                //
                // Validate?
                //
                if (object instanceof String && ((String) object).length() != 0) {
                    // Will fail if not valid geometry
                    //
                    object = READER.read((String) object);
                }
                //
                // If geometry, write to WKT
                //
                if (object instanceof Geometry) {
                    return WRITER.write((Geometry) object);
                }
            } catch (Exception e) { /* Consume */ }
            //
            // All attempts to adapt failed
            //
            throw new IllegalArgumentException("Object " + object + " is not a WKT");
        }

    };

    /**
     * Adapts given object to a WKT {@link String}
     * 
     * @param object - the object to adapt to a WKT {@link String} 
     * @return a WKT {@link String}
     */
    public abstract String toWKT(Object object);

    /**
     * Adapts given object to a WKT {@link String}
     * 
     * @param object - the object to adapt to a WKT {@link String} 
     * @return a WKT {@link String}
     */
    public String adapt(Object value) {
        return toWKT(value);
    }

}
