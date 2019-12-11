package com.example.hello.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hello.R;
import com.example.hello.model.OrderCheck;
import com.example.hello.model.OrderInfo;
import com.example.hello.presenter.impl.MainAPresenterImpl;
import com.example.hello.presenter.inter.IMainAPresenter;
import com.example.hello.view.inter.IMainAView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements IMainAView, View.OnClickListener {
    private static final String TAG = "Order";
    private static final String UID = "50937";
    private static final String UTOKEN = "377e5e6029891171315836dc9d28c62a";
    private static final String PCZAPI = "6ffe853bfUIOJOm3538f5";
    private static final String ADDRESS_ID = "4068";

    private ScheduledExecutorService ss = Executors.newScheduledThreadPool(5); //传入核心线程数

    private IMainAPresenter mIMainAPresenter;

    private Button btn;
    private Button btnStop;
    private EditText et;
    private TextView tvNum;
    private TextView tvLog;
    private final int MAX_NUM = 60;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btnStop = findViewById(R.id.btnStop);
        btn.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        et = findViewById(R.id.et);
        tvNum = findViewById(R.id.tvNum);
        tvLog = findViewById(R.id.tvLog);

        mIMainAPresenter = new MainAPresenterImpl(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            loopHttpRequest();
        } else if (view.getId() == R.id.btnStop) {
            Log.d(TAG, "手动结束抢单");
            ss.shutdown();
        }
    }

    private void loopHttpRequest() {
        int period = TextUtils.isEmpty(et.getText().toString().trim()) ? 3 : Integer.valueOf(et.getText().toString().trim());
        if (ss.isShutdown()||ss.isTerminated()) {
            ss = Executors.newScheduledThreadPool(5); //传入核心线程数
        }
        ss.scheduleAtFixedRate(startRunnable,0, period, TimeUnit.SECONDS); // 每3秒后开始任务
        ss.scheduleAtFixedRate(checkRunnable,0, 2, TimeUnit.SECONDS);
    }

    Runnable startRunnable = new Runnable() {
        @Override
        public void run() {
            startRequest();
        }
    };

    Runnable checkRunnable = new Runnable() {
        @Override
        public void run() {
            checkLostOrder();
        }
    };

    private void checkLostOrder() {
        Log.v(TAG, "检查未处理的订单……");
        HttpURLConnection connection = null;
        try{
            URL url = new URL("http://wpp.peczz.top/Apigame/getshoudan");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);// 使用 URL 连接进行输出
            connection.setDoInput(true);// 使用 URL 连接进行输入
            connection.setUseCaches(false);// 忽略缓存
            connection.connect();

            // 建立输出流，并写入数据
            OutputStream outputStream = connection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            String params = new StringBuffer().append("uid=").append(UID)
                    .append("&utoken=").append(UTOKEN)
                    .append("&pczapi=").append(PCZAPI)
                    .append("&types=1").append("p=1")
                    .toString();
            dataOutputStream.writeBytes(params);
            dataOutputStream.close();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 当正确响应时处理数据
                StringBuffer response = new StringBuffer();
                String line;
                BufferedReader responseReader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                // 处理响应流，必须与服务器响应流输出的编码一致
                while (null != (line = responseReader.readLine())) {
                    response.append(line);
                }
                responseReader.close();
                OrderCheck orderCheck = new Gson().fromJson(response.toString(), OrderCheck.class);

                showInfo(orderCheck.getMsg());
                if (orderCheck.getStatus() == 1000) {
//                    String orderId = orderCheck.getId();
//                    submitOrder(orderId);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null!= connection) {
                connection.disconnect();
            }
        }

    }

    private void startRequest() {
        Log.d(TAG, "抢单尝试……");
        HttpURLConnection connection = null;
        try{
            URL url = new URL("http://wpp.peczz.top/Apigame/sendordernow");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);// 使用 URL 连接进行输出
            connection.setDoInput(true);// 使用 URL 连接进行输入
            connection.setUseCaches(false);// 忽略缓存
            connection.connect();

            // 建立输出流，并写入数据
            OutputStream outputStream = connection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            String params = new StringBuffer().append("uid=").append(UID)
                    .append("&utoken=").append(UTOKEN)
                    .append("&pczapi=").append(PCZAPI)
                    .toString();
            dataOutputStream.writeBytes(params);
            dataOutputStream.close();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 当正确响应时处理数据
                StringBuffer response = new StringBuffer();
                String line;
                BufferedReader responseReader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                // 处理响应流，必须与服务器响应流输出的编码一致
                while (null != (line = responseReader.readLine())) {
                    response.append(line);
                }
                responseReader.close();
                OrderInfo orderInfo = new Gson().fromJson(response.toString(), OrderInfo.class);

                showInfo(orderInfo.getMsg());
                if (orderInfo.getStatus() == 1000) {
                    String orderId = orderInfo.getId();
                    submitOrder(orderId);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null!= connection) {
                connection.disconnect();
            }
        }


    }

    private void showInfo(final String msg) {
        Log.d(TAG, unicode2String(msg));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                tvLog.setText(tvLog.getText()+"\n"+msg);
            }
        });
    }

    private void submitOrder(String orderId) {
        Log.d(TAG, "提交订单: orderId = [" + orderId + "]");
        HttpURLConnection connection = null;
        try{
            URL url = new URL("http://wpp.peczz.top/Apiuser/cart3");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);// 使用 URL 连接进行输出
            connection.setDoInput(true);// 使用 URL 连接进行输入
            connection.setUseCaches(false);// 忽略缓存
            connection.connect();

            // 建立输出流，并写入数据
            OutputStream outputStream = connection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            String params = new StringBuffer().append("uid=").append(UID)
                    .append("&utoken=").append(UTOKEN)
                    .append("&pczapi=").append(PCZAPI)
                    .append("&address_id=").append(ADDRESS_ID)
                    .append("&pczapi=").append(orderId)
                    .toString();
            dataOutputStream.writeBytes(params);
            dataOutputStream.close();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 当正确响应时处理数据
                StringBuffer response = new StringBuffer();
                String line;
                BufferedReader responseReader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                // 处理响应流，必须与服务器响应流输出的编码一致
                while (null != (line = responseReader.readLine())) {
                    response.append(line);
                }
                responseReader.close();
                OrderInfo orderInfo = new Gson().fromJson(response.toString(), OrderInfo.class);
                if (orderInfo.getStatus() == 1000) {
                    showInfo("抢单成功了！！！！！！！"+orderId);
                    count++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvNum.setText("已抢："+ count);
                        }
                    });
                    if (count >= MAX_NUM) {
                        showInfo("已超过限制，自动停止");
                        ss.shutdown();
                    }
                } else {
                    showInfo("抢单失败，重试：" + orderId);
                    submitOrder(orderId);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null!= connection) {
                connection.disconnect();
            }
        }

    }

    @Override
    public <T> T request(int requestFlag) {
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }


    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }
}
