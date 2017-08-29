package neu.edu.cn.rocketlaunch;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class RocketService extends Service {

    // 手机窗体布局的管理者
    private WindowManager mWindowManager;
    // 手机窗体的布局
    private WindowManager.LayoutParams mParams;
    // 展示小火箭的自定义布局
    private View mToastRocketView;
    // 展示小火箭的ImageView
    private ImageView mRocketImage;
    // 手机窗体的宽度
    private int mWindowWidth;
    // 手机窗体的高度
    private int mWindowHeight;
    // 消息传递机制
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mParams.y = (Integer) msg.obj;
            mWindowManager.updateViewLayout(mToastRocketView, mParams);
        }
    };

    @Override
    public void onCreate() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 获取手机屏幕的宽高值
        mWindowWidth = mWindowManager.getDefaultDisplay().getWidth();
        mWindowHeight = mWindowManager.getDefaultDisplay().getHeight();
        mParams = new WindowManager.LayoutParams();
        // 服务启动，打开自定义Toast的控件
        showRocketView();
        // 拖拽小火箭到任意位置
        dragRocket();
        super.onCreate();
    }

    /**
     * 拖拽小火箭到任意位置
     */
    private void dragRocket() {
        mToastRocketView.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        // 两个方向上所移动的距离值
                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        mParams.x = mParams.x + disX;
                        mParams.y = mParams.y + disY;

                        if (mParams.x < 0) {
                            mParams.x = 0;
                        }

                        if (mParams.y < 0) {
                            mParams.y = 0;
                        }

                        if (mParams.x > mWindowManager.getDefaultDisplay().getWidth() - v.getWidth()) {
                            mParams.x = mWindowManager.getDefaultDisplay().getWidth() - v.getWidth();
                        }

                        if (mParams.y > mWindowManager.getDefaultDisplay().getHeight() - 21 - v.getHeight()) {
                            mParams.y = mWindowManager.getDefaultDisplay().getHeight() - 21 - v.getHeight();
                        }

                        // 更新小火箭的坐标位置X和Y值
                        mWindowManager.updateViewLayout(mToastRocketView, mParams);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        // 小火箭拖拽到手机屏幕下方的中间时，触发小火箭发射
                        if (mParams.x > mWindowWidth / 2 - 150 && mParams.x < mWindowWidth / 2 - mToastRocketView.getWidth() / 2 + 50
                                && mParams.y > mWindowHeight - mToastRocketView.getHeight() - 25) {
                            // 小火箭发射升空
                            launchRocket();
                            Intent intent = new Intent(RocketService.this, SmokeBackActivity.class);
                            // 服务中开启activity，需要设置任务栈
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 小火箭发射升空
     */
    private void launchRocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int disY = mWindowHeight / 5;
                for (int i = 0; i < 6; i++) {
                    int height = mWindowHeight - i * disY;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 子线程不能改变主线程中的UI的变化，因此，由消息机制告知主线程进行改变，并携带相应的值
                    Message msg = Message.obtain();
                    msg.obj = height;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 显示小火箭的自定义View
     */
    private void showRocketView() {
        // 自定义Toast
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        // 修改完左上角对齐
        mParams.gravity = Gravity.LEFT + Gravity.TOP;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 加载ToastRocketView显示效果的布局文件
        mToastRocketView = View.inflate(this, R.layout.toast_rocket_view, null);
        // 窗体布局中加入自定义的展示小火箭的View
        mWindowManager.addView(mToastRocketView, mParams);
        mRocketImage = (ImageView) mToastRocketView.findViewById(R.id.rocket_image);
        // 获取动画，并开启动画
        AnimationDrawable animDraw = (AnimationDrawable) mRocketImage.getBackground();
        animDraw.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mWindowManager != null && mToastRocketView != null) {
            mWindowManager.removeView(mToastRocketView);
        }
        super.onDestroy();
    }
}
