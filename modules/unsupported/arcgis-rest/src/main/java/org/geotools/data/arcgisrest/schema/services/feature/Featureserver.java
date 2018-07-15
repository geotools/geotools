package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Featureserver {

    /** (Required) */
    @SerializedName("currentVersion")
    @Expose
    private Double currentVersion;
    /** (Required) */
    @SerializedName("serviceItemId")
    @Expose
    private String serviceItemId;
    /** (Required) */
    @SerializedName("serviceDescription")
    @Expose
    private String serviceDescription;
    /** (Required) */
    @SerializedName("hasVersionedData")
    @Expose
    private Boolean hasVersionedData;
    /** (Required) */
    @SerializedName("supportsDisconnectedEditing")
    @Expose
    private Boolean supportsDisconnectedEditing;
    /** (Required) */
    @SerializedName("hasStaticData")
    @Expose
    private Boolean hasStaticData;
    /** (Required) */
    @SerializedName("maxRecordCount")
    @Expose
    private Integer maxRecordCount;
    /** (Required) */
    @SerializedName("supportedQueryFormats")
    @Expose
    private String supportedQueryFormats;
    /** (Required) */
    @SerializedName("capabilities")
    @Expose
    private String capabilities;
    /** (Required) */
    @SerializedName("description")
    @Expose
    private String description;
    /** (Required) */
    @SerializedName("copyrightText")
    @Expose
    private String copyrightText;
    /** (Required) */
    @SerializedName("spatialReference")
    @Expose
    private SpatialReference spatialReference;
    /** (Required) */
    @SerializedName("initialExtent")
    @Expose
    private InitialExtent initialExtent;
    /** (Required) */
    @SerializedName("fullExtent")
    @Expose
    private FullExtent fullExtent;
    /** (Required) */
    @SerializedName("allowGeometryUpdates")
    @Expose
    private Boolean allowGeometryUpdates;
    /** (Required) */
    @SerializedName("units")
    @Expose
    private String units;
    /** (Required) */
    @SerializedName("size")
    @Expose
    private Integer size;
    /** (Required) */
    @SerializedName("syncEnabled")
    @Expose
    private Boolean syncEnabled;
    /** (Required) */
    @SerializedName("syncCapabilities")
    @Expose
    private SyncCapabilities syncCapabilities;
    /** (Required) */
    @SerializedName("supportsApplyEditsWithGlobalIds")
    @Expose
    private Boolean supportsApplyEditsWithGlobalIds;
    /** (Required) */
    @SerializedName("editorTrackingInfo")
    @Expose
    private EditorTrackingInfo editorTrackingInfo;
    /** (Required) */
    @SerializedName("xssPreventionInfo")
    @Expose
    private XssPreventionInfo xssPreventionInfo;
    /** (Required) */
    @SerializedName("layers")
    @Expose
    private List<Layer> layers = new ArrayList<Layer>();
    /** (Required) */
    @SerializedName("tables")
    @Expose
    private List<Object> tables = new ArrayList<Object>();

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
     * @return The serviceDescription
     */
    public String getServiceDescription() {
        return serviceDescription;
    }

    /**
     * (Required)
     *
     * @param serviceDescription The serviceDescription
     */
    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    /**
     * (Required)
     *
     * @return The hasVersionedData
     */
    public Boolean getHasVersionedData() {
        return hasVersionedData;
    }

    /**
     * (Required)
     *
     * @param hasVersionedData The hasVersionedData
     */
    public void setHasVersionedData(Boolean hasVersionedData) {
        this.hasVersionedData = hasVersionedData;
    }

    /**
     * (Required)
     *
     * @return The supportsDisconnectedEditing
     */
    public Boolean getSupportsDisconnectedEditing() {
        return supportsDisconnectedEditing;
    }

    /**
     * (Required)
     *
     * @param supportsDisconnectedEditing The supportsDisconnectedEditing
     */
    public void setSupportsDisconnectedEditing(Boolean supportsDisconnectedEditing) {
        this.supportsDisconnectedEditing = supportsDisconnectedEditing;
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
     * @return The spatialReference
     */
    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    /**
     * (Required)
     *
     * @param spatialReference The spatialReference
     */
    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    /**
     * (Required)
     *
     * @return The initialExtent
     */
    public InitialExtent getInitialExtent() {
        return initialExtent;
    }

    /**
     * (Required)
     *
     * @param initialExtent The initialExtent
     */
    public void setInitialExtent(InitialExtent initialExtent) {
        this.initialExtent = initialExtent;
    }

    /**
     * (Required)
     *
     * @return The fullExtent
     */
    public FullExtent getFullExtent() {
        return fullExtent;
    }

    /**
     * (Required)
     *
     * @param fullExtent The fullExtent
     */
    public void setFullExtent(FullExtent fullExtent) {
        this.fullExtent = fullExtent;
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
     * @return The units
     */
    public String getUnits() {
        return units;
    }

    /**
     * (Required)
     *
     * @param units The units
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * (Required)
     *
     * @return The size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * (Required)
     *
     * @param size The size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * (Required)
     *
     * @return The syncEnabled
     */
    public Boolean getSyncEnabled() {
        return syncEnabled;
    }

    /**
     * (Required)
     *
     * @param syncEnabled The syncEnabled
     */
    public void setSyncEnabled(Boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    /**
     * (Required)
     *
     * @return The syncCapabilities
     */
    public SyncCapabilities getSyncCapabilities() {
        return syncCapabilities;
    }

    /**
     * (Required)
     *
     * @param syncCapabilities The syncCapabilities
     */
    public void setSyncCapabilities(SyncCapabilities syncCapabilities) {
        this.syncCapabilities = syncCapabilities;
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
     * @return The editorTrackingInfo
     */
    public EditorTrackingInfo getEditorTrackingInfo() {
        return editorTrackingInfo;
    }

    /**
     * (Required)
     *
     * @param editorTrackingInfo The editorTrackingInfo
     */
    public void setEditorTrackingInfo(EditorTrackingInfo editorTrackingInfo) {
        this.editorTrackingInfo = editorTrackingInfo;
    }

    /**
     * (Required)
     *
     * @return The xssPreventionInfo
     */
    public XssPreventionInfo getXssPreventionInfo() {
        return xssPreventionInfo;
    }

    /**
     * (Required)
     *
     * @param xssPreventionInfo The xssPreventionInfo
     */
    public void setXssPreventionInfo(XssPreventionInfo xssPreventionInfo) {
        this.xssPreventionInfo = xssPreventionInfo;
    }

    /**
     * (Required)
     *
     * @return The layers
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * (Required)
     *
     * @param layers The layers
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    /**
     * (Required)
     *
     * @return The tables
     */
    public List<Object> getTables() {
        return tables;
    }

    /**
     * (Required)
     *
     * @param tables The tables
     */
    public void setTables(List<Object> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(currentVersion)
                .append(serviceItemId)
                .append(serviceDescription)
                .append(hasVersionedData)
                .append(supportsDisconnectedEditing)
                .append(hasStaticData)
                .append(maxRecordCount)
                .append(supportedQueryFormats)
                .append(capabilities)
                .append(description)
                .append(copyrightText)
                .append(spatialReference)
                .append(initialExtent)
                .append(fullExtent)
                .append(allowGeometryUpdates)
                .append(units)
                .append(size)
                .append(syncEnabled)
                .append(syncCapabilities)
                .append(supportsApplyEditsWithGlobalIds)
                .append(editorTrackingInfo)
                .append(xssPreventionInfo)
                .append(layers)
                .append(tables)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Featureserver) == false) {
            return false;
        }
        Featureserver rhs = ((Featureserver) other);
        return new EqualsBuilder()
                .append(currentVersion, rhs.currentVersion)
                .append(serviceItemId, rhs.serviceItemId)
                .append(serviceDescription, rhs.serviceDescription)
                .append(hasVersionedData, rhs.hasVersionedData)
                .append(supportsDisconnectedEditing, rhs.supportsDisconnectedEditing)
                .append(hasStaticData, rhs.hasStaticData)
                .append(maxRecordCount, rhs.maxRecordCount)
                .append(supportedQueryFormats, rhs.supportedQueryFormats)
                .append(capabilities, rhs.capabilities)
                .append(description, rhs.description)
                .append(copyrightText, rhs.copyrightText)
                .append(spatialReference, rhs.spatialReference)
                .append(initialExtent, rhs.initialExtent)
                .append(fullExtent, rhs.fullExtent)
                .append(allowGeometryUpdates, rhs.allowGeometryUpdates)
                .append(units, rhs.units)
                .append(size, rhs.size)
                .append(syncEnabled, rhs.syncEnabled)
                .append(syncCapabilities, rhs.syncCapabilities)
                .append(supportsApplyEditsWithGlobalIds, rhs.supportsApplyEditsWithGlobalIds)
                .append(editorTrackingInfo, rhs.editorTrackingInfo)
                .append(xssPreventionInfo, rhs.xssPreventionInfo)
                .append(layers, rhs.layers)
                .append(tables, rhs.tables)
                .isEquals();
    }
}
