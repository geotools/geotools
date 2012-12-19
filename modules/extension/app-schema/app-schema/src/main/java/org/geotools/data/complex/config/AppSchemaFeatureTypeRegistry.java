/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.config;

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDAttributeUseCategory;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.Types;
import org.geotools.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.GMLSchema;
import org.geotools.gml3.smil.SMIL20LANGSchema;
import org.geotools.gml3.smil.SMIL20Schema;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.geotools.xs.XS;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * A registry of GeoTools {@link AttributeType} and {@link AttributeDescriptor} lazily parsed from
 * the EMF {@link XSDTypeDefinition} and {@link XSDElementDeclaration} added through
 * {@link #addSchemas(SchemaIndex)}.
 * <p>
 * This class is meant to be used in conjunction with {@link EmfAppSchemaReader}. See
 * {@link AppSchemaDataAccessConfigurator}
 * </p>
 * <p>
 * Usage:
 * 
 * <pre>
 * <code>
 * FeatureTypeRegistry registry = new FeatureTypeRegistry();
 * EmfAppSchemaReader schemaParser = EmfAppSchemaReader.newInstance();
 * URL schemaLocation1 = ...
 * SchemaIndex schemas = schemaParser.parse(schemaLocation1, null);
 * registry.addSchemas(schemas);
 * URL schemaLocation2 = ...
 * schemas = schemaParser.parse(schemaLocation1, null);
 * registry.addSchemas(schemas);
 * 
 * Name typeName = ...
 * FeatureType ft = (FeatureType)registry.getAttributeType(typeName);
 * </p>
 * 
 * @author Gabriel Roldan
 * 
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/config/FeatureTypeRegistry.java $
 * @version $Id$
 * 
 */
public class AppSchemaFeatureTypeRegistry extends FeatureTypeRegistry {
   

    public AppSchemaFeatureTypeRegistry() {
        this(null);
    }

    public AppSchemaFeatureTypeRegistry(NamespaceSupport namespaces) {
        super(new ComplexFeatureTypeFactoryImpl(), namespaces==null? new GMLHandler(null) : new GMLHandler(namespaces.getURI("gml")));
    }

    @Override
    protected void onCreateFoundationTypes() {
        
        Schema schema;

        schema = new SMIL20Schema();
        importSchema(schema);

        schema = new SMIL20LANGSchema();
        importSchema(schema);

        schema = new GMLSchema();
        importSchema(schema);

        schema = new org.geotools.gml3.v3_2.GMLSchema();
        importSchema(schema);

        schema = new org.geotools.gml3.v3_2.gco.GCOSchema();
        importSchema(schema);

        schema = new org.geotools.gml3.v3_2.gmd.GMDSchema();
        importSchema(schema);

        schema = new org.geotools.gml3.v3_2.gmx.GMXSchema();
        importSchema(schema);

        schema = new org.geotools.gml3.v3_2.gsr.GSRSchema();
        importSchema(schema);

        schema = new org.geotools.gml3.v3_2.gss.GSSSchema();
        importSchema(schema);

        schema = new org.geotools.gml3.v3_2.gts.GTSSchema();
        importSchema(schema);

        // schedule the gml schemas to be lazily loaded for any type missing from the above
        // import of prebuilt types

        // GML 3.1
        addSchemas(Schemas.findSchemas(new GMLConfiguration()));
        // GML 3.2
        addSchemas(Schemas.findSchemas(new org.geotools.gml3.v3_2.GMLConfiguration()));

    }

    
    /**
     * Private inner glass for handling any request made to GML. Depending on the schema type
     * different version of GML class may be called upon. eg {@link org.geotools.gml3.v3_2.GML} or
     * {@link org.geotools.gml3.GML}
     * 
     * @author Victor Tey, CSIRO Exploration and Mining
     * @author Niels Charlier
     *
     */

    private static class GMLHandler implements FeatureTypeRegistryHelper {
        String namespace;

        public GMLHandler(String uri) {
            if (uri != null) {
                namespace = uri;
            } else {
                namespace = "";
            }
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

        @Override
        public boolean isFeatureType(XSDTypeDefinition typeDefinition) {
            return isDerivedFrom(typeDefinition, getAbstractFeatureType());
        }

        @Override
        public boolean isGeometryType(XSDTypeDefinition typeDefinition) {
            return isDerivedFrom(typeDefinition, getAbstractGeometryType());
        }

        @Override
        public boolean isIdentifiable(XSDComplexTypeDefinition typeDefinition) {
            List attributeUses = typeDefinition.getAttributeUses();

            final String idAttName = getId().getLocalPart();

            for (Iterator it = attributeUses.iterator(); it.hasNext();) {
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
    }
    

    /**
     * Returns whether <code>typeDefinition</code> has an ancestor named <code>baseTypeName</code>.
     * 
     * @param typeDefinition
     * @param baseTypeName
     * @return
     */
    private static boolean isDerivedFrom(final XSDTypeDefinition typeDefinition, final QName baseTypeName) {
        return isDerivedFrom(typeDefinition, Types.toTypeName(baseTypeName));
    }

    /**
     * Returns <code>true</code> if <code>typeDefinition</code> is derived from a type named
     * <code>superTypeName</code>
     * 
     * @param typeDefinition
     * @param superTypeName
     * @return
     */
    private static boolean isDerivedFrom(XSDTypeDefinition typeDefinition, final Name superTypeName) {

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

}
