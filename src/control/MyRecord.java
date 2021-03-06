package control;

import java.applet.Applet;
import java.awt.*;  

import javax.swing.*;  
import java.awt.event.*;  
import java.io.*;  
  
import javax.sound.sampled.*;  
  
import java.lang.*;
import java.net.URL; 
import javax.media.ControllerEvent;  
import javax.media.ControllerListener;  
import javax.media.NoPlayerException;  
import javax.media.Player;  
import javax.media.Manager;  
import javax.media.MediaLocator;  
import javax.media.EndOfMediaEvent;  
import javax.media.PrefetchCompleteEvent;  
import javax.media.RealizeCompleteEvent;
import javax.media.bean.playerbean.MediaPlayer;

import java.io.*;  
import java.util.*;  

public class MyRecord
{
	
    //定义录音格式
    AudioFormat af = null;
    //定义目标数据衿,可以从中读取音频数据,诿 TargetDataLine 接口提供从目标数据行的缓冲区读取承捕获数据的方法㿿
    TargetDataLine td = null;
    //定义源数据行,源数据行是可以写入数据的数据行㿂它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传鿒给混频器㿿
    SourceDataLine sd = null;
    //定义字节数组输入输出浿
    ByteArrayInputStream bais = null;
    ByteArrayOutputStream baos = null;
    //定义音频输入浿
    AudioInputStream ais = null;
    //定义停止录音的标志，来控制录音线程的运行
    Boolean stopflag = false;
    private MediaPlayer playMP3;
    
    //构鿠函敿
    public MyRecord()
    {

    }
    //弿始录響
    public void capture()
    {
        try {
            //af为AudioFormat也就是音频格弿
            af = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,af);
            td = (TargetDataLine)(AudioSystem.getLine(info));
            //打开具有指定格式的行，这样可使行获得承有所霿的系统资源并变得可操作㿿
            td.open(af);
            //允许某一数据行执行数捿 I/O
            td.start();

            //创建播放录音的线稿
            Record record = new Record();
            Thread t1 = new Thread(record);
            t1.start();

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }
    }
    //停止录音
    public void stop()
    {
        stopflag = true;
    }
    //播放录音
    public void play()
    {
    	//将baos中的数据转换为字节数捿
    	FileInputStream fi=null;
    	baos = new ByteArrayOutputStream();
		File file = new File("./src/record/1.mp3");
		long fileSize = file.length();
		try {
			fi = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("fail open");
		}
		
        byte audioData[] = new byte[(int) fileSize];  
        int offset = 0;  
        int numRead = 0;  
        try {
			while (offset < audioData.length  
			&& (numRead = fi.read(audioData, offset, audioData.length - offset)) >= 0) {  
			    offset += numRead;  
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
        // 确保所有数据均被读取  
        if (offset != audioData.length) {  
        	System.out.println("canque");
        }  
        try {
			fi.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
        //转换为输入流
        bais = new ByteArrayInputStream(audioData);
        af = getAudioFormat();
        ais = new AudioInputStream(bais, af, audioData.length/af.getFrameSize());

        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
            sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sd.open(af);
            sd.start();
            //创建播放进程
            Play py = new Play();
            Thread t2 = new Thread(py);
            t2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                //关闭浿
                if(ais != null)
                {
                    ais.close();
                }
                if(bais != null)
                {
                    bais.close();
                }
                if(baos != null)
                {
                    baos.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //保存录音
    public void save()
    {
         //取得录音输入浿
    	af = getAudioFormat();

        byte audioData[] = baos.toByteArray();
        bais = new ByteArrayInputStream(audioData);
        ais = new AudioInputStream(bais,af, audioData.length / af.getFrameSize());
        //定义朿终保存的文件吿
        File file = null;
        //写入文件
        try {
            //以当前的时间命名录音的名孿
            //将录音的文件存放到F盘下语音文件夹下
            File filePath = new File("./src/record");
            if(!filePath.exists())
            {//如果文件不存在，则创建该目录
                filePath.mkdir();
            }
            file = new File("./src/record/1.mp3");
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭浿
            try {

                if(bais != null)
                {
                    bais.close();
                }
                if(ais != null)
                {
                    ais.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //设置AudioFormat的参敿
    public AudioFormat getAudioFormat()
    {
        //下面注释部分是另外一种音频格式，两迅都可以
        AudioFormat.Encoding encoding = AudioFormat.Encoding.
        PCM_SIGNED ;
        float rate = 8000f;
        int sampleSize = 16;
        String signedString = "signed";
        boolean bigEndian = true;
        int channels = 1;
        AudioFormat a=new AudioFormat(encoding, rate, sampleSize, channels,
                (sampleSize / 8) * channels, rate, bigEndian);
        return a;
//      //采样率是每秒播放和录制的样本敿
//      float sampleRate = 16000.0F;
//      // 采样玿8000,11025,16000,22050,44100
//      //sampleSizeInBits表示每个具有此格式的声音样本中的位数
//      int sampleSizeInBits = 16;
//      // 8,16
//      int channels = 1;
//      // 单声道为1，立体声丿2
//      boolean signed = true;
//      // true,false
//      boolean bigEndian = true;
//      // true,false
//      return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);
    }
    //录音类，因为要用到MyRecord类中的变量，承以将其做成内部类
    class Record implements Runnable
    {
        //定义存放录音的字节数绿,作为缓冲匿
        byte bts[] = new byte[10000];
        //将字节数组包装到流里，最终存入到baos丿
        //重写run函数
        public void run() {
            baos = new ByteArrayOutputStream();
            try {
                System.out.println("ok3");
                stopflag = false;
                while(stopflag != true)
                {
                    //当停止录音没按下时，该线程一直执衿
                    //从数据行的输入缓冲区读取音频数据〿
                    //要读取bts.length长度的字芿,cnt 是实际读取的字节敿
                    int cnt = td.read(bts, 0, bts.length);
                    if(cnt > 0)
                    {
                        baos.write(bts, 0, cnt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    //关闭打开的字节数组流
                    if(baos != null)
                    {
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    //td.drain();
                    td.close();
                }
            }
        }

    }
    //播放籿,同样也做成内部类
    class Play implements Runnable
    {
        //播放baos中的数据即可
        public void run() {
            byte bts[] = new byte[10000];
            try {
                int cnt;
                //读取数据到缓存数捿
                while ((cnt = ais.read(bts, 0, bts.length)) != -1)
                {
                    if (cnt > 0)
                    {
                        //写入缓存数据
                        //将音频数据写入到混频噿
                        sd.write(bts, 0, cnt);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                 sd.drain();
                 sd.close();
            }


        }
    }
}
