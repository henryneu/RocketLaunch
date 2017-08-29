package neu.edu.cn.rocketlaunch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SmokeBackActivity extends Activity {
    // 尾气喷射上部图片所在的ImageView
    private ImageView mSmokeTopImageView;
    // 尾气喷射下部图片所在的ImageView
    private ImageView mSmokeBottomImageView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 尾气喷射动画结束后关闭活动
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke_back);
        // 初始化布局文件
        initView();
    }

    /**
     * 初始化布局文件
     */
    private void initView() {
        mSmokeTopImageView = (ImageView) findViewById(R.id.smoke_top);
        mSmokeBottomImageView = (ImageView) findViewById(R.id.smoke_bottom);
        // 尾气喷射设置动画效果并开启
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        mSmokeTopImageView.startAnimation(alphaAnimation);
        mSmokeBottomImageView.startAnimation(alphaAnimation);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
