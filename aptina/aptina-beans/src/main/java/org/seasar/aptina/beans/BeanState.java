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
 * Aptina Beans の提供する Annotation Processor ({@link BeansProcessor})は，
 * この注釈が付けられたクラス (以下「状態クラス」) のサブクラス (以下「Bean クラス」) を生成します． Bean
 * クラスは，状態クラスに定義されたフィールドに対する getter/setter メソッドを持つ JavaBeans です．
 * </p>
 * 
 * <p>
 * この注釈が付けられる状態クラスは次のようなクラスでなければなりません．
 * </p>
 * <ul>
 * <li>トップレベルのクラスであること．</li>
 * <li>{@literal final} クラスではないこと．</li>
 * </ul>
 * 
 * <p>
 * 生成される Bean クラスの名前は次のようになります．
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
 * <td>生成される Beans クラスの名前</th>
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
 * 
 * <p>
 * 状態クラスの次の条件を満たすフィールドが Bean クラスのプロパティとなります．
 * </p>
 * <ul>
 * <li>{@literal static}，{@literal private}，{@literal public} の修飾子が付けられていないこと．</li>
 * <li>{@link Property} アノテーションで {@link AccessType#NONE} が指定されていないこと．</li>
 * </ul>
 * 
 * <p>
 * 状態クラスの非 {@literal private}
 * </p>
 * 
 * <p>
 * 次に状態クラスの例を示します．
 * </p>
 * 
 * <pre>
 * @BeanState
 * public abstract class FooBeanState {
 *     private String aaa;   // private なので getter/setter とも生成されません
 *     final String bbb;     // final なので getter メソッドのみ生成されます
 *     protected String ccc; // getter/setter とも生成されます
 *     public String ddd;    // public なので getter/setter とも生成されません
 *     @Property(access=AccessType.NONE)
 *     public String eee;    // AccessType.NONE なので getter/setter とも生成されません
 *     @Property(access=AccessType.WRITE_ONLY)
 *     public String fff;    // AccessType.WRITE_ONLY なので setter メソッドのみ生成されます
 *     static String ggg;    // static なので getter/setter とも生成されません
 * }
 * </pre>
 * <p>
 * {@link BeansProcessor} は， 上記の状態クラスから次の Bean クラスが生成します．
 * </p>
 * 
 * <pre>
 * @JavaBeans
 * public class FooBean extends FooBeanState {
 *     public FooBean() {
 *         super();
 *     }
 *     public String getBbb() {
 *         return bbb;
 *     }
 *     public String getCcc() {
 *         return ccc;
 *     }
 *     public void setCcc(String ccc) {
 *         this.ccc = ccc;
 *     }
 *     public void setFff(String fff) {
 *         this.fff = fff;
 *     }
 * }
 * </pre>
 * 
 * @author koichik
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface BeanState {

}
