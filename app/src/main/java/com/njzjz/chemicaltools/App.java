package com.njzjz.chemicaltools;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.sangbo.autoupdate.CheckVersion;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by zeng on 2016/10/4.
 */

public class App extends Application {
    {

        PlatformConfig.setWeixin("wxce20db4ddce39c01", "7f42598bfdbfb74be4eaa121baef6746");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("1105651247", "P4CtQZJ1Sb5wRKus");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        String appkey = "A55JRMC53SPT";

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"wUzGKF5dp34OqCeaI0VwVG8E-gzGzoHsz","QiyXtJjBHFJCIVYQRbrKFiB7");

        // 在startStatService之前调用StatConfig配置类接口，使得MTA配置及时生效
        // 初始化并启动MTA
        // 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式或MTA分配的代码。
        // 其它普通的app可自行选择是否调用
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, appkey,
                    com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败

        }
        //     UMShareAPI.get(this);

        CheckVersion.checkUrl = "http://test-10061032.cos.myqcloud.com/version.txt";     //定义服务器版本信息
        CheckVersion.update(this);
   }
}