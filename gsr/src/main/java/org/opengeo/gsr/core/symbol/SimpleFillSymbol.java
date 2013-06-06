/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.opengeo.gsr.core.symbol;

/**
 * 
 * @author Juan Marin, OpenGeo
 *
 */
public class SimpleFillSymbol extends Symbol {

    private SimpleFillSymbolEnum style;

    private int[] color;

    private SimpleLineSymbol outline;

    public SimpleFillSymbolEnum getStyle() {
        return style;
    }

    public void setStyle(SimpleFillSymbolEnum style) {
        this.style = style;
    }

    public int[] getColor() {
        return color;
    }

    public void setColor(int[] color) {
        this.color = color;
    }

    public SimpleLineSymbol getOutline() {
        return outline;
    }

    public void setOutline(SimpleLineSymbol outline) {
        this.outline = outline;
    }

    public SimpleFillSymbol(SimpleFillSymbolEnum style, int[] color, SimpleLineSymbol outline) {
        super("SFS");
        this.style = style;
        this.color = color;
        this.outline = outline;
    }

}
