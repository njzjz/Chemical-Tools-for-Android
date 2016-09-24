package com.njzjz.chemicaltools;

import android.content.Intent;
import android.net.Uri;
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

import com.mikepenz.aboutlibraries.Libs;

public class ExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        TextView mFileContentView = (TextView) findViewById(R.id.examTextview);
        mFileContentView.setMovementMethod(ScrollingMovementMethod.getInstance());
        final EditText examText = (EditText) findViewById(R.id.examText);
        final Button examButton = (Button) findViewById(R.id.examButton);
        final String[] elementNameArray = getResources().getStringArray(R.array.elementNameArray);
        final String[] elementAbbrArray = getResources().getStringArray(R.array.elementAbbrArray);
        final String[] elementIUPACArray = getResources().getStringArray(R.array.elementIUPACArray);
        final TextView examTextviewTop = (TextView) findViewById(R.id.examTextviewTop);
        final String[] examMode = {PreferenceUtils.getPrefString(getApplicationContext(), "examMode", "0")};
        final int[] examElementnumber = {examReflesh(examTextviewTop,examText, elementNameArray,elementAbbrArray,elementIUPACArray, examMode[0])};
        examText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                examText.setHint(null);
            }
        });
        examText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    examButton.performClick();
                }
                return false;
            }
        });

        examButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText examText = (EditText) findViewById(R.id.examText);
                String examInput=examText.getText().toString();
                TextView examTextview = (TextView) findViewById(R.id.examTextview);
                int examCorrectNumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examCorrectNumber","0"));
                int examIncorrectnumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examIncorrectnumber","0"));
                String correctAnswer="";
                switch (examMode[0]){
                    case "3":case"6":case"9":
                        correctAnswer=elementNameArray[examElementnumber[0]];
                        break;
                    case "0":case"7":case"10":
                        correctAnswer=elementAbbrArray[examElementnumber[0]];
                        break;
                    case "1":case"4":case"11":
                        correctAnswer=String.valueOf(examElementnumber[0]+1);
                        break;
                    case "2":case"5":case"8":
                        correctAnswer=elementIUPACArray[examElementnumber[0]];
                        break;
                }
                if(examInput.toUpperCase().equals(correctAnswer.toUpperCase())){
                    examTextview.setText(getResources().getString(R.string.examOutputRight_name));
                    examCorrectNumber++;
                    PreferenceUtils.setPrefString(getApplicationContext(),"examCorrectNumber",String.valueOf(examCorrectNumber));
                }else{
                    examTextview.setText(String.format(getResources().getString(R.string.examOutputWrong_name),correctAnswer,examInput));
                    examIncorrectnumber++;
                    PreferenceUtils.setPrefString(getApplicationContext(),"examIncorrectnumber",String.valueOf(examIncorrectnumber));
                }
                examScoreOutput(examCorrectNumber,examIncorrectnumber);
                examMode[0] =PreferenceUtils.getPrefString(getApplicationContext(),"examMode","0");
                examElementnumber[0] =examReflesh(examTextviewTop,examText,elementNameArray,elementAbbrArray,elementIUPACArray, examMode[0]);
                examText.setText("");
            }
        });
        int examCorrectNumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examCorrectNumber","0"));
        int examIncorrectnumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examIncorrectnumber","0"));
        if(examCorrectNumber+examIncorrectnumber>0) examScoreOutput(examCorrectNumber,examIncorrectnumber);
    }
    public void examScoreOutput(int examCorrectNumber,int examIncorrectnumber){
        int sum=examCorrectNumber+examIncorrectnumber;
        double examCorrectPercent;
        if(sum>0) examCorrectPercent=(double)examCorrectNumber/sum*100; else examCorrectPercent=0;
        TextView examScoreTextview = (TextView) findViewById(R.id.examScoreTextview);
        examScoreTextview.setText(String.format(getResources().getString(R.string.examScoreOutput_name),sum,examCorrectNumber,examCorrectPercent));
    }

    public int examReflesh(TextView textView,EditText editText,String[] elementNameArray,String[] elementAbbrArray,String[] elementIUPACArray,String examMode){
        final double d = Math.random();
        int elementnumber_limit=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"elementnumber_limit","118"));
        final int i = (int)(d*elementnumber_limit);
        String output="";
        switch (examMode){
            case "0":case"1":case"2":
                output=elementNameArray[i];
                break;
            case "3":case"4":case"5":
                output=elementAbbrArray[i];
                break;
            case "6":case"7":case"8":
                output=String.valueOf(i+1);
                break;
            case "9":case"10":case"11":
                output=elementIUPACArray[i];
                break;
        }
        textView.setText(output);
        switch (examMode){
            case "3":case"6":case"9":
                editText.setHint(getResources().getString(R.string.examEditText_name_elementname));
                break;
            case "0":case"7":case"10":
                editText.setHint(getResources().getString(R.string.examEditText_name));
                break;
            case "1":case"4":case"11":
                editText.setHint(getResources().getString(R.string.examEditText_name_number));
                break;
            case "2":case"5":case"8":
                editText.setHint(getResources().getString(R.string.examEditText_name_IUPAC));
                break;
        }
        return i;
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
                int examCorrectNumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examCorrectNumber","0"));
                int examIncorrectnumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examIncorrectnumber","0"));
                int sum=examCorrectNumber+examIncorrectnumber;
                double examCorrectPercent;
                if(sum>0) examCorrectPercent=(double)examCorrectNumber/sum*100; else examCorrectPercent=0;
                share.putExtra(Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.examShare_name),sum,examCorrectPercent));
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
                new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(ExamActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openSettings(){
        Intent intent =new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
