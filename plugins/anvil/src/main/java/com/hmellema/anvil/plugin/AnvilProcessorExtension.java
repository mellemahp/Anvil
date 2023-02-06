package com.hmellema.anvil.plugin;

import com.hmellema.anvil.plugin.processors.HttpErrorCodeTraitProcessor;
import com.hmellema.sgf4j.extension.ProcessorExtension;
import com.hmellema.sgf4j.traitprocessing.Processor;
import org.pf4j.Extension;

import java.util.Collection;
import java.util.List;

@Extension
public class AnvilProcessorExtension implements ProcessorExtension {
    private static final String EXTENSION_NAME = "anvil";

    private static final List<Processor> PROCESSOR_LIST = List.of(
            new HttpErrorCodeTraitProcessor()
    );

    @Override
    public String getName() {
        return EXTENSION_NAME;
    }

    @Override
    public Collection<Processor> getProcessors() {
        return PROCESSOR_LIST;
    }
}
