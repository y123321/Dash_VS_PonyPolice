    package il.co.ovalley.dashvsponypolice.app.Controller;

    import android.app.Activity;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.RelativeLayout;
    import il.co.ovalley.dashvsponypolice.app.Model.*;
    import java.util.ArrayList;

    /**
     * Created by yuval on 16/04/2014.
     */
    public class GameRunnable implements Runnable {
        public int getClockCounter() {
            return m_ClockCounter;
        }

        private volatile int m_ClockCounter;
        private int m_ClockWait;
        private int m_SpawnTime;
        private ArrayList<Cop> m_cops;
        private ArrayList<Drop> m_drops;
        private ArrayList<Shot> m_shots;
        private Object m_PauseObject;
        private boolean m_Pause;
        private RelativeLayout m_layout;
        private volatile boolean m_IsGameRunning;
        private int m_counter;
        private volatile RainbowDash m_rainbowDash;

        public RainbowDash getRainbowDash() {
            return m_rainbowDash;
        }

        public GameRunnable(Activity activity,RelativeLayout layout){
            m_cops = new ArrayList<Cop>();
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
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            m_rainbowDash.setGoal(event.getX(), event.getY());
                            m_rainbowDash.changeDirection();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            m_rainbowDash.setGoal(event.getX(), event.getY());
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
            m_IsGameRunning=true;
            while (m_IsGameRunning){
                try {
                    if(m_Pause) synchronized (m_PauseObject){
                        m_PauseObject.wait();
                    }
                    m_rainbowDash.update();
                    new Thread(new CopsAndDropsRunnable(this,m_cops,m_drops)).start();
                    for(Drop drop :m_drops){
                        if (m_ClockCounter % drop.getWaitTime() == 0) drop.update();

                    }
                    new Thread(new ShotsRunnable(this,m_shots)).start();
                    if(m_ClockCounter%m_SpawnTime==0 && m_counter<40)spawnCop();
                    Thread.sleep(m_ClockWait);
                    m_ClockCounter++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


            private void spawnCop() {
            m_counter++;
                Activity activity=(Activity)m_layout.getContext();
                activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Cop cop=new Cop(m_layout);
                    m_cops.add(cop);
                }
            });
        }
        private void releaseDrop() {
            Drop drop=new Drop(m_layout);
            float x=m_rainbowDash.getX();
            float y=m_rainbowDash.getY();
            if(!m_rainbowDash.isRight()){
                x=x+m_rainbowDash.getWidth();
            }
            drop.setX(x);
            drop.setY(y);
            m_drops.add(drop);
   //         Log.d("test","drop!");
        }
        protected void shoot(Cop cop){
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
     //               Log.d("test","fire!");
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
