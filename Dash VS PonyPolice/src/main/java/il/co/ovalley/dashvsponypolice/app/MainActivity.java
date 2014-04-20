package il.co.ovalley.dashvsponypolice.app;

import android.content.ClipData;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuView;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import il.co.ovalley.dashvsponypolice.app.Controller.GameRunnable;
import il.co.ovalley.dashvsponypolice.app.Model.RainbowDash;


public class MainActivity extends ActionBarActivity {
    GameRunnable m_GameRunnable;
    RelativeLayout m_Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        new Thread(m_GameRunnable).start();

    }

    private void init() {
         m_Layout=(RelativeLayout)findViewById(R.id.layout);
        m_GameRunnable =new GameRunnable(this,m_Layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
       // m_GameRunnable.pauseGame();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_stop:
                m_GameRunnable.stopGame();
                return true;
            case R.id.action_resume:
                try{
                    m_GameRunnable.resume();
                }
                catch (Exception e){}
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
