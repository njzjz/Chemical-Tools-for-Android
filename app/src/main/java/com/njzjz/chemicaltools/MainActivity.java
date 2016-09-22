package com.njzjz.chemicaltools;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        TextView mFileContentView = (TextView) findViewById(R.id.elementTextview);
        mFileContentView.setMovementMethod(ScrollingMovementMethod.getInstance());
        final Button elementButton = (Button) findViewById(R.id.elementButton);
        final String[] elementNameArray = getResources().getStringArray(R.array.elementNameArray);
        final String[] elementAbbrArray = getResources().getStringArray(R.array.elementAbbrArray);
        final String[] elementMassArray= getResources().getStringArray(R.array.elementMassArray);
        final String[] elementIUPACArray= getResources().getStringArray(R.array.elementIUPACArray);
        final String[] elementOriginArray= getResources().getStringArray(R.array.elementOriginArray);
        elementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i;
                int elementNumber=0;
                String elementInput="";
                EditText elementText =(EditText)findViewById(R.id.elementText);
                TextView elementTextview=(TextView)findViewById(R.id.elementTextview);
                elementInput=elementText.getText().toString();
                for(i=0;i<118;i++) {
                    if (elementInput.equals(String.valueOf(i+1))){
                        elementNumber=i+1;
                    }else{
                        if(elementInput.equals(elementNameArray[i])){
                            elementNumber=i+1;
                        }else{
                            if(elementInput.equals(elementAbbrArray[i])){
                                elementNumber=i+1;
                            }
                        }
                    }
                }
                Resources res = getResources();
                if(elementNumber>0){
                    elementTextview.setText(String.format(res.getString(R.string.elementOutput_name),elementNumber,elementNameArray[elementNumber-1],elementAbbrArray[elementNumber-1],elementMassArray[elementNumber-1],elementIUPACArray[elementNumber-1],elementOriginArray[elementNumber-1]));
                }else{
                    elementTextview.setText(String.format(res.getString(R.string.error_name)));
                };
            };
        });
        final EditText elementText = (EditText) findViewById(R.id.elementText);
        elementText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                elementText.setHint(null);
            }
        });
        elementText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    elementButton.performClick();
                }
                return false;
            }
        });
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //菜单栏返回键功能
            case android.R.id.home:
                Intent intent = new Intent(this, TitleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.action_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.app_name));
                TextView elementTextview=(TextView) findViewById(R.id.elementTextview);
                share.putExtra(Intent.EXTRA_TEXT, elementTextview.getText().toString());
                startActivity(Intent.createChooser(share,
                        getString(R.string.app_name)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
};

