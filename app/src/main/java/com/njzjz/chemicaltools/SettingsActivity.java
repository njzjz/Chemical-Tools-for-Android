package com.njzjz.chemicaltools;

/**
 * Created by zeng on 2016/9/23.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.aboutlibraries.Libs;
import com.sangbo.autoupdate.CheckVersion;
import com.tencent.stat.StatService;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

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
                findPreference("elementnumber_limit").setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                ListPreference elementnumber_limitList= (ListPreference) findPreference("elementnumber_limit");
                                String elementnumber_limit=newValue.toString();
                                PreferenceUtils.setPrefString(getApplicationContext(),"elementnumber_limit",elementnumber_limit);
                                elementnumber_limitList.setSummary(elementnumber_limit);
                                Intent intent=new Intent();
                                intent.putExtra("result","1");
                                setResult(1001,intent);
                                return true;
                            }
                        }
                );
                ListPreference elementnumber_limitList= (ListPreference) findPreference("elementnumber_limit");
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
                findPreference("examMode").setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                ListPreference examModeList= (ListPreference) findPreference("examMode");
                                String examMode=newValue.toString();
                                PreferenceUtils.setPrefString(getApplicationContext(),"examMode",examMode);
                                examModeList.setSummary(examModeList.getEntries()[Integer.parseInt(examMode)]);
                                Intent intent=new Intent();
                                intent.putExtra("result","1");
                                setResult(1001,intent);
                                return true;
                            }
                        }
                );
                ListPreference examModeList= (ListPreference) findPreference("examMode");
                int examMode=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examMode","0"));
                examModeList.setValueIndex(examMode);
                examModeList.setSummary(examModeList.getEntry());
                findPreference("setting_examOptionMode").setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                boolean setting_examOptionMode=Boolean.valueOf(newValue.toString());
                                PreferenceUtils.setPrefBoolean(getApplicationContext(),"setting_examOptionMode",setting_examOptionMode);
                                Intent intent=new Intent();
                                intent.putExtra("result","1");
                                setResult(1001,intent);
                                return true;
                            }
                        }
                );
                CheckBoxPreference setting_examOptionModeCheck= (CheckBoxPreference) findPreference("setting_examOptionMode");
                Boolean setting_examOptionMode=PreferenceUtils.getPrefBoolean(getApplicationContext(),"setting_examOptionMode",false);
                setting_examOptionModeCheck.setChecked(setting_examOptionMode); ;
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
}