package org.geotools.data.complex.config;

import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;

/**
 * Customizer for Feature Type Registry, tells the registry when to create a Feature Type, a Geometry Type or set Identifiable
 * 
 * @author Niels Charlier
 *
 */
public interface FeatureTypeRegistryHelper {

    boolean isFeatureType(XSDTypeDefinition typeDefinition);

    boolean isGeometryType(XSDTypeDefinition typeDefinition);

    boolean isIdentifiable(XSDComplexTypeDefinition typeDefinition);

}
