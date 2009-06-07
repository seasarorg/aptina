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

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.seasar.aptina.beans.example.BarBeanState;
import org.seasar.aptina.beans.example.BazBeanState;
import org.seasar.aptina.beans.example.BoundAndConstrainedBeanState;
import org.seasar.aptina.beans.example.BoundBeanState;
import org.seasar.aptina.beans.example.ConstrainedBeanState;
import org.seasar.aptina.beans.example.FooBeanState;
import org.seasar.aptina.unit.AptinaTestCase;

/**
 * 
 * @author koichik
 */
public class BeanInfoFactoryTest extends AptinaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setLocale(Locale.JAPANESE);
        setCharset(Charset.forName("UTF-8"));
        addSourcePath("aptina-beans/src/test/java", "src/test/java");
    }

    /**
     * @throws Exception
     */
    public void testProcessTypeFoo() throws Exception {
        addCompilationUnit(FooBeanState.class);
        compile();

        final TypeElement typeElement = getTypeElement(FooBeanState.class);
        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final BeanInfo beanInfo = beanInfoFactory.processType(typeElement);

        assertEquals(FooBeanState.class.getName(), beanInfo.getStateClassName());
        assertEquals("org.seasar.aptina.beans.example", beanInfo
                .getPackageName());
        assertEquals("FooBean", beanInfo.getBeanClassName());
        assertFalse(beanInfo.isBoundProperties());
        assertFalse(beanInfo.isConstrainedProperties());
    }

    /**
     * @throws Exception
     */
    public void testProcessTypeBar() throws Exception {
        addCompilationUnit(BarBeanState.class);
        compile();

        final TypeElement typeElement = getTypeElement(BarBeanState.class);
        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final BeanInfo beanInfo = beanInfoFactory.processType(typeElement);

        assertEquals("org.seasar.aptina.beans.example.BarBeanState<E, T>",
                beanInfo.getStateClassName());
        assertEquals("org.seasar.aptina.beans.example", beanInfo
                .getPackageName());
        assertEquals("BarBean", beanInfo.getBeanClassName());
        assertEquals("<E, T extends java.util.List<E> & java.io.Serializable>",
                beanInfo.getTypeParameter());
        assertFalse(beanInfo.isBoundProperties());
        assertFalse(beanInfo.isConstrainedProperties());
    }

    /**
     * @throws Exception
     */
    public void testProcessTypeBound() throws Exception {
        addCompilationUnit(BoundBeanState.class);
        compile();

        final TypeElement typeElement = getTypeElement(BoundBeanState.class);
        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final BeanInfo beanInfo = beanInfoFactory.processType(typeElement);

        assertEquals("org.seasar.aptina.beans.example.BoundBeanState", beanInfo
                .getStateClassName());
        assertEquals("org.seasar.aptina.beans.example", beanInfo
                .getPackageName());
        assertEquals("BoundBean", beanInfo.getBeanClassName());
        assertEquals("", beanInfo.getTypeParameter());
        assertTrue(beanInfo.isBoundProperties());
        assertFalse(beanInfo.isConstrainedProperties());
    }

    /**
     * @throws Exception
     */
    public void testProcessTypeConstrained() throws Exception {
        addCompilationUnit(ConstrainedBeanState.class);
        compile();

        final TypeElement typeElement = getTypeElement(ConstrainedBeanState.class);
        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final BeanInfo beanInfo = beanInfoFactory.processType(typeElement);

        assertEquals("org.seasar.aptina.beans.example.ConstrainedBeanState",
                beanInfo.getStateClassName());
        assertEquals("org.seasar.aptina.beans.example", beanInfo
                .getPackageName());
        assertEquals("ConstrainedBean", beanInfo.getBeanClassName());
        assertEquals("", beanInfo.getTypeParameter());
        assertFalse(beanInfo.isBoundProperties());
        assertTrue(beanInfo.isConstrainedProperties());
    }

    /**
     * @throws Exception
     */
    public void testProcessTypeBoundAndConstrained() throws Exception {
        addCompilationUnit(BoundAndConstrainedBeanState.class);
        compile();

        final TypeElement typeElement = getTypeElement(BoundAndConstrainedBeanState.class);
        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final BeanInfo beanInfo = beanInfoFactory.processType(typeElement);

        assertEquals(
                "org.seasar.aptina.beans.example.BoundAndConstrainedBeanState",
                beanInfo.getStateClassName());
        assertEquals("org.seasar.aptina.beans.example", beanInfo
                .getPackageName());
        assertEquals("BoundAndConstrainedBean", beanInfo.getBeanClassName());
        assertEquals("", beanInfo.getTypeParameter());
        assertTrue(beanInfo.isBoundProperties());
        assertTrue(beanInfo.isConstrainedProperties());
    }

    /**
     * @throws Exception
     */
    public void testProcessFieldFoo() throws Exception {
        addCompilationUnit(FooBeanState.class);
        compile();

        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final TypeElement typeElement = getTypeElement(FooBeanState.class);
        assertNotNull(typeElement);

        PropertyInfo propertyInfo = beanInfoFactory
                .processField(getFieldElement(typeElement, "a"));
        assertNotNull(propertyInfo);
        assertEquals("a", propertyInfo.getName());
        assertEquals("int", propertyInfo.getType());
        assertFalse(propertyInfo.isArray());
        assertTrue(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());

        propertyInfo = beanInfoFactory.processField(getFieldElement(
                typeElement, "b"));
        assertNotNull(propertyInfo);
        assertEquals("b", propertyInfo.getName());
        assertEquals("java.lang.String", propertyInfo.getType());
        assertFalse(propertyInfo.isArray());
        assertTrue(propertyInfo.isReadable());
        assertFalse(propertyInfo.isWritable());

        assertNull(beanInfoFactory.processField(getFieldElement(typeElement,
                "c")));
        assertNull(beanInfoFactory.processField(getFieldElement(typeElement,
                "d")));
        assertNull(beanInfoFactory.processField(getFieldElement(typeElement,
                "e")));
    }

    /**
     * @throws Exception
     */
    public void testProcessFieldBar() throws Exception {
        addCompilationUnit(BarBeanState.class);
        compile();

        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final TypeElement typeElement = getTypeElement(BarBeanState.class);

        assertNotNull(typeElement);
        PropertyInfo propertyInfo = beanInfoFactory
                .processField(getFieldElement(typeElement, "aaa"));
        assertNotNull(propertyInfo);
        assertEquals("aaa", propertyInfo.getName());
        assertEquals("T", propertyInfo.getType());
        assertFalse(propertyInfo.isArray());
        assertTrue(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());

        propertyInfo = beanInfoFactory.processField(getFieldElement(
                typeElement, "bbb"));
        assertNotNull(propertyInfo);
        assertEquals("bbb", propertyInfo.getName());
        assertEquals("T[]", propertyInfo.getType());
        assertTrue(propertyInfo.isArray());
        assertEquals("T", propertyInfo.getComponentType());
        assertTrue(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());

        propertyInfo = beanInfoFactory.processField(getFieldElement(
                typeElement, "ccc"));
        assertNotNull(propertyInfo);
        assertEquals("ccc", propertyInfo.getName());
        assertEquals("java.util.List<? extends T>", propertyInfo.getType());
        assertFalse(propertyInfo.isArray());
        assertTrue(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());
    }

    /**
     * @throws Exception
     */
    public void testProcessConstructorFoo() throws Exception {
        addCompilationUnit(FooBeanState.class);
        compile();

        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final TypeElement typeElement = getTypeElement(FooBeanState.class);

        final ExecutableElement constructor = getConstructorElement(typeElement);
        final ConstructorInfo constructorInfo = beanInfoFactory
                .processConstructor(constructor);
        assertNotNull(constructorInfo);
        assertEquals("public", constructorInfo.getModifier());
        assertEquals("", constructorInfo.getTypeParameters());
        assertTrue(constructorInfo.getParameterNames().isEmpty());
        assertTrue(constructorInfo.getThrownTypes().isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testProcessConstructorBar() throws Exception {
        addCompilationUnit(BarBeanState.class);
        compile();

        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final TypeElement typeElement = getTypeElement(BarBeanState.class);

        ExecutableElement constructor = getConstructorElement(typeElement);
        ConstructorInfo constructorInfo = beanInfoFactory
                .processConstructor(constructor);
        assertNull(constructorInfo);

        constructor = getConstructorElement(typeElement, "T");
        constructorInfo = beanInfoFactory.processConstructor(constructor);
        assertEquals("protected", constructorInfo.getModifier());
        assertEquals("", constructorInfo.getTypeParameters());
        List<String> parameterNames = constructorInfo.getParameterNames();
        assertEquals(1, parameterNames.size());
        assertEquals("aaa", parameterNames.get(0));
        assertEquals("T", constructorInfo.getParameterTypes().get(0));

        constructor = getConstructorElement(typeElement, "T[]", "S");
        constructorInfo = beanInfoFactory.processConstructor(constructor);
        assertEquals("", constructorInfo.getModifier());
        assertEquals("<S>", constructorInfo.getTypeParameters());
        parameterNames = constructorInfo.getParameterNames();
        assertEquals(2, parameterNames.size());
        assertEquals("bbb", parameterNames.get(0));
        assertEquals("T[]", constructorInfo.getParameterTypes().get(0));
        assertEquals("hoge", parameterNames.get(1));
        assertEquals("S", constructorInfo.getParameterTypes().get(1));

        constructor = getConstructorElement(typeElement, "T", "T[]",
                "java.util.List<? extends T>");
        constructorInfo = beanInfoFactory.processConstructor(constructor);
        assertEquals("public", constructorInfo.getModifier());
        parameterNames = constructorInfo.getParameterNames();
        assertEquals(3, parameterNames.size());
        assertEquals("aaa", parameterNames.get(0));
        assertEquals("T", constructorInfo.getParameterTypes().get(0));
        assertEquals("bbb", parameterNames.get(1));
        assertEquals("T[]", constructorInfo.getParameterTypes().get(1));
        assertEquals("ccc", parameterNames.get(2));
        assertEquals("java.util.List<? extends T>", constructorInfo
                .getParameterTypes().get(2));
    }

    /**
     * @throws Exception
     */
    public void testProcessMethodBaz() throws Exception {
        addCompilationUnit(BazBeanState.class);
        compile();

        final BeanInfoFactory beanInfoFactory = new BeanInfoFactory(
                getProcessingEnvironment());
        final TypeElement typeElement = getTypeElement(BazBeanState.class);
        final BeanInfo beanInfo = beanInfoFactory.createBeanInfo(typeElement);

        PropertyInfo propertyInfo = beanInfo.getPropertyInfo("a");
        assertTrue(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());

        propertyInfo = beanInfo.getPropertyInfo("b");
        assertFalse(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());

        propertyInfo = beanInfo.getPropertyInfo("c");
        assertFalse(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());

        propertyInfo = beanInfo.getPropertyInfo("d");
        assertTrue(propertyInfo.isReadable());
        assertFalse(propertyInfo.isWritable());

        propertyInfo = beanInfo.getPropertyInfo("e");
        assertFalse(propertyInfo.isReadable());
        assertFalse(propertyInfo.isWritable());

        propertyInfo = beanInfo.getPropertyInfo("f");
        assertTrue(propertyInfo.isReadable());
        assertTrue(propertyInfo.isWritable());
    }

    /**
     * @throws Exception
     */
    public void testToPackageName() throws Exception {
        assertEquals("org.seasar.aptina.beans.example", BeanInfoFactory
                .toPackageName(FooBeanState.class.getName()));
    }

    /**
     * @throws Exception
     */
    public void testToBeanClassName() throws Exception {
        assertEquals("HogeBean", BeanInfoFactory
                .toBeanClassName("AbstractHogeBean"));
        assertEquals("HogeBean", BeanInfoFactory
                .toBeanClassName("HogeBeanState"));
        assertEquals("HogeBeanImpl", BeanInfoFactory
                .toBeanClassName("HogeBean"));
        assertEquals("HogeBean", BeanInfoFactory.toBeanClassName("Hoge"));
    }

    /**
     * @throws Exception
     */
    public void testToStringTypeParameterypeParameterDecl() throws Exception {
        addCompilationUnit(FooBeanState.class);
        addCompilationUnit(BarBeanState.class);
        compile();

        TypeElement typeElement = getTypeElement(FooBeanState.class);
        assertEquals("", BeanInfoFactory.toStringTypeParameterDecl(typeElement
                .getTypeParameters()));

        typeElement = getTypeElement(BarBeanState.class);
        assertEquals("<E, T extends java.util.List<E> & java.io.Serializable>",
                BeanInfoFactory.toStringTypeParameterDecl(typeElement
                        .getTypeParameters()));
    }

}
