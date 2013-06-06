/* Copyright (c) 2001 - 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.opengeo.gsr.core.label;

/**
 * 
 * @author Juan Marin, OpenGeo
 * 
 */
public enum PointLabelPlacementEnum {

    ABOVE_CENTER("above-center"), ABOVE_RIGHT("above-right"), ABOVE_LEFT("above-left"), BELOW_CENTER(
            "below-center"), BELOW_LEFT("below-left"), BELOW_RIGHT("below-right"), CENTER_CENTER(
            "center-center"), CENTER_LEFT("center-left"), CENTER_RIGHT("center-right");
    private final String placement;

    public String getPlacement() {
        return placement;
    }

    private PointLabelPlacementEnum(String placement) {
        this.placement = placement;
    }

}
