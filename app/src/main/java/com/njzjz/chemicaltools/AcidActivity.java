package com.njzjz.chemicaltools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Html;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;
import com.tencent.stat.StatService;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class AcidActivity extends AppCompatActivity implements View.OnTouchListener,
        AdapterView.OnItemClickListener  {
    private EditText etTest;
    private ListPopupWindow lpw;
    private String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acid);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        final Button acidButton = (Button) findViewById(R.id.acidButton);
        acidButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double liquidpKa=-1.74;
                String pKwstr=PreferenceUtils.getPrefString(getApplicationContext(),"pKw","14");
                double pKw=Double.parseDouble(pKwstr);
                Switch switchAB=(Switch) findViewById(R.id.switchAB);
                boolean AorB=!switchAB.isChecked();
                String ABname,ABnameall;
                EditText acidText_name=(EditText)findViewById(R.id.acidText_name);
                String acidname=acidText_name.getText().toString();
                if(AorB){
                    ABname="A";
                    ABnameall="HA";
                    String[] acidnameArray=getResources().getStringArray(R.array.acidnameArray);
                    String[] acidAbbrArray=getResources().getStringArray(R.array.acidAbbrArray);
                    for(int i=0;i<acidnameArray.length;i++){
                        if (acidnameArray[i].equals(acidname)){
                            ABname=acidAbbrArray[i];
                            ABnameall=acidnameArray[i];
                        }
                    }
                }else{
                    ABname="B";
                    ABnameall="BOH";
                    String[] basenameArray=getResources().getStringArray(R.array.basenameArray);
                    String[] baseAbbrArray=getResources().getStringArray(R.array.baseAbbrArray);
                    for(int i=0;i<basenameArray.length;i++){
                        if (basenameArray[i].equals(acidname)){
                            ABname=baseAbbrArray[i];
                            ABnameall=basenameArray[i];
                        }
                    }
                }
                String ABnameHtml="",ABnameallHtml="";
                for(int i3=0;i3<ABname.length();i3++) {
                    if (ABname.charAt(i3) >= 48 && ABname.charAt(i3) <= 57) {
                        ABnameHtml += "<sub>" + ABname.charAt(i3) + "</sub>";
                    } else {
                        ABnameHtml += ABname.charAt(i3);
                    }
                }
                for(int i3=0;i3<ABnameall.length();i3++) {
                    if (ABnameall.charAt(i3) >= 48 && ABnameall.charAt(i3) <= 57) {
                        ABnameallHtml += "<sub>" + ABnameall.charAt(i3) + "</sub>";
                    } else {
                        ABnameallHtml += ABnameall.charAt(i3);
                    }
                }
                EditText acidText_c = (EditText) findViewById(R.id.acidText_c);
                String strc = acidText_c.getText().toString();
                EditText acidText_pKa = (EditText) findViewById(R.id.acidText_pK);
                String strpKa = acidText_pKa.getText().toString();
                if(isNumeric(strc) && strpKa!=""){
                    String[] strpKaArray=strpKa.split(" ");
                    double[] valpKa= new double[strpKaArray.length];
                    double c=Double.parseDouble(strc);
                    boolean error=false;
                    for(int i=0;i<strpKaArray.length;i++){
                        if (isNumeric(strpKaArray[i])){
                            valpKa[i]=Double.parseDouble(strpKaArray[i]);
                            if (valpKa[i]<liquidpKa) valpKa[i]=liquidpKa;
                        }
                        else error=true;
                    }
                    if(!error){
                        double pH=calpH(valpKa,c,pKw);
                        double[] cAB=calpHtoc(valpKa,c,pH);
                        if(!AorB) pH=pKw-pH;
                        double H=Math.pow(10,-pH);
                        String acidOutput=ABnameallHtml+" ,c="+strc+"mol/L, ";
                        for(int i=0;i<valpKa.length;i++){
                            if(AorB)acidOutput=acidOutput+"pK<sub>a</sub>";else acidOutput=acidOutput+"pK<sub>b</sub>";
                            if(valpKa.length>1)acidOutput=acidOutput+"<sub>"+String.valueOf(i+1)+"</sub>";
                            acidOutput=acidOutput+"="+strpKaArray[i]+", ";
                        }
                        acidOutput=acidOutput+"\n"+String.format(getString(R.string.acidpHOut_name),pH);
                        acidOutput=acidOutput+"\n"+"c(H<sup>+</sup>)="+String.format("%1$.2e",H)+"mol/L,";
                        for(int i=0;i<cAB.length;i++){
                            String cABoutput="c(";
                            if(AorB){
                                if(i<cAB.length-1){
                                    cABoutput=cABoutput+"H";
                                    if(cAB.length-i>2) cABoutput=cABoutput+ "<sub>"+String.valueOf(cAB.length - i-1)+"</sub>";
                                }
                                cABoutput=cABoutput+ABnameHtml;
                                if(i>0){
                                    if(i>1) cABoutput=cABoutput+ "<sup>"+String.valueOf(i)+"</sup>";
                                    cABoutput=cABoutput+"<sup>-</sup>";
                                }
                            }else{
                                cABoutput=cABoutput+ABnameHtml;
                                if(cAB.length-i>2){
                                    cABoutput=cABoutput+"(OH)<sub>"+ String.valueOf(cAB.length - i-1)+"</sub>";
                                }else if(cAB.length-i==2){
                                    cABoutput=cABoutput+"OH";
                                }
                                if(i>0){
                                    if(i>1) cABoutput="<sup>"+cABoutput+String.valueOf(i)+"</sup>";
                                    cABoutput=cABoutput+"<sup>+</sup>";
                                }
                            }
                            cABoutput=cABoutput+")=";
                            acidOutput=acidOutput+"\n"+cABoutput+String.format("%1$.2e",cAB[i])+"mol/L,";
                        }
                        acidOutput=acidOutput.substring(0,acidOutput.length()-1)+".";
                        TextView acidTextview = (TextView) findViewById(R.id.acidTextview);
                        acidTextview.setText(Html.fromHtml(parseContent(acidOutput)));
                        PreferenceUtils.setPrefString(getApplicationContext(),"historyAcidOutput",acidOutput);
                    }else Snackbar.make(v, getResources().getString(R.string.error_name), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }else Snackbar.make(v, getResources().getString(R.string.error_name), Snackbar.LENGTH_LONG)
                        .setAction("Error", null).show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(AcidActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }});
        final EditText acidText_c = (EditText) findViewById(R.id.acidText_c);
        final TextView acidTextview=(TextView) findViewById(R.id.acidTextview);
        acidText_c.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    acidButton.performClick();
                }
                return false;
            }
        });
        acidTextview.setMovementMethod(ScrollingMovementMethod.getInstance());
        String historyAcidOutput=PreferenceUtils.getPrefString(getApplicationContext(),"historyAcidOutput","");
        if(!historyAcidOutput.equals("")){
            acidTextview.setText(Html.fromHtml(parseContent(historyAcidOutput)));
        }
        etTest = (EditText) findViewById(R.id.acidText_name);
        etTest.setOnTouchListener((View.OnTouchListener) this);

        Switch switchAB=(Switch) findViewById(R.id.switchAB);
        boolean AorB=!switchAB.isChecked();
        String name;
        EditText acidText_name=(EditText)findViewById(R.id.acidText_name);
        EditText acidText_pK=(EditText)findViewById(R.id.acidText_pK);
        if(AorB){
            name="HA";
            list =  getResources().getStringArray(R.array.acidnameArray);
            acidText_name.setHint(getResources().getString(R.string.acidname));
            acidText_pK.setHint(getResources().getString(R.string.acidpKa));
        } else{
            name="BOH";
            list =  getResources().getStringArray(R.array.basenameArray);
            acidText_name.setHint(getResources().getString(R.string.basename));
            acidText_pK.setHint(getResources().getString(R.string.basepKb));
        }
        acidText_name.setText(name);
        lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list));
        lpw.setAnchorView(etTest);
        lpw.setModal(true);
        lpw.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        acidText_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Switch switchAB=(Switch) findViewById(R.id.switchAB);
                    boolean AorB=!switchAB.isChecked();
                    EditText acidText_name=(EditText)findViewById(R.id.acidText_name);
                    EditText acidText_pK=(EditText)findViewById(R.id.acidText_pK);
                    String acidname=acidText_name.getText().toString();
                    if(AorB){
                        String[] acidnameArray=getResources().getStringArray(R.array.acidnameArray);
                        String[] acidpKaArray=getResources().getStringArray(R.array.acidpKaArray);
                        for(int i=0;i<acidnameArray.length;i++){
                            if(acidnameArray[i].equals(acidname))acidText_pK.setText(acidpKaArray[i]);
                        }
                    }else{
                        String[] basenameArray=getResources().getStringArray(R.array.basenameArray);
                        String[] basepKbArray=getResources().getStringArray(R.array.basepKbArray);
                        for(int i=0;i<basenameArray.length;i++){
                            if(basenameArray[i].equals(acidname))acidText_pK.setText(basepKbArray[i]);
                        }
                    }
                }
                return false;
            }
        });
        switchAB.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String name;
                EditText acidText_name=(EditText)findViewById(R.id.acidText_name);
                EditText acidText_pK=(EditText)findViewById(R.id.acidText_pK);
                if(isChecked){
                    list =  getResources().getStringArray(R.array.basenameArray);
                    name="BOH";
                    acidText_name.setHint(getResources().getString(R.string.basename));
                    acidText_pK.setHint(getResources().getString(R.string.basepKb));
                }else{
                    list =  getResources().getStringArray(R.array.acidnameArray);
                    name="HA";
                    acidText_name.setHint(getResources().getString(R.string.acidname));
                    acidText_pK.setHint(getResources().getString(R.string.acidpKa));
                }
                lpw.setAdapter(new ArrayAdapter<String>(AcidActivity.this,
                        android.R.layout.simple_list_item_1, list));
                acidText_name.setText(name);
                acidText_pK.setText("");
            }
        });
    };
    private String parseContent(String content) {
        content = content.replace("\n","<br>");
        return content;
    }
    public double calpH(double[] pKa,double c,double pKw) {
        double cH,Ka1,Kw;
        Ka1=Math.pow(10,-pKa[0]);
        Kw=Math.pow(10,-pKw);
        cH=(Math.sqrt(Ka1*Ka1+4*Ka1*c+Kw)-Ka1)*0.5;
        if(cH>0) return -Math.log10(cH); else return 1024;
    }
    public double[] calpHtoc(double[] pKa,double c, double pH){
        double D=0,E=1,F,H;
        double[] G=new double[pKa.length+1],Ka=new double[pKa.length+1],pHtoc=new double[pKa.length+1];
        H=Math.pow(10,-pH);
        F=Math.pow(H,pKa.length+1);
        for(int i=0;i<pKa.length;i++){
            Ka[i]=Math.pow(10,-pKa[i]);
        }
        for(int i=0;i<pKa.length+1;i++){
            G[i]=F*E;
            D=D+G[i];
            F=F/H;
            E=E*Ka[i];
        }
        for(int i=0;i<pKa.length+1;i++){
            pHtoc[i]=c*G[i]/D;
        }
        return pHtoc;
    }
    public static boolean isNumeric(String s){	try{	Double.parseDouble(s);return true;	}catch (Exception e){return false;}}
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
             //   Intent share = new Intent(Intent.ACTION_SEND);
             //   share.setType("text/plain");
             //   share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             //   share.putExtra(Intent.EXTRA_SUBJECT,
             //           getString(R.string.app_name));
                TextView acidTextview=(TextView) findViewById(R.id.acidTextview);
             //   share.putExtra(Intent.EXTRA_TEXT,
             //           acidTextview.getText().toString());
             //   startActivity(Intent.createChooser(share,
             //           getString(R.string.app_name)));
                new ShareAction(this).withText(acidTextview.getText().toString())
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
                new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openSettings(){
        Intent intent =new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String item = list[position];
        etTest.setText(item);
        lpw.dismiss();
        EditText acidText_pK=(EditText)findViewById(R.id.acidText_pK);
        Switch switchAB=(Switch) findViewById(R.id.switchAB);
        boolean AorB=!switchAB.isChecked();
        if(AorB){
            String[] acidpKaArray=getResources().getStringArray(R.array.acidpKaArray);
            acidText_pK.setText(acidpKaArray[position]);
        }else{
            String[] basepKbArray=getResources().getStringArray(R.array.basepKbArray);
            acidText_pK.setText(basepKbArray[position]);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() >= (v.getWidth() - ((EditText) v)
                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                lpw.show();
           //     String historyElementNumber=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementNumber","0");
           //     int elementNumber=Integer.parseInt(historyElementNumber);
           //     lpw.setSelection(elementNumber-1);
                return true;
            }
        }
        return false;
    }
}
