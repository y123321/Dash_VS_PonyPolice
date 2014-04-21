package il.co.ovalley.dashvsponypolice.app.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
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
        m_xSpeed=1.5f;
        Log.d("test","getBottom: "+container.getBottom()+"getHeight: "+container.getHeight());
        setY(container.getBottom()-Common.random.nextFloat()*100-25/*-getPaddingBottom()*/);
        float i=(Common.random.nextFloat()+getPaddingRight())*m_container.getWidth()-getPaddingRight();
      //  Log.d("test", "x: "+i);
        setX(i);
        drwableState=0;
    //    this.setImageResource(R.drawable.police_pony_small_right);
        changeDirection();
    }
    private boolean m_isLoading;
    private boolean m_isShooting;
    private int m_LoadingTime;
    private int m_LoadingTimeCounter;
    private int drwableState;
     void changeDirection(){
        stepCounter=Common.random.nextInt(20);
        m_direction=Common.random.nextBoolean()?Direction.LEFT:Direction.RIGHT;


     //    AnimationDrawable CopAnimation=(AnimationDrawable)getDrawable();
     //    CopAnimation.start();

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
                animateCop();
                stepCounter--;
                if (stepCounter == 0) changeDirection();
                int rand=Common.random.nextInt(100);
                if(rand==30) {
                    shoot();
         //           Log.d("test","rand = "+rand);
                }
            }
        });
    }

    private void animateCop() {
        if(stepCounter%3==0)
        if(m_direction==Direction.LEFT){
            switch (drwableState){
                case 0:
                    setImageResource(R.drawable.police_pony_small_left1);
                    drwableState=1;
                    break;
                case 1:
                    setImageResource(R.drawable.police_pony_small_left2);
                    drwableState=0;
                    break;
            }
        }
        else switch (drwableState){
            case 0:
                setImageResource(R.drawable.police_pony_small_right1);
                drwableState=1;
                break;
            case 1:
                setImageResource(R.drawable.police_pony_small_right2);
                drwableState=0;
                break;
        }
        float x = getX();
        switch (m_direction) {
            case LEFT:
                if (x - 1 > getPaddingLeft()) {
                    setX(x - m_xSpeed);
                } else changeDirection();
                break;
            case RIGHT:
                if (x < m_container.getWidth() - m_container.getPaddingRight()) setX(x + m_xSpeed);
                else changeDirection();
                break;
        }
    }
}
