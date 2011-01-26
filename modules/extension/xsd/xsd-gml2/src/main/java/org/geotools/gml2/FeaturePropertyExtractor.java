/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.FeatureCollection;
import org.geotools.xml.ComplexBinding;
import org.geotools.xml.PropertyExtractor;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * Special property extractor for extracting attributes from features.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @deprecated This interface is replaced with {@link ComplexBinding#getProperties(Object, XSDElementDeclaration)}
 *
 * @source $URL$
 */
public class FeaturePropertyExtractor implements PropertyExtractor {
    SchemaIndex schemaIndex;

    public FeaturePropertyExtractor(SchemaIndex schemaIndex) {
        this.schemaIndex = schemaIndex;
    }

    public boolean canHandle(Object object) {
        return object instanceof SimpleFeature && !(object instanceof FeatureCollection);
    }

    public List properties(Object object, XSDElementDeclaration element) {
        SimpleFeature feature = (SimpleFeature) object;

        //check if this was a resolved feature, if so dont return anything
        // TODO: this is just a hack for our lame xlink implementation
        if (feature.getUserData().get("xlink:id") != null) {
            return Collections.EMPTY_LIST;
        }

        SimpleFeatureType featureType = feature.getFeatureType();

        String namespace = featureType.getName().getNamespaceURI();

        if (namespace == null) {
            namespace = element.getTargetNamespace();
        }

        String typeName = featureType.getTypeName();

        //find the type in the schema
        XSDTypeDefinition type = schemaIndex.getTypeDefinition(new QName(namespace, typeName));

        if (type == null) {
            //type not found, do a check for an element, and use its type
            XSDElementDeclaration e = schemaIndex.getElementDeclaration(new QName(namespace,
                        typeName));

            if (e != null) {
                type = e.getTypeDefinition();
            }
        }

        if (type == null) {
            String msg = "Could not find element declaration: (" + namespace + ", " + typeName
                + " )";
            throw new RuntimeException(msg);
        }

        List particles = Schemas.getChildElementParticles(type, true);
        List properties = new ArrayList();

        for (Iterator p = particles.iterator(); p.hasNext();) {
            XSDParticle particle = (XSDParticle) p.next();
            XSDElementDeclaration attribute = (XSDElementDeclaration) particle.getContent();

            if (attribute.isElementDeclarationReference()) {
                attribute = attribute.getResolvedElementDeclaration();
            }

            //ignore gml attributes
            if (GML.NAMESPACE.equals(attribute.getTargetNamespace())) {
                continue;
            }

            //make sure the feature type has an element
            if (featureType.getDescriptor(attribute.getName()) == null) {
                continue;
            }

            //get the value
            Object attributeValue = feature.getAttribute(attribute.getName());
            properties.add(new Object[] { particle, attributeValue });
        }

        return properties;
    }
}
