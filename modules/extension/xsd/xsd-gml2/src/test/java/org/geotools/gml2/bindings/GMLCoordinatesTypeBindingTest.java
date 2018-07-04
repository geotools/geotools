/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml2.bindings;

import org.geotools.gml2.GML;
import org.geotools.xml.AttributeInstance;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.XS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/** @source $URL$ */
public class GMLCoordinatesTypeBindingTest extends AbstractGMLBindingTest {
    AttributeInstance decimal;
    AttributeInstance ts;
    AttributeInstance cs;
    ElementInstance coordinates;
    MutablePicoContainer container;

    protected void setUp() throws Exception {
        super.setUp();

        decimal = createAtribute(GML.NAMESPACE, "decimal", XS.STRING, ".");
        ts = createAtribute(GML.NAMESPACE, "ts", XS.STRING, null);
        cs = createAtribute(GML.NAMESPACE, "cs", XS.STRING, null);
        coordinates = createElement(GML.NAMESPACE, "myCoordinates", GML.COORDTYPE, null);
        container = new DefaultPicoContainer();
        container.registerComponentInstance(CoordinateArraySequenceFactory.instance());
        container.registerComponentImplementation(GMLCoordinatesTypeBinding.class);
    }

    /*
     * Test method for 'org.geotools.gml2.strategies.GMLCoordinatesTypeBinding.parse(Element, Node[], Node[], Object)'
     */
    public void testParseDefaults() throws Exception {
        coordinates.setText("12.34,56.78 9.10,11.12 13.14,15.16");

        Node node = createNode(coordinates, null, null, null, null);
        GMLCoordinatesTypeBinding strategy =
                (GMLCoordinatesTypeBinding)
                        container.getComponentInstanceOfType(GMLCoordinatesTypeBinding.class);

        CoordinateSequence c = (CoordinateSequence) strategy.parse(coordinates, node, null);
        assertNotNull(c);
        assertEquals(3, c.size());
        assertEquals(c.getCoordinate(0), new Coordinate(12.34, 56.78));
        assertEquals(c.getCoordinate(1), new Coordinate(9.10, 11.12));
        assertEquals(c.getCoordinate(2), new Coordinate(13.14, 15.16));
    }

    public void testParseNonDefaults() throws Exception {
        coordinates.setText("12.34:56.78;9.10:11.12;13.14:15.16");

        Node node =
                createNode(
                        coordinates,
                        null,
                        null,
                        new AttributeInstance[] {cs, ts},
                        new String[] {":", ";"});

        GMLCoordinatesTypeBinding strategy =
                (GMLCoordinatesTypeBinding)
                        container.getComponentInstanceOfType(GMLCoordinatesTypeBinding.class);

        CoordinateSequence c = (CoordinateSequence) strategy.parse(coordinates, node, null);
        assertNotNull(c);
        assertEquals(3, c.size());
        assertEquals(c.getCoordinate(0), new Coordinate(12.34, 56.78));
        assertEquals(c.getCoordinate(1), new Coordinate(9.10, 11.12));
        assertEquals(c.getCoordinate(2), new Coordinate(13.14, 15.16));
    }

    /** Data coming with multiple blanks or newlines shouldn't happen, but it does */
    public void testParseMultipleBlankCharacters() throws Exception {
        coordinates.setText("\n12.34,56.78\n 9.10,11.12\t\t\n 13.14,15.16\t\n  ");

        Node node = createNode(coordinates, null, null, null, null);
        GMLCoordinatesTypeBinding strategy =
                (GMLCoordinatesTypeBinding)
                        container.getComponentInstanceOfType(GMLCoordinatesTypeBinding.class);

        CoordinateSequence c = (CoordinateSequence) strategy.parse(coordinates, node, null);
        assertNotNull(c);
        assertEquals(3, c.size());
        assertEquals(c.getCoordinate(0), new Coordinate(12.34, 56.78));
        assertEquals(c.getCoordinate(1), new Coordinate(9.10, 11.12));
        assertEquals(c.getCoordinate(2), new Coordinate(13.14, 15.16));
    }

