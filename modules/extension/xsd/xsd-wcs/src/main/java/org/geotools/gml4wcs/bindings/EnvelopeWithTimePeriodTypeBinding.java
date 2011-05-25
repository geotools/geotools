package org.geotools.gml4wcs.bindings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml4wcs.GML;
import org.geotools.metadata.iso.extent.ExtentImpl;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.temporal.Position;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type
 * http://www.opengis.net/gml:EnvelopeWithTimePeriodType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;EnvelopeWithTimePeriodType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Envelope that includes also a temporal extent.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:EnvelopeType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs=&quot;2&quot; minOccurs=&quot;2&quot; ref=&quot;gml:timePosition&quot;/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute default=&quot;#ISO-8601&quot; name=&quot;frame&quot; type=&quot;anyURI&quot; use=&quot;optional&quot;/&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/bindings/EnvelopeWithTimePeriodTypeBinding.java $
 */
public class EnvelopeWithTimePeriodTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.EnvelopeWithTimePeriodType;
    }

    /**
     * <!-- begin-user-doc --> 
     * <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return GeneralEnvelope.class;
    }

    /**
     * <!-- begin-user-doc --> 
     * <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        GeneralEnvelope envelope = (GeneralEnvelope) value;

        List<Node> timePositions = (List<Node>) node.getChildren("timePosition");

        if (timePositions != null && !timePositions.isEmpty()) {
            final Map<String, Object> properties = new HashMap<String, Object>(4);
            properties.put(CoordinateReferenceSystem.NAME_KEY, "Compound");
            properties.put(CoordinateReferenceSystem.DOMAIN_OF_VALIDITY_KEY, ExtentImpl.WORLD);

            CoordinateReferenceSystem crs = new DefaultCompoundCRS(properties,
                    new CoordinateReferenceSystem[] {
                            envelope.getCoordinateReferenceSystem(),
                            DefaultTemporalCRS.TRUNCATED_JULIAN });

            double[] minCP = new double[envelope.getDimension() + 1];
            double[] maxCP = new double[envelope.getDimension() + 1];

            for (int i = 0; i < envelope.getDimension(); i++) {
                minCP[i] = envelope.getLowerCorner().getOrdinate(i);
                maxCP[i] = envelope.getUpperCorner().getOrdinate(i);
            }

            DefaultTemporalCRS TCRS = (DefaultTemporalCRS) ((CompoundCRS) crs).getCoordinateReferenceSystems().get(1);

            Node timePositionNodeBegin = timePositions.get(0);
            Node timePositionNodeEnd = timePositions.get(1);
            minCP[minCP.length - 1] = TCRS.toValue(((DefaultPosition)timePositionNodeBegin.getValue()).getDate());
            maxCP[maxCP.length - 1] = TCRS.toValue(((DefaultPosition)timePositionNodeEnd.getValue()).getDate());

            GeneralDirectPosition minDP = new GeneralDirectPosition(minCP);
                                  minDP.setCoordinateReferenceSystem(crs);
            GeneralDirectPosition maxDP = new GeneralDirectPosition(maxCP);
                                  maxDP.setCoordinateReferenceSystem(crs);

            GeneralEnvelope envelopeWithTime = new GeneralEnvelope(minDP, maxDP);

            return envelopeWithTime;
        }

        return envelope;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.xml.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return AFTER;
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

        if (name.getLocalPart().equals("timePosition")) {
            CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();

            TemporalCRS temporalCRS = null;

            if (crs instanceof CompoundCRS) {
                List CRSs = ((DefaultCompoundCRS) crs).getCoordinateReferenceSystems();

                for (Object item : CRSs) {
                    if (item instanceof TemporalCRS) {
                        temporalCRS = (TemporalCRS) item;
                    }
                }
            }

            if (temporalCRS != null) {
                List<Position> envelopePositions = new LinkedList<Position>();

                Position beginning = new DefaultPosition(
                        ((DefaultTemporalCRS) temporalCRS).toDate(envelope.getLowerCorner().getOrdinate(envelope.getDimension() - 1)));
                Position ending = new DefaultPosition(
                        ((DefaultTemporalCRS) temporalCRS).toDate(envelope.getUpperCorner().getOrdinate(envelope.getDimension() - 1)));

                envelopePositions.add(beginning);
                envelopePositions.add(ending);

                return envelopePositions;
            }
        }

        return null;
    }
}
