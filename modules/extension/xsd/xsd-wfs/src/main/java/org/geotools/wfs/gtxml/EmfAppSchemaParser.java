/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.gtxml;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.data.DataSourceException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml3.ApplicationSchemaConfiguration;
import org.geotools.gml3.bindings.GML3ParsingUtils;
import org.geotools.xsd.Binding;
import org.geotools.xsd.BindingFactory;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.impl.BindingFactoryImpl;
import org.geotools.xsd.impl.BindingLoader;
import org.geotools.xsd.impl.BindingWalkerFactoryImpl;
import org.geotools.xsd.impl.NamespaceSupportWrapper;
import org.geotools.xsd.impl.ParserHandler;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Utility class to parse FeatureType given by an XML schema location and the name of the Feature
 * <b>Element</b> whose type is the one needed.
 *
 * <p>Currently only <b>simple</b> FeatureTypes are supported. In the feature, complex schemas may
 * be supported by porting the EmfAppSchemaParser class in the community schema datastore module,
 * depending on the availability of complex {@link Feature} support on the mainstream GeoTools
 * distribution.
 *
 * @author Gabriel Roldan
 * @since 2.5.x
 */
class EmfAppSchemaParser {
    /**
     * Parses the FeatureType pointed out by the {@code schemaLocation} URL and returns it.
     *
     * <p>The returned {@link FeatureType} default geometry, will be the first geometric attribute
     * distinct from {@code gml:location}, or {@code gml:location} if no additional geometric
     * property is found. Note: this code is borrowed and adapted from {@link
     * ParserHandler#startDocument()}
     *
     * @param configuration the WFS configuration for the parser to grab {@link Binding}s from.
     * @param featureName the qualified name of the Feature element in the schema, for which the
     *     feature type is to be parsed.
     * @param crs the CRS to be assigned to the geometric attributes in the parsed feature type.
     *     This information shall be provided here as the schema itself has no knowledge of the CRS
     *     used.
     */
    public static SimpleFeatureType parse(
            Configuration configuration, final QName featureName, CoordinateReferenceSystem crs)
            throws IOException {
        XSDElementDeclaration elementDecl = parseFeatureType(featureName, configuration);
        return parse(configuration, elementDecl, crs);
    }

    /** Use the provided schemaLocation with a GML3 ApplicationSchemaConfiguration */
    public static SimpleFeatureType parse(
            URL schemaLocation, QName featureName, CoordinateReferenceSystem crs)
            throws IOException {
        Configuration configuration;
        // use GML3 application Schema by default
        String namespaceURI = featureName.getNamespaceURI();
        String uri = schemaLocation.toExternalForm();
        configuration = new ApplicationSchemaConfiguration(namespaceURI, uri);

        XSDElementDeclaration elementDecl = parseFeatureType(featureName, configuration);
        return parse(configuration, elementDecl, crs);
    }

    /** Parse the provided element declaration into a SimpleFeatureType. */
    public static SimpleFeatureType parse(
            Configuration configuration,
            XSDElementDeclaration elementDecl,
            CoordinateReferenceSystem crs)
            throws IOException {

        Map<?, ?> bindings = configuration.setupBindings();
        BindingLoader bindingLoader = new BindingLoader(bindings);

        // create the document handler + root context
        // DocumentHandler docHandler =
        // handlerFactory.createDocumentHandler(this);

        MutablePicoContainer context = configuration.setupContext(new DefaultPicoContainer());
        NamespaceSupport namespaces = new NamespaceSupport();
        // setup the namespace support
        context.registerComponentInstance(namespaces);
        context.registerComponentInstance(new NamespaceSupportWrapper(namespaces));

        // binding factory support
        BindingFactory bindingFactory = new BindingFactoryImpl(bindingLoader);
        context.registerComponentInstance(bindingFactory);

        // binding walker support
        BindingWalkerFactoryImpl bwFactory = new BindingWalkerFactoryImpl(bindingLoader, context);
        context.registerComponentInstance(bwFactory);

        try {
            SimpleFeatureType featureType = GML3ParsingUtils.featureType(elementDecl, bwFactory);

            if (crs != null) {
                SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
                builder.setName(featureType.getName());
                builder.setAbstract(featureType.isAbstract());
                builder.setDescription(featureType.getDescription());
                if (featureType.getSuper() instanceof SimpleFeatureType) {
                    builder.setSuperType((SimpleFeatureType) featureType.getSuper());
                }
                List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();
                final GeometryDescriptor defaultGeometry = featureType.getGeometryDescriptor();
                for (AttributeDescriptor descriptor : attributes) {
                    if (descriptor instanceof GeometryDescriptor) {
                        String name = descriptor.getLocalName();
                        Class<?> binding = descriptor.getType().getBinding();
                        builder.add(name, binding, crs);
                    } else {
                        builder.add(descriptor);
                    }
                }
                if (defaultGeometry != null) {
                    builder.setDefaultGeometry(defaultGeometry.getLocalName());
                }
                featureType = builder.buildFeatureType();
            }
            return featureType;
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            String msg = "Error parsing feature type for " + elementDecl.getName();
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    private static XSDElementDeclaration parseFeatureType(
            final QName featureTypeName, Configuration configuration) throws DataSourceException {

        SchemaIndex schemaIndex;
        try {
            schemaIndex = Schemas.findSchemas(configuration);
        } catch (RuntimeException e) {
            throw new DataSourceException("Error parsing feature type for " + featureTypeName, e);
        }

        XSDElementDeclaration elementDeclaration;
        elementDeclaration = schemaIndex.getElementDeclaration(featureTypeName);
        schemaIndex.destroy();
        return elementDeclaration;
    }
}
