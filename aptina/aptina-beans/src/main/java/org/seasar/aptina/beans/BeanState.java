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
package org.seasar.aptina.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.aptina.beans.internal.BeansProcessor;

/**
 * JavaBean の状態を保持するクラスであることを示す注釈です．
 * <p>
 * Aptina Beans の提供する Annotation Processor ({@link BeansProcessor}) は，
 * この注釈が付けられたクラス (以下「状態クラス」) のサブクラス (以下「Bean クラス」) を生成します． Bean
 * クラスは，状態クラスに定義されたフィールドに対する getter/setter メソッドを持つ JavaBeans です．
 * </p>
 * <p>
 * この注釈が付けられる状態クラスは次のようなクラスでなければなりません．
 * </p>
 * <ul>
 * <li>トップレベルのクラスであること．</li>
 * <li>{@literal final} クラスではないこと．</li>
 * </ul>
 * <h3>プロパティ</h3>
 * <p>
 * 状態クラスの次の条件を満たすフィールドが Bean クラスのプロパティとなります．
 * </p>
 * <ul>
 * <li>{@literal static}，{@literal private}，{@literal public} の修飾子が付けられていないこと．</li>
 * <li>{@link Property} アノテーションで {@link AccessType#NONE} が指定されていないこと．</li>
 * </ul>
 * <h3>コンストラクタ</h3>
 * <p>
 * 状態クラスの非 {@literal private} コンストラクタは Bean クラスに引き継がれます．
 * </p>
 * 
 * @author koichik
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface BeanState {

}
