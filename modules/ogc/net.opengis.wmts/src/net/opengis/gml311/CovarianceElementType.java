/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Covariance Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An element of a covariance matrix.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CovarianceElementType#getRowIndex <em>Row Index</em>}</li>
 *   <li>{@link net.opengis.gml311.CovarianceElementType#getColumnIndex <em>Column Index</em>}</li>
 *   <li>{@link net.opengis.gml311.CovarianceElementType#getCovariance <em>Covariance</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCovarianceElementType()
 * @model extendedMetaData="name='CovarianceElementType' kind='elementOnly'"
 * @generated
 */
public interface CovarianceElementType extends EObject {
    /**
     * Returns the value of the '<em><b>Row Index</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Row number of this covariance element value. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Row Index</em>' attribute.
     * @see #setRowIndex(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getCovarianceElementType_RowIndex()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='rowIndex' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getRowIndex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CovarianceElementType#getRowIndex <em>Row Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Row Index</em>' attribute.
     * @see #getRowIndex()
     * @generated
     */
    void setRowIndex(BigInteger value);

    /**
     * Returns the value of the '<em><b>Column Index</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Column number of this covariance element value. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Column Index</em>' attribute.
     * @see #setColumnIndex(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getCovarianceElementType_ColumnIndex()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='columnIndex' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getColumnIndex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CovarianceElementType#getColumnIndex <em>Column Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Column Index</em>' attribute.
     * @see #getColumnIndex()
     * @generated
     */
    void setColumnIndex(BigInteger value);

    /**
     * Returns the value of the '<em><b>Covariance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Value of covariance matrix element. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Covariance</em>' attribute.
     * @see #isSetCovariance()
     * @see #unsetCovariance()
     * @see #setCovariance(double)
     * @see net.opengis.gml311.Gml311Package#getCovarianceElementType_Covariance()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='covariance' namespace='##targetNamespace'"
     * @generated
     */
    double getCovariance();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CovarianceElementType#getCovariance <em>Covariance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Covariance</em>' attribute.
     * @see #isSetCovariance()
     * @see #unsetCovariance()
     * @see #getCovariance()
     * @generated
     */
    void setCovariance(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.CovarianceElementType#getCovariance <em>Covariance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetCovariance()
     * @see #getCovariance()
     * @see #setCovariance(double)
     * @generated
     */
    void unsetCovariance();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.CovarianceElementType#getCovariance <em>Covariance</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Covariance</em>' attribute is set.
     * @see #unsetCovariance()
     * @see #getCovariance()
     * @see #setCovariance(double)
     * @generated
     */
    boolean isSetCovariance();

} // CovarianceElementType
