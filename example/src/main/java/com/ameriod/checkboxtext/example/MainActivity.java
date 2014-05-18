package com.ameriod.checkboxtext.example;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ameriod.lib.checkboxtext.CheckBoxText;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBoxText below = (CheckBoxText) findViewById(R.id.below);
        CheckBoxText left = (CheckBoxText) findViewById(R.id.left);
        CheckBoxText right = (CheckBoxText) findViewById(R.id.right);
        CheckBoxText above = (CheckBoxText) findViewById(R.id.above);

        below.setOnCheckedChangeListener(this);
        left.setOnCheckedChangeListener(this);
        right.setOnCheckedChangeListener(this);
        above.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(getApplicationContext(), buttonView.getTag().toString() + " Checked: " + isChecked, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
