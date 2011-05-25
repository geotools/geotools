package org.geotools.gml4wcs.bindings;

import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml4wcs.GML;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.AttributeInstance;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/gml:EnvelopeType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 * 
 *  &lt;complexType name=&quot;EnvelopeType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Envelope defines an extent using a pair of positions defining opposite corners in arbitrary dimensions.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractGeometryType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs=&quot;2&quot; minOccurs=&quot;2&quot; ref=&quot;gml:pos&quot;/&gt;
 *              &lt;/sequence&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/bindings/EnvelopeTypeBinding.java $
 */
public class EnvelopeTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.EnvelopeType;
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
     * <!-- begin-user-doc --> ATTENTION: I'm assuming a LatLon envelope here.
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
            
            for (AttributeInstance att : instance.getAttributes()) {
                if (att.getName().equals("srsName"))
                    envelope.setCoordinateReferenceSystem(CRS.decode(att.getText()));
            }

            return envelope;
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    @Override
    public Element encode(Object object, Document document, Element value)
            throws Exception {
        GeneralEnvelope envelope = (GeneralEnvelope) object;

        if (envelope == null) {
            value.appendChild(document.createElementNS(GML.NAMESPACE, org.geotools.gml3.GML.Null.getLocalPart()));
        }

        return null;
    }

    public Object getProperty(Object object, QName name) {
        GeneralEnvelope envelope = (GeneralEnvelope) object;

        if (envelope == null) {
            return null;
        }

        if (name.getLocalPart().equals("srsName")) {
            try {
                return CRS.lookupIdentifier(envelope.getCoordinateReferenceSystem(), true);
            } catch (FactoryException e) {
                return null;
            }
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
