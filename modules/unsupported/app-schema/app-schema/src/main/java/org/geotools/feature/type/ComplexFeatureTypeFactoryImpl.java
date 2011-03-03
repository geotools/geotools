package org.geotools.feature.type;

import java.util.Collection;
import java.util.List;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * Feature type factory to produce complex feature type that can be used in feature chaining. The
 * specific complex feature type will have an additional system field called "FEATURE_LINK" that can
 * be used to link the feature type to its parent, i.e. allow the type to be nested.
 * 
 * @author Rini Angreani, Curtin University of Technology 
 *
 * @source $URL$
 */
public class ComplexFeatureTypeFactoryImpl extends UniqueNameFeatureTypeFactoryImpl {

    @Override
    public FeatureType createFeatureType(Name name, Collection schema,
            GeometryDescriptor defaultGeometry, boolean isAbstract, List restrictions,
            AttributeType superType, InternationalString description) {

        return new ComplexFeatureTypeImpl(name, schema, defaultGeometry, isAbstract, restrictions,
                superType, description);
    }
}
