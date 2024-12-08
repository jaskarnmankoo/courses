# Sprint Planning Meeting 2
###### Participants:
Daniel, Faiz, James, Juwon, Michelle

## Release Goals
The main goal of this sprint is to test functionality of our program, so that the user can interact with it properly in a controlled setting. We will mainly be using placeholder shows for the entirety of this sprint.
##### Database
- Implement online server side database which holds:
    - User Accounts
    - TV Shows & Movies (placeholder ones for now)
##### User Account
- Proper Login System using user information from database
- When logged in, users can see their list of shows & movies
##### Manipulate Shows
- Add Shows to a User Account
- Remove Shows from an Account
- Modify show properties

#### User Stories to Be Completed

##### User Story 0
As a user, I want a way to display the shows and movies I have watched in lists, so that I have easy access to my whole watch history.

**Front-End**
Displaying the associated shows to the logged-in User Account

##### User Story 1
As a user, I should be able to sort and filter my shows/movies from a list based on certain attributes I specify, such as their name, rating and genre. This is so I can see easily find a show I am searching for. (Only name & rating for this sprint)

**Back-End**
For providing the modified/filtered list based on the specifications.

**Front-End**
For updating the view with the new list provided by back-end.

##### User Story 2
As a user, I want to be able to add a show to track on Trackr by searching for it via criterion like rating, genre, already watched etc. into a list. This is so that I can easily find shows that fit my desired specifications and add them to the tracker.

**Back-End**
Adding the shows to the associated User Account's list of shows.

##### User Story 7
As a user, I want a flexible way to manage shows, by putting them in lists I create, which can be categorized, sorted, and even named to my liking, so that I can group, and later easily access certain shows, in a manner that is convenient to me.

**Back-End**
Creating the relationship table in the database between users and their lists of shows.

##### User Story 9
As a user, I want a publicly available 5-star rating system for each show, so that I can search shows based on rating, sort shows in my lists based on rating, filter shows based on rating and rate shows myself.

**Back-End**
Creating the relationship table between users and their ratings/review on media.

##### User Story 13
As a user, I want to be able to log-in so that I can view my personal list of tracked shows.

**Back-End**
Checking the input information against the database to see if its a real user, and logging in the user if so.

##### User Story 15
As a user, I want to be able to remove shows from my tracked lists so that I'm not tracking a show I don't want to track.

**Back-End**
For removing the show from the User Account's list.

**Front-End**
For button on screen that prompts this command.

##### User Story 16
As a user, I want to modify shows in my list (episodes watched, rating) so it better reflects the accuracy of where I am at for that show, and what I think about the show.

**Back-End**
For updating the database with the modifications to the user's relationship with the show.

**Front-End**
For button on screen that prompts this command.

##### User Story 17
As a user, I want to be able to sign up for an account on Trackr so that I can start tracking my shows/movies.

**Back-End**
For adding the newly made user account to the database.

**Front-End**
For Registration View that allows users to sign up.

##### User Story ???

**Back-End**
Complete InputManager with most inputs

##### User Story ???

**Front-End**
Finish creating a main screen for when the user logs in
- Shows their lists of shows
- Links to other pages (profile, friends list, etc.)

##### User Story ???

**Front-End**
Polishing up existing views (Login, Main) and making them look nicer
