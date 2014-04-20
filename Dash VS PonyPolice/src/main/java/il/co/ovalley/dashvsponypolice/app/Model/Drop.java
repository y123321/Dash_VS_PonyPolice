package il.co.ovalley.dashvsponypolice.app.Model;

import android.app.Activity;
import android.content.Context;
import android.view.ViewManager;
import android.widget.RelativeLayout;
import il.co.ovalley.dashvsponypolice.app.Controller.GameRunnable;
import il.co.ovalley.dashvsponypolice.app.R;

/**
 * Created by yuval on 16/04/2014.
 */
public class Drop extends GameView{

    public Drop(RelativeLayout container) {
        super(container);
        setImageResource(R.drawable.shit);
        m_waitTime=1;
    }

    @Override
    public void update() {

        Activity activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isDead()) {
                    removeView();
                    return;
                }
                float y = getY();
                setY(y + 1);
                if (y > m_container.getHeight() - 10) {
                    isDead(true);
                }

            }
        });
    }

    @Override
    void changeDirection() {

    }
}