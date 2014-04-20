package il.co.ovalley.dashvsponypolice.app.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
        setX(0);
        setY(0);
     //   setY((getY()-m_goingToY)/(getX()-m_goingToX)*getX());
        changeDirection();
    }
    private float m_yAdvance;
    float m_goingToX;
    float m_goingToY;
    Direction m_directionVer;
    @Override
    public void update() {
        Activity activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float x = getX();
                float y = getY();
                if (Math.abs(x - m_goingToX) < 1) m_direction = Direction.STOP;
                if (Math.abs(y - m_goingToY) < 1) m_directionVer = Direction.STOP;
         //       if (x > 0 && x < m_container.getWidth())
                    switch (m_direction) {
                    case RIGHT:
                        setX(x + 1);
                        break;

                    case LEFT:
                        setX(x - 1);
                        break;
                }
              //      float newY = m * x+n;
            //            if (newY > 0 && newY < m_container.getHeight() - 200 &&Math.abs(getY()-newY)<15) setY(newY);
              //  else
                    switch (m_directionVer){
                            case DOWN:
                                setY(y+m_yAdvance);
                                break;
                            case UP:
                                setY(y-m_yAdvance);
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
        m_yAdvance=Math.abs((y - m_goingToY) / (x - m_goingToX));
        Log.d("test","m = "+m_yAdvance);
        m_yAdvance=m_yAdvance>2?2:m_yAdvance;
        float rotation=(float)Math.toDegrees(Math.atan(m_yAdvance));

        if(x<m_goingToX) {
            m_direction = Direction.RIGHT;
            setImageResource(R.drawable.rainbow_dash_small_right);
           // setRotation((float)Math.toDegrees(Math.atan(m_yAdvance)));
        }
        else{
            m_direction = Direction.LEFT;
            setImageResource(R.drawable.rainbow_dash_small_left);
            rotation=-rotation;
    //       setRotation((float)Math.toDegrees(Math.atan(m_yAdvance)));

        }
        if(y<m_goingToY) {
            m_directionVer = Direction.DOWN;
         //   setRotation(30);
            setRotation(rotation);
        }
        else{
            m_directionVer = Direction.UP;
            setRotation(-rotation);
        //    setRotation(-30);
        }
    }

    public void setGoal(float x, float y) {
        m_goingToY=y;
        m_goingToX=x;

    }
}