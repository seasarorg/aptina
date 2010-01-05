/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.aptina.commons.source.SimpleSourceGenerator;

import static org.seasar.aptina.beans.internal.AptinaBeans.*;
import static org.seasar.aptina.beans.internal.BeanClassFormat.*;
import static org.seasar.aptina.commons.util.ClassUtils.*;
import static org.seasar.aptina.commons.util.CollectionUtils.*;
import static org.seasar.aptina.commons.util.ModifierUtils.*;
import static org.seasar.aptina.commons.util.StringUtils.*;

/**
 * 状態クラスを継承した Bean クラスのソースを生成するクラスです．
 * 
 * @author koichik
 */
public class BeanClassGenerator extends SimpleSourceGenerator<BeanClassFormat> {

    /**
     * インスタンスを構築します．
     * 
     * @param env
     *            {@link ProcessingEnvironment}
     */
    public BeanClassGenerator(final ProcessingEnvironment env) {
        super(env, BeanClassFormat.class);
    }

    /**
     * Bean クラスのソースを生成して {@link Filer} に出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param originalElement
     *            生成元となった状態クラスの{@link TypeElement}
     * @throws IOException
     *             入出力で例外が発生した場合
     */
    public void generateAndWrite(final BeanInfo beanInfo,
            final TypeElement originalElement) throws IOException {
        generate(beanInfo, originalElement);
        write(getQualifiedName(beanInfo.getPackageName(), beanInfo
            .getBeanClassName()), originalElement);
    }

