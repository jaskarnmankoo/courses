package _backend.framework.models.media;

import java.text.DecimalFormat;

public class Media {
    // Multiple Producers?
    // Synopsis?
    // Actors / Cast? - Own class?

    // Missing: Actors/Cast, User Reviews

    private String mediaID;
    // Used to uniquely identify any form of media in our database

    private String title;
    // Exceptions: Not Null or Empty

    private String producer;
    // Should it be its own class?
    // Exceptions: Not Null or Empty

    private int[] rating = new int[5];
    // Each index holds the amount of votes for that respective rating
    // Example : { 1, 0, 0, 3, 5}
    // 1 vote for 1 star
    // 0 votes for 2 and 3 stars
    // 3 votes for 4 stars
    // 5 votes for 5 stars
    // If every index is 0, that means this piece of media has not been rated yet

    private String genre;
    // Should we have a set list of genres, or let the user define their own?
    // Should media have more than one genre?

    private String contentRating;
    // Content Rating should only be from
    // G, PG, PG-13, R, NC-17 (MPAA Ratings)
    // Should we care about other country's rating systems too?

    public Media() {
    }

    public void setID(String ID) {
        this.mediaID = ID;
    }

    public String getID() {
        return this.mediaID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducer() {
        return this.producer;
    }

    public void setRating(int[] rating) {
        this.rating = rating;

        // Exception: rating.length<= 5
        // No index should contain a negative number
    }

    public void addRating(int rate) {
        rating[rate] += 1;

        // Exceptions: 1<= rate<= 5
    }

    public void changeRating(int oldRate, int newRate) {
        rating[oldRate] -= 1;
        addRating(newRate);

        // Exceptions: 1<= oldRate<= 5
        // 1<= rating[oldRate]
    }

    public int[] getRatingArray() {
        return this.rating;
    }

    public float getRating() {
        int voteCount = 0;
        int totalRating = 0;

        for (int i = 0; i < rating.length; i++) {
            totalRating += rating[i] * (i + 1);
            voteCount += rating[i];
        }

        // No one has voted yet
        if (voteCount == 0) {
            return 0;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        float result = (float) totalRating / voteCount;
        return Float.valueOf(df.format(result));
    }

    public int getTotalVotes() {
        int voteCount = 0;

        for (int i = 0; i < rating.length; i++) {
            voteCount += rating[i];
        }

        return voteCount;
    }

    public void setGenre(String genre) {
        this.genre = genre;

        // Exceptions: Not Null or Empty?
    }

    public String getGenre() {
        return genre;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;

        // Exceptions: Not Null or Empty?
        // Should be from MPAA ratings?
    }

    public String getContentRating() {
        return contentRating;
    }

    public String toString() {
        String s = "";
        s += "ID: " + this.getID() + "\n";
        s += "Title: " + this.getTitle() + "\n";
        s += "Producer: " + this.getProducer() + "\n";

        float averageRating = this.getRating();
        // If not yet rated by anyone
        if (averageRating == 0) {
            s += "No Rating\n";
        } else {
            s += "Rating:\n";
            s += "\t 1 Star: " + getRatingArray()[0] + "\n";
            s += "\t 2 Stars: " + getRatingArray()[1] + "\n";
            s += "\t 3 Stars: " + getRatingArray()[2] + "\n";
            s += "\t 4 Stars: " + getRatingArray()[3] + "\n";
            s += "\t 5 Stars: " + getRatingArray()[4] + "\n";
            s += "Average Rating: " + averageRating + "\n";
        }

        s += "Genre: " + this.getGenre() + "\n";
        s += "Content: " + this.getContentRating() + "\n";
        return s;
    }
}
