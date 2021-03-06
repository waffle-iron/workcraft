package org.workcraft.plugins.circuit.interop;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.workcraft.dom.Model;
import org.workcraft.exceptions.ModelValidationException;
import org.workcraft.exceptions.SerialisationException;
import org.workcraft.interop.Exporter;
import org.workcraft.plugins.circuit.serialisation.SdcSerialiser;
import org.workcraft.serialisation.Format;

public class SdcExporter implements Exporter {
    SdcSerialiser serialiser = new SdcSerialiser();

    public void export(Model model, OutputStream out)
            throws IOException, ModelValidationException, SerialisationException {

        serialiser.serialise(model, out, null);
    }

    public String getDescription() {
        return serialiser.getExtension() + " (" + serialiser.getDescription() + ")";
    }

    public String getExtenstion() {
        return serialiser.getExtension();
    }

    public int getCompatibility(Model model) {
        if (serialiser.isApplicableTo(model)) {
            return Exporter.BEST_COMPATIBILITY;
        } else {
            return Exporter.NOT_COMPATIBLE;
        }
    }

    @Override
    public UUID getTargetFormat() {
        return Format.SDC;
    }
}
