/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */

package org.geotools.gml4wcs.bindings;

import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CompoundCRS;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.GeneralPosition;
import org.geotools.gml4wcs.GML;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.AttributeInstance;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/gml:EnvelopeType.
 *
 * <p>
 *
 * <pre>
 *  <code>
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
 *  </pre>
 *
 * @generated
 */
public class EnvelopeTypeBinding extends AbstractComplexBinding {

    /** @generated */
    @Override
    public QName getTarget() {
        return GML.EnvelopeType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class<GeneralBounds> getType() {
        return GeneralBounds.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * ATTENTION: I'm assuming a LatLon envelope here.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List positions = node.getChildren("pos");

        if (!positions.isEmpty() && positions.size() == 2) {
            Node n1 = (Node) positions.get(0);
            Node n2 = (Node) positions.get(1);
            GeneralPosition p1 = (GeneralPosition) n1.getValue();
            GeneralPosition p2 = (GeneralPosition) n2.getValue();

            GeneralBounds envelope = new GeneralBounds(p1, p2);

            for (AttributeInstance att : instance.getAttributes()) {
                if (att.getName().equals("srsName")) envelope.setCoordinateReferenceSystem(CRS.decode(att.getText()));
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
     * @see org.geotools.xsd.AbstractComplexBinding#getExecutionMode()
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
    public Element encode(Object object, Document document, Element value) throws Exception {
        GeneralBounds envelope = (GeneralBounds) object;

        if (envelope == null) {
            value.appendChild(document.createElementNS(GML.NAMESPACE, org.geotools.gml3.GML.Null.getLocalPart()));
        }

        return null;
    }

    @Override
    public Object getProperty(Object object, QName name) {
        GeneralBounds envelope = (GeneralBounds) object;

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
                List<Position> envelopePositions = new LinkedList<>();

                GeneralPosition lowerCorner = new GeneralPosition(envelope.getCoordinateReferenceSystem());
                GeneralPosition upperCorner = new GeneralPosition(envelope.getCoordinateReferenceSystem());

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
