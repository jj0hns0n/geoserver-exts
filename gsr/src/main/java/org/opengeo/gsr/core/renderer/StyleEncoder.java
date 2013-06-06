package org.opengeo.gsr.core.renderer;

import java.awt.Color;
import java.io.IOException;
import java.util.Set;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.opengeo.gsr.core.geometry.GeometryTypeEnum;
import org.opengeo.gsr.core.symbol.MarkerSymbol;
import org.opengeo.gsr.core.symbol.Outline;
import org.opengeo.gsr.core.symbol.SimpleFillSymbol;
import org.opengeo.gsr.core.symbol.SimpleFillSymbolEnum;
import org.opengeo.gsr.core.symbol.SimpleLineSymbol;
import org.opengeo.gsr.core.symbol.SimpleLineSymbolEnum;
import org.opengeo.gsr.core.symbol.SimpleMarkerSymbol;
import org.opengeo.gsr.core.symbol.SimpleMarkerSymbolEnum;
import org.opengeo.gsr.core.symbol.Symbol;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Fill;
import org.opengis.style.GraphicalSymbol;

import net.sf.json.util.JSONBuilder;

public class StyleEncoder {
//    public static void defaultFillStyle(JSONBuilder json) {
//        json.object()
//          .key("type").value("simple")
//          .key("symbol").object()
//            .key("type").value("esriSFS")
//            .key("style").value("esriSFSSolid")
//            .key("color");
//            color(json, 255, 0, 0, 255);
//            json.key("outline").object()
//              .key("type").value("esriSLS")
//              .key("style").value("esriSLSSolid")
//              .key("color");
//              color(json, 0,  0, 0, 255);
//              json.key("width").value(1)
//            .endObject()
//          .endObject()
//          .key("label").value("")
//          .key("description").value("")
//        .endObject();
//    }
//    
//    public static void defaultRasterStyle(JSONBuilder json) {
//        json.object()
//          .key("type").value("simple")
//          .key("symbol").object()
//            .key("type").value("esriSFS")
//            .key("style").value("esriSFSSolid")
//            .key("color").value(null)
//            .key("outline").object()
//              .key("type").value("esriSLS")
//              .key("style").value("esriSLSSolid")
//              .key("color").value(null)
//              .key("width").value(1)
//            .endObject()
//          .endObject()
//          .key("label").value("")
//          .key("description").value("")
//        .endObject();
//    }
//
//    public static void defaultLineStyle(JSONBuilder json) {
//        json.object()
//          .key("type").value("simple")
//          .key("symbol");
//          encodeLineStyle(json, new SimpleLineSymbol(SimpleLineSymbolEnum.SOLID, components(Color.RED, 1d), 1d));
//          json.key("label").value("")
//          .key("description").value("");
//        json.endObject();
//    }
//
//    public static void defaultMarkStyle(JSONBuilder json) {
//        json.object()
//          .key("type").value("simple")
//          .key("symbol").object()
//            .key("type").value("esriSMS")
//            .key("style").value("esriSMSCircle")
//            .key("color");
//            color(json, 255, 0, 0, 255);
//            json.key("outline").object()
//              .key("type").value("esriSLS")
//              .key("style").value("esriSLSSolid")
//              .key("color");
//              color(json, 0, 0, 0, 255);
//              json.key("width").value("1");
//            json.endObject()
//          .endObject()
//          .key("label").value("")
//          .key("description").value("")
//        .endObject();
//    }
//    
//    private static void color(JSONBuilder json, int r, int g, int b, int a) {
//        json.array().value(r).value(g).value(b).value(a).endArray();
//    }
    
