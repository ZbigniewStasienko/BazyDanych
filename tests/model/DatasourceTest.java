package model;

import junit.framework.TestCase;

public class DatasourceTest extends TestCase {

    Datasource datasource = new Datasource();
    public void testAddingTransaction() {
        datasource.open();
        boolean result = datasource.addingTransaction("Tyler", "Johnson", "PG", 1, "43.1", "1", "1");
        assertTrue(result);
        }

    public void testUpdateStats() {
        datasource.open();
        int result = datasource.updateStats("-3", "1", "3", 1);
        assertEquals(-1, result);
    }
}