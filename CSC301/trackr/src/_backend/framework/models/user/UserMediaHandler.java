package _backend.framework.models.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import _backend.framework.models.media.Media;
import _backend.framework.models.media.MediaMovie;
import _backend.framework.models.media.MediaTV;
import _backend.utils.errorHandling.ErrorLogger;

/**
 * This class handles everything that has to do with a user's media.
 *
 */
public class UserMediaHandler {
    private Collection<UserMedia> mediaList = new ArrayList<UserMedia>(); // User's Media List

    public UserMediaHandler() {
    }

    public void setUserMediaList(Collection<UserMedia> mediaList) {
        this.mediaList.clear();
        this.addUserMedia(mediaList);
    }

    public void setMediaList(Collection<Media> mediaList) {
        this.mediaList.clear();
        this.addMedia(mediaList);
    }

    public void addUserMedia(UserMedia newMedia) {
        if (newMedia == null) {
            ErrorLogger.logMessage("UserMedia (newMedia) should not be null");
        }

        this.mediaList.add(newMedia);
    }

    /**
     * Adds multiple UserMedia Objects
     *
     * @param newMedia
     */
    public void addUserMedia(Collection<UserMedia> newMedia) {
        Iterator<UserMedia> iterator = newMedia.iterator();

        while (iterator.hasNext()) {
            this.addUserMedia(iterator.next());
        }
    }

    /**
     * Constructs UserMedia object based on Media object given. Automatically
     * specializes its object type based on its original Media object type If it
     * can't specialize, just create a regular UserMedia object
     *
     * @param media
     */
    public void addMedia(Media media) {
        if (media instanceof MediaTV) {
            UserMediaTV userMedia = new UserMediaTV(media);
            this.addUserMedia(userMedia);
        } else if (media instanceof MediaMovie) {
            UserMediaMovie userMedia = new UserMediaMovie(media);
            this.addUserMedia(userMedia);
        } else {
            UserMedia userMedia = new UserMedia(media);
            this.addUserMedia(userMedia);
        }
    }

    /**
     * Adds multiple Media Objects
     *
     * @param media
     */
    public void addMedia(Collection<Media> media) {
        Iterator<Media> iterator = media.iterator();

        while (iterator.hasNext()) {
            this.addMedia(iterator.next());
        }
    }

    /**
     * Returns a User Media object that matches the specified media ID Returns null
     * if the ID doesn't match any User Media in the media list
     *
     * @param ID
     * @return
     */
    public UserMedia getUserMedia(String ID) {
        Iterator<UserMedia> iterator = this.mediaList.iterator();

        while (iterator.hasNext()) {
            UserMedia curMedia = iterator.next();

            // Check for matching ID
            if (curMedia.getMediaID().equals(ID)) {
                return curMedia;
            }
        }

        return null;
    }

    /**
     * Like above, but specifically returns UserMediaTV objects If it finds a match,
     * but the object isn't a UserMedia TV object, it is skipped.
     *
     * @param ID
     * @return
     */
    public UserMediaTV getUserMediaTV(String ID) {
        Iterator<UserMedia> iterator = this.mediaList.iterator();

        while (iterator.hasNext()) {
            UserMedia curMedia = iterator.next();

            // Only check UserMediaTV objects
            if (curMedia instanceof UserMediaTV) {
                // Check for matching ID
                if (curMedia.getMediaID().equals(ID)) {
                    return (UserMediaTV) curMedia;
                }
            }
        }

        return null;
    }

    /**
     * Like above, but for UserMediaMovie objects
     *
     * @param ID
     * @return
     */
    public UserMediaMovie getUserMediaMovie(String ID) {
        Iterator<UserMedia> iterator = this.mediaList.iterator();

        while (iterator.hasNext()) {
            UserMedia curMedia = iterator.next();

            // Only check UserMediaMovie objects
            if (curMedia instanceof UserMediaMovie) {
                // Check for matching ID
                if (curMedia.getMediaID().equals(ID)) {
                    return (UserMediaMovie) curMedia;
                }
            }
        }

        return null;
    }

    public Collection<UserMedia> getAllUserMedia() {
        return this.mediaList;
    }

    public int mediaCount() {
        return this.mediaList.size();
    }

    public String toString() {
        String s = "";
        Iterator<UserMedia> iterator = this.mediaList.iterator();
        while (iterator.hasNext()) {
            s += iterator.next();
            s += "--------\n";
        }
        s += "\n";

        return s;
    }
}
