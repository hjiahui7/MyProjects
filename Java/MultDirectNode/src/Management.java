
/**
 * use two hashtable and a orthogonalist to manage the movie review
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.20
 */
public class Management {

    private HashTable<String, Node<Integer>> movies;
    private HashTable<String, Node<Integer>> reviewers;
    private OrthogonalList<Integer> list;


    /**
     * constractor, create the manage system based on the size given
     *
     * @param hashSize
     *            the size of the memory memager
     */
    public Management(int hashSize) {
        movies = new HashTable<>(hashSize, "Movie");
        reviewers = new HashTable<>(hashSize, "Reviewer");
        list = new OrthogonalList<Integer>();
    }


    /**
     * add movie review into the database based on the movie name reviewers and
     * score.
     *
     * @param localReviewers
     *            reviewers' name
     * @param localMovies
     *            movie name 
     * @param score
     *            score of the movie
     * @return add status success if true
     */
    public boolean add(String localReviewers, String localMovies, int score) {
        StringBuilder str = new StringBuilder();
        if (score > 10 || score < 1) {
            str.append("Bad score |").append(score).append(
                "|. Scores must be between 1 and 10.");
            System.out.println(str.toString());
            return false;
        }
        int y = this.movies.contains(localMovies);
        int x = this.reviewers.contains(localReviewers);
        Node<Integer> aMovie = null;
        Node<Integer> aReview = null;
        boolean movieFind = false;
        boolean reviewersFind = false;
        if (x != -1) {
            aReview = this.reviewers.getData(x).getValue();
            reviewersFind = true;
        }
        if (y != -1) {
            aMovie = this.movies.getData(y).getValue();
            movieFind = true;
        }
        @SuppressWarnings("unchecked")
        Node<Integer>[] twoNode = list.insert(aReview, aMovie, score);
        if (!reviewersFind) {
            aReview = twoNode[0];
            this.reviewers.add(localReviewers, aReview);
        }
        if (!movieFind) {
            aMovie = twoNode[1];
            this.movies.add(localMovies, aMovie);
        }
        str.append("Rating added: |").append(localReviewers).append("|, |")
            .append(localMovies).append("|, ").append(score);
        System.out.println(str.toString());
        return true;
    }


    /**
     * remove the reviewer based on its name
     *
     * @param localReviewers
     *            the name of the reviewer
     * @return if successfuly deleted
     */
    public boolean deleteReviewer(String localReviewers) {
        StringBuilder str = new StringBuilder();
        int indexOfHashtable = this.reviewers.contains(localReviewers);
        if (indexOfHashtable == -1) {
            str.append("|").append(localReviewers).append(
                "| not deleted because it does "
                    + "not exist in the Reviewer database.");
            System.out.println(str.toString());
            return false;
        }
        else {
            Node<Integer> col = this.reviewers.getData(indexOfHashtable)
                .getValue();
            this.reviewers.remove(localReviewers);
            // remove from hashtable
            list.removeRow(col);
            // remove from list
            str.append("|").append(localReviewers).append(
                "| has been deleted from the " + "Reviewer database.");
            System.out.println(str.toString());
        }
        return true;
    }


    /**
     * remove the movie based on its name
     *
     * @param movie
     *            the name of the movie
     * @return if successfuly deleted
     */
    public boolean deleteMovie(String movie) {
        StringBuilder str = new StringBuilder();
        int indexOfHashtable = this.movies.contains(movie);
        if (indexOfHashtable == -1) {
            str.append("|").append(movie).append(
                "| not deleted because it does not "
                    + "exist in the Movie database.");
            System.out.println(str.toString());
            return false;
        }
        else {
            list.removeCol(this.movies.getData(indexOfHashtable).getValue());
            this.movies.remove(movie);
            // remove from hashtable

            // remove from list
            str.append("|").append(movie).append("| has been deleted from the "
                + "Movie database.");
            System.out.println(str.toString());
        }
        return true;
    }


