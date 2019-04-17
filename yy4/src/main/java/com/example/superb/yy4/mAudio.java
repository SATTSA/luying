package com.example.superb.yy4;



import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/*
 * To getaudio or play audio
 * */
public class mAudio {
    private AudioRecord audioRecord;
    private Context context;
    private boolean isRecording = false ;
    private PipedOutputStream outstream ;//利用管道传输数据
    public mAudio(Context context , PipedInputStream instream) throws IOException {
        this.context  = context;
        //初始化管道流 用于向外传输数据
        outstream = new PipedOutputStream();
        outstream.connect(instream);
    }
    public void StartAudioData(){//得到录音数据
        int frequency = 11025;

        //frequency采样率：音频的采样频率，每秒钟能够采样的次数，采样率越高，音质越高。
        // 给出的实例是44100、22050、11025但不限于这几个参数。
        // 例如要采集低质量的音频就可以使用4000、8000等低采样率。
        @SuppressWarnings("deprecation")
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;//立体声录制通道
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        //编码制式和采样大小：采集来的数据当然使用PCM编码(脉冲代码调制编码，
        // 即PCM编码。PCM通过抽样、量化、编码三个步骤将连续变化的模拟信号转换为数字编码。) android支持的采样大小16bit 或者8bit。
        // 当然采样大小越大，那么信息量越多，音质也越高，
        // 现在主流的采样大小都是16bit，在低质量的语音传输的时候8bit足够了。
        //
        int buffersize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
    //采集数据需要的缓冲区的大小，如果不知道最小需要的大小可以在getMinBufferSize()查看。
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                frequency, channelConfiguration, audioEncoding, buffersize);
        //音频源：指的是从哪里采集音频。这里我们当然是从麦克风采集音频，所以此参数的值为MIC

        //frequency采样率：音频的采样频率，每秒钟能够采样的次数，采样率越高，音质越高。
        // 给出的实例是44100、22050、11025但不限于这几个参数。
        // 例如要采集低质量的音频就可以使用4000、8000等低采样率。
        byte[]buffer  = new byte[buffersize];
        audioRecord.startRecording();//开始录音
        isRecording = true;
        int bufferReadSize = 1024;
        while (isRecording){
            audioRecord.read(buffer, 0, bufferReadSize);
            try {
                outstream.write(buffer, 0, bufferReadSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopRecord(){//停止录音
        isRecording = false;
        audioRecord.stop();
        try {
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

