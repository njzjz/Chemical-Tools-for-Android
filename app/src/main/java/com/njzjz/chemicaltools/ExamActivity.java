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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.mikepenz.aboutlibraries.Libs;
import com.tencent.stat.StatService;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private static final int action_rank = Menu.FIRST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 有
            AVUser.getCurrentUser().fetchIfNeededInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if(e==null) {
                        // 调用 fetchIfNeededInBackground 和 refreshInBackground 效果是一样的。
                        String examCorrectNumber = avObject.getString("examCorrectNumber");
                        if (examCorrectNumber == null) examCorrectNumber = "0";
                        String examIncorrectnumber = avObject.getString("examIncorrectnumber");
                        if (examIncorrectnumber == null) examIncorrectnumber = "0";
                        String elementnumber_limit = avObject.getString("elementnumber_limit");
                        if (elementnumber_limit == null) elementnumber_limit = "118";
                        String examMode = avObject.getString("examMode");
                        if (examMode == null) examMode = "0";
                        Boolean setting_examOptionMode = avObject.getBoolean("setting_examOptionMode");
                        if (setting_examOptionMode == null) setting_examOptionMode = false;
                        PreferenceUtils.setPrefString(getApplicationContext(), "examCorrectNumber", examCorrectNumber);
                        PreferenceUtils.setPrefString(getApplicationContext(), "examIncorrectnumber", examIncorrectnumber);
                        PreferenceUtils.setPrefString(getApplicationContext(), "elementnumber_limit", elementnumber_limit);
                        PreferenceUtils.setPrefString(getApplicationContext(), "examMode", examMode);
                        PreferenceUtils.setPrefBoolean(getApplicationContext(), "setting_examOptionMode", setting_examOptionMode);
                    }
                }});

        }

        TextView mFileContentView = (TextView) findViewById(R.id.examTextview);
        final Button Optionbutton_1=(Button) findViewById(R.id.Optionbutton_1);
        final Button Optionbutton_2=(Button) findViewById(R.id.Optionbutton_2);
        final Button Optionbutton_3=(Button) findViewById(R.id.Optionbutton_3);
        final Button Optionbutton_4=(Button) findViewById(R.id.Optionbutton_4);
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
                String output="";
                switch (examMode[0]){
                    case "0":case"1":case"2":
                        output=elementNameArray[examElementnumber[0]];
                        break;
                    case "3":case"4":case"5":
                        output=elementAbbrArray[examElementnumber[0]];
                        break;
                    case "6":case"7":case"8":
                        output=String.valueOf(examElementnumber[0]+1);
                        break;
                    case "9":case"10":case"11":
                        output=elementIUPACArray[examElementnumber[0]];
                        break;
                }
                if(examInput.toUpperCase().equals(correctAnswer.toUpperCase())){
                    examTextview.setText(getResources().getString(R.string.examOutputRight_name));
                    examCorrectNumber++;
                    PreferenceUtils.setPrefString(getApplicationContext(),"examCorrectNumber",String.valueOf(examCorrectNumber));
                    AVUser currentUser = AVUser.getCurrentUser();
                    if (currentUser != null) {
                        AVUser.getCurrentUser().put("examCorrectNumber",String.valueOf(examCorrectNumber));
                        AVUser.getCurrentUser().saveInBackground();
                    }
                }else{
                    examTextview.setText(String.format(getResources().getString(R.string.examOutputWrong_name),correctAnswer,examInput,output));
                    examIncorrectnumber++;
                    PreferenceUtils.setPrefString(getApplicationContext(),"examIncorrectnumber",String.valueOf(examIncorrectnumber));
                    AVUser currentUser = AVUser.getCurrentUser();
                    if (currentUser != null) {
                        AVUser.getCurrentUser().put("examIncorrectnumber",String.valueOf(examCorrectNumber));
                        AVUser.getCurrentUser().saveInBackground();
                    }
                }
                examScoreOutput(examCorrectNumber,examIncorrectnumber);
                examMode[0] =PreferenceUtils.getPrefString(getApplicationContext(),"examMode","0");
                examElementnumber[0] =examReflesh(examTextviewTop,examText,elementNameArray,elementAbbrArray,elementIUPACArray, examMode[0]);
                examText.setText("");
            }
        });
        Optionbutton_1.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
            examText.setText(Optionbutton_1.getText());
            examButton.callOnClick();
        }});
        Optionbutton_2.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
            examText.setText(Optionbutton_2.getText());
            examButton.callOnClick();
        }});
        Optionbutton_3.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
            examText.setText(Optionbutton_3.getText());
            examButton.callOnClick();
        }});
        Optionbutton_4.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
            examText.setText(Optionbutton_4.getText());
            examButton.callOnClick();
        }});
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
        Boolean setting_examOptionMode=PreferenceUtils.getPrefBoolean(getApplicationContext(),"setting_examOptionMode",false);
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
        RelativeLayout relativeLayout1=(RelativeLayout)findViewById(R.id.relativeLayout1);
        RelativeLayout relativeLayout2=(RelativeLayout)findViewById(R.id.relativeLayout2);
        if(setting_examOptionMode){
            relativeLayout1.setVisibility(View.GONE);
            relativeLayout2.setVisibility(View.VISIBLE);
            int[] optionNumber=new int[4];
            List list = new ArrayList();
            optionNumber[0]=i;
            for(int i2 = 1;i2<optionNumber.length;i2++){
                optionNumber[i2] = (int)(Math.random()*elementnumber_limit);
                for(int i3=0;i3<i2;i3++) {
                    while (optionNumber[i2]==optionNumber[i3]) optionNumber[i2] = (int) (Math.random() * elementnumber_limit);
                }
            }
            for(int i3 = 0;i3 < optionNumber.length;i3++){
                switch (examMode){
                    case "3":case"6":case"9":
                        list.add(elementNameArray[optionNumber[i3]]);
                        break;
                    case "0":case"7":case"10":
                        list.add(elementAbbrArray[optionNumber[i3]]);
                        break;
                    case "1":case"4":case"11":
                        list.add(optionNumber[i3]);
                        break;
                    case "2":case"5":case"8":
                        list.add(elementIUPACArray[optionNumber[i3]]);
                        break;
                }
            }
            Collections.shuffle(list);
            Iterator ite = list.iterator();
            Button Optionbutton_1=(Button) findViewById(R.id.Optionbutton_1);
            Button Optionbutton_2=(Button) findViewById(R.id.Optionbutton_2);
            Button Optionbutton_3=(Button) findViewById(R.id.Optionbutton_3);
            Button Optionbutton_4=(Button) findViewById(R.id.Optionbutton_4);
            Optionbutton_1.setText(ite.next().toString());
            Optionbutton_2.setText(ite.next().toString());
            Optionbutton_3.setText(ite.next().toString());
            Optionbutton_4.setText(ite.next().toString());
        }else{
            relativeLayout2.setVisibility(View.GONE);
            relativeLayout1.setVisibility(View.VISIBLE);
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
        }
        return i;
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        menu.add(0, action_rank, 100, getString(R.string.button_rank));
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
                //Intent share = new Intent(Intent.ACTION_SEND);
                //share.setType("text/plain");
                //share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //share.putExtra(Intent.EXTRA_SUBJECT,
                //        getString(R.string.app_name));
                int examCorrectNumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examCorrectNumber","0"));
                int examIncorrectnumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examIncorrectnumber","0"));
                int sum=examCorrectNumber+examIncorrectnumber;
                double examCorrectPercent;
                if(sum>0) examCorrectPercent=(double)examCorrectNumber/sum*100; else examCorrectPercent=0;
                //share.putExtra(Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.examShare_name),sum,examCorrectPercent));
                //startActivity(Intent.createChooser(share,
                //        getString(R.string.app_name)));
                UMImage image = new UMImage(this, R.drawable.ic_launcher);//资源文件
                new ShareAction(this).withText(String.format(getResources().getString(R.string.examShare_name),sum,examCorrectPercent))
                        .withTargetUrl("https://web.zgchemicals.mobi/exam.php")
                        .withMedia(image)
                        .withTitle(getString(R.string.app_name))
                        .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                        .open();
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
            case action_rank:
                openrank();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openSettings(){
        Intent intent =new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,1000);
    }
    public void openrank(){
        Intent intent =new Intent(this, RankActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1000 && resultCode==1001)
        if(data.getStringExtra("result").equals("1")){
            finish();
            Intent intent = new Intent(ExamActivity.this, ExamActivity.class);
            startActivity(intent);
        }
    }
}
