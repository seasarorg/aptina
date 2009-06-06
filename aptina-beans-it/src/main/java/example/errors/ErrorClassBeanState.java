package example.errors;

import org.seasar.aptina.beans.BeanState;

/**
 * 
 * @author koichik
 */
public class ErrorClassBeanState {

    /**
     * 
     */
    @BeanState
    public static class NestedClass {
    }

    /**
     * 
     */
    @BeanState
    public class InnerClass {
    }

    /**
     * 
     */
    public void foo() {
        /**
         * 
         */
        @SuppressWarnings("unused")
        @BeanState
        class LocalClass {

        }
    }
}

/**
 * 
 */
@BeanState
class NonpublicClass {

    String aaa;
}
