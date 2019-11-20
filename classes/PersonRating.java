package classes;

// Each PersonRating includes a person id (= user id in the data set)
// and a rating
public class PersonRating {
    private int personId;
    private double rating;

    public PersonRating(int p, double r) {
        this.personId = p;
        this.rating = r;
    }

    public double getRating() {
        return this.rating;
    }

    public int getPersonId() {
        return this.personId;
    }

    public void setRating(int r) {
        this.rating = r;
    }

    public void setPersonId(int p) {
        this.personId = p;
    }

    @Override
    public String toString() {
        return "User " + this.personId + " gave a rating of " + this.rating;
    }
}