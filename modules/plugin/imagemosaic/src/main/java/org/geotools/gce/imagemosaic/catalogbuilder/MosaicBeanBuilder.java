/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalogbuilder;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.util.Arrays;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Simple builder which builds the configuration bean of a mosaic configuration.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class MosaicBeanBuilder {

    private MosaicConfigurationBean bean;
    private Double noData;

    /** Default constructor */
    public MosaicBeanBuilder() {}

    /**
     * <code>true</code> if we need to expand to RGB(A) the single tiles in case they use a
     * different {@link IndexColorModel}.
     */
    private boolean expandToRGB;

    /** <code>true</code> if we need to look for PAM auxiliary metadata xml files. */
    private boolean checkAuxiliaryMetadata;

    /** OverviewLevel levels */
    private double[][] levels;

    /** name for the mosaic. */
    private String name;

    /** number of levels */
    private int levelsNum;

    /** time attribute name. <code>null</code> if absent. */
    private String timeAttribute;

    /** elevation attribute name. <code>null</code> if absent. */
    private String elevationAttribute;

    /** crs attribute name. <code>null</code> if absent. */
    private String crsAttribute;

    /** additional domain attributes names. <code>null</code> if absent. */
    private String additionalDomainAttributes;

    private String auxiliaryFilePath;

    private String auxiliaryDatastorePath;

    /**
     * mosaic's dummy sample model useful to store dataType and number of bands. All the other
     * fields shouldn't be queried since they are meaningless for the whole mosaic (width, height,
     * ...)
     */
    private SampleModel sampleModel;

    private ColorModel colorModel;

    private byte[][] palette = null;

    private CoordinateReferenceSystem crs = null;

    // /** Imposed envelope for this mosaic. If not present we need to compute from catalogue. */
    // private ReferencedEnvelope envelope;

    private CatalogConfigurationBean catalogConfigurationBean;

    // public ReferencedEnvelope getEnvelope() {
    // return envelope;
    // }
    //
    // public void setEnvelope(ReferencedEnvelope envelope) {
    // this.envelope = envelope;
    // }

    public SampleModel getSampleModel() {
        return sampleModel;
    }

    public void setSampleModel(SampleModel sampleModel) {
        this.sampleModel = sampleModel;
        bean = null;
    }

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
        bean = null;
    }

    public byte[][] getPalette() {
        return palette;
    }

    public void setPalette(byte[][] palette) {
        this.palette = palette;
        bean = null;
    }

    public String getElevationAttribute() {
        return elevationAttribute;
    }

    public void setElevationAttribute(final String elevationAttribute) {
        this.elevationAttribute = elevationAttribute;
        bean = null;
    }

    public String getTimeAttribute() {
        return timeAttribute;
    }

    public void setTimeAttribute(final String timeAttribute) {
        this.timeAttribute = timeAttribute;
        bean = null;
    }

    public String getCrsAttribute() {
        return timeAttribute;
    }

    public void setCrsAttribute(final String crsAttribute) {
        this.crsAttribute = crsAttribute;
        bean = null;
    }

    public String getAdditionalDomainAttributes() {
        return additionalDomainAttributes;
    }

    public void setAdditionalDomainAttributes(String additionalDomainAttributes) {
        this.additionalDomainAttributes = additionalDomainAttributes;
        bean = null;
    }

    public boolean isExpandToRGB() {
        return expandToRGB;
    }

    public void setExpandToRGB(final boolean expandToRGB) {
        this.expandToRGB = expandToRGB;
        bean = null;
    }

    public boolean isCheckAuxiliaryMetadata() {
        return checkAuxiliaryMetadata;
    }

    public void setCheckAuxiliaryMetadata(boolean checkAuxiliaryMetadata) {
        this.checkAuxiliaryMetadata = checkAuxiliaryMetadata;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
        bean = null;
    }

    public int getLevelsNum() {
        return levelsNum;
    }

    public void setLevelsNum(final int levelsNum) {
        this.levelsNum = levelsNum;
        bean = null;
    }

    public double[][] getLevels() {
        return levels.clone();
    }

    public void setLevels(final double[][] levels) {
        this.levels = levels.clone();
        bean = null;
    }

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public void setCrs(CoordinateReferenceSystem crs) {
        this.crs = crs;
        bean = null;
    }

    public CatalogConfigurationBean getCatalogConfigurationBean() {
        return catalogConfigurationBean;
    }

    public void setCatalogConfigurationBean(CatalogConfigurationBean catalogConfigurationBean) {
        this.catalogConfigurationBean = catalogConfigurationBean;
        bean = null;
    }

    public String getAuxiliaryFilePath() {
        return auxiliaryFilePath;
    }

    public void setAuxiliaryFilePath(String auxiliaryFilePath) {
        this.auxiliaryFilePath = auxiliaryFilePath;
        bean = null;
    }

    public String getAuxiliaryDatastorePath() {
        return auxiliaryDatastorePath;
    }

    public void setAuxiliaryDatastorePath(String auxiliaryDatastorePath) {
        this.auxiliaryDatastorePath = auxiliaryDatastorePath;
    }

    @Override
    public String toString() {
        return "MosaicConfigurationBean [expandToRGB="
                + expandToRGB
                + ", levels="
                + Arrays.toString(levels)
                + ", name="
                + name
                + ", levelsNum="
                + levelsNum
                + ", timeAttribute="
                + timeAttribute
                + ", elevationAttribute="
                + elevationAttribute
                + ", crsAttribute="
                + crsAttribute
                + ",sampleModel="
                + sampleModel
                + "]";
    }

    public MosaicConfigurationBean getMosaicConfigurationBean() {
        if (bean == null) {
            bean = new MosaicConfigurationBean();
            bean.setSampleModel(sampleModel);
            bean.setColorModel(colorModel);
            bean.setPalette(palette);
            bean.setCrs(crs);
            bean.setCatalogConfigurationBean(catalogConfigurationBean);
            bean.setTimeAttribute(timeAttribute);
            bean.setCRSAttribute(crsAttribute);
            bean.setElevationAttribute(elevationAttribute);
            bean.setAdditionalDomainAttributes(additionalDomainAttributes);
            bean.setExpandToRGB(expandToRGB);
            bean.setLevels(levels);
            bean.setLevelsNum(levelsNum);
            bean.setName(name);
            bean.setAuxiliaryFilePath(auxiliaryFilePath);
            bean.setAuxiliaryDatastorePath(auxiliaryDatastorePath);
            bean.setCheckAuxiliaryMetadata(checkAuxiliaryMetadata);
            bean.setNoData(noData);
            ;
        }
        return bean;
    }

    public void setNoData(Double noData) {
        this.noData = noData;
    }

    public Double getNoData() {
        return noData;
    }
}
