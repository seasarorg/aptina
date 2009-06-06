package example.errors;

import org.seasar.aptina.beans.AccessType;
import org.seasar.aptina.beans.BeanState;
import org.seasar.aptina.beans.Property;

/**
 * 
 * @author koichik
 */
@BeanState
public class ErrorFieldBeanState {

    @SuppressWarnings("unused")
    @Property
    private final int aaa = 0;

    /** */
    @Property
    public int bbb = 0;

    @Property
    static int ccc = 0;

    @Property(access = AccessType.WRITE_ONLY)
    final int ddd = 0;

}
