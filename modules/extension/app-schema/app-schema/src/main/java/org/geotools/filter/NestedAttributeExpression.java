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
package org.geotools.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPath.Step;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.xlink.XLINK;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

/**
 * This class represents a list of expressions broken up from a single XPath expression that is
 * nested in more than one feature. The purpose is to allow filtering these attributes on the parent
 * feature.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/filter/NestedAttributeExpression.java $
 */
public class NestedAttributeExpression extends AttributeExpressionImpl {
    private FeatureTypeMapping mappings;

    private StepList fullSteps;
    
    /**
     * Used for polymorphism. Would be null if mappings is known. Otherwise used to determine the
     * FeatureTypeMapping to use, depending on the function evaluation upon a feature.
     */
//    private Expression function;

    /**
     * First constructor
     *
     * @param xpath
     *            Attribute XPath
     * @param expressions
     *            List of broken up expressions
     */
    public NestedAttributeExpression(String xpath, FeatureTypeMapping mappings) {
        super(xpath);
        this.mappings = mappings;
        fullSteps = XPath.steps(mappings.getTargetFeature(), this.attPath.toString(), mappings
                .getNamespaces());
    }
    
    public NestedAttributeExpression(StepList xpath, FeatureTypeMapping mappings) {
        super(xpath.toString());
        this.mappings = mappings;
        fullSteps = xpath;
    }
    
    public NestedAttributeExpression(Expression expression, FeatureTypeMapping mappings) {
        super(expression.toString());
        this.mappings = mappings;
        fullSteps = XPath.steps(mappings.getTargetFeature(), this.attPath.toString(), mappings
                .getNamespaces());
    }
    
//    private boolean isConditional() {
//        return function != null;
//    }

    /**
     * see {@link org.geotools.filter.AttributeExpressionImpl#evaluate(Object)}
     */
    @Override
    public Object evaluate(Object object) {
        if (object == null) {
            return null;
        }

        // only simple/complex features are supported
        if (!(object instanceof Feature)) {
            throw new UnsupportedOperationException(
                    "Expecting a feature to apply filter, but found: " + object);
        }

        // if (object instanceof FeatureImpl) {
        // AttributeExpressionImpl exp = new AttributeExpressionImpl(attPath, new Hints(
        // FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, namespaces));
        // List<Object> values = new ArrayList<Object>(1);
        // values.add(exp.evaluate(object));
        // return values;
        // }

        List<Feature> roots = new ArrayList<Feature>();
        roots.add((Feature) object);
        
//        if (isConditional()) {
//            fullSteps = XPath.steps(((Feature) object).getDescriptor(), this.attPath, this.namespaceSupport);            
//            String fTypeString = function.evaluate(object, String.class);
//            if (fTypeString != null) {
//                Name fTypeName = Types.degloseName(fTypeString, this.namespaceSupport);
//                try {
//                    FeatureSource<FeatureType, Feature> fSource = DataAccessRegistry.getFeatureSource(fTypeName);
//                    if (fSource != null && fSource instanceof MappingFeatureSource) {
//                        mappings = ((MappingFeatureSource) fSource).getMapping();
//                    } else {
//                        return null;
//                    }
//                } catch (IOException e) {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        }

        return getValues(0, 0, roots, mappings, null);
    }

    private boolean isLastStep(int index) {
        return index >= fullSteps.size();
    }

