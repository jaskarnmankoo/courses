# Sprint Planning Meeting 3
#### Participants: 
Andrew, Daniel, Faiz, James, Jaskarn, Juwon, Michelle

## Release Goals
- Complete database functionality:
	- User should connect to a remote database and update their local database
	- When the user makes changes to their local database, the changes should be reflected in the remote database
- Making button commands (InputManager & ActionListeners) more abstract (they are mostly hard coded as of now)
- Make screens better:
	- Unify themes (Matching colours, fonts, etc.)
	- Resizing screens

## User Stories to be completed

#### User Story: Integrate Online Database
As a programmer, I want an database hosted on an online server that will house information on users and media, so that data is not only stored locally, and users can use the program's features on different devices.
**Back-End**
Create and hook up server-side database with program.

#### User Story: Unify Screen Themes
As a developer, I want there to be a similar visual theme between all the screens of the program, so that it looks aesthetically pleasing to the users.
**Front-End**
Create a similar theme for all the screens.

#### User Story: Custom List Screen
As a user, I want a screen that has a list of all my shows at the moment, so I can easily access my shows in one place.
**Front-End**
Display list of shows.
**Back-End**
Create a way to store and access shows in the database.

#### User Story: Update Profile Screen
As a user, I want my profile screen updated so that it now lists my friends on there as well.
**Front-End**
Display a section for the user's friends.

#### User Story: Update Modification Screen
As a programmer, I want the modification screen updated so that it is not the placeholder screen, so that I can actually modify the status of my shows.
**Front-End**
Display options to the user to modify:
-	Seasons watched
-	Star rating
-	Status: Watching / Want To Watch / Completed
-	etc.

#### User Story: Create a Chat System
As a user, I want a chat system so that Friends in the Trackr Application can talk with one another.
**Front-End**
Creating the chat screen.
**Back-End**
Creating the server for the chat system.

#### User Story ?: Create a Reviews Page
As a user, I want a page that displays the reviews for a selected show, so that I have an easy way to view all the reviews and ratings for a specific show.
**Front-End**
Displaying the reviews for a selected show.
