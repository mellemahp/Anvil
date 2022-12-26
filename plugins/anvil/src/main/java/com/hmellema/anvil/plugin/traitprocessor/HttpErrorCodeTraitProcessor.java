package com.hmellema.anvil.plugin.traitprocessor;

import com.hmellema.sgf4j.core.mapping.ShapeGeneratorMap;
import com.hmellema.sgf4j.core.plugin.extensionpoints.TraitProcessorExtensionPoint;
import com.hmellema.sgf4j.core.shapegenerator.AbstractShapeGenerator;
import com.hmellema.anvil.core.annotations.HttpErrorCode;
import com.squareup.javapoet.AnnotationSpec;
import java.util.EnumSet;
import org.pf4j.Extension;
import software.amazon.smithy.model.shapes.ShapeType;
import software.amazon.smithy.model.traits.HttpErrorTrait;

@Extension
public final class HttpErrorCodeTraitProcessor implements TraitProcessorExtensionPoint {
  private static final EnumSet<ShapeType> SUPPORTED_SHAPES = EnumSet.of(ShapeType.STRUCTURE);

  @Override
  public EnumSet<ShapeType> getSupportedShapeTypes() {
    return SUPPORTED_SHAPES;
  }

  @Override
  public void process(AbstractShapeGenerator shapeData, ShapeGeneratorMap shapeGeneratorMap) {
    var traitData = shapeData.getShape().getTrait(HttpErrorTrait.class)
        .orElseThrow(() -> new IllegalStateException("attempted to run processor on shape without supported trait."));
    shapeData.addClassAnnotation(
        AnnotationSpec.builder(HttpErrorCode.class)
            .addMember("code", "$L", traitData.getCode())
            .build());
  }

  @Override
  public String getTrait() {
    return "smithy.api#httpError";
  }
}
