# Scrum Meeting Notes (Sprint 1)
### February 2, 2018

**Meeting led by Faiz, Michelle, James**  

### Logistics
- Languages / Tools
- Pull Requests
- User Stories
- Decided on the time frame to finish components for Sprint 1:
    - Have something stable by Feb. 13
- Faiz demos early framework for the team
    - Using CardLayout API
    - Using a State Manager for switching to different states
        - Ex. Login State ---(Successful Login)---> Main State
        - Ex. Main State ---(Select Friends)---> Friends List State

### Languages / Tools  
***Team Members: Jaskarn, Daniel, Andrew***
- Programming language: Java
- - Swing GUI
- Database language: MySQL

### Pull Requests  
***Team Members: James, Michelle***
- Stressed about the importance of using pull requests, so that implementing features do not lead to conflicts in master
- Makes changes to master more seamless

**Process:**
 - Create local branch to work on a specific feature / user story
 - Pull the newest version of master so that there are no conflicts with your version
 - Create a remote branch for said feature when finished
 - Submit a pull request so members can review the feature so that it functions properly
 - If successful review, merge the branch with master 

### User Stories / Features  
- Started assigning features to members
- Front End: JPanel Creation, & User Stories 0 & 18
    - Show a list of the user's shows
    - Show a list of the user's recommended shows
    - Show a list of friends? (Maybe next sprint?)
    - Show a profile page? (Maybe next sprint?)

**Back End:**  
***Team Members: Juwon, Faiz***
- Setup Database
- Login Credentials Table
    - **Attributes:** Username, Password, PIN
- Shows Table
    - **Attributes:** Name, Ratings, Genre
- Movies Table
    - **Attributes:** Name, Ratings, Genre
- A way to encrypt User Login Information, so the database cannot be exploited by malicious users to retrieve information
