/**
 * <copyright>
 * (C) 2012, Open Source Geospatial Foundation (OSGeo)
 * (C) 2004,2010 Open Geospatial Consortium.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Distributed Search Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Governs the behaviour of a distributed search.
 *          hopCount     - the maximum number of message hops before
 *                         the search is terminated. Each catalogue node
 *                         decrements this value when the request is received,
 *                         and must not forward the request if hopCount=0.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.DistributedSearchType#getHopCount <em>Hop Count</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getDistributedSearchType()
 * @model extendedMetaData="name='DistributedSearchType' kind='empty'"
 * @generated
 */
public interface DistributedSearchType extends EObject {
    /**
     * Returns the value of the '<em><b>Hop Count</b></em>' attribute.
     * The default value is <code>"2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Hop Count</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hop Count</em>' attribute.
     * @see #setHopCount(Integer)
     * @see net.opengis.cat.csw20.Csw20Package#getDistributedSearchType_HopCount()
     * @model default="2"
     * @generated
     */
    Integer getHopCount();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DistributedSearchType#getHopCount <em>Hop Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hop Count</em>' attribute.
     * @see #getHopCount()
     * @generated
     */
    void setHopCount(Integer value);


} // DistributedSearchType
