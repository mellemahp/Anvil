package com.hmellema.anvil.plugin.processors;

import com.hmellema.anvil.core.annotations.HttpErrorCode;

import com.hmellema.sgf4j.gendata.ShapeGenMetadata;
import com.hmellema.sgf4j.loader.MetaDataLoader;
import com.hmellema.sgf4j.traitprocessing.Processor;
import com.squareup.javapoet.AnnotationSpec;
import software.amazon.smithy.model.shapes.ShapeType;
import software.amazon.smithy.model.traits.HttpErrorTrait;

import java.util.EnumSet;
import java.util.Set;

public final class HttpErrorCodeTraitProcessor implements Processor {
    private static final EnumSet<ShapeType> SUPPORTED_SHAPES = EnumSet.of(ShapeType.STRUCTURE);
    private static final String SUPPORTED_TRAIT_NAME = "smithy.api#httpError";
    @Override
    public EnumSet<ShapeType> getSupportedShapeTypes() {
        return SUPPORTED_SHAPES;
    }

    @Override
    public Set<String> getSupportedTraitNames() {
        return Set.of(SUPPORTED_TRAIT_NAME);
    }

    @Override
    public void process(ShapeGenMetadata shapeGenMetadata, MetaDataLoader metaDataLoader) {
        var traitData = shapeGenMetadata.getShape().getTrait(HttpErrorTrait.class)
                .orElseThrow(() -> new IllegalStateException("attempted to run processor on shape without supported trait."));
        shapeGenMetadata.addClassAnnotation(
                AnnotationSpec.builder(HttpErrorCode.class)
                        .addMember("code", "$L", traitData.getCode())
                        .build());
    }
}
