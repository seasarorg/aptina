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
 * <li>通常のクラスであること (インタフェースやアノテーション，列挙は状態クラスにできません)．</li>
 * <li>トップレベルのクラスであること (ネストしたクラスは状態クラスにできません)．</li>
 * <li>{@literal public} なクラスであること．</li>
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
 * <p>
 * 状態クラスのプロパティが配列型の場合， Bean クラスにはインデックス付きの getter/setter メソッドが生成されます．
 * </p>
 * <p>
 * 状態クラスのフィールドに {@link Property} アノテーションを付与し， {@link Property#access} 要素で
 * getter/setter をどのように生成するか指定することができます． {@link Property#access} 要素の型は
 * {@link AccessType} です．
 * </p>
 * <table border="1">
 * <tr>
 * <th>
 * {@link AccessType} の値</th>
 * <th>説明</th>
 * </tr>
 * <tr>
 * <td>{@link AccessType#NONE}</td>
 * <td>プロパティとしてアクセスしません (getter/setter とも生成されません)．</td>
 * </tr>
 * <tr>
 * <td>{@link AccessType#READ_ONLY}</td>
 * <td>参照のみ可能なプロパティです (getter のみ生成されます)．</td>
 * </tr>
 * <tr>
 * <td>{@link AccessType#WRITE_ONLY}</td>
 * <td>
 * 変更のみ可能なプロパティです (setter のみ生成されます)． フィールドが {@literal final} の場合はエラーになります．</td>
 * </tr>
 * <tr>
 * <td>
 * {@link AccessType#READ_WRITE}<br />
 * (デフォルト)</td>
 * <td>
 * 参照・変更とも可能なプロパティです (getter/setter とも生成されます)． フィールドが {@literal final} の場合，
 * setter は生成されません．</td>
 * </tr>
 * </table>
 * <h3>コンストラクタ</h3>
 * <p>
 * 状態クラスの非 {@literal private} コンストラクタは Bean クラスに引き継がれます．
 * 引き継がれるコンストラクタが一つもない場合はエラーとなります．
 * </p>
 * 
 * @author koichik
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface BeanState {

}
