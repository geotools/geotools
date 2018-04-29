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

import java.util.Date;
import org.opengis.annotation.XmlElement;

/**
 * Resource identifier as per FES 2.0.
 *
 * <p>Please note this is a query object for use with the Filter <b>Id</b> filter as shown:
 *
 * <pre>Filter filter = filterFactory.id(
 *    ff.featureId("CITY.123"),
 *    ff.resourceId("CITY.123",Version.Action.PREVIOUS) );</pre>
 *
 * In cases where a plain FetureId is used for lookup it is understood to refer to
 * Version.Action.LAST.
 *
 * <p>If an implementation that references this International Standard does not support versioning,
 * any value specified for the attributes {@link #getPreviousRid() previousRid}, {@link
 * #getVersion() version}, {@link #getStartTime() startTime}, and {@link #getEndTime() endTime}
 * shall be ignored and the predicate shall always select the single version that is available.
 */
@XmlElement("ResourceId")
public interface ResourceId extends FeatureId {

    /**
     * Used to navigate versions of a resource.
     *
     * <p>
     *
     * @return Version based resource query; non {@code null} but possibly {@link Version#isEmpty()
     *     empty} if used a date range query or asked for a specific feature id + version id
     */
    @XmlElement("version")
    Version getVersion();

    /**
     * Used to select versions of a resource between start and end time.
     *
     * @return start time for a time based query; or {@code null} if using version or an end time
     *     was provided but the start time is unconstrained TODO: consider using an
     *     org.geotools.util.Range<Date> instead of both start and end time?
     */
    @XmlElement("startTime")
    Date getStartTime();

    /**
     * Used to select versions of a resource between start and end time.
     *
     * @return end time for a time based query; or {@code null} if using version or an start time
     *     was provided but the end time is unconstrained TODO: consider using an
     *     org.geotools.util.Range<Date> instead of both start and end time?
     */
    @XmlElement("endTime")
    Date getEndTime();
}
