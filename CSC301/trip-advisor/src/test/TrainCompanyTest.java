package a1;

import org.junit.Test;
import a1.TrainCompany;
import util.Constants;
import java.util.ArrayList;
import org.junit.Assert; // Try Assert Equals statements

public class TrainCompanyTest {
    // An example of how to verify that an exception is thrown
    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateCompanyWithNullName() {
        new TrainCompany(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateCompanyWithWhitespaceName() {
        new TrainCompany("        ");
    }

    @Test
    public void testCreateInstanceWithoutException() {
        new TrainCompany(" Via1 "); // Notice whitespace is ignored
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDoubleException() {
        new TrainCompany("Faster");
        new TrainCompany("Faster"); // Can't create two companies with the same name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetException() {
        TrainCompany TC1 = new TrainCompany(" Via2 "); // Notice whitespace is ignored
        TC1.setName(null);
    }

    @Test
    public void testAttributesReturned() {
        TrainCompany TC1 = new TrainCompany(" Via3      ");

        Assert.assertEquals("Via3", TC1.getName()); // Whitespace removed
        Assert.assertEquals(0, TC1.getRoutesTo(Constants.OTTAWA).size()); // No routes to or from yet

        Assert.assertEquals(0, TC1.getDirectRoutesCount()); // No Routes have been made yet
        DirectRoute dr1 = TC1.createOrUpdateDirectRoute(Constants.TORONTO, Constants.OTTAWA, 10);
        DirectRoute dr2 = TC1.createOrUpdateDirectRoute(Constants.OTTAWA, Constants.TORONTO, 15);

        Assert.assertEquals(2, TC1.getDirectRoutesCount()); // 2 Routes have been made
        DirectRoute dr3 = TC1.createOrUpdateDirectRoute(Constants.OTTAWA, Constants.MONTREAL, 20);

        Assert.assertEquals(dr1, TC1.getDirectRoute(Constants.TORONTO, Constants.OTTAWA)); // Return the specified route

        // Routes from Locations
        ArrayList<DirectRoute> testFrom = new ArrayList<DirectRoute>();
        testFrom.add(dr2);
        testFrom.add(dr3);

        // Routes to locations
        ArrayList<DirectRoute> testTo = new ArrayList<DirectRoute>();
        testTo.add(dr1);

        Assert.assertEquals(testFrom, TC1.getDirectRoutesFrom(Constants.OTTAWA + ""));
        Assert.assertEquals(testTo, TC1.getRoutesTo(Constants.OTTAWA));
        Assert.assertEquals(2, TC1.getDirectRoutesFrom(Constants.OTTAWA + "").size()); // 2 Routes to Ottawa so far
        Assert.assertEquals(1, TC1.getRoutesTo(Constants.OTTAWA).size()); // 1 Route to Ottawa added so far

        // Updating a route
        Assert.assertEquals(20, dr3.getPrice(), 0);
        TC1.getRoutesTo(Constants.OTTAWA);
        TC1.createOrUpdateDirectRoute(Constants.OTTAWA, Constants.MONTREAL, 40);
        dr3 = TC1.getDirectRoute(Constants.OTTAWA, Constants.MONTREAL);
        Assert.assertEquals(40, dr3.getPrice(), 0); // Price updated
        Assert.assertEquals(3, TC1.getDirectRoutesCount()); // No new route was added, the existing one was only updated

        DirectRoute dr4 = TC1.createOrUpdateDirectRoute(Constants.TORONTO, "Milton", 5);

        // Added a new station, so we should have 4 total stations
        Assert.assertEquals(4, TC1.getStationsCount());

        ArrayList<DirectRoute> testAll = new ArrayList<DirectRoute>();
        testAll.add(dr1);
        testAll.add(dr2);
        testAll.add(dr3);
        testAll.add(dr4);

        Assert.assertEquals(testAll, TC1.getAllDirectRoutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetArgExceptions1() {
        TrainCompany TC1 = new TrainCompany(" Via5 "); // Notice whitespace is ignored
        TC1.createOrUpdateDirectRoute("", "Station", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetArgExceptions2() {
        TrainCompany TC1 = new TrainCompany(" Via6 "); // Notice whitespace is ignored
        TC1.createOrUpdateDirectRoute("From", "To", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetArgExceptions3() {
        TrainCompany TC1 = new TrainCompany(" Via7 "); // Notice whitespace is ignored
        TC1.createOrUpdateDirectRoute("From", "To", 1);
        TC1.getDirectRoute("   ", "");
    }
}
