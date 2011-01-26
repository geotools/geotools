/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.datum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.datum.Datum;
import org.opengis.util.InternationalString;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Utilities;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Vocabulary;


/**
 * Specifies the relationship of a coordinate system to the earth, thus creating a {@linkplain
 * org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference system}. A datum
 * uses a parameter or set of parameters that determine the location of the origin of the coordinate
 * reference system. Each datum subtype can be associated with only specific types of
 * {@linkplain org.opengis.referencing.cs.AbstractCS coordinate systems}.
 * <p>
 * A datum can be defined as a set of real points on the earth that have coordinates.
 * The definition of the datum may also include the temporal behavior (such as the
 * rate of change of the orientation of the coordinate axes).
 * <p>
 * This class is conceptually <cite>abstract</cite>, even if it is technically possible to
 * instantiate it. Typical applications should create instances of the most specific subclass with
 * {@code Default} prefix instead. An exception to this rule may occurs when it is not possible to
 * identify the exact type.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 *
 * @see org.geotools.referencing.cs.AbstractCS
 * @see org.geotools.referencing.crs.AbstractCRS
 */
public class AbstractDatum extends AbstractIdentifiedObject implements Datum {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -4894180465652474930L;

    /**
     * List of localizable properties. To be given to
     * {@link AbstractIdentifiedObject} constructor.
     */
    private static final String[] LOCALIZABLES = {ANCHOR_POINT_KEY, SCOPE_KEY};

    /**
     * Description, possibly including coordinates, of the point or points used to anchor the datum
     * to the Earth. Also known as the "origin", especially for Engineering and Image Datums.
     */
    private final InternationalString anchorPoint;

    /**
     * The time after which this datum definition is valid. This time may be precise
     * (e.g. 1997 for IRTF97) or merely a year (e.g. 1983 for NAD83). If the time is
     * not defined, then the value is {@link Long#MIN_VALUE}.
     */
    private final long realizationEpoch;

    /**
     * Area or region in which this datum object is valid.
     */
    private final Extent domainOfValidity;

    /**
     * Description of domain of usage, or limitations of usage, for which this
     * datum object is valid.
     */
    private final InternationalString scope;

    /**
     * Constructs a new datum with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param datum The datum to copy.
     *
     * @since 2.2
     */
    public AbstractDatum(final Datum datum) {
        super(datum);
        final Date epoch = datum.getRealizationEpoch();
        realizationEpoch = (epoch!=null) ? epoch.getTime() : Long.MIN_VALUE;
        domainOfValidity = datum.getDomainOfValidity();
        scope            = datum.getScope();
        anchorPoint      = datum.getAnchorPoint();
    }

