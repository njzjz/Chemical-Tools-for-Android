package com.njzjz.chemicaltools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;
import com.tencent.stat.StatService;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class MassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        TextView mFileContentView = (TextView) findViewById(R.id.massTextview);
        mFileContentView.setMovementMethod(ScrollingMovementMethod.getInstance());
        final Button massButton = (Button) findViewById(R.id.massButton);
        final String[] elementNameArray = getResources().getStringArray(R.array.elementNameArray);
        final String[] elementAbbrArray = getResources().getStringArray(R.array.elementAbbrArray);
        final String[] elementMassArray = getResources().getStringArray(R.array.elementMassArray);
        final TextView massTextview = (TextView) findViewById(R.id.massTextview);
        massButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText massText = (EditText) findViewById(R.id.massText);
                String x = massText.getText().toString().trim();
                int l = x.length(), i = 0, n, s = 0, i2, c;
                double m = 0;
                double[] massPer=new double[119];
                String y1 = "", y2 = "", y3 = "", y4 = "", T = "";
                int[] AtomNumber = new int[119];
                int[] MulNumber = new int[l + 1], MulIf = new int[l + 1], MulLeft = new int[l + 1], MulRight = new int[l + 1], MulNum = new int[l + 1];
                if (l > 0) {
                    while (i < l) {
                        i++;
                        MulNumber[i] = 1;
                        y1 = x.substring(i - 1, i);
                        if (calAsc(y1) == 4)
                            MulIf[i] = 1;
                        else if (calAsc(y1) == 5)
                            MulIf[i] = -1;
                        else
                            MulIf[i] = 0;
                        s = s + MulIf[i];
                    }
                    if (s == 0) {
                        i = 1;
                        n = 0;
                        while (i < l) {
                            if (MulIf[i] == 1) {
                                n++;
                                c = 1;
                                i2 = i + 1;
                                MulLeft[n] = i;
                                while (c > 0) {
                                    c = c + MulIf[i2];
                                    i2++;
                                }
                                i2 = i2 - 1;
                                MulRight[n] = i2;
                                if (i2 + 1 > l)
                                    y3 = "a";
                                else
                                    y3 = x.substring(i2, i2 + 1);
                                if (calAsc(y3) == 3) {
                                    if (i2 + 2 > l)
                                        y4 = "a";
                                    else
                                        y4 = x.substring(i2 + 1, i2 + 2);
                                    if (calAsc(y4) == 3)
                                        MulNum[n] = Integer.parseInt(y3 + y4);
                                    else
                                        MulNum[n] = Integer.parseInt(y3);
                                } else {
                                    MulNum[n] = 1;
                                }
                            }
                            i++;
                        }
                        i = 0;
                        while (i < n) {
                            i++;
                            for (i2 = MulLeft[i]; i2 <= MulRight[i]; i2++)
                                MulNumber[i2] = MulNumber[i2] * MulNum[i];
                        }
                        i = 0;
                        while (i < l) {
                            i++;
                            y1 = x.substring(i - 1, i);
                            if (calAsc(y1) == 1) {
                                if (i >= l)
                                    y2 = "1";
                                else
                                    y2 = x.substring(i, i + 1);
                                if (calAsc(y2) == 2) {
                                    T = y1 + y2;
                                    n = calElementChoose(T, elementAbbrArray);
                                    if (n > 0) {
                                        if (i + 1 >= l)
                                            y3 = "1";
                                        else
                                            y3 = x.substring(i + 1, i + 2);
                                        if (calAsc(y3) == 3) {
                                            if (i + 2 >= l)
                                                y4 = "a";
                                            else
                                                y4 = x.substring(i + 2, i + 3);
                                            if (calAsc(y4) == 3) {
                                                AtomNumber[n] = AtomNumber[n] + Integer.parseInt(y3 + y4) * MulNumber[i];
                                                i = i + 3;
                                            } else {
                                                AtomNumber[n] = AtomNumber[n] + Integer.parseInt(y3) * MulNumber[i];
                                                i = i + 2;
                                            }
                                        } else {
                                            AtomNumber[n] = AtomNumber[n] + MulNumber[i];
                                            i = i + 1;
                                        }
                                    }
                                } else if (calAsc(y2) == 3) {
                                    n = calElementChoose(y1, elementAbbrArray);
                                    if (n > 0) {
                                        if (i + 1 >= l)
                                            y3 = "a";
                                        else
                                            y3 = x.substring(i + 1, i + 2);
                                        if (calAsc(y3) == 3) {
                                            AtomNumber[n] = AtomNumber[n] + Integer.parseInt(y2 + y3) * MulNumber[i];
                                            i = i + 2;
                                        } else {
                                            AtomNumber[n] = AtomNumber[n] + Integer.parseInt(y2) * MulNumber[i];
                                        }
                                    }
                                } else {
                                    n = calElementChoose(y1, elementAbbrArray);
                                    if (n > 0)
                                        AtomNumber[n] = AtomNumber[n] + MulNumber[i];
                                }
                            } else if (calAsc(y1) == 4) {
                            } else if (calAsc(y1) == 5) {
                                if (i >= l)
                                    y2 = "a";
                                else
                                    y2 = x.substring(i, i + 1);
                                if (calAsc(y2) == 3) {
                                    if (i + 1 >= l)
                                        y2 = "a";
                                    else
                                        y3 = x.substring(i + 1, i + 2);
                                    if (calAsc(y3) == 3) i++;
                                    i++;
                                }
                            }
                        }
                        for (i = 0; i < 118; i++) {
                            m = m + AtomNumber[i + 1] * Double.parseDouble(elementMassArray[i]);
                        }
                    }
                }
                Resources res = getResources();
                if (m > 0) {
                    String xHtml="";
                    for(int i3=0;i3<x.length();i3++) {
                        if (x.charAt(i3) >= 48 && x.charAt(i3) <= 57) {
                            xHtml += "<sub>" + x.charAt(i3) + "</sub>";
                        } else {
                            xHtml += x.charAt(i3);
                        }
                    }
                    String massOutput=String.format(res.getString(R.string.massOutput_name), xHtml,m);
                    massTextview.setText(Html.fromHtml(parseContent(massOutput)));
                    for(i=0;i<118;i++){
                        if(AtomNumber[i+1]>0){
                            massPer[i+1]=AtomNumber[i + 1] * Double.parseDouble(elementMassArray[i])/m*100;
                            massOutput=massOutput+"\n"+String.format(res.getString(R.string.massper_name), elementNameArray[i],elementAbbrArray[i],AtomNumber[i+1],elementMassArray[i],massPer[i+1]);
                        }
                    }
                    massOutput=massOutput.substring(0,massOutput.length()-1)+res.getString(R.string.juhao);
                    massTextview.setText(Html.fromHtml(parseContent(massOutput)));
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyMass",x);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyMassOutput",massOutput);
                } else {
                    Snackbar.make(v, res.getString(R.string.error_name), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                };
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

            ;
        });
        final EditText massText = (EditText) findViewById(R.id.massText);
        massText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                massText.setHint(null);
            }
        });
        massText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    massButton.performClick();
                }
                return false;
            }
        });
        String historyMassOutput=PreferenceUtils.getPrefString(getApplicationContext(),"historyMassOutput","");
        if(!historyMassOutput.equals("")){
            massTextview.setText(Html.fromHtml(parseContent(historyMassOutput)));
        }
    };
    private String parseContent(String content) {
        content = content.replace("\n","<br>");
        return content;
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
        return super.onCreateOptionsMenu(menu);
    }

    public static int calAsc(String x) {
        char c = x.toCharArray()[0];
        int n = (char) c;
        if (n > 64 & n < 91)
            return 1;
        else if (n > 96 & n < 123)
            return 2;
        else if (n > 47 & n < 58)
            return 3;
        else if (n == 40 | n == 91 | n == -23640)
            return 4;
        else if (n == 41 | n == 93 | n == -23639)
            return 5;
        else
            return 0;
    }

    public static int calElementChoose(String x, String[] elementAbbrArray) {
        int i, elementNumber = 0;
        for (i = 0; i < 118; i++) {
            if (x.equals(elementAbbrArray[i]))
                elementNumber = i + 1;
        }
        return elementNumber;
    }
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
                TextView massTextview=(TextView) findViewById(R.id.massTextview);
                //share.putExtra(Intent.EXTRA_TEXT, massTextview.getText().toString());
                //startActivity(Intent.createChooser(share,
                //        getString(R.string.app_name)));
                new ShareAction(this).withText(massTextview.getText().toString())
                        .setDisplayList(/*SHARE_MEDIA.QQ,*/SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
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
                new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(MassActivity.this);
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

