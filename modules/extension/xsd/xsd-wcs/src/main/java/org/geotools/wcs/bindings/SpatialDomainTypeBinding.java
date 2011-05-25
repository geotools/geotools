package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wcs10.SpatialDomainType;

import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:SpatialDomainType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;SpatialDomainType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Defines the spatial domain of a coverage offering. A server shall describe the spatial domain by its edges, using one or more gml:Envelope elements. The gml:EnvelopeWithTimePeriod element may be used in place of gml:Envelope, to add the time bounds of the coverage offering. Each of these elements describes a bounding box defined by two points in space (or two positions in space and two in time). This bounding box could simply duplicate the information in the lonLatEnvelope of CoverageOfferingBrief; but the intent is to describe the locations in more detail (e.g., in several different CRSs, or several rectangular areas instead of one overall bounding box). 
 *  
 *  In addition, a server can describe the internal grid structure of a coverage offering, using a gml:Grid (or gml:RectifiedGrid) in addition to a gml:Envelope. This element can help clients assess the fitness of the gridded data for their use (e.g. its native resolution, inferred from the offsetVector of a gml:RectifiedGrid), and to formulate grid coverage requests expressed in the internal grid coordinate reference system.
 *  
 *  Finally, a server can describe the spatial domain by means of a (repeatable) gml:Polygon, representing the polygon(s) covered by the coverage spatial domain. This is particularly useful for areas that are poorly approximated by a gml:Envelope (such as satellite image swaths, island groups, other non-convex areas). &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs=&quot;unbounded&quot; ref=&quot;gml:Envelope&quot;/&gt;
 *          &lt;element maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot; ref=&quot;gml:Grid&quot;/&gt;
 *          &lt;element maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot; ref=&quot;gml:Polygon&quot;/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/SpatialDomainTypeBinding.java $
 */
public class SpatialDomainTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return WCS.SpatialDomainType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return SpatialDomainType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        
        return super.parse(instance, node, value);
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

}