    /**
     * Constructs a datum from a set of properties. The properties given in argument follow
     * the same rules than for the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map)
     * super-class constructor}. Additionally, the following properties are understood by this
     * construtor:
     * <br><br>
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #ANCHOR_POINT_KEY "anchorPoint"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link InternationalString} or {@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getAnchorPoint}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #REALIZATION_EPOCH_KEY "realizationEpoch"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link Date}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getRealizationEpoch}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #DOMAIN_OF_VALIDITY_KEY "domainOfValidity"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link Extent}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getDomainOfValidity}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #SCOPE_KEY "scope"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link InternationalString} or {@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getScope}</td>
     *   </tr>
     * </table>
     *
     * @param properties The properties to be given to the identified object.
     */
    public AbstractDatum(final Map<String,?> properties) {
        this(properties, new HashMap<String,Object>());
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private AbstractDatum(final Map<String,?> properties, final Map<String,Object> subProperties) {
        super(properties, subProperties, LOCALIZABLES);
        final Date realizationEpoch;
        anchorPoint      = (InternationalString) subProperties.get(ANCHOR_POINT_KEY      );
        realizationEpoch = (Date)                subProperties.get(REALIZATION_EPOCH_KEY );
        domainOfValidity = (Extent)              subProperties.get(DOMAIN_OF_VALIDITY_KEY);
        scope            = (InternationalString) subProperties.get(SCOPE_KEY             );
        this.realizationEpoch = (realizationEpoch != null) ?
                                 realizationEpoch.getTime() : Long.MIN_VALUE;
    }

    /**
     * Same convenience method than {@link org.geotools.cs.AbstractCS#name} except that we get
     * the unlocalized name (usually in English locale), because the name is part of the elements
     * compared by the {@link #equals} method.
     */
    static Map<String,Object> name(final int key) {
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        final InternationalString name = Vocabulary.formatInternational(key);
        properties.put(NAME_KEY,  name.toString(null)); // "null" required for unlocalized version.
        properties.put(ALIAS_KEY, name);
        return properties;
    }

    /**
     * Description, possibly including coordinates, of the point or points used to anchor the datum
     * to the Earth. Also known as the "origin", especially for Engineering and Image Datums.
     *
     * <ul>
     *   <li>For a geodetic datum, this point is also known as the fundamental point, which is
     *       traditionally the point where the relationship between geoid and ellipsoid is defined.
     *       In some cases, the "fundamental point" may consist of a number of points. In those
     *       cases, the parameters defining the geoid/ellipsoid relationship have then been averaged
     *       for these points, and the averages adopted as the datum definition.</li>
     *
     *   <li>For an engineering datum, the anchor point may be a physical point, or it may be a
     *       point with defined coordinates in another CRS.</li>
     *
     *   <li>For an image datum, the anchor point is usually either the centre of the image or the
     *       corner of the image.</li>
     *
     *   <li>For a temporal datum, this attribute is not defined. Instead of the anchor point,
     *       a temporal datum carries a separate time origin of type {@link Date}.</li>
     * </ul>
     */
    public InternationalString getAnchorPoint() {
        return anchorPoint;
    }

    /**
     * The time after which this datum definition is valid. This time may be precise (e.g. 1997
     * for IRTF97) or merely a year (e.g. 1983 for NAD83). In the latter case, the epoch usually
     * refers to the year in which a major recalculation of the geodetic control network, underlying
     * the datum, was executed or initiated. An old datum can remain valid after a new datum is
     * defined. Alternatively, a datum may be superseded by a later datum, in which case the
     * realization epoch for the new datum defines the upper limit for the validity of the
     * superseded datum.
     */
    public Date getRealizationEpoch() {
        return (realizationEpoch!=Long.MIN_VALUE) ? new Date(realizationEpoch) : null;
    }

    /**
     * Area or region or timeframe in which this datum is valid.
     *
     * @since 2.4
     */
    public Extent getDomainOfValidity() {
        return domainOfValidity;
    }

    /**
     * Area or region in which this datum object is valid.
     *
     * @deprecated Renamed {@link #getDomainOfValidity}.
     */
    public Extent getValidArea() {
        return domainOfValidity;
    }

    /**
     * Description of domain of usage, or limitations of usage, for which this
     * datum object is valid.
     */
    public InternationalString getScope() {
        return scope;
    }

    /**
     * Gets the type of the datum as an enumerated code. Datum type was provided
     * for all kind of datum in the legacy OGC 01-009 specification. In the new
     * OGC 03-73 (ISO 19111) specification, datum type is provided only for
     * vertical datum. Nevertheless, we keep this method around since it is
     * needed for WKT formatting. Note that we returns the datum type ordinal
     * value, not the code list object.
     */
    int getLegacyDatumType() {
        return 0;
    }

    /**
     * Compares the specified object with this datum for equality.
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
                /*
                 * Tests for name, since datum with different name have completly
                 * different meaning. We don't perform this comparaison if the user
                 * asked for metadata comparaison, because in such case the names
                 * have already been compared by the subclass.
                 */
                return nameMatches(object.getName().getCode()) ||
                       object.nameMatches(getName().getCode());
            }
            final AbstractDatum that = (AbstractDatum) object;
            return this.realizationEpoch == that.realizationEpoch &&
                   Utilities.equals(this.domainOfValidity, that.domainOfValidity) &&
                   Utilities.equals(this.anchorPoint,      that.anchorPoint) &&
                   Utilities.equals(this.scope,            that.scope);
        }
        return false;
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * Note: All subclasses will override this method, but only {@link DefaultGeodeticDatum} will
     *       <strong>not</strong> invokes this parent method, because horizontal datum do not write
     *       the datum type.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        formatter.append(getLegacyDatumType());
        return Classes.getShortClassName(this);
    }
}
