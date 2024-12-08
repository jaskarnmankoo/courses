package _backend.framework.models.media;

public class MediaMovie extends Media {

    // Distribution Companies?

    private int runningtime; // In minutes

    private String releaseDate;
    // Format: "YYYY-MM-DD"
    // Example: "2018/02/10"

    public MediaMovie() {
    }

    public void setRunningTime(int runningTime) {
        this.runningtime = runningTime;
    }

    public int getRunningTime() {
        return this.runningtime;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;

        // Exceptions: Does not follow format
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public String toString() {
        String s = "--Movie--\n";
        String mediaString = super.toString();
        s += mediaString;
        s += "Running Time: " + getRunningTime() + " Minutes\n";
        s += "Release Date: " + getReleaseDate() + "\n";
        s += "\n";
        return s;
    }
}
