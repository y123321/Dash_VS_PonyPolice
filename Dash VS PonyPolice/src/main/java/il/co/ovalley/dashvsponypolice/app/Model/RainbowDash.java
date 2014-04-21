package il.co.ovalley.dashvsponypolice.app.Model;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import il.co.ovalley.dashvsponypolice.app.R;

/**
 * Created by yuval on 18/04/2014.
 */
public class RainbowDash extends GameView {
    public RainbowDash(RelativeLayout container) {
        super(container);
        m_goingToX=150;
        m_goingToY=200;
        m_waitTime=1;
        m_xSpeed=2;
        setX(0);
        setY(0);
        changeDirection();
    }
    boolean isRight;

    public boolean isRight() {
        return isRight;
    }

    float m_goingToX;
    float m_goingToY;
    Direction m_directionVer;
    @Override
    public void update() {
        Activity activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isDead()){
                    AnimatorSet animatorSet=new AnimatorSet();
                    animatorSet.setDuration(2000);
                    animatorSet.setTarget(this);

     //               Log.d("test","RD was hit");
                    AlphaAnimation alpha=new AlphaAnimation(100,1);
                    alpha.setDuration(2000);
                    startAnimation(alpha);
                    isDead(false);
                    return;
                //    startAnimation(new AlphaAnimation(1,100));

                }
                float x = getX();
                float y = getY();
                float xPoint=m_xSpeed>1?m_xSpeed:1;
                float yPoint=m_ySpeed>1?m_ySpeed:1;
                if (Math.abs(x - m_goingToX) < xPoint) m_direction = Direction.STOP;
                if (Math.abs(y - m_goingToY) < yPoint) m_directionVer = Direction.STOP;
         //       if (x > 0 && x < m_container.getWidth())
                    switch (m_direction) {
                    case RIGHT:
                        setX(x + m_xSpeed);
                        break;

                    case LEFT:
                        setX(x - m_xSpeed);
                        break;
                }

                    switch (m_directionVer){
                            case DOWN:
                                setY(y+m_ySpeed*m_xSpeed);
                                break;
                            case UP:
                                setY(y-m_ySpeed*m_xSpeed);
                                break;
                        }
      //              Log.d("test","dash going "+m_direction+" "+m_directionVer.toString()+" x: "+getX()+"  y: "+getY()+ "  m: "+m+"  m*x: "+m*x);

            }
        });

    }
    @Override
    public void changeDirection() {
        float x=getX();
        float y=getY();
        m_ySpeed=Math.abs((y - m_goingToY) / (x - m_goingToX));
    //    Log.d("test","m = "+m_ySpeed);
        m_ySpeed=m_ySpeed>2?2:m_ySpeed;
        float rotation=(float)Math.toDegrees(Math.atan(m_ySpeed));

        if(x<m_goingToX) {
            m_direction = Direction.RIGHT;
            setImageResource(R.drawable.rainbow_dash_small_right);
            isRight=true;

        }
        else{
            m_direction = Direction.LEFT;
            setImageResource(R.drawable.rainbow_dash_small_left);
            rotation=-rotation;
            isRight=false;

        }
        AnimationDrawable RDAnimation=(AnimationDrawable)getDrawable();
        RDAnimation.start();
        if(y<m_goingToY) {
            m_directionVer = Direction.DOWN;
            setRotation(rotation);
        }
        else{
            m_directionVer = Direction.UP;
            setRotation(-rotation);
        }
    }

    public void setGoal(float x, float y) {
        m_goingToY=y;
        m_goingToX=x;

    }

}