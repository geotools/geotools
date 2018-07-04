/**
 */
package net.opengis.wmts.v_1;

import java.math.BigInteger;

import net.opengis.ows11.OnlineResourceType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Legend URL Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The URL from which the legend image can be retrieved
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.LegendURLType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LegendURLType#getHeight <em>Height</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LegendURLType#getMaxScaleDenominator <em>Max Scale Denominator</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LegendURLType#getMinScaleDenominator <em>Min Scale Denominator</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LegendURLType#getWidth <em>Width</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getLegendURLType()
 * @model extendedMetaData="name='LegendURL_._type' kind='empty'"
 * @generated
 */
public interface LegendURLType extends OnlineResourceType {
    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A supported output format for the legend image
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLegendURLType_Format()
     * @model dataType="net.opengis.ows11.MimeType"
     *        extendedMetaData="kind='attribute' name='format'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Height</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Height (in pixels) of the legend image
     * <!-- end-model-doc -->
     * @return the value of the '<em>Height</em>' attribute.
     * @see #setHeight(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLegendURLType_Height()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='height'"
     * @generated
     */
    BigInteger getHeight();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getHeight <em>Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Height</em>' attribute.
     * @see #getHeight()
     * @generated
     */
    void setHeight(BigInteger value);

    /**
     * Returns the value of the '<em><b>Max Scale Denominator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Denominator of the maximum scale (exclusive) for which this legend image is valid
     * <!-- end-model-doc -->
     * @return the value of the '<em>Max Scale Denominator</em>' attribute.
     * @see #isSetMaxScaleDenominator()
     * @see #unsetMaxScaleDenominator()
     * @see #setMaxScaleDenominator(double)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLegendURLType_MaxScaleDenominator()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="kind='attribute' name='maxScaleDenominator'"
     * @generated
     */
    double getMaxScaleDenominator();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getMaxScaleDenominator <em>Max Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Scale Denominator</em>' attribute.
     * @see #isSetMaxScaleDenominator()
     * @see #unsetMaxScaleDenominator()
     * @see #getMaxScaleDenominator()
     * @generated
     */
    void setMaxScaleDenominator(double value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getMaxScaleDenominator <em>Max Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetMaxScaleDenominator()
     * @see #getMaxScaleDenominator()
     * @see #setMaxScaleDenominator(double)
     * @generated
     */
    void unsetMaxScaleDenominator();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getMaxScaleDenominator <em>Max Scale Denominator</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Max Scale Denominator</em>' attribute is set.
     * @see #unsetMaxScaleDenominator()
     * @see #getMaxScaleDenominator()
     * @see #setMaxScaleDenominator(double)
     * @generated
     */
    boolean isSetMaxScaleDenominator();

    /**
     * Returns the value of the '<em><b>Min Scale Denominator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Denominator of the minimum scale (inclusive) for which this legend image is valid
     * <!-- end-model-doc -->
     * @return the value of the '<em>Min Scale Denominator</em>' attribute.
     * @see #isSetMinScaleDenominator()
     * @see #unsetMinScaleDenominator()
     * @see #setMinScaleDenominator(double)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLegendURLType_MinScaleDenominator()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="kind='attribute' name='minScaleDenominator'"
     * @generated
     */
    double getMinScaleDenominator();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getMinScaleDenominator <em>Min Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Min Scale Denominator</em>' attribute.
     * @see #isSetMinScaleDenominator()
     * @see #unsetMinScaleDenominator()
     * @see #getMinScaleDenominator()
     * @generated
     */
    void setMinScaleDenominator(double value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getMinScaleDenominator <em>Min Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetMinScaleDenominator()
     * @see #getMinScaleDenominator()
     * @see #setMinScaleDenominator(double)
     * @generated
     */
    void unsetMinScaleDenominator();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getMinScaleDenominator <em>Min Scale Denominator</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Min Scale Denominator</em>' attribute is set.
     * @see #unsetMinScaleDenominator()
     * @see #getMinScaleDenominator()
     * @see #setMinScaleDenominator(double)
     * @generated
     */
    boolean isSetMinScaleDenominator();

    /**
     * Returns the value of the '<em><b>Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Width (in pixels) of the legend image
     * <!-- end-model-doc -->
     * @return the value of the '<em>Width</em>' attribute.
     * @see #setWidth(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLegendURLType_Width()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='width'"
     * @generated
     */
    BigInteger getWidth();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.LegendURLType#getWidth <em>Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Width</em>' attribute.
     * @see #getWidth()
     * @generated
     */
    void setWidth(BigInteger value);

} // LegendURLType
