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
 */
package org.geotools.csw.bindings;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.RecordType;
import net.opengis.cat.csw20.SimpleLiteral;
import net.opengis.ows10.WGS84BoundingBoxType;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.geotools.csw.CSW;
import org.geotools.csw.DC;
import org.geotools.csw.DCT;
import org.geotools.xsd.ComplexEMFBinding;
import org.geotools.xsd.ows.OWS;

public class RecordBinding extends ComplexEMFBinding {

    public RecordBinding() {
        super(Csw20Factory.eINSTANCE, CSW.RecordType);
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        RecordType record = (RecordType) object;

        List result = new ArrayList();
        XSDParticle previous = null;
        String previousName = null;
        for (SimpleLiteral sl : record.getDCElement()) {
            XSDSchema dctSchema = DCT.getInstance().getSchema();
            XSDElementDeclaration declaration = dctSchema.resolveElementDeclaration(sl.getName());
            if (declaration.getTypeDefinition() == null) {
                XSDSchema dcSchema = DC.getInstance().getSchema();
                declaration = dcSchema.resolveElementDeclaration(sl.getName());
            }
            if (declaration != null) {
                XSDParticle particle;
                if (previousName != null && previousName.equals(sl.getName())) {
                    particle = previous;
                } else {
                    particle = buildParticle(declaration);
                    previous = particle;
                    previousName = sl.getName();
                }
                result.add(new Object[] {particle, sl});
            }
        }

        if (record.getBoundingBox() != null && record.getBoundingBox().size() > 0) {
            for (Object box : record.getBoundingBox()) {
                XSDElementDeclaration bboxElement;
                if (box instanceof WGS84BoundingBoxType) {
                    bboxElement =
                            OWS.getInstance()
                                    .getSchema()
                                    .resolveElementDeclaration("WGS84BoundingBox");
                } else {
                    bboxElement =
                            OWS.getInstance().getSchema().resolveElementDeclaration("BoundingBox");
                }
                XSDParticle particle = buildParticle(bboxElement);
                result.add(new Object[] {particle, box});
            }
        }

        return result;
    }

    private XSDParticle buildParticle(XSDElementDeclaration declaration) {
        XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();
        particle.setContent(declaration);
        particle.setMinOccurs(0);
        particle.setMaxOccurs(-1);
        return particle;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        return null;
    }
}
