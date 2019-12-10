# MovieNight - a movie recommendation application for groups

MovieNight calculates movie recommendation lists for a group.

The application provides two recommendation lists for the group: one list which is calculated based on the group members' movie ratings and another list which emphasizes the movie genres that are preferred by the group members.

Movie rating data is from MovieLens grouplens.org/datasets/movielens/latest
The data contains about 100 000 ratings (9 742 movies rated by 610 users).

Each member of the group ('MovieNight user') first enters a couple of ratings for movies that exist in the database. (At the moment the group member ratings are entered to the system in advance.) In addition, each member of the group can choose one movie genre, that they prefer. The ratings of a group member is used to find the most suitable movies for the user and the chosen genre is used to emphasize those movies, that are tagged with that particular genre.

For each user in the group, a recommendation list is calculated using ratings data from similar users in the database. The similarity between users is calculated using Pearson correlation.

The group recommendation list is combined from the individual group members' recommendation lists using Borda count method. From this group recommendation list, another recommendation list is combined in which the genres preferred by the group members are emphasized.
