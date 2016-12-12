package uk.ac.cam.olb22.oop.tick5;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GUILife extends JFrame implements ListSelectionListener {

    private World mWorld;
    private java.util.List<World> mCachedWorlds;
    private PatternStore mStore;
    private GamePanel mGamePanel;

    private JButton mPlayButton;
    private boolean mPlaying;
    private Timer mTimer;

    public GUILife(PatternStore ps) throws PatternFormatException {
        super("Game of Life");
        mStore=ps;
        mCachedWorlds = new LinkedList<>();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,768);

        add(createPatternsPanel(),BorderLayout.WEST);
        add(createControlPanel(),BorderLayout.SOUTH);
        add(createGamePanel(),BorderLayout.CENTER);
    }

    private void addBorder(JComponent component, String title) {
        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch,title);
        component.setBorder(tb);
    }

    private JPanel createGamePanel() {
        mGamePanel = new GamePanel();
        addBorder(mGamePanel,"Game Panel");
        return mGamePanel;
    }

    private JPanel createPatternsPanel() {
        JPanel patt = new JPanel();
        addBorder(patt,"Patterns");
        patt.setLayout(new BorderLayout());

        List<Pattern> patterns = mStore.getPatternsNameSorted();
        JList pattList = new JList(patterns.toArray());
        pattList.addListSelectionListener(this::valueChanged);

        JScrollPane scrollPane = new JScrollPane(pattList);
        patt.add(scrollPane, BorderLayout.CENTER);
        return patt;
    }

    private JPanel createControlPanel() {
        JPanel ctrl =  new JPanel();
        addBorder(ctrl,"Controls");
        ctrl.setLayout(new GridLayout(1,3));

        JButton back = new JButton("< Back");
        back.addActionListener(e->moveBack());

        JButton forward = new JButton("Forward >");
        forward.addActionListener(e->moveForward());

        mPlayButton = new JButton("Play");
        mPlayButton.addActionListener(e->runOrPause());

        ctrl.add(back, BorderLayout.WEST);
        ctrl.add(mPlayButton, BorderLayout.CENTER);
        ctrl.add(forward, BorderLayout.EAST);
        return ctrl;
    }

    public void moveForward() {
        if (mCachedWorlds.size()>mWorld.getGenerationCount()+1) {
            mWorld = mCachedWorlds.get(mWorld.getGenerationCount()+1);
        } else {
            mWorld = copyWorld(true);
            mWorld.nextGeneration();
            mCachedWorlds.add(mWorld);
        }
        mGamePanel.display(mWorld);
    }

    public void moveBack() {
        try {
            mWorld = mCachedWorlds.get(mWorld.getGenerationCount() - 1);
            mGamePanel.display(mWorld);
        } catch (IndexOutOfBoundsException e) {
            mWorld = mWorld;
        }
    }

    private World copyWorld(boolean useCloning) {
        World result;
        if (useCloning) {
            result = (World) mWorld.clone();
        } else {
            if (mWorld instanceof PackedWorld) {
                result = new PackedWorld((PackedWorld) mWorld);
            } else if (mWorld instanceof ArrayWorld) {
                result = new ArrayWorld((ArrayWorld) mWorld);
            } else {
                result = null;
            }
        }
        return result;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<Pattern> list = (JList<Pattern>) e.getSource();
        Pattern pattern = list.getSelectedValue();
        // DONE
        // Based on size, create either a PackedWorld or ArrayWorld
        // from p. Clear the cache, set mWorld and put it into
        // the now-empty cache. Tell the game panel to display
        // the new mWorld.
        mCachedWorlds.clear();
        try {
            if (pattern.getHeight()*pattern.getWidth() <= 64) {
                mWorld = new PackedWorld(pattern);
            } else {
                mWorld = new ArrayWorld(pattern);
            }
        } catch (PatternFormatException er) {
            er.printStackTrace();
        }
        mGamePanel.display(mWorld);
        mCachedWorlds.add(mWorld);

        if (mPlaying) {
            runOrPause();
        }
    }

    private void runOrPause() {
        if (mPlaying) {
            mTimer.cancel();
            mPlaying=false;
            mPlayButton.setText("Play");
        }
        else {
            mPlaying=true;
            mPlayButton.setText("Stop");
            mTimer = new Timer(true);
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    moveForward();
                }
            }, 0, 500);
        }
    }

    public static void main(String[] args) {
        try {
            PatternStore ps = new PatternStore("http://www.cl.cam.ac.uk/teaching/1617/OOProg/ticks/life.txt");
            GUILife gui = new GUILife(ps);
            gui.setVisible(true);
        } catch (IOException | PatternFormatException e) {
            e.printStackTrace();
        }
    }
}