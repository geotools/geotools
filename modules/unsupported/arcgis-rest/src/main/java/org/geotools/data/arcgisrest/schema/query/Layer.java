
package org.geotools.data.arcgisrest.schema.query;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
=======
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS

public class Layer {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("objectIdFieldName")
    @Expose
    private String objectIdFieldName;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("globalIdFieldName")
    @Expose
    private String globalIdFieldName;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("geometryType")
    @Expose
    private String geometryType;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("spatialReference")
    @Expose
    private SpatialReference spatialReference;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("fields")
    @Expose
    private List<Object> fields = new ArrayList<Object>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("features")
    @Expose
    private List<Object> features = new ArrayList<Object>();

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public String getObjectIdFieldName() {
        return objectIdFieldName;
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
    public void setObjectIdFieldName(String objectIdFieldName) {
        this.objectIdFieldName = objectIdFieldName;
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
    public String getGlobalIdFieldName() {
        return globalIdFieldName;
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
    public void setGlobalIdFieldName(String globalIdFieldName) {
        this.globalIdFieldName = globalIdFieldName;
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
    public String getGeometryType() {
        return geometryType;
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
    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
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
    public SpatialReference getSpatialReference() {
        return spatialReference;
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
    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
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
    public List<Object> getFields() {
        return fields;
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
    public void setFields(List<Object> fields) {
        this.fields = fields;
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
    public List<Object> getFeatures() {
        return features;
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
    public void setFeatures(List<Object> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
        sb.append(Layer.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("objectIdFieldName");
        sb.append('=');
        sb.append(((this.objectIdFieldName == null)?"<null>":this.objectIdFieldName));
        sb.append(',');
        sb.append("globalIdFieldName");
        sb.append('=');
        sb.append(((this.globalIdFieldName == null)?"<null>":this.globalIdFieldName));
        sb.append(',');
        sb.append("geometryType");
        sb.append('=');
        sb.append(((this.geometryType == null)?"<null>":this.geometryType));
        sb.append(',');
        sb.append("spatialReference");
        sb.append('=');
        sb.append(((this.spatialReference == null)?"<null>":this.spatialReference));
        sb.append(',');
        sb.append("fields");
        sb.append('=');
        sb.append(((this.fields == null)?"<null>":this.fields));
        sb.append(',');
        sb.append("features");
        sb.append('=');
        sb.append(((this.features == null)?"<null>":this.features));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(Layer.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("objectIdFieldName");
        sb.append('=');
        sb.append(((this.objectIdFieldName == null) ? "<null>" : this.objectIdFieldName));
        sb.append(',');
        sb.append("globalIdFieldName");
        sb.append('=');
        sb.append(((this.globalIdFieldName == null) ? "<null>" : this.globalIdFieldName));
        sb.append(',');
        sb.append("geometryType");
        sb.append('=');
        sb.append(((this.geometryType == null) ? "<null>" : this.geometryType));
        sb.append(',');
        sb.append("spatialReference");
        sb.append('=');
        sb.append(((this.spatialReference == null) ? "<null>" : this.spatialReference));
        sb.append(',');
        sb.append("fields");
        sb.append('=');
        sb.append(((this.fields == null) ? "<null>" : this.fields));
        sb.append(',');
        sb.append("features");
        sb.append('=');
        sb.append(((this.features == null) ? "<null>" : this.features));
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
        result = ((result* 31)+((this.features == null)? 0 :this.features.hashCode()));
        result = ((result* 31)+((this.globalIdFieldName == null)? 0 :this.globalIdFieldName.hashCode()));
        result = ((result* 31)+((this.objectIdFieldName == null)? 0 :this.objectIdFieldName.hashCode()));
        result = ((result* 31)+((this.spatialReference == null)? 0 :this.spatialReference.hashCode()));
        result = ((result* 31)+((this.fields == null)? 0 :this.fields.hashCode()));
        result = ((result* 31)+((this.geometryType == null)? 0 :this.geometryType.hashCode()));
=======
        result = ((result * 31) + ((this.features == null) ? 0 : this.features.hashCode()));
        result =
                ((result * 31)
                        + ((this.globalIdFieldName == null)
                                ? 0
                                : this.globalIdFieldName.hashCode()));
        result =
                ((result * 31)
                        + ((this.objectIdFieldName == null)
                                ? 0
                                : this.objectIdFieldName.hashCode()));
        result =
                ((result * 31)
                        + ((this.spatialReference == null) ? 0 : this.spatialReference.hashCode()));
        result = ((result * 31) + ((this.fields == null) ? 0 : this.fields.hashCode()));
        result = ((result * 31) + ((this.geometryType == null) ? 0 : this.geometryType.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Layer) == false) {
            return false;
        }
        Layer rhs = ((Layer) other);
<<<<<<< HEAD
        return (((((((this.features == rhs.features)||((this.features!= null)&&this.features.equals(rhs.features)))&&((this.globalIdFieldName == rhs.globalIdFieldName)||((this.globalIdFieldName!= null)&&this.globalIdFieldName.equals(rhs.globalIdFieldName))))&&((this.objectIdFieldName == rhs.objectIdFieldName)||((this.objectIdFieldName!= null)&&this.objectIdFieldName.equals(rhs.objectIdFieldName))))&&((this.spatialReference == rhs.spatialReference)||((this.spatialReference!= null)&&this.spatialReference.equals(rhs.spatialReference))))&&((this.fields == rhs.fields)||((this.fields!= null)&&this.fields.equals(rhs.fields))))&&((this.geometryType == rhs.geometryType)||((this.geometryType!= null)&&this.geometryType.equals(rhs.geometryType))));
=======
        return (((((((this.features == rhs.features)
                                                        || ((this.features != null)
                                                                && this.features.equals(
                                                                        rhs.features)))
                                                && ((this.globalIdFieldName
                                                                == rhs.globalIdFieldName)
                                                        || ((this.globalIdFieldName != null)
                                                                && this.globalIdFieldName.equals(
                                                                        rhs.globalIdFieldName))))
                                        && ((this.objectIdFieldName == rhs.objectIdFieldName)
                                                || ((this.objectIdFieldName != null)
                                                        && this.objectIdFieldName.equals(
                                                                rhs.objectIdFieldName))))
                                && ((this.spatialReference == rhs.spatialReference)
                                        || ((this.spatialReference != null)
                                                && this.spatialReference.equals(
                                                        rhs.spatialReference))))
                        && ((this.fields == rhs.fields)
                                || ((this.fields != null) && this.fields.equals(rhs.fields))))
                && ((this.geometryType == rhs.geometryType)
                        || ((this.geometryType != null)
                                && this.geometryType.equals(rhs.geometryType))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
