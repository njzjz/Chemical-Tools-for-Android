package com.njzjz.chemicaltools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;
import com.tencent.stat.StatService;

public class AcidActivity extends AppCompatActivity {

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
                String pKwstr=PreferenceUtils.getPrefString(getApplicationContext(),"pKw","14");
                double pKw=Double.parseDouble(pKwstr);
                Switch switchAB=(Switch) findViewById(R.id.switchAB);
                boolean AorB=!switchAB.isChecked();
                String ABname;
                if(AorB)ABname="A";else ABname="B";
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
                        if (isNumeric(strpKaArray[i])) valpKa[i]=Double.parseDouble(strpKaArray[i]);
                        else error=true;
                    }
                    if(!error){
                        double pH=calpH(valpKa,c,pKw);
                        double[] cAB=calpHtoc(valpKa,c,pH);
                        if(!AorB) pH=pKw-pH;
                        double H=Math.pow(10,-pH);
                        String acidOutput="c="+strc+"mol/L, ";
                        for(int i=0;i<valpKa.length;i++){
                            if(AorB)acidOutput=acidOutput+"pKa";else acidOutput=acidOutput+"pKb";
                            if(valpKa.length>1)acidOutput=acidOutput+String.valueOf(i+1);
                            acidOutput=acidOutput+"="+strpKaArray[i]+", ";
                        }
                        acidOutput=acidOutput+"\n"+String.format(getString(R.string.acidpHOut_name),pH);
                        acidOutput=acidOutput+"\n"+"c(H+)="+String.format("%1$.2e",H)+"mol/L,";
                        for(int i=0;i<cAB.length;i++){
                            String cABoutput="c(";
                            if(AorB){
                                if(i<cAB.length-1){
                                    cABoutput=cABoutput+"H";
                                    if(cAB.length-i>2) cABoutput=cABoutput+ String.valueOf(cAB.length - i-1);
                                }
                                cABoutput=cABoutput+ABname;
                                if(i>0){
                                    if(i>1) cABoutput=cABoutput+ String.valueOf(i);
                                    cABoutput=cABoutput+"-";
                                }
                            }else{
                                cABoutput=cABoutput+"B";
                                if(cAB.length-i>2){
                                    cABoutput=cABoutput+"(OH)"+ String.valueOf(cAB.length - i-1);
                                }else if(cAB.length-i==2){
                                    cABoutput=cABoutput+"OH";
                                }
                                if(i>0){
                                    if(i>1) cABoutput=cABoutput+String.valueOf(i);
                                    cABoutput=cABoutput+"+";
                                }
                            }
                            cABoutput=cABoutput+")=";
                            acidOutput=acidOutput+"\n"+cABoutput+String.format("%1$.2e",cAB[i])+"mol/L,";
                        }
                        TextView acidTextview = (TextView) findViewById(R.id.acidTextview);
                        acidTextview.setText(acidOutput);
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
        String historyAcidOutput=PreferenceUtils.getPrefString(getApplicationContext(),"historyAcidOutput","");
        if(!historyAcidOutput.equals("")){
            acidTextview.setText(historyAcidOutput);
        }
    };
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
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.app_name));
                TextView acidTextview=(TextView) findViewById(R.id.acidTextview);
                share.putExtra(Intent.EXTRA_TEXT,
                        acidTextview.getText().toString());
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
}
