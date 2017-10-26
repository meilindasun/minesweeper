import java.lang.Math;
import java.util.Random;

public class P1_Sun_Meilinda_MinesweeperModel
{
    final int xLen = 10;
    final int yLen = 10;
    static int numMines = 8;
    
    // '*' = mine
    // ' ' = empty
    char[][] gridActual = {
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '} , 
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                        };
    char[][] gridShown = {
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'} , 
                        {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'}
                        };
    
    public static final int[] yInc = {0, -1, -1, -1, 0, 1, 1, 1, 0};
    public static final int[] xInc = {0, -1, 0, 1, 1, 1, 0, -1, -1};
    
    public static final char MINE = '*';
    public static final char EMPTY = ' ';
    public static final char FLAG = 'F';
    public static final char COVERED = '-';
    public static final char QUESTION = '?';
    public static final char NULLCHAR = 'n';
    public static final char LOST = '!';
    
    public P1_Sun_Meilinda_MinesweeperModel()  {
        setMines();
        fillMine();
    }
    
    char[][] getActual()  {
        return gridActual;
    }
    
    private static char next(char curr)  {
        switch(curr)  {
            case FLAG:
                return QUESTION;
            case QUESTION:
                return COVERED;
            case COVERED:
                return FLAG;
        }
        return NULLCHAR;
    }
    
    char getNext(int currY, int currX) {
        return next(gridShown[currY][currX]);
    }
    
    char[][] getShown()  {
        return gridShown;
    }
    
    int getXLen()  {
        return xLen;
    }
    
    int getYLen()  {
        return yLen;
    }
    
    int getMineNum() {
        return numMines;
    }

    char[][] getGridShown()  {
        return gridShown;
    }
    
    char[][] getGridActual()  {
        return gridActual;
    }
    
    void moveMine(int y, int x)  {
       Random r = new Random();
       while (true) {
           int moveY = (int)(r.nextInt(yLen));
           int moveX = (int)(r.nextInt(xLen));
           if (gridActual[moveY][moveX] != MINE)  {
               gridActual[moveY][moveX] = MINE;
               break;
            }
        }
       gridActual[y][x] = EMPTY;
       fillMine();
    }
    
    boolean isEmpty()  {
        for (int i = 0; i < yLen; i++)  {
            for (int j = 0; j < xLen; j++)  {
                if (gridShown[i][j] != COVERED)  {
                    return false;
                }
            }
        }
        return true;
    }
    
    int getMinesLeft()  {
        int minesLeft = 0;
        for (int i = 0; i < yLen; i++)  {
            for (int j = 0; j < xLen; j++)  {
                if ((gridActual[i][j] == MINE || gridActual[i][j] == 'F')
                && (gridShown[i][j] == COVERED
                    || gridShown[i][j] == QUESTION))  {
                    minesLeft++;
                }
            }
        }
        return minesLeft;
    }
    
    void setMineNum(int num)  {
        if (num <= 0 || num >= xLen * yLen * 2 / 3)  {
            return;
        }
        numMines = num;
    }
    
    void setMines()  {
        Random r = new Random();
        for (int i = 0; i < numMines;)  {
            int y = (int)(r.nextInt(yLen));
            int x = (int)(r.nextInt(xLen));
            if (gridActual[y][x] != MINE)  {
                gridActual[y][x] = MINE;
                i++;
            }           
        }
    }
    
    void lose(int y, int x)  {
        gridShown[y][x] = LOST;
    }
    
    boolean checkBounds(int y, int x)  {
        if (y < 0 || y >= yLen || x < 0 || x >= xLen)  {
            return false;
        }
        return true;
    }
    
    boolean canVisit(int y, int x)  {
        if (!checkBounds(y, x) || gridShown[y][x] != COVERED)  {
            return false;
        }
        return true;
    }
    
    int countMines(int y, int x)  { 
        int incLen = yInc.length;
        int numMines = 0;
        for (int k = 0; k < incLen; k++)  {
            int nY = y + yInc[k];
            int nX = x + xInc[k];
            if (checkBounds(nY, nX))  {
                if (gridActual[nY][nX] == MINE
                    || gridActual[nY][nX] == FLAG)  {
                    numMines++;
                }
            }
        }
        return numMines;
    }
    
    void flag(int y, int x)  {
        gridShown[y][x] = FLAG;
    }
    
    void question(int y, int x)  {
        gridShown[y][x] = QUESTION;
    }
    
    void covered(int y, int x)  {
        gridShown[y][x] = COVERED;
    }
    
    void search(int y, int x)  {
        int len = yInc.length;
        for (int i = 0; i < len; i++)  {
            int newY = y + yInc[i];
            int newX = x + xInc[i];
            if (canVisit(newY, newX))  {
                if (gridActual[newY][newX] != MINE)  {
                    gridShown[newY][newX] = gridActual[newY][newX];
                }
                
                if (gridActual[newY][newX] == EMPTY)  {
                    search(newY, newX);
                }
            }
        }
    }
    
    void fillMine()  {
        for (int i = 0; i < yLen; i++)  {
            for (int j = 0; j < xLen; j++)  {
                if (gridActual[i][j] == MINE)  {
                    continue;
                }
                
                int numMines = countMines(i, j);
                if (numMines == 0)  {
                    gridActual[i][j] = EMPTY;
                }
                else  {
                    gridActual[i][j] = ("" + numMines).charAt(0);
                }
            }
        }
    }
    
    boolean win()  {
        for (int i = 0; i < yLen; i++)  {
            for (int j = 0; j < xLen; j++)  {
                if (gridActual[i][j] == FLAG 
                    || gridActual[i][j] == MINE)  {
                    if (gridShown[i][j] == COVERED)  {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
