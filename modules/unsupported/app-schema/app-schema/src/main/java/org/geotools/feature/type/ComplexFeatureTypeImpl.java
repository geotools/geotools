package org.geotools.feature.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotools.data.complex.ComplexFeatureConstants;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * This is a specialization of complex feature type that can be nested inside another feature type.
 * A system attribute descriptor called "FEATURE_LINK" is added in the descriptors so it can be used
 * to link the type to its parent type, without being encoded, since it doesn't exist in the real
 * schema.
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 * @source $URL$
 */
public class ComplexFeatureTypeImpl extends UniqueNameFeatureTypeImpl {
    /**
     * Type specific descriptors, excluding FEATURE_LINK
     */
    private Collection<PropertyDescriptor> schema;

    /**
     * Constructor for complex feature type with fake feature type descriptor provided.
     * 
     * @param name
     *            Name of feature type
     * @param schema
     *            Schema property descriptors
     * @param defaultGeometry
     *            Default geometry
     * @param isAbstract
     *            True if this type is abstract
     * @param restrictions
     *            List of restrictions
     * @param superType
     *            Super type
     * @param description
     *            Feature description
     * @param featureLink
     *            System attribute used to link between features
     */
    public ComplexFeatureTypeImpl(Name name, Collection<PropertyDescriptor> schema,
            GeometryDescriptor defaultGeometry, boolean isAbstract, List<Filter> restrictions,
            AttributeType superType, InternationalString description) {
        super(name, new ArrayList<PropertyDescriptor>(schema) {
            {
                add(ComplexFeatureConstants.FEATURE_CHAINING_LINK);
            };
        }, defaultGeometry, isAbstract, restrictions, superType, description);

        this.schema = schema;
    }

    /**
     * Create a clone of an existing ComplexFeatureTypeImpl with new schema.
     * 
     * @param type
     *            Type to copy
     * @param schema
     *            Set of descriptors
     */
    public ComplexFeatureTypeImpl(ComplexFeatureTypeImpl type, Collection<PropertyDescriptor> schema) {
        super(type.name, schema, null, type.isAbstract, type.restrictions,
                (AttributeType) type.superType, type.description);
        this.schema = schema;
        this.userData.putAll(type.userData);
    }

    /**
     * Return all the descriptors that come from the schema, excluding the system descriptors, such
     * as 'FEATURE_LINK', used for linking features.
     * 
     * @return schema descriptors
     */
    public Collection<PropertyDescriptor> getTypeDescriptors() {
        return schema;
    }
}
