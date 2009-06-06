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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.aptina.beans.Property;

import static org.seasar.aptina.beans.internal.Strings.*;

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
    protected MessageFormatter messageFormatter;

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
        messageFormatter = new MessageFormatter(env.getLocale());
    }

    /**
     * 状態クラスの {@link TypeElement} を処理して Bean クラスの情報を持つ {@link BeanInfo} を生成します．
     * 
     * @param typeElement
     *            状態クラスの {@link TypeElement}
     * @return Bean クラスの情報を持つ {@link BeanInfo}
     */
    public BeanInfo createBeanInfo(final TypeElement typeElement) {
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
            printMessage(typeElement, MessageCode.CLS0000);
            return null;
        case ENUM:
            printMessage(typeElement, MessageCode.CLS0001);
            return null;
        case ANNOTATION_TYPE:
            printMessage(typeElement, MessageCode.CLS0002);
            return null;
        }
        switch (typeElement.getNestingKind()) {
        case LOCAL:
            printMessage(typeElement, MessageCode.CLS0003);
            return null;
        case MEMBER:
        case ANONYMOUS:
            printMessage(typeElement, MessageCode.CLS0004);
            return null;
        }
        if (typeElement.getModifiers().contains(Modifier.FINAL)) {
            printMessage(typeElement, MessageCode.CLS0005);
            return null;
        }
        if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            printMessage(typeElement, MessageCode.CLS0006);
            return null;
        }

        beanInfo.setComment(env.getElementUtils().getDocComment(typeElement));
        final String stateClassName = typeElement.getQualifiedName().toString();
        final String packageName = toPackageName(stateClassName);
        beanInfo.setPackageName(packageName);
        beanInfo.setBeanClassName(toBeanClassName(typeElement.getSimpleName()
                .toString()));
        final List<? extends TypeParameterElement> typeParameters = typeElement
                .getTypeParameters();
        beanInfo.setTypeParameter(toStringTypeParameterDecl(typeParameters));
        beanInfo.setStateClassName(stateClassName
                + toStringTypeParameterNames(typeParameters));
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
            if (modifiers.contains(Modifier.PRIVATE)) {
                printMessage(variableElement, MessageCode.FLD0000);
            } else if (modifiers.contains(Modifier.PUBLIC)) {
                printMessage(variableElement, MessageCode.FLD0001);
            } else if (modifiers.contains(Modifier.STATIC)) {
                printMessage(variableElement, MessageCode.FLD0002);
            }
            switch (property.access()) {
            case NONE:
                return null;
            case WRITE_ONLY:
                if (modifiers.contains(Modifier.FINAL)) {
                    printMessage(variableElement, MessageCode.FLD0003);
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
        propertyInfo.setType(variableElement.asType().toString());
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
        if (modifiers.contains(Modifier.PRIVATE)) {
            if (parameters.isEmpty()) {
                printMessage(executableElement, MessageCode.CTOR0000);
            }
            return null;
        } else if (modifiers.contains(Modifier.PROTECTED)
                || !modifiers.contains(Modifier.PUBLIC)) {
            printMessage(executableElement, MessageCode.CTOR0000);
        }
        constructorInfo.setComment(env.getElementUtils().getDocComment(
                executableElement));
        if (executableElement.getModifiers().contains(Modifier.PUBLIC)) {
            constructorInfo.setModifier("public");
        } else if (executableElement.getModifiers()
                .contains(Modifier.PROTECTED)) {
            constructorInfo.setModifier("protected");
        } else {
            constructorInfo.setModifier("");
        }
        constructorInfo
                .setTypeParameters(toStringTypeParameterDecl(executableElement
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
     * クラスの完全限定名からパッケージ名を返します．
     * 
     * @param qualifiedName
     *            完全限定名
     * @return パッケージ名
     */
    protected static String toPackageName(final String qualifiedName) {
        final int pos = qualifiedName.lastIndexOf('.');
        if (pos == -1) {
            return null;
        }
        return qualifiedName.substring(0, pos);
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
     * 型引数名の文字列表現を返します．
     * 
     * @param typeParameters
     *            型引数の {@link List}
     * @return 型引数名の文字列表現
     */
    protected static String toStringTypeParameterNames(
            final List<? extends TypeParameterElement> typeParameters) {
        if (typeParameters.isEmpty()) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(64);
        buf.append("<").append(
                join(toStringListOfTypeParameterNames(typeParameters), ", "))
                .append(">");
        return new String(buf);
    }

    /**
     * 型引数宣言の文字列表現を返します．
     * 
     * @param typeParameters
     *            型引数の {@link List}
     * @return 型引数宣言の文字列表現
     */
    protected static String toStringTypeParameterDecl(
            final List<? extends TypeParameterElement> typeParameters) {
        if (typeParameters.isEmpty()) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(64);
        String parameterPrefix = "<";
        for (final TypeParameterElement typeParameter : typeParameters) {
            final List<? extends TypeMirror> bounds = typeParameter.getBounds();
            buf.append(parameterPrefix).append(typeParameter.getSimpleName());
            if (bounds.size() > 1
                    || bounds.get(0).toString() == "java.lang.Object") {
                buf.append(" extends ").append(
                        join(toStringListOfTypeParameters(bounds), " & "));
            }
            parameterPrefix = ", ";
        }
        buf.append('>');
        return new String(buf);
    }

    /**
     * 型引き数名の {@link List} を返します．
     * 
     * @param elements
     *            型引数を表す {@link TypeElement} の {@link List}
     * @return 型引き数名の {@link List}
     */
    protected static List<String> toStringListOfTypeParameterNames(
            final List<? extends Element> elements) {
        final List<String> result = new ArrayList<String>();
        for (final Element element : elements) {
            result.add(element.getSimpleName().toString());
        }
        return result;
    }

    /**
     * 型名の {@link List} を返します．
     * 
     * @param typeMirrors
     *            型の {@link List}
     * @return 型名の {@link List}
     */
    protected static List<String> toStringListOfTypeParameters(
            final List<? extends TypeMirror> typeMirrors) {
        final List<String> result = new ArrayList<String>();
        for (final TypeMirror typeMirror : typeMirrors) {
            result.add(typeMirror.toString());
        }
        return result;
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

}
