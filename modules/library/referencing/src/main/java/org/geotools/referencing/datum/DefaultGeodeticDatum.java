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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.Matrix;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.operation.matrix.XMatrix;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.wkt.Formatter;


/**
 * Defines the location and precise orientation in 3-dimensional space of a defined ellipsoid
 * (or sphere) that approximates the shape of the earth. Used also for Cartesian coordinate
 * system centered in this ellipsoid (or sphere).
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see Ellipsoid
 * @see PrimeMeridian
 */
public class DefaultGeodeticDatum extends AbstractDatum implements GeodeticDatum {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8832100095648302943L;

    /**
     * The default WGS 1984 datum.
     */
    public static final DefaultGeodeticDatum WGS84;
    static {
        final ReferenceIdentifier[] identifiers = {
            new NamedIdentifier(Citations.OGC,    "WGS84"),
            new NamedIdentifier(Citations.ORACLE, "WGS 84"),
            new NamedIdentifier(null,             "WGS_84"),
            new NamedIdentifier(null,             "WGS 1984"),
            new NamedIdentifier(Citations.EPSG,   "WGS_1984"),
            new NamedIdentifier(Citations.ESRI,   "D_WGS_1984"),
            new NamedIdentifier(Citations.EPSG,   "World Geodetic System 1984")
        };
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(NAME_KEY,  identifiers[0]);
        properties.put(ALIAS_KEY, identifiers);
        WGS84 = new DefaultGeodeticDatum(properties, DefaultEllipsoid.WGS84,
                                         DefaultPrimeMeridian.GREENWICH);
    }

    /**
     * The <code>{@value #BURSA_WOLF_KEY}</code> property for
     * {@linkplain #getAffineTransform datum shifts}.
     */
    public static final String BURSA_WOLF_KEY = "bursaWolf";

    /**
     * The ellipsoid.
     */
    private final Ellipsoid ellipsoid;

    /**
     * The prime meridian.
     */
    private final PrimeMeridian primeMeridian;

    /**
     * Bursa Wolf parameters for datum shifts, or {@code null} if none.
     */
    private final BursaWolfParameters[] bursaWolf;

    /**
     * Constructs a new datum with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @since 2.2
     */
    public DefaultGeodeticDatum(final GeodeticDatum datum) {
        super(datum);
        ellipsoid     = datum.getEllipsoid();
        primeMeridian = datum.getPrimeMeridian();
        bursaWolf     = (datum instanceof DefaultGeodeticDatum) ?
                        ((DefaultGeodeticDatum) datum).bursaWolf : null;
    }

    /**
     * Constructs a geodetic datum from a name.
     *
     * @param name          The datum name.
     * @param ellipsoid     The ellipsoid.
     * @param primeMeridian The prime meridian.
     */
    public DefaultGeodeticDatum(final String        name,
                                final Ellipsoid     ellipsoid,
                                final PrimeMeridian primeMeridian)
    {
        this(Collections.singletonMap(NAME_KEY, name), ellipsoid, primeMeridian);
    }

    /**
     * Constructs a geodetic datum from a set of properties. The properties map is given
     * unchanged to the {@linkplain AbstractDatum#AbstractDatum(Map) super-class constructor}.
     * Additionally, the following properties are understood by this construtor:
     * <p>
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #BURSA_WOLF_KEY "bursaWolf"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link BursaWolfParameters} or an array of those&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getBursaWolfParameters}</td>
     *   </tr>
     * </table>
     *
     * @param properties    Set of properties. Should contains at least {@code "name"}.
     * @param ellipsoid     The ellipsoid.
     * @param primeMeridian The prime meridian.
     */
    public DefaultGeodeticDatum(final Map<String,?> properties,
                                final Ellipsoid     ellipsoid,
                                final PrimeMeridian primeMeridian)
    {
        super(properties);
        this.ellipsoid     = ellipsoid;
        this.primeMeridian = primeMeridian;
        ensureNonNull("ellipsoid",     ellipsoid);
        ensureNonNull("primeMeridian", primeMeridian);
        BursaWolfParameters[] bursaWolf;
        final Object object = properties.get(BURSA_WOLF_KEY);
        if (object instanceof BursaWolfParameters) {
            bursaWolf = new BursaWolfParameters[] {
                ((BursaWolfParameters) object).clone()
            };
        } else {
            bursaWolf = (BursaWolfParameters[]) object;
            if (bursaWolf != null) {
                if (bursaWolf.length == 0) {
                    bursaWolf = null;
                } else {
                    final Set<BursaWolfParameters> s = new LinkedHashSet<BursaWolfParameters>();
                    for (int i=0; i<bursaWolf.length; i++) {
                        s.add(bursaWolf[i].clone());
                    }
                    bursaWolf = s.toArray(new BursaWolfParameters[s.size()]);
                }
            }
        }
        this.bursaWolf = bursaWolf;
    }

