    package il.co.ovalley.dashvsponypolice.app.Controller;

    import android.app.Activity;
    import android.content.Context;
    import android.graphics.Rect;
    import android.text.Layout;
    import android.util.Log;
    import android.view.MotionEvent;
    import android.view.SurfaceView;
    import android.view.View;
    import android.widget.RelativeLayout;
    import il.co.ovalley.dashvsponypolice.app.Model.*;

    import java.util.ArrayList;
    import java.util.concurrent.ThreadPoolExecutor;

    /**
     * Created by yuval on 16/04/2014.
     */
    public class GameRunnable implements Runnable {
        private int m_ClockCounter;
        private int m_ClockWait;
        private int m_SpawnTime;
        private ArrayList<Cop> m_Cops;
        private ArrayList<Drop> m_drops;
        private ArrayList<Shot> m_shots;
        private Object m_PauseObject;
        private boolean m_Pause;
        private RelativeLayout m_layout;
        private volatile boolean m_IsGameRunning;
        private int m_counter;
        private RainbowDash m_rainbowDash;
        public GameRunnable(Activity activity,RelativeLayout layout){
            m_Cops = new ArrayList<Cop>();
            m_drops=new ArrayList<Drop>();
            m_shots=new ArrayList<Shot>();
            m_layout=layout;
            m_ClockCounter=0;
            m_ClockWait=10;
            m_SpawnTime=500;
            m_IsGameRunning=false;
            m_Pause = false;
            m_PauseObject=new Object();
            m_counter=0;
            m_rainbowDash=new RainbowDash(m_layout);
            m_layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            m_rainbowDash.setGoal(event.getX(),event.getY());
                            m_rainbowDash.changeDirection();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            m_rainbowDash.setGoal(event.getX(),event.getY());
                            m_rainbowDash.changeDirection();
                            break;
                        case MotionEvent.ACTION_UP:
                            releaseDrop();
                            break;
                    }


                    return true;
                }
            });


        }


        @Override
        public void run() {
         //   Activity activity=(Activity)m_layout.getContext();
            m_IsGameRunning=true;
            while (m_IsGameRunning){
                try {
                    if(m_Pause) synchronized (m_PauseObject){
                        m_PauseObject.wait();
                    }
                    m_rainbowDash.update();
                    int i=0;
                    while(i<m_Cops.size()){
                    Cop cop=m_Cops.get(i);
                        Rect copRect=new Rect();
                        if(cop.isShooting()) shoot(cop);
                        if (m_ClockCounter % cop.getWaitTime() == 0) cop.update();
                        cop.getHitRect(copRect);
                        int j=0;
                        while(j<m_drops.size()){
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
                        i++;

                    }
                    for(Drop drop :m_drops){
                        if (m_ClockCounter % drop.getWaitTime() == 0) drop.update();

                    }
                    /*for(Shot shot:m_shots){
                        if(m_ClockCounter%shot.getWaitTime()==0)shot.update();
                    }*/
                    i=0;
                    while (i<m_shots.size()){
                        Shot shot=m_shots.get(i);
                        if(shot.isDead()){
                            unregister(shot);
                            i--;
                        }
                        else if(m_ClockCounter%shot.getWaitTime()==0)shot.update();
                        i++;

                    }
                    if(m_ClockCounter%m_SpawnTime==0)spawnCop();

                    Thread.sleep(m_ClockWait);
                    m_ClockCounter++;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private void killCop(Cop cop,Drop drop) {
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
            if(m_Cops.contains(cop))m_Cops.remove(cop);
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
        public void unregister(Shot shot) {
            if(shot==null) return;
            if(m_shots.contains(shot))m_shots.remove(shot);
       //     shot.isDead(true);
            shot.update();
            shot=null;
        }
            private void spawnCop() {
            m_counter++;
                Activity activity=(Activity)m_layout.getContext();
                activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Cop cop=new Cop(m_layout);
                    m_Cops.add(cop);
                    Log.d("test", "cop spawned "+m_counter);
                }
            });


        }
        private void releaseDrop() {

            Drop drop=new Drop(m_layout);
            drop.setX(m_rainbowDash.getX());
            drop.setY(m_rainbowDash.getY());
            m_drops.add(drop);
            Log.d("test","drop!");
        }
        private void shoot(Cop cop){
            cop.update();
            final float x=cop.getDirection()==Direction.LEFT?cop.getX()-cop.getWidth():cop.getX();
            final float y=cop.getY()-cop.getHeight();
            Activity activity=(Activity)m_layout.getContext();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Shot shot=new Shot(m_layout);
                    shot.setX(x);
                    shot.setY(y);
                    m_shots.add(shot);
  //                  _cop.update();
                    Log.d("test","fire!");
                }
            });


        }
        public void stopGame(){
            m_IsGameRunning=false;
        }
        public void pauseGame() {
            m_Pause = true;
        }
        public void resume(){
            m_Pause=false;
            m_PauseObject.notify();
        }

    }
