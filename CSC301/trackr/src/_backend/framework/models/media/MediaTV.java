package _backend.framework.models.media;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MediaTV extends Media {

    // Missing: TV Network

    // Should we have multiple TV Networks for one show if possible?

    private Collection<Season> seasons = new ArrayList<Season>();
    private boolean isCompleted;
    private String airDate;
    // Format: "YYYY-MM-DD"
    // Example: "2018/02/10"

    public MediaTV() {
    }

    public void setSeasons(Collection<Season> seasons) {
        this.seasons.clear();
        this.addSeasons(seasons);
    }

    public void addSeason(Season newSeason) {
        seasons.add(newSeason);

        // Exceptions: newSeason not Null
    }

    public void addSeasons(Collection<Season> newSeasons) {
        Iterator<Season> iterator = newSeasons.iterator();

        while (iterator.hasNext()) {
            this.addSeason(iterator.next());
        }
    }

    public Collection<Season> getSeasons() {
        return this.seasons;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isAiring() {
        return !this.isCompleted;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
        // Exceptions: Does not follow format
    }

    public String getAirDate() {
        return this.airDate;
    }

    public int getSeasonCount() {
        return this.seasons.size();
    }

    public int getEpisodeCount() {
        int count = 0;

        Iterator<Season> iterator = seasons.iterator();

        while (iterator.hasNext()) {
            count += iterator.next().getEpisodeCount();
        }

        return count;
    }

    public String toString() {
        String s = "--TV Show--\n";
        String mediaString = super.toString();
        s += mediaString;
        s += "Air Date: " + getAirDate() + "\n";
        s += "Seasons: " + getSeasonCount() + "\n";
        s += "Episodes: " + getEpisodeCount() + "\n";
        s += "\n";

        Iterator<Season> iterator = seasons.iterator();

        while (iterator.hasNext()) {
            s += iterator.next() + "\n";
        }

        s += (isAiring()) ? "Is Airing" : "Finished Airing";
        s += "\n\n";
        return s;
    }
}
