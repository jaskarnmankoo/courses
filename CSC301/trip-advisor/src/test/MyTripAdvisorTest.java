package a1;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.List;
import org.junit.Before;
import util.Constants;
import util.Utils;

public class MyTripAdvisorTest {
    private TrainCompany fastTrain, swiftRail, JaskarnTrainA, JuwonRailA, JaskarnTrainB, JuwonRailB, JaskarnTrainC,
            JuwonRailC;

    @Before
    public void setUp() throws Exception {
        // Create TrainCompany instances from data files in the resources folder.
        fastTrain = Utils.createCompanyFromDataFile("resources/FastTrain.txt");
        swiftRail = Utils.createCompanyFromDataFile("resources/SwiftRail.txt");
        JaskarnTrainA = Utils.createCompanyFromDataFile("resources/JaskarnTrainA.txt");
        JuwonRailA = Utils.createCompanyFromDataFile("resources/JuwonRailA.txt");
        JaskarnTrainB = Utils.createCompanyFromDataFile("resources/JaskarnTrainB.txt");
        JuwonRailB = Utils.createCompanyFromDataFile("resources/JuwonRailB.txt");
        JaskarnTrainC = Utils.createCompanyFromDataFile("resources/JaskarnTrainC.txt");
        JuwonRailC = Utils.createCompanyFromDataFile("resources/JuwonRailC.txt");
    }

    @Test(timeout = 3000)
    public void twoRouteTripWhereRoutesAreFromDifferentCompanies() {
        MyTripAdvisor advisor = new MyTripAdvisor();
        advisor.addTrainCompany(fastTrain);
        advisor.addTrainCompany(swiftRail);

        List<DirectRoute> trip = advisor.getCheapestTrip(Constants.TORONTO, Constants.MONTREAL);

        if (trip == null) {
            System.out.println("null");
        }

        // Make sure that we got the route we expect
        assertEquals(2, trip.size());
        assertEquals(new DirectRoute(swiftRail, Constants.TORONTO, Constants.OTTAWA, 30), trip.get(0));
        assertEquals(new DirectRoute(fastTrain, Constants.OTTAWA, Constants.MONTREAL, 25), trip.get(1));
        assertTrue(55 == advisor.getCheapestPrice(Constants.TORONTO, Constants.MONTREAL));
    }

    @Test(timeout = 3000)
    public void twoRouteTripWhereRoutesAreFromSameCompanies() {
        MyTripAdvisor advisor = new MyTripAdvisor();
        advisor.addTrainCompany(fastTrain);

        List<DirectRoute> trip = advisor.getCheapestTrip(Constants.TORONTO, Constants.MONTREAL);

        if (trip == null) {
            System.out.println("null");
        }

        // Make sure that we got the route we expect
        assertEquals(2, trip.size());
        assertEquals(new DirectRoute(fastTrain, Constants.TORONTO, Constants.OTTAWA, 31), trip.get(0));
        assertEquals(new DirectRoute(fastTrain, Constants.OTTAWA, Constants.MONTREAL, 25), trip.get(1));
        assertTrue(56 == advisor.getCheapestPrice(Constants.TORONTO, Constants.MONTREAL));
    }

    @Test(timeout = 3000)
    public void directRouteTripVsTwoRouteTripWhereDirectRouteIsCheaper() {
        MyTripAdvisor advisor = new MyTripAdvisor();
        advisor.addTrainCompany(JaskarnTrainA);
        advisor.addTrainCompany(JuwonRailA);

        List<DirectRoute> trip = advisor.getCheapestTrip(Constants.TORONTO, Constants.MONTREAL);

        if (trip == null) {
            System.out.println("null");
        }

        // Make sure that we got the route we expect
        assertEquals(1, trip.size());
        assertEquals(new DirectRoute(JaskarnTrainA, Constants.TORONTO, Constants.MONTREAL, 22), trip.get(0));
        assertTrue(22 == advisor.getCheapestPrice(Constants.TORONTO, Constants.MONTREAL));
    }

    @Test(timeout = 3000)
    public void directRouteTripVsTwoRouteTripWhereTwoRouteTripIsCheaper() {
        MyTripAdvisor advisor = new MyTripAdvisor();
        advisor.addTrainCompany(JaskarnTrainB);
        advisor.addTrainCompany(JuwonRailB);

        List<DirectRoute> trip = advisor.getCheapestTrip(Constants.TORONTO, Constants.MONTREAL);

        if (trip == null) {
            System.out.println("null");
        }

        // Make sure that we got the route we expect
        assertEquals(2, trip.size());
        assertEquals(new DirectRoute(JuwonRailB, Constants.TORONTO, Constants.OTTAWA, 10), trip.get(0));
        assertEquals(new DirectRoute(JuwonRailB, Constants.OTTAWA, Constants.MONTREAL, 10), trip.get(1));
        assertTrue(20 == advisor.getCheapestPrice(Constants.TORONTO, Constants.MONTREAL));
    }

    @Test(timeout = 3000)
    public void fiveRouteTripWhereRoutesAreFromDifferentCompanies() {
        MyTripAdvisor advisor = new MyTripAdvisor();
        advisor.addTrainCompany(JaskarnTrainC);
        advisor.addTrainCompany(JuwonRailC);

        List<DirectRoute> trip = advisor.getCheapestTrip(Constants.TORONTO, Constants.SCARBOROUGH);

        if (trip == null) {
            System.out.println("null");
        }

        // Make sure that we got the route we expect
        assertEquals(5, trip.size());
        assertEquals(new DirectRoute(JaskarnTrainC, Constants.TORONTO, Constants.OTTAWA, 20), trip.get(0));
        assertEquals(new DirectRoute(JaskarnTrainC, Constants.OTTAWA, Constants.MONTREAL, 30), trip.get(1));
        assertEquals(new DirectRoute(JaskarnTrainC, Constants.MONTREAL, Constants.BRAMPTON, 40), trip.get(2));
        assertEquals(new DirectRoute(JaskarnTrainC, Constants.BRAMPTON, Constants.MISSISSAUGA, 5), trip.get(3));
        assertEquals(new DirectRoute(JaskarnTrainC, Constants.MISSISSAUGA, Constants.SCARBOROUGH, 10), trip.get(4));
        assertTrue(105 == advisor.getCheapestPrice(Constants.TORONTO, Constants.SCARBOROUGH));
    }
}
