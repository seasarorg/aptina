package example;

import org.seasar.aptina.beans.BeanState;

/**
 * Baz な JavaBeans。
 * 
 * @author koichik
 * @param <T>
 *            何かの型
 */
@BeanState
public class BazBeanState<T> {

    int aaa;

    T bbb;

    /**
     * インスタンスを構築します。
     */
    public BazBeanState() {
    }

}
