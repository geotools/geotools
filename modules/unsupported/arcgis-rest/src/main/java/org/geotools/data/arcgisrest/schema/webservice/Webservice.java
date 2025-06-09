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

package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

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
    private List<Object> relationships = new ArrayList<>();
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
    private List<Field> fields = new ArrayList<>();
    /** (Required) */
    @SerializedName("indexes")
    @Expose
    private List<Index> indexes = new ArrayList<>();
    /** (Required) */
    @SerializedName("types")
    @Expose
    private List<Object> types = new ArrayList<>();
    /** (Required) */
    @SerializedName("templates")
    @Expose
    private List<Template> templates = new ArrayList<>();
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

    /** (Required) */
    public Double getCurrentVersion() {
        return currentVersion;
    }

    /** (Required) */
    public void setCurrentVersion(Double currentVersion) {
        this.currentVersion = currentVersion;
    }

    /** (Required) */
    public Integer getId() {
        return id;
    }

    /** (Required) */
    public void setId(Integer id) {
        this.id = id;
    }

    /** (Required) */
    public String getName() {
        return name;
    }

    /** (Required) */
    public void setName(String name) {
        this.name = name;
    }

    /** (Required) */
    public String getType() {
        return type;
    }

    /** (Required) */
    public void setType(String type) {
        this.type = type;
    }

    /** (Required) */
    public String getServiceItemId() {
        return serviceItemId;
    }

    /** (Required) */
    public void setServiceItemId(String serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    /** (Required) */
    public String getDisplayField() {
        return displayField;
    }

    /** (Required) */
    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    /** (Required) */
    public String getDescription() {
        return description;
    }

    /** (Required) */
    public void setDescription(String description) {
        this.description = description;
    }

    /** (Required) */
    public String getCopyrightText() {
        return copyrightText;
    }

    /** (Required) */
    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    /** (Required) */
    public Boolean getDefaultVisibility() {
        return defaultVisibility;
    }

    /** (Required) */
    public void setDefaultVisibility(Boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    /** (Required) */
    public EditingInfo getEditingInfo() {
        return editingInfo;
    }

    /** (Required) */
    public void setEditingInfo(EditingInfo editingInfo) {
        this.editingInfo = editingInfo;
    }

    /** (Required) */
    public MultiScaleGeometryInfo getMultiScaleGeometryInfo() {
        return multiScaleGeometryInfo;
    }

    /** (Required) */
    public void setMultiScaleGeometryInfo(MultiScaleGeometryInfo multiScaleGeometryInfo) {
        this.multiScaleGeometryInfo = multiScaleGeometryInfo;
    }

    /** (Required) */
    public List<Object> getRelationships() {
        return relationships;
    }

    /** (Required) */
    public void setRelationships(List<Object> relationships) {
        this.relationships = relationships;
    }

    /** (Required) */
    public Boolean getIsDataVersioned() {
        return isDataVersioned;
    }

    /** (Required) */
    public void setIsDataVersioned(Boolean isDataVersioned) {
        this.isDataVersioned = isDataVersioned;
    }

    /** (Required) */
    public Boolean getSupportsCalculate() {
        return supportsCalculate;
    }

    /** (Required) */
    public void setSupportsCalculate(Boolean supportsCalculate) {
        this.supportsCalculate = supportsCalculate;
    }

    /** (Required) */
    public Boolean getSupportsTruncate() {
        return supportsTruncate;
    }

    /** (Required) */
    public void setSupportsTruncate(Boolean supportsTruncate) {
        this.supportsTruncate = supportsTruncate;
    }

    /** (Required) */
    public Boolean getSupportsAttachmentsByUploadId() {
        return supportsAttachmentsByUploadId;
    }

    /** (Required) */
    public void setSupportsAttachmentsByUploadId(Boolean supportsAttachmentsByUploadId) {
        this.supportsAttachmentsByUploadId = supportsAttachmentsByUploadId;
    }

    /** (Required) */
    public Boolean getSupportsRollbackOnFailureParameter() {
        return supportsRollbackOnFailureParameter;
    }

    /** (Required) */
    public void setSupportsRollbackOnFailureParameter(Boolean supportsRollbackOnFailureParameter) {
        this.supportsRollbackOnFailureParameter = supportsRollbackOnFailureParameter;
    }

    /** (Required) */
    public Boolean getSupportsStatistics() {
        return supportsStatistics;
    }

    /** (Required) */
    public void setSupportsStatistics(Boolean supportsStatistics) {
        this.supportsStatistics = supportsStatistics;
    }

    /** (Required) */
    public Boolean getSupportsAdvancedQueries() {
        return supportsAdvancedQueries;
    }

    /** (Required) */
    public void setSupportsAdvancedQueries(Boolean supportsAdvancedQueries) {
        this.supportsAdvancedQueries = supportsAdvancedQueries;
    }

    /** (Required) */
    public Boolean getSupportsValidateSql() {
        return supportsValidateSql;
    }

    /** (Required) */
    public void setSupportsValidateSql(Boolean supportsValidateSql) {
        this.supportsValidateSql = supportsValidateSql;
    }

    /** (Required) */
    public Boolean getSupportsCoordinatesQuantization() {
        return supportsCoordinatesQuantization;
    }

    /** (Required) */
    public void setSupportsCoordinatesQuantization(Boolean supportsCoordinatesQuantization) {
        this.supportsCoordinatesQuantization = supportsCoordinatesQuantization;
    }

    /** (Required) */
    public Boolean getSupportsApplyEditsWithGlobalIds() {
        return supportsApplyEditsWithGlobalIds;
    }

    /** (Required) */
    public void setSupportsApplyEditsWithGlobalIds(Boolean supportsApplyEditsWithGlobalIds) {
        this.supportsApplyEditsWithGlobalIds = supportsApplyEditsWithGlobalIds;
    }

    /** (Required) */
    public Boolean getSupportsMultiScaleGeometry() {
        return supportsMultiScaleGeometry;
    }

    /** (Required) */
    public void setSupportsMultiScaleGeometry(Boolean supportsMultiScaleGeometry) {
        this.supportsMultiScaleGeometry = supportsMultiScaleGeometry;
    }

    /** (Required) */
    public AdvancedQueryCapabilities getAdvancedQueryCapabilities() {
        return advancedQueryCapabilities;
    }

    /** (Required) */
    public void setAdvancedQueryCapabilities(AdvancedQueryCapabilities advancedQueryCapabilities) {
        this.advancedQueryCapabilities = advancedQueryCapabilities;
    }

    /** (Required) */
    public Boolean getUseStandardizedQueries() {
        return useStandardizedQueries;
    }

    /** (Required) */
    public void setUseStandardizedQueries(Boolean useStandardizedQueries) {
        this.useStandardizedQueries = useStandardizedQueries;
    }

    /** (Required) */
    public String getGeometryType() {
        return geometryType;
    }

    /** (Required) */
    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    /** (Required) */
    public Integer getMinScale() {
        return minScale;
    }

    /** (Required) */
    public void setMinScale(Integer minScale) {
        this.minScale = minScale;
    }

    /** (Required) */
    public Integer getMaxScale() {
        return maxScale;
    }

    /** (Required) */
    public void setMaxScale(Integer maxScale) {
        this.maxScale = maxScale;
    }

    /** (Required) */
    public Extent getExtent() {
        return extent;
    }

    /** (Required) */
    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    /** (Required) */
    public DrawingInfo getDrawingInfo() {
        return drawingInfo;
    }

    /** (Required) */
    public void setDrawingInfo(DrawingInfo drawingInfo) {
        this.drawingInfo = drawingInfo;
    }

    /** (Required) */
    public Boolean getAllowGeometryUpdates() {
        return allowGeometryUpdates;
    }

    /** (Required) */
    public void setAllowGeometryUpdates(Boolean allowGeometryUpdates) {
        this.allowGeometryUpdates = allowGeometryUpdates;
    }

    /** (Required) */
    public Boolean getHasAttachments() {
        return hasAttachments;
    }

    /** (Required) */
    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    /** (Required) */
    public String getHtmlPopupType() {
        return htmlPopupType;
    }

    /** (Required) */
    public void setHtmlPopupType(String htmlPopupType) {
        this.htmlPopupType = htmlPopupType;
    }

    /** (Required) */
    public Boolean getHasM() {
        return hasM;
    }

    /** (Required) */
    public void setHasM(Boolean hasM) {
        this.hasM = hasM;
    }

    /** (Required) */
    public Boolean getHasZ() {
        return hasZ;
    }

    /** (Required) */
    public void setHasZ(Boolean hasZ) {
        this.hasZ = hasZ;
    }

    /** (Required) */
    public String getObjectIdField() {
        return objectIdField;
    }

    /** (Required) */
    public void setObjectIdField(String objectIdField) {
        this.objectIdField = objectIdField;
    }

    /** (Required) */
    public String getGlobalIdField() {
        return globalIdField;
    }

    /** (Required) */
    public void setGlobalIdField(String globalIdField) {
        this.globalIdField = globalIdField;
    }

    /** (Required) */
    public String getTypeIdField() {
        return typeIdField;
    }

    /** (Required) */
    public void setTypeIdField(String typeIdField) {
        this.typeIdField = typeIdField;
    }

    /** (Required) */
    public List<Field> getFields() {
        return fields;
    }

    /** (Required) */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /** (Required) */
    public List<Index> getIndexes() {
        return indexes;
    }

    /** (Required) */
    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    /** (Required) */
    public List<Object> getTypes() {
        return types;
    }

    /** (Required) */
    public void setTypes(List<Object> types) {
        this.types = types;
    }

    /** (Required) */
    public List<Template> getTemplates() {
        return templates;
    }

    /** (Required) */
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    /** (Required) */
    public String getSupportedQueryFormats() {
        return supportedQueryFormats;
    }

    /** (Required) */
    public void setSupportedQueryFormats(String supportedQueryFormats) {
        this.supportedQueryFormats = supportedQueryFormats;
    }

    /** (Required) */
    public Boolean getHasStaticData() {
        return hasStaticData;
    }

    /** (Required) */
    public void setHasStaticData(Boolean hasStaticData) {
        this.hasStaticData = hasStaticData;
    }

    /** (Required) */
    public Integer getMaxRecordCount() {
        return maxRecordCount;
    }

    /** (Required) */
    public void setMaxRecordCount(Integer maxRecordCount) {
        this.maxRecordCount = maxRecordCount;
    }

    /** (Required) */
    public Integer getStandardMaxRecordCount() {
        return standardMaxRecordCount;
    }

    /** (Required) */
    public void setStandardMaxRecordCount(Integer standardMaxRecordCount) {
        this.standardMaxRecordCount = standardMaxRecordCount;
    }

    /** (Required) */
    public Integer getTileMaxRecordCount() {
        return tileMaxRecordCount;
    }

    /** (Required) */
    public void setTileMaxRecordCount(Integer tileMaxRecordCount) {
        this.tileMaxRecordCount = tileMaxRecordCount;
    }

    /** (Required) */
    public Integer getMaxRecordCountFactor() {
        return maxRecordCountFactor;
    }

    /** (Required) */
    public void setMaxRecordCountFactor(Integer maxRecordCountFactor) {
        this.maxRecordCountFactor = maxRecordCountFactor;
    }

    /** (Required) */
    public String getCapabilities() {
        return capabilities;
    }

    /** (Required) */
    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }
}
