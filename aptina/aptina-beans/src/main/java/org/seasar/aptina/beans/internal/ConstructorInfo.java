package org.seasar.aptina.beans.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

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

/**
 * 生成するコンストラクタの情報を保持するクラスです．
 * 
 * @author koichik
 */
public class ConstructorInfo {

    /** Javadoc コメント */
    protected String comment;

    /** 修飾子 */
    protected final Set<Modifier> modifiers = new HashSet<Modifier>();

    /** 型引数 */
    protected String typeParameters;

    /** 引数型の {@link List} */
    protected final List<String> parameterTypes = new ArrayList<String>();

    /** 引き数名の {@link List} */
    protected final List<String> parameterNames = new ArrayList<String>();

    /** 例外型の {@link List} */
    protected final List<String> thrownTypes = new ArrayList<String>();

    /**
     * インスタンスを構築します．
     */
    public ConstructorInfo() {
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
     * {@link Modifier 修飾子} の {@link Set} を返します．
     * 
     * @return {@link Modifier 修飾子} の {@link Set}
     */
    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    /**
     * {@link Modifier 修飾子} の {@link Set} を設定します．
     * 
     * @param modifiers
     *            {@link Modifier 修飾子} の {@link Set}
     */
    public void addModifiers(final Collection<? extends Modifier> modifiers) {
        this.modifiers.addAll(modifiers);
    }

    /**
     * 型引数を返します．
     * 
     * @return 型引数
     */
    public String getTypeParameters() {
        return typeParameters;
    }

    /**
     * 型引数を設定します．
     * 
     * @param typeParameters
     *            型引数
     */
    public void setTypeParameters(final String typeParameters) {
        this.typeParameters = typeParameters;
    }

    /**
     * パラメータ型の {@link List} を返します．
     * 
     * @return パラメータ型の {@link List}
     */
    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    /**
     * パラメータ型を追加します．
     * 
     * @param parameterType
     *            パラメータ型
     */
    public void addParameterType(final String parameterType) {
        parameterTypes.add(parameterType);
    }

    /**
     * パラメータ名の {@link List} を返します．
     * 
     * @return パラメータ名の {@link List}
     */
    public List<String> getParameterNames() {
        return parameterNames;
    }

    /**
     * パラメータ名を追加します．
     * 
     * @param parameterName
     *            パラメータ名
     */
    public void addParameterName(final String parameterName) {
        parameterNames.add(parameterName);
    }

    /**
     * 例外型の {@link List} を返します．
     * 
     * @return 例外型の {@link List}
     */
    public List<String> getThrownTypes() {
        return thrownTypes;
    }

    /**
     * 例外型を追加します．
     * 
     * @param thrownType
     *            例外型
     */
    public void addThrownType(final String thrownType) {
        thrownTypes.add(thrownType);
    }

}
