package _backend.framework.models.media;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Season {
    private int seasonNumber = 0;
    // Default value

    private Collection<Episode> episodeList = new ArrayList<Episode>();
    // Exceptions: no episode should be Null

    public Season() {
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getSeasonNumber() {
        return this.seasonNumber;
    }

    public void setEpisodes(Collection<Episode> episodeList) {
        this.episodeList.clear();
        this.addEpisodes(episodeList);
    }

    public void addEpisode(Episode newEpisode) {
        episodeList.add(newEpisode);
        // Exceptions: newEpisode not Null
    }

    public void addEpisodes(Collection<Episode> newEpisodes) {
        Iterator<Episode> iterator = newEpisodes.iterator();

        while (iterator.hasNext()) {
            this.addEpisode(iterator.next());
        }
    }

    public Collection<Episode> getEpisodes() {
        return this.episodeList;
    }

    public int getEpisodeCount() {
        return this.episodeList.size();
    }

    public String toString() {
        String s = "Season No. " + getSeasonNumber() + "\n";
        s += "Episodes: " + getEpisodeCount() + "\n";
        s += "--Episode List--\n";

        Iterator<Episode> iterator = episodeList.iterator();

        while (iterator.hasNext()) {
            s += iterator.next();
        }

        return s;
    }
}
