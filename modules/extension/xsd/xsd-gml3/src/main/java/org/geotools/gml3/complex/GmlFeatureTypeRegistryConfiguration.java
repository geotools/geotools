/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.complex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDAttributeUseCategory;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.type.Types;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.GMLSchema;
import org.geotools.gml3.smil.SMIL20LANGSchema;
import org.geotools.gml3.smil.SMIL20Schema;
import org.geotools.xs.XS;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.complex.FeatureTypeRegistryConfiguration;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;

/**
 * Feature Type Registry Configuration for GML. Depending on the schema type different version of
 * GML class may be called upon. eg {@link org.geotools.gml3.v3_2.GML} or {@link
 * org.geotools.gml3.GML}
 *
 * @author Victor Tey, CSIRO Exploration and Mining
 * @author Niels Charlier
 */
public class GmlFeatureTypeRegistryConfiguration implements FeatureTypeRegistryConfiguration {

    String namespace;

    public GmlFeatureTypeRegistryConfiguration(String uri) {
        if (uri != null) {
            namespace = uri;
        } else {
            namespace = "";
        }
    }

    @Override
    public Collection<Schema> getSchemas() {
        ArrayList<Schema> schemas = new ArrayList<Schema>();
        schemas.add(new SMIL20Schema());
        schemas.add(new SMIL20LANGSchema());
        schemas.add(new GMLSchema());
        schemas.add(new org.geotools.gml3.v3_2.GMLSchema());
        schemas.add(new org.geotools.gml3.v3_2.gco.GCOSchema());
        schemas.add(new org.geotools.gml3.v3_2.gmd.GMDSchema());
        schemas.add(new org.geotools.gml3.v3_2.gmx.GMXSchema());
        schemas.add(new org.geotools.gml3.v3_2.gsr.GSRSchema());
        schemas.add(new org.geotools.gml3.v3_2.gss.GSSSchema());
        schemas.add(new org.geotools.gml3.v3_2.gts.GTSSchema());
        return schemas;
    }

    @Override
    public Collection<Configuration> getConfigurations() {
        ArrayList<Configuration> configurations = new ArrayList<Configuration>();
        configurations.add(new GMLConfiguration());
        configurations.add(new org.geotools.gml3.v3_2.GMLConfiguration());
        return configurations;
    }

    public QName getAbstractFeatureType() {
        if (namespace.equals(org.geotools.gml3.v3_2.GML.NAMESPACE)) {
            return org.geotools.gml3.v3_2.GML.AbstractFeatureType;
        } else {
            return org.geotools.gml3.GML.AbstractFeatureType;
        }
    }

    public QName getAbstractGeometryType() {
        if (namespace.equals(org.geotools.gml3.v3_2.GML.NAMESPACE)) {
            return org.geotools.gml3.v3_2.GML.AbstractGeometryType;
        } else {
            return org.geotools.gml3.GML.AbstractGeometryType;
        }
    }

    public String getNameSpace() {
        if (namespace.equals(org.geotools.gml3.v3_2.GML.NAMESPACE)) {
            return org.geotools.gml3.v3_2.GML.NAMESPACE;
        } else {
            return org.geotools.gml3.GML.NAMESPACE;
        }
    }

    public QName getId() {
        if (namespace.equals(org.geotools.gml3.v3_2.GML.NAMESPACE)) {
            return org.geotools.gml3.v3_2.GML.id;
        } else {
            return org.geotools.gml3.GML.id;
        }
    }

    public void setEmptyNamespace(XSDTypeDefinition typeDefinition) {
        if (namespace.isEmpty()) {
            // GEOT-4756:
            // namespace could be unset when GML namespace is not set in the mapping file
            // which is valid when it's not used in the mapping
            if (isBasedOn(typeDefinition, org.geotools.gml3.v3_2.GML.NAMESPACE)) {
                namespace = org.geotools.gml3.v3_2.GML.NAMESPACE;
            } else if (isBasedOn(typeDefinition, org.geotools.gml3.GML.NAMESPACE)) {
                namespace = org.geotools.gml3.GML.NAMESPACE;
            }
        }
    }

    @Override
    public boolean isFeatureType(XSDTypeDefinition typeDefinition) {
        setEmptyNamespace(typeDefinition);
        return isDerivedFrom(typeDefinition, getAbstractFeatureType());
    }

    @Override
    public boolean isGeometryType(XSDTypeDefinition typeDefinition) {
        setEmptyNamespace(typeDefinition);
        return isDerivedFrom(typeDefinition, getAbstractGeometryType());
    }

