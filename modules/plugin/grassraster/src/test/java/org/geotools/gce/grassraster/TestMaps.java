/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import static java.lang.Double.NaN;

/**
 * Support maps for testcases. 
 * 
 * <p>These data are all synthetically created
 * by the author and released into Public Domain.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 * @source $URL$
 */
public class TestMaps {
    static double[][] matrix = new double[][]{
            {800.0, 900.0, 1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0, 1450.0, 1500.0},
            {600.0, 650.0, 750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0, 1250.0, 1500.0},
            {500.0, 550.0, 700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0, 1500.0},
            {400.0, 410.0, 650.0, 700.0, 750.0, 800.0, 850.0, 800.0, 800.0, 1500.0},
            {450.0, 550.0, 430.0, 500.0, 600.0, 700.0, 800.0, 800.0, 800.0, 1500.0},
            {500.0, 600.0, 700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0, 1500.0},
            {600.0, 700.0, 750.0, 800.0, 780.0, 790.0, 1000.0, 1100.0, 1250.0, 1500.0},
            {800.0, 910.0, 980.0, 1001.0, 1150.0, 1200.0, 1250.0, 1300.0, 1450.0, 1500.0}

    };

    static double[][] matrixMore = new double[][]{
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN, 800.0, 900.0, 1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0, 1450.0, 1500.0, NaN, NaN},
            {NaN, NaN, 600.0, 650.0, 750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0, 1250.0, 1500.0, NaN, NaN},
            {NaN, NaN, 500.0, 550.0, 700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0, 1500.0, NaN, NaN},
            {NaN, NaN, 400.0, 410.0, 650.0, 700.0, 750.0, 800.0, 850.0, 800.0, 800.0, 1500.0, NaN, NaN},
            {NaN, NaN, 450.0, 550.0, 430.0, 500.0, 600.0, 700.0, 800.0, 800.0, 800.0, 1500.0, NaN, NaN},
            {NaN, NaN, 500.0, 600.0, 700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0, 1500.0, NaN, NaN},
            {NaN, NaN, 600.0, 700.0, 750.0, 800.0, 780.0, 790.0, 1000.0, 1100.0, 1250.0, 1500.0, NaN, NaN},
            {NaN, NaN, 800.0, 910.0, 980.0, 1001.0, 1150.0, 1200.0, 1250.0, 1300.0, 1450.0, 1500.0, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN}};

    static double[][] matrixDifferentResolution2 = new double[][]{
            {800.0,    900.0,   1000.0,  1200.0,  1250.0,  1300.0,  1450.0,  1500.0 }, 
            {500.0,    550.0,   750.0,   800.0,   850.0,   900.0,   1100.0,  1500.0}, 
            {400.0,    410.0,   700.0,   750.0,   800.0,   850.0,   800.0,   1500.0},  
            {450.0,    550.0,   500.0,   600.0,   700.0,   800.0,   800.0,   1500.0},  
            {600.0,    700.0,   800.0,   780.0,   790.0,   1000.0,  1250.0,  1500.0},  
            {800.0,    910.0,   1001.0,  1150.0,  1200.0,  1250.0,  1450.0,  1500.0}  
    };

    static double[][] matrixDifferentResolution = new double[][]{
            {800.0, 1000.0, 1200.0, 1300.0, 1450.0},
            {500.0, 700.0, 800.0, 900.0, 1100.0,}, 
            {450.0, 430.0, 600.0, 800.0, 800.0},
            {600.0, 750.0, 780.0, 1000.0, 1250.0}, 
            {800.0, 980.0, 1150.0, 1250.0, 1450.0}
    };

    static double matrixLess[][] = new double[][]{
            {700.0, 750.0, 800.0, 850.0, 900.0, 1000.0},
            {650.0, 700.0, 750.0, 800.0, 850.0, 800.0}, 
            {430.0, 500.0, 600.0, 700.0, 800.0, 800.0},
            {700.0, 750.0, 760.0, 770.0, 850.0, 1000.0}

    };

    static double differentRegion1[][] = new double[][]{
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0, 1450.0, 1500.0, NaN, NaN},
            {750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0, 1250.0, 1500.0, NaN, NaN},
            {700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0, 1500.0, NaN, NaN},
            {650.0, 700.0, 750.0, 800.0, 850.0, 800.0, 800.0, 1500.0, NaN, NaN},
            {430.0, 500.0, 600.0, 700.0, 800.0, 800.0, 800.0, 1500.0, NaN, NaN},
            {700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0, 1500.0, NaN, NaN}};

    static double differentRegion2[][] = new double[][]{
            {NaN, NaN, 500.0, 550.0, 700.0, 750.0, 800.0, 850.0, 900.0, 1000.0},
            {NaN, NaN, 400.0, 410.0, 650.0, 700.0, 750.0, 800.0, 850.0, 800.0},
            {NaN, NaN, 450.0, 550.0, 430.0, 500.0, 600.0, 700.0, 800.0, 800.0},
            {NaN, NaN, 500.0, 600.0, 700.0, 750.0, 760.0, 770.0, 850.0, 1000.0},
            {NaN, NaN, 600.0, 700.0, 750.0, 800.0, 780.0, 790.0, 1000.0, 1100.0},
            {NaN, NaN, 800.0, 910.0, 980.0, 1001.0, 1150.0, 1200.0, 1250.0, 1300.0},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},};

    static double differentRegion3[][] = new double[][]{
            { 700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0, 1500.0, NaN, NaN},
            { 650.0, 700.0, 750.0, 800.0, 850.0, 800.0, 800.0, 1500.0, NaN, NaN},
            { 430.0, 500.0, 600.0, 700.0, 800.0, 800.0, 800.0, 1500.0, NaN, NaN},
            {700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0, 1500.0, NaN, NaN},
            { 750.0, 800.0, 780.0, 790.0, 1000.0, 1100.0, 1250.0, 1500.0, NaN, NaN},
            { 980.0, 1001.0, 1150.0, 1200.0, 1250.0, 1300.0, 1450.0, 1500.0, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},};
    
    static double differentRegion4[][] = new double[][]{
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN, NaN},
            {NaN, NaN,800.0, 900.0, 1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0},
            {NaN, NaN,600.0, 650.0, 750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0},
            {NaN, NaN,500.0, 550.0, 700.0, 750.0, 800.0, 850.0, 900.0, 1000.0},
            {NaN, NaN,400.0, 410.0, 650.0, 700.0, 750.0, 800.0, 850.0, 800.0},
            {NaN, NaN,450.0, 550.0, 430.0, 500.0, 600.0, 700.0, 800.0, 800.0},
            {NaN, NaN,500.0, 600.0, 700.0, 750.0, 760.0, 770.0, 850.0, 1000.0}};
    
}
