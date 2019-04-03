
package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditorTrackingInfo {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("enableEditorTracking")
    @Expose
    private Boolean enableEditorTracking;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("enableOwnershipAccessControl")
    @Expose
    private Boolean enableOwnershipAccessControl;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("allowOthersToQuery")
    @Expose
    private Boolean allowOthersToQuery;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("allowOthersToUpdate")
    @Expose
    private Boolean allowOthersToUpdate;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("allowOthersToDelete")
    @Expose
    private Boolean allowOthersToDelete;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("allowAnonymousToUpdate")
    @Expose
    private Boolean allowAnonymousToUpdate;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("allowAnonymousToDelete")
    @Expose
    private Boolean allowAnonymousToDelete;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getEnableEditorTracking() {
        return enableEditorTracking;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setEnableEditorTracking(Boolean enableEditorTracking) {
        this.enableEditorTracking = enableEditorTracking;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getEnableOwnershipAccessControl() {
        return enableOwnershipAccessControl;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setEnableOwnershipAccessControl(Boolean enableOwnershipAccessControl) {
        this.enableOwnershipAccessControl = enableOwnershipAccessControl;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getAllowOthersToQuery() {
        return allowOthersToQuery;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setAllowOthersToQuery(Boolean allowOthersToQuery) {
        this.allowOthersToQuery = allowOthersToQuery;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getAllowOthersToUpdate() {
        return allowOthersToUpdate;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setAllowOthersToUpdate(Boolean allowOthersToUpdate) {
        this.allowOthersToUpdate = allowOthersToUpdate;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getAllowOthersToDelete() {
        return allowOthersToDelete;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setAllowOthersToDelete(Boolean allowOthersToDelete) {
        this.allowOthersToDelete = allowOthersToDelete;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getAllowAnonymousToUpdate() {
        return allowAnonymousToUpdate;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setAllowAnonymousToUpdate(Boolean allowAnonymousToUpdate) {
        this.allowAnonymousToUpdate = allowAnonymousToUpdate;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getAllowAnonymousToDelete() {
        return allowAnonymousToDelete;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setAllowAnonymousToDelete(Boolean allowAnonymousToDelete) {
        this.allowAnonymousToDelete = allowAnonymousToDelete;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
<<<<<<< HEAD
        sb.append(EditorTrackingInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("enableEditorTracking");
        sb.append('=');
        sb.append(((this.enableEditorTracking == null)?"<null>":this.enableEditorTracking));
        sb.append(',');
        sb.append("enableOwnershipAccessControl");
        sb.append('=');
        sb.append(((this.enableOwnershipAccessControl == null)?"<null>":this.enableOwnershipAccessControl));
        sb.append(',');
        sb.append("allowOthersToQuery");
        sb.append('=');
        sb.append(((this.allowOthersToQuery == null)?"<null>":this.allowOthersToQuery));
        sb.append(',');
        sb.append("allowOthersToUpdate");
        sb.append('=');
        sb.append(((this.allowOthersToUpdate == null)?"<null>":this.allowOthersToUpdate));
        sb.append(',');
        sb.append("allowOthersToDelete");
        sb.append('=');
        sb.append(((this.allowOthersToDelete == null)?"<null>":this.allowOthersToDelete));
        sb.append(',');
        sb.append("allowAnonymousToUpdate");
        sb.append('=');
        sb.append(((this.allowAnonymousToUpdate == null)?"<null>":this.allowAnonymousToUpdate));
        sb.append(',');
        sb.append("allowAnonymousToDelete");
        sb.append('=');
        sb.append(((this.allowAnonymousToDelete == null)?"<null>":this.allowAnonymousToDelete));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(EditorTrackingInfo.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
=======
        sb.append(EditorTrackingInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        sb.append("enableEditorTracking");
        sb.append('=');
        sb.append(((this.enableEditorTracking == null)?"<null>":this.enableEditorTracking));
        sb.append(',');
        sb.append("enableOwnershipAccessControl");
        sb.append('=');
        sb.append(((this.enableOwnershipAccessControl == null)?"<null>":this.enableOwnershipAccessControl));
        sb.append(',');
        sb.append("allowOthersToQuery");
        sb.append('=');
        sb.append(((this.allowOthersToQuery == null)?"<null>":this.allowOthersToQuery));
        sb.append(',');
        sb.append("allowOthersToUpdate");
        sb.append('=');
        sb.append(((this.allowOthersToUpdate == null)?"<null>":this.allowOthersToUpdate));
        sb.append(',');
        sb.append("allowOthersToDelete");
        sb.append('=');
        sb.append(((this.allowOthersToDelete == null)?"<null>":this.allowOthersToDelete));
        sb.append(',');
        sb.append("allowAnonymousToUpdate");
        sb.append('=');
        sb.append(((this.allowAnonymousToUpdate == null)?"<null>":this.allowAnonymousToUpdate));
        sb.append(',');
        sb.append("allowAnonymousToDelete");
        sb.append('=');
        sb.append(((this.allowAnonymousToDelete == null)?"<null>":this.allowAnonymousToDelete));
        sb.append(',');
<<<<<<< HEAD
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        result = ((result* 31)+((this.allowOthersToUpdate == null)? 0 :this.allowOthersToUpdate.hashCode()));
        result = ((result* 31)+((this.allowAnonymousToUpdate == null)? 0 :this.allowAnonymousToUpdate.hashCode()));
        result = ((result* 31)+((this.enableEditorTracking == null)? 0 :this.enableEditorTracking.hashCode()));
        result = ((result* 31)+((this.allowOthersToQuery == null)? 0 :this.allowOthersToQuery.hashCode()));
        result = ((result* 31)+((this.allowOthersToDelete == null)? 0 :this.allowOthersToDelete.hashCode()));
        result = ((result* 31)+((this.enableOwnershipAccessControl == null)? 0 :this.enableOwnershipAccessControl.hashCode()));
        result = ((result* 31)+((this.allowAnonymousToDelete == null)? 0 :this.allowAnonymousToDelete.hashCode()));
<<<<<<< HEAD
=======
        result =
                ((result * 31)
                        + ((this.allowOthersToUpdate == null)
                                ? 0
                                : this.allowOthersToUpdate.hashCode()));
        result =
                ((result * 31)
                        + ((this.allowAnonymousToUpdate == null)
                                ? 0
                                : this.allowAnonymousToUpdate.hashCode()));
        result =
                ((result * 31)
                        + ((this.enableEditorTracking == null)
                                ? 0
                                : this.enableEditorTracking.hashCode()));
        result =
                ((result * 31)
                        + ((this.allowOthersToQuery == null)
                                ? 0
                                : this.allowOthersToQuery.hashCode()));
        result =
                ((result * 31)
                        + ((this.allowOthersToDelete == null)
                                ? 0
                                : this.allowOthersToDelete.hashCode()));
        result =
                ((result * 31)
                        + ((this.enableOwnershipAccessControl == null)
                                ? 0
                                : this.enableOwnershipAccessControl.hashCode()));
        result =
                ((result * 31)
                        + ((this.allowAnonymousToDelete == null)
                                ? 0
                                : this.allowAnonymousToDelete.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
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
<<<<<<< HEAD
<<<<<<< HEAD
        return ((((((((this.allowOthersToUpdate == rhs.allowOthersToUpdate)||((this.allowOthersToUpdate!= null)&&this.allowOthersToUpdate.equals(rhs.allowOthersToUpdate)))&&((this.allowAnonymousToUpdate == rhs.allowAnonymousToUpdate)||((this.allowAnonymousToUpdate!= null)&&this.allowAnonymousToUpdate.equals(rhs.allowAnonymousToUpdate))))&&((this.enableEditorTracking == rhs.enableEditorTracking)||((this.enableEditorTracking!= null)&&this.enableEditorTracking.equals(rhs.enableEditorTracking))))&&((this.allowOthersToQuery == rhs.allowOthersToQuery)||((this.allowOthersToQuery!= null)&&this.allowOthersToQuery.equals(rhs.allowOthersToQuery))))&&((this.allowOthersToDelete == rhs.allowOthersToDelete)||((this.allowOthersToDelete!= null)&&this.allowOthersToDelete.equals(rhs.allowOthersToDelete))))&&((this.enableOwnershipAccessControl == rhs.enableOwnershipAccessControl)||((this.enableOwnershipAccessControl!= null)&&this.enableOwnershipAccessControl.equals(rhs.enableOwnershipAccessControl))))&&((this.allowAnonymousToDelete == rhs.allowAnonymousToDelete)||((this.allowAnonymousToDelete!= null)&&this.allowAnonymousToDelete.equals(rhs.allowAnonymousToDelete))));
=======
        return ((((((((this.allowOthersToUpdate == rhs.allowOthersToUpdate)
                                                                || ((this.allowOthersToUpdate
                                                                                != null)
                                                                        && this.allowOthersToUpdate
                                                                                .equals(
                                                                                        rhs.allowOthersToUpdate)))
                                                        && ((this.allowAnonymousToUpdate
                                                                        == rhs.allowAnonymousToUpdate)
                                                                || ((this.allowAnonymousToUpdate
                                                                                != null)
                                                                        && this
                                                                                .allowAnonymousToUpdate
                                                                                .equals(
                                                                                        rhs.allowAnonymousToUpdate))))
                                                && ((this.enableEditorTracking
                                                                == rhs.enableEditorTracking)
                                                        || ((this.enableEditorTracking != null)
                                                                && this.enableEditorTracking.equals(
                                                                        rhs.enableEditorTracking))))
                                        && ((this.allowOthersToQuery == rhs.allowOthersToQuery)
                                                || ((this.allowOthersToQuery != null)
                                                        && this.allowOthersToQuery.equals(
                                                                rhs.allowOthersToQuery))))
                                && ((this.allowOthersToDelete == rhs.allowOthersToDelete)
                                        || ((this.allowOthersToDelete != null)
                                                && this.allowOthersToDelete.equals(
                                                        rhs.allowOthersToDelete))))
                        && ((this.enableOwnershipAccessControl == rhs.enableOwnershipAccessControl)
                                || ((this.enableOwnershipAccessControl != null)
                                        && this.enableOwnershipAccessControl.equals(
                                                rhs.enableOwnershipAccessControl))))
                && ((this.allowAnonymousToDelete == rhs.allowAnonymousToDelete)
                        || ((this.allowAnonymousToDelete != null)
                                && this.allowAnonymousToDelete.equals(
                                        rhs.allowAnonymousToDelete))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        return ((((((((this.allowOthersToUpdate == rhs.allowOthersToUpdate)||((this.allowOthersToUpdate!= null)&&this.allowOthersToUpdate.equals(rhs.allowOthersToUpdate)))&&((this.allowAnonymousToUpdate == rhs.allowAnonymousToUpdate)||((this.allowAnonymousToUpdate!= null)&&this.allowAnonymousToUpdate.equals(rhs.allowAnonymousToUpdate))))&&((this.enableEditorTracking == rhs.enableEditorTracking)||((this.enableEditorTracking!= null)&&this.enableEditorTracking.equals(rhs.enableEditorTracking))))&&((this.allowOthersToQuery == rhs.allowOthersToQuery)||((this.allowOthersToQuery!= null)&&this.allowOthersToQuery.equals(rhs.allowOthersToQuery))))&&((this.allowOthersToDelete == rhs.allowOthersToDelete)||((this.allowOthersToDelete!= null)&&this.allowOthersToDelete.equals(rhs.allowOthersToDelete))))&&((this.enableOwnershipAccessControl == rhs.enableOwnershipAccessControl)||((this.enableOwnershipAccessControl!= null)&&this.enableOwnershipAccessControl.equals(rhs.enableOwnershipAccessControl))))&&((this.allowAnonymousToDelete == rhs.allowAnonymousToDelete)||((this.allowAnonymousToDelete!= null)&&this.allowAnonymousToDelete.equals(rhs.allowAnonymousToDelete))));
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
