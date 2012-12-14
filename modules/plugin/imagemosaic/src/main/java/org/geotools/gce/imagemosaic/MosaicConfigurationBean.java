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
package org.geotools.gce.imagemosaic;

import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.commons.beanutils.BeanUtils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Utilities;

/**
 * Very simple bean to hold the configuration of the mosaic.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 *
 * @source $URL$
 */
public class MosaicConfigurationBean {

		/**
         * Default constructor
         */
	public MosaicConfigurationBean() {
		
	}
	
	public MosaicConfigurationBean(final MosaicConfigurationBean that) {
            Utilities.ensureNonNull("MosaicConfigurationBean", that);
            try {
                    BeanUtils.copyProperties(this, that);
            } catch (IllegalAccessException e) {
                    final IllegalArgumentException iae= new IllegalArgumentException(e);
                    throw iae;
            } catch (InvocationTargetException e) {
                    final IllegalArgumentException iae= new IllegalArgumentException(e);
                    throw iae;
            }
        }
	
	/**
	 * <code>true</code> it tells us if the mosaic points to absolute paths or to relative ones. (in case of <code>false</code>).
	 */
	private boolean absolutePath= Utils.DEFAULT_PATH_BEHAVIOR;
	
	/**
	 * <code>true</code> if we need to expand to RGB(A) the single tiles in case they use a different {@link IndexColorModel}.
	 */
	private boolean expandToRGB;
	
	/** OverviewLevel levels */
	private double[][] levels;
	
	/** name for the mosaic.*/
	private String name;
	
	/** number of levels*/
	private int levelsNum;
	
	/** location attribute name*/
	private String locationAttribute=Utils.DEFAULT_LOCATION_ATTRIBUTE;
	
	/**Suggested SPI for the various tiles. May be null.**/
	private String suggestedSPI;
	
	/** time attribute name. <code>null</code> if absent.*/
	private String timeAttribute;
	
	/** elevation attribute name. <code>null</code> if absent.*/
	private String elevationAttribute;

        /** additional domain attributes names. <code>null</code> if absent.*/
        private String additionalDomainAttributes;

        /** The typename to use for the mosaic index*/
	private String typeName;
	
    public String getTypeName() {
    	return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

		/** 
         * mosaic's dummy sample model useful to store dataType and number of bands. All the other fields
         * shouldn't be queried since they are meaningless for the whole mosaic (width, height, ...)
         */
        private SampleModel sampleModel;
	
	/** Imposed envelope for this mosaic. If not present we need to compute from catalogue.*/
	private ReferencedEnvelope envelope;
	
	private boolean heterogeneous;

	public ReferencedEnvelope getEnvelope() {
		return envelope;
	}

	public void setEnvelope(ReferencedEnvelope envelope) {
		this.envelope = envelope;
	}

    public SampleModel getSampleModel() {
        return sampleModel;
    }

    public void setSampleModel(SampleModel sampleModel) {
        this.sampleModel = sampleModel;
    }

	public String getElevationAttribute() {
		return elevationAttribute;
	}
	public void setElevationAttribute(final String elevationAttribute) {
		this.elevationAttribute = elevationAttribute;
	}

	/** we want to use caching for our index.*/
	private boolean caching = Utils.DEFAULT_CONFIGURATION_CACHING;

   /** <code>true</code> if we need to manage footprint if available.  */
    private boolean footprintManagement;

	public String getTimeAttribute() {
		return timeAttribute;
	}
	public void setTimeAttribute(final String timeAttribute) {
		this.timeAttribute = timeAttribute;
	}
	
    public String getAdditionalDomainAttributes() {
        return additionalDomainAttributes;
    }

    public void setAdditionalDomainAttributes(String additionalDomainAttributes) {
        this.additionalDomainAttributes = additionalDomainAttributes;
    }
	/**
	 * @return the suggestedSPI
	 */
	public String getSuggestedSPI() {
		return suggestedSPI;
	}
	/**
	 * @param suggestedSPI the suggestedSPI to set
	 */
	public void setSuggestedSPI(final String suggestedSPI) {
		this.suggestedSPI = suggestedSPI;
	}
	
	public boolean isAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(final boolean absolutePath) {
		this.absolutePath = absolutePath;
	}
	public boolean isExpandToRGB() {
		return expandToRGB;
	}
	public void setExpandToRGB(final boolean expandToRGB) {
		this.expandToRGB = expandToRGB;
	}
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public int getLevelsNum() {
		return levelsNum;
	}
	public void setLevelsNum(final int levelsNum) {
		this.levelsNum = levelsNum;
	}
	public double[][] getLevels() {
		return levels.clone();
	}
	public void setLevels(final double[][] levels) {
		this.levels = levels.clone();
	}
	public String getLocationAttribute() {
		return locationAttribute;
	}
	public void setLocationAttribute(final String locationAttribute) {
		this.locationAttribute = locationAttribute;
	}
	public boolean isCaching() {
		return caching;
	}
	public void setCaching(final boolean caching) {
		this.caching = caching;
	}

    public void setFootprintManagement(final boolean footprintManagement) {
            this.footprintManagement = footprintManagement;
    }

    public boolean isFootprintManagement() {
        return footprintManagement;
    }

    public boolean isHeterogeneous() {
        return heterogeneous;
    }

    public void setHeterogeneous(boolean heterogeneous) {
        this.heterogeneous = heterogeneous;
    }

	@Override
	public String toString() {
		return "MosaicConfigurationBean [absolutePath=" + absolutePath
				+ ", expandToRGB=" + expandToRGB + ", levels="
				+ Arrays.toString(levels) + ", name=" + name
				+ ", levelsNum=" + levelsNum + ", locationAttribute="
				+ locationAttribute + ", suggestedSPI=" + suggestedSPI
				+ ", timeAttribute=" + timeAttribute
				+ ", elevationAttribute=" + elevationAttribute
				+ ", typeName=" + typeName + ", sampleModel=" + sampleModel
				+ ", envelope=" + envelope + ", heterogeneous="
				+ heterogeneous + ", caching=" + caching
				+ ", footprintManagement=" + footprintManagement + "]";
		}
}
