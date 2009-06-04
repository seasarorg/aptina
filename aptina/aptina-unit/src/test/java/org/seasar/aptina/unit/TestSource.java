package org.seasar.aptina.unit;

import java.util.List;

/**
 * 
 * @author koichik
 * @param <T>
 */
@Hoge
public class TestSource<T> {

    int aaa;

    String[] bbb;

    List<T> ccc;

    TestSource() {
    }

    TestSource(final int aaa) {
        this.aaa = aaa;
    }

    TestSource(final String... bbb) {
        this.bbb = bbb;
    }

    TestSource(final List<T> ccc) {
        this.ccc = ccc;
    }

    void hoge() {
    }

    void setAaa(final int aaa) {
        this.aaa = aaa;
    }

    void setBbb(final String... bbb) {
        this.bbb = bbb;
    }

    void setCcc(final List<T> ccc) {
        this.ccc = ccc;
    }

}
