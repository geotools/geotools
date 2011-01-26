/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import net.opengis.ows11.CodeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interpolation Method Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identifier of an interpolation method applicable to continuous grid coverages. The string in this type shall be one interpolation type identifier string, selected from the referenced dictionary. 
 * Adapts gml:CodeWithAuthorityType from GML 3.2 for this WCS purpose, allowing the codeSpace to be omitted but providing a default value for the standard interpolation methods defined in Annex C of ISO 19123: Geographic information - Schema for coverage geometry and functions. 
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodBaseType()
 * @model extendedMetaData="name='InterpolationMethodBaseType' kind='simple'"
 * @generated
 */
public interface InterpolationMethodBaseType extends CodeType {
} // InterpolationMethodBaseType