    /**
     * Returns the ellipsoid.
     */
    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    /**
     * Returns the prime meridian.
     */
    public PrimeMeridian getPrimeMeridian() {
        return primeMeridian;
    }

    /**
     * Returns all Bursa Wolf parameters specified in the {@code properties} map at
     * construction time.
     *
     * @since 2.4
     */
    public BursaWolfParameters[] getBursaWolfParameters() {
        if (bursaWolf != null) {
            return bursaWolf.clone();
        }
        return new BursaWolfParameters[0];
    }

    /**
     * Returns Bursa Wolf parameters for a datum shift toward the specified target, or {@code null}
     * if none. This method search only for Bursa-Wolf parameters explicitly specified in the
     * {@code properties} map at construction time. This method doesn't try to infer a set of
     * parameters from indirect informations. For example it doesn't try to inverse the parameters
     * specified in the {@code target} datum if none were found in this datum. If such an elaborated
     * search is wanted, use {@link #getAffineTransform} instead.
     */
    public BursaWolfParameters getBursaWolfParameters(final GeodeticDatum target) {
        if (bursaWolf != null) {
            for (int i=0; i<bursaWolf.length; i++) {
                final BursaWolfParameters candidate = bursaWolf[i];
                if (equals(target, candidate.targetDatum, false)) {
                    return candidate.clone();
                }
            }
        }
        return null;
    }

    /**
     * Returns a matrix that can be used to define a transformation to the specified datum.
     * If no transformation path is found, then this method returns {@code null}.
     *
     * @param  source The source datum.
     * @param  target The target datum.
     * @return An affine transform from {@code source} to {@code target}, or {@code null} if none.
     *
     * @see BursaWolfParameters#getAffineTransform
     */
    public static Matrix getAffineTransform(final GeodeticDatum source,
                                            final GeodeticDatum target)
    {
        return getAffineTransform(source, target, null);
    }

