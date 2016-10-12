package org.geotools.mbtiles;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;

public class MBTilesMetadata {
    
    private static Logger LOGGER = Logging.getLogger(MBTilesMetadata.class); 
    
    protected static Pattern patternEnvelope = Pattern.compile(" *(\\-?[0-9\\.]*) *, *(\\-?[0-9\\.]*) *, *(\\-?[0-9\\.]*) *, *(\\-?[0-9\\.]*) *");
    
    public enum t_type {
        OVERLAY("overlay"), 
        BASE_LAYER("baselayer");
        
        private t_type(String identifier) {
            this.identifier = identifier;
        }
        
        public final String identifier;
        
        public static t_type lookUp(final String s) throws IllegalArgumentException {
            return Arrays.stream(t_type.values())
                .filter((t)->t.identifier.equalsIgnoreCase(s))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(String.format("Unknown mbtiles type '%s'",s)));
        }
    } ;
    
    public enum t_format {
        JPEG, 
        PNG, 
        
        /** Not part of the spec but used by some implementations of MBTiles for vector tiles */
        PBF
    } ;
    
    protected String name;
    
    protected String version;
    
    protected String description;
    
    protected t_type type;
    
    protected t_format format;
    
    protected Envelope bounds;
    
    protected String attribution;

    protected int minZoom;

    protected int maxZoom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public t_type getType() {
        return type;
    }

    public void setType(t_type type) {
        this.type = type;
    }

    public t_format getFormat() {
        return format;
    }

    public void setFormat(t_format format) {
        this.format = format;
    }

    public Envelope getBounds() {
        return bounds;
    }

    public void setBounds(Envelope bounds) {
        this.bounds = bounds;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public String getTypeStr() {
        if (type == null) {
            return null;
        } else {
            return type.toString().toLowerCase();
        }
    }
   
    public String getFormatStr() {
        if (format == null) {
            return null;
        } else {
            return format.toString().toLowerCase();
        }
    }
    
    public String getBoundsStr() {
        if (bounds == null) {
            return null;
        } else {
            return bounds.getMinimum(0) + "," + bounds.getMinimum(1) + "," + bounds.getMaximum(0) + "," + bounds.getMaximum(1);
        }
    }

    public void setTypeStr(final String typeStr) {
        if (typeStr == null) {
            setType(null);
        } else {
            if(typeStr.equalsIgnoreCase("BASE_LAYER")) {
                LOGGER.log(Level.WARNING, ()->String.format("MBTiles file has invalid type '%s', using '%s' instead", typeStr, t_type.BASE_LAYER));
                setType(t_type.BASE_LAYER);
            } else {
                setType(t_type.lookUp(typeStr));
            }
        }
    }

    public void setFormatStr(String formatStr) {
        if (formatStr == null) {
            setFormat(null);
        } else {
            setFormat(t_format.valueOf(formatStr.toUpperCase()));
        }
    }

    public void setBoundsStr(String boundsStr) {
        if (boundsStr == null) {
            setBounds(null);
        } else {
            Matcher matcherEnvelope = patternEnvelope.matcher(boundsStr);
            if (!matcherEnvelope.matches()) {
                throw new IllegalArgumentException("Envelope not in correct format: minx,miny,maxx,maxy");
            }
            double minx = Double.parseDouble(matcherEnvelope.group(1));
            double miny = Double.parseDouble(matcherEnvelope.group(2));
            double maxx = Double.parseDouble(matcherEnvelope.group(3));
            double maxy = Double.parseDouble(matcherEnvelope.group(4));
            try {
                setBounds(new ReferencedEnvelope(minx, maxx, miny, maxy, CRS.decode("EPSG:4326", true)));
            } catch (FactoryException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        
    }

    public void setMinZoomStr(String minZoomStr) {
        if (minZoomStr == null) {
            minZoom = 0;
        } else {
            minZoom = Integer.parseInt(minZoomStr);
        }
    }

    public void setMaxZoomStr(String maxZoomStr) {
        if (maxZoomStr == null) {
            maxZoom = 0;
        } else {
            maxZoom = Integer.parseInt(maxZoomStr);
        }
    }

}
