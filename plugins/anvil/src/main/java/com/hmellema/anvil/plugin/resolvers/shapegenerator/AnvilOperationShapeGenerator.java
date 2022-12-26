package com.hmellema.anvil.plugin.resolvers.shapegenerator;

import com.hmellema.sgf4j.core.mapping.ShapeGeneratorMap;
import com.hmellema.sgf4j.core.shapegenerator.AbstractShapeGenerator;
import com.hmellema.sgf4j.core.shapegenerator.methodgenerators.ClassAssociatedMethodSpecGenerator;
import com.hmellema.sgf4j.core.shapegenerator.methodgenerators.FieldAssociatedMethodSpecGenerator;
import com.hmellema.sgf4j.core.shapegenerator.providers.TypeProvider;
import com.hmellema.anvil.core.operation.AbstractAnvilOperationHandler;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.lang.model.element.Modifier;
import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.model.shapes.Shape;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.shapes.ShapeType;

public class AnvilOperationShapeGenerator extends AbstractShapeGenerator {
  private static final String ABSTRACT_CLASS_NAME_TEMPLATE = "Abstract%sHandler";
  private static final String OPERATION_NAME_FIELD = "OPERATION_NAME";
  private String className;
  private TypeName parentClassType;
  private final List<AnnotationSpec> classAnnotations = new ArrayList<>();
  private final List<AnnotationSpec> fieldAnnotations = new ArrayList<>();
  private final List<ClassAssociatedMethodSpecGenerator> associatedClassMethodSpecGenerators = new ArrayList<>();
  private final List<FieldAssociatedMethodSpecGenerator> associatedFieldMethodGenerators = new ArrayList<>();
  private static final EnumSet<ShapeType> SUPPORTED_SHAPES = EnumSet.of(ShapeType.OPERATION);

  public AnvilOperationShapeGenerator(Shape shape) {
    super(shape, SUPPORTED_SHAPES);
    this.className = shape.getId().getName();
  }

  @Override
  public Optional<ShapeId> getTargetId() {
    return Optional.empty();
  }

  @Override
  public Optional<TypeSpec> asClass(ShapeGeneratorMap shapeGeneratorMap) {
    var specBuilder = TypeSpec.classBuilder(
          String.format(ABSTRACT_CLASS_NAME_TEMPLATE, className)
        )
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

    for (var annotation : classAnnotations) {
      specBuilder.addAnnotation(annotation);
    }

    // TODO: this should just be added outside of this method
    // Add the static field data to define the
    specBuilder.addField(FieldSpec.builder(String.class, OPERATION_NAME_FIELD)
        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
        .initializer("$S", className)
        .build()
    );

    // FIX: this is clunky as hell.
    var operationShape = (OperationShape) this.getShape();
    var requestType = shapeGeneratorMap.get(operationShape.getInputShape())
        .map(TypeProvider.class::cast)
        .map(TypeProvider::getTypeName)
        .orElseThrow(() -> new IllegalStateException("Input shape " + operationShape.getInputShape() + " not found for operation " + this.getShape()));
    var responseType = shapeGeneratorMap.get(operationShape.getOutputShape())
        .map(TypeProvider.class::cast)
        .map(TypeProvider::getTypeName)
        .orElseThrow(() -> new IllegalStateException("Output shape " + operationShape.getInputShape() + " not found for operation " + this.getShape()));

    var paramClassName = ClassName.get(AbstractAnvilOperationHandler.class);
    var typeName = ParameterizedTypeName.get(paramClassName, requestType, responseType);
    specBuilder.superclass(typeName);

    specBuilder.addMethod(MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addStatement("super($T.class)", requestType)
        .build());

    specBuilder.addMethod(
        MethodSpec.methodBuilder("getOperationName")
            .addStatement("return $L", OPERATION_NAME_FIELD)
            .returns(String.class)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .build());
    // END TODO

    for (var associatedMethod: this.getClassAssociatedMethods()) {
      specBuilder.addMethod(associatedMethod);
    }

    return Optional.of(specBuilder.build());
  }

  @Override
  public List<ClassAssociatedMethodSpecGenerator> getClassAssociatedMethodGenerator() {
    return associatedClassMethodSpecGenerators;
  }

  @Override
  public void addClassAssociatedMethodGenerator(ClassAssociatedMethodSpecGenerator methodSpec) {
    Objects.requireNonNull(methodSpec, "methodSpec cannot be null");
    associatedClassMethodSpecGenerators.add(methodSpec);
  }

  @Override
  public void addAssociatedMethodGenerator(FieldAssociatedMethodSpecGenerator specGenerator) {
    Objects.requireNonNull(specGenerator, "specGenerator cannot be null");
    associatedFieldMethodGenerators.add(specGenerator);
  }

  @Override
  public List<AnnotationSpec> getClassAnnotations() {
    return this.classAnnotations;
  }

  @Override
  public void addClassAnnotation(AnnotationSpec annotationSpec) {
    Objects.requireNonNull(annotationSpec, "annotationSpec cannot be null");
    classAnnotations.add(annotationSpec);
  }

  @Override
  public Optional<TypeName> getParentClassType() {
    return Optional.of(parentClassType);
  }

  @Override
  public void setParentClassType(TypeName parentClassType) {
    Objects.requireNonNull(parentClassType, "parentClassType cannot be null.");
    this.parentClassType = parentClassType;
  }

  @Override
  public List<TypeSpec> getNestedClasses() {
    return Collections.emptyList();
  }

  @Override
  public void addNestedClass(TypeSpec nestedClass) {
    throw new UnsupportedOperationException("addNestedClass not supported by an Operation Shape");
  }

  @Override
  public FieldSpec asField(String fieldName, ShapeGeneratorMap shapeGeneratorMap) {
    return null;
  }

  @Override
  public Collection<FieldAssociatedMethodSpecGenerator> getFieldAssociatedMethodGenerators() {
    return null;
  }

  @Override
  public Collection<AnnotationSpec> getFieldAnnotations() {
    return fieldAnnotations;
  }

  @Override
  public void addFieldAnnotation(AnnotationSpec annotationSpec) {
    Objects.requireNonNull(annotationSpec, "annotationSpec cannot be null");
    fieldAnnotations.add(annotationSpec);
  }

  @Override
  public ParameterSpec asParameter(String parameterName, ShapeGeneratorMap shapeGeneratorMap) {
    return null;
  }
}
