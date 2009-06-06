package example;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class FooTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        final FooBean fooBean = new FooBean();
        fooBean.setAaa(100);
        assertEquals(100, fooBean.getAaa());
    }

}
