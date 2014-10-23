package org.geotools.ysld.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

/**
 * ZoomContext defined by an initial scale and a ratio between levels.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public class SimpleZoomContext extends ContinuousZoomContext implements ZoomContext {
    
    final int initialLevel;
    final double initialScale;
    final double ratio;
    
    /**
     * Create a zoom context where zoom level 0 has a scale denominator of {@code initial}, and each subsequent level is {@code ratio} times that.
     * @param initilScalel scale denominator at level 0
     * @param ratio ratio between zoom levels
     */
    public SimpleZoomContext(final double initialScale, final double ratio) {
        this(0, initialScale, ratio);
    }
    
    /**
     * Create a zoom context where zoom level {@code initialLevel} has a scale denominator of {@code
     *  initial}, and each subsequent level is {@code ratio} times that.
     * @param initialLevel Level to use as initial
     * @param initialScale scale denominator at level {@code initialLevel}
     * @param ratio ratio between the scale at consecutive zoom levels.  Zoom level z+1 has a scale
     *  ratio times that of z and a scale denominator 1/ratio times that of z. 
     */
    public SimpleZoomContext(final int initialLevel, final double initialScale, final double ratio) {
        super();
        Preconditions.checkArgument(initialScale>0, "initialScale must be greater than 0");
        Preconditions.checkArgument(ratio>1, "ratio must be greater than 1");
        this.initialLevel = initialLevel;
        this.initialScale = initialScale;
        this.ratio = ratio;
    }
    
    @Override
    protected double getScaleDenominator(double level) {
        // We're returning the denominator, so divide by the ratio
        return initialScale / Math.pow(ratio, level-initialLevel);
    }
    
    protected double getMedialScale(int level) {
        return getScaleDenominator(level+0.5d);
    }

    private static final Pattern PATTERN = Pattern.compile("^\\s*simple:\\s*(?<ratio>\\d*(?:\\.\\d*)?)\\s*x\\s*(?<initialScale>\\d*(?:\\.\\d*)?)(?:@\\s*(?<initialLevel>\\d+))?\\s*$", Pattern.CASE_INSENSITIVE);
    public static SimpleZoomContext parse(String str) throws IllegalArgumentException {
        final Matcher m = PATTERN.matcher(str);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
        final double ratio = Double.parseDouble(m.group("ratio"));
        final double initialScale = Double.parseDouble(m.group("initialScale"));
        final int initialLevel;
        if(m.group("initialLevel")!=null) {
            initialLevel = Integer.parseInt(m.group("initialLevel"));
        } else {
            initialLevel = 0;
        }
        
        return new SimpleZoomContext(initialLevel, initialScale, ratio);
    }
}