    private List<Object> getValues(int startIndex, int endIndex, List<Feature> roots,
            FeatureTypeMapping fMapping, AttributeMapping prevMapping) {
        List<Object> values = new ArrayList<Object>();

        if (startIndex > fullSteps.size() || endIndex > fullSteps.size()) {
            return values;
        }

        while (startIndex <= endIndex) {
            List<AttributeMapping> attMappings = new ArrayList<AttributeMapping>();
            StepList steps = null;
            if (isLastStep(endIndex)) {
                // exhausted all paths
                return values;
            }

            while (attMappings.isEmpty() && endIndex < fullSteps.size()) {
                endIndex++;
                steps = fullSteps.subList(startIndex, endIndex);
                attMappings = fMapping.getAttributeMappingsIgnoreIndex(steps);

                if (steps.size() == 1) {
                    if (Types.equals(fMapping.getTargetFeature().getName(), steps.get(0).getName())
                            && !(Types.isSimpleContentType(fMapping.getTargetFeature().getType()))) {
                        // skip element type name, but not when it's a simple content
                        // like gml:name because it wouldn't have the element type name in the xpath        
                        startIndex++;
                        endIndex = startIndex;      
                        steps = fullSteps.subList(startIndex, endIndex);
                        attMappings = fMapping.getAttributeMappingsIgnoreIndex(steps);
                        continue;
                    } else if (attMappings.isEmpty() && steps.get(0).isId()) {
                        // sometimes there's no explicit attribute mapping for top element name
                        // but id should still resolve to primary key by default
                        // e.g. gsml:GeologicUnit/@gml:id should resolve even though there's no
                        // AttributeMapping for gsml:GeologicUnit 
                        setIdValues(null, roots, values);                         
                        return values;
                    }
                }
            }

            if (attMappings.isEmpty()) {                
                // not found here, but might be found in other nodes if multi-valued
                // and polymorphic
                continue;
            }   

            for (AttributeMapping mapping : attMappings) {
                if (mapping instanceof NestedAttributeMapping) {                    
                    if (isClientProperty(endIndex)) {
                        // check for client properties
                        boolean isNestedXlinkHref = isXlinkHref(mapping);
                        boolean valueFound = false;
                        if (!isNestedXlinkHref) {
                            // check if client properties are set in the parent attributeMapping in root mapping file
                            valueFound = getClientProperties(mapping, values, roots);
                        }
                        if (!valueFound) {
                            // or if they're set in the attributeMapping in feature chained mapping file
                            getNestedClientProperties((NestedAttributeMapping) mapping, roots,
                                    values, isNestedXlinkHref);
                        }
                    } else {
                        boolean isSimpleContent = Types.isSimpleContent(steps, fMapping
                                .getTargetFeature().getType());
                        // if simple content, then it doesn't need to increment the next starting
                        // index
                        // since there will be no type name in the xpath, e.g. when gml:name is
                        // feature
                        // chained
                        // the path stays as gml:name.. but if it's a complex type with complex
                        // content,
                        // e.g. gsml:specification
                        // the path will be gsml:specification/gsml:GeologicUnit/<some leaf
                        // attribute to
                        // filter by>
                        getNestedValues((NestedAttributeMapping) mapping, roots, values,
                                isSimpleContent ? startIndex : startIndex + 1);
                    }
                } else {
                    // normal attribute mapping
                    if (endIndex == fullSteps.size()) {
                        Expression exp = mapping.getSourceExpression();
                        for (Feature f : roots) {
                            Object value = getValue(exp, f);
                            if (value != null) {
                                values.add(value);
                            }
                        }
                    } else if (isClientProperty(endIndex)) {
                        // peek at the next attribute to check for client properties     
                        if (getLastStep().isId()) {
                            setIdValues(mapping, roots, values);
                        } else {
                            getClientProperties(mapping, values, roots);       
                        }
                    } else {
                        // increment the xpath
                        List<Object> nestedValues = getValues(startIndex, endIndex, roots,
                                fMapping, mapping);
                        if (nestedValues != null) {
                            values.addAll(nestedValues);
                        }
                    }
                }
            }

            return values;
        }
        return values;
    }
    
    private boolean isXlinkHref(AttributeMapping mapping) {
        if (fullSteps.get(fullSteps.size() - 1).getName().equals(XLINK.HREF)) {
            // special case for xlink:href by feature chaining
            // must get the value from the nested attribute mapping instead, i.e. from another table
            // if it's to get the values from the local table, it shouldn't be set with feature chaining
            return true;
        }
        return false;
    }

