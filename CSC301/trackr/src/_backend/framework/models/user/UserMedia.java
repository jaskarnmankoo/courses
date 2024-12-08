package _backend.framework.models.user;

import _backend.framework.models.media.Media;
import _backend.utils.errorHandling.ErrorLogger;

// This class is used to handle how Users interact with media
public class UserMedia {
    private String mediaID;
    // The ID of the media that this class instance refers to
    // Using this ID, we can extract all the other information like
    // genre, rating, producer, etc., by querying the database

    private int userRating;
    // The rating given to this piece of media by the user
    // If userRating == 0, the user has not rated this piece of media yet

    private boolean markedFinished;
    // Whether or not the media was marked as finished being watched by the user

    public UserMedia() {
    }

    /**
     * Constructs itself with a given Media Object.
     *
     * @param media
     */
    public UserMedia(Media media) {
        this.setMediaID(media.getID());
        this.setUserRating(0);
        this.markAsFinished(false);
    }

    public void setMediaID(String ID) {
        if (ID == null) {
            ErrorLogger.logMessage("Media ID should not be null!");
        }

        this.mediaID = ID;
    }

    public String getMediaID() {
        return this.mediaID;
    }

    public void setUserRating(int rating) {
        if (rating < 1 || rating > 5) {
            ErrorLogger.logMessage("User Rating should from 1-5");
        }

        this.userRating = rating;

    }

    public int getUserRating() {
        return this.userRating;
    }

    public void markAsFinished(boolean isFinished) {
        this.markedFinished = isFinished;
    }

    public boolean isFinished() {
        return this.markedFinished;
    }

    public String toString() {
        String s = "";
        // get Media name from database using ID
        s += "Media ID: " + this.getMediaID() + "\n";
        int yourRating = this.getUserRating();
        s += (yourRating == 0) ? "No Rating" : (yourRating == 1) ? "1 Star" : yourRating + " Stars";
        s += "\n";

        s += (this.isFinished()) ? "Finished Watching" : "Not yet Finished";
        s += "\n";
        return s;
    }
}
