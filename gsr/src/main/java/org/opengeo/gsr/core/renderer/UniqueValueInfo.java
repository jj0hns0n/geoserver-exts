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
public class UniqueValueInfo {

    private String value;
    
    private String label;
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public UniqueValueInfo(String value, String label, String description, Symbol symbol) {
        super();
        this.value = value;
        this.label = label;
        this.description = description;
        this.symbol = symbol;
    }

    private String description;
    
    private Symbol symbol;
    
}