    /**
     * Bean クラスのソースを生成します．
     * <p>
     * 生成されたソースは {@link #toString()} で取得することができます．
     * </p>
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param originalElement
     *            生成元となった状態クラスの{@link TypeElement}
     * @throws IOException
     *             入出力で例外が発生した場合
     */
    public void generate(final BeanInfo beanInfo,
            final TypeElement originalElement) {
        reset();
        putClassHeader(beanInfo);
        putFields(beanInfo);
        for (final ConstructorInfo constructorInfo : beanInfo.getConstructors()) {
            putConstructor(beanInfo, constructorInfo);
        }
        putEventListener(beanInfo);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            putGetter(beanInfo, propertyInfo);
            putSetter(beanInfo, propertyInfo);
            putSpecificEventListener(beanInfo, propertyInfo);
        }
        putClassFooter(beanInfo);
    }

    /**
     * クラスのヘッダを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     */
    protected void putClassHeader(final BeanInfo beanInfo) {
        final String packageName = beanInfo.getPackageName();
        if (isNotEmpty(packageName)) {
            printf("package %1$s;%n%n", packageName);
        }
        printJavadoc(beanInfo.getComment());
        printf("@org.seasar.aptina.beans.JavaBean%n");
        printGeneratedAnnotation(PRODUCT_NAME, GROUP_ID, ARTIFACT_ID);
        printf("public class %1$s%2$s extends %3$s {%n", beanInfo
            .getBeanClassName(), beanInfo.getTypeParameter(), beanInfo
            .getStateClassName());
        printf("%n");
    }

    /**
     * フィールドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     */
    protected void putFields(final BeanInfo beanInfo) {
        enter();
        if (beanInfo.isBoundProperties()) {
            printf("java.beans.PropertyChangeSupport propertyChangeSupport =%n");
            printf("    new java.beans.PropertyChangeSupport(this);%n%n");
        }
        if (beanInfo.isConstrainedProperties()) {
            printf("java.beans.VetoableChangeSupport vetoableChangeSupport =%n");
            printf("    new java.beans.VetoableChangeSupport(this);%n%n");
        }
        leave();
    }

    /**
     * クラスのフッタを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     */
    protected void putClassFooter(final BeanInfo beanInfo) {
        printf("}%n");
    }

    /**
     * コンストラクタを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param constructorInfo
     *            生成するコンストラクタの情報
     */
    protected void putConstructor(final BeanInfo beanInfo,
            final ConstructorInfo constructorInfo) {
        final String modifiers = toStringOfModifiers(constructorInfo
            .getModifiers());
        final String typeParameters = constructorInfo.getTypeParameters();
        final String className = beanInfo.getBeanClassName();
        final String params = join(
            constructorInfo.getParameterTypes(),
            constructorInfo.getParameterNames(),
            " ",
            ", ");
        final String exceptions = constructorInfo.getThrownTypes().isEmpty() ? ""
                : " throws " + join(constructorInfo.getThrownTypes(), ", ");

        enter();
        printJavadoc(constructorInfo.getComment());
        printf(
            "%1$s%2$s %3$s(%4$s)%5$s {%n",
            modifiers,
            typeParameters,
            className,
            params,
            exceptions);
        enter();
        printf(
            "super(%1$s);%n",
            join(constructorInfo.getParameterNames(), ", "));
        leave();
        printf("}%n%n");
        leave();
    }

    /**
     * getter メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putGetter(final BeanInfo beanInfo,
            final PropertyInfo propertyInfo) {
        if (propertyInfo.isReadable()) {
            putNormalGetter(beanInfo, propertyInfo);
            if (propertyInfo.isArray()) {
                putIndexedGetter(beanInfo, propertyInfo);
            }
        }
    }

    /**
     * 通常のプロパティの getter メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putNormalGetter(final BeanInfo beanInfo,
            final PropertyInfo propertyInfo) {
        final String comment = propertyInfo.getComment();
        final String type = propertyInfo.getType();
        final String name = propertyInfo.getName();
        final String capitalizedName = capitalize(name);

        enter();
        printJavadoc(JDOC0000, comment);
        printf(
            "public %1$s %2$s%3$s() {%n",
            type,
            type.equals("boolean") ? "is" : "get",
            capitalizedName);
        enter();
        printf("return %1$s;%n", name);
        leave();
        printf("}%n%n");
        leave();
    }

    /**
     * indexed プロパティの getter メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putIndexedGetter(final BeanInfo beanInfo,
            final PropertyInfo propertyInfo) {
        final String comment = propertyInfo.getComment();
        final String type = propertyInfo.getType();
        final String methodPrefix = type.equals("boolean") ? "is" : "get";
        final String name = propertyInfo.getName();
        final String capitalizedName = capitalize(name);
        final String componentType = propertyInfo.getComponentType();

        enter();
        printJavadoc(JDOC0001, comment);
        printf(
            "public %1$s %2$s%3$s(int n) throws ArrayIndexOutOfBoundsException {%n",
            componentType,
            methodPrefix,
            capitalizedName);
        enter();
        printf("return %1$s[n];%n", name);
        leave();
        printf("}%n%n");
        leave();
    }

    /**
     * setter メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putSetter(final BeanInfo beanInfo,
            final PropertyInfo propertyInfo) {
        if (propertyInfo.isWritable()) {
            putNormalSetter(beanInfo, propertyInfo);
            if (propertyInfo.isArray()) {
                putIndexedSetter(beanInfo, propertyInfo);
            }
        }
    }

    /**
     * 通常のプロパティの setter メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putNormalSetter(final BeanInfo beanInfo,
            final PropertyInfo propertyInfo) {
        final String comment = propertyInfo.getComment();
        final String type = propertyInfo.getType();
        final String name = propertyInfo.getName();
        final String capitalizedName = capitalize(name);
        final boolean bound = beanInfo.isBoundProperties();
        final boolean constrained = beanInfo.isConstrainedProperties();
        final String exceptions = constrained ? " throws java.beans.PropertyVetoException"
                : "";

        enter();
        printJavadoc(constrained ? JDOC0004 : JDOC0002, comment, name);
        printf(
            "public void set%1$s(%2$s %3$s)%4$s {%n",
            capitalizedName,
            type,
            name,
            exceptions);
        enter();
        if (bound || constrained) {
            printf("%1$s old%2$s = this.%3$s;%n", type, capitalizedName, name);
        }
        if (constrained) {
            printf(
                "vetoableChangeSupport.fireVetoableChange(\"%1$s\", old%2$s, %1$s);%n",
                name,
                capitalizedName);
        }
        printf("this.%1$s = %1$s;%n", name);
        if (bound) {
            printf(
                "propertyChangeSupport.firePropertyChange(\"%1$s\", old%2$s, %1$s);%n",
                name,
                capitalizedName);
        }
        leave();
        printf("}%n%n");
        leave();
    }

    /**
     * setter メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putIndexedSetter(final BeanInfo beanInfo,
            final PropertyInfo propertyInfo) {
        final String comment = propertyInfo.getComment();
        final String name = propertyInfo.getName();
        final String capitalizedName = capitalize(name);
        final String componentType = propertyInfo.getComponentType();
        final boolean bound = beanInfo.isBoundProperties();
        final boolean constrained = beanInfo.isConstrainedProperties();
        final List<String> exceptions = newArrayList();
        exceptions.add("ArrayIndexOutOfBoundsException");
        if (constrained) {
            exceptions.add("java.beans.PropertyVetoException");
        }

        enter();
        printJavadoc(constrained ? JDOC0005 : JDOC0003, comment, name);
        printf(
            "public void set%1$s(int n, %2$s %3$s) throws %4$s {%n",
            capitalizedName,
            componentType,
            name,
            join(exceptions, ", "));
        enter();
        if (bound || constrained) {
            printf(
                "%1$s old%2$s = this.%3$s[n];%n",
                componentType,
                capitalizedName,
                name);
        }
        if (constrained) {
            printf(
                "vetoableChangeSupport.fireVetoableChange("
                        + "new java.beans.IndexedPropertyChangeEvent("
                        + "this, \"%1$s\", old%2$s, %1$s, n));%n",
                name,
                capitalizedName);
        }
        printf("this.%1$s[n] = %1$s;%n", name);
        if (bound) {
            printf("propertyChangeSupport.fireIndexedPropertyChange("
                    + "\"%1$s\", n, old%2$s, %1$s);%n", name, capitalizedName);
        }
        leave();
        printf("}%n%n");
        leave();
    }

    /**
     * イベントリスナの追加・削除メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     */
    protected void putEventListener(final BeanInfo beanInfo) {
        enter();
        if (beanInfo.isBoundProperties()) {
            printJavadoc(JDOC0006, PropertyChangeListener.class.getName());
            printf("public void addPropertyChangeListener("
                    + "java.beans.PropertyChangeListener listener) {%n");
            enter();
            printf("propertyChangeSupport.addPropertyChangeListener(listener);%n");
            leave();
            printf("}%n%n");

            printJavadoc(JDOC0007, PropertyChangeListener.class.getName());
            printf("public void addPropertyChangeListener("
                    + "String propertyName, java.beans.PropertyChangeListener listener) {%n");
            enter();
            printf("propertyChangeSupport.addPropertyChangeListener("
                    + "propertyName, listener);%n");
            leave();
            printf("}%n%n");

            printJavadoc(JDOC0008, PropertyChangeListener.class.getName());
            printf("public void removePropertyChangeListener("
                    + "java.beans.PropertyChangeListener listener) {%n");
            enter();
            printf("propertyChangeSupport.removePropertyChangeListener(listener);%n");
            leave();
            printf("}%n%n");

            printJavadoc(JDOC0009, PropertyChangeListener.class.getName());
            printf("public void removePropertyChangeListener("
                    + "String propertyName, java.beans.PropertyChangeListener listener) {%n");
            enter();
            printf("propertyChangeSupport.removePropertyChangeListener("
                    + "propertyName, listener);%n");
            leave();
            printf("}%n%n");
        }

        if (beanInfo.isConstrainedProperties()) {
            printJavadoc(JDOC0006, VetoableChangeListener.class.getName());
            printf("public void addVetoableChangeListener("
                    + "java.beans.VetoableChangeListener listener) {%n");
            enter();
            printf("vetoableChangeSupport.addVetoableChangeListener(listener);%n");
            leave();
            printf("}%n%n");

            printJavadoc(JDOC0007, VetoableChangeListener.class.getName());
            printf("public void addVetoableChangeListener("
                    + "String propertyName, java.beans.VetoableChangeListener listener) {%n");
            enter();
            printf("vetoableChangeSupport.addVetoableChangeListener("
                    + "propertyName, listener);%n");
            leave();
            printf("}%n%n");

            printJavadoc(JDOC0008, VetoableChangeListener.class.getName());
            printf("public void removeVetoableChangeListener("
                    + "java.beans.VetoableChangeListener listener) {%n");
            enter();
            printf("vetoableChangeSupport.removeVetoableChangeListener(listener);%n");
            leave();
            printf("}%n%n");

            printJavadoc(JDOC0009, VetoableChangeListener.class.getName());
            printf("public void removeVetoableChangeListener("
                    + "String propertyName, java.beans.VetoableChangeListener listener) {%n");
            enter();
            printf("vetoableChangeSupport.removeVetoableChangeListener("
                    + "propertyName, listener);%n");
            leave();
            printf("}%n%n");
        }
        leave();
    }

    /**
     * イベントリスナの追加・削除メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putSpecificEventListener(final BeanInfo beanInfo,
            final PropertyInfo propertyInfo) {
        if (!propertyInfo.isWritable()) {
            return;
        }
        final String comment = propertyInfo.getComment();
        final String name = propertyInfo.getName();
        final String capitalizeName = capitalize(name);

        enter();
        if (beanInfo.isBoundProperties()) {
            printJavadoc(
                JDOC0010,
                PropertyChangeListener.class.getName(),
                comment);
            printf(
                "public void add%1$sChangeListener("
                        + "java.beans.PropertyChangeListener listener) {%n",
                capitalizeName);
            enter();
            printf("propertyChangeSupport.addPropertyChangeListener("
                    + "\"%1$s\", listener);%n", name);
            leave();
            printf("}%n%n");

            printJavadoc(
                JDOC0011,
                PropertyChangeListener.class.getName(),
                comment);
            printf(
                "public void remove%1$sChangeListener("
                        + "java.beans.PropertyChangeListener listener) {%n",
                capitalizeName);
            enter();
            printf("propertyChangeSupport.removePropertyChangeListener("
                    + "\"%1$s\", listener);%n", name);
            leave();
            printf("}%n%n");
        }

        if (beanInfo.isConstrainedProperties()) {
            printJavadoc(
                JDOC0010,
                VetoableChangeListener.class.getName(),
                comment);
            printf(
                "public void add%1$sChangeListener("
                        + "java.beans.VetoableChangeListener listener) {%n",
                capitalizeName);
            enter();
            printf("vetoableChangeSupport.addVetoableChangeListener("
                    + "\"%1$s\", listener);%n", name);
            leave();
            printf("}%n%n");

            printJavadoc(
                JDOC0011,
                VetoableChangeListener.class.getName(),
                comment);
            printf(
                "public void remove%1$sChangeListener("
                        + "java.beans.VetoableChangeListener listener) {%n",
                capitalizeName);
            enter();
            printf("vetoableChangeSupport.removeVetoableChangeListener("
                    + "\"%1$s\", listener);%n", name);
            leave();
            printf("}%n%n");
        }
        leave();
    }

}
