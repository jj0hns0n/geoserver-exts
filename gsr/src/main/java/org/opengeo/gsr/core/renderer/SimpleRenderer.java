/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.opengeo.gsr.core.renderer;

import org.opengeo.gsr.core.symbol.Symbol;

/**
 * 
 * @author Juan Marin, OpenGeo
 *
 */
public class SimpleRenderer extends Renderer {

    private Symbol symbol;

    private String label;

    private String description;

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SimpleRenderer(Symbol symbol, String label, String description) {
        super("simple");
        this.symbol = symbol;
        this.label = label;
        this.description = description;
    }
}
