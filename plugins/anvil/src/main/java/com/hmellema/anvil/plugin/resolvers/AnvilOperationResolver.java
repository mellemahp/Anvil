package com.hmellema.anvil.plugin.resolvers;

import com.hmellema.anvil.plugin.resolvers.shapegenerator.OperationShapeGenMetadata;
import com.hmellema.sgf4j.gendata.ShapeGenMetadata;
import com.hmellema.sgf4j.loader.MetaDataLoader;
import com.hmellema.sgf4j.resolving.Resolver;

import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.model.shapes.Shape;
import software.amazon.smithy.model.shapes.ShapeType;

public class AnvilOperationResolver implements Resolver {
    private static final ShapeType SUPPORTED_TYPE = ShapeType.OPERATION;

    @Override
    public ShapeType getSupportedShapeType() {
        return SUPPORTED_TYPE;
    }

    @Override
    public ShapeGenMetadata resolve(Shape shape, MetaDataLoader metaDataLoader) {
        var operationShape = (OperationShape) shape; 
        var inputShape = metaDataLoader.resolve(operationShape.getInputShape());
        var outputShape = metaDataLoader.resolve(operationShape.getOutputShape());
        return new OperationShapeGenMetadata(shape, inputShape, outputShape);
    }
}