    /**
     * print the hashmap which contain movie information and the other one which
     * contain reviewer information
     *
     * @param name
     *            string to locate which hashmap required to print
     */
    public void printHashTable(String name) {
        if (name.equals("movie")) {
            System.out.println("Movies:");
            System.out.println(this.movies.toString());
        }
        if (name.equals("reviewer")) {
            System.out.println("Reviewers:");
            System.out.println(this.reviewers.toString());
        }
    }


    /**
     * output all exist ratings store in the list
     */
    @SuppressWarnings("unchecked")
    public void printRatings() {
        if (this.movies.isEmpty() || this.reviewers.isEmpty()) {
            System.out.println("There are no ratings " + "in the database");
            return;
        }
        Node<Integer>[] moviesIndexs = this.list.getAllYNode(list.getCerter());
        Node<Integer>[] reviewersIndexs = this.list.getAllXNode(list
            .getCerter());
        for (int i = 0; i < reviewersIndexs.length; i++) {
            StringBuilder str = new StringBuilder();
            int index = this.reviewers.findValue(reviewersIndexs[i]);
            String name = this.reviewers.getData(index).getKey();
            str.append(name).append(": ").append(reviewersIndexs[i].getxId());
            System.out.println(str.toString());
        }
        for (int i = 0; i < moviesIndexs.length; i++) {
            StringBuilder str = new StringBuilder();
            String data = list.toStringForOneCol(moviesIndexs[i]);
            int index = this.movies.findValue(moviesIndexs[i]);
            str.append(this.movies.getData(index).getKey()).append(": ");
            str.append(data);
            System.out.println(str.toString());
        }

    }


