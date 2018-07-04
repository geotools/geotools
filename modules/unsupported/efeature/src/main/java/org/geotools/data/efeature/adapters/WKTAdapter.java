package org.geotools.data.efeature.adapters;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.eclipse.emf.query.conditions.IDataTypeAdapter;

/**
 * An Adapter class to be used to extract from -adapt- the argument object to some {@link String}
 * value that would later be used in <code>Condition</code> evaluation.
 *
 * <p>Clients can subclass it and provide their own implementation
 *
 * @see {@link Condition}
 * @source $URL$
 */
public abstract class WKTAdapter implements IDataTypeAdapter<String> {

    /** Cached {@link WKTWriter} instance for writing */
    public static final WKTWriter WRITER = new WKTWriter();

    /** Cached {@link WKTReader} instance for reading */
    public static final WKTReader READER = new WKTReader();

    /**
     * The simplest {@link WKTAdapter} implementation that represents the case when the object to
     * adapt is a {@link String} object or a {@link Geometry} instance.
     */
    public static final WKTAdapter DEFAULT =
            new WKTAdapter() {
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
                    } catch (Exception e) {
                        /* Consume */
                    }
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
    @Override
    public String adapt(Object value) {
        return toWKT(value);
    }
}
