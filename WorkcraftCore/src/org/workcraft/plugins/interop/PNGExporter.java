package org.workcraft.plugins.interop;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.workcraft.dom.Model;
import org.workcraft.dom.visual.VisualModel;
import org.workcraft.exceptions.SerialisationException;
import org.workcraft.interop.Exporter;
import org.workcraft.serialisation.Format;

public class PNGExporter implements Exporter {

    public void export(Model model, OutputStream out) throws IOException, SerialisationException {
        InputStream svg = SVGExportUtils.stream(model);
        Transcoder transcoder = new PNGTranscoder();
        TranscoderInput transcoderInput = new TranscoderInput(svg);
        TranscoderOutput transcoderOutput = new TranscoderOutput(out);
        try {
            transcoder.transcode(transcoderInput, transcoderOutput);
        } catch (TranscoderException e) {
            throw new SerialisationException(e);
        }
    }

    public String getDescription() {
        return ".png (FOP PNG transcoder)";
    }

    public String getExtenstion() {
        return ".png";
    }

    public int getCompatibility(Model model) {
        if (model instanceof VisualModel) {
            return Exporter.GENERAL_COMPATIBILITY;
        } else {
            return Exporter.NOT_COMPATIBLE;
        }
    }

    @Override
    public UUID getTargetFormat() {
        return Format.PNG;
    }
}