    /**
     * Returns a matrix that can be used to define a transformation to the specified datum.
     * If no transformation path is found, then this method returns {@code null}.
     *
     * @param  source The source datum.
     * @param  target The target datum.
     * @param  exclusion The set of datum to exclude from the search, or {@code null}.
     *         This is used in order to avoid never-ending recursivity.
     * @return An affine transform from {@code source} to {@code target}, or {@code null} if none.
     *
     * @see BursaWolfParameters#getAffineTransform
     */
    private static XMatrix getAffineTransform(final GeodeticDatum source,
                                              final GeodeticDatum target,
                                              Set<GeodeticDatum> exclusion)
    {
        ensureNonNull("source", source);
        ensureNonNull("target", target);
        if (source instanceof DefaultGeodeticDatum) {
            final BursaWolfParameters[] bursaWolf = ((DefaultGeodeticDatum) source).bursaWolf;
            if (bursaWolf != null) {
                for (int i=0; i<bursaWolf.length; i++) {
                    final BursaWolfParameters transformation = bursaWolf[i];
                    if (equals(target, transformation.targetDatum, false)) {
                        return transformation.getAffineTransform();
                    }
                }
            }
        }
        /*
         * No transformation found to the specified target datum.
         * Search if a transform exists in the opposite direction.
         */
        if (target instanceof DefaultGeodeticDatum) {
            final BursaWolfParameters[] bursaWolf = ((DefaultGeodeticDatum) target).bursaWolf;
            if (bursaWolf != null) {
                for (int i=0; i<bursaWolf.length; i++) {
                    final BursaWolfParameters transformation = bursaWolf[i];
                    if (equals(source, transformation.targetDatum, false)) {
                        final XMatrix matrix = transformation.getAffineTransform();
                        matrix.invert();
                        return matrix;
                    }
                }
            }
        }
        /*
         * No direct tranformation found. Search for a path through some intermediate datum.
         * First, search if there is some BursaWolfParameters for the same target in both
         * 'source' and 'target' datum. If such an intermediate is found, ask for a path
         * as below:
         *
         *    source   -->   [common datum]   -->   target
         */
        if (source instanceof DefaultGeodeticDatum && target instanceof DefaultGeodeticDatum) {
            final BursaWolfParameters[] sourceParam = ((DefaultGeodeticDatum) source).bursaWolf;
            final BursaWolfParameters[] targetParam = ((DefaultGeodeticDatum) target).bursaWolf;
            if (sourceParam!=null && targetParam!=null) {
                GeodeticDatum sourceStep;
                GeodeticDatum targetStep;
                for (int i=0; i<sourceParam.length; i++) {
                    sourceStep = sourceParam[i].targetDatum;
                    for (int j=0; j<targetParam.length; j++) {
                        targetStep = targetParam[j].targetDatum;
                        if (equals(sourceStep, targetStep, false)) {
                            final XMatrix step1, step2;
                            if (exclusion == null) {
                                exclusion = new HashSet<GeodeticDatum>();
                            }
                            if (exclusion.add(source)) {
                                if (exclusion.add(target)) {
                                    step1 = getAffineTransform(source, sourceStep, exclusion);
                                    if (step1 != null) {
                                        step2 = getAffineTransform(targetStep, target, exclusion);
                                        if (step2 != null) {
                                            /*
                                             * Note: XMatrix.multiply(XMatrix) is equivalent to
                                             *       AffineTransform.concatenate(...): First
                                             *       transform by the supplied transform and
                                             *       then transform the result by the original
                                             *       transform.
                                             */
                                            step2.multiply(step1);
                                            return step2;
                                        }
                                    }
                                    exclusion.remove(target);
                                }
                                exclusion.remove(source);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified object is equals (at least on
     * computation purpose) to the {@link #WGS84} datum. This method may conservatively
     * returns {@code false} if the specified datum is uncertain (for example
     * because it come from an other implementation).
     */
    public static boolean isWGS84(final Datum datum) {
        if (datum instanceof AbstractIdentifiedObject) {
            return WGS84.equals((AbstractIdentifiedObject) datum, false);
        }
        // Maybe the specified object has its own test...
        return datum!=null && datum.equals(WGS84);
    }

    /**
     * Compare this datum with the specified object for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultGeodeticDatum that = (DefaultGeodeticDatum) object;
            if (equals(this.ellipsoid,     that.ellipsoid,     compareMetadata) &&
                equals(this.primeMeridian, that.primeMeridian, compareMetadata))
            {
                /*
                 * HACK: We do not consider Bursa Wolf parameters as a non-metadata field.
                 *       This is needed in order to get equalsIgnoreMetadata(...) to returns
                 *       'true' when comparing the WGS84 constant in this class with a WKT
                 *       DATUM element with a TOWGS84[0,0,0,0,0,0,0] element. Furthermore,
                 *       the Bursa Wolf parameters are not part of ISO 19111 specification.
                 *       We don't want two CRS to be considered as different because one has
                 *       more of those transformation informations (which is nice, but doesn't
                 *       change the CRS itself).
                 */
                return !compareMetadata || Arrays.equals(this.bursaWolf, that.bursaWolf);
            }
        }
        return false;
    }

    /**
     * Returns a hash value for this geodetic datum. {@linkplain #getName Name},
     * {@linkplain #getRemarks remarks} and the like are not taken in account. In
     * other words, two geodetic datums will return the same hash value if they
     * are equal in the sense of
     * <code>{@link #equals equals}(AbstractIdentifiedObject, <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        int code = (int)serialVersionUID ^
            37*(super        .hashCode() ^
            37*(ellipsoid    .hashCode() ^
            37*(primeMeridian.hashCode())));
        return code;
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name, which is "DATUM"
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        // Do NOT invokes the super-class method, because
        // horizontal datum do not write the datum type.
        formatter.append(ellipsoid);
        if (bursaWolf != null) {
            for (int i=0; i<bursaWolf.length; i++) {
                final BursaWolfParameters transformation = bursaWolf[i];
                if (isWGS84(transformation.targetDatum)) {
                    formatter.append(transformation);
                    break;
                }
            }
        }
        return "DATUM";
    }
}
