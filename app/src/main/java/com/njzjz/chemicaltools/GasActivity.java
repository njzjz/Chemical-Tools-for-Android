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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mikepenz.aboutlibraries.Libs;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class GasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText EditText_p=(EditText)findViewById(R.id.textView_p);
                EditText EditText_V=(EditText)findViewById(R.id.textView_V);
                EditText EditText_n=(EditText)findViewById(R.id.textView_n);
                EditText EditText_T=(EditText)findViewById(R.id.textView_T);
                try {
                    double p = Double.parseDouble(EditText_p.getText().toString());
                    double V = Double.parseDouble(EditText_V.getText().toString());
                    double n = Double.parseDouble(EditText_n.getText().toString());
                    double T = Double.parseDouble(EditText_T.getText().toString());
                    double gasR = 8.314;
                    RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup1);
                    RadioButton mRadioButton = (RadioButton) findViewById(mRadioGroup.getCheckedRadioButtonId());
                    switch (mRadioButton.getText().toString()) {
                        case "p":
                            p = n * gasR * T / V;
                            EditText_p.setText(String.format("%.3f", p));
                            break;
                        case "V":
                            V = n * gasR * T / p;
                            EditText_V.setText(String.format("%.3f", V));
                            break;
                        case "n":
                            n = p * V / gasR / T;
                            EditText_n.setText(String.format("%.3f", n));
                            break;
                        case "T":
                            T = p * V / n / gasR;
                            EditText_T.setText(String.format("%.3f", T));
                            break;
                    }
                }catch (Exception e){
                    Snackbar.make(v, getString(R.string.error_name), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                }
            }
        });
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
