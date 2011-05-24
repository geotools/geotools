/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import org.opengis.annotation.Extension;
import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;


/**
 * A PointPlacement specifies how a text label is positioned relative to a
 * geometric point.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/PointPlacement.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Ian Turton
 * @since GeoAPI 2.2
 */
@XmlElement("PointPlacement")
public interface PointPlacement extends LabelPlacement {

    /**
     * The AnchorPoint element of a PointPlacement gives the location inside of a label to use
     * for anchoring the label to the main-geometry point.
     *
     * This will often be used to avoid over-plotting a graphic symbol marking a city or some
     * such feature. The displacements are in units of pixels above and to the right of the point.
     * A system may reflect this displacement about the X and/or Y axes to de-conflict labels.
     * The default displacement is X=0, Y=0.
     *
     * See {@link AnchorPoint} for details.
     *
     * @return AnchorPoint : if null use X=0.5 Y=0.5
     */
    @XmlElement("PointPlacement")
    AnchorPoint getAnchorPoint();

    /**
     * The Displacement gives the X and Y displacements from the "hot-spot" point. This
     * element may be used to avoid over-plotting of multiple graphic symbols used as part of
     * the same point symbol. The displacements are in units of measure above and to the right
     * of the point. The default displacement is X=0, Y=0.
     *
     * If Displacement is used in conjunction with Size and/or Rotation then the graphic
     * symbol shall be scaled and/or rotated before it is displaced.s
     *
     * @return Displacement
     */
    @XmlElement("Displacement")
    Displacement getDisplacement();

    /**
     * Returns the expression that will be used to calculate the rotation of the
     * graphic when it is drawn.
     *
     * The Rotation of a PointPlacement gives the clockwise rotation of the label in degrees
     * from the normal direction for a font (left-to-right for Latin-derived human languages at
     * least).
     *
     * @return Expression
     */
    @XmlElement("Rotation")
    Expression getRotation();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);

}
