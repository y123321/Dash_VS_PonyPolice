package il.co.ovalley.dashvsponypolice.app.Model;

import android.content.Context;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by yuval on 18/04/2014.
 */
public abstract class GameView extends ImageView {
    public void isDead(boolean m_isDead) {
        this.m_isDead = m_isDead;
    }

    public boolean isDead() {
        return m_isDead;
    }

    public GameView(RelativeLayout container) {
        super(container.getContext());
        m_container=container;
        container.addView(this);
        m_waitTime=10;//default waiting time
        m_isDead=false;

    }

    boolean m_isDead;
    RelativeLayout m_container;
    Direction m_direction;
    int stepCounter;
    int m_waitTime;
    abstract public void update();
    abstract void changeDirection();

    public int getWaitTime() {
        return m_waitTime;
    }

    public int getStepCounter() {
        return stepCounter;
    }

    public Direction getDirection() {
        return m_direction;
    }

    public RelativeLayout getContainer() {
        return m_container;
    }

    public void removeView() {
        m_container.removeView(this);
    }
}
