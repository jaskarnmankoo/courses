package _backend.framework.models.user;

import _backend.framework.models.media.Media;
import _backend.utils.errorHandling.ErrorLogger;

public class UserMediaMovie extends UserMedia {
    private int timestamp; // Time in minutes where the user stopped watching the movie

    public UserMediaMovie() {
    }

    public UserMediaMovie(Media media) {
        this.setMediaID(media.getID());
        this.setUserRating(0);
        this.markAsFinished(false);
    }

    public void setTimestamp(int time) {
        if (time < 0) {
            ErrorLogger.logMessage("You can't have a negative timestamp for a Movie!!");
        }

        this.timestamp = time;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public String toString() {
        String s = "--Movie--\n";
        String mediaString = super.toString();
        s += mediaString;

        // If the user isn't done yet, show the season & episode they are currently on
        if (!this.isFinished()) {
            s += "Currently at: " + this.getTimestamp() + " minutes\n";
        }

        return s;
    }
}
