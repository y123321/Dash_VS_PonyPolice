package il.co.ovalley.dashvsponypolice.app.Model;

import android.app.Activity;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.widget.RelativeLayout;
import il.co.ovalley.dashvsponypolice.app.R;

/**
 * Created by yuval on 20/04/2014.
 */
public class Shot extends GameView{
    public Shot(RelativeLayout container) {
        super(container);
        setImageResource(R.drawable.circle_small);
        m_waitTime=3;
        m_ySpeed=3;
     //   setScaleX(20);
    //    setScaleY(20);

    }


    @Override
    public void update() {
        Activity activity=(Activity)getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isDead()) {
                    removeView();
                    return;
                }
                float y = getY();
                setY(y - m_ySpeed);
                if(y<1) isDead(true);

            }
        });

    }

    @Override
    void changeDirection() {

    }
}
