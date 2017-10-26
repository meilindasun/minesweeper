/* Meilinda Sun, P1, 3/20/16
 * Time Taken: 9 hours (not aggregate)
 * I used "probability" in order to figure out
 * whether I needed to flag or reveal a certain
 * space. However, my "probability" did not range
 * from 0 to 1. It was much larger than in that
 * range. I used 0.2 as a stock value for any
 * uncovered square because I was trying to estimate
 * the number of mines it neighbors, and since
 * most squares will typically have 0 neighbors
 * then 0.2 is the "stock" value. Then I "weighed"
 * the square that was least likely to be a mine
 * with the one that was most likely, and depending
 * on conditions, I decided whether I would choose
 * to flag one square or reveal the other.
 * 
 */
public class P1_Sun_Meilinda_MinesweeperDriver
{
    public static void main(String[] args)  {
        P1_Sun_Meilinda_MinesweeperController myController = new P1_Sun_Meilinda_MinesweeperController();
        myController.myModel.fillMine();
    }
}
