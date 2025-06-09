/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
 */

package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class EditorTrackingInfo {

    /** (Required) */
    @SerializedName("enableEditorTracking")
    @Expose
    private Boolean enableEditorTracking;
    /** (Required) */
    @SerializedName("enableOwnershipAccessControl")
    @Expose
    private Boolean enableOwnershipAccessControl;
    /** (Required) */
    @SerializedName("allowOthersToQuery")
    @Expose
    private Boolean allowOthersToQuery;
    /** (Required) */
    @SerializedName("allowOthersToUpdate")
    @Expose
    private Boolean allowOthersToUpdate;
    /** (Required) */
    @SerializedName("allowOthersToDelete")
    @Expose
    private Boolean allowOthersToDelete;
    /** (Required) */
    @SerializedName("allowAnonymousToUpdate")
    @Expose
    private Boolean allowAnonymousToUpdate;
    /** (Required) */
    @SerializedName("allowAnonymousToDelete")
    @Expose
    private Boolean allowAnonymousToDelete;

    /** (Required) */
    public Boolean getEnableEditorTracking() {
        return enableEditorTracking;
    }

    /** (Required) */
    public void setEnableEditorTracking(Boolean enableEditorTracking) {
        this.enableEditorTracking = enableEditorTracking;
    }

    /** (Required) */
    public Boolean getEnableOwnershipAccessControl() {
        return enableOwnershipAccessControl;
    }

    /** (Required) */
    public void setEnableOwnershipAccessControl(Boolean enableOwnershipAccessControl) {
        this.enableOwnershipAccessControl = enableOwnershipAccessControl;
    }

    /** (Required) */
    public Boolean getAllowOthersToQuery() {
        return allowOthersToQuery;
    }

    /** (Required) */
    public void setAllowOthersToQuery(Boolean allowOthersToQuery) {
        this.allowOthersToQuery = allowOthersToQuery;
    }

    /** (Required) */
    public Boolean getAllowOthersToUpdate() {
        return allowOthersToUpdate;
    }

    /** (Required) */
    public void setAllowOthersToUpdate(Boolean allowOthersToUpdate) {
        this.allowOthersToUpdate = allowOthersToUpdate;
    }

    /** (Required) */
    public Boolean getAllowOthersToDelete() {
        return allowOthersToDelete;
    }

    /** (Required) */
    public void setAllowOthersToDelete(Boolean allowOthersToDelete) {
        this.allowOthersToDelete = allowOthersToDelete;
    }

    /** (Required) */
    public Boolean getAllowAnonymousToUpdate() {
        return allowAnonymousToUpdate;
    }

    /** (Required) */
    public void setAllowAnonymousToUpdate(Boolean allowAnonymousToUpdate) {
        this.allowAnonymousToUpdate = allowAnonymousToUpdate;
    }

    /** (Required) */
    public Boolean getAllowAnonymousToDelete() {
        return allowAnonymousToDelete;
    }

    /** (Required) */
    public void setAllowAnonymousToDelete(Boolean allowAnonymousToDelete) {
        this.allowAnonymousToDelete = allowAnonymousToDelete;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(EditorTrackingInfo.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("enableEditorTracking");
        sb.append('=');
        sb.append(((this.enableEditorTracking == null) ? "<null>" : this.enableEditorTracking));
        sb.append(',');
        sb.append("enableOwnershipAccessControl");
        sb.append('=');
        sb.append(((this.enableOwnershipAccessControl == null) ? "<null>" : this.enableOwnershipAccessControl));
        sb.append(',');
        sb.append("allowOthersToQuery");
        sb.append('=');
        sb.append(((this.allowOthersToQuery == null) ? "<null>" : this.allowOthersToQuery));
        sb.append(',');
        sb.append("allowOthersToUpdate");
        sb.append('=');
        sb.append(((this.allowOthersToUpdate == null) ? "<null>" : this.allowOthersToUpdate));
        sb.append(',');
        sb.append("allowOthersToDelete");
        sb.append('=');
        sb.append(((this.allowOthersToDelete == null) ? "<null>" : this.allowOthersToDelete));
        sb.append(',');
        sb.append("allowAnonymousToUpdate");
        sb.append('=');
        sb.append(((this.allowAnonymousToUpdate == null) ? "<null>" : this.allowAnonymousToUpdate));
        sb.append(',');
        sb.append("allowAnonymousToDelete");
        sb.append('=');
        sb.append(((this.allowAnonymousToDelete == null) ? "<null>" : this.allowAnonymousToDelete));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.allowOthersToUpdate == null) ? 0 : this.allowOthersToUpdate.hashCode()));
        result = ((result * 31) + ((this.allowAnonymousToUpdate == null) ? 0 : this.allowAnonymousToUpdate.hashCode()));
        result = ((result * 31) + ((this.enableEditorTracking == null) ? 0 : this.enableEditorTracking.hashCode()));
        result = ((result * 31) + ((this.allowOthersToQuery == null) ? 0 : this.allowOthersToQuery.hashCode()));
        result = ((result * 31) + ((this.allowOthersToDelete == null) ? 0 : this.allowOthersToDelete.hashCode()));
        result = ((result * 31)
                + ((this.enableOwnershipAccessControl == null) ? 0 : this.enableOwnershipAccessControl.hashCode()));
        result = ((result * 31) + ((this.allowAnonymousToDelete == null) ? 0 : this.allowAnonymousToDelete.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EditorTrackingInfo) == false) {
            return false;
        }
        EditorTrackingInfo rhs = ((EditorTrackingInfo) other);
        return Objects.equals(this.allowOthersToUpdate, rhs.allowOthersToUpdate)
                && Objects.equals(this.allowAnonymousToUpdate, rhs.allowAnonymousToUpdate)
                && Objects.equals(this.enableEditorTracking, rhs.enableEditorTracking)
                && Objects.equals(this.allowOthersToQuery, rhs.allowOthersToQuery)
                && Objects.equals(this.allowOthersToDelete, rhs.allowOthersToDelete)
                && Objects.equals(this.enableOwnershipAccessControl, rhs.enableOwnershipAccessControl)
                && Objects.equals(this.allowAnonymousToDelete, rhs.allowAnonymousToDelete);
    }
}
