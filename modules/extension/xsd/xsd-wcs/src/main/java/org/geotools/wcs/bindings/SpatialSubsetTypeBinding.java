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

package org.geotools.wcs.bindings;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.Gml4wcsFactory;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.Wcs10Factory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:SpatialSubsetType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name=&quot;SpatialSubsetType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of a subset of a coverage spatial domain. Currently, only a grid subset of a coverage domain. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base=&quot;wcs:SpatialDomainType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref=&quot;gml:Envelope&quot;/&gt;
 *                  &lt;element ref=&quot;gml:Grid&quot;/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/restriction&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class SpatialSubsetTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.SpatialSubsetType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SpatialSubsetType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        SpatialSubsetType spatialSubset = Wcs10Factory.eINSTANCE.createSpatialSubsetType();

        List<Node> envelopes = node.getChildren("Envelope");
        for (Node envelopeNode : envelopes) {
            ReferencedEnvelope envelope = (ReferencedEnvelope) envelopeNode.getValue();
            EnvelopeType env = Gml4wcsFactory.eINSTANCE.createEnvelopeType();

            env.setSrsName(CRS.lookupIdentifier(envelope.getCoordinateReferenceSystem(), true));

            DirectPositionType pos1 = Gml4wcsFactory.eINSTANCE.createDirectPositionType();
            DirectPositionType pos2 = Gml4wcsFactory.eINSTANCE.createDirectPositionType();

            pos1.setDimension(BigInteger.valueOf(2));
            pos2.setDimension(BigInteger.valueOf(2));

            pos1.setValue(Arrays.asList(envelope.getMinX(), envelope.getMinY()));
            pos2.setValue(Arrays.asList(envelope.getMaxX(), envelope.getMaxY()));

            env.getPos().add(pos1);
            env.getPos().add(pos2);

            spatialSubset.getEnvelope().add(envelope);
        }

        List<Node> gridsNode = node.getChildren("Grid");
        for (Node grid : gridsNode) spatialSubset.getGrid().add(grid.getValue());

        return spatialSubset;
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
}