    private void getNestedValues(NestedAttributeMapping mapping, List<Feature> features,  List<Object> values, int nextIndex) {
        FeatureTypeMapping nextFMapping = null;
        for (Feature f : features) {
            try {
                nextFMapping = mapping.getFeatureTypeMapping(f);
            } catch (IOException e) {
                nextFMapping = null;
            }
            if (nextFMapping != null && mapping.isSameSource()) {
                // same root/database row, different mappings, used in
                // polymorphism
                List<Feature> nestedRoots = new ArrayList<Feature>(1);
                nestedRoots.add(f);
                List<Object> nestedValues = getValues(nextIndex, nextIndex, nestedRoots,
                            nextFMapping, mapping);
                
                if (nestedValues != null) {
                    values.addAll(nestedValues);
                }
                continue;
            }
            try {
                List<Feature> nestedFeatures = getNestedFeatures(f,
                        mapping, nextFMapping);
                if (nestedFeatures == null || nestedFeatures.isEmpty()) {
                    continue;
                }

                if (nextFMapping != null) {
                    List<Object> nestedValues = getValues(nextIndex, nextIndex, nestedFeatures,
                            nextFMapping, mapping);
                    if (nestedValues != null) {
                        values.addAll(nestedValues);
                    }
                } else if (!nestedFeatures.isEmpty()) {
                    throw new UnsupportedOperationException(
                            "FeatureTypeMapping not found for "
                                    + attPath
                                    + ". This shouldn't happen if it's set in AppSchemaDataAccess mapping file!");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed evaluating filter expression: '"
                        + attPath + "'. Caused by: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // might be a polymorphic case where it's looking for an attribute
                // from another type
                // that doesn't match this, but might match another database row
                // so just continue
                continue;
            }
        }
    }
    
    private void getNestedClientProperties(NestedAttributeMapping mapping, List<Feature> features,
            List<Object> values, boolean isXlinkHref) {
        FeatureTypeMapping nextFMapping = null;
        for (Feature f : features) {
            try {
                nextFMapping = mapping.getFeatureTypeMapping(f);
                if (nextFMapping != null) {
                    List<Feature> nestedFeatures;
                    nestedFeatures = getNestedFeatures(f, mapping, nextFMapping);
                    if (nestedFeatures == null || nestedFeatures.isEmpty()) {
                        continue;
                    }
                    if (isXlinkHref) {
                        // xlink:href mapping done in the root mapping file
                        // there is no need to find attributeMapping in the nested feature type mapping
                        getClientProperties(mapping, values, nestedFeatures);
                    } else {
                        List<AttributeMapping> nestedAttMappings = nextFMapping
                            .getAttributeMappingsIgnoreIndex(mapping.getTargetXPath());
                        AttributeMapping attMapping = null;
                        boolean found = false;
                        if (!nestedAttMappings.isEmpty()) {
                            attMapping = nestedAttMappings.get(0);
                            found = getClientProperties(attMapping, values, nestedFeatures);
                        }
                        if (!found && getLastStep().isId()) {
                            setIdValues(attMapping, nestedFeatures, values);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed evaluating filter expression: '" + attPath
                        + "'. Caused by: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // might be a polymorphic case where it's looking for an attribute
                // from another type
                // that doesn't match this, but might match another database row
                // so just continue
                continue;
            }
        }

    }
    
    private boolean isClientProperty(int endIndex) {
        if (endIndex == fullSteps.size() - 1) {
            return fullSteps.get(endIndex).isXmlAttribute();
        }
        return false;
    }
    
    private boolean getClientProperties(AttributeMapping attMapping, List<Object> values, List<Feature> features) {
        boolean expressionFound = false;
        Step lastStep = getLastStep();        
        Expression exp = getClientPropertyExpression(attMapping, lastStep);
        if (exp != null) {
            for (Feature f : features) {
                Object value = getValue(exp, f);
                if (value != null) {
                    values.add(value);
                }
            }
            expressionFound = true;            
        }
        return expressionFound;
    }
    
    private Step getLastStep() {
        return fullSteps.get(fullSteps.size() - 1);
    }

    /**
     * Get nested features from a feature chaining attribute mapping
     *
     * @param root
     *            Root feature being evaluated
     * @param nestedMapping
     *            Attribute mapping for nested features
     * @param fMapping
     *            The root feature type mapping
     * @return list of nested features
     * @throws IOException
     */
    private List<Feature> getNestedFeatures(Feature root, NestedAttributeMapping nestedMapping,
            FeatureTypeMapping fMapping) throws IOException {
        Object fTypeName = nestedMapping.getNestedFeatureType(root);
        if (fTypeName == null || !(fTypeName instanceof Name)) {
            return null;
        }
        boolean hasSimpleFeatures = AppSchemaDataAccessRegistry.hasName((Name) fTypeName);
        // get foreign key
        Object val = getValue(nestedMapping.getSourceExpression(), root);
        if (val == null) {
            return null;
        }
        if (hasSimpleFeatures) {
            // normal app-schema mapping
            return nestedMapping.getInputFeatures(val, fMapping);
        } else {
            // app-schema with a complex feature source
            return nestedMapping.getFeatures(val, null, root);
        }
    }

    private Object getValue(Expression expression, Feature feature) {
        Object value = expression.evaluate(feature);

        return extractAttributeValue(value);
    }

    /**
     * Extract the value that might be wrapped in an attribute. If the value is a collection, gets
     * the first value.
     *
     * @param value
     * @return
     */
    private Object extractAttributeValue(Object value) {
        if (value == null) {
            return null;
        }

        while (value instanceof Attribute) {
            // get real value
            value = ((Attribute) value).getValue();
        }
        if (value == null) {
            return null;
        }
        if (value instanceof Collection) {
            if (((Collection) value).isEmpty()) {
                return null;
            }
            value = ((Collection) value).iterator().next();
            while (value instanceof Attribute) {
                value = ((Attribute) value).getValue();
            }
        }
        return value;
    }
    
    private void setIdValues(AttributeMapping mapping, List<Feature> features, List<Object> values) {
        Expression exp = null;
        if (mapping == null || mapping.getIdentifierExpression() == Expression.NIL) {
            // no specific attribute mapping or that idExpression is not mapped
            // use primary key
            exp = CommonFactoryFinder.getFilterFactory(null).property("@id");
        } else {
            exp = mapping.getIdentifierExpression();
        }    
        if (exp != null) {
            for (Feature f : features) {
                Object value = getValue(exp, f);
                if (value != null) {
                    values.add(value);
                }
            }  
        }
    }

    /**
     * Find the source expression if the step is a client property.
     *
     * @param nextRootStep
     *            the step
     * @param fMapping
     *            feature type mapping to get namespaces from
     * @param mapping
     *            attribute mapping
     * @param targetXPath
     *            the full target xpath
     * @return
     */
    private Expression getClientPropertyExpression(AttributeMapping mapping, Step lastStep) {
        if (lastStep.isXmlAttribute()) {
            Map<Name, Expression> clientProperties = mapping.getClientProperties();
            QName lastStepQName = lastStep.getName();
            Name lastStepName;
            if (lastStepQName.getPrefix() != null
                    && lastStepQName.getPrefix().length() > 0
                    && (lastStepQName.getNamespaceURI() == null || lastStepQName.getNamespaceURI()
                            .length() == 0)) {
                String prefix = lastStepQName.getPrefix();
                String uri = namespaceSupport.getURI(prefix);
                lastStepName = Types.typeName(uri, lastStepQName.getLocalPart());
            } else {
                lastStepName = Types.toTypeName(lastStepQName);
            }
            if (clientProperties.containsKey(lastStepName)) {
                // end NC - added
                return (Expression) clientProperties.get(lastStepName);
            }
        }
        return null;
    }
}
