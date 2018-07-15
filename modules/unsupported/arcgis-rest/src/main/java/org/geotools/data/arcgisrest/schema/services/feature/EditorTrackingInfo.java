package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    /**
     * (Required)
     *
     * @return The enableEditorTracking
     */
    public Boolean getEnableEditorTracking() {
        return enableEditorTracking;
    }

    /**
     * (Required)
     *
     * @param enableEditorTracking The enableEditorTracking
     */
    public void setEnableEditorTracking(Boolean enableEditorTracking) {
        this.enableEditorTracking = enableEditorTracking;
    }

    /**
     * (Required)
     *
     * @return The enableOwnershipAccessControl
     */
    public Boolean getEnableOwnershipAccessControl() {
        return enableOwnershipAccessControl;
    }

    /**
     * (Required)
     *
     * @param enableOwnershipAccessControl The enableOwnershipAccessControl
     */
    public void setEnableOwnershipAccessControl(Boolean enableOwnershipAccessControl) {
        this.enableOwnershipAccessControl = enableOwnershipAccessControl;
    }

    /**
     * (Required)
     *
     * @return The allowOthersToQuery
     */
    public Boolean getAllowOthersToQuery() {
        return allowOthersToQuery;
    }

    /**
     * (Required)
     *
     * @param allowOthersToQuery The allowOthersToQuery
     */
    public void setAllowOthersToQuery(Boolean allowOthersToQuery) {
        this.allowOthersToQuery = allowOthersToQuery;
    }

    /**
     * (Required)
     *
     * @return The allowOthersToUpdate
     */
    public Boolean getAllowOthersToUpdate() {
        return allowOthersToUpdate;
    }

    /**
     * (Required)
     *
     * @param allowOthersToUpdate The allowOthersToUpdate
     */
    public void setAllowOthersToUpdate(Boolean allowOthersToUpdate) {
        this.allowOthersToUpdate = allowOthersToUpdate;
    }

    /**
     * (Required)
     *
     * @return The allowOthersToDelete
     */
    public Boolean getAllowOthersToDelete() {
        return allowOthersToDelete;
    }

    /**
     * (Required)
     *
     * @param allowOthersToDelete The allowOthersToDelete
     */
    public void setAllowOthersToDelete(Boolean allowOthersToDelete) {
        this.allowOthersToDelete = allowOthersToDelete;
    }

    /**
     * (Required)
     *
     * @return The allowAnonymousToUpdate
     */
    public Boolean getAllowAnonymousToUpdate() {
        return allowAnonymousToUpdate;
    }

    /**
     * (Required)
     *
     * @param allowAnonymousToUpdate The allowAnonymousToUpdate
     */
    public void setAllowAnonymousToUpdate(Boolean allowAnonymousToUpdate) {
        this.allowAnonymousToUpdate = allowAnonymousToUpdate;
    }

    /**
     * (Required)
     *
     * @return The allowAnonymousToDelete
     */
    public Boolean getAllowAnonymousToDelete() {
        return allowAnonymousToDelete;
    }

    /**
     * (Required)
     *
     * @param allowAnonymousToDelete The allowAnonymousToDelete
     */
    public void setAllowAnonymousToDelete(Boolean allowAnonymousToDelete) {
        this.allowAnonymousToDelete = allowAnonymousToDelete;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(enableEditorTracking)
                .append(enableOwnershipAccessControl)
                .append(allowOthersToQuery)
                .append(allowOthersToUpdate)
                .append(allowOthersToDelete)
                .append(allowAnonymousToUpdate)
                .append(allowAnonymousToDelete)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(enableEditorTracking, rhs.enableEditorTracking)
                .append(enableOwnershipAccessControl, rhs.enableOwnershipAccessControl)
                .append(allowOthersToQuery, rhs.allowOthersToQuery)
                .append(allowOthersToUpdate, rhs.allowOthersToUpdate)
                .append(allowOthersToDelete, rhs.allowOthersToDelete)
                .append(allowAnonymousToUpdate, rhs.allowAnonymousToUpdate)
                .append(allowAnonymousToDelete, rhs.allowAnonymousToDelete)
                .isEquals();
    }
}
