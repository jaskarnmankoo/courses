# trip-advisor

A project done in pairs that involved implementing a trip advisor using Dijkstra's Algorithm.

## Schedule/Milestones
- **January 11**: GitHub repository was created by Professor Ilir Dema
- **January 13**: Have a teammate found or work alone in case this did not happen
- **January 15**: Implement at least one class of the provided three 
- **January 17**: Implement the test class of the class implemented on January 15
- **January 19**: Implement two of the three provided classes
- **January 20**: Implement the test class of the class implemented on January 19
- **January 22**: Implement the final class
- **January 23**: Implement the final test class
- **January 24**: Add JavaDoc/comments where necessary
- **January 25**: If necessary, add finishing touches to the code and push README.md
- **January 26**: Assignment is due at 11:59PM

## Development Process
Our development process differed slightly from the provided "Github issue management system," and we found this modified method to be a better way to manage our team communication. The development process differed in two ways: branches, and the way bugs were handled. We decided not to use branches as in the past during CSC207 we found they made the GitHub repository a mess and difficult to follow. Instead, what we did was push to the master branch with a detailed commit message, so the other individual would be aware of the changes made. If this message was somehow not clear enough Facebook messenger was the primary method of communication. The way bugs were handled was simple, one individual would implement a function they thought was correct; however, if there was a more efficient or readable way to program exactly the same function it would be rewritten by the other individual. Once we were satisfied with an implementation doctests would be tested and the code modified accordingly.

## Design Decisions

### **DirectRoute.java**
- Originally the equality method simply checked whether "this.getTrainCompany() == obj.getTrainCompany()"; however, we soon realized that we had to check whether object was an "instanceof DirectRoute" in the first place

### **TrainCompany.java**
- We chose an ArrayList as the collection to be used for storing the routes, and for storing the company names, as all of the functions within TrainCompany.java could be easily implemented with the default ArrayList functions

### **MyTripAdvisor.java**
- For GetCheapestTrip, we used a modified version of Uniform Cost Search, as this type of search yields the lowest cost path from the starting node, to the goal node
- We used a HashMap to record a station and its associated cost of getting there, and a HashMap to record a station and the cheapest route to get there. If we reach a station that we've recorded already (through another route), but the cost of getting there is lower than what we recorded, we update the respective HashMaps with the relevant information
- For our frontier nodes (stations in this case), we used a priority queue whose comparator is based on the cost of reaching that node from the starting node. This allows for the algorithm to prioritize checking stations with the lowest total cost first, which is what we want
- After checking all the routes, we know the lowest cost of reaching the goal node, and the associated path of getting there, returning the desired result
 
### **General for All Classes**
- Since it is not clear whether Strings came trimmed we decided to trim them anyway in Setters just in case to avoid failing any tests
- Originally the initializers would simply state "this.TrainCompany == trainCompany"; however, we found that using setters in the initializers would also allow us to catch errors so assigning statements were changed to "this.setTrainCompany(trainCompany)"
- DirectRoutes and TrainCompany instances were stored in ArrayList's because we could convert this type with the method ".toArray()" so it could be traversed
