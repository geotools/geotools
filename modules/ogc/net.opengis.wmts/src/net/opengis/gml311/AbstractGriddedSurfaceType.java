/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Gridded Surface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A gridded surface is a parametric curve
 *    surface derived from a rectangular grid in the parameter
 *    space. The rows from this grid are control points for
 *    horizontal surface curves; the columns are control points
 *    for vertical surface curves. The working assumption is that
 *    for a pair of parametric co-ordinates (s, t) that the
 *    horizontal curves for each integer offset are calculated
 *    and evaluated at "s". The defines a sequence of control
 *    points:
 *    
 *    cn(s) : s  1 .....columns 
 * 
 *    From this sequence a vertical curve is calculated for "s",
 *    and evaluated at "t". In most cases, the order of
 *    calculation (horizontal-vertical vs. vertical-horizontal)
 *    does not make a difference. Where it does, the horizontal-   
 *    vertical order shall be the one used.
 * 
 *    Logically, any pair of curve interpolation types can lead
 *    to a subtype of GriddedSurface. The following clauses
 *    define some most commonly encountered surfaces that can
 *    be represented in this manner.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractGriddedSurfaceType#getRow <em>Row</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGriddedSurfaceType#getRows <em>Rows</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGriddedSurfaceType#getColumns <em>Columns</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractGriddedSurfaceType()
 * @model extendedMetaData="name='AbstractGriddedSurfaceType' kind='elementOnly'"
 * @generated
 */
public interface AbstractGriddedSurfaceType extends AbstractParametricCurveSurfaceType {
    /**
     * Returns the value of the '<em><b>Row</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.RowType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Row</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Row</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractGriddedSurfaceType_Row()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='row' namespace='##targetNamespace'"
     * @generated
     */
    EList<RowType> getRow();

    /**
     * Returns the value of the '<em><b>Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute rows gives the number
     *          of rows in the parameter grid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Rows</em>' attribute.
     * @see #setRows(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractGriddedSurfaceType_Rows()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='element' name='rows' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getRows();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGriddedSurfaceType#getRows <em>Rows</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rows</em>' attribute.
     * @see #getRows()
     * @generated
     */
    void setRows(BigInteger value);

    /**
     * Returns the value of the '<em><b>Columns</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute columns gives the number
     *         of columns in the parameter grid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Columns</em>' attribute.
     * @see #setColumns(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractGriddedSurfaceType_Columns()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='element' name='columns' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getColumns();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGriddedSurfaceType#getColumns <em>Columns</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Columns</em>' attribute.
     * @see #getColumns()
     * @generated
     */
    void setColumns(BigInteger value);

} // AbstractGriddedSurfaceType
