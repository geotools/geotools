/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.wfs;

/**
 * Enumeration for the possible operations a WFS may implement.
 * 
 * @author Gabriel Roldan
 * @version $Id: WFSOperationType.java 31731 2008-10-29 13:51:20Z groldan $
 * @since 2.5.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/protocol/wfs/WFSOperationType.java $
 */
public enum WFSOperationType {
    GET_CAPABILITIES("GetCapabilities"), 
    DESCRIBE_FEATURETYPE("DescribeFeatureType"),
    GET_FEATURE("GetFeature"), 
    GET_GML_OBJECT("GetGmlObject"), 
    LOCK_FEATURE("LockFeature"), 
    GET_FEATURE_WITH_LOCK("GetFeatureWithLock"), 
    TRANSACTION("Transaction");

    private String operationName;

    private WFSOperationType(String operationName){
        this.operationName = operationName;
    }
    
    public String getName(){
        return operationName;
    }
}
