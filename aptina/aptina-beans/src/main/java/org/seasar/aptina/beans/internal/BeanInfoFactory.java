/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.aptina.beans.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.aptina.beans.BeanState;
import org.seasar.aptina.beans.Property;
import org.seasar.aptina.commons.message.EnumMessageFormatter;

import static org.seasar.aptina.commons.util.ClassUtils.*;
import static org.seasar.aptina.commons.util.ElementUtils.*;
import static org.seasar.aptina.commons.util.StringUtils.*;

/**
 * 状態クラスから生成される Bean クラスの情報を作成するクラスです．
 * 
 * @author koichik
 */
public class BeanInfoFactory {

    /** プロパティの対象とならないフィールドが持つ修飾子の {@link Set} です． */
    protected static final Set<Modifier> IGNORE_FIELD_MODIFIERS = new HashSet<Modifier>();
    static {
        IGNORE_FIELD_MODIFIERS.add(Modifier.STATIC);
        IGNORE_FIELD_MODIFIERS.add(Modifier.PUBLIC);
        IGNORE_FIELD_MODIFIERS.add(Modifier.PRIVATE);
    }

    /** {@link ProcessingEnvironment} */
    protected ProcessingEnvironment env;

    /** メッセージフォーマッタ */
    protected EnumMessageFormatter<MessageCode> messageFormatter;

    /** 処理対象のクラスに付けられた {@link BeanState} アノテーションを表現する {@link AnnotationMirror} */
    protected AnnotationMirror beanStateAnnotation;

    /** 状態クラスにエラーがある場合は {@literal true} */
    protected boolean hasError;

    /**
     * インスタンスを構築します．
     * 
     * @param env
     *            {@link ProcessingEnvironment}
     */
    public BeanInfoFactory(final ProcessingEnvironment env) {
        this.env = env;
        messageFormatter = new EnumMessageFormatter<MessageCode>(
                MessageCode.class, env.getLocale());
    }

    /**
     * 状態クラスの {@link TypeElement} を処理して Bean クラスの情報を持つ {@link BeanInfo} を生成します．
     * 
     * @param typeElement
     *            状態クラスの {@link TypeElement}
     * @return Bean クラスの情報を持つ {@link BeanInfo}
     */
    public BeanInfo createBeanInfo(final TypeElement typeElement) {
        beanStateAnnotation = getAnnotationMirror(typeElement, BeanState.class);
        hasError = false;
        final BeanInfo beanInfo = processType(typeElement);
        if (beanInfo == null) {
            return null;
        }
        for (final VariableElement fieldElement : ElementFilter
                .fieldsIn(typeElement.getEnclosedElements())) {
            final PropertyInfo propertyInfo = processField(fieldElement);
            if (propertyInfo != null) {
                beanInfo.addPropertyInfo(propertyInfo);
            }
        }

        for (final ExecutableElement constructorElement : ElementFilter
                .constructorsIn(typeElement.getEnclosedElements())) {
            final ConstructorInfo constructorInfo = processConstructor(constructorElement);
            if (constructorInfo != null) {
                beanInfo.addConstructor(constructorInfo);
            }
        }
        if (beanInfo.getConstructors().isEmpty()) {
            printMessage(typeElement, MessageCode.CTOR0001);
        }

        for (final ExecutableElement methodElement : ElementFilter
                .methodsIn(typeElement.getEnclosedElements())) {
            processMethod(methodElement, beanInfo);
        }

        return hasError ? null : beanInfo;
    }

    /**
     * 状態クラスの {@link TypeElement} を処理して Bean クラスの情報を集めます．
     * 
     * @param typeElement
     *            状態クラスの {@link TypeElement}
     * @return Bean クラスの情報
     */
    protected BeanInfo processType(final TypeElement typeElement) {
        final BeanInfo beanInfo = new BeanInfo();

        switch (typeElement.getKind()) {
        case INTERFACE:
            printMessage(typeElement, beanStateAnnotation, MessageCode.CLS0000);
            return null;
        case ENUM:
            printMessage(typeElement, beanStateAnnotation, MessageCode.CLS0001);
            return null;
        case ANNOTATION_TYPE:
            printMessage(typeElement, beanStateAnnotation, MessageCode.CLS0002);
            return null;
        }
        switch (typeElement.getNestingKind()) {
        case LOCAL:
            printMessage(typeElement, beanStateAnnotation, MessageCode.CLS0003);
            return null;
        case MEMBER:
        case ANONYMOUS:
            printMessage(typeElement, beanStateAnnotation, MessageCode.CLS0004);
            return null;
        }
        if (typeElement.getModifiers().contains(Modifier.FINAL)) {
            printMessage(typeElement, beanStateAnnotation, MessageCode.CLS0005);
            return null;
        }
        if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            printMessage(typeElement, beanStateAnnotation, MessageCode.CLS0006);
            return null;
        }

