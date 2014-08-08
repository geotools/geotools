package org.geotools.ysld.encode;

import org.geotools.styling.Stroke;

public class StrokeEncoder extends Encoder<Stroke> {

    StrokeEncoder(Stroke stroke) {
        super(stroke);
    }

    @Override
    protected void encode(Stroke stroke) {
        put("stroke-color", stroke.getColor());
        put("stroke-width", stroke.getWidth());
        put("stroke-opacity", nullIf(stroke.getOpacity(), 1d));
        put("stroke-linejoin", nullIf(stroke.getLineJoin(), "miter"));
        put("stroke-linecap", nullIf(stroke.getLineCap(), "butt"));
        put("stroke-dasharray", toStringOrNull(stroke.getDashArray()));
        put("stroke-dashoffset", nullIf(stroke.getDashOffset(), 0));

        if (stroke.getGraphicFill() != null) {
            push("stroke-graphic-fill").inline(new GraphicEncoder(stroke.getGraphicFill()));
        }

        if (stroke.getGraphicStroke() != null) {
            push("stroke-graphic-stroke").inline(new GraphicEncoder(stroke.getGraphicStroke()));
        }
    }

    String toStringOrNull(float[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]).append(" ");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
}
