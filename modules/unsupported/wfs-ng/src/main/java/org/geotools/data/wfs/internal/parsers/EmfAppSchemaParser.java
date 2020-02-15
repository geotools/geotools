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
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml3.ApplicationSchemaConfiguration;
import org.geotools.gml3.GML;
import org.geotools.gml3.bindings.GML3ParsingUtils;
import org.geotools.util.logging.Logging;
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
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
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
public class EmfAppSchemaParser {

    private static final Logger LOGGER = Logging.getLogger(EmfAppSchemaParser.class);

    public static SimpleFeatureType parseSimpleFeatureType(
            final QName featureName,
            final URL schemaLocation,
            final CoordinateReferenceSystem crs,
            final Configuration wfsConfiguration,
            final Map<QName, Class<?>> mappedBindings)
            throws IOException {
        return parseSimpleFeatureType(
                wfsConfiguration, featureName, schemaLocation, crs, mappedBindings);
    }

    /**
     * Parses the FeatureType pointed out by the {@code schemaLocation} URL and returns a subset
     * consisting only of the simple attributes found on the original schema.
     *
     * <p>Aditionally, the default properties inherited from {@code gml:AbstractFeatureType} (ie,
     * gml:name, gml:location, etc), will be ignored.
     *
     * <p>The returned {@link SimpleFeatureType} default geometry, thus, will be the first geometric
     * attribute distinct from {@code gml:location}. Note: this code is borrowed and adapted from
     * {@link ParserHandler#startDocument()}
     *
     * @param wfsConfiguration the WFS configuration for the parser to grab {@link Binding}s from.
     * @param featureName the qualified name of the Feature element in the schema, for which the
     *     feature type is to be parsed.
     * @param schemaLocation the location of the root schema file from where to parse the feature
     *     type.
     * @param crs the CRS to be assigned to the geometric attributes in the parsed feature type.
     *     This information shall be provided here as the schema itself has no knowledge of the CRS
     *     used.
     */
    public static SimpleFeatureType parseSimpleFeatureType(
            final Configuration wfsConfiguration,
            final QName featureName,
            final URL schemaLocation,
            final CoordinateReferenceSystem crs,
            final Map<QName, Class<?>> mappedBindings)
            throws IOException {
        final SimpleFeatureType realType =
                parse(wfsConfiguration, featureName, schemaLocation, crs, mappedBindings);
        SimpleFeatureType subsetType = toSimpleFeatureType(realType);
        return subsetType;
    }

    /**
     * Go through FeatureType description and convert to a SimpleFeatureType. Also ignores
     * AbstractFeatureType contributions such as name etc...
     */
    public static SimpleFeatureType toSimpleFeatureType(final FeatureType realType)
            throws DataSourceException {
        List<PropertyDescriptor> attributes;
        Collection<PropertyDescriptor> descriptors = realType.getDescriptors();
        attributes = new ArrayList<PropertyDescriptor>(descriptors);
        List<String> simpleProperties = new ArrayList<String>();

        // HACK HACK!! the parser sets no namespace to the properties so we're
        // doing a hardcode property name black list
        final Set<String> ignoreList =
                new HashSet<String>(
                        Arrays.asList(
                                new String[] {
                                    GML.location.getLocalPart(),
                                            GML.metaDataProperty.getLocalPart(),
                                    GML.description.getLocalPart(), GML.name.getLocalPart(),
                                    GML.boundedBy.getLocalPart()
                                }));

        if (attributes.size() > ignoreList.size()) {
            Set<String> firstAtts = new HashSet<String>();
            for (int i = 0; i < ignoreList.size(); i++) {
                firstAtts.add(attributes.get(i).getName().getLocalPart());
            }
            if (ignoreList.equals(firstAtts)) {
                attributes = attributes.subList(ignoreList.size(), attributes.size());
            }
        }
        // / HACK END

        for (PropertyDescriptor descriptor : attributes) {
            Class<?> binding = descriptor.getType().getBinding();
            int maxOccurs = descriptor.getMaxOccurs();
            Name name = descriptor.getName();
            if (GML.NAMESPACE.equals(name.getNamespaceURI())
                    || maxOccurs > 1
                    || Object.class.equals(binding)) {
                LOGGER.fine(
                        "Ignoring multivalued or complex property "
                                + name
                                + " on feature type "
                                + realType.getName());
                continue;
            }

            simpleProperties.add(((AttributeDescriptor) descriptor).getLocalName());
        }

        String[] properties = simpleProperties.toArray(new String[simpleProperties.size()]);
        SimpleFeatureType subsetType;
        try {
            // TODO: will need to handle FeatureType instead of direct casting
            // to SimpleFeatureType
            // once FeatureType support lands on trunk
            subsetType = DataUtilities.createSubType((SimpleFeatureType) realType, properties);
        } catch (SchemaException e) {
            throw new DataSourceException(e);
        }
        return subsetType;
    }

