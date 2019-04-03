
package org.geotools.data.arcgisrest.schema.webservice;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
=======
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS

public class Webservice {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("currentVersion")
    @Expose
    private Double currentVersion;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("id")
    @Expose
    private Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("type")
    @Expose
    private String type;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("serviceItemId")
    @Expose
    private String serviceItemId;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("displayField")
    @Expose
    private String displayField;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("description")
    @Expose
    private String description;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("copyrightText")
    @Expose
    private String copyrightText;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("defaultVisibility")
    @Expose
    private Boolean defaultVisibility;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("editingInfo")
    @Expose
    private EditingInfo editingInfo;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("multiScaleGeometryInfo")
    @Expose
    private MultiScaleGeometryInfo multiScaleGeometryInfo;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("relationships")
    @Expose
    private List<Object> relationships = new ArrayList<Object>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("isDataVersioned")
    @Expose
    private Boolean isDataVersioned;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsCalculate")
    @Expose
    private Boolean supportsCalculate;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsTruncate")
    @Expose
    private Boolean supportsTruncate;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsAttachmentsByUploadId")
    @Expose
    private Boolean supportsAttachmentsByUploadId;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsRollbackOnFailureParameter")
    @Expose
    private Boolean supportsRollbackOnFailureParameter;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsStatistics")
    @Expose
    private Boolean supportsStatistics;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsAdvancedQueries")
    @Expose
    private Boolean supportsAdvancedQueries;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsValidateSql")
    @Expose
    private Boolean supportsValidateSql;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsCoordinatesQuantization")
    @Expose
    private Boolean supportsCoordinatesQuantization;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsApplyEditsWithGlobalIds")
    @Expose
    private Boolean supportsApplyEditsWithGlobalIds;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportsMultiScaleGeometry")
    @Expose
    private Boolean supportsMultiScaleGeometry;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("advancedQueryCapabilities")
    @Expose
    private AdvancedQueryCapabilities advancedQueryCapabilities;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("useStandardizedQueries")
    @Expose
    private Boolean useStandardizedQueries;
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
    @SerializedName("minScale")
    @Expose
    private Integer minScale;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("maxScale")
    @Expose
    private Integer maxScale;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("extent")
    @Expose
    private Extent extent;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("drawingInfo")
    @Expose
    private DrawingInfo drawingInfo;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("allowGeometryUpdates")
    @Expose
    private Boolean allowGeometryUpdates;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("hasAttachments")
    @Expose
    private Boolean hasAttachments;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("htmlPopupType")
    @Expose
    private String htmlPopupType;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("hasM")
    @Expose
    private Boolean hasM;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("hasZ")
    @Expose
    private Boolean hasZ;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("objectIdField")
    @Expose
    private String objectIdField;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("globalIdField")
    @Expose
    private String globalIdField;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("typeIdField")
    @Expose
    private String typeIdField;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("fields")
    @Expose
    private List<Field> fields = new ArrayList<Field>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("indexes")
    @Expose
    private List<Index> indexes = new ArrayList<Index>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("types")
    @Expose
    private List<Object> types = new ArrayList<Object>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("templates")
    @Expose
    private List<Template> templates = new ArrayList<Template>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supportedQueryFormats")
    @Expose
    private String supportedQueryFormats;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("hasStaticData")
    @Expose
    private Boolean hasStaticData;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("maxRecordCount")
    @Expose
    private Integer maxRecordCount;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("standardMaxRecordCount")
    @Expose
    private Integer standardMaxRecordCount;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("tileMaxRecordCount")
    @Expose
    private Integer tileMaxRecordCount;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("maxRecordCountFactor")
    @Expose
    private Integer maxRecordCountFactor;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("capabilities")
    @Expose
    private String capabilities;

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
    public Double getCurrentVersion() {
        return currentVersion;
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
    public void setCurrentVersion(Double currentVersion) {
        this.currentVersion = currentVersion;
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
    public Integer getId() {
        return id;
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
    public void setId(Integer id) {
        this.id = id;
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
    public String getName() {
        return name;
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
    public void setName(String name) {
        this.name = name;
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
    public String getType() {
        return type;
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
    public void setType(String type) {
        this.type = type;
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
    public String getServiceItemId() {
        return serviceItemId;
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
    public void setServiceItemId(String serviceItemId) {
        this.serviceItemId = serviceItemId;
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
    public String getDisplayField() {
        return displayField;
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
    public void setDisplayField(String displayField) {
        this.displayField = displayField;
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
    public String getDescription() {
        return description;
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
    public void setDescription(String description) {
        this.description = description;
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
    public String getCopyrightText() {
        return copyrightText;
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
    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
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
    public Boolean getDefaultVisibility() {
        return defaultVisibility;
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
    public void setDefaultVisibility(Boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
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
    public EditingInfo getEditingInfo() {
        return editingInfo;
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
    public void setEditingInfo(EditingInfo editingInfo) {
        this.editingInfo = editingInfo;
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
    public MultiScaleGeometryInfo getMultiScaleGeometryInfo() {
        return multiScaleGeometryInfo;
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
    public void setMultiScaleGeometryInfo(MultiScaleGeometryInfo multiScaleGeometryInfo) {
        this.multiScaleGeometryInfo = multiScaleGeometryInfo;
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
    public List<Object> getRelationships() {
        return relationships;
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
    public void setRelationships(List<Object> relationships) {
        this.relationships = relationships;
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
    public Boolean getIsDataVersioned() {
        return isDataVersioned;
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
    public void setIsDataVersioned(Boolean isDataVersioned) {
        this.isDataVersioned = isDataVersioned;
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
    public Boolean getSupportsCalculate() {
        return supportsCalculate;
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
    public void setSupportsCalculate(Boolean supportsCalculate) {
        this.supportsCalculate = supportsCalculate;
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
    public Boolean getSupportsTruncate() {
        return supportsTruncate;
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
    public void setSupportsTruncate(Boolean supportsTruncate) {
        this.supportsTruncate = supportsTruncate;
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
    public Boolean getSupportsAttachmentsByUploadId() {
        return supportsAttachmentsByUploadId;
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
    public void setSupportsAttachmentsByUploadId(Boolean supportsAttachmentsByUploadId) {
        this.supportsAttachmentsByUploadId = supportsAttachmentsByUploadId;
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
    public Boolean getSupportsRollbackOnFailureParameter() {
        return supportsRollbackOnFailureParameter;
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
    public void setSupportsRollbackOnFailureParameter(Boolean supportsRollbackOnFailureParameter) {
        this.supportsRollbackOnFailureParameter = supportsRollbackOnFailureParameter;
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
    public Boolean getSupportsStatistics() {
        return supportsStatistics;
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
    public void setSupportsStatistics(Boolean supportsStatistics) {
        this.supportsStatistics = supportsStatistics;
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
    public Boolean getSupportsAdvancedQueries() {
        return supportsAdvancedQueries;
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
    public void setSupportsAdvancedQueries(Boolean supportsAdvancedQueries) {
        this.supportsAdvancedQueries = supportsAdvancedQueries;
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
    public Boolean getSupportsValidateSql() {
        return supportsValidateSql;
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
    public void setSupportsValidateSql(Boolean supportsValidateSql) {
        this.supportsValidateSql = supportsValidateSql;
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
    public Boolean getSupportsCoordinatesQuantization() {
        return supportsCoordinatesQuantization;
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
    public void setSupportsCoordinatesQuantization(Boolean supportsCoordinatesQuantization) {
        this.supportsCoordinatesQuantization = supportsCoordinatesQuantization;
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
    public Boolean getSupportsApplyEditsWithGlobalIds() {
        return supportsApplyEditsWithGlobalIds;
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
    public void setSupportsApplyEditsWithGlobalIds(Boolean supportsApplyEditsWithGlobalIds) {
        this.supportsApplyEditsWithGlobalIds = supportsApplyEditsWithGlobalIds;
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
    public Boolean getSupportsMultiScaleGeometry() {
        return supportsMultiScaleGeometry;
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
    public void setSupportsMultiScaleGeometry(Boolean supportsMultiScaleGeometry) {
        this.supportsMultiScaleGeometry = supportsMultiScaleGeometry;
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
    public AdvancedQueryCapabilities getAdvancedQueryCapabilities() {
        return advancedQueryCapabilities;
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
    public void setAdvancedQueryCapabilities(AdvancedQueryCapabilities advancedQueryCapabilities) {
        this.advancedQueryCapabilities = advancedQueryCapabilities;
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
    public Boolean getUseStandardizedQueries() {
        return useStandardizedQueries;
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
    public void setUseStandardizedQueries(Boolean useStandardizedQueries) {
        this.useStandardizedQueries = useStandardizedQueries;
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
    public String getGeometryType() {
        return geometryType;
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
    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
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
    public Integer getMinScale() {
        return minScale;
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
    public void setMinScale(Integer minScale) {
        this.minScale = minScale;
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
    public Integer getMaxScale() {
        return maxScale;
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
    public void setMaxScale(Integer maxScale) {
        this.maxScale = maxScale;
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
    public Extent getExtent() {
        return extent;
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
    public void setExtent(Extent extent) {
        this.extent = extent;
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
    public DrawingInfo getDrawingInfo() {
        return drawingInfo;
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
    public void setDrawingInfo(DrawingInfo drawingInfo) {
        this.drawingInfo = drawingInfo;
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
    public Boolean getAllowGeometryUpdates() {
        return allowGeometryUpdates;
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
    public void setAllowGeometryUpdates(Boolean allowGeometryUpdates) {
        this.allowGeometryUpdates = allowGeometryUpdates;
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
    public Boolean getHasAttachments() {
        return hasAttachments;
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
    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
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
    public String getHtmlPopupType() {
        return htmlPopupType;
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
    public void setHtmlPopupType(String htmlPopupType) {
        this.htmlPopupType = htmlPopupType;
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
    public Boolean getHasM() {
        return hasM;
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
    public void setHasM(Boolean hasM) {
        this.hasM = hasM;
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
    public Boolean getHasZ() {
        return hasZ;
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
    public void setHasZ(Boolean hasZ) {
        this.hasZ = hasZ;
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
    public String getObjectIdField() {
        return objectIdField;
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
    public void setObjectIdField(String objectIdField) {
        this.objectIdField = objectIdField;
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
    public String getGlobalIdField() {
        return globalIdField;
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
    public void setGlobalIdField(String globalIdField) {
        this.globalIdField = globalIdField;
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
    public String getTypeIdField() {
        return typeIdField;
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
    public void setTypeIdField(String typeIdField) {
        this.typeIdField = typeIdField;
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
    public List<Field> getFields() {
        return fields;
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
    public void setFields(List<Field> fields) {
        this.fields = fields;
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
    public List<Index> getIndexes() {
        return indexes;
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
    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
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
    public List<Object> getTypes() {
        return types;
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
    public void setTypes(List<Object> types) {
        this.types = types;
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
    public List<Template> getTemplates() {
        return templates;
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
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
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
    public String getSupportedQueryFormats() {
        return supportedQueryFormats;
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
    public void setSupportedQueryFormats(String supportedQueryFormats) {
        this.supportedQueryFormats = supportedQueryFormats;
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
    public Boolean getHasStaticData() {
        return hasStaticData;
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
    public void setHasStaticData(Boolean hasStaticData) {
        this.hasStaticData = hasStaticData;
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
    public Integer getMaxRecordCount() {
        return maxRecordCount;
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
    public void setMaxRecordCount(Integer maxRecordCount) {
        this.maxRecordCount = maxRecordCount;
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
    public Integer getStandardMaxRecordCount() {
        return standardMaxRecordCount;
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
    public void setStandardMaxRecordCount(Integer standardMaxRecordCount) {
        this.standardMaxRecordCount = standardMaxRecordCount;
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
    public Integer getTileMaxRecordCount() {
        return tileMaxRecordCount;
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
    public void setTileMaxRecordCount(Integer tileMaxRecordCount) {
        this.tileMaxRecordCount = tileMaxRecordCount;
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
    public Integer getMaxRecordCountFactor() {
        return maxRecordCountFactor;
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
    public void setMaxRecordCountFactor(Integer maxRecordCountFactor) {
        this.maxRecordCountFactor = maxRecordCountFactor;
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
    public String getCapabilities() {
        return capabilities;
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
    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
<<<<<<< HEAD
        sb.append(Webservice.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("currentVersion");
        sb.append('=');
        sb.append(((this.currentVersion == null)?"<null>":this.currentVersion));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("serviceItemId");
        sb.append('=');
        sb.append(((this.serviceItemId == null)?"<null>":this.serviceItemId));
        sb.append(',');
        sb.append("displayField");
        sb.append('=');
        sb.append(((this.displayField == null)?"<null>":this.displayField));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("copyrightText");
        sb.append('=');
        sb.append(((this.copyrightText == null)?"<null>":this.copyrightText));
        sb.append(',');
        sb.append("defaultVisibility");
        sb.append('=');
        sb.append(((this.defaultVisibility == null)?"<null>":this.defaultVisibility));
        sb.append(',');
        sb.append("editingInfo");
        sb.append('=');
        sb.append(((this.editingInfo == null)?"<null>":this.editingInfo));
        sb.append(',');
        sb.append("multiScaleGeometryInfo");
        sb.append('=');
        sb.append(((this.multiScaleGeometryInfo == null)?"<null>":this.multiScaleGeometryInfo));
        sb.append(',');
        sb.append("relationships");
        sb.append('=');
        sb.append(((this.relationships == null)?"<null>":this.relationships));
        sb.append(',');
        sb.append("isDataVersioned");
        sb.append('=');
        sb.append(((this.isDataVersioned == null)?"<null>":this.isDataVersioned));
        sb.append(',');
        sb.append("supportsCalculate");
        sb.append('=');
        sb.append(((this.supportsCalculate == null)?"<null>":this.supportsCalculate));
        sb.append(',');
        sb.append("supportsTruncate");
        sb.append('=');
        sb.append(((this.supportsTruncate == null)?"<null>":this.supportsTruncate));
        sb.append(',');
        sb.append("supportsAttachmentsByUploadId");
        sb.append('=');
        sb.append(((this.supportsAttachmentsByUploadId == null)?"<null>":this.supportsAttachmentsByUploadId));
        sb.append(',');
        sb.append("supportsRollbackOnFailureParameter");
        sb.append('=');
        sb.append(((this.supportsRollbackOnFailureParameter == null)?"<null>":this.supportsRollbackOnFailureParameter));
        sb.append(',');
        sb.append("supportsStatistics");
        sb.append('=');
        sb.append(((this.supportsStatistics == null)?"<null>":this.supportsStatistics));
        sb.append(',');
        sb.append("supportsAdvancedQueries");
        sb.append('=');
        sb.append(((this.supportsAdvancedQueries == null)?"<null>":this.supportsAdvancedQueries));
        sb.append(',');
        sb.append("supportsValidateSql");
        sb.append('=');
        sb.append(((this.supportsValidateSql == null)?"<null>":this.supportsValidateSql));
        sb.append(',');
        sb.append("supportsCoordinatesQuantization");
        sb.append('=');
        sb.append(((this.supportsCoordinatesQuantization == null)?"<null>":this.supportsCoordinatesQuantization));
        sb.append(',');
        sb.append("supportsApplyEditsWithGlobalIds");
        sb.append('=');
        sb.append(((this.supportsApplyEditsWithGlobalIds == null)?"<null>":this.supportsApplyEditsWithGlobalIds));
        sb.append(',');
        sb.append("supportsMultiScaleGeometry");
        sb.append('=');
        sb.append(((this.supportsMultiScaleGeometry == null)?"<null>":this.supportsMultiScaleGeometry));
        sb.append(',');
        sb.append("advancedQueryCapabilities");
        sb.append('=');
        sb.append(((this.advancedQueryCapabilities == null)?"<null>":this.advancedQueryCapabilities));
        sb.append(',');
        sb.append("useStandardizedQueries");
        sb.append('=');
        sb.append(((this.useStandardizedQueries == null)?"<null>":this.useStandardizedQueries));
        sb.append(',');
        sb.append("geometryType");
        sb.append('=');
        sb.append(((this.geometryType == null)?"<null>":this.geometryType));
        sb.append(',');
        sb.append("minScale");
        sb.append('=');
        sb.append(((this.minScale == null)?"<null>":this.minScale));
        sb.append(',');
        sb.append("maxScale");
        sb.append('=');
        sb.append(((this.maxScale == null)?"<null>":this.maxScale));
        sb.append(',');
        sb.append("extent");
        sb.append('=');
        sb.append(((this.extent == null)?"<null>":this.extent));
        sb.append(',');
        sb.append("drawingInfo");
        sb.append('=');
        sb.append(((this.drawingInfo == null)?"<null>":this.drawingInfo));
        sb.append(',');
        sb.append("allowGeometryUpdates");
        sb.append('=');
        sb.append(((this.allowGeometryUpdates == null)?"<null>":this.allowGeometryUpdates));
        sb.append(',');
        sb.append("hasAttachments");
        sb.append('=');
        sb.append(((this.hasAttachments == null)?"<null>":this.hasAttachments));
        sb.append(',');
        sb.append("htmlPopupType");
        sb.append('=');
        sb.append(((this.htmlPopupType == null)?"<null>":this.htmlPopupType));
        sb.append(',');
        sb.append("hasM");
        sb.append('=');
        sb.append(((this.hasM == null)?"<null>":this.hasM));
        sb.append(',');
        sb.append("hasZ");
        sb.append('=');
        sb.append(((this.hasZ == null)?"<null>":this.hasZ));
        sb.append(',');
        sb.append("objectIdField");
        sb.append('=');
        sb.append(((this.objectIdField == null)?"<null>":this.objectIdField));
        sb.append(',');
        sb.append("globalIdField");
        sb.append('=');
        sb.append(((this.globalIdField == null)?"<null>":this.globalIdField));
        sb.append(',');
        sb.append("typeIdField");
        sb.append('=');
        sb.append(((this.typeIdField == null)?"<null>":this.typeIdField));
        sb.append(',');
        sb.append("fields");
        sb.append('=');
        sb.append(((this.fields == null)?"<null>":this.fields));
        sb.append(',');
        sb.append("indexes");
        sb.append('=');
        sb.append(((this.indexes == null)?"<null>":this.indexes));
        sb.append(',');
        sb.append("types");
        sb.append('=');
        sb.append(((this.types == null)?"<null>":this.types));
        sb.append(',');
        sb.append("templates");
        sb.append('=');
        sb.append(((this.templates == null)?"<null>":this.templates));
        sb.append(',');
        sb.append("supportedQueryFormats");
        sb.append('=');
        sb.append(((this.supportedQueryFormats == null)?"<null>":this.supportedQueryFormats));
        sb.append(',');
        sb.append("hasStaticData");
        sb.append('=');
        sb.append(((this.hasStaticData == null)?"<null>":this.hasStaticData));
        sb.append(',');
        sb.append("maxRecordCount");
        sb.append('=');
        sb.append(((this.maxRecordCount == null)?"<null>":this.maxRecordCount));
        sb.append(',');
        sb.append("standardMaxRecordCount");
        sb.append('=');
        sb.append(((this.standardMaxRecordCount == null)?"<null>":this.standardMaxRecordCount));
        sb.append(',');
        sb.append("tileMaxRecordCount");
        sb.append('=');
        sb.append(((this.tileMaxRecordCount == null)?"<null>":this.tileMaxRecordCount));
        sb.append(',');
        sb.append("maxRecordCountFactor");
        sb.append('=');
        sb.append(((this.maxRecordCountFactor == null)?"<null>":this.maxRecordCountFactor));
        sb.append(',');
        sb.append("capabilities");
        sb.append('=');
        sb.append(((this.capabilities == null)?"<null>":this.capabilities));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(Webservice.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
=======
        sb.append(Webservice.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        sb.append("currentVersion");
        sb.append('=');
        sb.append(((this.currentVersion == null)?"<null>":this.currentVersion));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("serviceItemId");
        sb.append('=');
        sb.append(((this.serviceItemId == null)?"<null>":this.serviceItemId));
        sb.append(',');
        sb.append("displayField");
        sb.append('=');
        sb.append(((this.displayField == null)?"<null>":this.displayField));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("copyrightText");
        sb.append('=');
        sb.append(((this.copyrightText == null)?"<null>":this.copyrightText));
        sb.append(',');
        sb.append("defaultVisibility");
        sb.append('=');
        sb.append(((this.defaultVisibility == null)?"<null>":this.defaultVisibility));
        sb.append(',');
        sb.append("editingInfo");
        sb.append('=');
        sb.append(((this.editingInfo == null)?"<null>":this.editingInfo));
        sb.append(',');
        sb.append("multiScaleGeometryInfo");
        sb.append('=');
        sb.append(((this.multiScaleGeometryInfo == null)?"<null>":this.multiScaleGeometryInfo));
        sb.append(',');
        sb.append("relationships");
        sb.append('=');
        sb.append(((this.relationships == null)?"<null>":this.relationships));
        sb.append(',');
        sb.append("isDataVersioned");
        sb.append('=');
        sb.append(((this.isDataVersioned == null)?"<null>":this.isDataVersioned));
        sb.append(',');
        sb.append("supportsCalculate");
        sb.append('=');
        sb.append(((this.supportsCalculate == null)?"<null>":this.supportsCalculate));
        sb.append(',');
        sb.append("supportsTruncate");
        sb.append('=');
        sb.append(((this.supportsTruncate == null)?"<null>":this.supportsTruncate));
        sb.append(',');
        sb.append("supportsAttachmentsByUploadId");
        sb.append('=');
        sb.append(((this.supportsAttachmentsByUploadId == null)?"<null>":this.supportsAttachmentsByUploadId));
        sb.append(',');
        sb.append("supportsRollbackOnFailureParameter");
        sb.append('=');
        sb.append(((this.supportsRollbackOnFailureParameter == null)?"<null>":this.supportsRollbackOnFailureParameter));
        sb.append(',');
        sb.append("supportsStatistics");
        sb.append('=');
        sb.append(((this.supportsStatistics == null)?"<null>":this.supportsStatistics));
        sb.append(',');
        sb.append("supportsAdvancedQueries");
        sb.append('=');
        sb.append(((this.supportsAdvancedQueries == null)?"<null>":this.supportsAdvancedQueries));
        sb.append(',');
        sb.append("supportsValidateSql");
        sb.append('=');
        sb.append(((this.supportsValidateSql == null)?"<null>":this.supportsValidateSql));
        sb.append(',');
        sb.append("supportsCoordinatesQuantization");
        sb.append('=');
        sb.append(((this.supportsCoordinatesQuantization == null)?"<null>":this.supportsCoordinatesQuantization));
        sb.append(',');
        sb.append("supportsApplyEditsWithGlobalIds");
        sb.append('=');
        sb.append(((this.supportsApplyEditsWithGlobalIds == null)?"<null>":this.supportsApplyEditsWithGlobalIds));
        sb.append(',');
        sb.append("supportsMultiScaleGeometry");
        sb.append('=');
        sb.append(((this.supportsMultiScaleGeometry == null)?"<null>":this.supportsMultiScaleGeometry));
        sb.append(',');
        sb.append("advancedQueryCapabilities");
        sb.append('=');
        sb.append(((this.advancedQueryCapabilities == null)?"<null>":this.advancedQueryCapabilities));
        sb.append(',');
        sb.append("useStandardizedQueries");
        sb.append('=');
        sb.append(((this.useStandardizedQueries == null)?"<null>":this.useStandardizedQueries));
        sb.append(',');
        sb.append("geometryType");
        sb.append('=');
        sb.append(((this.geometryType == null)?"<null>":this.geometryType));
        sb.append(',');
        sb.append("minScale");
        sb.append('=');
        sb.append(((this.minScale == null)?"<null>":this.minScale));
        sb.append(',');
        sb.append("maxScale");
        sb.append('=');
        sb.append(((this.maxScale == null)?"<null>":this.maxScale));
        sb.append(',');
        sb.append("extent");
        sb.append('=');
        sb.append(((this.extent == null)?"<null>":this.extent));
        sb.append(',');
        sb.append("drawingInfo");
        sb.append('=');
        sb.append(((this.drawingInfo == null)?"<null>":this.drawingInfo));
        sb.append(',');
        sb.append("allowGeometryUpdates");
        sb.append('=');
        sb.append(((this.allowGeometryUpdates == null)?"<null>":this.allowGeometryUpdates));
        sb.append(',');
        sb.append("hasAttachments");
        sb.append('=');
        sb.append(((this.hasAttachments == null)?"<null>":this.hasAttachments));
        sb.append(',');
        sb.append("htmlPopupType");
        sb.append('=');
        sb.append(((this.htmlPopupType == null)?"<null>":this.htmlPopupType));
        sb.append(',');
        sb.append("hasM");
        sb.append('=');
        sb.append(((this.hasM == null)?"<null>":this.hasM));
        sb.append(',');
        sb.append("hasZ");
        sb.append('=');
        sb.append(((this.hasZ == null)?"<null>":this.hasZ));
        sb.append(',');
        sb.append("objectIdField");
        sb.append('=');
        sb.append(((this.objectIdField == null)?"<null>":this.objectIdField));
        sb.append(',');
        sb.append("globalIdField");
        sb.append('=');
        sb.append(((this.globalIdField == null)?"<null>":this.globalIdField));
        sb.append(',');
        sb.append("typeIdField");
        sb.append('=');
        sb.append(((this.typeIdField == null)?"<null>":this.typeIdField));
        sb.append(',');
        sb.append("fields");
        sb.append('=');
        sb.append(((this.fields == null)?"<null>":this.fields));
        sb.append(',');
        sb.append("indexes");
        sb.append('=');
        sb.append(((this.indexes == null)?"<null>":this.indexes));
        sb.append(',');
        sb.append("types");
        sb.append('=');
        sb.append(((this.types == null)?"<null>":this.types));
        sb.append(',');
        sb.append("templates");
        sb.append('=');
        sb.append(((this.templates == null)?"<null>":this.templates));
        sb.append(',');
        sb.append("supportedQueryFormats");
        sb.append('=');
        sb.append(((this.supportedQueryFormats == null)?"<null>":this.supportedQueryFormats));
        sb.append(',');
        sb.append("hasStaticData");
        sb.append('=');
        sb.append(((this.hasStaticData == null)?"<null>":this.hasStaticData));
        sb.append(',');
        sb.append("maxRecordCount");
        sb.append('=');
        sb.append(((this.maxRecordCount == null)?"<null>":this.maxRecordCount));
        sb.append(',');
        sb.append("standardMaxRecordCount");
        sb.append('=');
        sb.append(((this.standardMaxRecordCount == null)?"<null>":this.standardMaxRecordCount));
        sb.append(',');
        sb.append("tileMaxRecordCount");
        sb.append('=');
        sb.append(((this.tileMaxRecordCount == null)?"<null>":this.tileMaxRecordCount));
        sb.append(',');
        sb.append("maxRecordCountFactor");
        sb.append('=');
        sb.append(((this.maxRecordCountFactor == null)?"<null>":this.maxRecordCountFactor));
        sb.append(',');
        sb.append("capabilities");
        sb.append('=');
        sb.append(((this.capabilities == null)?"<null>":this.capabilities));
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
        result = ((result* 31)+((this.extent == null)? 0 :this.extent.hashCode()));
        result = ((result* 31)+((this.supportsApplyEditsWithGlobalIds == null)? 0 :this.supportsApplyEditsWithGlobalIds.hashCode()));
        result = ((result* 31)+((this.maxRecordCount == null)? 0 :this.maxRecordCount.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.hasStaticData == null)? 0 :this.hasStaticData.hashCode()));
        result = ((result* 31)+((this.editingInfo == null)? 0 :this.editingInfo.hashCode()));
        result = ((result* 31)+((this.supportsAttachmentsByUploadId == null)? 0 :this.supportsAttachmentsByUploadId.hashCode()));
        result = ((result* 31)+((this.relationships == null)? 0 :this.relationships.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.supportsStatistics == null)? 0 :this.supportsStatistics.hashCode()));
        result = ((result* 31)+((this.standardMaxRecordCount == null)? 0 :this.standardMaxRecordCount.hashCode()));
        result = ((result* 31)+((this.isDataVersioned == null)? 0 :this.isDataVersioned.hashCode()));
        result = ((result* 31)+((this.templates == null)? 0 :this.templates.hashCode()));
        result = ((result* 31)+((this.supportsAdvancedQueries == null)? 0 :this.supportsAdvancedQueries.hashCode()));
        result = ((result* 31)+((this.currentVersion == null)? 0 :this.currentVersion.hashCode()));
        result = ((result* 31)+((this.hasZ == null)? 0 :this.hasZ.hashCode()));
        result = ((result* 31)+((this.objectIdField == null)? 0 :this.objectIdField.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.displayField == null)? 0 :this.displayField.hashCode()));
        result = ((result* 31)+((this.supportsCalculate == null)? 0 :this.supportsCalculate.hashCode()));
        result = ((result* 31)+((this.fields == null)? 0 :this.fields.hashCode()));
        result = ((result* 31)+((this.maxRecordCountFactor == null)? 0 :this.maxRecordCountFactor.hashCode()));
        result = ((result* 31)+((this.hasM == null)? 0 :this.hasM.hashCode()));
        result = ((result* 31)+((this.allowGeometryUpdates == null)? 0 :this.allowGeometryUpdates.hashCode()));
        result = ((result* 31)+((this.useStandardizedQueries == null)? 0 :this.useStandardizedQueries.hashCode()));
        result = ((result* 31)+((this.globalIdField == null)? 0 :this.globalIdField.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.supportsRollbackOnFailureParameter == null)? 0 :this.supportsRollbackOnFailureParameter.hashCode()));
        result = ((result* 31)+((this.supportsTruncate == null)? 0 :this.supportsTruncate.hashCode()));
        result = ((result* 31)+((this.indexes == null)? 0 :this.indexes.hashCode()));
        result = ((result* 31)+((this.htmlPopupType == null)? 0 :this.htmlPopupType.hashCode()));
        result = ((result* 31)+((this.minScale == null)? 0 :this.minScale.hashCode()));
        result = ((result* 31)+((this.hasAttachments == null)? 0 :this.hasAttachments.hashCode()));
        result = ((result* 31)+((this.advancedQueryCapabilities == null)? 0 :this.advancedQueryCapabilities.hashCode()));
        result = ((result* 31)+((this.serviceItemId == null)? 0 :this.serviceItemId.hashCode()));
        result = ((result* 31)+((this.supportsValidateSql == null)? 0 :this.supportsValidateSql.hashCode()));
        result = ((result* 31)+((this.supportsCoordinatesQuantization == null)? 0 :this.supportsCoordinatesQuantization.hashCode()));
        result = ((result* 31)+((this.types == null)? 0 :this.types.hashCode()));
        result = ((result* 31)+((this.capabilities == null)? 0 :this.capabilities.hashCode()));
        result = ((result* 31)+((this.maxScale == null)? 0 :this.maxScale.hashCode()));
        result = ((result* 31)+((this.defaultVisibility == null)? 0 :this.defaultVisibility.hashCode()));
        result = ((result* 31)+((this.tileMaxRecordCount == null)? 0 :this.tileMaxRecordCount.hashCode()));
        result = ((result* 31)+((this.typeIdField == null)? 0 :this.typeIdField.hashCode()));
        result = ((result* 31)+((this.multiScaleGeometryInfo == null)? 0 :this.multiScaleGeometryInfo.hashCode()));
        result = ((result* 31)+((this.supportedQueryFormats == null)? 0 :this.supportedQueryFormats.hashCode()));
        result = ((result* 31)+((this.supportsMultiScaleGeometry == null)? 0 :this.supportsMultiScaleGeometry.hashCode()));
        result = ((result* 31)+((this.drawingInfo == null)? 0 :this.drawingInfo.hashCode()));
        result = ((result* 31)+((this.copyrightText == null)? 0 :this.copyrightText.hashCode()));
        result = ((result* 31)+((this.geometryType == null)? 0 :this.geometryType.hashCode()));
<<<<<<< HEAD
=======
        result = ((result * 31) + ((this.extent == null) ? 0 : this.extent.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsApplyEditsWithGlobalIds == null)
                                ? 0
                                : this.supportsApplyEditsWithGlobalIds.hashCode()));
        result =
                ((result * 31)
                        + ((this.maxRecordCount == null) ? 0 : this.maxRecordCount.hashCode()));
        result = ((result * 31) + ((this.type == null) ? 0 : this.type.hashCode()));
        result =
                ((result * 31)
                        + ((this.hasStaticData == null) ? 0 : this.hasStaticData.hashCode()));
        result = ((result * 31) + ((this.editingInfo == null) ? 0 : this.editingInfo.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsAttachmentsByUploadId == null)
                                ? 0
                                : this.supportsAttachmentsByUploadId.hashCode()));
        result =
                ((result * 31)
                        + ((this.relationships == null) ? 0 : this.relationships.hashCode()));
        result = ((result * 31) + ((this.id == null) ? 0 : this.id.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsStatistics == null)
                                ? 0
                                : this.supportsStatistics.hashCode()));
        result =
                ((result * 31)
                        + ((this.standardMaxRecordCount == null)
                                ? 0
                                : this.standardMaxRecordCount.hashCode()));
        result =
                ((result * 31)
                        + ((this.isDataVersioned == null) ? 0 : this.isDataVersioned.hashCode()));
        result = ((result * 31) + ((this.templates == null) ? 0 : this.templates.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsAdvancedQueries == null)
                                ? 0
                                : this.supportsAdvancedQueries.hashCode()));
        result =
                ((result * 31)
                        + ((this.currentVersion == null) ? 0 : this.currentVersion.hashCode()));
        result = ((result * 31) + ((this.hasZ == null) ? 0 : this.hasZ.hashCode()));
        result =
                ((result * 31)
                        + ((this.objectIdField == null) ? 0 : this.objectIdField.hashCode()));
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.displayField == null) ? 0 : this.displayField.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsCalculate == null)
                                ? 0
                                : this.supportsCalculate.hashCode()));
        result = ((result * 31) + ((this.fields == null) ? 0 : this.fields.hashCode()));
        result =
                ((result * 31)
                        + ((this.maxRecordCountFactor == null)
                                ? 0
                                : this.maxRecordCountFactor.hashCode()));
        result = ((result * 31) + ((this.hasM == null) ? 0 : this.hasM.hashCode()));
        result =
                ((result * 31)
                        + ((this.allowGeometryUpdates == null)
                                ? 0
                                : this.allowGeometryUpdates.hashCode()));
        result =
                ((result * 31)
                        + ((this.useStandardizedQueries == null)
                                ? 0
                                : this.useStandardizedQueries.hashCode()));
        result =
                ((result * 31)
                        + ((this.globalIdField == null) ? 0 : this.globalIdField.hashCode()));
        result = ((result * 31) + ((this.description == null) ? 0 : this.description.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsRollbackOnFailureParameter == null)
                                ? 0
                                : this.supportsRollbackOnFailureParameter.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsTruncate == null) ? 0 : this.supportsTruncate.hashCode()));
        result = ((result * 31) + ((this.indexes == null) ? 0 : this.indexes.hashCode()));
        result =
                ((result * 31)
                        + ((this.htmlPopupType == null) ? 0 : this.htmlPopupType.hashCode()));
        result = ((result * 31) + ((this.minScale == null) ? 0 : this.minScale.hashCode()));
        result =
                ((result * 31)
                        + ((this.hasAttachments == null) ? 0 : this.hasAttachments.hashCode()));
        result =
                ((result * 31)
                        + ((this.advancedQueryCapabilities == null)
                                ? 0
                                : this.advancedQueryCapabilities.hashCode()));
        result =
                ((result * 31)
                        + ((this.serviceItemId == null) ? 0 : this.serviceItemId.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsValidateSql == null)
                                ? 0
                                : this.supportsValidateSql.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsCoordinatesQuantization == null)
                                ? 0
                                : this.supportsCoordinatesQuantization.hashCode()));
        result = ((result * 31) + ((this.types == null) ? 0 : this.types.hashCode()));
        result = ((result * 31) + ((this.capabilities == null) ? 0 : this.capabilities.hashCode()));
        result = ((result * 31) + ((this.maxScale == null) ? 0 : this.maxScale.hashCode()));
        result =
                ((result * 31)
                        + ((this.defaultVisibility == null)
                                ? 0
                                : this.defaultVisibility.hashCode()));
        result =
                ((result * 31)
                        + ((this.tileMaxRecordCount == null)
                                ? 0
                                : this.tileMaxRecordCount.hashCode()));
        result = ((result * 31) + ((this.typeIdField == null) ? 0 : this.typeIdField.hashCode()));
        result =
                ((result * 31)
                        + ((this.multiScaleGeometryInfo == null)
                                ? 0
                                : this.multiScaleGeometryInfo.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportedQueryFormats == null)
                                ? 0
                                : this.supportedQueryFormats.hashCode()));
        result =
                ((result * 31)
                        + ((this.supportsMultiScaleGeometry == null)
                                ? 0
                                : this.supportsMultiScaleGeometry.hashCode()));
        result = ((result * 31) + ((this.drawingInfo == null) ? 0 : this.drawingInfo.hashCode()));
        result =
                ((result * 31)
                        + ((this.copyrightText == null) ? 0 : this.copyrightText.hashCode()));
        result = ((result * 31) + ((this.geometryType == null) ? 0 : this.geometryType.hashCode()));
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
        if ((other instanceof Webservice) == false) {
            return false;
        }
        Webservice rhs = ((Webservice) other);
<<<<<<< HEAD
<<<<<<< HEAD
        return ((((((((((((((((((((((((((((((((((((((((((((((((((this.extent == rhs.extent)||((this.extent!= null)&&this.extent.equals(rhs.extent)))&&((this.supportsApplyEditsWithGlobalIds == rhs.supportsApplyEditsWithGlobalIds)||((this.supportsApplyEditsWithGlobalIds!= null)&&this.supportsApplyEditsWithGlobalIds.equals(rhs.supportsApplyEditsWithGlobalIds))))&&((this.maxRecordCount == rhs.maxRecordCount)||((this.maxRecordCount!= null)&&this.maxRecordCount.equals(rhs.maxRecordCount))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.hasStaticData == rhs.hasStaticData)||((this.hasStaticData!= null)&&this.hasStaticData.equals(rhs.hasStaticData))))&&((this.editingInfo == rhs.editingInfo)||((this.editingInfo!= null)&&this.editingInfo.equals(rhs.editingInfo))))&&((this.supportsAttachmentsByUploadId == rhs.supportsAttachmentsByUploadId)||((this.supportsAttachmentsByUploadId!= null)&&this.supportsAttachmentsByUploadId.equals(rhs.supportsAttachmentsByUploadId))))&&((this.relationships == rhs.relationships)||((this.relationships!= null)&&this.relationships.equals(rhs.relationships))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.supportsStatistics == rhs.supportsStatistics)||((this.supportsStatistics!= null)&&this.supportsStatistics.equals(rhs.supportsStatistics))))&&((this.standardMaxRecordCount == rhs.standardMaxRecordCount)||((this.standardMaxRecordCount!= null)&&this.standardMaxRecordCount.equals(rhs.standardMaxRecordCount))))&&((this.isDataVersioned == rhs.isDataVersioned)||((this.isDataVersioned!= null)&&this.isDataVersioned.equals(rhs.isDataVersioned))))&&((this.templates == rhs.templates)||((this.templates!= null)&&this.templates.equals(rhs.templates))))&&((this.supportsAdvancedQueries == rhs.supportsAdvancedQueries)||((this.supportsAdvancedQueries!= null)&&this.supportsAdvancedQueries.equals(rhs.supportsAdvancedQueries))))&&((this.currentVersion == rhs.currentVersion)||((this.currentVersion!= null)&&this.currentVersion.equals(rhs.currentVersion))))&&((this.hasZ == rhs.hasZ)||((this.hasZ!= null)&&this.hasZ.equals(rhs.hasZ))))&&((this.objectIdField == rhs.objectIdField)||((this.objectIdField!= null)&&this.objectIdField.equals(rhs.objectIdField))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.displayField == rhs.displayField)||((this.displayField!= null)&&this.displayField.equals(rhs.displayField))))&&((this.supportsCalculate == rhs.supportsCalculate)||((this.supportsCalculate!= null)&&this.supportsCalculate.equals(rhs.supportsCalculate))))&&((this.fields == rhs.fields)||((this.fields!= null)&&this.fields.equals(rhs.fields))))&&((this.maxRecordCountFactor == rhs.maxRecordCountFactor)||((this.maxRecordCountFactor!= null)&&this.maxRecordCountFactor.equals(rhs.maxRecordCountFactor))))&&((this.hasM == rhs.hasM)||((this.hasM!= null)&&this.hasM.equals(rhs.hasM))))&&((this.allowGeometryUpdates == rhs.allowGeometryUpdates)||((this.allowGeometryUpdates!= null)&&this.allowGeometryUpdates.equals(rhs.allowGeometryUpdates))))&&((this.useStandardizedQueries == rhs.useStandardizedQueries)||((this.useStandardizedQueries!= null)&&this.useStandardizedQueries.equals(rhs.useStandardizedQueries))))&&((this.globalIdField == rhs.globalIdField)||((this.globalIdField!= null)&&this.globalIdField.equals(rhs.globalIdField))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.supportsRollbackOnFailureParameter == rhs.supportsRollbackOnFailureParameter)||((this.supportsRollbackOnFailureParameter!= null)&&this.supportsRollbackOnFailureParameter.equals(rhs.supportsRollbackOnFailureParameter))))&&((this.supportsTruncate == rhs.supportsTruncate)||((this.supportsTruncate!= null)&&this.supportsTruncate.equals(rhs.supportsTruncate))))&&((this.indexes == rhs.indexes)||((this.indexes!= null)&&this.indexes.equals(rhs.indexes))))&&((this.htmlPopupType == rhs.htmlPopupType)||((this.htmlPopupType!= null)&&this.htmlPopupType.equals(rhs.htmlPopupType))))&&((this.minScale == rhs.minScale)||((this.minScale!= null)&&this.minScale.equals(rhs.minScale))))&&((this.hasAttachments == rhs.hasAttachments)||((this.hasAttachments!= null)&&this.hasAttachments.equals(rhs.hasAttachments))))&&((this.advancedQueryCapabilities == rhs.advancedQueryCapabilities)||((this.advancedQueryCapabilities!= null)&&this.advancedQueryCapabilities.equals(rhs.advancedQueryCapabilities))))&&((this.serviceItemId == rhs.serviceItemId)||((this.serviceItemId!= null)&&this.serviceItemId.equals(rhs.serviceItemId))))&&((this.supportsValidateSql == rhs.supportsValidateSql)||((this.supportsValidateSql!= null)&&this.supportsValidateSql.equals(rhs.supportsValidateSql))))&&((this.supportsCoordinatesQuantization == rhs.supportsCoordinatesQuantization)||((this.supportsCoordinatesQuantization!= null)&&this.supportsCoordinatesQuantization.equals(rhs.supportsCoordinatesQuantization))))&&((this.types == rhs.types)||((this.types!= null)&&this.types.equals(rhs.types))))&&((this.capabilities == rhs.capabilities)||((this.capabilities!= null)&&this.capabilities.equals(rhs.capabilities))))&&((this.maxScale == rhs.maxScale)||((this.maxScale!= null)&&this.maxScale.equals(rhs.maxScale))))&&((this.defaultVisibility == rhs.defaultVisibility)||((this.defaultVisibility!= null)&&this.defaultVisibility.equals(rhs.defaultVisibility))))&&((this.tileMaxRecordCount == rhs.tileMaxRecordCount)||((this.tileMaxRecordCount!= null)&&this.tileMaxRecordCount.equals(rhs.tileMaxRecordCount))))&&((this.typeIdField == rhs.typeIdField)||((this.typeIdField!= null)&&this.typeIdField.equals(rhs.typeIdField))))&&((this.multiScaleGeometryInfo == rhs.multiScaleGeometryInfo)||((this.multiScaleGeometryInfo!= null)&&this.multiScaleGeometryInfo.equals(rhs.multiScaleGeometryInfo))))&&((this.supportedQueryFormats == rhs.supportedQueryFormats)||((this.supportedQueryFormats!= null)&&this.supportedQueryFormats.equals(rhs.supportedQueryFormats))))&&((this.supportsMultiScaleGeometry == rhs.supportsMultiScaleGeometry)||((this.supportsMultiScaleGeometry!= null)&&this.supportsMultiScaleGeometry.equals(rhs.supportsMultiScaleGeometry))))&&((this.drawingInfo == rhs.drawingInfo)||((this.drawingInfo!= null)&&this.drawingInfo.equals(rhs.drawingInfo))))&&((this.copyrightText == rhs.copyrightText)||((this.copyrightText!= null)&&this.copyrightText.equals(rhs.copyrightText))))&&((this.geometryType == rhs.geometryType)||((this.geometryType!= null)&&this.geometryType.equals(rhs.geometryType))));
=======
        return ((((((((((((((((((((((((((((((((((((((((((((((((((this.extent == rhs.extent)
                                                                                                                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                                                                                                                        .extent
                                                                                                                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                                                                                                                .extent
                                                                                                                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                        rhs.extent)))
                                                                                                                                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                                                                                                                                .supportsApplyEditsWithGlobalIds
                                                                                                                                                                                                                                                                                                                                                                                                                        == rhs.supportsApplyEditsWithGlobalIds)
                                                                                                                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                                                                                                                        .supportsApplyEditsWithGlobalIds
                                                                                                                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                                                                                                                .supportsApplyEditsWithGlobalIds
                                                                                                                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                        rhs.supportsApplyEditsWithGlobalIds))))
                                                                                                                                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                                                                                                                                        .maxRecordCount
                                                                                                                                                                                                                                                                                                                                                                                                                == rhs.maxRecordCount)
                                                                                                                                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                                                                                                                                .maxRecordCount
                                                                                                                                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                                                                                                                                        .maxRecordCount
                                                                                                                                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                rhs.maxRecordCount))))
                                                                                                                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                                                                                                                .type
                                                                                                                                                                                                                                                                                                                                                                                                        == rhs.type)
                                                                                                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                                                                                                        .type
                                                                                                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                                                                                                .type
                                                                                                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                                                                                                        rhs.type))))
                                                                                                                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                                                                                                                        .hasStaticData
                                                                                                                                                                                                                                                                                                                                                                                                == rhs.hasStaticData)
                                                                                                                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                                                                                                                .hasStaticData
                                                                                                                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                                                                                                                        .hasStaticData
                                                                                                                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                                                                                                                rhs.hasStaticData))))
                                                                                                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                                                                                                .editingInfo
                                                                                                                                                                                                                                                                                                                                                                                        == rhs.editingInfo)
                                                                                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                                                                                        .editingInfo
                                                                                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                                                                                .editingInfo
                                                                                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                                                                                        rhs.editingInfo))))
                                                                                                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                                                                                                        .supportsAttachmentsByUploadId
                                                                                                                                                                                                                                                                                                                                                                                == rhs.supportsAttachmentsByUploadId)
                                                                                                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                                                                                                .supportsAttachmentsByUploadId
                                                                                                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                                                                                                        .supportsAttachmentsByUploadId
                                                                                                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                                                                                                rhs.supportsAttachmentsByUploadId))))
                                                                                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                                                                                .relationships
                                                                                                                                                                                                                                                                                                                                                                        == rhs.relationships)
                                                                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                                                                        .relationships
                                                                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                                                                .relationships
                                                                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                                                                        rhs.relationships))))
                                                                                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                                                                                        .id
                                                                                                                                                                                                                                                                                                                                                                == rhs.id)
                                                                                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                                                                                .id
                                                                                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                                                                                        .id
                                                                                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                                                                                rhs.id))))
                                                                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                                                                .supportsStatistics
                                                                                                                                                                                                                                                                                                                                                        == rhs.supportsStatistics)
                                                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                                                        .supportsStatistics
                                                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                                                .supportsStatistics
                                                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                                                        rhs.supportsStatistics))))
                                                                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                                                                        .standardMaxRecordCount
                                                                                                                                                                                                                                                                                                                                                == rhs.standardMaxRecordCount)
                                                                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                                                                .standardMaxRecordCount
                                                                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                                                                        .standardMaxRecordCount
                                                                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                                                                rhs.standardMaxRecordCount))))
                                                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                                                .isDataVersioned
                                                                                                                                                                                                                                                                                                                                        == rhs.isDataVersioned)
                                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                                        .isDataVersioned
                                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                                .isDataVersioned
                                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                                        rhs.isDataVersioned))))
                                                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                                                        .templates
                                                                                                                                                                                                                                                                                                                                == rhs.templates)
                                                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                                                .templates
                                                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                                                        .templates
                                                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                                                rhs.templates))))
                                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                                .supportsAdvancedQueries
                                                                                                                                                                                                                                                                                                                        == rhs.supportsAdvancedQueries)
                                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                                        .supportsAdvancedQueries
                                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                                .supportsAdvancedQueries
                                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                                        rhs.supportsAdvancedQueries))))
                                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                                        .currentVersion
                                                                                                                                                                                                                                                                                                                == rhs.currentVersion)
                                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                                .currentVersion
                                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                                        .currentVersion
                                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                                rhs.currentVersion))))
                                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                                .hasZ
                                                                                                                                                                                                                                                                                                        == rhs.hasZ)
                                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                                        .hasZ
                                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                                .hasZ
                                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                                        rhs.hasZ))))
                                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                                        .objectIdField
                                                                                                                                                                                                                                                                                                == rhs.objectIdField)
                                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                                .objectIdField
                                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                                        .objectIdField
                                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                                rhs.objectIdField))))
                                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                                .name
                                                                                                                                                                                                                                                                                        == rhs.name)
                                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                                        .name
                                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                                .name
                                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                                        rhs.name))))
                                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                                        .displayField
                                                                                                                                                                                                                                                                                == rhs.displayField)
                                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                                .displayField
                                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                                        .displayField
                                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                                rhs.displayField))))
                                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                                .supportsCalculate
                                                                                                                                                                                                                                                                        == rhs.supportsCalculate)
                                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                                        .supportsCalculate
                                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                                .supportsCalculate
                                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                                        rhs.supportsCalculate))))
                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                        .fields
                                                                                                                                                                                                                                                                == rhs.fields)
                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                .fields
                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                        .fields
                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                rhs.fields))))
                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                .maxRecordCountFactor
                                                                                                                                                                                                                                                        == rhs.maxRecordCountFactor)
                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                        .maxRecordCountFactor
                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                .maxRecordCountFactor
                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                        rhs.maxRecordCountFactor))))
                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                        .hasM
                                                                                                                                                                                                                                                == rhs.hasM)
                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                .hasM
                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                        .hasM
                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                rhs.hasM))))
                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                .allowGeometryUpdates
                                                                                                                                                                                                                                        == rhs.allowGeometryUpdates)
                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                        .allowGeometryUpdates
                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                .allowGeometryUpdates
                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                        rhs.allowGeometryUpdates))))
                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                        .useStandardizedQueries
                                                                                                                                                                                                                                == rhs.useStandardizedQueries)
                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                .useStandardizedQueries
                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                        .useStandardizedQueries
                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                rhs.useStandardizedQueries))))
                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                .globalIdField
                                                                                                                                                                                                                        == rhs.globalIdField)
                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                        .globalIdField
                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                .globalIdField
                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                        rhs.globalIdField))))
                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                        .description
                                                                                                                                                                                                                == rhs.description)
                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                .description
                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                && this
                                                                                                                                                                                                                        .description
                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                rhs.description))))
                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                .supportsRollbackOnFailureParameter
                                                                                                                                                                                                        == rhs.supportsRollbackOnFailureParameter)
                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                        .supportsRollbackOnFailureParameter
                                                                                                                                                                                                                != null)
                                                                                                                                                                                                        && this
                                                                                                                                                                                                                .supportsRollbackOnFailureParameter
                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                        rhs.supportsRollbackOnFailureParameter))))
                                                                                                                                                                                && ((this
                                                                                                                                                                                                        .supportsTruncate
                                                                                                                                                                                                == rhs.supportsTruncate)
                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                .supportsTruncate
                                                                                                                                                                                                        != null)
                                                                                                                                                                                                && this
                                                                                                                                                                                                        .supportsTruncate
                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                rhs.supportsTruncate))))
                                                                                                                                                                        && ((this
                                                                                                                                                                                                .indexes
                                                                                                                                                                                        == rhs.indexes)
                                                                                                                                                                                || ((this
                                                                                                                                                                                                        .indexes
                                                                                                                                                                                                != null)
                                                                                                                                                                                        && this
                                                                                                                                                                                                .indexes
                                                                                                                                                                                                .equals(
                                                                                                                                                                                                        rhs.indexes))))
                                                                                                                                                                && ((this
                                                                                                                                                                                        .htmlPopupType
                                                                                                                                                                                == rhs.htmlPopupType)
                                                                                                                                                                        || ((this
                                                                                                                                                                                                .htmlPopupType
                                                                                                                                                                                        != null)
                                                                                                                                                                                && this
                                                                                                                                                                                        .htmlPopupType
                                                                                                                                                                                        .equals(
                                                                                                                                                                                                rhs.htmlPopupType))))
                                                                                                                                                        && ((this
                                                                                                                                                                                .minScale
                                                                                                                                                                        == rhs.minScale)
                                                                                                                                                                || ((this
                                                                                                                                                                                        .minScale
                                                                                                                                                                                != null)
                                                                                                                                                                        && this
                                                                                                                                                                                .minScale
                                                                                                                                                                                .equals(
                                                                                                                                                                                        rhs.minScale))))
                                                                                                                                                && ((this
                                                                                                                                                                        .hasAttachments
                                                                                                                                                                == rhs.hasAttachments)
                                                                                                                                                        || ((this
                                                                                                                                                                                .hasAttachments
                                                                                                                                                                        != null)
                                                                                                                                                                && this
                                                                                                                                                                        .hasAttachments
                                                                                                                                                                        .equals(
                                                                                                                                                                                rhs.hasAttachments))))
                                                                                                                                        && ((this
                                                                                                                                                                .advancedQueryCapabilities
                                                                                                                                                        == rhs.advancedQueryCapabilities)
                                                                                                                                                || ((this
                                                                                                                                                                        .advancedQueryCapabilities
                                                                                                                                                                != null)
                                                                                                                                                        && this
                                                                                                                                                                .advancedQueryCapabilities
                                                                                                                                                                .equals(
                                                                                                                                                                        rhs.advancedQueryCapabilities))))
                                                                                                                                && ((this
                                                                                                                                                        .serviceItemId
                                                                                                                                                == rhs.serviceItemId)
                                                                                                                                        || ((this
                                                                                                                                                                .serviceItemId
                                                                                                                                                        != null)
                                                                                                                                                && this
                                                                                                                                                        .serviceItemId
                                                                                                                                                        .equals(
                                                                                                                                                                rhs.serviceItemId))))
                                                                                                                        && ((this
                                                                                                                                                .supportsValidateSql
                                                                                                                                        == rhs.supportsValidateSql)
                                                                                                                                || ((this
                                                                                                                                                        .supportsValidateSql
                                                                                                                                                != null)
                                                                                                                                        && this
                                                                                                                                                .supportsValidateSql
                                                                                                                                                .equals(
                                                                                                                                                        rhs.supportsValidateSql))))
                                                                                                                && ((this
                                                                                                                                        .supportsCoordinatesQuantization
                                                                                                                                == rhs.supportsCoordinatesQuantization)
                                                                                                                        || ((this
                                                                                                                                                .supportsCoordinatesQuantization
                                                                                                                                        != null)
                                                                                                                                && this
                                                                                                                                        .supportsCoordinatesQuantization
                                                                                                                                        .equals(
                                                                                                                                                rhs.supportsCoordinatesQuantization))))
                                                                                                        && ((this
                                                                                                                                .types
                                                                                                                        == rhs.types)
                                                                                                                || ((this
                                                                                                                                        .types
                                                                                                                                != null)
                                                                                                                        && this
                                                                                                                                .types
                                                                                                                                .equals(
                                                                                                                                        rhs.types))))
                                                                                                && ((this
                                                                                                                        .capabilities
                                                                                                                == rhs.capabilities)
                                                                                                        || ((this
                                                                                                                                .capabilities
                                                                                                                        != null)
                                                                                                                && this
                                                                                                                        .capabilities
                                                                                                                        .equals(
                                                                                                                                rhs.capabilities))))
                                                                                        && ((this
                                                                                                                .maxScale
                                                                                                        == rhs.maxScale)
                                                                                                || ((this
                                                                                                                        .maxScale
                                                                                                                != null)
                                                                                                        && this
                                                                                                                .maxScale
                                                                                                                .equals(
                                                                                                                        rhs.maxScale))))
                                                                                && ((this
                                                                                                        .defaultVisibility
                                                                                                == rhs.defaultVisibility)
                                                                                        || ((this
                                                                                                                .defaultVisibility
                                                                                                        != null)
                                                                                                && this
                                                                                                        .defaultVisibility
                                                                                                        .equals(
                                                                                                                rhs.defaultVisibility))))
                                                                        && ((this.tileMaxRecordCount
                                                                                        == rhs.tileMaxRecordCount)
                                                                                || ((this
                                                                                                        .tileMaxRecordCount
                                                                                                != null)
                                                                                        && this
                                                                                                .tileMaxRecordCount
                                                                                                .equals(
                                                                                                        rhs.tileMaxRecordCount))))
                                                                && ((this.typeIdField
                                                                                == rhs.typeIdField)
                                                                        || ((this.typeIdField
                                                                                        != null)
                                                                                && this.typeIdField
                                                                                        .equals(
                                                                                                rhs.typeIdField))))
                                                        && ((this.multiScaleGeometryInfo
                                                                        == rhs.multiScaleGeometryInfo)
                                                                || ((this.multiScaleGeometryInfo
                                                                                != null)
                                                                        && this
                                                                                .multiScaleGeometryInfo
                                                                                .equals(
                                                                                        rhs.multiScaleGeometryInfo))))
                                                && ((this.supportedQueryFormats
                                                                == rhs.supportedQueryFormats)
                                                        || ((this.supportedQueryFormats != null)
                                                                && this.supportedQueryFormats
                                                                        .equals(
                                                                                rhs.supportedQueryFormats))))
                                        && ((this.supportsMultiScaleGeometry
                                                        == rhs.supportsMultiScaleGeometry)
                                                || ((this.supportsMultiScaleGeometry != null)
                                                        && this.supportsMultiScaleGeometry.equals(
                                                                rhs.supportsMultiScaleGeometry))))
                                && ((this.drawingInfo == rhs.drawingInfo)
                                        || ((this.drawingInfo != null)
                                                && this.drawingInfo.equals(rhs.drawingInfo))))
                        && ((this.copyrightText == rhs.copyrightText)
                                || ((this.copyrightText != null)
                                        && this.copyrightText.equals(rhs.copyrightText))))
                && ((this.geometryType == rhs.geometryType)
                        || ((this.geometryType != null)
                                && this.geometryType.equals(rhs.geometryType))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        return ((((((((((((((((((((((((((((((((((((((((((((((((((this.extent == rhs.extent)||((this.extent!= null)&&this.extent.equals(rhs.extent)))&&((this.supportsApplyEditsWithGlobalIds == rhs.supportsApplyEditsWithGlobalIds)||((this.supportsApplyEditsWithGlobalIds!= null)&&this.supportsApplyEditsWithGlobalIds.equals(rhs.supportsApplyEditsWithGlobalIds))))&&((this.maxRecordCount == rhs.maxRecordCount)||((this.maxRecordCount!= null)&&this.maxRecordCount.equals(rhs.maxRecordCount))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.hasStaticData == rhs.hasStaticData)||((this.hasStaticData!= null)&&this.hasStaticData.equals(rhs.hasStaticData))))&&((this.editingInfo == rhs.editingInfo)||((this.editingInfo!= null)&&this.editingInfo.equals(rhs.editingInfo))))&&((this.supportsAttachmentsByUploadId == rhs.supportsAttachmentsByUploadId)||((this.supportsAttachmentsByUploadId!= null)&&this.supportsAttachmentsByUploadId.equals(rhs.supportsAttachmentsByUploadId))))&&((this.relationships == rhs.relationships)||((this.relationships!= null)&&this.relationships.equals(rhs.relationships))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.supportsStatistics == rhs.supportsStatistics)||((this.supportsStatistics!= null)&&this.supportsStatistics.equals(rhs.supportsStatistics))))&&((this.standardMaxRecordCount == rhs.standardMaxRecordCount)||((this.standardMaxRecordCount!= null)&&this.standardMaxRecordCount.equals(rhs.standardMaxRecordCount))))&&((this.isDataVersioned == rhs.isDataVersioned)||((this.isDataVersioned!= null)&&this.isDataVersioned.equals(rhs.isDataVersioned))))&&((this.templates == rhs.templates)||((this.templates!= null)&&this.templates.equals(rhs.templates))))&&((this.supportsAdvancedQueries == rhs.supportsAdvancedQueries)||((this.supportsAdvancedQueries!= null)&&this.supportsAdvancedQueries.equals(rhs.supportsAdvancedQueries))))&&((this.currentVersion == rhs.currentVersion)||((this.currentVersion!= null)&&this.currentVersion.equals(rhs.currentVersion))))&&((this.hasZ == rhs.hasZ)||((this.hasZ!= null)&&this.hasZ.equals(rhs.hasZ))))&&((this.objectIdField == rhs.objectIdField)||((this.objectIdField!= null)&&this.objectIdField.equals(rhs.objectIdField))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.displayField == rhs.displayField)||((this.displayField!= null)&&this.displayField.equals(rhs.displayField))))&&((this.supportsCalculate == rhs.supportsCalculate)||((this.supportsCalculate!= null)&&this.supportsCalculate.equals(rhs.supportsCalculate))))&&((this.fields == rhs.fields)||((this.fields!= null)&&this.fields.equals(rhs.fields))))&&((this.maxRecordCountFactor == rhs.maxRecordCountFactor)||((this.maxRecordCountFactor!= null)&&this.maxRecordCountFactor.equals(rhs.maxRecordCountFactor))))&&((this.hasM == rhs.hasM)||((this.hasM!= null)&&this.hasM.equals(rhs.hasM))))&&((this.allowGeometryUpdates == rhs.allowGeometryUpdates)||((this.allowGeometryUpdates!= null)&&this.allowGeometryUpdates.equals(rhs.allowGeometryUpdates))))&&((this.useStandardizedQueries == rhs.useStandardizedQueries)||((this.useStandardizedQueries!= null)&&this.useStandardizedQueries.equals(rhs.useStandardizedQueries))))&&((this.globalIdField == rhs.globalIdField)||((this.globalIdField!= null)&&this.globalIdField.equals(rhs.globalIdField))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.supportsRollbackOnFailureParameter == rhs.supportsRollbackOnFailureParameter)||((this.supportsRollbackOnFailureParameter!= null)&&this.supportsRollbackOnFailureParameter.equals(rhs.supportsRollbackOnFailureParameter))))&&((this.supportsTruncate == rhs.supportsTruncate)||((this.supportsTruncate!= null)&&this.supportsTruncate.equals(rhs.supportsTruncate))))&&((this.indexes == rhs.indexes)||((this.indexes!= null)&&this.indexes.equals(rhs.indexes))))&&((this.htmlPopupType == rhs.htmlPopupType)||((this.htmlPopupType!= null)&&this.htmlPopupType.equals(rhs.htmlPopupType))))&&((this.minScale == rhs.minScale)||((this.minScale!= null)&&this.minScale.equals(rhs.minScale))))&&((this.hasAttachments == rhs.hasAttachments)||((this.hasAttachments!= null)&&this.hasAttachments.equals(rhs.hasAttachments))))&&((this.advancedQueryCapabilities == rhs.advancedQueryCapabilities)||((this.advancedQueryCapabilities!= null)&&this.advancedQueryCapabilities.equals(rhs.advancedQueryCapabilities))))&&((this.serviceItemId == rhs.serviceItemId)||((this.serviceItemId!= null)&&this.serviceItemId.equals(rhs.serviceItemId))))&&((this.supportsValidateSql == rhs.supportsValidateSql)||((this.supportsValidateSql!= null)&&this.supportsValidateSql.equals(rhs.supportsValidateSql))))&&((this.supportsCoordinatesQuantization == rhs.supportsCoordinatesQuantization)||((this.supportsCoordinatesQuantization!= null)&&this.supportsCoordinatesQuantization.equals(rhs.supportsCoordinatesQuantization))))&&((this.types == rhs.types)||((this.types!= null)&&this.types.equals(rhs.types))))&&((this.capabilities == rhs.capabilities)||((this.capabilities!= null)&&this.capabilities.equals(rhs.capabilities))))&&((this.maxScale == rhs.maxScale)||((this.maxScale!= null)&&this.maxScale.equals(rhs.maxScale))))&&((this.defaultVisibility == rhs.defaultVisibility)||((this.defaultVisibility!= null)&&this.defaultVisibility.equals(rhs.defaultVisibility))))&&((this.tileMaxRecordCount == rhs.tileMaxRecordCount)||((this.tileMaxRecordCount!= null)&&this.tileMaxRecordCount.equals(rhs.tileMaxRecordCount))))&&((this.typeIdField == rhs.typeIdField)||((this.typeIdField!= null)&&this.typeIdField.equals(rhs.typeIdField))))&&((this.multiScaleGeometryInfo == rhs.multiScaleGeometryInfo)||((this.multiScaleGeometryInfo!= null)&&this.multiScaleGeometryInfo.equals(rhs.multiScaleGeometryInfo))))&&((this.supportedQueryFormats == rhs.supportedQueryFormats)||((this.supportedQueryFormats!= null)&&this.supportedQueryFormats.equals(rhs.supportedQueryFormats))))&&((this.supportsMultiScaleGeometry == rhs.supportsMultiScaleGeometry)||((this.supportsMultiScaleGeometry!= null)&&this.supportsMultiScaleGeometry.equals(rhs.supportsMultiScaleGeometry))))&&((this.drawingInfo == rhs.drawingInfo)||((this.drawingInfo!= null)&&this.drawingInfo.equals(rhs.drawingInfo))))&&((this.copyrightText == rhs.copyrightText)||((this.copyrightText!= null)&&this.copyrightText.equals(rhs.copyrightText))))&&((this.geometryType == rhs.geometryType)||((this.geometryType!= null)&&this.geometryType.equals(rhs.geometryType))));
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
