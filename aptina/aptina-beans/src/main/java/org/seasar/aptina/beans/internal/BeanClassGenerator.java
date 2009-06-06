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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import static org.seasar.aptina.beans.internal.Strings.*;

/**
 * 状態クラスを継承した Bean クラスのソースを生成するクラスです．
 * 
 * @author koichik
 */
public class BeanClassGenerator {

    /** {@link ProcessingEnvironment} */
    protected ProcessingEnvironment env;

    /** メッセージフォーマッタ */
    protected MessageFormatter messageFormatter;

    /** ソースの出力先 */
    protected PrintWriter writer;

    /**
     * インスタンスを構築します．
     * 
     * @param env
     *            {@link ProcessingEnvironment}
     */
    public BeanClassGenerator(final ProcessingEnvironment env) {
        this.env = env;
        messageFormatter = new MessageFormatter(env.getLocale());
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
                toQualifiedName(beanInfo.getPackageName(), beanInfo
                        .getBeanClassName()), typeElement);
        writer = new PrintWriter(sourceFile.openWriter());
        try {
            putClassHeader(beanInfo);
            for (final ConstructorInfo constructorInfo : beanInfo
                    .getConstructors()) {
                putConstructor(beanInfo, constructorInfo);
            }
            for (final String propertyName : beanInfo.getPropertyNames()) {
                final PropertyInfo propertyInfo = beanInfo
                        .getPropertyInfo(propertyName);
                if (propertyInfo.isReadable()) {
                    putGetter(propertyInfo);
                }
                if (propertyInfo.isWritable()) {
                    putSetter(propertyInfo);
                }
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
        put("@javax.annotation.Generated(\"Aptina Beans\")%n");
        put("@org.seasar.aptina.beans.JavaBean%n");
        put("public class %1$s%2$s extends %3$s {%n", beanInfo
                .getBeanClassName(), beanInfo.getTypeParameter(), beanInfo
                .getStateClassName());
        put("%n");
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
        putJavadoc(constructorInfo.getComment(), "    ");
        put("    %1$s %2$s %3$s(%4$s)", constructorInfo.getModifier(),
                constructorInfo.getTypeParameters(), beanInfo
                        .getBeanClassName(), join(constructorInfo
                        .getParameterTypes(), " ", constructorInfo
                        .getParameterNames(), ", "));
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
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putGetter(final PropertyInfo propertyInfo) {
        putJavadoc(messageFormatter.getMessage(MessageCode.JDOC0000,
                propertyInfo.getComment()), "    ");
        put("    public %1$s %2$s%3$s() {%n", propertyInfo.getType(),
                propertyInfo.getType().equals("boolean") ? "is" : "get",
                capitalize(propertyInfo.getName()));
        put("        return %1$s;%n", propertyInfo.getName());
        put("    }%n%n");
    }

    /**
     * setter メソッドを出力します．
     * 
     * @param propertyInfo
     *            生成するプロパティの情報
     */
    protected void putSetter(final PropertyInfo propertyInfo) {
        putJavadoc(messageFormatter.getMessage(MessageCode.JDOC0001,
                propertyInfo.getComment(), propertyInfo.getName()), "    ");
        put("    public void set%1$s(%2$s %3$s) {%n", capitalize(propertyInfo
                .getName()), propertyInfo.getType(), propertyInfo.getName());
        put("        this.%1$s = %1$s;%n", propertyInfo.getName());
        put("    }%n%n");
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
     * フォーマットされた文字列を出力します．
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
     * クラスの完全限定名を返します．
     * 
     * @param packageName
     *            パッケージ名
     * @param simpleName
     *            単純名
     * @return 完全限定名
     */
    protected static String toQualifiedName(final String packageName,
            final String simpleName) {
        if (packageName == null) {
            return simpleName;
        }
        return packageName + '.' + simpleName;
    }

}
