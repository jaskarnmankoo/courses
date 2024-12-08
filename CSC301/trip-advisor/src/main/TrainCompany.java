package a1;

import java.util.Collection;
import java.util.ArrayList;

import a1.DirectRoute;

public class TrainCompany {
    private String name;
    private ArrayList<DirectRoute> routes;
    private static ArrayList<String> allCompanies = new ArrayList<String>();

    public TrainCompany(String name) {
        this.setName(name);
        this.routes = new ArrayList<DirectRoute>();
        System.out.print(this.name);
        System.out.println(" company still added");
        allCompanies.add(this.name);
    }

    public String toString() {
        return String.format("%s, offering %d routes between %d stations", getName(), getDirectRoutesCount(),
                getStationsCount());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || allCompanies.contains(name) || name.trim().length() == 0) {
            throw new IllegalArgumentException(); // COMMENT THIS LINE BEFORE TESTING MYTRIPADVISOR
        }

        this.name = name.trim();
    }

    /**
     * @return The DirectRoute object that was created/updated.
     */
    public DirectRoute createOrUpdateDirectRoute(String fromStation, String toStation, double price) {
        DirectRoute newRoute = new DirectRoute(this, fromStation, toStation, price);
        DirectRoute[] routeArray = routes.toArray(new DirectRoute[routes.size()]);
        boolean not_found = true;

        for (int i = 0; i < routeArray.length; i++) {
            if (routeArray[i].getFromStation() == fromStation && routeArray[i].getToStation() == toStation) {
                routeArray[i].setPrice(price);
                not_found = false;
            }
        }

        if (not_found) {
            routes.add(newRoute);
        }

        return newRoute;
    }

    /**
     * Delete the specified route, if it exists.
     */
    public void deleteDirectRoute(String fromStation, String toStation) {
        DirectRoute[] routeArray = routes.toArray(new DirectRoute[routes.size()]);

        for (int i = 0; i < routeArray.length; i++) {
            if (routeArray[i].getFromStation() == fromStation && routeArray[i].getToStation() == toStation) {
                routes.remove(routeArray[i]);
            }
        }
    }

    /**
     * @return null if there is no route from <code>fromStation</code> to
     *         <code>toStation</code> with this TrainCompany.
     */
    public DirectRoute getDirectRoute(String fromStation, String toStation) {
        if (fromStation == null || toStation == null || fromStation.trim().length() == 0
                || toStation.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        fromStation = fromStation.trim();
        toStation = toStation.trim();
        DirectRoute[] routeArray = routes.toArray(new DirectRoute[routes.size()]);

        for (int i = 0; i < routeArray.length; i++) {
            if (routeArray[i].getFromStation() == fromStation && routeArray[i].getToStation() == toStation) {
                return routeArray[i];
            }
        }

        return null;
    }

    public Collection<DirectRoute> getDirectRoutesFrom(String fromStation) {
        if (fromStation == null || fromStation.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        fromStation = fromStation.trim();
        ArrayList<DirectRoute> fromRoutes = new ArrayList<DirectRoute>();
        DirectRoute[] routeArray = routes.toArray(new DirectRoute[routes.size()]);

        for (int i = 0; i < routeArray.length; i++) {
            if (routeArray[i].getFromStation().equals(fromStation)) {
                fromRoutes.add(routeArray[i]);
            }
        }

        return fromRoutes;
    }

    public Collection<DirectRoute> getRoutesTo(String toStation) {
        if (toStation == null || toStation.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        toStation = toStation.trim();
        ArrayList<DirectRoute> toRoutes = new ArrayList<DirectRoute>();
        DirectRoute[] routeArray = routes.toArray(new DirectRoute[routes.size()]);

        for (int i = 0; i < routeArray.length; i++) {
            if (routeArray[i].getToStation().equals(toStation)) {
                toRoutes.add(routeArray[i]);
            }
        }

        return toRoutes;
    }

    public Collection<DirectRoute> getAllDirectRoutes() {
        return routes;
    }

    public int getDirectRoutesCount() {
        return getAllDirectRoutes().size();
    }

    /**
     * @return The number of stations with service by this TrainCompany. To be
     *         clearer: - Take the union of all stations (from and to) from
     *         this.getAllDirectRoutes() - Count the unique number of stations (i.e.
     *         You only count a station once, even if there are multiple routes
     *         from/to this station)
     */
    public int getStationsCount() {
        ArrayList<String> stations = new ArrayList<String>();
        DirectRoute[] routeArray = routes.toArray(new DirectRoute[routes.size()]);

        for (int i = 0; i < routeArray.length; i++) {
            // Add their fromStation if it hasn't been done already
            if (!stations.contains(routeArray[i].getFromStation())) {
                stations.add(routeArray[i].getFromStation());
            }

            // Add their toStation if it hasn't been done already
            if (!stations.contains(routeArray[i].getToStation())) {
                stations.add(routeArray[i].getToStation());
            }
        }

        return stations.size();
    }
}
