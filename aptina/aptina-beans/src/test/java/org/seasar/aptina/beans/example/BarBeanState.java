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
package org.seasar.aptina.beans.example;

import java.io.Serializable;
import java.util.List;

import org.seasar.aptina.beans.BeanState;

/**
 * テスト
 * 
 * @author koichik
 * @param <E>
 * @param <T>
 */
@BeanState
public class BarBeanState<E, T extends List<E> & Serializable> {

    /**
     * あああ
     */
    protected T aaa;

    /**
     * いいい
     */
    T[] bbb;

    /**
     * ううう
     */
    List<? extends T> ccc;

    /**
     * インスタンスを構築します。
     */
    @SuppressWarnings("unused")
    private BarBeanState() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param aaa
     *            あああ
     */
    protected BarBeanState(final T aaa) {
        this.aaa = aaa;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param <S>
     *            いいいの型
     * @param bbb
     *            いいい
     * @param hoge
     *            ほげ
     * @throws NullPointerException
     *             何かが{@literal null}だった場合
     */
    <S> BarBeanState(final T[] bbb, final S hoge) throws NullPointerException {
        this.bbb = bbb;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param aaa
     *            あああ
     * @param bbb
     *            いいい
     * @param ccc
     *            ううう
     */
    public BarBeanState(final T aaa, final T[] bbb, final List<? extends T> ccc) {
        this.aaa = aaa;
        this.bbb = bbb;
        this.ccc = ccc;
    }

}
