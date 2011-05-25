package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Binding object for the type http://www.opengis.net/gml:AbstractGeometryType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType abstract=&quot;true&quot; name=&quot;AbstractGeometryType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;All geometry elements are derived directly or indirectly from this abstract supertype. A geometry element may have an identifying attribute (&quot;id&quot;), a name (attribute &quot;name&quot;) and a description (attribute &quot;description&quot;). It may be associated with a spatial reference system (attribute &quot;srsName&quot;). The following rules shall be adhered: - Every geometry type shall derive from this abstract type. - Every geometry element (i.e. an element of a geometry type) shall be directly or indirectly in the substitution group of _Geometry.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractGeometryBaseType&quot;&gt;
 *              &lt;attribute name=&quot;srsName&quot; type=&quot;anyURI&quot; use=&quot;optional&quot;&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;No gid attribute added.&lt;/documentation&gt;
 *                      &lt;documentation&gt;In general srsName points to a CRS instance of CoordinateReferenceSystemType (see coordinateReferenceSystems.xsd). For well known references it is not required that the CRS description exists at the location the URI points to (Note: These &quot;WKCRS&quot;-ids still have to be specified).  If no srsName attribute is given, the CRS must be specified as part of the larger context this geometry element is part of, e.g. a geometric aggregate.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/bindings/AbstractGeometryTypeBinding.java $
 */
public class AbstractGeometryTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.AbstractGeometryType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Geometry.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        // set the crs
        if (value instanceof Geometry) {
            CoordinateReferenceSystem crs = GML3ParsingUtils.crs(node);

            if (crs != null) {
                Geometry geometry = (Geometry) value;
                geometry.setUserData(crs);
            }
        }

        return value;
    }

    public Object getProperty(Object object, QName name) throws Exception {
        Geometry geometry = (Geometry) object;
        if ("srsName".equals(name.getLocalPart())) {
            CoordinateReferenceSystem crs = GML3EncodingUtils.getCRS(geometry);

            if (crs != null) {
                return GML3EncodingUtils.crs(crs);
            }
        }

        if (GML.id.equals(name)) {
            return GML3EncodingUtils.getID(geometry);
        }

        if (GML.name.equals(name)) {
            return GML3EncodingUtils.getName(geometry);
        }

        if (GML.description.equals(name)) {
            return GML3EncodingUtils.getDescription(geometry);
        }
        return null;
    }

}
