package _backend.framework.models.user;

import _backend.framework.models.media.Media;
import _backend.utils.errorHandling.ErrorLogger;

public class UserMediaTV extends UserMedia {
    private int currentSeason;
    private int currentEpisode;
    // The season and episode the user stopped at, for this piece of media

    public UserMediaTV() {
    }

    public UserMediaTV(Media media) {
        this.setMediaID(media.getID());
        this.setUserRating(0);
        this.markAsFinished(false);
    }

    // GETTERS AND SETTERS

    public void setCurrentSeason(int season) {
        if (season < 0) { // Some seasons start 0, some at 1, but in the end, we are Computer Science
                          // students
            ErrorLogger.logMessage("Can't have a non-negative season!");
        }

        this.currentSeason = season;
    }

    public int getCurrentSeason() {
        return this.currentSeason;
    }

    public void setCurrentEpisode(int episode) {
        if (episode < 1) {
            ErrorLogger.logMessage("You dun goofed if you're watching negative episodes or the 0th one.");
        }

        this.currentEpisode = episode;
    }

    public int getCurrentEpisode() {
        return this.currentEpisode;
    }

    public String toString() {
        String s = "--TV Show--\n";
        String mediaString = super.toString();
        s += mediaString;

        // If the user isn't done yet, show the season & episode they are currently on
        if (!this.isFinished()) {
            s += "Currently on " + "Season " + this.getCurrentSeason() + ": " + "Episode " + this.getCurrentEpisode()
                    + "\n";
        }

        return s;
    }
}
