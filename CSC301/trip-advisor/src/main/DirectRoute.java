package a1;

import a1.TrainCompany;

public class DirectRoute {
    private TrainCompany trainCompany;
    private String fromStation;
    private String toStation;
    private double price;

    public DirectRoute(TrainCompany trainCompany, String fromStation, String toStation, double price) {
        this.setTrainCompany(trainCompany);
        this.setFromStation(fromStation);
        this.setToStation(toStation);
        this.setPrice(price);
    }

    public TrainCompany getTrainCompany() {
        return this.trainCompany;
    }

    public void setTrainCompany(TrainCompany trainCompany) {
        if (trainCompany == null) {
            throw new IllegalArgumentException();
        }

        this.trainCompany = trainCompany;
    }

    public String getFromStation() {
        return this.fromStation;
    }

    public void setFromStation(String fromStation) {
        if (fromStation == null || fromStation.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        this.fromStation = fromStation.trim();
    }

    public String getToStation() {
        return this.toStation;
    }

    public void setToStation(String toStation) {
        if (toStation == null || toStation.trim().length() == 0) {
            throw new IllegalArgumentException();
        }

        this.toStation = toStation.trim();
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }

        this.price = price;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DirectRoute) {
            return ((DirectRoute) obj).getTrainCompany() == this.getTrainCompany()
                    && ((DirectRoute) obj).getFromStation().equals(this.getFromStation())
                    && ((DirectRoute) obj).getToStation().equals(this.getToStation())
                    && ((DirectRoute) obj).getPrice() == this.getPrice();
        }

        return false;
    }

    public String toString() {
        return String.format("%s from %s to %s, %.2f$", getTrainCompany().getName(), getFromStation(), getToStation(),
                getPrice());
    }
}
