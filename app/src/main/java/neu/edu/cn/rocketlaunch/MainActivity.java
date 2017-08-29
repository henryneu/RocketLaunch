package neu.edu.cn.rocketlaunch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mStartRocketButton;
    private Button mStopRocketButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化布局文件中的View
        initView();
    }

    /**
     * 初始化布局文件中的View
     */
    private void initView() {
        mStartRocketButton = (Button) findViewById(R.id.start_rocket);
        mStopRocketButton = (Button) findViewById(R.id.stop_rocket);
        mStartRocketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, RocketService.class));
                finish();
            }
        });
        mStopRocketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, RocketService.class));
                finish();
            }
        });
    }
}
