/**
 */
package net.opengis.wps20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Mode Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wps20.Wps20Package#getModeType()
 * @model extendedMetaData="name='mode_._type'"
 * @generated
 */
public enum ModeType implements Enumerator {
	/**
	 * The '<em><b>Sync</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 										"sync" triggers the synchronous execution protocol
	 * 									
	 * <!-- end-model-doc -->
	 * @see #SYNC_VALUE
	 * @generated
	 * @ordered
	 */
	SYNC(0, "sync", "sync"),

	/**
	 * The '<em><b>Async</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 										"async" triggers the asynchronous execution protocol
	 * 									
	 * <!-- end-model-doc -->
	 * @see #ASYNC_VALUE
	 * @generated
	 * @ordered
	 */
	ASYNC(1, "async", "async"),

	/**
	 * The '<em><b>Auto</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 										"auto" delegates the choice of execution mode to the server.
	 * 									
	 * <!-- end-model-doc -->
	 * @see #AUTO_VALUE
	 * @generated
	 * @ordered
	 */
	AUTO(2, "auto", "auto");

	/**
	 * The '<em><b>Sync</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 										"sync" triggers the synchronous execution protocol
	 * 									
	 * <!-- end-model-doc -->
	 * @see #SYNC
	 * @model name="sync"
	 * @generated
	 * @ordered
	 */
	public static final int SYNC_VALUE = 0;

	/**
	 * The '<em><b>Async</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 										"async" triggers the asynchronous execution protocol
	 * 									
	 * <!-- end-model-doc -->
	 * @see #ASYNC
	 * @model name="async"
	 * @generated
	 * @ordered
	 */
	public static final int ASYNC_VALUE = 1;

	/**
	 * The '<em><b>Auto</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 										"auto" delegates the choice of execution mode to the server.
	 * 									
	 * <!-- end-model-doc -->
	 * @see #AUTO
	 * @model name="auto"
	 * @generated
	 * @ordered
	 */
	public static final int AUTO_VALUE = 2;

	/**
	 * An array of all the '<em><b>Mode Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ModeType[] VALUES_ARRAY =
		new ModeType[] {
			SYNC,
			ASYNC,
			AUTO,
		};

	/**
	 * A public read-only list of all the '<em><b>Mode Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ModeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Mode Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ModeType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ModeType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Mode Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ModeType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ModeType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Mode Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ModeType get(int value) {
		switch (value) {
			case SYNC_VALUE: return SYNC;
			case ASYNC_VALUE: return ASYNC;
			case AUTO_VALUE: return AUTO;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private ModeType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //ModeType
