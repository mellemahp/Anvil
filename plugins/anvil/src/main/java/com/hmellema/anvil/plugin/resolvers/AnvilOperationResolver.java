package com.hmellema.anvil.plugin.resolvers;

import com.hmellema.sgf4j.core.plugin.extensionpoints.ResolverExtensionPoint;
import com.hmellema.sgf4j.core.shapegenerator.AbstractShapeGenerator;
import com.hmellema.sgf4j.core.typeconversion.TypeConverterMap;
import com.hmellema.anvil.plugin.resolvers.shapegenerator.AnvilOperationShapeGenerator;
import java.util.EnumSet;
import org.pf4j.Extension;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.Shape;
import software.amazon.smithy.model.shapes.ShapeType;

@Extension
public class AnvilOperationResolver implements ResolverExtensionPoint {
  private static final EnumSet<ShapeType> SUPPORTED_SHAPES = EnumSet.of(ShapeType.OPERATION);

  @Override
  public EnumSet<ShapeType> getSupportedShapeTypes() {
    return SUPPORTED_SHAPES;
  }

  @Override
  public AbstractShapeGenerator process(Shape shape, Model model, TypeConverterMap converterMap) {
    return new AnvilOperationShapeGenerator(shape);
  }
}
