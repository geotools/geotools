/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import static org.geotools.data.complex.AbstractMappingFeatureIterator.MULTI_VALUE_TYPE;
import static org.geotools.data.complex.AbstractMappingFeatureIterator.UNBOUNDED_MULTI_VALUE;
import static org.geotools.data.complex.util.ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.feature.type.PropertyType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.appschema.feature.AppSchemaAttributeBuilder;
import org.geotools.data.complex.AbstractMappingFeatureIterator;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.data.complex.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.data.complex.util.XPathUtil;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.PropertyImpl;
import org.geotools.feature.ValidatingFeatureFactoryImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.gml3.GML;
import org.geotools.xs.XSSchema;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.Attributes;

/**
 * Utility class to evaluate XPath expressions against an Attribute instance, which may be any Attribute, whether it is
 * simple, complex, a feature, etc.
 *
 * <p>At the difference of the Filter subsystem, which works against Attribute contents (for example to evaluate a
 * comparison filter), the XPath subsystem, for which this class is the single entry point, works against Attribute
 * instances. That is, the result of an XPath expression, if a single value, is an Attribute, not the attribute content,
 * or a List of Attributes, for instance.
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @since 2.4
 */
public class XPath extends XPathUtil {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(XPath.class);

    private FilterFactory FF;

    private FeatureFactory featureFactory;

    private CoordinateReferenceSystem crs;

    /**
     * Used to create specific attribute descriptors for {@link #set(Attribute, String, Object, String, AttributeType)}
     * when the actual attribute instance is of a derived type of the corresponding one declared in the feature type.
     */
    private FeatureTypeFactory descriptorFactory;

    public XPath() {
        this.FF = CommonFactoryFinder.getFilterFactory(null);
        this.featureFactory = new ValidatingFeatureFactoryImpl();
        this.descriptorFactory = new UniqueNameFeatureTypeFactoryImpl();
    }

    public XPath(FilterFactory ff, FeatureFactory featureFactory) {
        setFilterFactory(ff);
        setFeatureFactory(featureFactory);
        // this.descriptorFactory = new TypeFactoryImpl();
    }

    public void setFilterFactory(FilterFactory ff) {
        this.FF = ff;
    }

