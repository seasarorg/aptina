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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.seasar.aptina.commons.util.CollectionUtils.*;

/**
 * 生成する Bean クラスの情報を保持するクラスです．
 * 
 * @author koichik
 */
public class BeanInfo {

    /** Javadoc コメント */
    protected String comment;

    /** パッケージ名 */
    protected String packageName;

    /** Bean クラスの単純名 */
    protected String beanClassName;

    /** 型引数 */
    protected String typeParameter;

    /** 状態クラスの完全限定名 */
    protected String stateClassName;

    /** bound プロパティをサポートする場合は {@literal true} */
    protected boolean boundProperties;

    /** constrained プロパティをサポートする場合は {@literal true} */
    protected boolean constrainedProperties;

    /** プロパティ情報の {@link Map} */
    protected final Map<String, PropertyInfo> properties = new LinkedHashMap<String, PropertyInfo>();

    /** コンストラクタ情報の {@link List} */
    protected final List<ConstructorInfo> constructors = newArrayList();

    /**
     * インスタンスを構築します．
     */
    public BeanInfo() {
    }

    /**
     * Javadoc コメントを返します．
     * 
     * @return Javadoc コメント
     */
    public String getComment() {
        return comment;
    }

    /**
     * Javadoc コメントを設定します．
     * 
     * @param comment
     *            Javadoc コメント
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * パッケージ名を返します．
     * 
     * @return パッケージ名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * パッケージ名を設定します．
     * 
     * @param packageName
     *            パッケージ名
     */
    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    /**
     * Bean クラスの単純名を返します．
     * 
     * @return Bean クラスの単純名
     */
    public String getBeanClassName() {
        return beanClassName;
    }

    /**
     * Bean クラスの単純名を設定します．
     * 
     * @param beanClassName
     *            Bean クラスの単純名
     */
    public void setBeanClassName(final String beanClassName) {
        this.beanClassName = beanClassName;
    }

    /**
     * 型引数を返します．
     * 
     * @return 型引数
     */
    public String getTypeParameter() {
        return typeParameter;
    }

    /**
     * 型引数を設定します．
     * 
     * @param typeParameter
     *            型引数
     */
    public void setTypeParameter(final String typeParameter) {
        this.typeParameter = typeParameter;
    }

    /**
     * 状態クラスの完全限定名を返します．
     * 
     * @return 状態クラスの完全限定名
     */
    public String getStateClassName() {
        return stateClassName;
    }

    /**
     * 状態クラスの完全限定名を設定します．
     * 
     * @param stateClassName
     *            状態クラスの完全限定名
     */
    public void setStateClassName(final String stateClassName) {
        this.stateClassName = stateClassName;
    }

    /**
     * bound プロパティをサポートする場合は {@literal true} を返します．
     * 
     * @return bound プロパティをサポートする場合は {@literal true}
     */
    public boolean isBoundProperties() {
        return boundProperties;
    }

    /**
     * bound プロパティをサポートする場合は {@literal true} を設定します．
     * 
     * @param boundProperties
     *            bound プロパティをサポートする場合は {@literal true}
     */
    public void setBoundProperties(final boolean boundProperties) {
        this.boundProperties = boundProperties;
    }

    /**
     * constrained プロパティをサポートする場合は {@literal true} を返します．
     * 
     * @return constrained プロパティをサポートする場合は {@literal true}
     */
    public boolean isConstrainedProperties() {
        return constrainedProperties;
    }

    /**
     * constrained プロパティをサポートする場合は {@literal true} を設定します．
     * 
     * @param constrainedProperties
     *            constrained プロパティをサポートする場合は {@literal true}
     */
    public void setConstrainedProperties(final boolean constrainedProperties) {
        this.constrainedProperties = constrainedProperties;
    }

    /**
     * プロパティ名の {@link Set} を返します．
     * 
     * @return プロパティ名の {@link Set}
     */
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    /**
     * 指定されたプロパティの情報を返します．
     * 
     * @param propertyName
     *            プロパティ名
     * @return 指定されたプロパティの情報
     */
    public PropertyInfo getPropertyInfo(final String propertyName) {
        return properties.get(propertyName);
    }

    /**
     * プロパティ情報を追加します．
     * 
     * @param propertyInfo
     *            プロパティ情報
     */
    public void addPropertyInfo(final PropertyInfo propertyInfo) {
        properties.put(propertyInfo.getName(), propertyInfo);
    }

    /**
     * コンストラクタ情報を返します．
     * 
     * @return コンストラクタ情報
     */
    public List<ConstructorInfo> getConstructors() {
        return constructors;
    }

    /**
     * コンストラクタ情報を追加します．
     * 
     * @param constructorInfo
     *            コンストラクタ情報
     */
    public void addConstructor(final ConstructorInfo constructorInfo) {
        constructors.add(constructorInfo);
    }

}
