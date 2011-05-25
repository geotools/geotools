/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005-2006, David Zwiers
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
package org.geotools.data.ows;

import java.util.Iterator;
import java.util.List;

import org.geotools.filter.FilterCapabilities;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author rpetty
 *
 * @source $URL$
 */
public class WSCapabilities extends Capabilities {
    private List<FeatureSetDescription> featureTypes; // FeatureSetDescriptions
    private OperationType describeFeatureType;
    private OperationType getCapabilities;
    private OperationType getFeature;
    private OperationType getFeatureWithLock;
    private OperationType transaction;
    private OperationType lockFeature;
    private String vendorSpecificCapabilities;
    private FilterCapabilities filterCapabilities;

    /**
     * Makes a few assumptions about ":" in the name (prefix:typename). 
     * 
     * Although this case is uncommon, it may result in the occational error. 
     * The specification is unclear as to the inclusion or exclusion of the 
     * prefix (although most xml documents would support prefix exclusion). 
     * 
     * @param capabilities
     * @param typename
     */
    public static FeatureSetDescription getFeatureSetDescription(WSCapabilities capabilities, String typename){
        List l = capabilities.getFeatureTypes();
        Iterator i = l.iterator();
        String crsName = null;

        while (i.hasNext() && crsName==null) {
                FeatureSetDescription fsd = (FeatureSetDescription) i.next();
                String name = fsd.getName();
                if (typename.equals( name )) {
                    return fsd;
                }
                if(name !=null){
                	int index = name.indexOf(':'); 
                	if(index!=-1 && typename.equals(name.substring(index+1))){
                	    return fsd;
                	}
                }
        }
        return null;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return Returns the featureTypes.
     */
    public List<FeatureSetDescription> getFeatureTypes() {
        return featureTypes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param featureTypes The featureTypes to set.
     */
    public void setFeatureTypes(List featureTypes) {
        this.featureTypes = featureTypes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the filterCapabilities.
     */
    public FilterCapabilities getFilterCapabilities() {
        return filterCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filterCapabilities The filterCapabilities to set.
     */
    public void setFilterCapabilities(FilterCapabilities filterCapabilities) {
        this.filterCapabilities = filterCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the vendorSpecificCapabilities.
     */
    public String getVendorSpecificCapabilities() {
        return vendorSpecificCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vendorSpecificCapabilities The vendorSpecificCapabilities to set.
     */
    public void setVendorSpecificCapabilities(String vendorSpecificCapabilities) {
        this.vendorSpecificCapabilities = vendorSpecificCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the describeFeatureType.
     */
    public OperationType getDescribeFeatureType() {
        return describeFeatureType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param describeFeatureType The describeFeatureType to set.
     */
    public void setDescribeFeatureType(OperationType describeFeatureType) {
        this.describeFeatureType = describeFeatureType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getCapabilities.
     */
    public OperationType getGetCapabilities() {
        return getCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getCapabilities The getCapabilities to set.
     */
    public void setGetCapabilities(OperationType getCapabilities) {
        this.getCapabilities = getCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getFeature.
     */
    public OperationType getGetFeature() {
        return getFeature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getFeature The getFeature to set.
     */
    public void setGetFeature(OperationType getFeature) {
        this.getFeature = getFeature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getFeatureWithLock.
     */
    public OperationType getGetFeatureWithLock() {
        return getFeatureWithLock;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getFeatureWithLock The getFeatureWithLock to set.
     */
    public void setGetFeatureWithLock(OperationType getFeatureWithLock) {
        this.getFeatureWithLock = getFeatureWithLock;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the transaction.
     */
    public OperationType getTransaction() {
        return transaction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transaction The transaction to set.
     */
    public void setTransaction(OperationType transaction) {
        this.transaction = transaction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the lockFeature.
     */
    public OperationType getLockFeature() {
        return lockFeature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lockFeature The lockFeature to set.
     */
    public void setLockFeature(OperationType lockFeature) {
        this.lockFeature = lockFeature;
    }
}
