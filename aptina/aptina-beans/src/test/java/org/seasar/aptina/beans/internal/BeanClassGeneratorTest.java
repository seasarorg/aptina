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
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.lang.model.element.TypeElement;

import org.seasar.aptina.beans.example.BarBeanState;
import org.seasar.aptina.beans.example.FooBeanState;
import org.seasar.aptina.unit.AptinaTestCase;

/**
 * 
 * @author koichik
 */
public class BeanClassGeneratorTest extends AptinaTestCase {

    TypeElement typeElement;

    BeanInfoFactory beanInfoFactory;

    BeanInfo beanInfo;

    BeanClassGenerator generator;

    StringWriter stringWriter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setLocale(Locale.JAPANESE);
        setCharset(Charset.forName("UTF-8"));
        addSourcePath("aptina-beans/src/test/java", "src/test/java");
    }

    /**
     * @param compilationUnit
     * @throws Exception
     */
    void createGenerator(final Class<?> compilationUnit) throws Exception {
        addCompilationUnit(compilationUnit);
        compile();

        typeElement = getTypeElement(compilationUnit);
        beanInfoFactory = new BeanInfoFactory(getProcessingEnvironment());
        beanInfo = beanInfoFactory.createBeanInfo(typeElement);

        generator = new BeanClassGenerator(getProcessingEnvironment());
        stringWriter = new StringWriter();
        generator.writer = new PrintWriter(stringWriter);
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderFoo() throws Exception {
        createGenerator(FooBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("package org.seasar.aptina.beans.example;", reader
                .readLine());
        assertEquals("", reader.readLine());
        assertEquals("@javax.annotation.Generated(\"Aptina Beans\")", reader
                .readLine());
        assertEquals("@org.seasar.aptina.beans.JavaBean", reader.readLine());
        assertEquals(
                "public class FooBean extends org.seasar.aptina.beans.example.FooBeanState {",
                reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderBar() throws Exception {
        createGenerator(BarBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("package org.seasar.aptina.beans.example;", reader
                .readLine());
        assertEquals("", reader.readLine());
        assertEquals("@javax.annotation.Generated(\"Aptina Beans\")", reader
                .readLine());
        assertEquals("@org.seasar.aptina.beans.JavaBean", reader.readLine());
        assertEquals(
                "public class BarBean<E, T extends java.util.List<E> & java.io.Serializable> extends org.seasar.aptina.beans.example.BarBeanState<E, T> {",
                reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutClassFooter() throws Exception {
        createGenerator(FooBeanState.class);
        generator.putClassFooter(beanInfo);
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("}", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutConstructorFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final ConstructorInfo constructorInfo : beanInfo.getConstructors()) {
            generator.putConstructor(beanInfo, constructorInfo);
        }
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("    public  FooBean() {", reader.readLine());
        assertEquals("        super();", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutConstructorBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final ConstructorInfo constructorInfo : beanInfo.getConstructors()) {
            generator.putConstructor(beanInfo, constructorInfo);
        }
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("    protected  BarBean(T aaa) {", reader.readLine());
        assertEquals("        super(aaa);", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals(
                "     <S> BarBean(T[] bbb, S hoge) throws java.lang.NullPointerException {",
                reader.readLine());
        assertEquals("        super(bbb, hoge);", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals(
                "    public  BarBean(T aaa, T[] bbb, java.util.List<? extends T> ccc) {",
                reader.readLine());
        assertEquals("        super(aaa, bbb, ccc);", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutGetterFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isReadable()) {
                generator.putGetter(propertyInfo);
            }
        }
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("    /**", reader.readLine());
        assertEquals("     * aを返します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @return a", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public int getA() {", reader.readLine());
        assertEquals("        return a;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals("    /**", reader.readLine());
        assertEquals("     * bを返します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @return b", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public java.lang.String getB() {", reader.readLine());
        assertEquals("        return b;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutGetterBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isReadable()) {
                generator.putGetter(propertyInfo);
            }
        }
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("    /**", reader.readLine());
        // テストケースでは Javadoc コメントは取得できないのでフィールド名になる
        assertEquals("     * aaaを返します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @return aaa", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public T getAaa() {", reader.readLine());
        assertEquals("        return aaa;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals("    /**", reader.readLine());
        // テストケースでは Javadoc コメントは取得できないのでフィールド名になる
        assertEquals("     * bbbを返します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @return bbb", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public T[] getBbb() {", reader.readLine());
        assertEquals("        return bbb;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals("    /**", reader.readLine());
        // テストケースでは Javadoc コメントは取得できないのでフィールド名になる
        assertEquals("     * cccを返します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @return ccc", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public java.util.List<? extends T> getCcc() {",
                reader.readLine());
        assertEquals("        return ccc;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutSetterFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putSetter(propertyInfo);
            }
        }
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("    /**", reader.readLine());
        assertEquals("     * aを設定します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @param a a", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public void setA(int a) {", reader.readLine());
        assertEquals("        this.a = a;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testPutSetterBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putSetter(propertyInfo);
            }
        }
        generator.writer.close();

        final BufferedReader reader = new BufferedReader(new StringReader(
                stringWriter.toString()));
        assertEquals("    /**", reader.readLine());
        // テストケースでは Javadoc コメントは取得できないのでフィールド名になる
        assertEquals("     * aaaを設定します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @param aaa aaa", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public void setAaa(T aaa) {", reader.readLine());
        assertEquals("        this.aaa = aaa;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals("    /**", reader.readLine());
        // テストケースでは Javadoc コメントは取得できないのでフィールド名になる
        assertEquals("     * bbbを設定します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @param bbb bbb", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals("    public void setBbb(T[] bbb) {", reader.readLine());
        assertEquals("        this.bbb = bbb;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertEquals("    /**", reader.readLine());
        // テストケースでは Javadoc コメントは取得できないのでフィールド名になる
        assertEquals("     * cccを設定します。", reader.readLine());
        assertEquals("     * ", reader.readLine());
        assertEquals("     * @param ccc ccc", reader.readLine());
        assertEquals("     */", reader.readLine());
        assertEquals(
                "    public void setCcc(java.util.List<? extends T> ccc) {",
                reader.readLine());
        assertEquals("        this.ccc = ccc;", reader.readLine());
        assertEquals("    }", reader.readLine());
        assertEquals("", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * @throws Exception
     */
    public void testToQualifiedName() throws Exception {
        assertEquals(FooBeanState.class.getName(), BeanClassGenerator
                .toQualifiedName("org.seasar.aptina.beans.example",
                        "FooBeanState"));
        assertEquals("FooBeanState", BeanClassGenerator.toQualifiedName(null,
                "FooBeanState"));
    }

}
