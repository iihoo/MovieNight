package classes;

// These UserSimilarities are linked to each MovienNightUser
// Each UserSimilarity includes a user id (= user in the data set) and a calculated similarity value
// between the user and the MovieNightUser
public class UserSimilarity implements Comparable<UserSimilarity>{
    private int userId;
    private double pearson;

    public UserSimilarity(int userId, double pearson) {
        this.userId = userId;
        this.pearson = pearson;
    }

    public int getUserId() {
        return this.userId;
    }

    public double getPearson() {
        return this.pearson;
    }

    @Override
    public int compareTo(UserSimilarity u) {
        if (this.pearson > u.pearson) {
            return -1;
        } else if (this.pearson < u.pearson) {
            return 1;
        } else {
            return 0;
        }
        
    }
    
    @Override
    public String toString() {
        return "similarity for user " +  this.userId +  " is " + this.pearson;
    }
    
}