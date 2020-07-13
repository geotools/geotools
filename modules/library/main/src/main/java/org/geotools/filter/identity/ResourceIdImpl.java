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
package org.geotools.filter.identity;

import java.util.Date;
import org.geotools.util.Utilities;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.ResourceId;
import org.opengis.filter.identity.Version;

/**
 * Implementation of {@link ResourceId} used for Query.
 *
 * <p>This class is mutable under one condition only; during a commit a datastore can update the
 * internal fid to reflect the real identify assigned by the database or wfs.
 *
 * <p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 8.0
 */
public class ResourceIdImpl extends FeatureIdVersionedImpl implements ResourceId {

    private Date startTime;
    private Date endTime;
    private long version;

    /**
     * Obtain a ResourceId based on version lookup.
     *
     * @param fid feature being queried
     * @param featureVersion version used as a reference point
     * @param version scope of version based query (first, last, index, all, date, etc...)
     */
    public ResourceIdImpl(String fid, String featureVersion, Version version) {
        super(fid, featureVersion, null);
        setVersion(version);
    }

    /**
     * Obtain a ResourceId that represents an explicit request for feature id and feature version
     * (essentially the quivalent of {@link FeatureId})
     */
    public ResourceIdImpl(String fid, String featureVersion) {
        this(fid, featureVersion, (Version) null);
    }

    /**
     * Date range constructor for a feature id; none or one of {@code start} and {@code end} can be
     * {@code null}, making for an unconstrained date range at either of the ends.
     *
     * @param fid feature id, non null;
     * @param start lower end of the time range, inclusive, or {@code null} only if {@code end !=
     *     null}
     * @param end upper end of the time range, inclusive, or {@code null} only if {@code start !=
     *     null}
     */
    public ResourceIdImpl(String fid, Date start, Date end) {
        this(fid, (String) null, (Version) null);
        if (start == null && end == null) {
            throw new NullPointerException(
                    "At least one of start and end time are required for a lookup based on a date range");
        }
        this.startTime = start;
        this.endTime = end;
    }

    public void setRid(String rid) {
        setID(rid);
    }

    public void setPreviousRid(final String previousRid) {
        this.previousRid = previousRid;
    }

    public void setVersion(final Version version) {
        if (version == null) {
            this.version = new Version().union();
        } else {
            this.version = version.union();
        }
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public Version getVersion() {
        return Version.valueOf(version);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ResourceId)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final ResourceId o = (ResourceId) obj;
        return Utilities.equals(featureVersion, o.getFeatureVersion())
                && Utilities.equals(previousRid, o.getPreviousRid())
                && Utilities.equals(version, o.getVersion())
                && Utilities.equals(startTime, o.getStartTime())
                && Utilities.equals(endTime, o.getEndTime());
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = Utilities.hash(previousRid, hash);
        hash = Utilities.hash(version, hash);
        hash = Utilities.hash(startTime, hash);
        hash = Utilities.hash(endTime, hash);
        return hash;
    }
}
