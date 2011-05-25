/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;

// J2SE dependencies
import java.util.Map;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.metadata.citation.CitationFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.util.NameFactory;


/**
 * Defines a common abstraction for getting the different factories. This default implementation
 * provides support for only the most basic factories ({@linkplain ReferencingFactoryFinder referencing},
 * <cite>etc.</cite>). Many methods thrown an {@link FactoryNotFoundException} in all cases, for
 * example all methods related to GO-1 canvas objects. Those methods will be implemented later
 * in a subclass.
 *
 * @since 2.3
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class BasicFactories {
    
    /**
     * The default authority name for authority factories.
     */
    private static final String DEFAULT_AUTHORITY = "EPSG";

    /**
     * The default instance. Will be created only when first needed.
     *
     * @see #getDefault
     */
    private static BasicFactories DEFAULT;

    /**
     * The hints to be used for all factory creation.
     */
    protected final Hints hints;

    /**
     * Creates a new instance of {@code BasicFactories} with the specified set of hints. The
     * {@code hints} map should contains only the minimum set of hints, since this constructor
     * will keep a reference to all objects found in this map.
     * 
     * @param hints The hints to be used for all factory creation, or {@code null} if none.
     */
    public BasicFactories(final Map hints) {
        this.hints = (hints!=null) ? new Hints(hints) : null;
    }

    /**
     * Returns a default common factory instance.
     */
    public static synchronized BasicFactories getDefault() {
        if (DEFAULT == null) {
            DEFAULT = new BasicFactories(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        }
        return DEFAULT;
    }

    /**
     * Format an error message saying that the specified factory is not yet supported.
     * The error message will be given to a {@link FactoryNotFoundException}.
     *
     * @param type The factory type requested by the users.
     */
    private static String unsupportedFactory(final Class type) {
        return Errors.format(ErrorKeys.FACTORY_NOT_FOUND_$1, Classes.getShortName(type));
    }

    /**
     * Returns the {@linkplain FeatureTypeFactory feature type factory} singleton.
     * <p>
     * <strong>NOTE:</strong> This method is not yet supported in Geotools.
     * The default implementation thrown an exception in all case.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     * 
     * @deprecated use {@link #getFeatureTypeFactory()}.
     */
    public FeatureTypeFactory getTypeFactory() throws FactoryRegistryException {
        return getFeatureTypeFactory();
    }

    /**
     * Returns the {@linkplain FeatureTypeFactory feature type factory} singleton.
     * <p>
     * <strong>NOTE:</strong> This method is not yet supported in Geotools.
     * The default implementation thrown an exception in all case.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     * 
     * @deprecated use {@link #getFeatureTypeFactory()}.
     * @since 2.5
     */
    public FeatureTypeFactory getFeatureTypeFactory() throws FactoryRegistryException {
        throw new FactoryNotFoundException(unsupportedFactory(FeatureTypeFactory.class));
    }
    
    /**
     * Returns the {@linkplain FilterFactory filter factory} singleton.
     * <p>
     * <strong>NOTE:</strong> This method is not yet supported in Geotools.
     * The default implementation thrown an exception in all case.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public FilterFactory getFilterFactory() throws FactoryRegistryException {
        throw new FactoryNotFoundException(unsupportedFactory(FilterFactory.class));
    }

    /**
     * Returns the {@linkplain NameFactory name factory} singleton.
     * <p>
     * <strong>NOTE:</strong> This method is not yet supported in Geotools.
     * The default implementation thrown an exception in all case.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public NameFactory getNameFactory() throws FactoryRegistryException {
        throw new FactoryNotFoundException(unsupportedFactory(NameFactory.class));
    }

    /**
     * Returns the {@linkplain CitationFactory citation factory} singleton.
     * <p>
     * <strong>NOTE:</strong> This method is not yet supported in Geotools.
     * The default implementation thrown an exception in all case.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public CitationFactory getCitationFactory() throws FactoryRegistryException {
        throw new FactoryNotFoundException(unsupportedFactory(CitationFactory.class));
    }

    /**
     * Returns the {@linkplain CRSAuthorityFactory CRS authority factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public CRSAuthorityFactory getCRSAuthorityFactory() throws FactoryRegistryException {
        return ReferencingFactoryFinder.getCRSAuthorityFactory(DEFAULT_AUTHORITY, hints);
    }

    /**
     * Returns the {@linkplain CRSFactory CRS factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public CRSFactory getCRSFactory() throws FactoryRegistryException {
        return ReferencingFactoryFinder.getCRSFactory(hints);
    }

    /**
     * Returns the {@linkplain CSAuthorityFactory CS authority factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public CSAuthorityFactory getCSAuthorityFactory() throws FactoryRegistryException {
        return ReferencingFactoryFinder.getCSAuthorityFactory(DEFAULT_AUTHORITY, hints);
    }

    /**
     * Returns the {@linkplain CSFactory CS factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public CSFactory getCSFactory() throws FactoryRegistryException {
        return ReferencingFactoryFinder.getCSFactory(hints);
    }

    /**
     * Returns the {@linkplain DatumAuthorityFactory datum authority factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public DatumAuthorityFactory getDatumAuthorityFactory() throws FactoryRegistryException {
        return ReferencingFactoryFinder.getDatumAuthorityFactory(DEFAULT_AUTHORITY, hints);
    }

    /**
     * Returns the {@linkplain DatumFactory datum factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public DatumFactory getDatumFactory() throws FactoryRegistryException {
        return ReferencingFactoryFinder.getDatumFactory(hints);
    }

    /**
     * Returns the {@linkplain CoordinateOperationAuthorityFactory coordinate operation authority
     * factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public CoordinateOperationAuthorityFactory getCoordinateOperationAuthorityFactory()
            throws FactoryRegistryException
    {
        return ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory(DEFAULT_AUTHORITY, hints);
    }

    /**
     * Returns the {@linkplain CoordinateOperationFactory coordinate operation factory} singleton.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public CoordinateOperationFactory getCoordinateOperationFactory()
            throws FactoryRegistryException
    {
        return ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
    }

    /**
     * Returns the {@linkplain GeometryFactory geometry factory} equiped to build geometries
     * using the given {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * <p>
     * <strong>NOTE:</strong> This method is not yet supported in Geotools.
     * The default implementation thrown an exception in all case.
     *
     * @param crs the {@linkplain CoordinateReferenceSystem coordinate reference system} the
     *        {@linkplain GeometryFactory geometry factory} should use.
     * @return the requested {@linkplain GeometryFactory geometry factory} or {@code null} if the 
     *         {@linkplain CoordinateReferenceSystem coordinate reference system} is not supported.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public GeometryFactory getGeometryFactory(final CoordinateReferenceSystem crs)
            throws FactoryRegistryException
    {
        throw new FactoryNotFoundException(unsupportedFactory(GeometryFactory.class));
    }

    /**
     * Returns the {@linkplain PrimitiveFactory primitive factory} equiped to build primitives
     * using the given {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * <p>
     * <strong>NOTE:</strong> This method is not yet supported in Geotools.
     * The default implementation thrown an exception in all case.
     *
     * @param crs the {@linkplain CoordinateReferenceSystem coordinate reference system} the
     *        {@linkplain PrimitiveFactory primitive factory} should use.
     * @return the requested {@linkplain PrimitiveFactory primitive factory} or {@code null} if the 
     *         {@linkplain CoordinateReferenceSystem coordinate reference system} is not supported.
     *
     * @throws FactoryNotFoundException if no factory was found for the requested type.
     * @throws FactoryRegistryException if the factory can't be obtained for an other reason.
     */
    public PrimitiveFactory getPrimitiveFactory(final CoordinateReferenceSystem crs)
            throws FactoryRegistryException
    {
        throw new FactoryNotFoundException(unsupportedFactory(PrimitiveFactory.class));
    }
}
