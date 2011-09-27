package org.geotools.data.efeature.adapters;

import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.IDataTypeAdapter;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

/**
 * An Adapter class to be used to extract from -adapt- the 
 * argument object to some {@link Geometry} value that 
 * would later be used in <code>Condition</code> evaluation.
 * 
 * Clients can subclass it and provide their own implementation
 * 
 * @see {@link Condition}
 *
 * @source $URL$
 */
public abstract class GeometryAdapter implements IDataTypeAdapter<Geometry> {

    /**
     * The simplest {@link GeometryAdapter} implementation that represents 
     * the case when the argument object to adapt is a
     * <ol> 
     *  <li>{@link Geometry}</li>
     *  <li>{@link DataTypes#WKT} formated string</li>
     *  <li>{@link DataTypes#WKB} formated byte array</li>
     * </ol>
     */
    public static final GeometryAdapter DEFAULT = new GeometryAdapter() {
        @Override
        public Geometry toGeometry(Object object) {
            try {
                //
                // Is not null?
                //
                if(object!=null) {
                    if (object instanceof Geometry) {
                        return (Geometry) object;
                    }
                    else if(object instanceof byte[]) {
                        return WKBAdapter.READER.read((byte[])object);
                    }
                    else if(Geometry.class.isAssignableFrom(object.getClass())) {
                        return DataBuilder.toEmptyGeometry(object.getClass());
                    }
                    //
                    // As a last resort, try as WKT 
                    //
                    String s = (object != null ? object.toString() : null);
                    if (!(s == null || s.length() == 0)) {
                        return WKTAdapter.READER.read(s);
                    }
                }
            } catch (ParseException e) { /*CONSUME*/ }
            //
            // All attempts to adapt failed
            //
            throw new IllegalArgumentException("Object " + object + " is not a Geometry");
        }

    };

    /**
    * Adapts given object to a {@link Geometry} instance.
    * <p>
    * The following data types are supported:
     * <ol> 
     *  <li>{@link Geometry}</li>
     *  <li>{@link DataTypes#WKT} formated string</li>
     *  <li>{@link DataTypes#WKB} formated byte array</li>
     * </ol>
    * @param object - the object to adapt to a {@link Geometry} instance 
    * @return a {@link Geometry}
    */
    public abstract Geometry toGeometry(Object object) throws IllegalArgumentException;

    /**
     * Adapts given object to a {@link Geometry} instance.
     * <p>
     * The following data types are supported:
      * <ol> 
      *  <li>{@link Geometry}</li>
      *  <li>{@link DataTypes#WKT} formated string</li>
      *  <li>{@link DataTypes#WKB} formated byte array</li>
      * </ol>
     * @param object - the object to adapt to a {@link Geometry} instance 
     * @return a {@link Geometry}
     */
    @Override
    public Geometry adapt(Object value) throws IllegalArgumentException {
        return toGeometry(value);
    }

}
