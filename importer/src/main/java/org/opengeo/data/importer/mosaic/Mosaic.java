package org.opengeo.data.importer.mosaic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.util.logging.Logging;
import org.opengeo.data.importer.DataFormat;
import org.opengeo.data.importer.Directory;
import org.opengeo.data.importer.FileData;
import org.opengeo.data.importer.GridFormat;
import org.opengeo.data.importer.RasterFormat;
import org.opengeo.data.importer.SpatialFile;
import org.opengeo.data.importer.job.ProgressMonitor;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Mosaic extends Directory {

    private static final Logger LOGGER = Logging.getLogger(Mosaic.class);

    TimeMode timeMode;
    TimeHandler timeHandler;
    
    public Mosaic(File file) {
        super(file, false);
        setTimeMode(TimeMode.NONE);
    }

    public TimeMode getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(TimeMode timeMode) {
        this.timeMode = timeMode;
        this.timeHandler = timeMode.createHandler();
    }

    public TimeHandler getTimeHandler() {
        return timeHandler;
    }

    @Override
    public void prepare(ProgressMonitor m) throws IOException {
        super.prepare(m);

        //strip away the shapefile index, properties file, and sample_image file
        files.removeAll(Collections2.filter(files, new Predicate<FileData>() {
            @Override
            public boolean apply(FileData input) {
                File f = input.getFile();
                String basename = FilenameUtils.getBaseName(f.getName());

                //is this file part a shapefile or properties file?
                if (new File(f.getParentFile(), basename+".shp").exists() || 
                    new File(f.getParentFile(), basename+".properties").exists()) {
                    return true;
                }

                if ("sample_image".equals(basename)) {
                    return true;
                }

                return false;
            }
        }));

        DataFormat format = format();
        if (format == null) {
            throw new IllegalArgumentException("Unable to determine format for mosaic files");
        }

        if (!(format instanceof RasterFormat)) {
            throw new IllegalArgumentException("Mosaic directory must contain only raster files");
        }

        setFormat(new MosaicFormat());
    }

    @Override
    protected SpatialFile newSpatialFile(File f, DataFormat format) {
        if (format instanceof GridFormat) {
            Granule g = new Granule(super.newSpatialFile(f, format));

            //process the granule
            try {
                AbstractGridCoverage2DReader r = ((GridFormat)format).gridReader(g);
                try {
                    //get the envelope
                    GridCoverage2D cov = r.read(null);

                    g.setEnvelope(cov.getEnvelope2D());
                    g.setGrid(cov.getGridGeometry());

                    cov.dispose(false);

                    //compute time stamp
                    g.setTimestamp(timeHandler.computeTimestamp(g));

                    return g;
                }
                finally {
                    if (r != null) {
                        r.dispose();
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Could not read file " + f + ", unable to get coverage info");
            }
        }
        return super.newSpatialFile(f, format);
    }

    @SuppressWarnings("unchecked")
    public Collection<Granule> granules() {
        return (Collection) Collections2.filter(files, new Predicate<FileData>() {
            @Override
            public boolean apply(FileData input) {
                return input instanceof Granule;
            }
        });
    }

}
