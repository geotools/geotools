/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.gce.netcdf;

import java.util.Date;

import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.GridEnvelope;

public class ParamInformation {
    private GeneralEnvelope requestedEnvelope = null;
    private Double elevation = null;
    private Date time = null;
    private Date analysisTime = null;
    private String parameter = null;
    private GridEnvelope dim = null;
    private OverviewPolicy overviewPolicy = null;

    public GeneralEnvelope getRequestedEnvelope() {
	return requestedEnvelope;
    }

    public void setRequestedEnvelope(GeneralEnvelope requestedEnvelope) {
	this.requestedEnvelope = requestedEnvelope;
    }

    public Double getElevation() {
	return elevation;
    }

    public void setElevation(Double elevation) {
	this.elevation = elevation;
    }

    public Date getTime() {
	return time;
    }

    public void setTime(Date time) {
	this.time = time;
    }

    public String getParameter() {
	return parameter;
    }

    public void setParameter(String parameter) {
	this.parameter = parameter;
    }

    public GridEnvelope getDim() {
	return dim;
    }

    public void setDim(GridEnvelope dim) {
	this.dim = dim;
    }

    public OverviewPolicy getOverviewPolicy() {
	return overviewPolicy;
    }

    public void setOverviewPolicy(OverviewPolicy overviewPolicy) {
	this.overviewPolicy = overviewPolicy;
    }

    public Date getAnalysisTime() {
	return analysisTime;
    }

    public void setAnalysisTime(Date analysisTime) {
	this.analysisTime = analysisTime;
    }

}
