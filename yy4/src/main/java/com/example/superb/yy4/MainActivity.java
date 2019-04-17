package com.example.superb.yy4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {
    PipedInputStream in;
    boolean isRrcord;
    mAudio mm ;
    mAudioPlayer m;

    TextView T1,T2;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.search_close_btn);

        T1=findViewById(R.id.dddd);
        isRrcord = false;

    }
    public void btnclick(View v){
        if (isRrcord){
            isRrcord = false;
            mm.stopRecord();m.stopPlay();
            btn.setText("开始");
            T1.setText("点击开始");

        }else{
            isRrcord = true;
            startRecord();

            btn.setText("停止");

            T1.setText("点击停止");

        }
    }


    private void startRecord(){
        in = new PipedInputStream();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    mm = new mAudio(MainActivity.this, in);
                    mm.StartAudioData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                PipedOutputStream pout = new PipedOutputStream();
                m = new mAudioPlayer();
                try {
                    m.setOutputStream(pout);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            m.startPlayAudio();
                        }
                    }).start();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                int size = 0 ;
                try {
                    while (true){
                        while (in.available()>0){
                            size = in.read(buffer);
                            pout.write(buffer, 0, size);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
