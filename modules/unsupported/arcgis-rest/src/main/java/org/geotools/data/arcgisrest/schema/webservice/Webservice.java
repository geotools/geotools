package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Webservice {

    /** (Required) */
    @SerializedName("currentVersion")
    @Expose
    private Double currentVersion;
    /** (Required) */
    @SerializedName("id")
    @Expose
    private Integer id;
    /** (Required) */
    @SerializedName("name")
    @Expose
    private String name;
    /** (Required) */
    @SerializedName("type")
    @Expose
    private String type;
    /** (Required) */
    @SerializedName("serviceItemId")
    @Expose
    private String serviceItemId;
    /** (Required) */
    @SerializedName("displayField")
    @Expose
    private String displayField;
    /** (Required) */
    @SerializedName("description")
    @Expose
    private String description;
    /** (Required) */
    @SerializedName("copyrightText")
    @Expose
    private String copyrightText;
    /** (Required) */
    @SerializedName("defaultVisibility")
    @Expose
    private Boolean defaultVisibility;
    /** (Required) */
    @SerializedName("editingInfo")
    @Expose
    private EditingInfo editingInfo;
    /** (Required) */
    @SerializedName("multiScaleGeometryInfo")
    @Expose
    private MultiScaleGeometryInfo multiScaleGeometryInfo;
    /** (Required) */
    @SerializedName("relationships")
    @Expose
    private List<Object> relationships = new ArrayList<Object>();
    /** (Required) */
    @SerializedName("isDataVersioned")
    @Expose
    private Boolean isDataVersioned;
    /** (Required) */
    @SerializedName("supportsCalculate")
    @Expose
    private Boolean supportsCalculate;
    /** (Required) */
    @SerializedName("supportsTruncate")
    @Expose
    private Boolean supportsTruncate;
    /** (Required) */
    @SerializedName("supportsAttachmentsByUploadId")
    @Expose
    private Boolean supportsAttachmentsByUploadId;
    /** (Required) */
    @SerializedName("supportsRollbackOnFailureParameter")
    @Expose
    private Boolean supportsRollbackOnFailureParameter;
    /** (Required) */
    @SerializedName("supportsStatistics")
    @Expose
    private Boolean supportsStatistics;
    /** (Required) */
    @SerializedName("supportsAdvancedQueries")
    @Expose
    private Boolean supportsAdvancedQueries;
    /** (Required) */
    @SerializedName("supportsValidateSql")
    @Expose
    private Boolean supportsValidateSql;
    /** (Required) */
    @SerializedName("supportsCoordinatesQuantization")
    @Expose
    private Boolean supportsCoordinatesQuantization;
    /** (Required) */
    @SerializedName("supportsApplyEditsWithGlobalIds")
    @Expose
    private Boolean supportsApplyEditsWithGlobalIds;
    /** (Required) */
    @SerializedName("supportsMultiScaleGeometry")
    @Expose
    private Boolean supportsMultiScaleGeometry;
    /** (Required) */
    @SerializedName("advancedQueryCapabilities")
    @Expose
    private AdvancedQueryCapabilities advancedQueryCapabilities;
    /** (Required) */
    @SerializedName("useStandardizedQueries")
    @Expose
    private Boolean useStandardizedQueries;
    /** (Required) */
    @SerializedName("geometryType")
    @Expose
    private String geometryType;
    /** (Required) */
    @SerializedName("minScale")
    @Expose
    private Integer minScale;
    /** (Required) */
    @SerializedName("maxScale")
    @Expose
    private Integer maxScale;
    /** (Required) */
    @SerializedName("extent")
    @Expose
    private Extent extent;
    /** (Required) */
    @SerializedName("drawingInfo")
    @Expose
    private DrawingInfo drawingInfo;
    /** (Required) */
    @SerializedName("allowGeometryUpdates")
    @Expose
    private Boolean allowGeometryUpdates;
    /** (Required) */
    @SerializedName("hasAttachments")
    @Expose
    private Boolean hasAttachments;
    /** (Required) */
    @SerializedName("htmlPopupType")
    @Expose
    private String htmlPopupType;
    /** (Required) */
    @SerializedName("hasM")
    @Expose
    private Boolean hasM;
    /** (Required) */
    @SerializedName("hasZ")
    @Expose
    private Boolean hasZ;
    /** (Required) */
    @SerializedName("objectIdField")
    @Expose
    private String objectIdField;
    /** (Required) */
    @SerializedName("globalIdField")
    @Expose
    private String globalIdField;
    /** (Required) */
    @SerializedName("typeIdField")
    @Expose
    private String typeIdField;
    /** (Required) */
    @SerializedName("fields")
    @Expose
    private List<Field> fields = new ArrayList<Field>();
    /** (Required) */
    @SerializedName("indexes")
    @Expose
    private List<Index> indexes = new ArrayList<Index>();
    /** (Required) */
    @SerializedName("types")
    @Expose
    private List<Object> types = new ArrayList<Object>();
    /** (Required) */
    @SerializedName("templates")
    @Expose
    private List<Template> templates = new ArrayList<Template>();
    /** (Required) */
    @SerializedName("supportedQueryFormats")
    @Expose
    private String supportedQueryFormats;
    /** (Required) */
    @SerializedName("hasStaticData")
    @Expose
    private Boolean hasStaticData;
    /** (Required) */
    @SerializedName("maxRecordCount")
    @Expose
    private Integer maxRecordCount;
    /** (Required) */
    @SerializedName("standardMaxRecordCount")
    @Expose
    private Integer standardMaxRecordCount;
    /** (Required) */
    @SerializedName("tileMaxRecordCount")
    @Expose
    private Integer tileMaxRecordCount;
    /** (Required) */
    @SerializedName("maxRecordCountFactor")
    @Expose
    private Integer maxRecordCountFactor;
    /** (Required) */
    @SerializedName("capabilities")
    @Expose
    private String capabilities;

    /**
     * (Required)
     *
     * @return The currentVersion
     */
    public Double getCurrentVersion() {
        return currentVersion;
    }

    /**
     * (Required)
     *
     * @param currentVersion The currentVersion
     */
    public void setCurrentVersion(Double currentVersion) {
        this.currentVersion = currentVersion;
    }

    /**
     * (Required)
     *
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * (Required)
     *
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * (Required)
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * (Required)
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * (Required)
     *
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * (Required)
     *
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * (Required)
     *
     * @return The serviceItemId
     */
    public String getServiceItemId() {
        return serviceItemId;
    }

    /**
     * (Required)
     *
     * @param serviceItemId The serviceItemId
     */
    public void setServiceItemId(String serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    /**
     * (Required)
     *
     * @return The displayField
     */
    public String getDisplayField() {
        return displayField;
    }

    /**
     * (Required)
     *
     * @param displayField The displayField
     */
    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    /**
     * (Required)
     *
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * (Required)
     *
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * (Required)
     *
     * @return The copyrightText
     */
    public String getCopyrightText() {
        return copyrightText;
    }

    /**
     * (Required)
     *
     * @param copyrightText The copyrightText
     */
    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    /**
     * (Required)
     *
     * @return The defaultVisibility
     */
    public Boolean getDefaultVisibility() {
        return defaultVisibility;
    }

    /**
     * (Required)
     *
     * @param defaultVisibility The defaultVisibility
     */
    public void setDefaultVisibility(Boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    /**
     * (Required)
     *
     * @return The editingInfo
     */
    public EditingInfo getEditingInfo() {
        return editingInfo;
    }

    /**
     * (Required)
     *
     * @param editingInfo The editingInfo
     */
    public void setEditingInfo(EditingInfo editingInfo) {
        this.editingInfo = editingInfo;
    }

    /**
     * (Required)
     *
     * @return The multiScaleGeometryInfo
     */
    public MultiScaleGeometryInfo getMultiScaleGeometryInfo() {
        return multiScaleGeometryInfo;
    }

    /**
     * (Required)
     *
     * @param multiScaleGeometryInfo The multiScaleGeometryInfo
     */
    public void setMultiScaleGeometryInfo(MultiScaleGeometryInfo multiScaleGeometryInfo) {
        this.multiScaleGeometryInfo = multiScaleGeometryInfo;
    }

    /**
     * (Required)
     *
     * @return The relationships
     */
    public List<Object> getRelationships() {
        return relationships;
    }

    /**
     * (Required)
     *
     * @param relationships The relationships
     */
    public void setRelationships(List<Object> relationships) {
        this.relationships = relationships;
    }

    /**
     * (Required)
     *
     * @return The isDataVersioned
     */
    public Boolean getIsDataVersioned() {
        return isDataVersioned;
    }

    /**
     * (Required)
     *
     * @param isDataVersioned The isDataVersioned
     */
    public void setIsDataVersioned(Boolean isDataVersioned) {
        this.isDataVersioned = isDataVersioned;
    }

    /**
     * (Required)
     *
     * @return The supportsCalculate
     */
    public Boolean getSupportsCalculate() {
        return supportsCalculate;
    }

    /**
     * (Required)
     *
     * @param supportsCalculate The supportsCalculate
     */
    public void setSupportsCalculate(Boolean supportsCalculate) {
        this.supportsCalculate = supportsCalculate;
    }

    /**
     * (Required)
     *
     * @return The supportsTruncate
     */
    public Boolean getSupportsTruncate() {
        return supportsTruncate;
    }

    /**
     * (Required)
     *
     * @param supportsTruncate The supportsTruncate
     */
    public void setSupportsTruncate(Boolean supportsTruncate) {
        this.supportsTruncate = supportsTruncate;
    }

    /**
     * (Required)
     *
     * @return The supportsAttachmentsByUploadId
     */
    public Boolean getSupportsAttachmentsByUploadId() {
        return supportsAttachmentsByUploadId;
    }

    /**
     * (Required)
     *
     * @param supportsAttachmentsByUploadId The supportsAttachmentsByUploadId
     */
    public void setSupportsAttachmentsByUploadId(Boolean supportsAttachmentsByUploadId) {
        this.supportsAttachmentsByUploadId = supportsAttachmentsByUploadId;
    }

    /**
     * (Required)
     *
     * @return The supportsRollbackOnFailureParameter
     */
    public Boolean getSupportsRollbackOnFailureParameter() {
        return supportsRollbackOnFailureParameter;
    }

    /**
     * (Required)
     *
     * @param supportsRollbackOnFailureParameter The supportsRollbackOnFailureParameter
     */
    public void setSupportsRollbackOnFailureParameter(Boolean supportsRollbackOnFailureParameter) {
        this.supportsRollbackOnFailureParameter = supportsRollbackOnFailureParameter;
    }

    /**
     * (Required)
     *
     * @return The supportsStatistics
     */
    public Boolean getSupportsStatistics() {
        return supportsStatistics;
    }

    /**
     * (Required)
     *
     * @param supportsStatistics The supportsStatistics
     */
    public void setSupportsStatistics(Boolean supportsStatistics) {
        this.supportsStatistics = supportsStatistics;
    }

    /**
     * (Required)
     *
     * @return The supportsAdvancedQueries
     */
    public Boolean getSupportsAdvancedQueries() {
        return supportsAdvancedQueries;
    }

    /**
     * (Required)
     *
     * @param supportsAdvancedQueries The supportsAdvancedQueries
     */
    public void setSupportsAdvancedQueries(Boolean supportsAdvancedQueries) {
        this.supportsAdvancedQueries = supportsAdvancedQueries;
    }

    /**
     * (Required)
     *
     * @return The supportsValidateSql
     */
    public Boolean getSupportsValidateSql() {
        return supportsValidateSql;
    }

    /**
     * (Required)
     *
     * @param supportsValidateSql The supportsValidateSql
     */
    public void setSupportsValidateSql(Boolean supportsValidateSql) {
        this.supportsValidateSql = supportsValidateSql;
    }

    /**
     * (Required)
     *
     * @return The supportsCoordinatesQuantization
     */
    public Boolean getSupportsCoordinatesQuantization() {
        return supportsCoordinatesQuantization;
    }

    /**
     * (Required)
     *
     * @param supportsCoordinatesQuantization The supportsCoordinatesQuantization
     */
    public void setSupportsCoordinatesQuantization(Boolean supportsCoordinatesQuantization) {
        this.supportsCoordinatesQuantization = supportsCoordinatesQuantization;
    }

    /**
     * (Required)
     *
     * @return The supportsApplyEditsWithGlobalIds
     */
    public Boolean getSupportsApplyEditsWithGlobalIds() {
        return supportsApplyEditsWithGlobalIds;
    }

    /**
     * (Required)
     *
     * @param supportsApplyEditsWithGlobalIds The supportsApplyEditsWithGlobalIds
     */
    public void setSupportsApplyEditsWithGlobalIds(Boolean supportsApplyEditsWithGlobalIds) {
        this.supportsApplyEditsWithGlobalIds = supportsApplyEditsWithGlobalIds;
    }

    /**
     * (Required)
     *
     * @return The supportsMultiScaleGeometry
     */
    public Boolean getSupportsMultiScaleGeometry() {
        return supportsMultiScaleGeometry;
    }

    /**
     * (Required)
     *
     * @param supportsMultiScaleGeometry The supportsMultiScaleGeometry
     */
    public void setSupportsMultiScaleGeometry(Boolean supportsMultiScaleGeometry) {
        this.supportsMultiScaleGeometry = supportsMultiScaleGeometry;
    }

    /**
     * (Required)
     *
     * @return The advancedQueryCapabilities
     */
    public AdvancedQueryCapabilities getAdvancedQueryCapabilities() {
        return advancedQueryCapabilities;
    }

    /**
     * (Required)
     *
     * @param advancedQueryCapabilities The advancedQueryCapabilities
     */
    public void setAdvancedQueryCapabilities(AdvancedQueryCapabilities advancedQueryCapabilities) {
        this.advancedQueryCapabilities = advancedQueryCapabilities;
    }

    /**
     * (Required)
     *
     * @return The useStandardizedQueries
     */
    public Boolean getUseStandardizedQueries() {
        return useStandardizedQueries;
    }

    /**
     * (Required)
     *
     * @param useStandardizedQueries The useStandardizedQueries
     */
    public void setUseStandardizedQueries(Boolean useStandardizedQueries) {
        this.useStandardizedQueries = useStandardizedQueries;
    }

    /**
     * (Required)
     *
     * @return The geometryType
     */
    public String getGeometryType() {
        return geometryType;
    }

    /**
     * (Required)
     *
     * @param geometryType The geometryType
     */
    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    /**
     * (Required)
     *
     * @return The minScale
     */
    public Integer getMinScale() {
        return minScale;
    }

    /**
     * (Required)
     *
     * @param minScale The minScale
     */
    public void setMinScale(Integer minScale) {
        this.minScale = minScale;
    }

    /**
     * (Required)
     *
     * @return The maxScale
     */
    public Integer getMaxScale() {
        return maxScale;
    }

    /**
     * (Required)
     *
     * @param maxScale The maxScale
     */
    public void setMaxScale(Integer maxScale) {
        this.maxScale = maxScale;
    }

    /**
     * (Required)
     *
     * @return The extent
     */
    public Extent getExtent() {
        return extent;
    }

    /**
     * (Required)
     *
     * @param extent The extent
     */
    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    /**
     * (Required)
     *
     * @return The drawingInfo
     */
    public DrawingInfo getDrawingInfo() {
        return drawingInfo;
    }

    /**
     * (Required)
     *
     * @param drawingInfo The drawingInfo
     */
    public void setDrawingInfo(DrawingInfo drawingInfo) {
        this.drawingInfo = drawingInfo;
    }

    /**
     * (Required)
     *
     * @return The allowGeometryUpdates
     */
    public Boolean getAllowGeometryUpdates() {
        return allowGeometryUpdates;
    }

    /**
     * (Required)
     *
     * @param allowGeometryUpdates The allowGeometryUpdates
     */
    public void setAllowGeometryUpdates(Boolean allowGeometryUpdates) {
        this.allowGeometryUpdates = allowGeometryUpdates;
    }

    /**
     * (Required)
     *
     * @return The hasAttachments
     */
    public Boolean getHasAttachments() {
        return hasAttachments;
    }

    /**
     * (Required)
     *
     * @param hasAttachments The hasAttachments
     */
    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    /**
     * (Required)
     *
     * @return The htmlPopupType
     */
    public String getHtmlPopupType() {
        return htmlPopupType;
    }

    /**
     * (Required)
     *
     * @param htmlPopupType The htmlPopupType
     */
    public void setHtmlPopupType(String htmlPopupType) {
        this.htmlPopupType = htmlPopupType;
    }

    /**
     * (Required)
     *
     * @return The hasM
     */
    public Boolean getHasM() {
        return hasM;
    }

    /**
     * (Required)
     *
     * @param hasM The hasM
     */
    public void setHasM(Boolean hasM) {
        this.hasM = hasM;
    }

    /**
     * (Required)
     *
     * @return The hasZ
     */
    public Boolean getHasZ() {
        return hasZ;
    }

    /**
     * (Required)
     *
     * @param hasZ The hasZ
     */
    public void setHasZ(Boolean hasZ) {
        this.hasZ = hasZ;
    }

    /**
     * (Required)
     *
     * @return The objectIdField
     */
    public String getObjectIdField() {
        return objectIdField;
    }

    /**
     * (Required)
     *
     * @param objectIdField The objectIdField
     */
    public void setObjectIdField(String objectIdField) {
        this.objectIdField = objectIdField;
    }

    /**
     * (Required)
     *
     * @return The globalIdField
     */
    public String getGlobalIdField() {
        return globalIdField;
    }

    /**
     * (Required)
     *
     * @param globalIdField The globalIdField
     */
    public void setGlobalIdField(String globalIdField) {
        this.globalIdField = globalIdField;
    }

    /**
     * (Required)
     *
     * @return The typeIdField
     */
    public String getTypeIdField() {
        return typeIdField;
    }

    /**
     * (Required)
     *
     * @param typeIdField The typeIdField
     */
    public void setTypeIdField(String typeIdField) {
        this.typeIdField = typeIdField;
    }

    /**
     * (Required)
     *
     * @return The fields
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * (Required)
     *
     * @param fields The fields
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /**
     * (Required)
     *
     * @return The indexes
     */
    public List<Index> getIndexes() {
        return indexes;
    }

    /**
     * (Required)
     *
     * @param indexes The indexes
     */
    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    /**
     * (Required)
     *
     * @return The types
     */
    public List<Object> getTypes() {
        return types;
    }

    /**
     * (Required)
     *
     * @param types The types
     */
    public void setTypes(List<Object> types) {
        this.types = types;
    }

    /**
     * (Required)
     *
     * @return The templates
     */
    public List<Template> getTemplates() {
        return templates;
    }

    /**
     * (Required)
     *
     * @param templates The templates
     */
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    /**
     * (Required)
     *
     * @return The supportedQueryFormats
     */
    public String getSupportedQueryFormats() {
        return supportedQueryFormats;
    }

    /**
     * (Required)
     *
     * @param supportedQueryFormats The supportedQueryFormats
     */
    public void setSupportedQueryFormats(String supportedQueryFormats) {
        this.supportedQueryFormats = supportedQueryFormats;
    }

    /**
     * (Required)
     *
     * @return The hasStaticData
     */
    public Boolean getHasStaticData() {
        return hasStaticData;
    }

    /**
     * (Required)
     *
     * @param hasStaticData The hasStaticData
     */
    public void setHasStaticData(Boolean hasStaticData) {
        this.hasStaticData = hasStaticData;
    }

    /**
     * (Required)
     *
     * @return The maxRecordCount
     */
    public Integer getMaxRecordCount() {
        return maxRecordCount;
    }

    /**
     * (Required)
     *
     * @param maxRecordCount The maxRecordCount
     */
    public void setMaxRecordCount(Integer maxRecordCount) {
        this.maxRecordCount = maxRecordCount;
    }

    /**
     * (Required)
     *
     * @return The standardMaxRecordCount
     */
    public Integer getStandardMaxRecordCount() {
        return standardMaxRecordCount;
    }

    /**
     * (Required)
     *
     * @param standardMaxRecordCount The standardMaxRecordCount
     */
    public void setStandardMaxRecordCount(Integer standardMaxRecordCount) {
        this.standardMaxRecordCount = standardMaxRecordCount;
    }

    /**
     * (Required)
     *
     * @return The tileMaxRecordCount
     */
    public Integer getTileMaxRecordCount() {
        return tileMaxRecordCount;
    }

    /**
     * (Required)
     *
     * @param tileMaxRecordCount The tileMaxRecordCount
     */
    public void setTileMaxRecordCount(Integer tileMaxRecordCount) {
        this.tileMaxRecordCount = tileMaxRecordCount;
    }

    /**
     * (Required)
     *
     * @return The maxRecordCountFactor
     */
    public Integer getMaxRecordCountFactor() {
        return maxRecordCountFactor;
    }

    /**
     * (Required)
     *
     * @param maxRecordCountFactor The maxRecordCountFactor
     */
    public void setMaxRecordCountFactor(Integer maxRecordCountFactor) {
        this.maxRecordCountFactor = maxRecordCountFactor;
    }

    /**
     * (Required)
     *
     * @return The capabilities
     */
    public String getCapabilities() {
        return capabilities;
    }

    /**
     * (Required)
     *
     * @param capabilities The capabilities
     */
    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(currentVersion)
                .append(id)
                .append(name)
                .append(type)
                .append(serviceItemId)
                .append(displayField)
                .append(description)
                .append(copyrightText)
                .append(defaultVisibility)
                .append(editingInfo)
                .append(multiScaleGeometryInfo)
                .append(relationships)
                .append(isDataVersioned)
                .append(supportsCalculate)
                .append(supportsTruncate)
                .append(supportsAttachmentsByUploadId)
                .append(supportsRollbackOnFailureParameter)
                .append(supportsStatistics)
                .append(supportsAdvancedQueries)
                .append(supportsValidateSql)
                .append(supportsCoordinatesQuantization)
                .append(supportsApplyEditsWithGlobalIds)
                .append(supportsMultiScaleGeometry)
                .append(advancedQueryCapabilities)
                .append(useStandardizedQueries)
                .append(geometryType)
                .append(minScale)
                .append(maxScale)
                .append(extent)
                .append(drawingInfo)
                .append(allowGeometryUpdates)
                .append(hasAttachments)
                .append(htmlPopupType)
                .append(hasM)
                .append(hasZ)
                .append(objectIdField)
                .append(globalIdField)
                .append(typeIdField)
                .append(fields)
                .append(indexes)
                .append(types)
                .append(templates)
                .append(supportedQueryFormats)
                .append(hasStaticData)
                .append(maxRecordCount)
                .append(standardMaxRecordCount)
                .append(tileMaxRecordCount)
                .append(maxRecordCountFactor)
                .append(capabilities)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(currentVersion, rhs.currentVersion)
                .append(id, rhs.id)
                .append(name, rhs.name)
                .append(type, rhs.type)
                .append(serviceItemId, rhs.serviceItemId)
                .append(displayField, rhs.displayField)
                .append(description, rhs.description)
                .append(copyrightText, rhs.copyrightText)
                .append(defaultVisibility, rhs.defaultVisibility)
                .append(editingInfo, rhs.editingInfo)
                .append(multiScaleGeometryInfo, rhs.multiScaleGeometryInfo)
                .append(relationships, rhs.relationships)
                .append(isDataVersioned, rhs.isDataVersioned)
                .append(supportsCalculate, rhs.supportsCalculate)
                .append(supportsTruncate, rhs.supportsTruncate)
                .append(supportsAttachmentsByUploadId, rhs.supportsAttachmentsByUploadId)
                .append(supportsRollbackOnFailureParameter, rhs.supportsRollbackOnFailureParameter)
                .append(supportsStatistics, rhs.supportsStatistics)
                .append(supportsAdvancedQueries, rhs.supportsAdvancedQueries)
                .append(supportsValidateSql, rhs.supportsValidateSql)
                .append(supportsCoordinatesQuantization, rhs.supportsCoordinatesQuantization)
                .append(supportsApplyEditsWithGlobalIds, rhs.supportsApplyEditsWithGlobalIds)
                .append(supportsMultiScaleGeometry, rhs.supportsMultiScaleGeometry)
                .append(advancedQueryCapabilities, rhs.advancedQueryCapabilities)
                .append(useStandardizedQueries, rhs.useStandardizedQueries)
                .append(geometryType, rhs.geometryType)
                .append(minScale, rhs.minScale)
                .append(maxScale, rhs.maxScale)
                .append(extent, rhs.extent)
                .append(drawingInfo, rhs.drawingInfo)
                .append(allowGeometryUpdates, rhs.allowGeometryUpdates)
                .append(hasAttachments, rhs.hasAttachments)
                .append(htmlPopupType, rhs.htmlPopupType)
                .append(hasM, rhs.hasM)
                .append(hasZ, rhs.hasZ)
                .append(objectIdField, rhs.objectIdField)
                .append(globalIdField, rhs.globalIdField)
                .append(typeIdField, rhs.typeIdField)
                .append(fields, rhs.fields)
                .append(indexes, rhs.indexes)
                .append(types, rhs.types)
                .append(templates, rhs.templates)
                .append(supportedQueryFormats, rhs.supportedQueryFormats)
                .append(hasStaticData, rhs.hasStaticData)
                .append(maxRecordCount, rhs.maxRecordCount)
                .append(standardMaxRecordCount, rhs.standardMaxRecordCount)
                .append(tileMaxRecordCount, rhs.tileMaxRecordCount)
                .append(maxRecordCountFactor, rhs.maxRecordCountFactor)
                .append(capabilities, rhs.capabilities)
                .isEquals();
    }
}
