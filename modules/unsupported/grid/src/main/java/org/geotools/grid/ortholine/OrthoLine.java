/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid.ortholine;

import org.geotools.grid.LineElement;

/**
 *
 * @author michael
 */
public interface OrthoLine extends LineElement {

    LineOrientation getOrientation();
    
}
