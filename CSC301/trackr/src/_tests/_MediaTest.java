package _tests;

import java.util.ArrayList;

import _backend.framework.models.media.Episode;
import _backend.framework.models.media.MediaMovie;
import _backend.framework.models.media.MediaTV;
import _backend.framework.models.media.Season;

public class _MediaTest {
	public static void main(String[] args) {
		// Test TV Show
		MediaTV TrueDetective = new MediaTV();

		Episode TDEp1 = new Episode();
		TDEp1.setEpisodeNumber(1);
		TDEp1.setEpisodeTitle("The Long Bright Dark");

		Episode TDEp2 = new Episode();
		TDEp2.setEpisodeNumber(2);
		TDEp2.setEpisodeTitle("Seeing Things");

		Episode TDEp11 = new Episode();
		TDEp11.setEpisodeNumber(1);
		TDEp11.setEpisodeTitle("Maybe Tomorrow");

		Season TDS1 = new Season();
		TDS1.setSeasonNumber(1);
		TDS1.setEpisodes(new ArrayList<Episode>() {
			{
				add(TDEp1);
				add(TDEp2);
			}
		});

		Season TDS2 = new Season();
		TDS2.setSeasonNumber(2);
		TDS2.addEpisode(TDEp11);

		TrueDetective.setID("TV_1234");
		TrueDetective.setTitle("True Detective");
		TrueDetective.setProducer("Anonymous Content");
		TrueDetective.setRating(new int[] { 3, 4, 5, 18, 12 });
		TrueDetective.setGenre("Crime Drama");
		TrueDetective.setContentRating("R");
		TrueDetective.setAirDate("2014-01-12");
		TrueDetective.setCompleted(false);
		TrueDetective.setSeasons(new ArrayList<Season>() {
			{
				add(TDS1);
				add(TDS2);
			}
		});

		// Test Movies
		MediaMovie LastJedi = new MediaMovie();
		LastJedi.setID("MV_232");
		LastJedi.setTitle("Star Wars: The Last Jedi");
		LastJedi.setProducer("Lucasfilm");
		LastJedi.setRating(new int[] { 6, 5, 12, 22, 15 });
		LastJedi.setGenre("Sci-Fi");
		LastJedi.setContentRating("PG-13");
		LastJedi.setReleaseDate("2017-12-15");
		LastJedi.setRunningTime(152);

		MediaMovie BlackPanther = new MediaMovie();
		BlackPanther.setID("MV_908");
		BlackPanther.setTitle("Black Panther");
		BlackPanther.setProducer("Marvel Studios");
		BlackPanther.setRating(new int[] { 0, 0, 0, 0, 0 });
		BlackPanther.setGenre("Action");
		BlackPanther.setContentRating("PG-13");
		BlackPanther.setReleaseDate("2018-02-16");
		BlackPanther.setRunningTime(134);

		System.out.println(TrueDetective);
		System.out.println(LastJedi);
		System.out.println(BlackPanther);
	}
}