    @Override
    public boolean isIdentifiable(XSDComplexTypeDefinition typeDefinition) {
        List attributeUses = typeDefinition.getAttributeUses();

        final String idAttName = getId().getLocalPart();

        for (Iterator it = attributeUses.iterator(); it.hasNext(); ) {
            XSDAttributeUse use = (XSDAttributeUse) it.next();
            XSDAttributeUseCategory useCategory = use.getUse();

            XSDAttributeDeclaration idAtt = use.getAttributeDeclaration();

            String targetNamespace = idAtt.getTargetNamespace();
            String name = idAtt.getName();
            if (getNameSpace().equals(targetNamespace) && idAttName.equals(name)) {
                if (XSDAttributeUseCategory.REQUIRED_LITERAL.equals(useCategory)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the <code>typeDefinition</code> is based on provided <code>superNS</code>.
     */
    private static boolean isBasedOn(XSDTypeDefinition typeDefinition, final String superNS) {

        XSDTypeDefinition baseType;

        String targetNamespace;
        String name;
        while ((baseType = typeDefinition.getBaseType()) != null) {
            targetNamespace = baseType.getTargetNamespace();
            name = baseType.getName();
            if (XS.NAMESPACE.equals(targetNamespace) && XS.ANYTYPE.getLocalPart().equals(name)) {
                // break the loop or this goes forever
                return false;
            }
            if (superNS.equals(targetNamespace)) {
                return true;
            }
            typeDefinition = baseType;
        }
        return false;
    }

    /**
     * Returns whether <code>typeDefinition</code> has an ancestor named <code>baseTypeName</code>.
     */
    private static boolean isDerivedFrom(
            final XSDTypeDefinition typeDefinition, final QName baseTypeName) {
        return isDerivedFrom(typeDefinition, Types.toTypeName(baseTypeName));
    }

    /**
     * Returns <code>true</code> if <code>typeDefinition</code> is derived from a type named <code>
     * superTypeName</code>
     */
    private static boolean isDerivedFrom(
            XSDTypeDefinition typeDefinition, final Name superTypeName) {

        XSDTypeDefinition baseType;
        final String superNS = superTypeName.getNamespaceURI();
        final String superName = superTypeName.getLocalPart();

        String targetNamespace;
        String name;
        while ((baseType = typeDefinition.getBaseType()) != null) {
            targetNamespace = baseType.getTargetNamespace();
            name = baseType.getName();
            if (XS.NAMESPACE.equals(targetNamespace) && XS.ANYTYPE.getLocalPart().equals(name)) {
                return false;
            }
            if (superNS.equals(targetNamespace) && superName.equals(name)) {
                return true;
            }
            typeDefinition = baseType;
        }
        return false;
    }

    /**
     * Map of the qualified-name of a known type in each supported GML version to the {@link
     * Configuration} for that GML version.
     */
    @SuppressWarnings("serial")
    private static final Map<QName, Class<? extends Configuration>>
            SUPPORTED_GML_KNOWN_TYPE_TO_CONFIGURATION_MAP //
            =
                    new LinkedHashMap<QName, Class<? extends Configuration>>() {
                        {
                            // GML 3.1
                            put(GML.AbstractFeatureType, GMLConfiguration.class);
                            // GML 3.2
                            put(
                                    org.geotools.gml3.v3_2.GML.AbstractFeatureType,
                                    org.geotools.gml3.v3_2.GMLConfiguration.class);
                        }
                    };

    public static Configuration findGmlConfiguration(Configuration configuration) {
        SchemaIndex index = null;
        try {
            index = Schemas.findSchemas(configuration);
            for (QName name : SUPPORTED_GML_KNOWN_TYPE_TO_CONFIGURATION_MAP.keySet()) {
                XSDTypeDefinition type = index.getTypeDefinition(name);
                if (type != null) {
                    try {
                        return SUPPORTED_GML_KNOWN_TYPE_TO_CONFIGURATION_MAP
                                .get(name)
                                .getDeclaredConstructor()
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            for (XSDSchema schema : index.getSchemas()) {
                String ns = schema.getTargetNamespace();
                if (ns != null && ns.startsWith("http://www.opengis.net/gml")) {
                    throw new RuntimeException(
                            "Unsupported GML version for schema at "
                                    + configuration.getXSD().getSchemaLocation());
                }
            }
        } finally {
            if (index != null) {
                index.destroy();
            }
        }
        return null;
    }
}
