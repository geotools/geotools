/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import net.opengis.gml.AbstractGMLType;
import net.opengis.gml.AbstractGeometricPrimitiveType;
import net.opengis.gml.AbstractGeometryType;
import net.opengis.gml.AbstractMetaDataType;
import net.opengis.gml.AbstractRingPropertyType;
import net.opengis.gml.AbstractRingType;
import net.opengis.gml.AbstractSurfaceType;
import net.opengis.gml.BoundingShapeType;
import net.opengis.gml.CodeType;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.DocumentRoot;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.EnvelopeWithTimePeriodType;
import net.opengis.gml.GmlPackage;

import net.opengis.gml.GridType;
import net.opengis.gml.LinearRingType;
import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.PolygonType;
import net.opengis.gml.RectifiedGridType;
import net.opengis.gml.StringOrRefType;
import net.opengis.gml.TimePositionType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getGeometricPrimitive <em>Geometric Primitive</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getGeometry <em>Geometry</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getGML <em>GML</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getObject <em>Object</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getMetaData <em>Meta Data</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getRing <em>Ring</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getSurface <em>Surface</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getBoundedBy <em>Bounded By</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getEnvelope <em>Envelope</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getEnvelopeWithTimePeriod <em>Envelope With Time Period</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getExterior <em>Exterior</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getGrid <em>Grid</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getInterior <em>Interior</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getLinearRing <em>Linear Ring</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getMetaDataProperty <em>Meta Data Property</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getPolygon <em>Polygon</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getRectifiedGrid <em>Rectified Grid</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getTimePosition <em>Time Position</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.gml.impl.DocumentRootImpl#getRemoteSchema <em>Remote Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
    /**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap mixed;

    /**
	 * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getXMLNSPrefixMap()
	 * @generated
	 * @ordered
	 */
    protected EMap xMLNSPrefixMap;

    /**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
    protected EMap xSISchemaLocation;

    /**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected String id = ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getRemoteSchema() <em>Remote Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRemoteSchema()
	 * @generated
	 * @ordered
	 */
    protected static final String REMOTE_SCHEMA_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getRemoteSchema() <em>Remote Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRemoteSchema()
	 * @generated
	 * @ordered
	 */
    protected String remoteSchema = REMOTE_SCHEMA_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected DocumentRootImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return GmlPackage.Literals.DOCUMENT_ROOT;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, GmlPackage.DOCUMENT_ROOT__MIXED);
		}
		return mixed;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EMap getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, GmlPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EMap getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, GmlPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractGeometricPrimitiveType getGeometricPrimitive() {
		return (AbstractGeometricPrimitiveType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGeometricPrimitive(AbstractGeometricPrimitiveType newGeometricPrimitive, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE, newGeometricPrimitive, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractGeometryType getGeometry() {
		return (AbstractGeometryType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__GEOMETRY, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGeometry(AbstractGeometryType newGeometry, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__GEOMETRY, newGeometry, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractGMLType getGML() {
		return (AbstractGMLType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__GML, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGML(AbstractGMLType newGML, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__GML, newGML, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EObject getObject() {
		return (EObject)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__OBJECT, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetObject(EObject newObject, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__OBJECT, newObject, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractMetaDataType getMetaData() {
		return (AbstractMetaDataType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__META_DATA, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMetaData(AbstractMetaDataType newMetaData, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__META_DATA, newMetaData, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractRingType getRing() {
		return (AbstractRingType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__RING, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRing(AbstractRingType newRing, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__RING, newRing, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractSurfaceType getSurface() {
		return (AbstractSurfaceType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__SURFACE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSurface(AbstractSurfaceType newSurface, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__SURFACE, newSurface, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public BoundingShapeType getBoundedBy() {
		return (BoundingShapeType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__BOUNDED_BY, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetBoundedBy(BoundingShapeType newBoundedBy, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__BOUNDED_BY, newBoundedBy, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBoundedBy(BoundingShapeType newBoundedBy) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__BOUNDED_BY, newBoundedBy);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public StringOrRefType getDescription() {
		return (StringOrRefType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__DESCRIPTION, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDescription(StringOrRefType newDescription, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__DESCRIPTION, newDescription, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(StringOrRefType newDescription) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__DESCRIPTION, newDescription);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EnvelopeType getEnvelope() {
		return (EnvelopeType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__ENVELOPE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEnvelope(EnvelopeType newEnvelope, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__ENVELOPE, newEnvelope, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEnvelope(EnvelopeType newEnvelope) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__ENVELOPE, newEnvelope);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EnvelopeWithTimePeriodType getEnvelopeWithTimePeriod() {
		return (EnvelopeWithTimePeriodType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEnvelopeWithTimePeriod(EnvelopeWithTimePeriodType newEnvelopeWithTimePeriod, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD, newEnvelopeWithTimePeriod, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEnvelopeWithTimePeriod(EnvelopeWithTimePeriodType newEnvelopeWithTimePeriod) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD, newEnvelopeWithTimePeriod);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractRingPropertyType getExterior() {
		return (AbstractRingPropertyType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__EXTERIOR, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetExterior(AbstractRingPropertyType newExterior, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__EXTERIOR, newExterior, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExterior(AbstractRingPropertyType newExterior) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__EXTERIOR, newExterior);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public GridType getGrid() {
		return (GridType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__GRID, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGrid(GridType newGrid, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__GRID, newGrid, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGrid(GridType newGrid) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__GRID, newGrid);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractRingPropertyType getInterior() {
		return (AbstractRingPropertyType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__INTERIOR, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetInterior(AbstractRingPropertyType newInterior, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__INTERIOR, newInterior, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInterior(AbstractRingPropertyType newInterior) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__INTERIOR, newInterior);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public LinearRingType getLinearRing() {
		return (LinearRingType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__LINEAR_RING, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLinearRing(LinearRingType newLinearRing, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__LINEAR_RING, newLinearRing, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLinearRing(LinearRingType newLinearRing) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__LINEAR_RING, newLinearRing);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public MetaDataPropertyType getMetaDataProperty() {
		return (MetaDataPropertyType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__META_DATA_PROPERTY, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMetaDataProperty(MetaDataPropertyType newMetaDataProperty, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__META_DATA_PROPERTY, newMetaDataProperty, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMetaDataProperty(MetaDataPropertyType newMetaDataProperty) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__META_DATA_PROPERTY, newMetaDataProperty);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CodeType getName() {
		return (CodeType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__NAME, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetName(CodeType newName, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__NAME, newName, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(CodeType newName) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__NAME, newName);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public PolygonType getPolygon() {
		return (PolygonType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__POLYGON, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPolygon(PolygonType newPolygon, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__POLYGON, newPolygon, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPolygon(PolygonType newPolygon) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__POLYGON, newPolygon);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public DirectPositionType getPos() {
		return (DirectPositionType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__POS, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPos(DirectPositionType newPos, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__POS, newPos, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPos(DirectPositionType newPos) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__POS, newPos);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public RectifiedGridType getRectifiedGrid() {
		return (RectifiedGridType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__RECTIFIED_GRID, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRectifiedGrid(RectifiedGridType newRectifiedGrid, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__RECTIFIED_GRID, newRectifiedGrid, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRectifiedGrid(RectifiedGridType newRectifiedGrid) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__RECTIFIED_GRID, newRectifiedGrid);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimePositionType getTimePosition() {
		return (TimePositionType)getMixed().get(GmlPackage.Literals.DOCUMENT_ROOT__TIME_POSITION, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTimePosition(TimePositionType newTimePosition, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GmlPackage.Literals.DOCUMENT_ROOT__TIME_POSITION, newTimePosition, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTimePosition(TimePositionType newTimePosition) {
		((FeatureMap.Internal)getMixed()).set(GmlPackage.Literals.DOCUMENT_ROOT__TIME_POSITION, newTimePosition);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getId() {
		return id;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.DOCUMENT_ROOT__ID, oldId, id));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getRemoteSchema() {
		return remoteSchema;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRemoteSchema(String newRemoteSchema) {
		String oldRemoteSchema = remoteSchema;
		remoteSchema = newRemoteSchema;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.DOCUMENT_ROOT__REMOTE_SCHEMA, oldRemoteSchema, remoteSchema));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GmlPackage.DOCUMENT_ROOT__MIXED:
				return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
			case GmlPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case GmlPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case GmlPackage.DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE:
				return basicSetGeometricPrimitive(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__GEOMETRY:
				return basicSetGeometry(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__GML:
				return basicSetGML(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__OBJECT:
				return basicSetObject(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__META_DATA:
				return basicSetMetaData(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__RING:
				return basicSetRing(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__SURFACE:
				return basicSetSurface(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__BOUNDED_BY:
				return basicSetBoundedBy(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__DESCRIPTION:
				return basicSetDescription(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE:
				return basicSetEnvelope(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD:
				return basicSetEnvelopeWithTimePeriod(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__EXTERIOR:
				return basicSetExterior(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__GRID:
				return basicSetGrid(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__INTERIOR:
				return basicSetInterior(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__LINEAR_RING:
				return basicSetLinearRing(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__META_DATA_PROPERTY:
				return basicSetMetaDataProperty(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__NAME:
				return basicSetName(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__POLYGON:
				return basicSetPolygon(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__POS:
				return basicSetPos(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__RECTIFIED_GRID:
				return basicSetRectifiedGrid(null, msgs);
			case GmlPackage.DOCUMENT_ROOT__TIME_POSITION:
				return basicSetTimePosition(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GmlPackage.DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case GmlPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case GmlPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case GmlPackage.DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE:
				return getGeometricPrimitive();
			case GmlPackage.DOCUMENT_ROOT__GEOMETRY:
				return getGeometry();
			case GmlPackage.DOCUMENT_ROOT__GML:
				return getGML();
			case GmlPackage.DOCUMENT_ROOT__OBJECT:
				return getObject();
			case GmlPackage.DOCUMENT_ROOT__META_DATA:
				return getMetaData();
			case GmlPackage.DOCUMENT_ROOT__RING:
				return getRing();
			case GmlPackage.DOCUMENT_ROOT__SURFACE:
				return getSurface();
			case GmlPackage.DOCUMENT_ROOT__BOUNDED_BY:
				return getBoundedBy();
			case GmlPackage.DOCUMENT_ROOT__DESCRIPTION:
				return getDescription();
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE:
				return getEnvelope();
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD:
				return getEnvelopeWithTimePeriod();
			case GmlPackage.DOCUMENT_ROOT__EXTERIOR:
				return getExterior();
			case GmlPackage.DOCUMENT_ROOT__GRID:
				return getGrid();
			case GmlPackage.DOCUMENT_ROOT__INTERIOR:
				return getInterior();
			case GmlPackage.DOCUMENT_ROOT__LINEAR_RING:
				return getLinearRing();
			case GmlPackage.DOCUMENT_ROOT__META_DATA_PROPERTY:
				return getMetaDataProperty();
			case GmlPackage.DOCUMENT_ROOT__NAME:
				return getName();
			case GmlPackage.DOCUMENT_ROOT__POLYGON:
				return getPolygon();
			case GmlPackage.DOCUMENT_ROOT__POS:
				return getPos();
			case GmlPackage.DOCUMENT_ROOT__RECTIFIED_GRID:
				return getRectifiedGrid();
			case GmlPackage.DOCUMENT_ROOT__TIME_POSITION:
				return getTimePosition();
			case GmlPackage.DOCUMENT_ROOT__ID:
				return getId();
			case GmlPackage.DOCUMENT_ROOT__REMOTE_SCHEMA:
				return getRemoteSchema();
		}
		return super.eGet(featureID, resolve, coreType);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GmlPackage.DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__BOUNDED_BY:
				setBoundedBy((BoundingShapeType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__DESCRIPTION:
				setDescription((StringOrRefType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE:
				setEnvelope((EnvelopeType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD:
				setEnvelopeWithTimePeriod((EnvelopeWithTimePeriodType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__EXTERIOR:
				setExterior((AbstractRingPropertyType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__GRID:
				setGrid((GridType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__INTERIOR:
				setInterior((AbstractRingPropertyType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__LINEAR_RING:
				setLinearRing((LinearRingType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__META_DATA_PROPERTY:
				setMetaDataProperty((MetaDataPropertyType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__NAME:
				setName((CodeType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__POLYGON:
				setPolygon((PolygonType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__POS:
				setPos((DirectPositionType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__RECTIFIED_GRID:
				setRectifiedGrid((RectifiedGridType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__TIME_POSITION:
				setTimePosition((TimePositionType)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__ID:
				setId((String)newValue);
				return;
			case GmlPackage.DOCUMENT_ROOT__REMOTE_SCHEMA:
				setRemoteSchema((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
		switch (featureID) {
			case GmlPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case GmlPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case GmlPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case GmlPackage.DOCUMENT_ROOT__BOUNDED_BY:
				setBoundedBy((BoundingShapeType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__DESCRIPTION:
				setDescription((StringOrRefType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE:
				setEnvelope((EnvelopeType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD:
				setEnvelopeWithTimePeriod((EnvelopeWithTimePeriodType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__EXTERIOR:
				setExterior((AbstractRingPropertyType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__GRID:
				setGrid((GridType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__INTERIOR:
				setInterior((AbstractRingPropertyType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__LINEAR_RING:
				setLinearRing((LinearRingType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__META_DATA_PROPERTY:
				setMetaDataProperty((MetaDataPropertyType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__NAME:
				setName((CodeType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__POLYGON:
				setPolygon((PolygonType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__POS:
				setPos((DirectPositionType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__RECTIFIED_GRID:
				setRectifiedGrid((RectifiedGridType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__TIME_POSITION:
				setTimePosition((TimePositionType)null);
				return;
			case GmlPackage.DOCUMENT_ROOT__ID:
				setId(ID_EDEFAULT);
				return;
			case GmlPackage.DOCUMENT_ROOT__REMOTE_SCHEMA:
				setRemoteSchema(REMOTE_SCHEMA_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GmlPackage.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case GmlPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case GmlPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case GmlPackage.DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE:
				return getGeometricPrimitive() != null;
			case GmlPackage.DOCUMENT_ROOT__GEOMETRY:
				return getGeometry() != null;
			case GmlPackage.DOCUMENT_ROOT__GML:
				return getGML() != null;
			case GmlPackage.DOCUMENT_ROOT__OBJECT:
				return getObject() != null;
			case GmlPackage.DOCUMENT_ROOT__META_DATA:
				return getMetaData() != null;
			case GmlPackage.DOCUMENT_ROOT__RING:
				return getRing() != null;
			case GmlPackage.DOCUMENT_ROOT__SURFACE:
				return getSurface() != null;
			case GmlPackage.DOCUMENT_ROOT__BOUNDED_BY:
				return getBoundedBy() != null;
			case GmlPackage.DOCUMENT_ROOT__DESCRIPTION:
				return getDescription() != null;
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE:
				return getEnvelope() != null;
			case GmlPackage.DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD:
				return getEnvelopeWithTimePeriod() != null;
			case GmlPackage.DOCUMENT_ROOT__EXTERIOR:
				return getExterior() != null;
			case GmlPackage.DOCUMENT_ROOT__GRID:
				return getGrid() != null;
			case GmlPackage.DOCUMENT_ROOT__INTERIOR:
				return getInterior() != null;
			case GmlPackage.DOCUMENT_ROOT__LINEAR_RING:
				return getLinearRing() != null;
			case GmlPackage.DOCUMENT_ROOT__META_DATA_PROPERTY:
				return getMetaDataProperty() != null;
			case GmlPackage.DOCUMENT_ROOT__NAME:
				return getName() != null;
			case GmlPackage.DOCUMENT_ROOT__POLYGON:
				return getPolygon() != null;
			case GmlPackage.DOCUMENT_ROOT__POS:
				return getPos() != null;
			case GmlPackage.DOCUMENT_ROOT__RECTIFIED_GRID:
				return getRectifiedGrid() != null;
			case GmlPackage.DOCUMENT_ROOT__TIME_POSITION:
				return getTimePosition() != null;
			case GmlPackage.DOCUMENT_ROOT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case GmlPackage.DOCUMENT_ROOT__REMOTE_SCHEMA:
				return REMOTE_SCHEMA_EDEFAULT == null ? remoteSchema != null : !REMOTE_SCHEMA_EDEFAULT.equals(remoteSchema);
		}
		return super.eIsSet(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(", id: ");
		result.append(id);
		result.append(", remoteSchema: ");
		result.append(remoteSchema);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
