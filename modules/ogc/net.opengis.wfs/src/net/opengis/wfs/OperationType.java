/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Operation Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs.WfsPackage#getOperationType()
 * @model
 * @generated
 */
public final class OperationType extends AbstractEnumerator {
	/**
     * The '<em><b>Insert</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Insert</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @see #INSERT_LITERAL
     * @model name="Insert"
     * @generated
     * @ordered
     */
	public static final int INSERT = 0;

	/**
     * The '<em><b>Update</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Update</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @see #UPDATE_LITERAL
     * @model name="Update"
     * @generated
     * @ordered
     */
	public static final int UPDATE = 1;

	/**
     * The '<em><b>Delete</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Delete</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @see #DELETE_LITERAL
     * @model name="Delete"
     * @generated
     * @ordered
     */
	public static final int DELETE = 2;

	/**
     * The '<em><b>Query</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Query</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @see #QUERY_LITERAL
     * @model name="Query"
     * @generated
     * @ordered
     */
	public static final int QUERY = 3;

	/**
     * The '<em><b>Lock</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Lock</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @see #LOCK_LITERAL
     * @model name="Lock"
     * @generated
     * @ordered
     */
	public static final int LOCK = 4;

	/**
     * The '<em><b>Get Gml Object</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Get Gml Object</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @see #GET_GML_OBJECT_LITERAL
     * @model name="GetGmlObject"
     * @generated
     * @ordered
     */
	public static final int GET_GML_OBJECT = 5;

	/**
     * The '<em><b>Insert</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #INSERT
     * @generated
     * @ordered
     */
	public static final OperationType INSERT_LITERAL = new OperationType(INSERT, "Insert", "Insert");

	/**
     * The '<em><b>Update</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #UPDATE
     * @generated
     * @ordered
     */
	public static final OperationType UPDATE_LITERAL = new OperationType(UPDATE, "Update", "Update");

	/**
     * The '<em><b>Delete</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #DELETE
     * @generated
     * @ordered
     */
	public static final OperationType DELETE_LITERAL = new OperationType(DELETE, "Delete", "Delete");

	/**
     * The '<em><b>Query</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #QUERY
     * @generated
     * @ordered
     */
	public static final OperationType QUERY_LITERAL = new OperationType(QUERY, "Query", "Query");

	/**
     * The '<em><b>Lock</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #LOCK
     * @generated
     * @ordered
     */
	public static final OperationType LOCK_LITERAL = new OperationType(LOCK, "Lock", "Lock");

	/**
     * The '<em><b>Get Gml Object</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #GET_GML_OBJECT
     * @generated
     * @ordered
     */
	public static final OperationType GET_GML_OBJECT_LITERAL = new OperationType(GET_GML_OBJECT, "GetGmlObject", "GetGmlObject");

	/**
     * An array of all the '<em><b>Operation Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private static final OperationType[] VALUES_ARRAY =
		new OperationType[] {
            INSERT_LITERAL,
            UPDATE_LITERAL,
            DELETE_LITERAL,
            QUERY_LITERAL,
            LOCK_LITERAL,
            GET_GML_OBJECT_LITERAL,
        };

	/**
     * A public read-only list of all the '<em><b>Operation Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
     * Returns the '<em><b>Operation Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static OperationType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            OperationType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

	/**
     * Returns the '<em><b>Operation Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static OperationType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            OperationType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

	/**
     * Returns the '<em><b>Operation Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static OperationType get(int value) {
        switch (value) {
            case INSERT: return INSERT_LITERAL;
            case UPDATE: return UPDATE_LITERAL;
            case DELETE: return DELETE_LITERAL;
            case QUERY: return QUERY_LITERAL;
            case LOCK: return LOCK_LITERAL;
            case GET_GML_OBJECT: return GET_GML_OBJECT_LITERAL;
        }
        return null;
    }

	/**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private OperationType(int value, String name, String literal) {
        super(value, name, literal);
    }

} //OperationType
