package org.geotools.ysld.parse;

import org.geotools.styling.Rule;

import com.google.common.base.Preconditions;

class ScaleRange {
	final double minDenom;
	final double maxDenom;

	public ScaleRange(double minDenom, double maxDenom) {
		Preconditions.checkArgument(minDenom>=0 && minDenom<Double.POSITIVE_INFINITY && !Double.isNaN(minDenom), "minDenom must be finite and non negative");
		Preconditions.checkArgument(maxDenom>0 && !Double.isNaN(minDenom), "maxDenom must be positive");
		Preconditions.checkArgument(minDenom<=maxDenom, "maxDenom must be greater than or equal to minDenom");
		this.minDenom = minDenom;
		this.maxDenom = maxDenom;
	}

	public void applyTo(Rule r) {
		r.setMaxScaleDenominator(maxDenom);
		r.setMinScaleDenominator(minDenom);
	}
}