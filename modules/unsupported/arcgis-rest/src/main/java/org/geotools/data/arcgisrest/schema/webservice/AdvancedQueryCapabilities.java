/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Objects;

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

    /** (Required) */
    public Boolean getSupportsPagination() {
        return supportsPagination;
    }

    /** (Required) */
    public void setSupportsPagination(Boolean supportsPagination) {
        this.supportsPagination = supportsPagination;
    }

    /** (Required) */
    public Boolean getSupportsQueryRelatedPagination() {
        return supportsQueryRelatedPagination;
    }

    /** (Required) */
    public void setSupportsQueryRelatedPagination(Boolean supportsQueryRelatedPagination) {
        this.supportsQueryRelatedPagination = supportsQueryRelatedPagination;
    }

    /** (Required) */
    public Boolean getSupportsQueryWithDistance() {
        return supportsQueryWithDistance;
    }

    /** (Required) */
    public void setSupportsQueryWithDistance(Boolean supportsQueryWithDistance) {
        this.supportsQueryWithDistance = supportsQueryWithDistance;
    }

    /** (Required) */
    public Boolean getSupportsReturningQueryExtent() {
        return supportsReturningQueryExtent;
    }

    /** (Required) */
    public void setSupportsReturningQueryExtent(Boolean supportsReturningQueryExtent) {
        this.supportsReturningQueryExtent = supportsReturningQueryExtent;
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
    public Boolean getSupportsOrderBy() {
        return supportsOrderBy;
    }

    /** (Required) */
    public void setSupportsOrderBy(Boolean supportsOrderBy) {
        this.supportsOrderBy = supportsOrderBy;
    }

    /** (Required) */
    public Boolean getSupportsDistinct() {
        return supportsDistinct;
    }

    /** (Required) */
    public void setSupportsDistinct(Boolean supportsDistinct) {
        this.supportsDistinct = supportsDistinct;
    }

    /** (Required) */
    public Boolean getSupportsQueryWithResultType() {
        return supportsQueryWithResultType;
    }

    /** (Required) */
    public void setSupportsQueryWithResultType(Boolean supportsQueryWithResultType) {
        this.supportsQueryWithResultType = supportsQueryWithResultType;
    }

    /** (Required) */
    public Boolean getSupportsSqlExpression() {
        return supportsSqlExpression;
    }

    /** (Required) */
    public void setSupportsSqlExpression(Boolean supportsSqlExpression) {
        this.supportsSqlExpression = supportsSqlExpression;
    }

    /** (Required) */
    public Boolean getSupportsAdvancedQueryRelated() {
        return supportsAdvancedQueryRelated;
    }

    /** (Required) */
    public void setSupportsAdvancedQueryRelated(Boolean supportsAdvancedQueryRelated) {
        this.supportsAdvancedQueryRelated = supportsAdvancedQueryRelated;
    }

    /** (Required) */
    public Boolean getSupportsReturningGeometryCentroid() {
        return supportsReturningGeometryCentroid;
    }

    /** (Required) */
    public void setSupportsReturningGeometryCentroid(Boolean supportsReturningGeometryCentroid) {
        this.supportsReturningGeometryCentroid = supportsReturningGeometryCentroid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AdvancedQueryCapabilities.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("supportsPagination");
        sb.append('=');
        sb.append(((this.supportsPagination == null) ? "<null>" : this.supportsPagination));
        sb.append(',');
        sb.append("supportsQueryRelatedPagination");
        sb.append('=');
        sb.append(((this.supportsQueryRelatedPagination == null) ? "<null>" : this.supportsQueryRelatedPagination));
        sb.append(',');
        sb.append("supportsQueryWithDistance");
        sb.append('=');
        sb.append(((this.supportsQueryWithDistance == null) ? "<null>" : this.supportsQueryWithDistance));
        sb.append(',');
        sb.append("supportsReturningQueryExtent");
        sb.append('=');
        sb.append(((this.supportsReturningQueryExtent == null) ? "<null>" : this.supportsReturningQueryExtent));
        sb.append(',');
        sb.append("supportsStatistics");
        sb.append('=');
        sb.append(((this.supportsStatistics == null) ? "<null>" : this.supportsStatistics));
        sb.append(',');
        sb.append("supportsOrderBy");
        sb.append('=');
        sb.append(((this.supportsOrderBy == null) ? "<null>" : this.supportsOrderBy));
        sb.append(',');
        sb.append("supportsDistinct");
        sb.append('=');
        sb.append(((this.supportsDistinct == null) ? "<null>" : this.supportsDistinct));
        sb.append(',');
        sb.append("supportsQueryWithResultType");
        sb.append('=');
        sb.append(((this.supportsQueryWithResultType == null) ? "<null>" : this.supportsQueryWithResultType));
        sb.append(',');
        sb.append("supportsSqlExpression");
        sb.append('=');
        sb.append(((this.supportsSqlExpression == null) ? "<null>" : this.supportsSqlExpression));
        sb.append(',');
        sb.append("supportsAdvancedQueryRelated");
        sb.append('=');
        sb.append(((this.supportsAdvancedQueryRelated == null) ? "<null>" : this.supportsAdvancedQueryRelated));
        sb.append(',');
        sb.append("supportsReturningGeometryCentroid");
        sb.append('=');
        sb.append(
                ((this.supportsReturningGeometryCentroid == null) ? "<null>" : this.supportsReturningGeometryCentroid));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.supportsStatistics == null) ? 0 : this.supportsStatistics.hashCode()));
        result = ((result * 31) + ((this.supportsSqlExpression == null) ? 0 : this.supportsSqlExpression.hashCode()));
        result = ((result * 31)
                + ((this.supportsReturningQueryExtent == null) ? 0 : this.supportsReturningQueryExtent.hashCode()));
        result = ((result * 31)
                + ((this.supportsQueryWithResultType == null) ? 0 : this.supportsQueryWithResultType.hashCode()));
        result = ((result * 31) + ((this.supportsOrderBy == null) ? 0 : this.supportsOrderBy.hashCode()));
        result = ((result * 31)
                + ((this.supportsQueryRelatedPagination == null) ? 0 : this.supportsQueryRelatedPagination.hashCode()));
        result = ((result * 31)
                + ((this.supportsQueryWithDistance == null) ? 0 : this.supportsQueryWithDistance.hashCode()));
        result = ((result * 31) + ((this.supportsPagination == null) ? 0 : this.supportsPagination.hashCode()));
        result = ((result * 31) + ((this.supportsDistinct == null) ? 0 : this.supportsDistinct.hashCode()));
        result = ((result * 31)
                + ((this.supportsReturningGeometryCentroid == null)
                        ? 0
                        : this.supportsReturningGeometryCentroid.hashCode()));
        result = ((result * 31)
                + ((this.supportsAdvancedQueryRelated == null) ? 0 : this.supportsAdvancedQueryRelated.hashCode()));
        return result;
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
        return Objects.equals(this.supportsStatistics, rhs.supportsStatistics)
                && Objects.equals(this.supportsSqlExpression, rhs.supportsSqlExpression)
                && Objects.equals(this.supportsReturningQueryExtent, rhs.supportsReturningQueryExtent)
                && Objects.equals(this.supportsQueryWithResultType, rhs.supportsQueryWithResultType)
                && Objects.equals(this.supportsOrderBy, rhs.supportsOrderBy)
                && Objects.equals(this.supportsQueryRelatedPagination, rhs.supportsQueryRelatedPagination)
                && Objects.equals(this.supportsQueryWithDistance, rhs.supportsQueryWithDistance)
                && Objects.equals(this.supportsPagination, rhs.supportsPagination)
                && Objects.equals(this.supportsDistinct, rhs.supportsDistinct)
                && Objects.equals(this.supportsReturningGeometryCentroid, rhs.supportsReturningGeometryCentroid)
                && Objects.equals(this.supportsAdvancedQueryRelated, rhs.supportsAdvancedQueryRelated);
    }
}
