package classes;

// UserSimilarities are linked to each MovienNightUser
// Each UserSimilarity includes a user X in the database (userId is the user id in the database)
// and a calculated similarity value between the user X and the MovieNightUser
public class UserSimilarity implements Comparable<UserSimilarity> {
    private int userId;
    private double pearson;

    public UserSimilarity(int userId, double pearson) {
        this.userId = userId;
        this.pearson = pearson;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setPearson(double p) {
        this.pearson = p;
    }

    public double getPearson() {
        return this.pearson;
    }

    // the UserSimilarity classes are compared according to the pearson value of the UserSimilarity
    // and that is how it is possible to arrange a set of UserSimilarity-classes according to the Pearson similarity value
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
        return "similarity for user " + this.userId + " is " + this.pearson;
    }
}