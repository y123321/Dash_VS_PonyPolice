package il.co.ovalley.dashvsponypolice.app.Controller;

import android.graphics.Rect;
import android.util.Log;
import il.co.ovalley.dashvsponypolice.app.Model.Shot;

import java.util.ArrayList;

/**
 * Created by yuval on 21/04/2014.
 */
public class ShotsRunnable implements Runnable {
    private volatile ArrayList<Shot> m_shots;
    private GameRunnable m_gameRunnable;
    public ShotsRunnable(GameRunnable gameRunnable,ArrayList<Shot> shots){
        m_shots=shots;
        m_gameRunnable=gameRunnable;
    }
    @Override
    public void run() {
        int i=0;
        Rect RDRect=new Rect();
        m_gameRunnable.getRainbowDash().getHitRect(RDRect);
        while (i<m_shots.size()){
            if(i<0){
                Log.d("test","shots iterator lower then zero. its " +i);
                i=0;
            }
            Shot shot=m_shots.get(i);
            if(shot.isDead()){
                unregister(shot);
                i--;
            }
            else{
                if(m_gameRunnable.getClockCounter()%shot.getWaitTime()==0)shot.update();
                Rect shotRect=new Rect();
                shot.getHitRect(shotRect);
                if(shotRect.intersect(RDRect)) hitDash(shot);
            }
            i++;
        }
    }
    public void hitDash(Shot shot) {
        unregister(shot);
        m_gameRunnable.getRainbowDash().isDead(true);
    }
    public void unregister(Shot shot) {
        if(shot==null) return;
        if(m_shots.contains(shot))m_shots.remove(shot);
        else Log.d("test", "Shot unregisterred. why?");
        shot.isDead(true);
        //     shot.isDead(true);
        shot.update();
        shot=null;
    }
}