    public static Renderer styleToRenderer(Style style) {
        final Symbolizer symbolizer = getSingleSymbolizer(style);
        if (symbolizer != null) {
            final Symbol symbol = symbolizerToSymbol(symbolizer);
            if (symbol != null) {
                return new SimpleRenderer(symbol, "", "");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    private static Renderer defaultPolyRenderer() {
        SimpleLineSymbol outline = new SimpleLineSymbol(SimpleLineSymbolEnum.SOLID, new int[] { 0, 0, 0, 255 }, 1);
        Symbol symbol = new SimpleFillSymbol(SimpleFillSymbolEnum.SOLID, new int[] { 255, 0, 0, 255 }, outline);
        return new SimpleRenderer(symbol, "Polygon", "Default polygon renderer");
    }
    
    private static Renderer defaultRasterRenderer() {
        SimpleLineSymbol outline = new SimpleLineSymbol(SimpleLineSymbolEnum.SOLID, null, 1);
        Symbol symbol = new SimpleFillSymbol(SimpleFillSymbolEnum.SOLID, null, outline);
        return new SimpleRenderer(symbol, "Raster", "Default raster renderer");
    }
    
    private static Renderer defaultLineRenderer() {
        SimpleLineSymbol outline = new SimpleLineSymbol(SimpleLineSymbolEnum.SOLID, new int[] { 0, 0, 0, 255 }, 1);
        return new SimpleRenderer(outline, "Line", "Default line renderer");
    }
    
    private static Renderer defaultMarkRenderer() {
        Outline outline = new Outline(new int[] { 0, 0, 0, 255 }, 1);
        SimpleMarkerSymbol marker = new SimpleMarkerSymbol(SimpleMarkerSymbolEnum.SQUARE, new int[] { 255, 0, 0, 255 }, 24, 0, 0, 0, outline);
        return new SimpleRenderer(marker, "Marker", "Default marker renderer");
    }
    
    public static Renderer effectiveRenderer(LayerInfo layer) throws IOException {
        Renderer renderer = null;
        
        StyleInfo styleInfo = layer.getDefaultStyle();
        if (styleInfo != null) {
            Style style = styleInfo.getStyle();
            if (style != null) {
                renderer = styleToRenderer(style);
            }
        }
        
        if (renderer == null) {
            GeometryTypeEnum gtype = GeometryTypeEnum.forResourceDefaultGeometry(layer.getResource());
            switch (gtype) {
            case ENVELOPE:
            case POLYGON:
                if (layer.getResource() instanceof CoverageInfo) {
                    renderer = defaultRasterRenderer();
                } else {
                    renderer = defaultPolyRenderer(); // TODO: Generate default polygon style
                }
                break;
            case MULTIPOINT:
            case POINT:
                renderer = defaultMarkRenderer(); // TODO: Generate default point style
                break;
            case POLYLINE:
                renderer = defaultLineRenderer(); // TODO: Generate default line style;
                break;
            default:
                renderer = null;
            }
        }
        
        return renderer;
    }
    
    private static Symbol symbolizerToSymbol(Symbolizer sym) {
        if (sym instanceof PointSymbolizer) {
            return pointSymbolizerToMarkSymbol((PointSymbolizer)sym);
        } else if (sym instanceof LineSymbolizer) {
            return lineSymbolizerToLineSymbol((LineSymbolizer)sym);
        } else if (sym instanceof PolygonSymbolizer) {
            return polygonSymbolizerToFillSymbol((PolygonSymbolizer)sym);
        } else return null; // TODO: Should we throw here?
    }
    
    private static Symbol polygonSymbolizerToFillSymbol(PolygonSymbolizer sym) {
        final Fill fill = sym.getFill();
        final Stroke stroke = sym.getStroke();
        final Color color;
        final double opacity;
        final SimpleLineSymbol outline;
        if (fill != null) {
            color = evaluateWithDefault(fill.getColor(), Color.GRAY);
            opacity = evaluateWithDefault(fill.getOpacity(), 1d);
        } else {
            color = Color.GRAY;
            opacity = 1d;
        }
        if (stroke != null) {
            Color strokeColor = evaluateWithDefault(stroke.getColor(), Color.BLACK);
            double strokeOpacity = evaluateWithDefault(stroke.getOpacity(), 1d);
            double strokeWidth = evaluateWithDefault(stroke.getWidth(), 1d);
            outline = new SimpleLineSymbol(SimpleLineSymbolEnum.SOLID, components(strokeColor, strokeOpacity), strokeWidth);
        } else {
            outline = new SimpleLineSymbol(
                SimpleLineSymbolEnum.SOLID,
                components(Color.BLACK, 1),
                1);
        }
        
        return new SimpleFillSymbol(SimpleFillSymbolEnum.SOLID, components(color, opacity), outline);
    }

    private static MarkerSymbol pointSymbolizerToMarkSymbol(PointSymbolizer sym) {
        if (sym.getGraphic() == null) return null;
        if (sym.getGraphic().graphicalSymbols().size() != 1) return null; // REVISIT: should we throw instead?
        GraphicalSymbol symbol = sym.getGraphic().graphicalSymbols().get(0);
        if (symbol instanceof Mark) {
            Mark mark = (Mark)symbol;
            String markName = evaluateWithDefault(mark.getWellKnownName(), "circle");
            final Color color;
            final double opacity;
            final double size;
            final double angle = evaluateWithDefault(sym.getGraphic().getRotation(), 0d);
            final double xoffset;
            final double yoffset;
            Fill fill = mark.getFill();
            if (fill != null) {
                color = evaluateWithDefault(fill.getColor(), Color.GRAY);
                opacity = evaluateWithDefault(fill.getOpacity(), 1d);
                size = evaluateWithDefault(sym.getGraphic().getSize(), 16);
            } else {
                color = Color.GRAY;
                opacity = 1d;
                size = 16d;
            }
            Displacement displacement = sym.getGraphic().getDisplacement();
            if (displacement != null) {
                xoffset = evaluateWithDefault(displacement.getDisplacementX(), 0d);
                yoffset = evaluateWithDefault(displacement.getDisplacementY(), 0d);
            } else {
                xoffset = 0d;
                yoffset = 0d;
            }
            
            final Outline outline;
            final Stroke stroke = mark.getStroke();
            if (stroke != null) {
                Color strokeColor = evaluateWithDefault(stroke.getColor(), Color.BLACK);
                double strokeOpacity = evaluateWithDefault(stroke.getOpacity(), 1d);
                double strokeWidth = evaluateWithDefault(stroke.getWidth(), 1d);
                outline = new Outline(components(strokeColor, strokeOpacity), (int)Math.round(strokeWidth));
            } else {
                outline = new Outline(components(Color.BLACK, 1d), 1);
            }
            return new SimpleMarkerSymbol(
                equivalentSMS(markName),
                components(color, opacity),
                size,
                angle,
                xoffset,
                yoffset,
                outline);
        } else if (symbol instanceof ExternalGraphic) {
            // TODO implement this.
            // ExternalGraphic exGraphic = (ExternalGraphic) symbol;
        }
        return null;
    }
    
    public static SimpleLineSymbol lineSymbolizerToLineSymbol(LineSymbolizer sym) {
        Stroke stroke = sym.getStroke();
        return strokeToLineSymbol(stroke);
    }

    private static SimpleLineSymbol strokeToLineSymbol(Stroke stroke) {
        final Color color;
        final double opacity; 
        final double width; 
        float[] dashArray;
        final SimpleLineSymbolEnum lineStyle;
        if (stroke != null) {
            opacity = evaluateWithDefault(stroke.getOpacity(), 1d);
            color = evaluateWithDefault(stroke.getColor(), Color.BLACK);
            width = evaluateWithDefault(stroke.getWidth(), 1d);
            dashArray = stroke.getDashArray();
        } else {
            color = Color.BLACK;
            opacity = 1d;
            width = 1d;
            dashArray = null;
        }
        if (dashArray == null || dashArray.length == 1) {
            lineStyle = SimpleLineSymbolEnum.SOLID;
        } else {
            Set<Float> uniqueValues = new java.util.HashSet<Float>();
            for (float f : dashArray) {
                uniqueValues.add(f);
            }
            if (uniqueValues.size() == 1) {
                lineStyle = SimpleLineSymbolEnum.DASH;
            } else {
                lineStyle = SimpleLineSymbolEnum.DASH_DOT;
            }
        }
        return new SimpleLineSymbol(lineStyle, components(color, opacity), width);
    }


    public static void encodeStyle(JSONBuilder json, Style style) {
        Symbolizer sym = getSingleSymbolizer(style);
        if (sym instanceof PointSymbolizer) {
            encodePointSymbolizer(json, (PointSymbolizer) sym);
        } else if (sym instanceof LineSymbolizer) {
            encodeLineSymbolizer(json, (LineSymbolizer) sym);
        } else if (sym instanceof PolygonSymbolizer) {
            encodePolygonSymbolizer(json, (PolygonSymbolizer) sym);
        }
    }
    
    public static void encodeSymbol(JSONBuilder json, Symbol symbol) {
        if (symbol instanceof SimpleMarkerSymbol) {
            encodeMarkerSymbol(json, (SimpleMarkerSymbol) symbol);
        } else if (symbol instanceof SimpleFillSymbol) {
            encodeFillSymbol(json, (SimpleFillSymbol) symbol);
        } else if (symbol instanceof SimpleLineSymbol) {
            encodeLineStyle(json, (SimpleLineSymbol) symbol);
        }
    }
    
    private static void encodePolygonSymbolizer(JSONBuilder json, PolygonSymbolizer sym) {
        final Fill fill = sym.getFill();
        final Stroke stroke = sym.getStroke();
        final Color color;
        final double opacity;
        final SimpleLineSymbol outline;
        if (fill != null) {
            color = evaluateWithDefault(fill.getColor(), Color.GRAY);
            opacity = evaluateWithDefault(fill.getOpacity(), 1d);
        } else {
            color = Color.GRAY;
            opacity = 1d;
        }
        if (stroke != null) {
            Color strokeColor = evaluateWithDefault(stroke.getColor(), Color.BLACK);
            double strokeOpacity = evaluateWithDefault(stroke.getOpacity(), 1d);
            double strokeWidth = evaluateWithDefault(stroke.getWidth(), 1d);
            outline = new SimpleLineSymbol(SimpleLineSymbolEnum.SOLID, components(strokeColor, strokeOpacity), strokeWidth);
        } else {
            outline = new SimpleLineSymbol(
                SimpleLineSymbolEnum.SOLID,
                components(Color.BLACK, 1),
                1);
        }
        
        encodeFillSymbol(json, 
                new SimpleFillSymbol(SimpleFillSymbolEnum.SOLID, components(color, opacity), outline));
    }

    private static void encodeFillSymbol(JSONBuilder json, SimpleFillSymbol sym) {
        json.object()
          .key("type").value("esriSFS")
          .key("style").value(sym.getStyle().getStyle())
          .key("color");
          writeInts(json, sym.getColor());
          json.key("outline");
          encodeLineStyle(json, sym.getOutline());
        json.endObject();
    }

    private static void encodeLineSymbolizer(JSONBuilder json, LineSymbolizer sym) {
        SimpleLineSymbol symbol = lineSymbolizerToLineSymbol(sym);
        encodeLineStyle(json, symbol);
    }
    
    private static int[] components(Color color, double opacity) {
        return new int[] { 
            color.getRed(), color.getGreen(), color.getBlue(), (int) Math.round(opacity * 255)
        };
    }

    private static void encodePointSymbolizer(JSONBuilder json, PointSymbolizer sym) {
        MarkerSymbol markSymbol = pointSymbolizerToMarkSymbol(sym);
        if (markSymbol instanceof SimpleMarkerSymbol) {
            encodeMarkerSymbol(json, (SimpleMarkerSymbol)markSymbol);
        }
    }
    
    private static void encodeMarkerSymbol(JSONBuilder json, SimpleMarkerSymbol sms) {
      json.object()
        .key("type").value("esriSMS")
        .key("style").value(sms.getStyle().getStyle())
        .key("color");
        writeInts(json, sms.getColor());
        json.key("outline").object()
          .key("type").value("SLS")
          .key("style").value("SLSSolid");
          json.key("color");
          writeInts(json, sms.getOutline().getColor());
          json.key("width").value(sms.getOutline().getWidth())
        .endObject();
        json.key("angle").value(sms.getAngle());
        json.key("size").value(sms.getSize());
        json.key("xoffset").value(sms.getXoffset());
        json.key("yoffset").value(sms.getYoffset());
      json.endObject();
    }

    private static void encodeLineStyle(JSONBuilder json, SimpleLineSymbol symbol) {
        json.object()
          .key("type").value("esriSLS")
          .key("style").value(symbol.getStyle().getStyle())
          .key("color");
          writeInts(json, symbol.getColor());
          json.key("width").value(symbol.getWidth())
        .endObject();
    }

    private static void writeInts(JSONBuilder json, int[] color) {
        json.array();
        for (int c : color) json.value(c);
        json.endArray();
    }

    @SuppressWarnings("unchecked")
    private static <T> T evaluateWithDefault(Expression exp, T def) {
        if (exp == null || def == null) return def;
        try {
            return (T)exp.evaluate(null, def.getClass());
        } catch (IllegalArgumentException e) {
            return def;
        } catch (ClassCastException e) {
            return def;
        }
    }

    private static Symbolizer getSingleSymbolizer(Style style) {
        if (style.featureTypeStyles() == null) return null;
        if (style.featureTypeStyles().size() != 1) return null;
        FeatureTypeStyle ftStyle = style.featureTypeStyles().get(0);
        if (ftStyle.rules().size() != 1) return null;
        Rule rule = ftStyle.rules().get(0);
        if (rule.getFilter() != null) return null;
        if (rule.getMinScaleDenominator() > 0) return null;
        if (rule.getMaxScaleDenominator() < Double.MAX_VALUE) return null;
        if (rule.symbolizers().size() != 1) return null;
        return rule.symbolizers().get(0);
    }
    
    private static SimpleMarkerSymbolEnum equivalentSMS(String markName) {
        if ("circle".equals(markName)) {
            return SimpleMarkerSymbolEnum.CIRCLE;
        } else if ("x".equals(markName)) {
            return SimpleMarkerSymbolEnum.X;
        } else if ("cross".equals(markName)) {
            return SimpleMarkerSymbolEnum.CROSS;
        } else if ("square".equals(markName)) {
            return SimpleMarkerSymbolEnum.SQUARE;
//          SLD does not define a diamond mark (you can always just rotate the square.)
//        } else if ("diamond".equals(markName)) {
//            return SimpleMarkerSymbolEnum.DIAMOND
        } else {
            return SimpleMarkerSymbolEnum.CIRCLE;
        }
    }

    public static void encodeRenderer(JSONBuilder json, Renderer renderer) {
        if (renderer == null) {
            json.value(null);
        } else if (renderer instanceof SimpleRenderer) {
            encodeSimpleRenderer(json, (SimpleRenderer) renderer);
        }
    }
    
    private static void encodeSimpleRenderer(JSONBuilder json, SimpleRenderer renderer) {
        json.object()
          .key("type").value("simple")
          .key("symbol");
          encodeSymbol(json, renderer.getSymbol());
          json.key("label").value(renderer.getLabel())
          .key("description").value(renderer.getDescription())
        .endObject();
    }
}