        beanInfo.setComment(env.getElementUtils().getDocComment(typeElement));
        final String stateClassName = typeElement.getQualifiedName().toString();
        final String packageName = getPackageName(stateClassName);
        beanInfo.setPackageName(packageName);
        beanInfo.setBeanClassName(toBeanClassName(typeElement.getSimpleName()
                .toString()));
        final List<? extends TypeParameterElement> typeParameters = typeElement
                .getTypeParameters();
        beanInfo.setTypeParameter(toStringOfTypeParameterDecl(typeParameters));
        beanInfo.setStateClassName(stateClassName
                + toStringOfTypeParameterNames(typeParameters));

        final BeanState beanState = typeElement.getAnnotation(BeanState.class);
        if (beanState.boundProperties()) {
            beanInfo.setBoundProperties(true);
        }
        if (beanState.constrainedProperties()) {
            beanInfo.setConstrainedProperties(true);
        }
        return beanInfo;
    }

    /**
     * 状態クラスのフィールドに対応する {@link VariableElement} を処理して Bean クラスのプロパティ情報を集めます．
     * 
     * @param variableElement
     *            状態クラスのフィールドに対応する {@link VariableElement}
     * @return Bean クラスのプロパティ情報
     */
    protected PropertyInfo processField(final VariableElement variableElement) {
        final PropertyInfo propertyInfo = new PropertyInfo();
        final Property property = variableElement.getAnnotation(Property.class);
        final Set<Modifier> modifiers = variableElement.getModifiers();
        if (property == null) {
            if (!Collections.disjoint(modifiers, IGNORE_FIELD_MODIFIERS)) {
                return null;
            }
        } else {
            final AnnotationMirror propertyAnnotationMirror = getAnnotationMirror(
                    variableElement, Property.class);
            if (modifiers.contains(Modifier.PRIVATE)) {
                printMessage(variableElement, propertyAnnotationMirror,
                        MessageCode.FLD0000);
                return null;
            } else if (modifiers.contains(Modifier.PUBLIC)) {
                printMessage(variableElement, propertyAnnotationMirror,
                        MessageCode.FLD0001);
                return null;
            } else if (modifiers.contains(Modifier.STATIC)) {
                printMessage(variableElement, propertyAnnotationMirror,
                        MessageCode.FLD0002);
                return null;
            }
            switch (property.access()) {
            case NONE:
                return null;
            case WRITE_ONLY:
                if (modifiers.contains(Modifier.FINAL)) {
                    printMessage(variableElement, propertyAnnotationMirror,
                            MessageCode.FLD0003);
                    return null;
                }
                break;
            }
        }

        final String propertyName = variableElement.getSimpleName().toString();
        final String comment = env.getElementUtils().getDocComment(
                variableElement);
        if (comment == null || comment.isEmpty()) {
            propertyInfo.setComment(propertyName);
        } else {
            propertyInfo.setComment(comment.trim());
        }
        propertyInfo.setName(propertyName);
        final TypeMirror propertyType = variableElement.asType();
        propertyInfo.setType(propertyType.toString());
        if (propertyType.getKind() == TypeKind.ARRAY) {
            propertyInfo.setArray(true);
            propertyInfo.setComponentType(ArrayType.class.cast(propertyType)
                    .getComponentType().toString());
        }
        if (property != null) {
            switch (property.access()) {
            case READ_ONLY:
                propertyInfo.setWritable(false);
                break;
            case WRITE_ONLY:
                propertyInfo.setReadable(false);
                break;
            }
        }
        if (modifiers.contains(Modifier.FINAL)) {
            propertyInfo.setWritable(false);
        }
        return propertyInfo;
    }

    /**
     * 状態クラスのコンストラクタに対応する {@link ExecutableElement} を処理して Bean
     * クラスのコンストラクタ情報を集めます．
     * 
     * @param executableElement
     *            状態クラスのコンストラクタに対応する {@link ExecutableElement}
     * @return Bean クラスのコンストラクタ情報
     */
    protected ConstructorInfo processConstructor(
            final ExecutableElement executableElement) {
        final ConstructorInfo constructorInfo = new ConstructorInfo();
        final List<? extends VariableElement> parameters = executableElement
                .getParameters();
        final Set<Modifier> modifiers = executableElement.getModifiers();
        if (parameters.isEmpty() && !modifiers.contains(Modifier.PUBLIC)) {
            printMessage(executableElement, MessageCode.CTOR0000);
        }
        if (modifiers.contains(Modifier.PRIVATE)) {
            return null;
        }
        constructorInfo.setComment(env.getElementUtils().getDocComment(
                executableElement));
        constructorInfo.addModifiers(modifiers);
        constructorInfo
                .setTypeParameters(toStringOfTypeParameterDecl(executableElement
                        .getTypeParameters()));
        for (final VariableElement variableElement : parameters) {
            constructorInfo.addParameterType(variableElement.asType()
                    .toString());
            constructorInfo.addParameterName(variableElement.getSimpleName()
                    .toString());
        }
        for (final TypeMirror typeMirror : executableElement.getThrownTypes()) {
            constructorInfo.addThrownType(typeMirror.toString());
        }
        return constructorInfo;
    }

    /**
     * 状態クラスのメソッドに対応する {@link ExecutableElement} を処理して Bean クラスのプロパティ情報を修正します．
     * 
     * @param executableElement
     *            状態クラスのメソッドに対応する {@link ExecutableElement}
     * @param beanInfo
     *            Bean クラスの情報
     */
    protected void processMethod(final ExecutableElement executableElement,
            final BeanInfo beanInfo) {
        final String methodName = executableElement.getSimpleName().toString();
        if (methodName.startsWith("is") && methodName.length() > 2
                && Character.isUpperCase(methodName.charAt(2))
                && executableElement.getParameters().isEmpty()) {
            final String propertyName = decapitalize(methodName.substring(2));
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo != null) {
                propertyInfo.setReadable(false);
            }
        } else if (methodName.startsWith("get") && methodName.length() > 3
                && Character.isUpperCase(methodName.charAt(3))
                && executableElement.getParameters().isEmpty()) {
            final String propertyName = decapitalize(methodName.substring(3));
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo != null) {
                propertyInfo.setReadable(false);
            }
        }
        if (methodName.startsWith("set") && methodName.length() > 3
                && Character.isUpperCase(methodName.charAt(3))
                && executableElement.getParameters().size() == 1) {
            final String propertyName = decapitalize(methodName.substring(3));
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo != null) {
                final String type = executableElement.getParameters().get(0)
                        .asType().toString();
                if (propertyInfo.getType().equals(type)) {
                    propertyInfo.setWritable(false);
                }
            }
        }
    }

    /**
     * 状態クラスの名前から対応する Bean クラスの名前を返します．
     * 
     * @param stateClassName
     *            状態クラスの単純名
     * @return Bean クラスの単純名
     */
    protected static String toBeanClassName(final String stateClassName) {
        if (stateClassName.startsWith("Abstract")) {
            return stateClassName.substring("Abstract".length());
        }
        if (stateClassName.endsWith("State")) {
            return stateClassName.substring(0, stateClassName.length()
                    - "State".length());
        }
        if (stateClassName.endsWith("Bean")) {
            return stateClassName + "Impl";
        }
        return stateClassName + "Bean";
    }

    /**
     * メッセージを出力します．
     * 
     * @param element
     *            メッセージの対象となる要素
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージに埋め込む引数
     */
    protected void printMessage(final Element element,
            final MessageCode messageCode, final Object... args) {
        if (messageCode.getKind() == Kind.ERROR) {
            hasError = true;
        }
        env.getMessager().printMessage(messageCode.getKind(),
                messageFormatter.getMessage(messageCode, args), element);
    }

    /**
     * メッセージを出力します．
     * 
     * @param element
     *            メッセージの対象となる要素
     * @param annotation
     *            メッセージの対象となるアノテーション
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージに埋め込む引数
     */
    protected void printMessage(final Element element,
            final AnnotationMirror annotation, final MessageCode messageCode,
            final Object... args) {
        if (messageCode.getKind() == Kind.ERROR) {
            hasError = true;
        }
        env.getMessager().printMessage(messageCode.getKind(),
                messageFormatter.getMessage(messageCode, args), element,
                annotation);
    }

}
