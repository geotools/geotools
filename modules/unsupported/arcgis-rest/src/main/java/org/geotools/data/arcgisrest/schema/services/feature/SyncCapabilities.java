
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

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsAsync() {
        return supportsAsync;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsAsync(Boolean supportsAsync) {
        this.supportsAsync = supportsAsync;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsRegisteringExistingData() {
        return supportsRegisteringExistingData;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsRegisteringExistingData(Boolean supportsRegisteringExistingData) {
        this.supportsRegisteringExistingData = supportsRegisteringExistingData;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsSyncDirectionControl() {
        return supportsSyncDirectionControl;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsSyncDirectionControl(Boolean supportsSyncDirectionControl) {
        this.supportsSyncDirectionControl = supportsSyncDirectionControl;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsPerLayerSync() {
        return supportsPerLayerSync;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsPerLayerSync(Boolean supportsPerLayerSync) {
        this.supportsPerLayerSync = supportsPerLayerSync;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsPerReplicaSync() {
        return supportsPerReplicaSync;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsPerReplicaSync(Boolean supportsPerReplicaSync) {
        this.supportsPerReplicaSync = supportsPerReplicaSync;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsSyncModelNone() {
        return supportsSyncModelNone;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsSyncModelNone(Boolean supportsSyncModelNone) {
        this.supportsSyncModelNone = supportsSyncModelNone;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsRollbackOnFailure() {
        return supportsRollbackOnFailure;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsRollbackOnFailure(Boolean supportsRollbackOnFailure) {
        this.supportsRollbackOnFailure = supportsRollbackOnFailure;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getSupportsAttachmentsSyncDirection() {
        return supportsAttachmentsSyncDirection;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setSupportsAttachmentsSyncDirection(Boolean supportsAttachmentsSyncDirection) {
        this.supportsAttachmentsSyncDirection = supportsAttachmentsSyncDirection;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
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
=======
        sb.append(SyncCapabilities.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("supportsAsync");
        sb.append('=');
        sb.append(((this.supportsAsync == null) ? "<null>" : this.supportsAsync));
        sb.append(',');
        sb.append("supportsRegisteringExistingData");
        sb.append('=');
        sb.append(
                ((this.supportsRegisteringExistingData == null)
                        ? "<null>"
                        : this.supportsRegisteringExistingData));
        sb.append(',');
        sb.append("supportsSyncDirectionControl");
        sb.append('=');
        sb.append(
                ((this.supportsSyncDirectionControl == null)
                        ? "<null>"
                        : this.supportsSyncDirectionControl));
        sb.append(',');
        sb.append("supportsPerLayerSync");
        sb.append('=');
        sb.append(((this.supportsPerLayerSync == null) ? "<null>" : this.supportsPerLayerSync));
        sb.append(',');
        sb.append("supportsPerReplicaSync");
        sb.append('=');
        sb.append(((this.supportsPerReplicaSync == null) ? "<null>" : this.supportsPerReplicaSync));
        sb.append(',');
        sb.append("supportsSyncModelNone");
        sb.append('=');
        sb.append(((this.supportsSyncModelNone == null) ? "<null>" : this.supportsSyncModelNone));
        sb.append(',');
        sb.append("supportsRollbackOnFailure");
        sb.append('=');
        sb.append(
                ((this.supportsRollbackOnFailure == null)
                        ? "<null>"
                        : this.supportsRollbackOnFailure));
        sb.append(',');
        sb.append("supportsAttachmentsSyncDirection");
        sb.append('=');
        sb.append(
                ((this.supportsAttachmentsSyncDirection == null)
                        ? "<null>"
                        : this.supportsAttachmentsSyncDirection));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
<<<<<<< HEAD
        result = ((result* 31)+((this.supportsAsync == null)? 0 :this.supportsAsync.hashCode()));
        result = ((result* 31)+((this.supportsSyncDirectionControl == null)? 0 :this.supportsSyncDirectionControl.hashCode()));
        result = ((result* 31)+((this.supportsRegisteringExistingData == null)? 0 :this.supportsRegisteringExistingData.hashCode()));
        result = ((result* 31)+((this.supportsAttachmentsSyncDirection == null)? 0 :this.supportsAttachmentsSyncDirection.hashCode()));
        result = ((result* 31)+((this.supportsPerLayerSync == null)? 0 :this.supportsPerLayerSync.hashCode()));
        result = ((result* 31)+((this.supportsSyncModelNone == null)? 0 :this.supportsSyncModelNone.hashCode()));
        result = ((result* 31)+((this.supportsRollbackOnFailure == null)? 0 :this.supportsRollbackOnFailure.hashCode()));
        result = ((result* 31)+((this.supportsPerReplicaSync == null)? 0 :this.supportsPerReplicaSync.hashCode()));
=======
        result =
                ((result * 31)
                        + ((this.supportsAsync == null) ? 0 : this.supportsAsync.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsSyncDirectionControl == null)
                                ? 0
                                : this.supportsSyncDirectionControl.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsRegisteringExistingData == null)
                                ? 0
                                : this.supportsRegisteringExistingData.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsAttachmentsSyncDirection == null)
                                ? 0
                                : this.supportsAttachmentsSyncDirection.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsPerLayerSync == null)
                                ? 0
                                : this.supportsPerLayerSync.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsSyncModelNone == null)
                                ? 0
                                : this.supportsSyncModelNone.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsRollbackOnFailure == null)
                                ? 0
                                : this.supportsRollbackOnFailure.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsPerReplicaSync == null)
                                ? 0
                                : this.supportsPerReplicaSync.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
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
<<<<<<< HEAD
        return (((((((((this.supportsAsync == rhs.supportsAsync)||((this.supportsAsync!= null)&&this.supportsAsync.equals(rhs.supportsAsync)))&&((this.supportsSyncDirectionControl == rhs.supportsSyncDirectionControl)||((this.supportsSyncDirectionControl!= null)&&this.supportsSyncDirectionControl.equals(rhs.supportsSyncDirectionControl))))&&((this.supportsRegisteringExistingData == rhs.supportsRegisteringExistingData)||((this.supportsRegisteringExistingData!= null)&&this.supportsRegisteringExistingData.equals(rhs.supportsRegisteringExistingData))))&&((this.supportsAttachmentsSyncDirection == rhs.supportsAttachmentsSyncDirection)||((this.supportsAttachmentsSyncDirection!= null)&&this.supportsAttachmentsSyncDirection.equals(rhs.supportsAttachmentsSyncDirection))))&&((this.supportsPerLayerSync == rhs.supportsPerLayerSync)||((this.supportsPerLayerSync!= null)&&this.supportsPerLayerSync.equals(rhs.supportsPerLayerSync))))&&((this.supportsSyncModelNone == rhs.supportsSyncModelNone)||((this.supportsSyncModelNone!= null)&&this.supportsSyncModelNone.equals(rhs.supportsSyncModelNone))))&&((this.supportsRollbackOnFailure == rhs.supportsRollbackOnFailure)||((this.supportsRollbackOnFailure!= null)&&this.supportsRollbackOnFailure.equals(rhs.supportsRollbackOnFailure))))&&((this.supportsPerReplicaSync == rhs.supportsPerReplicaSync)||((this.supportsPerReplicaSync!= null)&&this.supportsPerReplicaSync.equals(rhs.supportsPerReplicaSync))));
=======
        return (((((((((this.supportsAsync == rhs.supportsAsync)
                                                                        || ((this.supportsAsync
                                                                                        != null)
                                                                                && this
                                                                                        .supportsAsync
                                                                                        .equals(
                                                                                                rhs.supportsAsync)))
                                                                && ((this
                                                                                        .supportsSyncDirectionControl
                                                                                == rhs.supportsSyncDirectionControl)
                                                                        || ((this
                                                                                                .supportsSyncDirectionControl
                                                                                        != null)
                                                                                && this
                                                                                        .supportsSyncDirectionControl
                                                                                        .equals(
                                                                                                rhs.supportsSyncDirectionControl))))
                                                        && ((this.supportsRegisteringExistingData
                                                                        == rhs.supportsRegisteringExistingData)
                                                                || ((this
                                                                                        .supportsRegisteringExistingData
                                                                                != null)
                                                                        && this
                                                                                .supportsRegisteringExistingData
                                                                                .equals(
                                                                                        rhs.supportsRegisteringExistingData))))
                                                && ((this.supportsAttachmentsSyncDirection
                                                                == rhs.supportsAttachmentsSyncDirection)
                                                        || ((this.supportsAttachmentsSyncDirection
                                                                        != null)
                                                                && this
                                                                        .supportsAttachmentsSyncDirection
                                                                        .equals(
                                                                                rhs.supportsAttachmentsSyncDirection))))
                                        && ((this.supportsPerLayerSync == rhs.supportsPerLayerSync)
                                                || ((this.supportsPerLayerSync != null)
                                                        && this.supportsPerLayerSync.equals(
                                                                rhs.supportsPerLayerSync))))
                                && ((this.supportsSyncModelNone == rhs.supportsSyncModelNone)
                                        || ((this.supportsSyncModelNone != null)
                                                && this.supportsSyncModelNone.equals(
                                                        rhs.supportsSyncModelNone))))
                        && ((this.supportsRollbackOnFailure == rhs.supportsRollbackOnFailure)
                                || ((this.supportsRollbackOnFailure != null)
                                        && this.supportsRollbackOnFailure.equals(
                                                rhs.supportsRollbackOnFailure))))
                && ((this.supportsPerReplicaSync == rhs.supportsPerReplicaSync)
                        || ((this.supportsPerReplicaSync != null)
                                && this.supportsPerReplicaSync.equals(
                                        rhs.supportsPerReplicaSync))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
