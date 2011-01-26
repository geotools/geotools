/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.type.DefaultFeatureTypeBuilder;


/**
 * Replaced with use of FeatureTypeBuilder to follow standard pattern
 * naming conventions.
 *
 * @deprecated Please use FeatureTypeBuilder
 * @author Ian Schneider
 * @source $URL$
 * @version $Id$
 */
public abstract class FeatureTypeFactory extends FeatureTypeBuilder {
    /**
     * An empty public constructor. Subclasses should not provide a
     * constructor.
     */
    public FeatureTypeFactory() {
    	// no op constructor
    }

    /**
     * The service registry for this manager.
     * Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Returns the service registry. The registry will be created the first
     * time this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(FeatureTypeFactory.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {
                    FeatureTypeFactory.class}));
        }
        return registry;
    }

    /**
     * The most specific way to create a new FeatureType.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @param isAbstract True if this created type should be abstract.
     * @param superTypes A Collection of types the FeatureType will inherit
     *        from. Currently, all types inherit from feature in the opengis
     *        namespace.
     *
     * @return A new FeatureType created from the given arguments.
     *
     * @throws FactoryRegistryException If there are problems creating a
     *         factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in
     *         some way.
     */
    public static FeatureType newFeatureType(AttributeType[] types,
        String name, URI ns, boolean isAbstract, FeatureType[] superTypes)
        throws FactoryRegistryException, SchemaException {
            return newFeatureType(types, name, ns, isAbstract, superTypes, null);
    }

    /**
     * The most specific way to create a new FeatureType.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @param isAbstract True if this created type should be abstract.
     * @param superTypes A Collection of types the FeatureType will inherit
     *        from. Currently, all types inherit from feature in the opengis
     *        namespace.
     *
     * @return A new FeatureType created from the given arguments.
     *
     * @throws FactoryRegistryException If there are problems creating a
     *         factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in
     *         some way.
     */
    public static FeatureType newFeatureType(AttributeType[] types,
        String name, URI ns, boolean isAbstract, FeatureType[] superTypes, AttributeType defaultGeometry)
        throws FactoryRegistryException, SchemaException {

        return new DefaultFeatureType(name,ns, (List) Arrays.asList(types), (List) Arrays.asList(superTypes), (GeometryAttributeType) defaultGeometry);
    }

    /**
         * The most specific way to create a new FeatureType.
         *
         * @param types The AttributeTypes to create the FeatureType with.
         * @param name The typeName of the FeatureType. Required, may not be null.
         * @param ns The namespace of the FeatureType. Optional, may be null.
         * @param isAbstract True if this created type should be abstract.
         * @param superTypes A Collection of types the FeatureType will inherit
         *        from. Currently, all types inherit from feature in the opengis
         *        namespace.
         *
         * @return A new FeatureType created from the given arguments.
         *
         * @throws FactoryRegistryException If there are problems creating a
         *         factory.
         * @throws SchemaException If the AttributeTypes provided are invalid in
         *         some way.
         */
        public static FeatureType newFeatureType(AttributeType[] types,
            String name, URI ns, boolean isAbstract, FeatureType[] superTypes, GeometryAttributeType defaultGeometry)
            throws FactoryRegistryException, SchemaException {

            List typeList = types == null ? null : (List) Arrays.asList(types);
			List superTypeList = superTypes == null ? null : (List) Arrays.asList(superTypes);
			return new DefaultFeatureType(name, ns, typeList, superTypeList, defaultGeometry);
        }

    /**
     * Create a new FeatureType with the given AttributeTypes. A short cut for
     * calling <code>newFeatureType(types,name,ns,isAbstract,null)</code>.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     * @param isAbstract True if this created type should be abstract.
     *
     * @return A new FeatureType created from the given arguments.
     *
     * @throws FactoryRegistryException If there are problems creating a
     *         factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in
     *         some way.
     */
    public static FeatureType newFeatureType(AttributeType[] types,
        String name, URI ns, boolean isAbstract)
        throws FactoryRegistryException, SchemaException {
        return newFeatureType(types, name, ns, isAbstract, null);
    }

