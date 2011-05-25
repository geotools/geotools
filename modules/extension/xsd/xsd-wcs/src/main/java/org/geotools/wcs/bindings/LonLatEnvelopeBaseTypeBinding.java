package org.geotools.wcs.bindings;

import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml3.GML;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type
 * http://www.opengis.net/wcs:LonLatEnvelopeBaseType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;LonLatEnvelopeBaseType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;For WCS use, LonLatEnvelopeBaseType restricts gml:Envelope to the WGS84 geographic CRS with Longitude preceding Latitude and both using decimal degrees only. If included, height values are third and use metre units. &lt;/documentation&gt;
 *          &lt;documentation&gt;Envelope defines an extent using a pair of positions defining opposite corners in arbitrary dimensions. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base=&quot;gml:EnvelopeType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs=&quot;2&quot; minOccurs=&quot;2&quot; ref=&quot;gml:pos&quot;/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute fixed=&quot;urn:ogc:def:crs:OGC:1.3:CRS84&quot;
 *                  name=&quot;srsName&quot; type=&quot;anyURI&quot; use=&quot;optional&quot;/&gt;
 *          &lt;/restriction&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/LonLatEnvelopeBaseTypeBinding.java $
 */
public class LonLatEnvelopeBaseTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return WCS.LonLatEnvelopeBaseType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class<GeneralEnvelope> getType() {
        return GeneralEnvelope.class;
    }

    /**
     * <!-- begin-user-doc --> 
     * ATTENTION: I'm assuming a LatLon envelope here.
     * <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        List positions = node.getChildren("pos");

        if (!positions.isEmpty() && (positions.size() == 2)) {
            Node n1 = (Node) positions.get(0);
            Node n2 = (Node) positions.get(1);
            GeneralDirectPosition p1 = (GeneralDirectPosition) n1.getValue();
            GeneralDirectPosition p2 = (GeneralDirectPosition) n2.getValue();

            GeneralEnvelope envelope = new GeneralEnvelope(p1, p2);
            
            if (p1.getDimension() == 2 && p1.getDimension() == 2) {
                envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

                return envelope;
            } else if (p1.getDimension() > 2 && p2.getDimension() > 2) {
                envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84_3D);

                return envelope;
            }
        }

        if (!positions.isEmpty()) {
            throw new RuntimeException("Envelope can have only two coordinates");
        }

        throw new RuntimeException("Could not find coordinates for envelope");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.xml.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /*
     * <!-- begin-user-doc --> ATTENTION: I'm assuming a LatLon envelope here.
     * <!-- end-user-doc -->
     */
    @Override
    public Element encode(Object object, Document document, Element value)
            throws Exception {
        GeneralEnvelope envelope = (GeneralEnvelope) object;

        if (envelope == null) {
            value.appendChild(document.createElementNS(GML.NAMESPACE, GML.Null.getLocalPart()));
        }

        return null;
    }

    public Object getProperty(Object object, QName name) {
        GeneralEnvelope envelope = (GeneralEnvelope) object;

        if (envelope == null) {
            return null;
        }

        if (name.getLocalPart().equals("srsName")) {
            return "WGS84(DD)";
        }

        if (name.getLocalPart().equals("pos")) {
            CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();

            GeographicCRS spatialCRS = null;

            if (crs instanceof CompoundCRS) {
                List CRSs = ((DefaultCompoundCRS) crs).getCoordinateReferenceSystems();

                for (Object item : CRSs) {
                    if (item instanceof GeographicCRS) {
                        spatialCRS = (GeographicCRS) item;
                    }
                }
            } else {
                spatialCRS = (GeographicCRS) envelope.getCoordinateReferenceSystem();
            }

            if (spatialCRS != null) {
                List<DirectPosition> envelopePositions = new LinkedList<DirectPosition>();
                
                GeneralDirectPosition lowerCorner = new GeneralDirectPosition(envelope.getCoordinateReferenceSystem());
                GeneralDirectPosition upperCorner = new GeneralDirectPosition(envelope.getCoordinateReferenceSystem());

                for (int i = 0; i < spatialCRS.getCoordinateSystem().getDimension(); i++) {
                    lowerCorner.setOrdinate(i, envelope.getLowerCorner().getOrdinate(i));
                    upperCorner.setOrdinate(i, envelope.getUpperCorner().getOrdinate(i));
                }

                envelopePositions.add(lowerCorner);
                envelopePositions.add(upperCorner);


                return envelopePositions;
            }
        }

        return null;
    }
}
