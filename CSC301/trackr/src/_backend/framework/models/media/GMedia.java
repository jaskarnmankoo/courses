package _backend.framework.models.media;

import java.util.Map;

import _backend.utils.errorHandling.ErrorLogger;

public class GMedia {
    protected String title, type, genre;
    protected int duration;

    public GMedia(Map<String, Object> info) {
        if (info == null) {
            ErrorLogger.logMessage("GMedia info is null!");
        }

        this.title = (String) info.get("mediatitle");
        this.type = (String) info.get("mediatype");
        this.genre = (String) info.get("mediagenre");
        this.duration = Integer.parseInt((String) info.get("mediaduration"));
    }

    public GMedia() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