    /**
     * Create a new FeatureType with the given AttributeTypes. A short cut for
     * calling <code>newFeatureType(types,name,ns,false,null)</code>.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     * @param ns The namespace of the FeatureType. Optional, may be null.
     *
     * @return A new FeatureType created from the given arguments.
     *
     * @throws FactoryRegistryException If there are problems creating a
     *         factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in
     *         some way.
     */
    public static FeatureType newFeatureType(AttributeType[] types,
        String name, URI ns)
        throws FactoryRegistryException, SchemaException {
        return newFeatureType(types, name, ns, false);
    }

    /**
     * Create a new FeatureType with the given AttributeTypes. A short cut for
     * calling <code>newFeatureType(types,name,null,false,null)</code>. Useful
     * for test cases or datasources which may not allow a namespace.
     *
     * @param types The AttributeTypes to create the FeatureType with.
     * @param name The typeName of the FeatureType. Required, may not be null.
     *
     * @return A new FeatureType created from the given arguments.
     *
     * @throws FactoryRegistryException If there are problems creating a
     *         factory.
     * @throws SchemaException If the AttributeTypes provided are invalid in
     *         some way.
     */
    public static FeatureType newFeatureType(AttributeType[] types, String name)
        throws FactoryRegistryException, SchemaException {
        return newFeatureType(types, name, FeatureTypes.DEFAULT_NAMESPACE, false);
    }

    /**
     * Create a new FeatureTypeFactory with the given typeName.
     *
     * @param name The typeName of the feature to create.
     *
     * @return A new FeatureTypeFactory instance.
     *
     * @throws FactoryRegistryException If there exists a configuration error.
     */
    public static synchronized FeatureTypeFactory newInstance(String name)
        throws FactoryRegistryException {
    	FeatureTypeFactory factory = (FeatureTypeFactory) getServiceRegistry()
        	.getServiceProvider(FeatureTypeFactory.class, null, null, null);
    	FeatureTypeFactory newFactory;
		try {
			newFactory = (FeatureTypeFactory) factory.getClass().newInstance();
		} catch (InstantiationException e) {
			throw new FactoryRegistryException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new FactoryRegistryException(e.getMessage(), e);
		}
    	newFactory.setName(name);

        return newFactory;
    }

    /**
     * Create a FeatureTypeFactory which contains all of the AttributeTypes
     * from the given FeatureType. This is simply a convenience method for<br>
     * <code><pre>
     * FeatureTypeFactory factory = FeatureTypeFactory.newInstace();
     * factory.importType(yourTypeHere);
     * factory.setName(original.getName());
     * factory.setNamespace(original.getNamespace());
     * factory.setNillable(original.isNillable());
     * factory.setDefaultGeometry(original.getDefaultGeometry());
     * </pre></code>
     *
     * @param original The FeatureType to obtain information from.
     *
     * @return A new FeatureTypeFactory which is initialized with the state of
     *         the original FeatureType.
     *
     * @throws FactoryRegistryException If a FeatureTypeFactory cannot be
     *         found.
     */
    public static FeatureTypeFactory createTemplate(FeatureType original)
        throws FactoryRegistryException {

    	FeatureTypeFactory builder = FeatureTypeFactory.newInstance(original.getTypeName());
        builder.importType(original);
        builder.setNamespace(original.getNamespace());
        builder.setDefaultGeometry(original.getGeometryDescriptor());

        FeatureType[] ancestors = original.getAncestors();

        if (ancestors != null) {
        	builder.setSuperTypes(Arrays.asList(ancestors));
        }

        return builder;
    }

    /**
     * Returns a string representation of this factory.
     *
     * @return The string representing this factory.
     */
    public String toString() {
        String types = "";

        for (int i = 0, ii = getAttributeCount(); i < ii; i++) {
            types += get(i);

            if (i < ii) {
                types += " , ";
            }
        }

        return "FeatureTypeFactory(" + getClass().getName() + ") [ " + types
        + " ]";
    }

}
