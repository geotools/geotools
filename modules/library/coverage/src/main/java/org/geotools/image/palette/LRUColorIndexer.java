/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

import java.awt.image.IndexColorModel;

/**
 * A color indexer used when all we have is the target palette. Uses a LRU map to cache only
 * the most recenlty used colors (the original image can often have too many to practically
 * keep in memory under concurrent load)
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class LRUColorIndexer implements ColorIndexer {
    IndexColorModel icm;
    ColorIndexer delegate;
    ColorMap cm;
    LRUColors lru;
    int maxSize;

    public LRUColorIndexer(IndexColorModel icm, int maxSize) {
        this.icm = icm;
        this.delegate = new SimpleColorIndexer(icm);
        this.cm = new ColorMap(maxSize);
        this.lru = new LRUColors();
        this.maxSize = maxSize;
    }

    @Override
    public IndexColorModel toIndexColorModel() {
        return icm;
    }

    @Override
    public int getClosestIndex(int r, int g, int b, int a) {
        int idx = cm.get(r, g, b, a);
        if(idx == -1) {
            idx = delegate.getClosestIndex(r, g, b, a);
            cm.put(r, g, b, a, idx);
            if(cm.size() > maxSize) {
                ColorEntry ce = lru.removeLast();
                int red = ColorUtils.red(ce.color);
                int green = ColorUtils.green(ce.color);
                int blue = ColorUtils.blue(ce.color);
                int alpha = ColorUtils.alpha(ce.color);
                cm.remove(red, green, blue, alpha);
                ce.color = ColorUtils.color(r, g, b, a);
                lru.add(ce);
            } else {
                int color = ColorUtils.color(r, g, b, a);
                lru.add(new ColorEntry(color, null, null));
            }
        } 
        return idx;
    }

    static final class ColorEntry {
        int color;
        ColorEntry previous;
        ColorEntry next;
        
        public ColorEntry(int color, ColorEntry previous, ColorEntry next) {
            this.color = color;
            this.previous = previous;
            this.next = next;
        }
    }
    
    static final class LRUColors {
        ColorEntry first;
        ColorEntry last;
        
        ColorEntry removeLast() {
            if(last == null) {
                return null;
            }
            // remove the last one
            ColorEntry result = last;
            last = result.previous;
            // if it was the only one, clean up the first too
            if(last == null) {
                first = null;
            }
            return result;
        }
        
        void touch(int color) {
            // easy case, no moving needed
            if(last == null || last == first) {
                return;
            }
            
            ColorEntry result = null;
            for(ColorEntry entry = first; entry != null; entry = entry.next) {
                if(entry.color == color) {
                    result = entry;
                    break;
                }
            }
            
            // are we moving the last one to first?
            if(result == last) {
                last = result.previous;
                result.previous.next = null;
                result.previous = null;
                result.next = first;
                first = result;
            } else if(result != first) {
                result.previous.next = result.next;
                result.previous = null;
                result.next = first;
                first = result;
            }
        }
        
        void add(ColorEntry ce) {
            if(first == null) {
                ce.next = null;
                ce.previous = null;
                first = last = ce;
            } else {
                ce.next = first;
                ce.next.previous = ce;
                ce.previous = null;
                first = ce;
            }
        }
    }
}
