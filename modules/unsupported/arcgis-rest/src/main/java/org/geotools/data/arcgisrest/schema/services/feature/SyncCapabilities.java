
package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncCapabilities {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsAsync")
    @Expose
    private Boolean supportsAsync;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsRegisteringExistingData")
    @Expose
    private Boolean supportsRegisteringExistingData;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsSyncDirectionControl")
    @Expose
    private Boolean supportsSyncDirectionControl;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsPerLayerSync")
    @Expose
    private Boolean supportsPerLayerSync;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsPerReplicaSync")
    @Expose
    private Boolean supportsPerReplicaSync;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsSyncModelNone")
    @Expose
    private Boolean supportsSyncModelNone;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsRollbackOnFailure")
    @Expose
    private Boolean supportsRollbackOnFailure;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsAttachmentsSyncDirection")
    @Expose
    private Boolean supportsAttachmentsSyncDirection;

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsAsync() {
        return supportsAsync;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsAsync(Boolean supportsAsync) {
        this.supportsAsync = supportsAsync;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsRegisteringExistingData() {
        return supportsRegisteringExistingData;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsRegisteringExistingData(Boolean supportsRegisteringExistingData) {
        this.supportsRegisteringExistingData = supportsRegisteringExistingData;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsSyncDirectionControl() {
        return supportsSyncDirectionControl;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsSyncDirectionControl(Boolean supportsSyncDirectionControl) {
        this.supportsSyncDirectionControl = supportsSyncDirectionControl;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsPerLayerSync() {
        return supportsPerLayerSync;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsPerLayerSync(Boolean supportsPerLayerSync) {
        this.supportsPerLayerSync = supportsPerLayerSync;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsPerReplicaSync() {
        return supportsPerReplicaSync;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsPerReplicaSync(Boolean supportsPerReplicaSync) {
        this.supportsPerReplicaSync = supportsPerReplicaSync;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsSyncModelNone() {
        return supportsSyncModelNone;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsSyncModelNone(Boolean supportsSyncModelNone) {
        this.supportsSyncModelNone = supportsSyncModelNone;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsRollbackOnFailure() {
        return supportsRollbackOnFailure;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsRollbackOnFailure(Boolean supportsRollbackOnFailure) {
        this.supportsRollbackOnFailure = supportsRollbackOnFailure;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getSupportsAttachmentsSyncDirection() {
        return supportsAttachmentsSyncDirection;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupportsAttachmentsSyncDirection(Boolean supportsAttachmentsSyncDirection) {
        this.supportsAttachmentsSyncDirection = supportsAttachmentsSyncDirection;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SyncCapabilities.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("supportsAsync");
        sb.append('=');
        sb.append(((this.supportsAsync == null)?"<null>":this.supportsAsync));
        sb.append(',');
        sb.append("supportsRegisteringExistingData");
        sb.append('=');
        sb.append(((this.supportsRegisteringExistingData == null)?"<null>":this.supportsRegisteringExistingData));
        sb.append(',');
        sb.append("supportsSyncDirectionControl");
        sb.append('=');
        sb.append(((this.supportsSyncDirectionControl == null)?"<null>":this.supportsSyncDirectionControl));
        sb.append(',');
        sb.append("supportsPerLayerSync");
        sb.append('=');
        sb.append(((this.supportsPerLayerSync == null)?"<null>":this.supportsPerLayerSync));
        sb.append(',');
        sb.append("supportsPerReplicaSync");
        sb.append('=');
        sb.append(((this.supportsPerReplicaSync == null)?"<null>":this.supportsPerReplicaSync));
        sb.append(',');
        sb.append("supportsSyncModelNone");
        sb.append('=');
        sb.append(((this.supportsSyncModelNone == null)?"<null>":this.supportsSyncModelNone));
        sb.append(',');
        sb.append("supportsRollbackOnFailure");
        sb.append('=');
        sb.append(((this.supportsRollbackOnFailure == null)?"<null>":this.supportsRollbackOnFailure));
        sb.append(',');
        sb.append("supportsAttachmentsSyncDirection");
        sb.append('=');
        sb.append(((this.supportsAttachmentsSyncDirection == null)?"<null>":this.supportsAttachmentsSyncDirection));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.supportsAsync == null)? 0 :this.supportsAsync.hashCode()));
        result = ((result* 31)+((this.supportsSyncDirectionControl == null)? 0 :this.supportsSyncDirectionControl.hashCode()));
        result = ((result* 31)+((this.supportsRegisteringExistingData == null)? 0 :this.supportsRegisteringExistingData.hashCode()));
        result = ((result* 31)+((this.supportsAttachmentsSyncDirection == null)? 0 :this.supportsAttachmentsSyncDirection.hashCode()));
        result = ((result* 31)+((this.supportsPerLayerSync == null)? 0 :this.supportsPerLayerSync.hashCode()));
        result = ((result* 31)+((this.supportsSyncModelNone == null)? 0 :this.supportsSyncModelNone.hashCode()));
        result = ((result* 31)+((this.supportsRollbackOnFailure == null)? 0 :this.supportsRollbackOnFailure.hashCode()));
        result = ((result* 31)+((this.supportsPerReplicaSync == null)? 0 :this.supportsPerReplicaSync.hashCode()));
        return result;
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
        return (((((((((this.supportsAsync == rhs.supportsAsync)||((this.supportsAsync!= null)&&this.supportsAsync.equals(rhs.supportsAsync)))&&((this.supportsSyncDirectionControl == rhs.supportsSyncDirectionControl)||((this.supportsSyncDirectionControl!= null)&&this.supportsSyncDirectionControl.equals(rhs.supportsSyncDirectionControl))))&&((this.supportsRegisteringExistingData == rhs.supportsRegisteringExistingData)||((this.supportsRegisteringExistingData!= null)&&this.supportsRegisteringExistingData.equals(rhs.supportsRegisteringExistingData))))&&((this.supportsAttachmentsSyncDirection == rhs.supportsAttachmentsSyncDirection)||((this.supportsAttachmentsSyncDirection!= null)&&this.supportsAttachmentsSyncDirection.equals(rhs.supportsAttachmentsSyncDirection))))&&((this.supportsPerLayerSync == rhs.supportsPerLayerSync)||((this.supportsPerLayerSync!= null)&&this.supportsPerLayerSync.equals(rhs.supportsPerLayerSync))))&&((this.supportsSyncModelNone == rhs.supportsSyncModelNone)||((this.supportsSyncModelNone!= null)&&this.supportsSyncModelNone.equals(rhs.supportsSyncModelNone))))&&((this.supportsRollbackOnFailure == rhs.supportsRollbackOnFailure)||((this.supportsRollbackOnFailure!= null)&&this.supportsRollbackOnFailure.equals(rhs.supportsRollbackOnFailure))))&&((this.supportsPerReplicaSync == rhs.supportsPerReplicaSync)||((this.supportsPerReplicaSync!= null)&&this.supportsPerReplicaSync.equals(rhs.supportsPerReplicaSync))));
    }

}
