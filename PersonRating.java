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

    public int getPerson() {
        return this.personId;
    }

    public void setRating(int r) {
        this.rating = r;
    }

    public void setPerson(int p) {
        this.personId = p;
    }

    public String toString() {
        return "User " + this.personId + " gave a rating of " + this.rating;
    }
}