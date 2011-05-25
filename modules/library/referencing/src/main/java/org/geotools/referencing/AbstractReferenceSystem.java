/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing;

import java.util.HashMap;
import java.util.Map;

import org.opengis.metadata.extent.Extent;
import org.opengis.util.InternationalString;
import org.opengis.referencing.ReferenceSystem;
import org.geotools.util.Utilities;


/**
 * Description of a spatial and temporal reference system used by a dataset.
 * <p>
 * This class is conceptually <cite>abstract</cite>, even if it is technically possible to
 * instantiate it. Typical applications should create instances of the most specific subclass with
 * {@code Default} prefix instead. An exception to this rule may occurs when it is not possible to
 * identify the exact type.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class AbstractReferenceSystem extends AbstractIdentifiedObject implements ReferenceSystem {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3337659819553899435L;

    /**
     * List of localizable properties. To be given to {@link AbstractIdentifiedObject} constructor.
     */
    private static final String[] LOCALIZABLES = {SCOPE_KEY};

    /**
     * Area for which the (coordinate) reference system is valid.
     */
    private final Extent domainOfValidity;

    /**
     * Description of domain of usage, or limitations of usage, for which this
     * (coordinate) reference system object is valid.
     */
    private final InternationalString scope;

    /**
     * Constructs a new reference system with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param object The reference system to copy.
     *
     * @since 2.2
     */
    public AbstractReferenceSystem(final ReferenceSystem object) {
        super(object);
        domainOfValidity = object.getDomainOfValidity();
        scope            = object.getScope();
    }

    /**
     * Constructs a reference system from a set of properties.
     * The properties given in argument follow the same rules than for the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     * Additionally, the following properties are understood by this construtor:
     * <p>
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #DOMAIN_OF_VALIDITY_KEY "domainOfValidity"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link Extent}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getDomainOfValidity}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #SCOPE_KEY "scope"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String} or {@link InternationalString}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getScope}</td>
     *   </tr>
     * </table>
     *
     * @param properties The properties to be given to this object.
     */
    public AbstractReferenceSystem(final Map<String,?> properties) {
        this(properties, new HashMap<String,Object>());
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private AbstractReferenceSystem(final Map<String,?> properties, final Map<String,Object> subProperties) {
        super(properties, subProperties, LOCALIZABLES);
        domainOfValidity = (Extent)   subProperties.get(DOMAIN_OF_VALIDITY_KEY);
        scope = (InternationalString) subProperties.get(SCOPE_KEY);
    }

    /**
     * Area or region or timeframe in which this (coordinate) reference system is valid.
     * Returns {@code null} if not available.
     *
     * @since 2.4
     */
    public Extent getDomainOfValidity() {
        return domainOfValidity;
    }

    /**
     * Area for which the (coordinate) reference system is valid.
     * Returns {@code null} if not available.
     *
     * @deprecated Renamed {@link #getDomainOfValidity}.
     */
    public Extent getValidArea() {
        return domainOfValidity;
    }

    /**
     * Description of domain of usage, or limitations of usage, for which this
     * (coordinate) reference system object is valid.
     * Returns {@code null} if not available.
     */
    public InternationalString getScope() {
        return scope;
    }

    /**
     * Compare this reference system with the specified object for equality.
     * If {@code compareMetadata} is {@code true}, then all available properties are
     * compared including {@linkplain #getValidArea valid area} and {@linkplain #getScope scope}.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (super.equals(object, compareMetadata)) {
            if (!compareMetadata) {
                return true;
            }
            final AbstractReferenceSystem that = (AbstractReferenceSystem) object;
            return Utilities.equals(domainOfValidity, that.domainOfValidity) &&
                   Utilities.equals(scope,            that.scope);
        }
        return false;
    }
}
