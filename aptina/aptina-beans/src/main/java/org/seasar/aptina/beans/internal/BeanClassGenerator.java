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

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.seasar.aptina.commons.message.EnumMessageFormatter;

import static org.seasar.aptina.beans.internal.AptinaBeans.*;
import static org.seasar.aptina.beans.internal.BeanClassFormat.*;
import static org.seasar.aptina.commons.util.ClassUtils.*;
import static org.seasar.aptina.commons.util.ModifierUtils.*;
import static org.seasar.aptina.commons.util.StringUtils.*;
import static org.seasar.aptina.commons.util.VersionUtils.*;

/**
 * 状態クラスを継承した Bean クラスのソースを生成するクラスです．
 * 
 * @author koichik
 */
public class BeanClassGenerator {

    /** Aptina Beans のバージョン情報 */
    protected static final String PRODUCT_VERSION = getVersion(GROUP_ID,
            ARTIFACT_ID, "DEV");

    /** {@link ProcessingEnvironment} */
    protected ProcessingEnvironment env;

    /** ソースの出力先 */
    protected PrintWriter writer;

    /** メッセージフォーマッタ */
    protected EnumMessageFormatter<BeanClassFormat> messageFormatter;

    /**
     * インスタンスを構築します．
     * 
     * @param env
     *            {@link ProcessingEnvironment}
     */
    public BeanClassGenerator(final ProcessingEnvironment env) {
        this.env = env;
        messageFormatter = new EnumMessageFormatter<BeanClassFormat>(
                BeanClassFormat.class, env.getLocale());
    }

