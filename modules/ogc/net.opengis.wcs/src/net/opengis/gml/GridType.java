/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;
import org.opengis.coverage.grid.GridEnvelope;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grid Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Implicitly defines an unrectified grid, which is a network composed of two or more sets of equally spaced parallel lines in which the members of each set intersect the members of the other sets at right angles. This profile does not extend AbstractGeometryType, so it defines the srsName attribute.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.GridType#getLimits <em>Limits</em>}</li>
 *   <li>{@link net.opengis.gml.GridType#getAxisName <em>Axis Name</em>}</li>
 *   <li>{@link net.opengis.gml.GridType#getDimension <em>Dimension</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getGridType()
 * @model extendedMetaData="name='GridType' kind='elementOnly'"
 * @generated
 */
public interface GridType extends AbstractGeometryType {
    /**
     * Returns the value of the '<em><b>Limits</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Limits</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Limits</em>' containment reference.
     * @see #setLimits(GridLimitsType)
     * @see net.opengis.gml.Gml4wcsPackage#getGridType_Limits()
     * @model
     */
    GridEnvelope getLimits();

				/**
	 * Sets the value of the '{@link net.opengis.gml.GridType#getLimits <em>Limits</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Limits</em>' attribute.
	 * @see #getLimits()
	 * @generated
	 */
	void setLimits(GridEnvelope value);

				/**
	 * Returns the value of the '<em><b>Axis Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Axis Name</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Axis Name</em>' attribute.
	 * @see #setAxisName(String)
	 * @see net.opengis.gml.GmlPackage#getGridType_AxisName()
	 * @model type="java.lang.String"
	 * @generated NOT 
	 */
    EList getAxisName();

				/**
	 * Returns the value of the '<em><b>Dimension</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dimension</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Dimension</em>' attribute.
	 * @see #setDimension(BigInteger)
	 * @see net.opengis.gml.GmlPackage#getGridType_Dimension()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
	 *        extendedMetaData="kind='attribute' name='dimension'"
	 * @generated
	 */
    BigInteger getDimension();

    /**
	 * Sets the value of the '{@link net.opengis.gml.GridType#getDimension <em>Dimension</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dimension</em>' attribute.
	 * @see #getDimension()
	 * @generated
	 */
    void setDimension(BigInteger value);

} // GridType
