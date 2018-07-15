package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SyncCapabilities {

    /** (Required) */
    @SerializedName("supportsAsync")
    @Expose
    private Boolean supportsAsync;
    /** (Required) */
    @SerializedName("supportsRegisteringExistingData")
    @Expose
    private Boolean supportsRegisteringExistingData;
    /** (Required) */
    @SerializedName("supportsSyncDirectionControl")
    @Expose
    private Boolean supportsSyncDirectionControl;
    /** (Required) */
    @SerializedName("supportsPerLayerSync")
    @Expose
    private Boolean supportsPerLayerSync;
    /** (Required) */
    @SerializedName("supportsPerReplicaSync")
    @Expose
    private Boolean supportsPerReplicaSync;
    /** (Required) */
    @SerializedName("supportsSyncModelNone")
    @Expose
    private Boolean supportsSyncModelNone;
    /** (Required) */
    @SerializedName("supportsRollbackOnFailure")
    @Expose
    private Boolean supportsRollbackOnFailure;
    /** (Required) */
    @SerializedName("supportsAttachmentsSyncDirection")
    @Expose
    private Boolean supportsAttachmentsSyncDirection;

    /**
     * (Required)
     *
     * @return The supportsAsync
     */
    public Boolean getSupportsAsync() {
        return supportsAsync;
    }

    /**
     * (Required)
     *
     * @param supportsAsync The supportsAsync
     */
    public void setSupportsAsync(Boolean supportsAsync) {
        this.supportsAsync = supportsAsync;
    }

    /**
     * (Required)
     *
     * @return The supportsRegisteringExistingData
     */
    public Boolean getSupportsRegisteringExistingData() {
        return supportsRegisteringExistingData;
    }

    /**
     * (Required)
     *
     * @param supportsRegisteringExistingData The supportsRegisteringExistingData
     */
    public void setSupportsRegisteringExistingData(Boolean supportsRegisteringExistingData) {
        this.supportsRegisteringExistingData = supportsRegisteringExistingData;
    }

    /**
     * (Required)
     *
     * @return The supportsSyncDirectionControl
     */
    public Boolean getSupportsSyncDirectionControl() {
        return supportsSyncDirectionControl;
    }

    /**
     * (Required)
     *
     * @param supportsSyncDirectionControl The supportsSyncDirectionControl
     */
    public void setSupportsSyncDirectionControl(Boolean supportsSyncDirectionControl) {
        this.supportsSyncDirectionControl = supportsSyncDirectionControl;
    }

    /**
     * (Required)
     *
     * @return The supportsPerLayerSync
     */
    public Boolean getSupportsPerLayerSync() {
        return supportsPerLayerSync;
    }

    /**
     * (Required)
     *
     * @param supportsPerLayerSync The supportsPerLayerSync
     */
    public void setSupportsPerLayerSync(Boolean supportsPerLayerSync) {
        this.supportsPerLayerSync = supportsPerLayerSync;
    }

    /**
     * (Required)
     *
     * @return The supportsPerReplicaSync
     */
    public Boolean getSupportsPerReplicaSync() {
        return supportsPerReplicaSync;
    }

    /**
     * (Required)
     *
     * @param supportsPerReplicaSync The supportsPerReplicaSync
     */
    public void setSupportsPerReplicaSync(Boolean supportsPerReplicaSync) {
        this.supportsPerReplicaSync = supportsPerReplicaSync;
    }

    /**
     * (Required)
     *
     * @return The supportsSyncModelNone
     */
    public Boolean getSupportsSyncModelNone() {
        return supportsSyncModelNone;
    }

    /**
     * (Required)
     *
     * @param supportsSyncModelNone The supportsSyncModelNone
     */
    public void setSupportsSyncModelNone(Boolean supportsSyncModelNone) {
        this.supportsSyncModelNone = supportsSyncModelNone;
    }

    /**
     * (Required)
     *
     * @return The supportsRollbackOnFailure
     */
    public Boolean getSupportsRollbackOnFailure() {
        return supportsRollbackOnFailure;
    }

    /**
     * (Required)
     *
     * @param supportsRollbackOnFailure The supportsRollbackOnFailure
     */
    public void setSupportsRollbackOnFailure(Boolean supportsRollbackOnFailure) {
        this.supportsRollbackOnFailure = supportsRollbackOnFailure;
    }

    /**
     * (Required)
     *
     * @return The supportsAttachmentsSyncDirection
     */
    public Boolean getSupportsAttachmentsSyncDirection() {
        return supportsAttachmentsSyncDirection;
    }

    /**
     * (Required)
     *
     * @param supportsAttachmentsSyncDirection The supportsAttachmentsSyncDirection
     */
    public void setSupportsAttachmentsSyncDirection(Boolean supportsAttachmentsSyncDirection) {
        this.supportsAttachmentsSyncDirection = supportsAttachmentsSyncDirection;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(supportsAsync)
                .append(supportsRegisteringExistingData)
                .append(supportsSyncDirectionControl)
                .append(supportsPerLayerSync)
                .append(supportsPerReplicaSync)
                .append(supportsSyncModelNone)
                .append(supportsRollbackOnFailure)
                .append(supportsAttachmentsSyncDirection)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SyncCapabilities) == false) {
            return false;
        }
        SyncCapabilities rhs = ((SyncCapabilities) other);
        return new EqualsBuilder()
                .append(supportsAsync, rhs.supportsAsync)
                .append(supportsRegisteringExistingData, rhs.supportsRegisteringExistingData)
                .append(supportsSyncDirectionControl, rhs.supportsSyncDirectionControl)
                .append(supportsPerLayerSync, rhs.supportsPerLayerSync)
                .append(supportsPerReplicaSync, rhs.supportsPerReplicaSync)
                .append(supportsSyncModelNone, rhs.supportsSyncModelNone)
                .append(supportsRollbackOnFailure, rhs.supportsRollbackOnFailure)
                .append(supportsAttachmentsSyncDirection, rhs.supportsAttachmentsSyncDirection)
                .isEquals();
    }
}
