package il.co.ovalley.dashvsponypolice.app.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import il.co.ovalley.dashvsponypolice.app.R;

/**
 * Created by yuval on 16/04/2014.
 */
public class Cop extends GameView {
    public boolean isShooting() {
        return m_isShooting;
    }

    public Cop(RelativeLayout container) {
        super(container);
        m_isShooting=false;
        m_LoadingTime=10;
        m_isLoading=false;

        setY(Common.random.nextFloat()*100+m_container.getHeight()-160);
       
        float i=Common.random.nextFloat()*m_container.getWidth();
      //  Log.d("test", "x: "+i);
        setX(i);
    //    this.setImageResource(R.drawable.police_pony_small_right);
        changeDirection();
    }
    private boolean m_isLoading;
    private boolean m_isShooting;
    private int m_LoadingTime;
    private int m_LoadingTimeCounter;
     void changeDirection(){
        stepCounter=Common.random.nextInt(20);
        m_direction=Common.random.nextBoolean()?Direction.LEFT:Direction.RIGHT;
        if(m_direction==Direction.LEFT)this.setImageResource(R.drawable.police_pony_small_left);
        else this.setImageResource(R.drawable.police_pony_small_right);


    }
    void shoot(){
        m_isLoading=true;
        m_LoadingTimeCounter=m_LoadingTime;
    }
    @Override
    public void update(){

        Activity activity=(Activity)getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isDead()) {
                    removeView();
                    return;
                }
                if (m_isShooting) {
                    m_isShooting = false;
                    return;
                }
                if (m_isLoading) {
                    if (m_LoadingTimeCounter == 0) {
                        m_isShooting = true;
                        m_isLoading = false;
                    } else m_LoadingTimeCounter--;
                    return;
                }
                float x = getX();
                switch (m_direction) {
                    case LEFT:
                        if (x - 1 > 20) {
                            setX(x - 1);
                        } else changeDirection();
                        break;
                    case RIGHT:
                        if (x < m_container.getWidth() - 20) setX(x + 1);
                        else changeDirection();
                        break;
                }
                stepCounter--;
                if (stepCounter == 0) changeDirection();
                int rand=Common.random.nextInt(100);
                if(rand==30) {
                    shoot();
                    Log.d("test","rand = "+rand);
                }
            }
        });
    }
}
