package com.ameriod.checkboxtext.example;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ameriod.lib.checkboxtext.CheckBoxText;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        CheckBoxText checkBoxText = new CheckBoxText(getApplicationContext(), CheckBoxText.TEXT_BELOW);
        checkBoxText.setLayoutParams(params);
        checkBoxText.setText("BELOW");
        checkBoxText.setTextColor(Color.WHITE);

        CheckBoxText checkBoxText2 = new CheckBoxText(getApplicationContext(), CheckBoxText.TEXT_LEFT);
        checkBoxText2.setLayoutParams(params);
        checkBoxText2.setText("LEFT");
        checkBoxText2.setTextColor(Color.WHITE);
        CheckBoxText checkBoxText3 = new CheckBoxText(getApplicationContext(), CheckBoxText.TEXT_RIGHT);
        checkBoxText3.setLayoutParams(params);
        checkBoxText3.setText("RIGHT");
        checkBoxText3.setTextColor(Color.WHITE);

        CheckBoxText checkBoxText1 = new CheckBoxText(getApplicationContext(), CheckBoxText.TEXT_ABOVE);
        checkBoxText1.setLayoutParams(params);
        checkBoxText1.setText("ABOVE");
        checkBoxText1.setTextColor(Color.WHITE);

        container.addView(checkBoxText);
        container.addView(checkBoxText2);
        container.addView(checkBoxText3);
        container.addView(checkBoxText1);

        checkBoxText.setOnCheckedChangeListener(this);
        checkBoxText1.setOnCheckedChangeListener(this);
        checkBoxText2.setOnCheckedChangeListener(this);
        checkBoxText3.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e("TEST", "TAG " + buttonView.getTag() + " isChecked " + isChecked);
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
