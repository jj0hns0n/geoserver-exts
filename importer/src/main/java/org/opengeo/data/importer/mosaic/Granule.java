package org.opengeo.data.importer.mosaic;

import java.util.Date;

import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.Envelope2D;
import org.opengeo.data.importer.SpatialFile;

/**
 * A tile of a mosaic. 
 */
public class Granule extends SpatialFile {

    Date timestamp;
    Envelope2D envelope;
    GridGeometry2D grid;

    public Granule(SpatialFile file) {
        super(file);
    }

    public Envelope2D getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope2D envelope) {
        this.envelope = envelope;
    }

    public GridGeometry2D getGrid() {
        return grid;
    }

    public void setGrid(GridGeometry2D grid) {
        this.grid = grid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    
}
