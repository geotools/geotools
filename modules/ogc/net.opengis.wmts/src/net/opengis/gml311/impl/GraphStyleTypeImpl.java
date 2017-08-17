/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.AesheticCriteriaType;
import net.opengis.gml311.DrawingTypeType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.GraphStyleType;
import net.opengis.gml311.GraphTypeType;
import net.opengis.gml311.LineTypeType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Graph Style Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#isPlanar <em>Planar</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#isDirected <em>Directed</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#isGrid <em>Grid</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#getMinDistance <em>Min Distance</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#getMinAngle <em>Min Angle</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#getGraphType <em>Graph Type</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#getDrawingType <em>Drawing Type</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#getLineType <em>Line Type</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GraphStyleTypeImpl#getAestheticCriteria <em>Aesthetic Criteria</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GraphStyleTypeImpl extends BaseStyleDescriptorTypeImpl implements GraphStyleType {
    /**
     * The default value of the '{@link #isPlanar() <em>Planar</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isPlanar()
     * @generated
     * @ordered
     */
    protected static final boolean PLANAR_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isPlanar() <em>Planar</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isPlanar()
     * @generated
     * @ordered
     */
    protected boolean planar = PLANAR_EDEFAULT;

    /**
     * This is true if the Planar attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean planarESet;

    /**
     * The default value of the '{@link #isDirected() <em>Directed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDirected()
     * @generated
     * @ordered
     */
    protected static final boolean DIRECTED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isDirected() <em>Directed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDirected()
     * @generated
     * @ordered
     */
    protected boolean directed = DIRECTED_EDEFAULT;

    /**
     * This is true if the Directed attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean directedESet;

    /**
     * The default value of the '{@link #isGrid() <em>Grid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGrid()
     * @generated
     * @ordered
     */
    protected static final boolean GRID_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGrid() <em>Grid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGrid()
     * @generated
     * @ordered
     */
    protected boolean grid = GRID_EDEFAULT;

    /**
     * This is true if the Grid attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean gridESet;

    /**
     * The default value of the '{@link #getMinDistance() <em>Min Distance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinDistance()
     * @generated
     * @ordered
     */
    protected static final double MIN_DISTANCE_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getMinDistance() <em>Min Distance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinDistance()
     * @generated
     * @ordered
     */
    protected double minDistance = MIN_DISTANCE_EDEFAULT;

    /**
     * This is true if the Min Distance attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean minDistanceESet;

    /**
     * The default value of the '{@link #getMinAngle() <em>Min Angle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinAngle()
     * @generated
     * @ordered
     */
    protected static final double MIN_ANGLE_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getMinAngle() <em>Min Angle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinAngle()
     * @generated
     * @ordered
     */
    protected double minAngle = MIN_ANGLE_EDEFAULT;

    /**
     * This is true if the Min Angle attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean minAngleESet;

    /**
     * The default value of the '{@link #getGraphType() <em>Graph Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGraphType()
     * @generated
     * @ordered
     */
    protected static final GraphTypeType GRAPH_TYPE_EDEFAULT = GraphTypeType.TREE;

    /**
     * The cached value of the '{@link #getGraphType() <em>Graph Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGraphType()
     * @generated
     * @ordered
     */
    protected GraphTypeType graphType = GRAPH_TYPE_EDEFAULT;

    /**
     * This is true if the Graph Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean graphTypeESet;

    /**
     * The default value of the '{@link #getDrawingType() <em>Drawing Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDrawingType()
     * @generated
     * @ordered
     */
    protected static final DrawingTypeType DRAWING_TYPE_EDEFAULT = DrawingTypeType.POLYLINE;

    /**
     * The cached value of the '{@link #getDrawingType() <em>Drawing Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDrawingType()
     * @generated
     * @ordered
     */
    protected DrawingTypeType drawingType = DRAWING_TYPE_EDEFAULT;

    /**
     * This is true if the Drawing Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean drawingTypeESet;

    /**
     * The default value of the '{@link #getLineType() <em>Line Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineType()
     * @generated
     * @ordered
     */
    protected static final LineTypeType LINE_TYPE_EDEFAULT = LineTypeType.STRAIGHT;

    /**
     * The cached value of the '{@link #getLineType() <em>Line Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineType()
     * @generated
     * @ordered
     */
    protected LineTypeType lineType = LINE_TYPE_EDEFAULT;

    /**
     * This is true if the Line Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean lineTypeESet;

    /**
     * The cached value of the '{@link #getAestheticCriteria() <em>Aesthetic Criteria</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAestheticCriteria()
     * @generated
     * @ordered
     */
    protected EList<AesheticCriteriaType> aestheticCriteria;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GraphStyleTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGraphStyleType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isPlanar() {
        return planar;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPlanar(boolean newPlanar) {
        boolean oldPlanar = planar;
        planar = newPlanar;
        boolean oldPlanarESet = planarESet;
        planarESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__PLANAR, oldPlanar, planar, !oldPlanarESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetPlanar() {
        boolean oldPlanar = planar;
        boolean oldPlanarESet = planarESet;
        planar = PLANAR_EDEFAULT;
        planarESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__PLANAR, oldPlanar, PLANAR_EDEFAULT, oldPlanarESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetPlanar() {
        return planarESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDirected(boolean newDirected) {
        boolean oldDirected = directed;
        directed = newDirected;
        boolean oldDirectedESet = directedESet;
        directedESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__DIRECTED, oldDirected, directed, !oldDirectedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetDirected() {
        boolean oldDirected = directed;
        boolean oldDirectedESet = directedESet;
        directed = DIRECTED_EDEFAULT;
        directedESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__DIRECTED, oldDirected, DIRECTED_EDEFAULT, oldDirectedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetDirected() {
        return directedESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGrid() {
        return grid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGrid(boolean newGrid) {
        boolean oldGrid = grid;
        grid = newGrid;
        boolean oldGridESet = gridESet;
        gridESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__GRID, oldGrid, grid, !oldGridESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetGrid() {
        boolean oldGrid = grid;
        boolean oldGridESet = gridESet;
        grid = GRID_EDEFAULT;
        gridESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__GRID, oldGrid, GRID_EDEFAULT, oldGridESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetGrid() {
        return gridESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinDistance(double newMinDistance) {
        double oldMinDistance = minDistance;
        minDistance = newMinDistance;
        boolean oldMinDistanceESet = minDistanceESet;
        minDistanceESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__MIN_DISTANCE, oldMinDistance, minDistance, !oldMinDistanceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMinDistance() {
        double oldMinDistance = minDistance;
        boolean oldMinDistanceESet = minDistanceESet;
        minDistance = MIN_DISTANCE_EDEFAULT;
        minDistanceESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__MIN_DISTANCE, oldMinDistance, MIN_DISTANCE_EDEFAULT, oldMinDistanceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMinDistance() {
        return minDistanceESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getMinAngle() {
        return minAngle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinAngle(double newMinAngle) {
        double oldMinAngle = minAngle;
        minAngle = newMinAngle;
        boolean oldMinAngleESet = minAngleESet;
        minAngleESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__MIN_ANGLE, oldMinAngle, minAngle, !oldMinAngleESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMinAngle() {
        double oldMinAngle = minAngle;
        boolean oldMinAngleESet = minAngleESet;
        minAngle = MIN_ANGLE_EDEFAULT;
        minAngleESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__MIN_ANGLE, oldMinAngle, MIN_ANGLE_EDEFAULT, oldMinAngleESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMinAngle() {
        return minAngleESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GraphTypeType getGraphType() {
        return graphType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGraphType(GraphTypeType newGraphType) {
        GraphTypeType oldGraphType = graphType;
        graphType = newGraphType == null ? GRAPH_TYPE_EDEFAULT : newGraphType;
        boolean oldGraphTypeESet = graphTypeESet;
        graphTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__GRAPH_TYPE, oldGraphType, graphType, !oldGraphTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetGraphType() {
        GraphTypeType oldGraphType = graphType;
        boolean oldGraphTypeESet = graphTypeESet;
        graphType = GRAPH_TYPE_EDEFAULT;
        graphTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__GRAPH_TYPE, oldGraphType, GRAPH_TYPE_EDEFAULT, oldGraphTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetGraphType() {
        return graphTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DrawingTypeType getDrawingType() {
        return drawingType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDrawingType(DrawingTypeType newDrawingType) {
        DrawingTypeType oldDrawingType = drawingType;
        drawingType = newDrawingType == null ? DRAWING_TYPE_EDEFAULT : newDrawingType;
        boolean oldDrawingTypeESet = drawingTypeESet;
        drawingTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__DRAWING_TYPE, oldDrawingType, drawingType, !oldDrawingTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetDrawingType() {
        DrawingTypeType oldDrawingType = drawingType;
        boolean oldDrawingTypeESet = drawingTypeESet;
        drawingType = DRAWING_TYPE_EDEFAULT;
        drawingTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__DRAWING_TYPE, oldDrawingType, DRAWING_TYPE_EDEFAULT, oldDrawingTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetDrawingType() {
        return drawingTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LineTypeType getLineType() {
        return lineType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLineType(LineTypeType newLineType) {
        LineTypeType oldLineType = lineType;
        lineType = newLineType == null ? LINE_TYPE_EDEFAULT : newLineType;
        boolean oldLineTypeESet = lineTypeESet;
        lineTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRAPH_STYLE_TYPE__LINE_TYPE, oldLineType, lineType, !oldLineTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetLineType() {
        LineTypeType oldLineType = lineType;
        boolean oldLineTypeESet = lineTypeESet;
        lineType = LINE_TYPE_EDEFAULT;
        lineTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.GRAPH_STYLE_TYPE__LINE_TYPE, oldLineType, LINE_TYPE_EDEFAULT, oldLineTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetLineType() {
        return lineTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AesheticCriteriaType> getAestheticCriteria() {
        if (aestheticCriteria == null) {
            aestheticCriteria = new EDataTypeEList<AesheticCriteriaType>(AesheticCriteriaType.class, this, Gml311Package.GRAPH_STYLE_TYPE__AESTHETIC_CRITERIA);
        }
        return aestheticCriteria;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.GRAPH_STYLE_TYPE__PLANAR:
                return isPlanar();
            case Gml311Package.GRAPH_STYLE_TYPE__DIRECTED:
                return isDirected();
            case Gml311Package.GRAPH_STYLE_TYPE__GRID:
                return isGrid();
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_DISTANCE:
                return getMinDistance();
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_ANGLE:
                return getMinAngle();
            case Gml311Package.GRAPH_STYLE_TYPE__GRAPH_TYPE:
                return getGraphType();
            case Gml311Package.GRAPH_STYLE_TYPE__DRAWING_TYPE:
                return getDrawingType();
            case Gml311Package.GRAPH_STYLE_TYPE__LINE_TYPE:
                return getLineType();
            case Gml311Package.GRAPH_STYLE_TYPE__AESTHETIC_CRITERIA:
                return getAestheticCriteria();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.GRAPH_STYLE_TYPE__PLANAR:
                setPlanar((Boolean)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__DIRECTED:
                setDirected((Boolean)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__GRID:
                setGrid((Boolean)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_DISTANCE:
                setMinDistance((Double)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_ANGLE:
                setMinAngle((Double)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__GRAPH_TYPE:
                setGraphType((GraphTypeType)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__DRAWING_TYPE:
                setDrawingType((DrawingTypeType)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__LINE_TYPE:
                setLineType((LineTypeType)newValue);
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__AESTHETIC_CRITERIA:
                getAestheticCriteria().clear();
                getAestheticCriteria().addAll((Collection<? extends AesheticCriteriaType>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Gml311Package.GRAPH_STYLE_TYPE__PLANAR:
                unsetPlanar();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__DIRECTED:
                unsetDirected();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__GRID:
                unsetGrid();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_DISTANCE:
                unsetMinDistance();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_ANGLE:
                unsetMinAngle();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__GRAPH_TYPE:
                unsetGraphType();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__DRAWING_TYPE:
                unsetDrawingType();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__LINE_TYPE:
                unsetLineType();
                return;
            case Gml311Package.GRAPH_STYLE_TYPE__AESTHETIC_CRITERIA:
                getAestheticCriteria().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Gml311Package.GRAPH_STYLE_TYPE__PLANAR:
                return isSetPlanar();
            case Gml311Package.GRAPH_STYLE_TYPE__DIRECTED:
                return isSetDirected();
            case Gml311Package.GRAPH_STYLE_TYPE__GRID:
                return isSetGrid();
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_DISTANCE:
                return isSetMinDistance();
            case Gml311Package.GRAPH_STYLE_TYPE__MIN_ANGLE:
                return isSetMinAngle();
            case Gml311Package.GRAPH_STYLE_TYPE__GRAPH_TYPE:
                return isSetGraphType();
            case Gml311Package.GRAPH_STYLE_TYPE__DRAWING_TYPE:
                return isSetDrawingType();
            case Gml311Package.GRAPH_STYLE_TYPE__LINE_TYPE:
                return isSetLineType();
            case Gml311Package.GRAPH_STYLE_TYPE__AESTHETIC_CRITERIA:
                return aestheticCriteria != null && !aestheticCriteria.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (planar: ");
        if (planarESet) result.append(planar); else result.append("<unset>");
        result.append(", directed: ");
        if (directedESet) result.append(directed); else result.append("<unset>");
        result.append(", grid: ");
        if (gridESet) result.append(grid); else result.append("<unset>");
        result.append(", minDistance: ");
        if (minDistanceESet) result.append(minDistance); else result.append("<unset>");
        result.append(", minAngle: ");
        if (minAngleESet) result.append(minAngle); else result.append("<unset>");
        result.append(", graphType: ");
        if (graphTypeESet) result.append(graphType); else result.append("<unset>");
        result.append(", drawingType: ");
        if (drawingTypeESet) result.append(drawingType); else result.append("<unset>");
        result.append(", lineType: ");
        if (lineTypeESet) result.append(lineType); else result.append("<unset>");
        result.append(", aestheticCriteria: ");
        result.append(aestheticCriteria);
        result.append(')');
        return result.toString();
    }

} //GraphStyleTypeImpl
