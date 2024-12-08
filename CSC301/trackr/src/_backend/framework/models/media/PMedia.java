package _backend.framework.models.media;

import java.util.Map;

/**
 * Personal Media Object Model.
 *
 */
public class PMedia extends GMedia {
    private String startDate, endDate, status;
    private int episodeCount, rating;

    public PMedia(Map<String, Object> info) {
        super(info);
        this.startDate = (String) info.get("startdate");
        this.endDate = (String) info.get("enddate");
        this.status = (String) info.get("status");
        this.episodeCount = Integer.parseInt((String) info.get("episodecount"));
    }

    public PMedia() {
    }

    public int getEpisodeCount() {
        return this.episodeCount;
    }

    public void setEpisodeCount(int ep) {
        this.episodeCount = ep;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
