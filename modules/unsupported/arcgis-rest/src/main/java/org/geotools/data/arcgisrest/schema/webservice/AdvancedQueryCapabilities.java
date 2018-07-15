package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdvancedQueryCapabilities {

    /** (Required) */
    @SerializedName("supportsPagination")
    @Expose
    private Boolean supportsPagination;
    /** (Required) */
    @SerializedName("supportsQueryRelatedPagination")
    @Expose
    private Boolean supportsQueryRelatedPagination;
    /** (Required) */
    @SerializedName("supportsQueryWithDistance")
    @Expose
    private Boolean supportsQueryWithDistance;
    /** (Required) */
    @SerializedName("supportsReturningQueryExtent")
    @Expose
    private Boolean supportsReturningQueryExtent;
    /** (Required) */
    @SerializedName("supportsStatistics")
    @Expose
    private Boolean supportsStatistics;
    /** (Required) */
    @SerializedName("supportsOrderBy")
    @Expose
    private Boolean supportsOrderBy;
    /** (Required) */
    @SerializedName("supportsDistinct")
    @Expose
    private Boolean supportsDistinct;
    /** (Required) */
    @SerializedName("supportsQueryWithResultType")
    @Expose
    private Boolean supportsQueryWithResultType;
    /** (Required) */
    @SerializedName("supportsSqlExpression")
    @Expose
    private Boolean supportsSqlExpression;
    /** (Required) */
    @SerializedName("supportsAdvancedQueryRelated")
    @Expose
    private Boolean supportsAdvancedQueryRelated;
    /** (Required) */
    @SerializedName("supportsReturningGeometryCentroid")
    @Expose
    private Boolean supportsReturningGeometryCentroid;

    /**
     * (Required)
     *
     * @return The supportsPagination
     */
    public Boolean getSupportsPagination() {
        return supportsPagination;
    }

    /**
     * (Required)
     *
     * @param supportsPagination The supportsPagination
     */
    public void setSupportsPagination(Boolean supportsPagination) {
        this.supportsPagination = supportsPagination;
    }

    /**
     * (Required)
     *
     * @return The supportsQueryRelatedPagination
     */
    public Boolean getSupportsQueryRelatedPagination() {
        return supportsQueryRelatedPagination;
    }

    /**
     * (Required)
     *
     * @param supportsQueryRelatedPagination The supportsQueryRelatedPagination
     */
    public void setSupportsQueryRelatedPagination(Boolean supportsQueryRelatedPagination) {
        this.supportsQueryRelatedPagination = supportsQueryRelatedPagination;
    }

    /**
     * (Required)
     *
     * @return The supportsQueryWithDistance
     */
    public Boolean getSupportsQueryWithDistance() {
        return supportsQueryWithDistance;
    }

    /**
     * (Required)
     *
     * @param supportsQueryWithDistance The supportsQueryWithDistance
     */
    public void setSupportsQueryWithDistance(Boolean supportsQueryWithDistance) {
        this.supportsQueryWithDistance = supportsQueryWithDistance;
    }

    /**
     * (Required)
     *
     * @return The supportsReturningQueryExtent
     */
    public Boolean getSupportsReturningQueryExtent() {
        return supportsReturningQueryExtent;
    }

    /**
     * (Required)
     *
     * @param supportsReturningQueryExtent The supportsReturningQueryExtent
     */
    public void setSupportsReturningQueryExtent(Boolean supportsReturningQueryExtent) {
        this.supportsReturningQueryExtent = supportsReturningQueryExtent;
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
     * @return The supportsOrderBy
     */
    public Boolean getSupportsOrderBy() {
        return supportsOrderBy;
    }

    /**
     * (Required)
     *
     * @param supportsOrderBy The supportsOrderBy
     */
    public void setSupportsOrderBy(Boolean supportsOrderBy) {
        this.supportsOrderBy = supportsOrderBy;
    }

    /**
     * (Required)
     *
     * @return The supportsDistinct
     */
    public Boolean getSupportsDistinct() {
        return supportsDistinct;
    }

    /**
     * (Required)
     *
     * @param supportsDistinct The supportsDistinct
     */
    public void setSupportsDistinct(Boolean supportsDistinct) {
        this.supportsDistinct = supportsDistinct;
    }

    /**
     * (Required)
     *
     * @return The supportsQueryWithResultType
     */
    public Boolean getSupportsQueryWithResultType() {
        return supportsQueryWithResultType;
    }

    /**
     * (Required)
     *
     * @param supportsQueryWithResultType The supportsQueryWithResultType
     */
    public void setSupportsQueryWithResultType(Boolean supportsQueryWithResultType) {
        this.supportsQueryWithResultType = supportsQueryWithResultType;
    }

    /**
     * (Required)
     *
     * @return The supportsSqlExpression
     */
    public Boolean getSupportsSqlExpression() {
        return supportsSqlExpression;
    }

    /**
     * (Required)
     *
     * @param supportsSqlExpression The supportsSqlExpression
     */
    public void setSupportsSqlExpression(Boolean supportsSqlExpression) {
        this.supportsSqlExpression = supportsSqlExpression;
    }

    /**
     * (Required)
     *
     * @return The supportsAdvancedQueryRelated
     */
    public Boolean getSupportsAdvancedQueryRelated() {
        return supportsAdvancedQueryRelated;
    }

    /**
     * (Required)
     *
     * @param supportsAdvancedQueryRelated The supportsAdvancedQueryRelated
     */
    public void setSupportsAdvancedQueryRelated(Boolean supportsAdvancedQueryRelated) {
        this.supportsAdvancedQueryRelated = supportsAdvancedQueryRelated;
    }

    /**
     * (Required)
     *
     * @return The supportsReturningGeometryCentroid
     */
    public Boolean getSupportsReturningGeometryCentroid() {
        return supportsReturningGeometryCentroid;
    }

    /**
     * (Required)
     *
     * @param supportsReturningGeometryCentroid The supportsReturningGeometryCentroid
     */
    public void setSupportsReturningGeometryCentroid(Boolean supportsReturningGeometryCentroid) {
        this.supportsReturningGeometryCentroid = supportsReturningGeometryCentroid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(supportsPagination)
                .append(supportsQueryRelatedPagination)
                .append(supportsQueryWithDistance)
                .append(supportsReturningQueryExtent)
                .append(supportsStatistics)
                .append(supportsOrderBy)
                .append(supportsDistinct)
                .append(supportsQueryWithResultType)
                .append(supportsSqlExpression)
                .append(supportsAdvancedQueryRelated)
                .append(supportsReturningGeometryCentroid)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AdvancedQueryCapabilities) == false) {
            return false;
        }
        AdvancedQueryCapabilities rhs = ((AdvancedQueryCapabilities) other);
        return new EqualsBuilder()
                .append(supportsPagination, rhs.supportsPagination)
                .append(supportsQueryRelatedPagination, rhs.supportsQueryRelatedPagination)
                .append(supportsQueryWithDistance, rhs.supportsQueryWithDistance)
                .append(supportsReturningQueryExtent, rhs.supportsReturningQueryExtent)
                .append(supportsStatistics, rhs.supportsStatistics)
                .append(supportsOrderBy, rhs.supportsOrderBy)
                .append(supportsDistinct, rhs.supportsDistinct)
                .append(supportsQueryWithResultType, rhs.supportsQueryWithResultType)
                .append(supportsSqlExpression, rhs.supportsSqlExpression)
                .append(supportsAdvancedQueryRelated, rhs.supportsAdvancedQueryRelated)
                .append(supportsReturningGeometryCentroid, rhs.supportsReturningGeometryCentroid)
                .isEquals();
    }
}