    /**
     * Bean クラスのソースを生成します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     * @param typeElement
     *            生成元となった状態クラスの{@link TypeElement}
     * @throws IOException
     *             入出力で例外が発生した場合
     */
    public void generate(final BeanInfo beanInfo, final TypeElement typeElement)
            throws IOException {
        final Filer filer = env.getFiler();
        final JavaFileObject sourceFile = filer.createSourceFile(
                getQualifiedName(beanInfo.getPackageName(), beanInfo
                        .getBeanClassName()), typeElement);
        writer = new PrintWriter(sourceFile.openWriter());
        messageFormatter = new EnumMessageFormatter<BeanClassFormat>(
                BeanClassFormat.class, env.getLocale(), writer);
        try {
            putClassHeader(beanInfo);
            for (final ConstructorInfo constructorInfo : beanInfo
                    .getConstructors()) {
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
        } finally {
            writer.close();
        }
    }

    /**
     * クラスのヘッダを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     */
    protected void putClassHeader(final BeanInfo beanInfo) {
        put("package %1$s;%n%n", beanInfo.getPackageName());
        putJavadoc(beanInfo.getComment(), "");
        put("@javax.annotation.Generated({\"Aptina Beans\", \"%1$s\"})%n",
                PRODUCT_VERSION);
        put("@org.seasar.aptina.beans.JavaBean%n");
        put("public class %1$s%2$s extends %3$s {%n", beanInfo
                .getBeanClassName(), beanInfo.getTypeParameter(), beanInfo
                .getStateClassName());
        put("%n");

        if (beanInfo.isBoundProperties()) {
            put("    java.beans.PropertyChangeSupport propertyChangeSupport =%n"
                    + "        new java.beans.PropertyChangeSupport(this);%n%n");
        }
        if (beanInfo.isConstrainedProperties()) {
            put("    java.beans.VetoableChangeSupport vetoableChangeSupport =%n"
                    + "        new java.beans.VetoableChangeSupport(this);%n%n");
        }
    }

    /**
     * クラスのフッタを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     */
    protected void putClassFooter(final BeanInfo beanInfo) {
        put("}%n");
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
        final String params = join(constructorInfo.getParameterTypes(),
                constructorInfo.getParameterNames(), " ", ", ");
        putJavadoc(constructorInfo.getComment(), "    ");
        put("    %1$s%2$s %3$s(%4$s)", modifiers, typeParameters, className,
                params);
        if (!constructorInfo.getThrownTypes().isEmpty()) {
            put(" throws %1$s", join(constructorInfo.getThrownTypes(), ", "));
        }
        put(" {%n");
        put("        super(%1$s);%n", join(constructorInfo.getParameterNames(),
                ", "));
        put("    }%n%n");
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

        putJavadoc(messageFormatter.getMessage(JDOC0000, comment), "    ");
        put("    public %1$s %2$s%3$s() {%n", type,
                type.equals("boolean") ? "is" : "get", capitalizedName);
        put("        return %1$s;%n", name);
        put("    }%n%n");
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
        final String name = propertyInfo.getName();
        final String capitalizedName = capitalize(name);

        final String componentType = propertyInfo.getComponentType();
        putJavadoc(messageFormatter.getMessage(JDOC0001, comment), "    ");
        put(
                "    public %1$s %2$s%3$s(int n) throws ArrayIndexOutOfBoundsException {%n",
                componentType, type.equals("boolean") ? "is" : "get",
                capitalizedName);
        put("        return %1$s[n];%n", name);
        put("    }%n%n");
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

        putJavadoc(messageFormatter.getMessage(constrained ? JDOC0004
                : JDOC0002, comment, name), "    ");
        put("    public void set%1$s(%2$s %3$s)%4$s {%n", capitalizedName,
                type, name, exceptions);
        if (bound || constrained) {
            put("        %1$s old%2$s = this.%3$s;%n", type, capitalizedName,
                    name);
        }
        if (constrained) {
            put(
                    "        vetoableChangeSupport.fireVetoableChange(\"%1$s\", old%2$s, %1$s);%n",
                    name, capitalizedName);
        }
        put("        this.%1$s = %1$s;%n", name);
        if (bound) {
            put(
                    "        propertyChangeSupport.firePropertyChange(\"%1$s\", old%2$s, %1$s);%n",
                    name, capitalizedName);
        }
        put("    }%n%n");
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
        final String exceptions = (constrained ? ", java.beans.PropertyVetoException"
                : "");

        putJavadoc(messageFormatter.getMessage(constrained ? JDOC0005
                : JDOC0003, comment, name), "    ");
        put("    public void set%1$s(int n, %2$s %3$s) "
                + "throws ArrayIndexOutOfBoundsException%4$s {%n",
                capitalizedName, componentType, name, exceptions);
        if (bound || constrained) {
            put("        %1$s old%2$s = this.%3$s[n];%n", componentType,
                    capitalizedName, name);
        }
        if (constrained) {
            put("        vetoableChangeSupport.fireVetoableChange("
                    + "new java.beans.IndexedPropertyChangeEvent("
                    + "this, \"%1$s\", old%2$s, %1$s, n));%n", name,
                    capitalizedName);
        }
        put("        this.%1$s[n] = %1$s;%n", name);
        if (bound) {
            put("        propertyChangeSupport.fireIndexedPropertyChange("
                    + "\"%1$s\", n, old%2$s, %1$s);%n", name, capitalizedName);
        }
        put("    }%n%n");
    }

    /**
     * イベントリスナの追加・削除メソッドを出力します．
     * 
     * @param beanInfo
     *            生成する JavaBeans の情報
     */
    protected void putEventListener(final BeanInfo beanInfo) {
        if (beanInfo.isBoundProperties()) {
            putJavadoc(messageFormatter.getMessage(JDOC0006,
                    PropertyChangeListener.class.getName()), "    ");
            put("    public void addPropertyChangeListener("
                    + "java.beans.PropertyChangeListener listener) {%n");
            put("        propertyChangeSupport.addPropertyChangeListener(listener);%n");
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0007,
                    PropertyChangeListener.class.getName()), "    ");
            put("    public void addPropertyChangeListener("
                    + "String propertyName, java.beans.PropertyChangeListener listener) {%n");
            put("        propertyChangeSupport.addPropertyChangeListener("
                    + "propertyName, listener);%n");
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0008,
                    PropertyChangeListener.class.getName()), "    ");
            put("    public void removePropertyChangeListener("
                    + "java.beans.PropertyChangeListener listener) {%n");
            put("        propertyChangeSupport.removePropertyChangeListener(listener);%n");
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0009,
                    PropertyChangeListener.class.getName()), "    ");
            put("    public void removePropertyChangeListener("
                    + "String propertyName, java.beans.PropertyChangeListener listener) {%n");
            put("        propertyChangeSupport.removePropertyChangeListener("
                    + "propertyName, listener);%n");
            put("    }%n%n");
        }

        if (beanInfo.isConstrainedProperties()) {
            putJavadoc(messageFormatter.getMessage(JDOC0006,
                    VetoableChangeListener.class.getName()), "    ");
            put("    public void addVetoableChangeListener("
                    + "java.beans.VetoableChangeListener listener) {%n");
            put("        vetoableChangeSupport.addVetoableChangeListener(listener);%n");
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0007,
                    VetoableChangeListener.class.getName()), "    ");
            put("    public void addVetoableChangeListener("
                    + "String propertyName, java.beans.VetoableChangeListener listener) {%n");
            put("        vetoableChangeSupport.addVetoableChangeListener("
                    + "propertyName, listener);%n");
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0008,
                    VetoableChangeListener.class.getName()), "    ");
            put("    public void removeVetoableChangeListener("
                    + "java.beans.VetoableChangeListener listener) {%n");
            put("        vetoableChangeSupport.removeVetoableChangeListener(listener);%n");
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0009,
                    VetoableChangeListener.class.getName()), "    ");
            put("    public void removeVetoableChangeListener("
                    + "String propertyName, java.beans.VetoableChangeListener listener) {%n");
            put("        vetoableChangeSupport.removeVetoableChangeListener("
                    + "propertyName, listener);%n");
            put("    }%n%n");
        }
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

        if (beanInfo.isBoundProperties()) {
            putJavadoc(messageFormatter.getMessage(JDOC0010,
                    PropertyChangeListener.class.getName(), comment), "    ");
            put("    public void add%1$sChangeListener("
                    + "java.beans.PropertyChangeListener listener) {%n",
                    capitalizeName);
            put("        propertyChangeSupport.addPropertyChangeListener("
                    + "\"%1$s\", listener);%n", name);
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0011,
                    PropertyChangeListener.class.getName(), comment), "    ");
            put("    public void remove%1$sChangeListener("
                    + "java.beans.PropertyChangeListener listener) {%n",
                    capitalizeName);
            put("        propertyChangeSupport.removePropertyChangeListener("
                    + "\"%1$s\", listener);%n", name);
            put("    }%n%n");
        }

        if (beanInfo.isConstrainedProperties()) {
            putJavadoc(messageFormatter.getMessage(JDOC0010,
                    VetoableChangeListener.class.getName(), comment), "    ");
            put("    public void add%1$sChangeListener("
                    + "java.beans.VetoableChangeListener listener) {%n",
                    capitalizeName);
            put("        vetoableChangeSupport.addVetoableChangeListener("
                    + "\"%1$s\", listener);%n", name);
            put("    }%n%n");

            putJavadoc(messageFormatter.getMessage(JDOC0011,
                    VetoableChangeListener.class.getName(), comment), "    ");
            put("    public void remove%1$sChangeListener("
                    + "java.beans.VetoableChangeListener listener) {%n",
                    capitalizeName);
            put("        vetoableChangeSupport.removeVetoableChangeListener("
                    + "\"%1$s\", listener);%n", name);
            put("    }%n%n");
        }
    }

    /**
     * Javadoc コメントを出力します．
     * 
     * @param comment
     *            Javadoc コメント
     * @param indent
     *            インデントするスペース
     */
    protected void putJavadoc(final String comment, final String indent) {
        if (comment == null || comment.isEmpty()) {
            return;
        }
        put("%1$s/**%n", indent);
        final BufferedReader reader = new BufferedReader(new StringReader(
                comment));
        try {
            String line = reader.readLine();
            if (!line.isEmpty()) {
                put("%1$s *%2$s%n", indent, line);
            }
            while ((line = reader.readLine()) != null) {
                put("%1$s *%2$s%n", indent, line);
            }
        } catch (final IOException ignore) {
        }
        put("%1$s */%n", indent);
    }

    /**
     * フォーマットした文字列を出力します．
     * 
     * @param format
     *            フォーマット文字列
     * @param args
     *            フォーマットから参照される引数の並び
     */
    protected void put(final String format, final Object... args) {
        writer.printf(format, args);
    }

    /**
     * フォーマットした文字列を出力します．
     * 
     * @param format
     *            フォーマットを示す列挙
     * @param args
     *            フォーマットから参照される引数の並び
     */
    protected void put(final BeanClassFormat format, final Object... args) {
        messageFormatter.format(format, args);
    }

}
