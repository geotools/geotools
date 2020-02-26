/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.identity;

import org.opengis.annotation.XmlElement;

/**
 * Feature identifier.
 *
 * <p>Features are identified as strings.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @author Justin Deoliveira (The Open Planning Project)
 * @since GeoAPI 2.0
 */
@XmlElement("FeatureId")
public interface FeatureId extends Identifier {

    public static final char VERSION_SEPARATOR = '@';

    /** The identifier value, which is a string. */
    @XmlElement("fid")
    String getID();

    //
    // Query and Test methods used to test a feature or record
    //
    /**
     * Evaluates the identifer value against the given feature.
     *
     * @param feature The feature to be tested.
     * @return {@code true} if a match, otherwise {@code false}.
     */
    boolean matches(Object feature);

    /**
     * Check if the provided FeatureId is an exact match (including any optional version
     * information).
     *
     * @return true if this is an exact match (including any optional version information)
     */
    boolean equalsExact(FeatureId id);

    /**
     * Checks if the provided FeatureId reflects the same feature.
     *
     * <p>This comparison does not compare any optional version information.
     *
     * @return true if both identifiers describe the same feature (does not compare version
     *     information).
     */
    boolean equalsFID(FeatureId id);

    //
    // Filter 2.0 Versioning Support
    //
    // The following methods are optional and are used as part of the FeatureId data
    // structure to report any available version information associated with a resoruce.
    //
    /**
     * id of the resource that shall be selected by the predicate.
     *
     * <p>Equals to {@link #getID()} if no feature version is provided, or {@code getID() + "@" +
     * getFeatureVersion()} if {@code getFeatureVersion() != null}
     *
     * <p>If an implementation that references this International Standard supports versioning, the
     * rid shall be a system generated hash containing a logical resource identifier and a version
     * number. The specific details of the hash are implementation dependant and shall be opaque to
     * a client
     *
     * <p>If versioning is not supported, the same value than {@link FeatureId#getID()} shall be
     * returned.
     *
     * @return Resource identifier made up of FID (combined with FeatureVersion if available)
     */
    @XmlElement("rid")
    String getRid();

    /**
     * previousRid attribute may be used, in implementations that support versioning, to report the
     * previous identifier of a resource.
     *
     * @return Previous rid if available; or {@code null}
     */
    @XmlElement("previousRid")
    String getPreviousRid();

    /**
     * Version identifier for the feature instance, may be {@code null}
     *
     * @see #getID()
     * @see #getRid()
     * @return Optional version information; {@code null} if not available
     */
    String getFeatureVersion();
}
