package a1;

import org.junit.Test;
import util.Constants;
import org.junit.Assert; // Try Assert Equals statements

public class DirectRouteTest {
    // A very basic example of a passing test
    @Test
    public void testCreateInstanceWithoutException() {
        new DirectRoute(new TrainCompany("ViaA"), Constants.TORONTO, Constants.OTTAWA, 37.5);
    }

    // A very basic example of a failing test where TrainCompany is null
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceWithNullException() {
        new DirectRoute(null, Constants.TORONTO, Constants.OTTAWA, 37.5);
    }

    // A very basic example of a failing test where a station contains no
    // non-whitespace characters
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceWithNonWhiteSpaceException() {
        new DirectRoute(new TrainCompany("ViaB"), "", Constants.OTTAWA, 37.5);
    }

    // A very basic example of a failing test where price < 0
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceWithPriceException() {
        new DirectRoute(new TrainCompany("ViaC"), Constants.TORONTO, Constants.OTTAWA, -1);
    }

    // Testing of setting and getting values
    @Test
    public void testAttributesReturned() {
        DirectRoute testRoute = new DirectRoute(new TrainCompany(" ViaD "), Constants.TORONTO, Constants.OTTAWA, 37.5);

        // Getters
        Assert.assertEquals("ViaD", testRoute.getTrainCompany().getName());
        Assert.assertEquals(Constants.TORONTO, testRoute.getFromStation());
        Assert.assertEquals(Constants.OTTAWA, testRoute.getToStation());
        Assert.assertEquals(37.5, testRoute.getPrice(), 0);

        // Setters
        testRoute.setFromStation(Constants.MONTREAL);
        Assert.assertEquals("Station name should change", Constants.MONTREAL, testRoute.getFromStation());

        TrainCompany testTC = new TrainCompany("Fast Rail");
        testRoute.setTrainCompany(testTC);
        Assert.assertEquals("Company name should change", testTC.getName(), testRoute.getTrainCompany().getName());
    }

    // Testing of two routes to see if they differ or not
    @Test
    public void testEquality() {
        TrainCompany via = new TrainCompany("ViaE");
        DirectRoute testRoute = new DirectRoute(via, Constants.TORONTO, Constants.OTTAWA, 37.5);
        DirectRoute equalRoutes = new DirectRoute(via, Constants.TORONTO, Constants.OTTAWA, 37.5);

        Assert.assertEquals("Routes should be the same", true, testRoute.equals(equalRoutes));

        Assert.assertEquals("null is never the same", false, testRoute.equals(null));

        equalRoutes.setFromStation(Constants.MONTREAL);
        Assert.assertEquals("FromStation is different, so they are unequal", false, testRoute.equals(equalRoutes));

        equalRoutes.setFromStation(Constants.TORONTO);
        Assert.assertEquals("FromStation is changed back properly, so equality should hold", true,
                testRoute.equals(equalRoutes));
    }
}
