/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;

/**
 * FeatureCollection that remaps attribute names using a given map.
 *
 * @author Mauro Bartolomeoli, mbarto@infosia.it
 */
class RemappingFeatureCollection extends DecoratingSimpleFeatureCollection {

    /**
     * Returns a shapefile compatible collection, that is, geometry field is always named the_geom
     * and field names have a max length of 10. Will return the original collection if already
     * compatible
     */
    public static SimpleFeatureCollection getShapefileCompatibleCollection(
            SimpleFeatureCollection fc) {
        Map<String, String> attributeMappings = createAttributeMappings(fc.getSchema());

        // check if the remapping is actually needed
        for (Entry<String, String> entry : attributeMappings.entrySet()) {
            if (!entry.getKey().equals(entry.getValue())) {
                return new RemappingFeatureCollection(fc, attributeMappings);
            }
        }

        // return the original collection otherwise
        return fc;
    }

    /** Maps schema attributes to shapefile-compatible attributes. */
    private static Map<String, String> createAttributeMappings(SimpleFeatureType schema) {
        Map<String, String> result = new HashMap<String, String>();

        // track the field names used and reserve "the_geom" for the geometry
        Set<String> usedFieldNames = new HashSet<String>();
        usedFieldNames.add("the_geom");

        // scan and remap
        for (AttributeDescriptor attDesc : schema.getAttributeDescriptors()) {
            if (attDesc instanceof GeometryDescriptor) {
                result.put(attDesc.getLocalName(), "the_geom");
            } else {
                String name = attDesc.getLocalName();
                result.put(attDesc.getLocalName(), getShapeCompatibleName(usedFieldNames, name));
            }
        }
        return result;
    }

    /** If necessary remaps the name so that it's less than 10 chars long and */
    private static String getShapeCompatibleName(Set<String> usedFieldNames, String name) {
        // 10 chars limit
        if (name.length() > 10) {
            name = name.substring(0, 10);
        }

        // don't use an already assigned name, create a new unique name (it might
        // conflict even if we did not cut it to 10 chars due to remaps of previous long attributes)
        int counter = 0;
        while (usedFieldNames.contains(name)) {
            String postfix = (counter++) + "";
            name = name.substring(0, name.length() - postfix.length()) + postfix;
        }
        usedFieldNames.add(name);

        return name;
    }

    Map<String, String> attributesMapping;

    public RemappingFeatureCollection(
            SimpleFeatureCollection delegate, Map<String, String> attributesMapping) {
        super(delegate);
        this.attributesMapping = attributesMapping;
    }

    public SimpleFeatureType getSchema() {
        return remapSchema(delegate.getSchema());
    }

    /**
     * Builds an inverted version of the given map. Inversion means that key->value becomes
     * value->key
     */
    static Map<String, String> invertMappings(Map<String, String> map) {
        Map<String, String> result = new HashMap<String, String>();
        for (String key : map.keySet()) result.put(map.get(key), key);
        return result;
    }

    /** Gets a new schema, built remapping attribute names via the attributeMappings map. */
    private SimpleFeatureType remapSchema(SimpleFeatureType schema) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(schema.getName());
        for (AttributeDescriptor attDesc : schema.getAttributeDescriptors()) {
            if (attDesc instanceof GeometryDescriptor) {
                GeometryDescriptor geoDesc = (GeometryDescriptor) attDesc;
                builder.add(
                        attributesMapping.get(attDesc.getLocalName()),
                        attDesc.getType().getBinding(),
                        geoDesc.getCoordinateReferenceSystem());
            } else {
                List<Filter> filters = attDesc.getType().getRestrictions();
                if (filters != null && !filters.isEmpty()) {
                    builder.restrictions(filters);
                }
                builder.add(
                        attributesMapping.get(attDesc.getLocalName()),
                        attDesc.getType().getBinding());
            }
        }
        return builder.buildFeatureType();
    }

    public SimpleFeatureIterator features() {
        return new RemappingIterator(delegate.features(), attributesMapping, getSchema());
    }

    /**
     * Remaps a SimpleFeature, using the given mappings (oldname -> mappedname). The builder uses
     * the mapped schema.
     */
    static SimpleFeature remap(
            SimpleFeature source,
            Map<String, String> attributeMappings,
            SimpleFeatureBuilder builder) {
        SimpleFeatureType target = builder.getFeatureType();
        for (int i = 0; i < target.getAttributeCount(); i++) {
            AttributeDescriptor attributeType = target.getDescriptor(i);
            Object value = null;
            String mappedName = attributeMappings.get(attributeType.getLocalName());
            if (source.getFeatureType().getDescriptor(mappedName) != null) {
                value = source.getAttribute(mappedName);
            }

            builder.add(value);
        }

        return builder.buildFeature(source.getIdentifier().getID());
    }

    /** An iterator remapping features as it scrolls through */
    public static class RemappingIterator implements SimpleFeatureIterator {
        Map<String, String> attributesMapping;

        SimpleFeatureIterator delegate;

        SimpleFeatureBuilder builder;

        public RemappingIterator(
                SimpleFeatureIterator delegate, Map attributesMapping, SimpleFeatureType schema) {
            this.delegate = delegate;
            this.attributesMapping = RemappingFeatureCollection.invertMappings(attributesMapping);
            this.builder = new SimpleFeatureBuilder(schema);
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }

        public SimpleFeature next() {
            return RemappingFeatureCollection.remap(delegate.next(), attributesMapping, builder);
        }

        @Override
        public void close() {
            delegate.close();
        }
    }
}