    /**
     * Parses the FeatureType pointed out by the {@code schemaLocation} URL and returns it.
     *
     * <p>The returned {@link FeatureType} default geometry, will be the first geometric attribute
     * distinct from {@code gml:location}, or {@code gml:location} if no additional geometric
     * property is found. Note: this code is borrowed and adapted from {@link
     * ParserHandler#startDocument()}
     *
     * @param wfsConfiguration the WFS configuration for the parser to grab {@link Binding}s from.
     * @param featureName the qualified name of the Feature element in the schema, for which the
     *     feature type is to be parsed.
     * @param schemaLocation the location of the root schema file from where to parse the feature
     *     type.
     * @param crs the CRS to be assigned to the geometric attributes in the parsed feature type.
     *     This information shall be provided here as the schema itself has no knowledge of the CRS
     *     used.
     */
    public static SimpleFeatureType parse(
            final Configuration wfsConfiguration,
            final QName featureName,
            final URL schemaLocation,
            final CoordinateReferenceSystem crs,
            final Map<QName, Class<?>> mappedBindings)
            throws IOException {
        XSDElementDeclaration elementDecl = parseFeatureType(featureName, schemaLocation);

        Map bindings = wfsConfiguration.setupBindings();
        if (mappedBindings != null) {
            bindings.putAll(mappedBindings);
        }
        BindingLoader bindingLoader = new BindingLoader(bindings);

        // create the document handler + root context
        // DocumentHandler docHandler =
        // handlerFactory.createDocumentHandler(this);

        MutablePicoContainer context = wfsConfiguration.setupContext(new DefaultPicoContainer());
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
                        Class binding = descriptor.getType().getBinding();
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
            String msg =
                    "Error parsing feature type for "
                            + featureName
                            + " from "
                            + schemaLocation.toExternalForm();
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    /** TODO: add connectionfactory parameter to handle authentication, gzip, etc */
    private static XSDElementDeclaration parseFeatureType(
            final QName featureTypeName, final URL schemaLocation) throws DataSourceException {
        ApplicationSchemaConfiguration configuration;
        {
            String namespaceURI = featureTypeName.getNamespaceURI();
            String uri = schemaLocation.toExternalForm();
            configuration = new ApplicationSchemaConfiguration(namespaceURI, uri);
        }
        SchemaIndex schemaIndex;
        try {
            schemaIndex = Schemas.findSchemas(configuration);
        } catch (RuntimeException e) {
            throw new DataSourceException("Error parsing feature type for " + featureTypeName, e);
        }

        XSDElementDeclaration elementDeclaration;
        elementDeclaration = schemaIndex.getElementDeclaration(featureTypeName);
        schemaIndex.destroy();
        if (elementDeclaration == null) {
            throw new DataSourceException("No XSDElementDeclaration found for " + featureTypeName);
        }
        return elementDeclaration;
    }

    public static SimpleFeatureType parseSimpleFeatureType(
            Configuration configuration,
            QName featureTypeName,
            URL schemaLocation,
            CoordinateReferenceSystem crs)
            throws IOException {
        return parseSimpleFeatureType(configuration, featureTypeName, schemaLocation, crs, null);
    }
}