    public void testSameSeparators() throws Exception {
        coordinates.setText(
                "20.6275752990001 69.0458485340001 23.6522989730001 67.9591256830001 23.394147676 67.4853849730001 23.7645843610001 67.4282118410001 23.5511838610001 67.171321484 23.995291303 66.8217779030001 23.6458027940001 66.3015228150001 24.1776751290001 65.6603581030001 23.8992902910001 65.363938606 23.1222006670001 65.2873440590001 21.8152155180001 64.8360917040001 22.099123046 64.4539134830001 21.3948099770001 63.9560658770001 21.3725223480001 63.6351714200001 20.097484583 63.163352188 19.4093118760001 63.068044048 18.2875747670001 62.4174439930001 18.0643892840001 61.9889935410001 17.883996095 60.9361734680001 19.1048713440001 60.6303523110001 19.118231322 60.073555147 19.991083185 59.5595338790001 19.816309173 59.2938278990001 18.9407839030001 58.8569371410001 17.5617016180001 58.396982409 17.2509618060001 57.614452509 17.6063095360001 57.349279976 18.1743547990001 57.9478307260001 18.9872553610001 58.177281001 18.7971457340001 58.4280731210001 19.1178240500001 58.5937904720001 19.6780403080001 58.44086136 19.7036982210001 57.8930463090001 19.3030909770001 57.5766251890001 19.3544883810001 57.3589766740001 18.5284181070001 56.7710770250001 17.8834277610001 56.758222791 17.4898702400001 57.2317293520001 16.6072220550001 56.0283846240001 15.7667531740001 55.751910502 15.152757147 55.8982079070001 14.5944246270001 55.755000302 14.7016791680001 55.6034274630001 14.1722953990001 55.177492537 12.9247851440001 55.148989542 12.6121940250001 55.4320428880001 12.8957081780001 55.64353357 12.6525834940001 56.0457802180001 12.1174567820001 56.3831608610001 12.2974291680001 56.6364623520001 11.2025644020001 57.674656364 10.592075917 58.760985131 11.3399133590001 59.114979066 11.4574487390001 58.8884361220001 11.652005021 58.9062336490001 11.8397152050001 59.8407838300001 12.3411394010001 59.9656698890001 12.541907601 60.193378733 12.6068834610001 60.5127437800001 12.223992303 61.0130781190001 12.6814029960001 61.0595377940001 12.8708379260001 61.35649483 12.1376663360001 61.7238176260001 12.2993694230001 62.2674935150001 11.9745828370001 63.2692271230001 12.683566484 63.974222475 13.2111085380001 64.0953681140001 13.9675251440001 64.007968766 14.157109645 64.1950531990001 14.1138697580001 64.462483723 13.6542581190001 64.5803397670001 14.5068320140001 65.3097281190001 14.516287386 66.132577573 15.484729385 66.282457526 15.3772232190001 66.4843027150001 16.387754896 67.0454610790001 16.0898237740001 67.4352772250001 16.7381138730001 67.914208257 17.2815163380001 68.1188137410001 17.8997572840001 67.9693709380001 18.405678815 68.5818759750001 19.9213869880001 68.3560124810001 20.3358613110001 68.8023123440001 20.0600357110001 69.045758543 20.6275752990001 69.0458485340001");

        Node node =
                createNode(
                        coordinates,
                        null,
                        null,
                        new AttributeInstance[] {cs, ts},
                        new String[] {" ", " "});
        GMLCoordinatesTypeBinding strategy =
                (GMLCoordinatesTypeBinding)
                        container.getComponentInstanceOfType(GMLCoordinatesTypeBinding.class);

        CoordinateSequence c = (CoordinateSequence) strategy.parse(coordinates, node, null);
        assertNotNull(c);
        assertEquals(85, c.size());
    }
}
