import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.Timer;
import javax.swing.JComponent;

public class P1_Sun_Meilinda_MinesweeperView  implements ActionListener {
    P1_Sun_Meilinda_MinesweeperModel myModel;
    final P1_Sun_Meilinda_MinesweeperController myController;
    
    Scanner in = new Scanner(System.in);
    
    static final char REVEAL = 'R';
    static final char FLAG = 'F';
    static final char QUESTION = '?';
    static final char COVERED = '-';
    static final String CHEAT = "CHEAT";
    HashMap<Character, BufferedImage> img = new HashMap<Character, BufferedImage>();
    MyDrawingPanel myPanel;
    boolean finished = false;
    boolean usedHint = false;
    Color color = Color.RED;
    MyDrawingPanel drawingPanel;
    Color emptyCol = Color.DARK_GRAY;
    JFrame window;
    long aTime;
    JTextField time;
    Timer timer;
    JTextField numMines;
    
    public boolean getUsedHint()  {
        return usedHint;
    }
    
    public void setUsedHint(boolean rep)  {
        usedHint = rep;
    }

    P1_Sun_Meilinda_MinesweeperView (P1_Sun_Meilinda_MinesweeperModel model,
    P1_Sun_Meilinda_MinesweeperController controller) {
        myModel = model;
        myController = controller;
        
        imageLoop();
        
        // Create Java Window
        window = new JFrame("SimpleDraw");
        window.setBounds(100, 100, 445, 600);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
            KeyStroke.getKeyStroke("A"), CHEAT);
        window.getRootPane().getActionMap().put(CHEAT, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                myController.actionPerformed(e);
            }
        });
        // Create GUI elements

        // JPanel to draw in
        drawingPanel = new MyDrawingPanel(this);
        drawingPanel.setBounds(20, 20, 400,400);
        drawingPanel.setBorder(BorderFactory.createEtchedBorder());
        MyMouseListener mouseListener = new MyMouseListener(drawingPanel);
        drawingPanel.addMouseListener(mouseListener);
        drawingPanel.addMouseMotionListener(mouseListener);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu game  = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem exit = new JMenuItem("Exit");
        game.add(newGame);
        game.add(exit);
        newGame.addActionListener(myController);
        exit.addActionListener(myController);
        menuBar.add(game);
        
        JMenu options = new JMenu("Options");
        JMenuItem setNumMines = new JMenuItem("Set Number of Mines");
        options.add(setNumMines);
        menuBar.add(options);
        setNumMines.addActionListener(myController);
        
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        JMenuItem howToPlay = new JMenuItem("How to Play");
        help.add(about);
        help.add(howToPlay);
        about.addActionListener(myController);
        howToPlay.addActionListener(myController);
        menuBar.add(help);
        
        window.setJMenuBar(menuBar);

        // Add GUI elements to the Java window's ContentPane
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.add(drawingPanel);
        window.getContentPane().add(mainPanel);

        JPanel timeElapsed = new JPanel();
        time = new JTextField("0     ");
        timeElapsed.add(time);
        aTime = System.currentTimeMillis();
        timer = new Timer(1000, this);
        timer.start();
        timeElapsed.setBorder(BorderFactory.createTitledBorder("Time Elapsed"));
        timeElapsed.setBounds(25, 445, 175, 80);
        mainPanel.add(timeElapsed);
        time.setEditable(false);
         
        JPanel mines = new JPanel();
        numMines = new JTextField(myModel.numMines + "    ");
        mines.add(numMines);
        mines.setBorder(BorderFactory.createTitledBorder("Mines"));
        mines.setBounds(225, 445, 175, 80);
        mainPanel.add(mines);
        numMines.setEditable(false);
        
        // Let there be light
        window.setVisible(true);

    }
    
    public void actionPerformed(ActionEvent e)  {
        time.setText("" + ((System.currentTimeMillis() - aTime) / 1000));
    }
    
    void newGame(P1_Sun_Meilinda_MinesweeperModel mod)  {
        timer.restart();
        aTime = System.currentTimeMillis();
        finished = false;
        myModel = mod;
        drawingPanel.repaint();
    }
    
    void flagLoop(int y, int x, char c)  {
        if (c == FLAG)  {
            myModel.flag(y, x);
            if (myModel.win())  {
                if (usedHint)  {
                    JOptionPane.showMessageDialog(window, "Congratulations! You won with the computer.");
                }
                else  {
                    JOptionPane.showMessageDialog(window, "Congratulations! You won.");
                }
                finished = true;
            }
        }
        
        else if (c == QUESTION)  {
            myModel.question(y, x);
            if (myModel.win())  {
                if (usedHint)  {
                    JOptionPane.showMessageDialog(window, "Congratulations! You won with the computer.");
                }
                else  {
                    JOptionPane.showMessageDialog(window, "Congratulations! You won.");
                }
                finished = true;
            }
        }
        
        else if (c == COVERED)  {
            myModel.covered(y, x);
            if (myModel.win())  {
                if (usedHint)  {
                    JOptionPane.showMessageDialog(window, "Congratulations! You won with the computer.");
                }
                else  {
                    JOptionPane.showMessageDialog(window, "Congratulations! You won.");
                }
                finished = true;
            }
        }
    }
    
    void runSweep(int y, int x, char c) {
        if (!myModel.canVisit(y, x))  {
            return;
        }
        
        else if (myModel.canVisit(y, x) && c == REVEAL && myModel.getActual()[y][x] == P1_Sun_Meilinda_MinesweeperModel.MINE)  {
            if (myModel.isEmpty())  {
                myModel.moveMine(y, x);
                myModel.search(y, x);
            }
            else  {
                timer.stop();
                myModel.lose(y, x);
                drawingPanel.repaint();
                if (usedHint)  {
                    JOptionPane.showMessageDialog(window, "Sorry, you lost with the computer.");
                }
                else  {
                    JOptionPane.showMessageDialog(window, "Sorry, you lost.");
                }
                finished = true;
                return;
            }
        }

        else  {
            myModel.search(y, x);
            if (myModel.win())  {
                JOptionPane.showMessageDialog(window, "Congratulations! You won.");
                finished = true;
            }
        }
    }
    
    void printActual()  {
        // means covered
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < myModel.getYLen(); i++)  {
            System.out.print(i + " ");
            for (int j = 0; j < myModel.getXLen(); j++)  {
                System.out.print(myModel.getActual()[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    void printGrid()  {
        // means covered
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < myModel.getYLen(); i++)  {
            System.out.print(i + " ");
            for (int j = 0; j < myModel.getXLen(); j++)  {
                System.out.print(myModel.getShown()[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    private class MyDrawingPanel extends JPanel  {
        public static final int GRIDLEN = 40;

        // Not required, but gets rid of the serialVersionUID warning.  Google it, if desired.
        static final long serialVersionUID = 1234567890L;
        P1_Sun_Meilinda_MinesweeperView myGui;
        
        public MyDrawingPanel(P1_Sun_Meilinda_MinesweeperView gui)  {
            myGui = gui;
        }
        
        private void drawCell(int x, int y, Graphics g)  {
            g.drawImage(img.get(myModel.gridShown[y][x]), x * GRIDLEN, y * GRIDLEN, GRIDLEN, GRIDLEN, null);
        }
        
        public void paintComponent(Graphics g) {
            g.setColor(emptyCol);
            g.fillRect(2, 2, this.getWidth() - 2, this.getHeight() - 2);
            
            for (int y = 0; y < myModel.getYLen(); y += 1)  {
               for (int x = 0; x < myModel.getXLen(); x += 1)  {
                    drawCell(y, x, g);
                }
            }
            
            g.setColor(Color.LIGHT_GRAY);
            for (int y = 0; y < myModel.getYLen(); y += 1)
                g.drawLine(0, y * GRIDLEN, myModel.getXLen() * GRIDLEN, y * GRIDLEN);
                
            for (int x = 0; x < myModel.getXLen(); x += 1)
                g.drawLine(x * GRIDLEN, 0, x * GRIDLEN, myModel.getYLen() * GRIDLEN);
        }
    }
    
    void imageLoop()  {
        img.put('-', imageLoad("blank.gif"));
        img.put(' ', imageLoad("num_0.gif"));
        img.put('*', imageLoad("bomb_revealed.gif"));
        img.put('F', imageLoad("bomb_flagged.gif"));
        img.put('1', imageLoad("num_1.gif"));
        img.put('2', imageLoad("num_2.gif"));
        img.put('3', imageLoad("num_3.gif"));
        img.put('4', imageLoad("num_4.gif"));
        img.put('5', imageLoad("num_5.gif"));
        img.put('6', imageLoad("num_6.gif"));
        img.put('7', imageLoad("num_7.gif"));
        img.put('8', imageLoad("num_8.gif"));
        img.put('?', imageLoad("bomb_question.gif"));
        img.put('!', imageLoad("bomb_death.gif"));
    }
    
    BufferedImage imageLoad(String picName)  {
        try  {
            URL url = getClass().getClassLoader().getResource(picName);
            return ImageIO.read(url);
        }
        catch (IOException e)  {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    void howToPlay()  {
        JEditorPane helpContent;
        try  {
            URL url = getClass().getClassLoader().getResource("howtoplay.html");
            helpContent = new JEditorPane(url);
            helpContent.setContentType("text/html");
            helpContent.setEditable(false);
            JScrollPane helpPane = new JScrollPane(helpContent);
            JOptionPane.showMessageDialog(window, helpPane, 
                                    "How To Play", 
                                    JOptionPane.PLAIN_MESSAGE, null);
        }
        catch (MalformedURLException e)  {
            System.out.println(e.getMessage());
        }
        catch (IOException e)  {
            System.out.println(e.getMessage());
        }
    }
    
    void about()  {
        JEditorPane aboutContent;
        try  {
            URL url = getClass().getClassLoader().getResource("about.html");
            aboutContent = new JEditorPane(url);
            JScrollPane aboutPane = new JScrollPane(aboutContent);
            JOptionPane.showMessageDialog(window, aboutPane, 
                                    "About", 
                                    JOptionPane.PLAIN_MESSAGE, null);
        }
        catch (MalformedURLException e)  {
            System.out.println(e.getMessage());
        }
        catch (IOException e)  {
            System.out.println(e.getMessage());
        }
    }
    
    int setNumMines(int mines)  {
        JEditorPane numMineContent;
        String rep = (String)JOptionPane.showInputDialog(window, "Set number of Mines", "Number of Mines: ", 
                JOptionPane.PLAIN_MESSAGE, null, null, "" + mines);
        if (rep == null)  {
            return -1;
        }
        int ans = Integer.parseInt(rep);
        return ans;
    }
    
    class MyMouseListener implements  MouseListener, 
                                      MouseMotionListener  {
        public MyMouseListener(MyDrawingPanel panel)  {
        }
        
        public void mouseClicked (MouseEvent e)  {
            int currY = e.getY() / MyDrawingPanel.GRIDLEN;
            int currX = e.getX() / MyDrawingPanel.GRIDLEN;
            char c;
            if (finished)  {
                return;
            }
            if (SwingUtilities.isMiddleMouseButton(e))  {
                return;
            }
            else if (SwingUtilities.isLeftMouseButton(e))  {
                runSweep(currY, currX, REVEAL);
                drawingPanel.repaint();
                numMines.setText("" + myModel.getMinesLeft());
            }
            else  {
                c = myModel.getNext(currY, currX);
                flagLoop(currY, currX, c);
                drawingPanel.repaint();
                numMines.setText("" + myModel.getMinesLeft());
            }
        }
    
        public void mouseEntered (MouseEvent e)  {
        }
        public void mouseExited (MouseEvent e)  {
        }
        public void mousePressed (MouseEvent e)  {
        }
        public void mouseReleased (MouseEvent e)  {
        }
        public void mouseDragged (MouseEvent e)  {
        }
        public void mouseMoved(MouseEvent e)  {
        } 
    }
    
    public void runHint(int currY, int currX, char action)  {
        Point pt = new Point((int) ((currX + 0.5) * MyDrawingPanel.GRIDLEN), (int) ((currY + 0.5) * MyDrawingPanel.GRIDLEN));
        SwingUtilities.convertPointToScreen(pt, drawingPanel);
        try  {
            Robot robot = new Robot();
            robot.mouseMove((int) pt.getX(), (int) pt.getY());
            Thread.sleep(250);
        }
        catch (Exception e)  {
            System.out.println(e.getMessage());
        }
        if (action == REVEAL)  {
            runSweep(currY, currX, action);
        }
        else  {
            char c = myModel.getNext(currY, currX);
            flagLoop(currY, currX, c);
        }
        drawingPanel.repaint();
        numMines.setText("" + myModel.getMinesLeft());
    }
}