    /**
     * print all the ratings for one movie
     *
     * @param movie
     *            movie name
     */
    @SuppressWarnings("unchecked")
    public void listMovies(String movie) {
        int index = movies.contains(movie);
        StringBuilder str = new StringBuilder();
        if (index == -1) {
            str.append("Cannot list, movie |").append(movie).append(
                "| not found in the database.");
            System.out.println(str.toString());
            return;
        }
        Node<Integer>[] data = list.getAllXNode(movies.getData(index)
            .getValue());
        str.append("Ratings for movie |").append(movie).append("|:");
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                Node<Integer> header = list.getXHeader(data[i]);
                str.append("\n" + this.reviewers.getData(this.reviewers
                    .findValue(header)).getKey());
                str.append(": ").append(data[i].getData());
            }
        }
        System.out.println(str.toString());
    }


    /**
     * print all reviews from a reviewer
     *
     * @param reviewer
     *            reviewer's name
     */
    @SuppressWarnings("unchecked")
    public void listReviewers(String reviewer) {
        int index = this.reviewers.contains(reviewer);
        StringBuilder str = new StringBuilder();
        if (index == -1) {
            str.append("Cannot list, reviewer |").append(reviewer).append(
                "| not found in the database.");
            System.out.println(str.toString());
            return;
        }
        Node<Integer>[] data = list.getAllYNode(this.reviewers.getData(index)
            .getValue());
        str.append("Ratings for reviewer |").append(reviewer).append("|:");
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                Node<Integer> header = list.getYHeader(data[i]);
                str.append("\n" + this.movies.getData(this.movies.findValue(
                    header)).getKey());
                str.append(": ").append(data[i].getData());
            }
        }
        System.out.println(str.toString());
    }


    /**
     * looking for similar movie
     *
     * @param movie
     *            movie name
     */
    @SuppressWarnings("unchecked")
    public void similarMovie(String movie) {
        int index = this.movies.contains(movie);
        StringBuilder str = new StringBuilder();
        if (index == -1) {
            str.append("Movie |").append(movie).append(
                "| not found in the database.");
            System.out.println(str.toString());
            return;
        }
        double similarScore = 0;
        Node<Integer> best = null;
        Node<Integer>[] themovies = this.list.getAllXNode(this.movies.getData(
            index).getValue());
        index = this.movies.getData(index).getValue().getyId();
        Node<Integer> cur = list.getCerter();
        cur = cur.getDown();
        while (cur != null) {
            if (cur.getyId() == index) {
                cur = cur.getDown();
                continue;
            }
            Node<Integer>[] otherone = list.getAllXNode(cur);
            if (otherone == null) {
                cur = cur.getDown();
                continue;
            }
            double score = getSimilarMovieScore(themovies, otherone);
            if (score > similarScore) {
                best = otherone[0].getLeft();
                similarScore = score;
            }
            cur = cur.getDown();
        }
        if (best == null) {
            System.out.println("There is no movie similar to |" + movie + "|");
            return;
        }
        similarScore = 10 - similarScore;
        String name = this.movies.getData(this.movies.findValue(best)).getKey();
        str.append("The movie |").append(name).append("| is similar to |")
            .append(movie).append("| with score ").append(String.format(
                "%1$.2f", similarScore));
        System.out.println(str.toString());
    }


    /**
     * help method to locate similar movie
     *
     * @param themovies
     *            movie node array
     * @param otherone
     *            other node array
     * @return the number
     */
    private double getSimilarMovieScore(
        Node<Integer>[] themovies,
        Node<Integer>[] otherone) {
        int sum = 0;
        int count = 0;
        for (int i = 0, j = 0; i < themovies.length && j < otherone.length;) {
            if (themovies[i].getxId() < otherone[j].getxId()) {
                i++;
            }
            else if (themovies[i].getxId() > otherone[j].getxId()) {
                j++;
            }
            else {
                sum += Math.abs(themovies[i].getData() - otherone[j].getData());
                count++;
                i++;
                j++;
            }
        }
        return 10 - (double)sum / (double)count;
    }


    /**
     * looking for similar reviewer
     *
     * @param reviewer
     *            reviewer name
     */
    @SuppressWarnings("unchecked")
    public void similarReviewers(String reviewer) {
        int index = this.reviewers.contains(reviewer);
        StringBuilder str = new StringBuilder();
        if (index == -1) {
            str.append("Reviewer |").append(reviewer).append(
                "| not found in the database.");
            System.out.println(str.toString());
            return;
        }
        double similarScore = 0;
        Node<Integer> best = null;
        Node<Integer>[] thereviewers = this.list.getAllYNode(this.reviewers
            .getData(index).getValue());
        index = this.reviewers.getData(index).getValue().getxId();
        Node<Integer> cur = list.getCerter();
        cur = cur.getRight();
        while (cur != null) {
            if (cur.getxId() == index) {
                cur = cur.getRight();
                continue;
            }
            Node<Integer>[] otherone = list.getAllYNode(cur);
            if (otherone == null) {
                cur = cur.getRight();
                continue;
            }
            double score = getSimilarReviewScore(thereviewers, otherone);
            if (score > similarScore) {
                best = otherone[0].getUp();
                similarScore = score;
            }
            cur = cur.getRight();
        }
        if (best == null) {
            System.out.println("There is no reviewer similar to |" + reviewer
                + "|");
            return;
        }
        String name = this.reviewers.getData(this.reviewers.findValue(best))
            .getKey();
        similarScore = 10 - similarScore;
        str.append("The reviewer |").append(name).append("| is similar to |")
            .append(reviewer).append("| with score ").append(String.format(
                "%1$.2f", similarScore));
        System.out.println(str.toString());
    }


    /**
     * helper method to locate similar reviewer
     *
     * @param reviewer
     *            node array of reviewer
     * @param otherone
     *            other node array
     * @return score number
     */
    private double getSimilarReviewScore(
        Node<Integer>[] reviewer,
        Node<Integer>[] otherone) {
        int sum = 0;
        int count = 0;
        for (int i = 0, j = 0; i < reviewer.length && j < otherone.length;) {
            if (reviewer[i].getyId() < otherone[j].getyId()) {
                i++;
            }
            else if (reviewer[i].getyId() > otherone[j].getyId()) {
                j++;
            }
            else {
                sum += Math.abs(reviewer[i].getData() - otherone[j].getData());
                count++;
                i++;
                j++;
            }
        }
        return 10 - (double)sum / (double)count;
    }
}
