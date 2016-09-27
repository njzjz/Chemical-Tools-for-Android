package com.njzjz.chemicaltools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,
        AdapterView.OnItemClickListener {
    private EditText etTest;
    private ListPopupWindow lpw;
    private String[] list;

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
        final TextView elementTextview=(TextView)findViewById(R.id.elementTextview);
        elementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i;
                int elementNumber=0;
                String elementInput;
                EditText elementText =(EditText)findViewById(R.id.elementText);
                elementInput=elementText.getText().toString();
                for(i=0;i<118;i++) {
                    if (elementInput.equals(String.valueOf(i+1))){
                        elementNumber=i+1;
                    }else if(elementInput.equals(elementNameArray[i])){
                        elementNumber=i+1;
                    }else if(elementInput.toUpperCase().equals(elementAbbrArray[i].toUpperCase())){
                        elementNumber=i+1;
                    }else if(elementInput.toUpperCase().equals(elementIUPACArray[i].toUpperCase())){
                        elementNumber=i+1;
                    }
                }
                Resources res = getResources();
                if(elementNumber>0){
                    String elementOutput=String.format(res.getString(R.string.elementOutput_name),elementNumber,elementNameArray[elementNumber-1],elementAbbrArray[elementNumber-1],elementMassArray[elementNumber-1],elementIUPACArray[elementNumber-1],elementOriginArray[elementNumber-1]);
                    elementTextview.setText(elementOutput);
                    TypedArray elementPictureArray = getResources().obtainTypedArray(R.array.elementPictureArray);
                    int  resId = elementPictureArray.getResourceId(elementNumber-1, 0);
                    ImageView elementImage=(ImageView) findViewById(R.id.elementImage);
                    elementImage.setImageResource(resId);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElement",elementInput);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementNumber",String.valueOf(elementNumber));
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementOutput",elementOutput);
                }else{
                    Snackbar.make(v, res.getString(R.string.error_name), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                };
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        String historyElementNumber=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementNumber","0");
        int elementNumber=Integer.parseInt(historyElementNumber);
        if (elementNumber>0) {
            TypedArray elementPictureArray = getResources().obtainTypedArray(R.array.elementPictureArray);
            int resId = elementPictureArray.getResourceId(elementNumber - 1, 0);
            ImageView elementImage = (ImageView) findViewById(R.id.elementImage);
            elementImage.setImageResource(resId);
        }
        String historyElementOutput=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementOutput","");
        if(!historyElementOutput.equals("")){
            elementTextview.setText(historyElementOutput);
        }

        etTest = (EditText) findViewById(R.id.elementText);
        etTest.setOnTouchListener((View.OnTouchListener) this);

        list = elementAbbrArray;
        lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list));
        lpw.setAnchorView(etTest);
        lpw.setModal(true);
        lpw.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String item = list[position];
        etTest.setText(item);
        lpw.dismiss();
        Button elementButton=(Button)findViewById(R.id.elementButton);
        elementButton.callOnClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() >= (v.getWidth() - ((EditText) v)
                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                lpw.show();
                String historyElementNumber=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementNumber","0");
                int elementNumber=Integer.parseInt(historyElementNumber);
                lpw.setSelection(elementNumber-1);
                return true;
            }
        }
        return false;
    }
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
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_Feedback:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:njzjz@msn.com?subject=Chemical Tools App Feedback"));
                startActivity(browserIntent);
                return true;
            case R.id.action_About:
                new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openSettings(){
        Intent intent =new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
};

