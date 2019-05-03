

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MyTrieTest {

    @Test
    void testMyTrie() {
	fail("Not yet implemented");
    }

    @Test
    void testSize() {
	fail("Not yet implemented");
    }

    @Test
    void testContainsEmptyString() {
	fail("Not yet implemented");
    }

    @Test
    void testContains() {
	MyTrie test = new MyTrie();
	assertTrue(test.add("love"));
	assertTrue(test.contains("love"));
    }

    @Test
    void testContainsPrefix() {
	fail("Not yet implemented");
    }

    @Test
    void testAdd() {
	MyTrie test = new MyTrie();
	assertTrue(test.add("love"));
	assertEquals(1,test.size());
	assertFalse(test.add("love"));
	assertTrue(test.add("who"));
	assertEquals(2,test.size());
	assertTrue(test.add("lofi"));
	assertEquals(3,test.size());
    }

    @Test
    void testIsEmpty() {
	fail("Not yet implemented");
    }

    @Test
    void testToString() {
	fail("Not yet implemented");
    }

    @Test
    void testIterator() {
	fail("Not yet implemented");
    }

}
