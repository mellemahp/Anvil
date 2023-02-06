package com.hmellema.anvil.plugin.resolvers.shapegenerator;

import com.hmellema.anvil.core.operation.AbstractAnvilOperationHandler;

import com.hmellema.sgf4j.gendata.ShapeGenMetadata;
import com.squareup.javapoet.*;
import software.amazon.smithy.model.shapes.Shape;
import software.amazon.smithy.model.shapes.ShapeType;

import javax.lang.model.element.Modifier;
import java.util.*;

public class OperationShapeGenMetadata extends ShapeGenMetadata {
    private static final EnumSet<ShapeType> SUPPORTED_SHAPE_TYPES = EnumSet.of(ShapeType.OPERATION);

    private static final String ABSTRACT_CLASS_NAME_TEMPLATE = "Abstract%sHandler";
    private static final String OPERATION_NAME_FIELD = "OPERATION_NAME";
    private static final ClassName paramClassName = ClassName.get(AbstractAnvilOperationHandler.class);
    private final List<AnnotationSpec> classAnnotations = new ArrayList<>();
    private final List<AnnotationSpec> fieldAnnotations = new ArrayList<>();
    private final List<MethodSpec> fieldAssociatedMethods = new ArrayList<>();

    private final String className;
    private String nameSpace;
    private TypeName typeName;
    private final TypeName requestType;
    private final TypeName responseType;

    public OperationShapeGenMetadata(Shape shape, ShapeGenMetadata inputShape, ShapeGenMetadata outputShape) {
        super(shape, SUPPORTED_SHAPE_TYPES);
        Objects.requireNonNull(shape, "shape cannot be null.");
        Objects.requireNonNull(inputShape, "inputShape cannot be null.");
        Objects.requireNonNull(outputShape, "outputShape cannot be null");

        this.className = shape.getId().getName();
        this.requestType = inputShape.getTypeName();
        this.responseType = outputShape.getTypeName();
        this.typeName = ParameterizedTypeName.get(paramClassName, requestType, responseType);
    }

    @Override
    public List<MethodSpec> getFieldAssociatedMethods() {
        return fieldAssociatedMethods;
    }

    @Override
    public void addFieldMethod(MethodSpec fieldMethod) {
        Objects.requireNonNull(fieldMethod, "fieldMethod cannot be null");
        fieldAssociatedMethods.add(fieldMethod);
    }

    @Override
    public List<AnnotationSpec> getFieldAnnotations() {
        return fieldAnnotations;
    }

    @Override
    public void addFieldAnnotation(AnnotationSpec annotationSpec) {
        Objects.requireNonNull(annotationSpec, "annotationSpec cannot be null.");
        fieldAnnotations.add(annotationSpec);
    }

    @Override
    public TypeName getTypeName() {
        return typeName;
    }

    @Override
    public void setTypeName(TypeName typeName) {
        this.typeName = Objects.requireNonNull(typeName, "typeName cannot be null.");
    }

    @Override
    public String getNameSpace() {
        return nameSpace;
    }

    @Override
    public void setNameSpace(String nameSpace) {
        this.nameSpace = Objects.requireNonNull(nameSpace, "nameSpace cannot be null.");
    }

    @Override
    public Optional<TypeSpec> asClass() {
        var specBuilder = TypeSpec.classBuilder(String.format(ABSTRACT_CLASS_NAME_TEMPLATE, className))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotations(classAnnotations)
                .addField(getOperationNameStaticField())
                .addMethod(getOperationNameGetterMethod())
                .addMethod(getConstructor())
                .superclass(typeName);

        return Optional.of(specBuilder.build());
    }

    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super($T.class)", requestType)
                .build();
    }

    private FieldSpec getOperationNameStaticField() {
        return FieldSpec.builder(String.class, OPERATION_NAME_FIELD)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", className)
                .build();
    }

    private MethodSpec getOperationNameGetterMethod() {
        return MethodSpec.methodBuilder("getOperationName")
                .addStatement("return $L", OPERATION_NAME_FIELD)
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .build();
    }
}
