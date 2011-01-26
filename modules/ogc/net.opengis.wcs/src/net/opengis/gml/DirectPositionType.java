/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import java.math.BigInteger;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Direct Position Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DirectPosition instances hold the coordinates for a position within some coordinate reference system (CRS). Since DirectPositions, as data types, will often be included in larger objects (such as geometry elements) that have references to CRS, the "srsName" attribute will in general be missing, if this particular DirectPosition is included in a larger element with such a reference to a CRS. In this case, the CRS is implicitly assumed to take on the value of the containing object's CRS.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.DirectPositionType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml.DirectPositionType#getDimension <em>Dimension</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getDirectPositionType()
 * @model extendedMetaData="name='DirectPositionType' kind='simple'"
 * @generated
 */
public interface DirectPositionType extends EObject {
    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(List)
	 * @see net.opengis.gml.GmlPackage#getDirectPositionType_Value()
	 * @model dataType="net.opengis.gml.DoubleList" many="false"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    List getValue();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DirectPositionType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(List value);

    /**
	 * Returns the value of the '<em><b>Dimension</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The attribute "dimension" is the length of coordinate sequence (the number of entries in the list). This is determined by the coordinate reference system.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dimension</em>' attribute.
	 * @see #setDimension(BigInteger)
	 * @see net.opengis.gml.GmlPackage#getDirectPositionType_Dimension()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
	 *        extendedMetaData="kind='attribute' name='dimension'"
	 * @generated
	 */
    BigInteger getDimension();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DirectPositionType#getDimension <em>Dimension</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dimension</em>' attribute.
	 * @see #getDimension()
	 * @generated
	 */
    void setDimension(BigInteger value);

} // DirectPositionType
