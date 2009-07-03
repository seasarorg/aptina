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
 * <dt>状態クラスの名前が {@code Abstract} で始まっている場合</dt>
 * <dd>状態クラスの名前の先頭から {@code Abstract} を除去した名前になります．</dd>
 * <dt>状態クラスの名前が {@code State} で終わっている場合</dt>
 * <dd>状態クラスの名前の末尾から {@code State} を除去した名前になります．</dd>
 * <dt>状態クラスの名前が {@code Bean} で終わっている場合</dt>
 * <dd>状態クラスの名前の末尾に {@code Impl} を付加した名前になります．</dd>
 * <dt>その他の場合</dt>
 * <dd>状態クラスの名前の末尾に {@code Bean} を付加した名前になります．</dd>
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
 * <td>{@code AbstractHogeBean}</td>
 * <td>{@code HogeBean}</td>
 * </tr>
 * <tr>
 * <td>{@code HogeBeanState}</td>
 * <td>{@code HogeBean}</td>
 * </tr>
 * <tr>
 * <td>{@code HogeBean}</td>
 * <td>{@code HogeBeanImpl}</td>
 * </tr>
 * <tr>
 * <td>{@code Hoge}</td>
 * <td>{@code HogeBean}</td>
 * </tr>
 * </table>
 * <h3>プロパティ</h3>
 * <p>
 * Bean クラスは， 状態クラスのフィールドに対する getter/setter メソッドを持ちます．
 * </p>
 * <h3>コンストラクタ</h3>
 * <p>
 * Bean クラスは状態クラスの非 {@code private} コンストラクタを引き継ぎます．
 * 引き継ぐことのできるコンストラクタが一つもない場合はエラーとなります．
 * </p>
 * 
 * @author koichik
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface JavaBean {

}
