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
package org.geotools.data.postgis;

import java.util.Collections;
import java.util.Set;

/**
 * Contains the three sets of feature ids that identify those changes occurred between two versions
 * in the versioned datastore
 * 
 * @author aaime
 * @since 2.4
 * 
 *
 *
 * @source $URL$
 */
public class ModifiedFeatureIds {
    RevisionInfo fromRevision;

    RevisionInfo toRevision;

    Set created;

    Set deleted;

    Set modified;

    public ModifiedFeatureIds(RevisionInfo fromRevision, RevisionInfo toRevision) {
        this(fromRevision, toRevision, Collections.EMPTY_SET, Collections.EMPTY_SET,
                Collections.EMPTY_SET);
    }

    public ModifiedFeatureIds(RevisionInfo fromRevision, RevisionInfo toRevision, Set created,
            Set deleted, Set modified) {
        this.fromRevision = fromRevision;
        this.toRevision = toRevision;
        this.deleted = deleted;
        this.created = created;
        this.modified = modified;
    }

    /**
     * Contains all ids for features that were deleted before endVersion (a feature may be both in
     * this set and in created if it was created and then deleted)
     * 
     * @return
     */
    public Set getDeleted() {
        return Collections.unmodifiableSet(deleted);
    }

    /**
     * Contains all ids for features that were created after startVersion (a feature may be both in
     * this set and in deleted if it was created and then deleted)
     * 
     * @return
     */
    public Set getCreated() {
        return Collections.unmodifiableSet(created);
    }

    /**
     * Contains all ids for features that were already there at startVersion, were modified before
     * endVersion and were not deleted.
     * 
     * @return
     */
    public Set getModified() {
        return Collections.unmodifiableSet(modified);
    }

    public String getToVersion() {
        return toRevision.getVersion();
    }

    public String getFromVersion() {
        return fromRevision.getVersion();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Diff between: " + fromRevision + " and " + toRevision).append("\n");
        sb.append("Created: ").append(created).append("\n");
        sb.append("Deleted: ").append(deleted).append("\n");
        sb.append("Modified: ").append(modified);
        return sb.toString();
    }

}
