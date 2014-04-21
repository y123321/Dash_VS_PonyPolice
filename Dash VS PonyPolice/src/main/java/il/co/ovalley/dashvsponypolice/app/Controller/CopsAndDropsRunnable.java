package il.co.ovalley.dashvsponypolice.app.Controller;

import android.graphics.Rect;
import android.util.Log;
import il.co.ovalley.dashvsponypolice.app.Model.Cop;
import il.co.ovalley.dashvsponypolice.app.Model.Drop;

import java.util.ArrayList;

/**
 * Created by yuval on 21/04/2014.
 */
public class CopsAndDropsRunnable implements Runnable {
    private GameRunnable m_gameRunnable;
    private volatile ArrayList<Cop> m_cops;
    private volatile ArrayList<Drop> m_drops;
    public CopsAndDropsRunnable(GameRunnable gameRunnable,ArrayList<Cop> cops,ArrayList<Drop> drops){
        m_gameRunnable=gameRunnable;
        m_cops=cops;
        m_drops=drops;
    }
    @Override
    public void run() {
        int i=0;
        while(i<m_cops.size()){
            if(i<0){
                Log.d("test","cops iterator lower then zero. its " +i);

                i=0;
            }
            Cop cop=m_cops.get(i);
            i++;
            Rect copRect=new Rect();
            if(cop.isShooting()) m_gameRunnable.shoot(cop);
            if (m_gameRunnable.getClockCounter() % cop.getWaitTime() == 0) cop.update();
            cop.getHitRect(copRect);

            int j=0;

            while(j<m_drops.size()){
                if(j<0){
                    Log.d("test","drops iterator lower then zero. its " +j);

                    j=0;
                }
                Drop drop = m_drops.get(j);
                if(drop.isDead()){
                    unregister(drop);
                    j--;
                }
                else {
                    Rect dropRect = new Rect();
                    drop.getHitRect(dropRect);
                    if (dropRect.intersect(copRect)) {
                        Log.d("test", "HIT!!!");
                        killCop(cop, drop);
                        i--;
                        j--;

                    }
                }
                j++;
            }

        }
    }
    protected void killCop(Cop cop, Drop drop) {
        try{
            unregister(cop);
            unregister(drop);
        }
        catch (Exception e){
            e.printStackTrace();
            killCop(cop,drop);
        }

    }
    public void unregister(Cop cop){
        if(cop==null) return;
        if(m_cops.contains(cop))m_cops.remove(cop);
        cop.isDead(true);
        cop.update();
        cop=null;

    }
    public void unregister(Drop drop) {
        if(drop==null) return;
        if(m_drops.contains(drop))m_drops.remove(drop);
        drop.isDead(true);
        drop.update();
        drop=null;
    }
}
