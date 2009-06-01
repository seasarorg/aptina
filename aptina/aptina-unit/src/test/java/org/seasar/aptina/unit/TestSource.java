package org.seasar.aptina.unit;

/**
 * 
 * @author koichik
 */
@Hoge
public class TestSource {

    int aaa;

    String[] bbb;

    TestSource() {
    }

    TestSource(final int aaa) {
        this.aaa = aaa;
    }

    TestSource(final String... bbb) {
        this.bbb = bbb;
    }

    void hoge() {
    }

    void setAaa(final int aaa) {
        this.aaa = aaa;
    }

    void setBbb(final String... bbb) {
        this.bbb = bbb;
    }
}
