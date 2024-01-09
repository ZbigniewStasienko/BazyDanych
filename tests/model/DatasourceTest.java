package model;

import junit.framework.TestCase;

public class DatasourceTest extends TestCase {

    Datasource datasource = new Datasource();

    // Przecinek w statystykach
    public void testAddingTransaction1() {
        datasource.open();
        boolean result = datasource.addingTransaction("Tyler", "Johnson", "PG", 1, "43,1", "1", "1");
        assertTrue(result);
        }
    // Jedna niepoprawna(ujemna) wartość w statystykach
    public void testAddingTransaction2() {
        datasource.open();
        boolean result = datasource.addingTransaction("Tyler", "Herro", "SF", 6, "43,1", "-1", "1");
        assertFalse(result);
    }

    // Jedna niepoprawna (litera) wartość w statystykach
    public void testAddingTransaction3() {
        datasource.open();
        boolean result = datasource.addingTransaction("Tyler", "Knocks", "SG", 7, "43,1a", "1", "1");
        assertFalse(result);
    }

    // Aktualizowanie statystyk dla ujemnych wartości
    public void testUpdateStats1() {
        datasource.open();
        int result = datasource.updateStats("-3", "1", "3", 1);
        assertEquals(-1, result);
    }
    // Aktualizowanie dla statystyk z przecinkiem
    public void testUpdateStats2() {
        datasource.open();
        int result = datasource.updateStats("3,7", "1", "3", 1);
        assertEquals(1, result);
    }

    // Aktualizowanie dla statystyk z literą
    public void testUpdateStats3() {
        datasource.open();
        int result = datasource.updateStats("3.7", "1a", "3", 1);
        assertEquals(-1, result);
    }
}