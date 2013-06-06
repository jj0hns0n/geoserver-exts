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
public class ClassBreakInfo {

    private double classMaxValue;

    private String label;

    private String description;

    private Symbol symbol;

    public double getClassMaxValue() {
        return classMaxValue;
    }

    public void setClassMaxValue(double classMaxValue) {
        this.classMaxValue = classMaxValue;
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

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public ClassBreakInfo(double classMaxValue, String label, String description, Symbol symbol) {
        super();
        this.classMaxValue = classMaxValue;
        this.label = label;
        this.description = description;
        this.symbol = symbol;
    }

}