    public void setCRS(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    public void setFeatureFactory(FeatureFactory featureFactory) {
        this.featureFactory = featureFactory;
    }

    /**
     * Sets the value of the attribute of <code>att</code> addressed by <code>xpath</code> and of type <code>
     * targetNodeType</code> to be <code>value</code> with id <code>id</code>.
     *
     * @param att the root attribute for which to set the child attribute value
     * @param xpath the xpath expression that addresses the <code>att</code> child whose value is to be set
     * @param value the value of the attribute addressed by <code>xpath</code>
     * @param id the identifier of the attribute addressed by <code>xpath</code>, might be <code>
     *     null</code>
     * @param targetNodeType the expected type of the attribute addressed by <code>xpath</code>, or <code>null</code> if
     *     unknown
     * @param isXlinkRef true if the attribute would only contain xlink:href client property
     */
    public Attribute set(
            final Attribute att,
            final StepList xpath,
            Object value,
            String id,
            AttributeType targetNodeType,
            boolean isXlinkRef,
            Expression sourceExpression) {
        return set(att, xpath, value, id, targetNodeType, isXlinkRef, null, sourceExpression);
    }

    public Attribute set(
            final Attribute att,
            final StepList xpath,
            Object value,
            String id,
            AttributeType targetNodeType,
            boolean isXlinkRef,
            AttributeDescriptor targetDescriptor,
            Expression sourceExpression) {
        if (LOGGER.isLoggable(Level.CONFIG)) {
            LOGGER.entering("XPath", "set", new Object[] {att, xpath, value, id, targetNodeType});
        }

        final StepList steps = new StepList(xpath);

        Attribute parent = att;
        Name rootName = null;
        AttributeDescriptor parentDescriptor = parent.getDescriptor();
        if (parentDescriptor != null) {
            rootName = parentDescriptor.getName();
            Step rootStep = steps.get(0);
            QName stepName = rootStep.getName();
            if (Types.equals(rootName, stepName)) {
                // first step is the self reference to att, so skip it
                if (steps.size() > 1) {
                    steps.remove(0);
                } else {
                    // except when the xpath is the root itself
                    // where it is done for feature chaining for simple content
                    if (Types.isSimpleContentType(parent.getType()) || Types.canHaveTextContent(parent.getType())) {
                        return setSimpleContentValue(parent, value);
                    } else if (Types.isGeometryType(parent.getType())) {
                        ComplexFeatureTypeFactoryImpl typeFactory = new ComplexFeatureTypeFactoryImpl();
                        GeometryType geomType;
                        if (parent.getType() instanceof GeometryType) {
                            geomType = (GeometryType) parent.getType();
                        } else {
                            geomType = (GeometryType) ((NonFeatureTypeProxy) parent.getType()).getSubject();
                        }
                        GeometryDescriptor geomDescriptor = typeFactory.createGeometryDescriptor(
                                geomType,
                                rootName,
                                parentDescriptor.getMinOccurs(),
                                parentDescriptor.getMaxOccurs(),
                                parentDescriptor.isNillable(),
                                parentDescriptor.getDefaultValue());
                        GeometryAttributeImpl geom = new GeometryAttributeImpl(value, geomDescriptor, null);
                        parent.setValue(List.of(geom));
                        return geom;
                    }
                }
            }
        }

        Iterator<Step> stepsIterator = steps.iterator();

        while (stepsIterator.hasNext()) {
            final XPath.Step currStep = stepsIterator.next();
            AttributeDescriptor currStepDescriptor = null;
            final boolean isLastStep = !stepsIterator.hasNext();
            final QName stepName = currStep.getName();
            final Name attributeName = org.geotools.feature.type.Types.toTypeName(stepName);

            final AttributeType _parentType = parent.getType();
            if (_parentType.getName().equals(XSSchema.ANYTYPE_TYPE.getName()) && targetDescriptor != null) {
                // this needs to be passed on if casting anyType to something else, since it won't
                // exist in the schema
                currStepDescriptor = targetDescriptor;
            } else {
                ComplexType parentType = (ComplexType) _parentType;

                if (!isLastStep || targetNodeType == null) {
                    if (null == attributeName.getNamespaceURI()) {
                        currStepDescriptor =
                                (AttributeDescriptor) Types.findDescriptor(parentType, attributeName.getLocalPart());
                    } else {
                        currStepDescriptor = (AttributeDescriptor) Types.findDescriptor(parentType, attributeName);
                    }

                    if (currStepDescriptor == null) {
                        // need to take the non easy way, may be the instance has a
                        // value for this step with a different name, of a derived
                        // type of the one declared in the parent type
                        String prefixedStepName = currStep.toString();
                        PropertyName name = FF.property(prefixedStepName);
                        Attribute child = (Attribute) name.evaluate(parent);
                        if (child != null) {
                            currStepDescriptor = child.getDescriptor();
                        }
                    }
                } else {
                    AttributeDescriptor actualDescriptor;
                    if (null == attributeName.getNamespaceURI()) {
                        actualDescriptor =
                                (AttributeDescriptor) Types.findDescriptor(parentType, attributeName.getLocalPart());
                    } else {
                        actualDescriptor = (AttributeDescriptor) Types.findDescriptor(parentType, attributeName);
                    }

                    if (actualDescriptor != null) {
                        int minOccurs = actualDescriptor.getMinOccurs();
                        int maxOccurs = actualDescriptor.getMaxOccurs();
                        boolean nillable = actualDescriptor.isNillable();
                        if (actualDescriptor instanceof GeometryDescriptor) {
                            // important to maintain CRS information encoding
                            if (Geometry.class.isAssignableFrom(targetNodeType.getBinding())) {
                                if (!(targetNodeType instanceof GeometryType)) {
                                    targetNodeType = new GeometryTypeImpl(
                                            targetNodeType.getName(),
                                            targetNodeType.getBinding(),
                                            ((GeometryDescriptor) actualDescriptor).getCoordinateReferenceSystem(),
                                            targetNodeType.isIdentified(),
                                            targetNodeType.isAbstract(),
                                            targetNodeType.getRestrictions(),
                                            targetNodeType.getSuper(),
                                            targetNodeType.getDescription());
                                }
                                currStepDescriptor = descriptorFactory.createGeometryDescriptor(
                                        (GeometryType) targetNodeType,
                                        attributeName,
                                        minOccurs,
                                        maxOccurs,
                                        nillable,
                                        null);
                            } else {
                                throw new IllegalArgumentException("Can't set targetNodeType: "
                                        + targetNodeType.toString()
                                        + " for attribute mapping: "
                                        + attributeName
                                        + " as it is not a Geometry type!");
                            }
                        } else {
                            currStepDescriptor = descriptorFactory.createAttributeDescriptor(
                                    targetNodeType, attributeName, minOccurs, maxOccurs, nillable, null);
                        }
                    }
                }

                if (currStepDescriptor == null) {
                    StringBuffer parentAtts = new StringBuffer();
                    Collection<PropertyDescriptor> properties = parentType.getDescriptors();
                    for (Iterator<PropertyDescriptor> it = properties.iterator(); it.hasNext(); ) {
                        PropertyDescriptor desc = it.next();
                        Name name = desc.getName();
                        parentAtts.append(name.getNamespaceURI());
                        parentAtts.append("#");
                        parentAtts.append(name.getLocalPart());
                        if (it.hasNext()) {
                            parentAtts.append(", ");
                        }
                    }
                    throw new IllegalArgumentException(currStep
                            + " is not a valid location path for type "
                            + parentType.getName()
                            + ". "
                            + currStep
                            + " ns: "
                            + currStep.getName().getNamespaceURI()
                            + ", "
                            + parentType.getName().getLocalPart()
                            + " properties: "
                            + parentAtts);
                }
            }

            if (isLastStep) {
                // reached the leaf, currStepDescriptor is guaranteed to be non-null
                assert currStepDescriptor != null;

                return setLeafAttribute(currStepDescriptor, currStep, id, value, parent, targetNodeType, isXlinkRef);
            } else {
                // parent = appendComplexProperty(parent, currStep,
                // currStepDescriptor);
                int index = currStep.isIndexed() ? currStep.getIndex() : -1;
                parent = setValue(currStepDescriptor, null, List.of(), index, parent, null, false);
            }
        }
        throw new IllegalStateException();
    }

    /**
     * Set a simple content value for an attribute.
     *
     * @param attribute Attribute of simple content type.
     * @param value Value for the simple content.
     * @return The attribute with simple content type.
     */
    private Attribute setSimpleContentValue(Attribute attribute, Object value) {
        Property simpleContent = null;
        if (attribute instanceof ComplexAttribute) {
            simpleContent = ((ComplexAttribute) attribute).getProperty(ComplexFeatureConstants.SIMPLE_CONTENT);
        }
        if (simpleContent == null) {
            simpleContent = buildSimpleContent(attribute.getType(), value);
            Collection<Property> contents = List.of(simpleContent);
            Attribute nestedAtt =
                    new ComplexAttributeImpl(contents, attribute.getDescriptor(), attribute.getIdentifier());
            List<Attribute> nestedAttContents = List.of(nestedAtt);
            attribute.setValue(nestedAttContents);

            return nestedAtt;
        } else {
            PropertyType simpleContentType = getSimpleContentType((AttributeType) simpleContent.getType());
            Object convertedValue = FF.literal(value).evaluate(value, simpleContentType.getBinding());
            simpleContent.setValue(convertedValue);
            return attribute;
        }
    }

    private Attribute setLeafAttribute(
            AttributeDescriptor currStepDescriptor,
            Step currStep,
            String id,
            Object value,
            Attribute parent,
            AttributeType targetNodeType,
            boolean isXlinkRef) {
        int index = currStep.isIndexed() ? currStep.getIndex() : -1;
        Attribute attribute = setValue(currStepDescriptor, id, value, index, parent, targetNodeType, isXlinkRef);
        return attribute;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Attribute setValue(
            final AttributeDescriptor descriptor,
            final String id,
            final Object value,
            final int index,
            final Attribute parent,
            final AttributeType targetNodeType,
            boolean isXlinkRef) {

        Object convertedValue = null;
        Map<Object, Object> simpleContentProperties = null;
        if (isFeatureChainedSimpleContent(descriptor, value)) {
            List<Property> nestedPropList = getSimpleContentList(value);
            if (!nestedPropList.isEmpty()) {
                Property nestedProp = nestedPropList.iterator().next();
                if (Types.isGeometryType(descriptor.getType())
                        || nestedProp.getName().equals(descriptor.getName())) {
                    convertedValue = nestedProp.getValue();
                } else {
                    convertedValue = nestedPropList;
                }
                simpleContentProperties = nestedProp.getUserData();
            }
        } else {
            // adapt value to context
            convertedValue = convertValue(descriptor, value);
        }

        final Name attributeName = descriptor.getName();

        Attribute leafAttribute = (parent instanceof ComplexAttribute)
                ? findLeafAttribute((ComplexAttribute) parent, attributeName, index, isXlinkRef, convertedValue)
                : null;

        // Build a new leaf if either:
        // (1) have no leaf (leafAttribute == null), or
        // (2) maxOccurs is greater than one and existing leaf already has xlink:href, in which
        // case, as insufficient information at this point to evaluate xlink:href expressions and
        // remove duplicates, assume building multivalued xlink:href ClientProperty.
        if (leafAttribute == null
                || (descriptor.getMaxOccurs() > 1
                        && leafAttribute.getUserData().containsKey(Attributes.class)
                        && ((Map<Object, Object>) leafAttribute.getUserData().get(Attributes.class))
                                .containsKey(AbstractMappingFeatureIterator.XLINK_HREF_NAME))) {
            AppSchemaAttributeBuilder builder = new AppSchemaAttributeBuilder(featureFactory);
            if (crs != null) {
                builder.setCRS(crs);
            }
            builder.setDescriptor(parent.getDescriptor());
            // check for mapped type override
            builder.setType(parent.getType());

            if (targetNodeType != null) {
                if (parent.getType().getName().equals(XSSchema.ANYTYPE_TYPE.getName())) {
                    // special handling for casting any type since there's no attributes in its
                    // schema
                    leafAttribute = builder.addAnyTypeValue(convertedValue, targetNodeType, descriptor, id);
                } else {
                    leafAttribute = builder.add(id, convertedValue, attributeName, targetNodeType);
                }
            } else if (descriptor.getType().getName().equals(XSSchema.ANYTYPE_TYPE.getName())
                    && (value == null || (value instanceof Collection && ((Collection) value).isEmpty()))) {
                // casting anyType as a complex attribute so we can set xlink:href
                leafAttribute = builder.addComplexAnyTypeAttribute(convertedValue, descriptor, id);
            } else {
                leafAttribute = builder.add(id, convertedValue, attributeName);
            }
            if (index > -1) {
                // set attribute index if specified so it can be retrieved later for grouping
                leafAttribute.getUserData().put(MAPPED_ATTRIBUTE_INDEX, index);
            }
            addProperty(parent, leafAttribute);
        }

        if (!isEmpty(convertedValue)) {
            leafAttribute.setValue(convertedValue);
        }
        if (simpleContentProperties != null) {
            mergeClientProperties(leafAttribute, simpleContentProperties);
        }
        return leafAttribute;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addProperty(final Attribute parent, Attribute attribute) {
        if (parent instanceof ComplexAttributeImpl) {
            ((ComplexAttributeImpl) parent).addValue(attribute);
        } else {
            Collection currValue = (Collection) parent.getValue();
            List newValue = new ArrayList<>(currValue);
            newValue.add(attribute);
            parent.setValue(newValue);
        }
    }

    private Attribute findLeafAttribute(
            final ComplexAttribute parent,
            final Name attributeName,
            final int index,
            boolean isXlinkRef,
            Object convertedValue) {
        Attribute leafAttribute = null;
        if (isXlinkRef || isUnboundedMultivalue(parent)) {
            return null;
        }

        if (parent instanceof ComplexAttributeImpl) {
            // faster than parent.getProperties(attributeName)
            if (((ComplexAttributeImpl) parent).findLast(attributeName).isEmpty()) {
                return null;
            }
        } else if (parent.getProperties(attributeName).isEmpty()) {
            return null;
        }

        // skip this process if the attribute would only contain xlink:ref
        // that is chained, because it won't contain any values, and we
        // want to create a new empty leaf attribute

        if (isEmpty(convertedValue)) {
            leafAttribute = getAttributeMatchingIndex(parent, attributeName, index);
        } else {
            // eliminate duplicates in case the values come from denormalized view..
            Predicate<Attribute> valueFilter = att -> att != null && Objects.equals(att.getValue(), (convertedValue));
            if (index > -1) {
                final boolean checkMappedAttributeIndexOnly = true;
                Attribute sameIndex =
                        getAttributeWithMappedIndex(parent, attributeName, index, checkMappedAttributeIndexOnly);
                if (valueFilter.test(sameIndex)) {
                    leafAttribute = sameIndex;
                }
            } else {
                leafAttribute = findFirst(parent, attributeName, valueFilter);
            }
        }
        return leafAttribute;
    }

    private Attribute getAttributeMatchingIndex(
            final ComplexAttribute parent, final Name attributeName, final int index) {
        Attribute leafAttribute;
        // when attribute is empty, it is probably just a parent of a leaf attribute
        // it could already exist from another attribute mapping for a different leaf
        // e.g. 2 different attribute mappings:
        // sa:relatedObservation/om:Observation/om:parameter[2]/swe:Time/swe:uom
        // sa:relatedObservation/om:Observation/om:parameter[2]/swe:Time/swe:value
        // and this could be processing om:parameter[2] the second time for
        // swe:value so we need to find it if it already exists
        if (index > -1) {
            // get the attribute of specified index
            final boolean checkMappedAttributeIndexOnly = false;
            leafAttribute = getAttributeWithMappedIndex(parent, attributeName, index, checkMappedAttributeIndexOnly);
        } else {
            // get the last existing node
            leafAttribute = getLastAttribute(parent, attributeName);
        }
        return leafAttribute;
    }

    private Attribute findFirst(ComplexAttribute parent, Name attributeName, Predicate<Attribute> filter) {
        Optional<? extends Property> found;

        // try to avoid calling parent.getProperties(attributeName) because it's a performance
        // killer
        if (parent instanceof ComplexAttributeImpl) {
            Predicate<Property> nameFilter = p -> attributeName.equals(p.getName());
            Predicate<Property> attFilter = p -> Attribute.class.isInstance(p);
            found = ((ComplexAttributeImpl) parent)
                    .findAll(attFilter.and(nameFilter))
                    .map(Attribute.class::cast)
                    .filter(filter)
                    .findFirst();
        } else {
            final List<Attribute> values = getAttributes(parent, attributeName);
            Predicate<Attribute> nameFilter = p -> attributeName.equals(p.getName());
            found = values.stream().filter(nameFilter.and(filter)).findFirst();
        }
        return found.map(Attribute.class::cast).orElse(null);
    }

    private Attribute getAttributeWithMappedIndex(
            ComplexAttribute parent, Name attributeName, int index, boolean checkMappedAttributeIndexOnly) {

        final Predicate<Attribute> filter = stepValue -> {
            int valueIndex = 1;
            if (attributeName.equals(stepValue.getName())) {
                Object mappedIndex;
                if (stepValue instanceof PropertyImpl) {
                    // non-api method
                    mappedIndex = ((PropertyImpl) stepValue).getUserData(MAPPED_ATTRIBUTE_INDEX);
                } else {
                    mappedIndex = stepValue.getUserData().get(MAPPED_ATTRIBUTE_INDEX);
                }
                if (null == mappedIndex) {
                    if (checkMappedAttributeIndexOnly) {
                        return false;
                    }
                    mappedIndex = valueIndex;
                }
                if (!(mappedIndex instanceof Number)) {
                    mappedIndex = Integer.parseInt(String.valueOf(mappedIndex));
                }
                if (index == ((Number) mappedIndex).intValue()) {
                    return true;
                }
                valueIndex++;
            }
            return false;
        };
        return findFirst(parent, attributeName, filter);
    }

    private Attribute getLastAttribute(ComplexAttribute parent, Name attributeName) {
        // try to avoid parent.getProperties(Name), performance killer
        if (parent instanceof ComplexAttributeImpl) {
            return ((ComplexAttributeImpl) parent)
                    .findLast(attributeName)
                    .map(Attribute.class::cast)
                    .orElse(null);
        }
        final List<Attribute> values = getAttributes(parent, attributeName);
        return values.isEmpty() ? null : values.get(values.size() - 1);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Attribute> getAttributes(ComplexAttribute parent, Name attributeName) {
        Collection<Property> currStepValue = parent.getProperties(attributeName);
        if (currStepValue.isEmpty()) {
            return List.of();
        }
        if (currStepValue instanceof List) {
            return (List) currStepValue;
        }
        return new ArrayList<>((Collection) currStepValue);
    }

    /**
     * Extract the simple content attribute from a list of features. This is used when feature chaining is used for
     * simple contents, such as gml:name.. therefore the iterator would create a list of features containing the simple
     * content attributes.
     *
     * @param value List of features
     * @return The attribute with simple content
     */
    private List<Property> getSimpleContentList(Object value) {
        if (value == null || !(value instanceof Collection)) {
            return null;
        }
        Collection<?> list = (Collection<?>) value;
        if (list.size() != 1) {
            // there should only 1 feature in a list even if it's multi-valued
            // since each value should be wrapped in its own parent node
            // eg. the format is
            // gsml:specification[1]/gsml:CompositionPart/...
            // gsml:specification[2]/gsml:CompositionPart/...
            throw new IllegalArgumentException("Expecting only 1 feature in the list!");
        }
        Object f = list.iterator().next();
        if (!(f instanceof Feature)) {
            throw new IllegalArgumentException("Expecting a feature!");
        }
        Feature feature = (Feature) f;
        Collection<Property> featureProps = feature.getProperties();
        List<Property> properties = new ArrayList<>(featureProps.size());
        for (Property prop : featureProps) {
            if (!ComplexFeatureConstants.FEATURE_CHAINING_LINK_NAME.equals(prop.getName())) {
                properties.add(prop);
            }
        }
        return properties;
    }

    /**
     * Merge client properties from an attribute with a given map.
     *
     * @param leafAttribute The attribute which will have the client properties
     * @param simpleContentProperties Map of new client properties
     */
    @SuppressWarnings("unchecked")
    private void mergeClientProperties(Attribute leafAttribute, Map<Object, Object> simpleContentProperties) {

        Map<Object, Object> origData = leafAttribute.getUserData();
        for (Object key : simpleContentProperties.keySet()) {
            if (key.equals(Attributes.class)) {
                // client properties
                Map inputMap = (Map) simpleContentProperties.get(key);
                if (origData.containsKey(Attributes.class)) {
                    // check each entry, and copy if it doesn't exist
                    Map existingMap = (Map) origData.get(key);
                    for (Object mapKey : inputMap.keySet()) {
                        if (!existingMap.containsKey(mapKey)) {
                            existingMap.put(mapKey, inputMap.get(mapKey));
                        }
                    }
                } else {
                    // copy the whole thing
                    origData.put(Attributes.class, inputMap);
                }
            } else {
                if (!origData.containsKey(key)) {
                    origData.put(key, simpleContentProperties.get(key));
                }
            }
        }
    }

    /**
     * Determine whether or not the value is a feature with target descriptor that is of the given attribute descriptor.
     * If it is, then it is a feature chained feature with only simple content.
     *
     * @param descriptor The attribute descriptor
     * @param value value to check
     * @return true if the value is a {@link Collection} containing a feature with the descriptor.
     */
    private boolean isFeatureChainedSimpleContent(AttributeDescriptor descriptor, Object value) {
        boolean isFeatureChainedSimpleContent = false;
        if (value instanceof Collection && !isEmpty(value)) {
            Object f = ((Collection<?>) value).iterator().next();
            if (f instanceof Feature) {
                Name featureName = ((Feature) f).getDescriptor().getName();
                if (((Feature) f).getProperty(featureName) != null) {
                    isFeatureChainedSimpleContent = true;
                }
            }
        }
        return isFeatureChainedSimpleContent;
    }

    private boolean isEmpty(Object convertedValue) {
        if (convertedValue == null) {
            return true;
        }
        if (convertedValue instanceof Collection) {
            return ((Collection<?>) convertedValue).isEmpty();
        }
        return false;
    }

    /** Return value converted into a type suitable for this descriptor. */
    private Object convertValue(final AttributeDescriptor descriptor, final Object value) {

        final AttributeType type = descriptor.getType();
        final Class<?> binding = type.getBinding();

        if (type instanceof ComplexType && binding == Collection.class) {
            final boolean isSimpleContent = Types.isSimpleContentType(type);
            final boolean canHaveTextContent = Types.canHaveTextContent(type);

            if (value instanceof Collection) {
                Collection<?> values = (Collection<?>) value;
                if (!isSimpleContent && !canHaveTextContent) {
                    return value; // no conversion required
                }
                return values.stream()
                        .map(v -> {
                            if (v instanceof Property) {
                                return (Property) v;
                            } else if (isSimpleContent) {
                                return buildSimpleContent(type, v);
                            } else if (canHaveTextContent) {
                                return buildTextContent(type, v);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

            } else if (isSimpleContent || canHaveTextContent) {
                if (value == null && !descriptor.isNillable()) {
                    return List.of();
                } else if (isSimpleContent) {
                    return List.of(buildSimpleContent(type, value));
                } else if (canHaveTextContent) {
                    return List.of(buildTextContent(type, value));
                }
                return List.of();
            }
        }
        if (binding == String.class && value instanceof Collection) {
            // if it's a single value in a collection, strip the square brackets
            String collectionString = value.toString();
            return collectionString.substring(1, collectionString.length() - 1);
        }
        if (value instanceof Literal) {
            final Literal literal = (Literal) value;
            return literal.evaluate(literal.getValue(), binding);
        }
        return FF.literal(value).evaluate(value, binding);
    }

    /** Get base (non-collection) type of simple content. */
    static AttributeType getSimpleContentType(AttributeType type) {
        Class<?> binding = type.getBinding();
        if (binding == Collection.class) {
            return getSimpleContentType(type.getSuper());
        } else {
            return type;
        }
    }

    /** Create a fake property for simple content of a complex type. */
    Attribute buildSimpleContent(AttributeType type, Object value) {
        AttributeType simpleContentType = getSimpleContentType(type);
        return buildSimpleContentInternal(simpleContentType, value);
    }

    /**
     * Create a fake property to store arbitrary text in a complex type.
     *
     * <p>Passed in value is converted to a string and then stored in the special <code>
     * simpleContent</code> attribute.
     */
    Attribute buildTextContent(AttributeType type, Object value) {
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        atb.setName(ComplexFeatureConstants.SIMPLE_CONTENT.getLocalPart());
        atb.setBinding(String.class);
        AttributeType textContentType = atb.buildType();
        return buildSimpleContentInternal(textContentType, value);
    }

    private Attribute buildSimpleContentInternal(AttributeType simpleContentType, Object value) {
        Object convertedValue = FF.literal(value).evaluate(value, simpleContentType.getBinding());
        AttributeDescriptor descriptor = new AttributeDescriptorImpl(
                simpleContentType, ComplexFeatureConstants.SIMPLE_CONTENT, 1, 1, true, null);
        return new AttributeImpl(convertedValue, descriptor, null);
    }

    public boolean isComplexType(final StepList attrXPath, final AttributeDescriptor featureType) {
        PropertyName attExp = FF.property(attrXPath.toString());
        Object type = attExp.evaluate(featureType);
        if (type == null) {
            type = attExp.evaluate(featureType);
            throw new IllegalArgumentException("path not found: " + attrXPath);
        }

        AttributeDescriptor node = (AttributeDescriptor) type;
        return node.getType() instanceof ComplexType;
    }

    /** @return true if this step represents an id attribute */
    public static boolean isId(Step step) {
        return step.isXmlAttribute() && step.getName().equals(GML.id);
    }

    private boolean isUnboundedMultivalue(final Attribute parent) {
        final Object value = parent.getUserData().get(MULTI_VALUE_TYPE);
        if (value instanceof String) {
            return UNBOUNDED_MULTI_VALUE.equals(value);
        }
        return false;
    }
}
