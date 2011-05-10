/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid;

/**
 * This is the lineal equivalent of {@link GridElement}. It is actually just
 * an empty, marker interface.
 * 
 * @author michael
 */
public interface LineElement extends Element {

    int getLevel();

    Object getValue();
}
