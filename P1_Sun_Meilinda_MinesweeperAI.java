import java.util.Random;
public class P1_Sun_Meilinda_MinesweeperAI  {
    final char COVERED = '-';
    final char EMPTY = ' ';
    final char REVEAL = 'R';
    final int THRESHOLD = 20;
    final char MINE = '*';
    final char FLAG = 'F';
    final int FLAGTHRESHOLD = 12;
    final static char NOMOVE = 'N';
    int yLen, xLen;
    char[][] myGridShown;
    public static final int[] yInc = {-1, -1, -1, 0, 1, 1, 1, 0};
    public static final int[] xInc = {-1, 0, 1, 1, 1, 0, -1, -1};
    
    public P1_Sun_Meilinda_MinesweeperAI(char[][] gridShown)  {
        myGridShown = gridShown;
        yLen = myGridShown.length;
        xLen = myGridShown[0].length;
    }
    
    void printGrid(char[][] grid)  {
        // means covered
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < grid.length; i++)  {
            System.out.print(i + " ");
            for (int j = 0; j < grid[0].length; j++)  {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.flush();
    }

    public class Move  {
        int myY, myX;
        char myAction;
        public Move(int y, int x, char action)  {
            myY = y;
            myX = x;
            myAction = action;
        }
        
        public void reset (int y, int x, char action)  {
            myY = y;
            myX = x;
            myAction = action;
        }
    }
    
    private int numC()  {
        int numCovered = 0;
        for (int i = 0; i < yLen; i++)  {
            for (int j = 0; j < xLen; j++)  {
                if (myGridShown[i][j] == COVERED)  {
                    numCovered++;
                }
            }
        }
        return numCovered;
    }
    
    private int percentRevealed()  {
        return (numC() / (yLen * xLen)) * 100;
    }
    
    private Move pickRandom()  {
       int n = numC();
       if (n <= 0)  {
           return null;
       }
       Random r = new Random();
       int valIndex = (int)(r.nextInt(n));
       int numCovered = 0;
       for (int i = 0; i < yLen; i++)  {
           for (int j = 0; j < xLen; j++)  {
               if (myGridShown[i][j] == COVERED)  {
                    numCovered++;
                    if (numCovered == valIndex)  {
                        return new Move(i, j, REVEAL);
                    }
               }
           }
       }
       return null;
    }
    
    boolean eligible(int y, int x)  {
        if (y < 0 || y >= yLen || x < 0 || x >= xLen)  {
            return false;
        }
        if (myGridShown[y][x] != COVERED)  {
            return false;
        }
        return true;
    }

    double numMines(int y, int x)  {
        if (y < 0 || y >= yLen || x < 0 || x >= xLen)  {
            return 0.2;
        }
        char c = myGridShown[y][x];
        double neighbors = 0;
        if (c >= '0' && c <= '9')  {
            neighbors = c - '0';
        }
        else if (c == MINE)  {
            neighbors = 0;
        }
        else if (c == ' ')  {
            neighbors = 0;
        }
        else if (c == COVERED)  {
            neighbors = 0.2;
        }
        return neighbors;
    }
    
    double score(int y, int x)  {
        double currScore = 0;
        for (int k = 0; k < yInc.length; k++)  {
            int newY = y + yInc[k];
            int newX = x + xInc[k];
            double numMines = numMines(newY, newX);
            currScore += numMines + (numMines > 0 ? 1 : 0);
        }
        return currScore;
    }
    
    private Move rank ()  {
        double lowestScore = 100000;
        Move lowestMove = new Move(-1, -1, NOMOVE);
        double highestScore = -1;
        Move highestMove = new Move(-1, -1, NOMOVE);
        Random random = new Random();
        for (int i = 0; i < yLen; i++)  {
            for (int j = 0; j < xLen; j++)  {
                if (!eligible(i, j))  {
                    continue;
                }
                double currScore = score (i, j);
                if (lowestScore > 100 || (currScore <= lowestScore && random.nextDouble() > 0.2))  {
                    lowestScore = currScore;
                    lowestMove.reset(i, j, REVEAL);
                }
                if (highestScore < 0 || (currScore > highestScore && random.nextDouble() > 0.2))  {
                    highestScore = currScore;
                    highestMove.reset(i, j, FLAG);
                }
            }
        }
        if (lowestMove.myAction == NOMOVE && highestMove.myAction == NOMOVE)  {
            return null;
        }
        if (lowestMove.myAction == NOMOVE)  {
            return highestMove;
        }
        if (highestMove.myAction == NOMOVE)  {
            return lowestMove;
        }
        if (lowestScore <= FLAGTHRESHOLD)  {
            return lowestMove;
        }
        else  {
            return highestMove;
        }
    }
    
    public Move getNextMove()  {
        return rank();      
    }
}
