import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class P1_Sun_Meilinda_MinesweeperController
       implements ActionListener  {
    Scanner in = new Scanner(System.in);
    P1_Sun_Meilinda_MinesweeperModel myModel;
    P1_Sun_Meilinda_MinesweeperView myView;
    P1_Sun_Meilinda_MinesweeperAI myAI;
    
    public P1_Sun_Meilinda_MinesweeperController()  {
        myModel = new P1_Sun_Meilinda_MinesweeperModel();
        myView = new P1_Sun_Meilinda_MinesweeperView(myModel, this);
        myAI = new P1_Sun_Meilinda_MinesweeperAI(myModel.getGridShown());
    }
    
    void newGame()  {
        myModel = new P1_Sun_Meilinda_MinesweeperModel();
        myView.numMines.setText("" + myModel.getMinesLeft());
        myView.newGame(myModel);
        myAI = new P1_Sun_Meilinda_MinesweeperAI(myModel.getGridShown());
    }
    
    public void actionPerformed(ActionEvent e)  {
        String rep = e.getActionCommand();
        if (rep.equals("New Game"))  {
            newGame();
        }
        else if (rep.equals("Exit"))  {
            System.exit(0);
        }
        else if (rep.equals("How to Play"))  {
            myView.howToPlay();
        }
        else if (rep.equals("About"))  {
            myView.about();
        }
        else if (rep.equals("Set Number of Mines"))  {
            int numMines = myView.setNumMines(myModel.getMineNum());
            myModel.setMineNum(numMines);
        }
        else if (rep.equals("a"))  {
            myView.setUsedHint(true);
            if (myModel.win())  {
                return;
            }
            P1_Sun_Meilinda_MinesweeperAI.Move hintMove;
            hintMove = myAI.getNextMove();
            if (hintMove == null)  {
                return;
            }
            myView.runHint(hintMove.myY, hintMove.myX, hintMove.myAction);
        }
    }
}