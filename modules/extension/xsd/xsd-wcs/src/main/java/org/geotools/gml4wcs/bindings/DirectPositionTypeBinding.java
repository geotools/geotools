package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/gml:DirectPositionType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;DirectPositionType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;DirectPosition instances hold the coordinates for a position within some coordinate reference system (CRS). Since DirectPositions, as data types, will often be included in larger objects (such as geometry elements) that have references to CRS, the &quot;srsName&quot; attribute will in general be missing, if this particular DirectPosition is included in a larger element with such a reference to a CRS. In this case, the CRS is implicitly assumed to take on the value of the containing object's CRS.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base=&quot;gml:doubleList&quot;&gt;
 *              &lt;attribute name=&quot;dimension&quot; type=&quot;positiveInteger&quot; use=&quot;optional&quot;&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The attribute &quot;dimension&quot; is the length of coordinate sequence (the number of entries in the list). This is determined by the coordinate reference system.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/bindings/DirectPositionTypeBinding.java $
 */
public class DirectPositionTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.DirectPositionType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return DirectPosition.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        DirectPosition dp = null;

        if ("pos".equals(instance.getName())) {
            String[] CP = instance.getText().split(" ");
            double[] coordinates = new double[CP.length];
            int c = 0;
            for (String coord : CP) {
                coordinates[c++] = Double.parseDouble(coord.trim());
            }

            dp = new GeneralDirectPosition(coordinates);
        }

        return dp;
    }

    public Element encode(Object object, Document document, Element value) throws Exception {
        DirectPosition dp = (DirectPosition) object;

        if (dp == null) {
            value.appendChild(document.createElementNS(GML.NAMESPACE, org.geotools.gml3.GML.Null.getLocalPart()));
        }
        
        double[] coordinates = dp.getCoordinate();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coordinates.length; i++) {
            sb.append(String.valueOf(coordinates[i]));

            if (i != (coordinates.length - 1)) {
                sb.append(" ");
            }
        }

        value.appendChild(document.createTextNode(sb.toString()));
        return null;
    }
    
    public Object getProperty(Object object, QName name) {
        DirectPosition dp = (DirectPosition) object;
        
        if (name.getLocalPart().equals("dimension")) {
            return dp.getDimension();
        }
        
        return null;
    }
}
