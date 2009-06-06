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

/**
 * Aptina Beans によって {@link BeanState} で注釈されたクラスから生成された JavaBeans であることを示す注釈です．
 * <p>
 * 生成される Bean クラスは状態クラスと同じパッケージに生成されます． Bean クラスの名前は次のようになります．
 * </p>
 * <dl>
 * <dt>状態クラスの名前が <code>Abstract</code> で始まっている場合</dt>
 * <dd>状態クラスの名前の先頭から <code>Abstract</code> を除去した名前になります．</dd>
 * <dt>状態クラスの名前が <code>State</code> で終わっている場合</dt>
 * <dd>状態クラスの名前の末尾から <code>State</code> を除去した名前になります．</dd>
 * <dt>状態クラスの名前が <code>Bean</code> で終わっている場合</dt>
 * <dd>状態クラスの名前の末尾に <code>Impl</code> を付加した名前になります．</dd>
 * <dt>その他の場合</dt>
 * <dd>状態クラスの名前の末尾に <code>Bean</code> を付加した名前になります．</dd>
 * </dl>
 * <p>
 * 例
 * </p>
 * <table border="1">
 * <tr>
 * <th>状態クラスの名前</th>
 * <th>生成される Bean クラスの名前</th>
 * </tr>
 * <tr>
 * <td><code>AbstractHogeBean</code></td>
 * <td><code>HogeBean</code></td>
 * </tr>
 * <tr>
 * <td><code>HogeBeanState</code></td>
 * <td><code>HogeBean</code></td>
 * </tr>
 * <tr>
 * <td><code>HogeBean</code></td>
 * <td><code>HogeBeanImpl</code></td>
 * </tr>
 * <tr>
 * <td><code>Hoge</code></td>
 * <td><code>HogeBean</code></td>
 * </tr>
 * </table>
 * <h3>プロパティ</h3>
 * <p>
 * Bean クラスは， 状態クラスの次の条件を満たすフィールドに対する getter/setter メソッドを持ちます．
 * </p>
 * <ul>
 * <li>{@literal static}，{@literal private}，{@literal public} の修飾子が付けられていないこと．</li>
 * <li>{@link Property} アノテーションで {@link AccessType#NONE} が指定されていないこと．</li>
 * </ul>
 * <h3>コンストラクタ</h3>
 * <p>
 * Bean クラスは状態クラスの非 {@literal private} コンストラクタを引き継ぎます．
 * </p>
 * 
 * @author koichik
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface JavaBean {

}
