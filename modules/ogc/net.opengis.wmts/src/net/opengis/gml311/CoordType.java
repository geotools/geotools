/**
 */
package net.opengis.gml311;

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coord Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Represents a coordinate tuple in one, two, or three dimensions. Deprecated with GML 3.0 and replaced by 
 * 			DirectPositionType.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CoordType#getX <em>X</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordType#getY <em>Y</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordType#getZ <em>Z</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCoordType()
 * @model extendedMetaData="name='CoordType' kind='elementOnly'"
 * @generated
 */
public interface CoordType extends EObject {
    /**
     * Returns the value of the '<em><b>X</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>X</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>X</em>' attribute.
     * @see #setX(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getCoordType_X()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Decimal" required="true"
     *        extendedMetaData="kind='element' name='X' namespace='##targetNamespace'"
     * @generated
     */
    BigDecimal getX();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordType#getX <em>X</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>X</em>' attribute.
     * @see #getX()
     * @generated
     */
    void setX(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Y</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Y</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Y</em>' attribute.
     * @see #setY(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getCoordType_Y()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Decimal"
     *        extendedMetaData="kind='element' name='Y' namespace='##targetNamespace'"
     * @generated
     */
    BigDecimal getY();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordType#getY <em>Y</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Y</em>' attribute.
     * @see #getY()
     * @generated
     */
    void setY(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Z</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Z</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Z</em>' attribute.
     * @see #setZ(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getCoordType_Z()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Decimal"
     *        extendedMetaData="kind='element' name='Z' namespace='##targetNamespace'"
     * @generated
     */
    BigDecimal getZ();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordType#getZ <em>Z</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Z</em>' attribute.
     * @see #getZ()
     * @generated
     */
    void setZ(BigDecimal value);

} // CoordType
