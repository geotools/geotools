/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

class TransformFeatureStore extends TransformFeatureSource implements SimpleFeatureStore {

    SimpleFeatureStore store;

    Transformer invertedTransformer;

    public TransformFeatureStore(SimpleFeatureStore store, Name name, List<Definition> definitions)
            throws IOException {
        super(store, name, definitions);
        this.store = store;

        // build the inverted definitions
        List<Definition> inverted = new ArrayList<Definition>();
        for (Definition definition : definitions) {
            List<Definition> inverses = definition.inverse();
            if (inverses != null) {
                inverted.addAll(inverses);
            }
        }

        // check it was possible to invert at least one definition
        if (inverted.size() == 0) {
            throw new IllegalArgumentException("None of the expressions could be inverted, cannot "
                    + "create a writable transformer");
        }

        // check we have enough to compute all required fields
        List<String> requiredAttributes = getRequiredAttributes(store.getSchema());
        for (Definition id : inverted) {
            requiredAttributes.remove(id.getName());
        }
        if (requiredAttributes.size() > 0) {
            throw new IllegalArgumentException(
                    "The inverted expressions do not cover some of the required attributes, "
                            + "cannot create a writable transformer. The missing mandatory attributes are: "
                            + requiredAttributes);
        }

        // ah, all is in order, create the inverse transformer then
        invertedTransformer = new Transformer(this, store.getName(), inverted, store.getSchema());
    }

    private List<String> getRequiredAttributes(SimpleFeatureType schema) {
        List<String> result = new ArrayList<String>();
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            if (ad.getMinOccurs() > 0) {
                result.add(ad.getLocalName());
            }
        }

        return result;
    }

    @Override
    public void setTransaction(Transaction transaction) {
        store.setTransaction(transaction);
    }

    @Override
    public Transaction getTransaction() {
        return store.getTransaction();
    }

    @Override
    public void removeFeatures(Filter filter) throws IOException {
        Filter transformed = transformer.transformFilter(filter);
        store.removeFeatures(transformed);
    }

    @Override
    public List<FeatureId> addFeatures(
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection) throws IOException {
        // re-shape back the collection provided, and then just call the wrapper store
        TransformFeatureCollectionWrapper transformed = new TransformFeatureCollectionWrapper(collection, invertedTransformer);
        return store.addFeatures(transformed);

        // TODO: re-shape feature ids...
    }

    @Override
    public void setFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        // transform back the reader and call back the wrapped store
        TransformFeatureReaderWrapper rr = new TransformFeatureReaderWrapper(reader, invertedTransformer);
        store.setFeatures(rr);
    }

    @Override
    public void modifyFeatures(Name[] attributeNames, Object[] attributeValues, Filter filter)
            throws IOException {
        String[] simpleNames = new String[attributeNames.length];
        for (int i = 0; i < attributeNames.length; i++) {
            simpleNames[i] = attributeNames[i].getLocalPart();
        }

        modifyFeatures(simpleNames, attributeValues, filter);
    }

    @Override
    public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
            throws IOException {
        String[] simpleNames = new String[type.length];
        for (int i = 0; i < type.length; i++) {
            simpleNames[i] = type[i].getLocalName();
        }

        modifyFeatures(simpleNames, value, filter);
    }

    @Override
    public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter)
            throws IOException {
        modifyFeatures(new Name[] { attributeName }, new Object[] { attributeValue }, filter);
    }

    @Override
    public void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
            throws IOException {
        modifyFeatures(new AttributeDescriptor[] { type }, new Object[] { value }, filter);
    }

    @Override
    public void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        modifyFeatures(new String[] { name }, new Object[] { attributeValue }, filter);
    }

    @Override
    public void modifyFeatures(String[] nameArray, Object[] attributeValues, Filter filter)
            throws IOException {
        // build a feature out of the provided attribute values
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(getSchema());
        for (int i = 0; i < nameArray.length; i++) {
            fb.set(nameArray[i], attributeValues[i]);
        }
        SimpleFeature sample = fb.buildFeature(null);

        // invert the values
        List<String> names = Arrays.asList(nameArray);
        Map<String, Object> invertedValueMap = new HashMap<String, Object>();
        for (Definition definition : transformer.getDefinitions()) {
            if(names.contains(definition.getName())) { 
                List<Definition> ids = definition.inverse();
                if(ids != null) {
                    for (Definition id : ids) {
                        Object value = id.getExpression().evaluate(sample);
                        invertedValueMap.put(id.getName(), value);
                    }
                }
            }
        }
        
        // if we have any inverted value, call through
        if(!invertedValueMap.isEmpty()) {
            String[] invertedNames = new String[invertedValueMap.size()];
            Object[] invertedValues = new Object[invertedValueMap.size()];
            int i = 0;
            for (String name : invertedValueMap.keySet()) {
                invertedNames[i] = name;
                invertedValues[i] = invertedValueMap.get(name);
                i++;
            }
            Filter txFilter = transformer.transformFilter(filter);
            store.modifyFeatures(invertedNames, invertedValues, txFilter);
        }
    }

}
