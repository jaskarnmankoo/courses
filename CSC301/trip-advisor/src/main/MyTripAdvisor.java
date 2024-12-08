package a1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import a1.DirectRoute;
import a1.TrainCompany;

public class MyTripAdvisor {
    private ArrayList<TrainCompany> companies;

    public MyTripAdvisor() {
        this.companies = new ArrayList<TrainCompany>();
    }

    public void addTrainCompany(TrainCompany trainCompany) {
        if (trainCompany == null) {
            throw new IllegalArgumentException();
        }
        companies.add(trainCompany);
    }

    /**
     * Return the price of a cheapest trip from <code>fromStation</code> to
     * <code>toStation</code>. Return -1, if there is no trip between the two
     * specified stations.
     */
    public double getCheapestPrice(String fromStation, String toStation) {
        if (fromStation == null || toStation == null || fromStation.trim().length() == 0
                || toStation.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        fromStation = fromStation.trim();
        toStation = toStation.trim();

        List<DirectRoute> cheapRoute = getCheapestTrip(fromStation, toStation);
        if (cheapRoute != null) {
            double curPrice = 0;
            for (int i = 0; i < cheapRoute.size(); i++) {
                curPrice += cheapRoute.get(i).getPrice();
            }
            return curPrice;
        }

        return -1;
    }

    /**
     * Return a cheapest trip from <code>fromStation</code> to
     * <code>toStation</code>, as a list of DirectRoute objects. Return null, if
     * there is no trip between the two specified stations.
     */
    public List<DirectRoute> getCheapestTrip(String fromStation, String toStation) {
        if (fromStation == null || toStation == null || fromStation.trim().length() == 0
                || toStation.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        fromStation = fromStation.trim();
        toStation = toStation.trim();

        final HashMap<String, Double> priceMap = new HashMap<String, Double>(); // A map with the station and its lowest
                                                                                // cost to get there
        HashMap<String, DirectRoute> routeMap = new HashMap<String, DirectRoute>(); // A map with a station and the best
                                                                                    // route to get there
        PriorityQueue<String> open = new PriorityQueue<String>(10, new Comparator<String>() { // A queue that
                                                                                              // prioritizes searching
                                                                                              // stations with
                                                                                              // relatively low cost
            @Override
            public int compare(String s1, String s2) {
                return Double.compare(priceMap.get(s1), priceMap.get(s2));
            }
        });

        // Set up pricing, routing, and unexplored nodes for search
        priceMap.put(fromStation, (double) 0); // costs 0 to start at the fromStation
        routeMap.put(fromStation, null); // null as there are no introductory routes to this station
        open.add(fromStation); // Nodes we will search from (starting from the fromStation)

        // While there are unexplored routes
        while (!open.isEmpty()) {
            String curStation = open.poll();
            // We found a route to the destination
            if (curStation.equals(toStation)) {
                System.out.println("Found Destination");
            } else { // Keep checking routes
                // Check all train companies for routes from this station
                ArrayList<DirectRoute> allRoutes = new ArrayList<DirectRoute>();

                for (int i = 0; i < companies.size(); i++) {
                    Collection<DirectRoute> routeCol = companies.get(i).getDirectRoutesFrom(curStation);
                    allRoutes.addAll(routeCol);
                }

                for (int i = 0; i < allRoutes.size(); i++) {
                    DirectRoute curRoute = allRoutes.get(i);
                    String newToStation = curRoute.getToStation();
                    double cost = priceMap.get(curStation) + curRoute.getPrice();
                    // If the price hasn't been recorded yet, or the price through this route is
                    // lower than another,
                    // explore this route, and update the cost

                    if (!priceMap.containsKey(newToStation)) {
                        priceMap.put(newToStation, cost);
                        routeMap.put(newToStation, curRoute);
                        open.add(newToStation);
                    } else if (cost < priceMap.get(newToStation)) {
                        open.remove(newToStation);
                        priceMap.put(newToStation, cost);
                        routeMap.put(newToStation, curRoute);
                        open.add(newToStation);
                    }
                }
            }
        }

        // If the routeMap has a solution, we can continuously its parent route, then
        // its parent's parent route, etc.
        // until we reach null, the starting station
        if (routeMap.containsKey(toStation)) {
            List<DirectRoute> cheapestRoute = new ArrayList<DirectRoute>();
            DirectRoute cr = routeMap.get(toStation);
            while (cr != null) {
                cheapestRoute.add(cr);
                cr = routeMap.get(cr.getFromStation());
            }
            // Since routes were added from the end to the beginning, we should flip the
            // list to get the correct order
            Collections.reverse(cheapestRoute);
            return cheapestRoute;
        }

        return null;
    }
}
