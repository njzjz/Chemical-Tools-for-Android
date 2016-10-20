package com.njzjz.chemicaltools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.mikepenz.aboutlibraries.Libs;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class DeviationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviation);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        Button mButton = (Button) findViewById(R.id.Button);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText EditText = (EditText) findViewById(R.id.EditText);
                String input=EditText.getText().toString().trim();
                String[] inputArray=input.split("\n");
                int t=inputArray.length,numnum=0,pointnum=0;
                double sum=0;
                double[] data=new double[t];
                if(t>1){
                    try {
                        for (int i = 0; i < t; i++) {
                            inputArray[i]=inputArray[i].trim();
                            data[i]=Double.parseDouble(inputArray[i]);
                            sum=sum+data[i];
                            int len=inputArray[i].length(),pointlen=0;
                            if(inputArray[i].substring(0,1)=="-")len=len-1;
                            if(inputArray[i].contains(".")){
                                len=len-1;
                                pointlen=len-inputArray[i].indexOf(".");
                                if(Math.abs(data[i])<1){
                                    int zeronum=(int) Math.floor(Math.log10(Math.abs(data[i])));
                                    len=len+zeronum;
                                }
                            }
                            if(i>0){
                                if(len<numnum)numnum=len;
                                if(pointlen<pointnum)pointnum=pointlen;
                            }else{
                                numnum=len;
                                pointnum=pointlen;
                            }
                        }
                        double average=sum/t,abssum=0,squresum=0;
                        for(int i=0;i<t;i++){
                            double xabs=Math.abs(average-data[i]);
                            double xsqure=Math.pow(average-data[i],2);
                            abssum+=xabs;
                            squresum+=xsqure;
                        }
                        double deviation=abssum/t;
                        double deviation_relatibe=deviation/average*1000;
                        double s=Math.sqrt(squresum/(t-1));
                        double s_relatibe=s/deviation*1000;
                        String output="您输入的数据："+input.replaceAll("\n","，")+"\n平均数："+String.format("%."+pointnum+"f",average)+
                        "\n平均偏差："+String.format("%."+pointnum+"f",deviation)+"\n相对平均偏差："+String.format("%."+(numnum-1)+"e",deviation_relatibe)+
                        "‰\n标准偏差："+String.format("%."+(numnum-1)+"e",s)+"\n相对标准偏差："+String.format("%."+(numnum-1)+"e",s_relatibe)+"‰";
                        TextView textView=(TextView)findViewById(R.id.Textview);
                        textView.setText(output);
                        PreferenceUtils.setPrefString(getApplicationContext(),"historyDeviation",output);
                        AVUser currentUser = AVUser.getCurrentUser();
                        if (currentUser != null) {
                            AVUser.getCurrentUser().put("historyDeviation",output);
                            AVUser.getCurrentUser().saveInBackground();
                        }
                    }catch (Exception e){
                        Snackbar.make(v, getString(R.string.error_name), Snackbar.LENGTH_LONG)
                                .setAction("Error", null).show();
                    }
                }else{
                    Snackbar.make(v, getString(R.string.error_name), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }
            }
        });
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 有
            AVUser.getCurrentUser().fetchIfNeededInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if(e==null) {
                        String historyDeviation = avObject.getString("historyDeviation");
                        PreferenceUtils.setPrefString(getApplicationContext(), "historyDeviation", historyDeviation);
                    }
                }});
        }
        TextView textView=(TextView)findViewById(R.id.Textview);
        String historyDeviation=PreferenceUtils.getPrefString(getApplicationContext(),"historyDeviation","");
        if(!historyDeviation.equals("")){
            textView.setText(historyDeviation);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
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
                UMImage image = new UMImage(this, R.drawable.ic_launcher);//资源文件
                new ShareAction(this).withText(getString(R.string.gas_welcome)).withTargetUrl("http://chemapp.njzjz.win/gas.php").withMedia(image).withTitle(getString(R.string.app_name))
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.MORE)
                        .open();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
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
}
