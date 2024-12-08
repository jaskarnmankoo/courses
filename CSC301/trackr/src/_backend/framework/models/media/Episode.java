package _backend.framework.models.media;

public class Episode {
    // Air Date?
    // Synopsis?

    // Missing: Preview Image

    private int episodeNumber;
    // Should episode number be episode in season or episode overall?

    private String episodeTitle;

    public Episode() {
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getEpisodeNumber() {
        return this.episodeNumber;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
        // Exceptions: episodeTitle not Null
    }

    public String getEpisodeTitle() {
        return this.episodeTitle;
    }

    public String toString() {
        String s = "";
        s += "No. " + getEpisodeNumber() + ": " + getEpisodeTitle() + "\n";
        return s;
    }
}
