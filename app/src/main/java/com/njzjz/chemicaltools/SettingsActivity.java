package com.njzjz.chemicaltools;

/**
 * Created by zeng on 2016/9/23.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.mikepenz.aboutlibraries.Libs;
import com.sangbo.autoupdate.CheckVersion;
import com.tencent.stat.StatService;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
                        String qqid = avObject.getString("qqid");
                        String qqname = avObject.getString("qqname");
                        String elementnumber_limit = avObject.getString("elementnumber_limit");
                        if (elementnumber_limit == null) elementnumber_limit = "118";
                        String examMode = avObject.getString("examMode");
                        if (examMode == null) examMode = "0";
                        Boolean setting_examOptionMode = avObject.getBoolean("setting_examOptionMode");
                        String pKw = avObject.getString("pKw");
                        if (pKw == null) pKw = "14";
                        PreferenceUtils.setPrefString(getApplicationContext(), "qqid", qqid);
                        PreferenceUtils.setPrefString(getApplicationContext(), "qqname", qqname);
                        PreferenceUtils.setPrefString(getApplicationContext(), "elementnumber_limit", elementnumber_limit);
                        PreferenceUtils.setPrefString(getApplicationContext(), "examMode", examMode);
                        PreferenceUtils.setPrefBoolean(getApplicationContext(), "setting_examOptionMode", setting_examOptionMode);
                        PreferenceUtils.setPrefString(getApplicationContext(), "pKw", pKw);
                    }
                }}
            );
        }

        PreferenceFragment fragment = new PreferenceFragment() {
            @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                               Bundle savedInstanceState) {
                View view = super.onCreateView(inflater, container, savedInstanceState);
                //Set night-mode for background
                return view;
            }

            @Override public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.settings_general);
                PreferenceScreen Login= (PreferenceScreen) findPreference("login");
                Login.setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener() {
                            @Override public boolean onPreferenceClick(Preference preference) {
                                AVUser currentUser = AVUser.getCurrentUser();
                                if(currentUser !=null){
                                    AVUser.logOut();// 清除缓存用户对象
                                    finish();
                                    Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                }else {
                                    openLogin();
                                }
                                return true;
                            }
                        });
                AVUser currentUser = AVUser.getCurrentUser();
                if (currentUser != null) {
                    // 有
                    Login.setTitle(getString(R.string.action_sign_out));
                    Login.setSummary(currentUser.getUsername());
                    String qqid=PreferenceUtils.getPrefString(getApplicationContext(),"qqid","");
                    if(qqid!=""){
                        String qqname=PreferenceUtils.getPrefString(getApplicationContext(),"qqname","");
                        findPreference("qq").setSummary(qqname);
                    }
                } else {
                    //缓存用户对象为空时，可打开用户注册界面…
                    Login.setTitle(getString(R.string.action_sign_in));
                    findPreference("qq").setEnabled(false);
                }
                findPreference("qq").setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener() {
                            @Override
                            public boolean onPreferenceClick(Preference preference) {
                                UMShareAPI  mShareAPI = UMShareAPI.get( SettingsActivity.this );
                                mShareAPI.doOauthVerify(SettingsActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                                return true;
                            }
                        }
                );
                /*
                findPreference("wechat").setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener() {
                            @Override
                            public boolean onPreferenceClick(Preference preference) {
                                UMShareAPI  mShareAPI = UMShareAPI.get( SettingsActivity.this );
                                mShareAPI.doOauthVerify(SettingsActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                                return true;
                            }
                        }
                );*/
                findPreference("about").setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener() {
                            @Override public boolean onPreferenceClick(Preference preference) {
                                new Libs.Builder().withActivityTitle( getString(R.string.button_About)).withFields(R.string.class.getFields()).start(getActivity());
                                return false;
                            }
                        });
                findPreference("clearSettings").setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener() {
                            @Override public boolean onPreferenceClick(Preference preference) {
                                showSimpleDialog(getView());
                                return false;
                            }
                        });
                findPreference("update").setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener(){
                            @Override public boolean onPreferenceClick(Preference preference){
                                CheckVersion.checkUrl = "http://test-10061032.cos.myqcloud.com/version.txt";     //定义服务器版本信息
                                CheckVersion.update(SettingsActivity.this,true);
                                return false;
                            }
                        });
                ListPreference elementnumber_limitList= (ListPreference) findPreference("elementnumber_limit");
                elementnumber_limitList.setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                ListPreference elementnumber_limitList= (ListPreference) findPreference("elementnumber_limit");
                                String elementnumber_limit=newValue.toString();
                                PreferenceUtils.setPrefString(getApplicationContext(),"elementnumber_limit",elementnumber_limit);
                                AVUser currentUser = AVUser.getCurrentUser();
                                if (currentUser != null) {
                                    AVUser.getCurrentUser().put("elementnumber_limit", elementnumber_limit);
                                    AVUser.getCurrentUser().saveInBackground();
                                }
                                elementnumber_limitList.setSummary(elementnumber_limit);
                                Intent intent=new Intent();
                                intent.putExtra("result","1");
                                setResult(1001,intent);
                                return true;
                            }
                        }
                );
                String elementnumber_limit=PreferenceUtils.getPrefString(getApplicationContext(),"elementnumber_limit","118");
                switch (elementnumber_limit){
                    case "18":
                        elementnumber_limitList.setValueIndex(0);
                        break;
                    case "36":
                        elementnumber_limitList.setValueIndex(1);
                        break;
                    case "54":
                        elementnumber_limitList.setValueIndex(2);
                        break;
                    case "86":
                        elementnumber_limitList.setValueIndex(3);
                        break;
                    case "118":
                        elementnumber_limitList.setValueIndex(4);
                        break;
                };
                elementnumber_limitList.setSummary(elementnumber_limit);
                ListPreference examModeList= (ListPreference) findPreference("examMode");
                examModeList.setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                ListPreference examModeList= (ListPreference) findPreference("examMode");
                                String examMode=newValue.toString();
                                PreferenceUtils.setPrefString(getApplicationContext(),"examMode",examMode);
                                AVUser currentUser = AVUser.getCurrentUser();
                                if (currentUser != null) {
                                    AVUser.getCurrentUser().put("examMode", examMode);
                                    AVUser.getCurrentUser().saveInBackground();
                                }
                                examModeList.setSummary(examModeList.getEntries()[Integer.parseInt(examMode)]);
                                Intent intent=new Intent();
                                intent.putExtra("result","1");
                                setResult(1001,intent);
                                return true;
                            }
                        }
                );
                int examMode=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examMode","0"));
                examModeList.setValueIndex(examMode);
                examModeList.setSummary(examModeList.getEntry());
                CheckBoxPreference setting_examOptionModeCheck= (CheckBoxPreference) findPreference("setting_examOptionMode");
                setting_examOptionModeCheck.setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                boolean setting_examOptionMode=Boolean.valueOf(newValue.toString());
                                PreferenceUtils.setPrefBoolean(getApplicationContext(),"setting_examOptionMode",setting_examOptionMode);
                                AVUser currentUser = AVUser.getCurrentUser();
                                if (currentUser != null) {
                                    AVUser.getCurrentUser().put("setting_examOptionMode", setting_examOptionMode);
                                    AVUser.getCurrentUser().saveInBackground();
                                }
                                Intent intent=new Intent();
                                intent.putExtra("result","1");
                                setResult(1001,intent);
                                return true;
                            }
                        }
                );
                Boolean setting_examOptionMode=PreferenceUtils.getPrefBoolean(getApplicationContext(),"setting_examOptionMode",false);
                setting_examOptionModeCheck.setChecked(setting_examOptionMode);
                EditTextPreference pKwEditText= (EditTextPreference) findPreference("pKw");
                pKwEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String pKw=newValue.toString();
                        if (isNumeric(pKw)) {
                            PreferenceUtils.setPrefString(getApplicationContext(), "pKw", pKw);
                            AVUser currentUser = AVUser.getCurrentUser();
                            if (currentUser != null) {
                                AVUser.getCurrentUser().put("pKw", pKw);
                                AVUser.getCurrentUser().saveInBackground();
                            }
                            EditTextPreference pKwEditText = (EditTextPreference) findPreference("pKw");
                            pKwEditText.setSummary(pKw);
                            return true;
                        }else return false;
                    }});
                String pKw=PreferenceUtils.getPrefString(getApplicationContext(),"pKw","14");
                pKwEditText.setText(pKw);
                pKwEditText.setSummary(pKw);
            }

        };
        getFragmentManager().beginTransaction()
                .replace(R.id.include_settings_container, fragment)
                .commit();
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
    public static boolean isNumeric(String s){	try{	Double.parseDouble(s);return true;	}catch (Exception e){return false;}}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //菜单栏返回键功能
            case android.R.id.home:
                Intent intent = new Intent(this, TitleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showSimpleDialog(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.setting_clearSettings);
        builder.setMessage(R.string.setting_message);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.postive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferenceUtils.clearPreference(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
                String qqid=PreferenceUtils.getPrefString(getApplicationContext(),"qqid","");
                String qqname=PreferenceUtils.getPrefString(getApplicationContext(),"qqname","");
                String historyElementOutput = PreferenceUtils.getPrefString(getApplicationContext(), "historyElementOutput", "");
                String historyElementOutputHtml = PreferenceUtils.getPrefString(getApplicationContext(), "historyElementOutputHtml", "");
                String historyElementNumber = PreferenceUtils.getPrefString(getApplicationContext(), "historyElementNumber", "0");
                String historyElement = PreferenceUtils.getPrefString(getApplicationContext(), "historyElement", "");
                String historyMass = PreferenceUtils.getPrefString(getApplicationContext(), "historyMass", "");
                String historyMassOutput = PreferenceUtils.getPrefString(getApplicationContext(), "historyMassOutput", "");
                String historyAcidOutput = PreferenceUtils.getPrefString(getApplicationContext(), "historyAcidOutput", "");
                String examCorrectNumber=PreferenceUtils.getPrefString(getApplicationContext(),"examCorrectNumber","0");
                String examIncorrectnumber=PreferenceUtils.getPrefString(getApplicationContext(),"examIncorrectnumber","0");
                String elementnumber_limit=PreferenceUtils.getPrefString(getApplicationContext(),"elementnumber_limit","118");
                String examMode=PreferenceUtils.getPrefString(getApplicationContext(),"examMode","0");
                Boolean setting_examOptionMode=PreferenceUtils.getPrefBoolean(getApplicationContext(),"setting_examOptionMode",false);
                String pKw=PreferenceUtils.getPrefString(getApplicationContext(),"pKw","14");
                AVUser.getCurrentUser().put("qqid", qqid);
                AVUser.getCurrentUser().put("qqname", qqname);
                AVUser.getCurrentUser().put("historyElementOutput", historyElementOutput);
                AVUser.getCurrentUser().put("historyElementOutputHtml", historyElementOutputHtml);
                AVUser.getCurrentUser().put("historyElementNumber", historyElementNumber);
                AVUser.getCurrentUser().put("historyElement", historyElement);
                AVUser.getCurrentUser().put("historyMass", historyMass);
                AVUser.getCurrentUser().put("historyMassOutput", historyMassOutput);
                AVUser.getCurrentUser().put("historyAcidOutput", historyAcidOutput);
                AVUser.getCurrentUser().put("examCorrectNumber", examCorrectNumber);
                AVUser.getCurrentUser().put("examIncorrectnumber", examIncorrectnumber);
                AVUser.getCurrentUser().put("elementnumber_limit", elementnumber_limit);
                AVUser.getCurrentUser().put("examMode", examMode);
                AVUser.getCurrentUser().put("setting_examOptionMode", setting_examOptionMode);
                AVUser.getCurrentUser().put("pKw", pKw);
                AVUser.getCurrentUser().saveInBackground();
                finish();
                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if(requestCode==1002 && resultCode==1003)
            if(data.getStringExtra("result").equals("1")){
                finish();
                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
    }
    public void openLogin(){
        Intent intent =new Intent(this, LoginActivity.class);
        startActivityForResult(intent,1002);
    }
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            String uid = data.get("uid");
            if (!TextUtils.isEmpty(uid)) {
                // uid不为空，获取用户信息
                UMShareAPI mShareAPI = UMShareAPI.get(SettingsActivity.this);
                mShareAPI.getPlatformInfo(SettingsActivity.this, platform, umAuthListener2);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();
        }
    };
    private UMAuthListener umAuthListener2 = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String username=data.get("screen_name");
            String openid=data.get("openid");
            /*
            if(platform==SHARE_MEDIA.WEIXIN){
                PreferenceUtils.setPrefString(getApplicationContext(), "wechatid", openid);
                PreferenceUtils.setPrefString(getApplicationContext(), "wechatname", username);
                AVUser.getCurrentUser().put("wechatid", openid);
                AVUser.getCurrentUser().put("wechatname", username);
                AVUser.getCurrentUser().saveInBackground();
            }else*/
            if(platform==SHARE_MEDIA.QQ){
                PreferenceUtils.setPrefString(getApplicationContext(), "qqid", openid);
                PreferenceUtils.setPrefString(getApplicationContext(), "qqname", username);
                AVUser.getCurrentUser().put("qqid", openid);
                AVUser.getCurrentUser().put("qqname", username);
                AVUser.getCurrentUser().saveInBackground();
            }
            finish();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };
}