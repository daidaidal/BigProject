package control;

import java.awt.*;  

import javax.swing.*;  
import java.awt.event.*;  
import java.io.*;  
  
import javax.sound.sampled.*;  
  
import java.lang.*; 

public class MyRecord
{

    //瀹氫箟褰曢煶鏍煎紡
    AudioFormat af = null;
    //瀹氫箟鐩爣鏁版嵁琛�,鍙互浠庝腑璇诲彇闊抽鏁版嵁,璇� TargetDataLine 鎺ュ彛鎻愪緵浠庣洰鏍囨暟鎹鐨勭紦鍐插尯璇诲彇鎵�鎹曡幏鏁版嵁鐨勬柟娉曘��
    TargetDataLine td = null;
    //瀹氫箟婧愭暟鎹,婧愭暟鎹鏄彲浠ュ啓鍏ユ暟鎹殑鏁版嵁琛屻�傚畠鍏呭綋鍏舵贩棰戝櫒鐨勬簮銆傚簲鐢ㄧ▼搴忓皢闊抽瀛楄妭鍐欏叆婧愭暟鎹锛岃繖鏍峰彲澶勭悊瀛楄妭缂撳啿骞跺皢瀹冧滑浼犻�掔粰娣烽鍣ㄣ��
    SourceDataLine sd = null;
    //瀹氫箟瀛楄妭鏁扮粍杈撳叆杈撳嚭娴�
    ByteArrayInputStream bais = null;
    ByteArrayOutputStream baos = null;
    //瀹氫箟闊抽杈撳叆娴�
    AudioInputStream ais = null;
    //瀹氫箟鍋滄褰曢煶鐨勬爣蹇楋紝鏉ユ帶鍒跺綍闊崇嚎绋嬬殑杩愯
    Boolean stopflag = false;



    //鏋勯�犲嚱鏁�
    public MyRecord()
    {

    }
    //寮�濮嬪綍闊�
    public void capture()
    {
        try {
            //af涓篈udioFormat涔熷氨鏄煶棰戞牸寮�
            af = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,af);
            td = (TargetDataLine)(AudioSystem.getLine(info));
            //鎵撳紑鍏锋湁鎸囧畾鏍煎紡鐨勮锛岃繖鏍峰彲浣胯鑾峰緱鎵�鏈夋墍闇�鐨勭郴缁熻祫婧愬苟鍙樺緱鍙搷浣溿��
            td.open(af);
            //鍏佽鏌愪竴鏁版嵁琛屾墽琛屾暟鎹� I/O
            td.start();

            //鍒涘缓鎾斁褰曢煶鐨勭嚎绋�
            Record record = new Record();
            Thread t1 = new Thread(record);
            t1.start();

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }
    }
    //鍋滄褰曢煶
    public void stop()
    {
        stopflag = true;
    }
    //鎾斁褰曢煶
    public void play()
    {
        //灏哹aos涓殑鏁版嵁杞崲涓哄瓧鑺傛暟鎹�
        byte audioData[] = baos.toByteArray();
        //杞崲涓鸿緭鍏ユ祦
        bais = new ByteArrayInputStream(audioData);
        af = getAudioFormat();
        ais = new AudioInputStream(bais, af, audioData.length/af.getFrameSize());

        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
            sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sd.open(af);
            sd.start();
            //鍒涘缓鎾斁杩涚▼
            Play py = new Play();
            Thread t2 = new Thread(py);
            t2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                //鍏抽棴娴�
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
    //淇濆瓨褰曢煶
    public void save()
    {
         //鍙栧緱褰曢煶杈撳叆娴�
        af = getAudioFormat();

        byte audioData[] = baos.toByteArray();
        bais = new ByteArrayInputStream(audioData);
        ais = new AudioInputStream(bais,af, audioData.length / af.getFrameSize());
        //瀹氫箟鏈�缁堜繚瀛樼殑鏂囦欢鍚�
        File file = null;
        //鍐欏叆鏂囦欢
        try {
            //浠ュ綋鍓嶇殑鏃堕棿鍛藉悕褰曢煶鐨勫悕瀛�
            //灏嗗綍闊崇殑鏂囦欢瀛樻斁鍒癋鐩樹笅璇煶鏂囦欢澶逛笅
            File filePath = new File("D:/璇煶鏂囦欢");
            if(!filePath.exists())
            {//濡傛灉鏂囦欢涓嶅瓨鍦紝鍒欏垱寤鸿鐩綍
                filePath.mkdir();
            }
            file = new File(filePath.getPath()+"/"+System.currentTimeMillis()+".mp3");
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //鍏抽棴娴�
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
    //璁剧疆AudioFormat鐨勫弬鏁�
    public AudioFormat getAudioFormat()
    {
        //涓嬮潰娉ㄩ噴閮ㄥ垎鏄彟澶栦竴绉嶉煶棰戞牸寮忥紝涓よ�呴兘鍙互
        AudioFormat.Encoding encoding = AudioFormat.Encoding.
        PCM_SIGNED ;
        float rate = 8000f;
        int sampleSize = 16;
        String signedString = "signed";
        boolean bigEndian = true;
        int channels = 1;
        return new AudioFormat(encoding, rate, sampleSize, channels,
                (sampleSize / 8) * channels, rate, bigEndian);
//      //閲囨牱鐜囨槸姣忕鎾斁鍜屽綍鍒剁殑鏍锋湰鏁�
//      float sampleRate = 16000.0F;
//      // 閲囨牱鐜�8000,11025,16000,22050,44100
//      //sampleSizeInBits琛ㄧず姣忎釜鍏锋湁姝ゆ牸寮忕殑澹伴煶鏍锋湰涓殑浣嶆暟
//      int sampleSizeInBits = 16;
//      // 8,16
//      int channels = 1;
//      // 鍗曞０閬撲负1锛岀珛浣撳０涓�2
//      boolean signed = true;
//      // true,false
//      boolean bigEndian = true;
//      // true,false
//      return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);
    }
    //褰曢煶绫伙紝鍥犱负瑕佺敤鍒癕yRecord绫讳腑鐨勫彉閲忥紝鎵�浠ュ皢鍏跺仛鎴愬唴閮ㄧ被
    class Record implements Runnable
    {
        //瀹氫箟瀛樻斁褰曢煶鐨勫瓧鑺傛暟缁�,浣滀负缂撳啿鍖�
        byte bts[] = new byte[10000];
        //灏嗗瓧鑺傛暟缁勫寘瑁呭埌娴侀噷锛屾渶缁堝瓨鍏ュ埌baos涓�
        //閲嶅啓run鍑芥暟
        public void run() {
            baos = new ByteArrayOutputStream();
            try {
                System.out.println("ok3");
                stopflag = false;
                while(stopflag != true)
                {
                    //褰撳仠姝㈠綍闊虫病鎸変笅鏃讹紝璇ョ嚎绋嬩竴鐩存墽琛�
                    //浠庢暟鎹鐨勮緭鍏ョ紦鍐插尯璇诲彇闊抽鏁版嵁銆�
                    //瑕佽鍙朾ts.length闀垮害鐨勫瓧鑺�,cnt 鏄疄闄呰鍙栫殑瀛楄妭鏁�
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
                    //鍏抽棴鎵撳紑鐨勫瓧鑺傛暟缁勬祦
                    if(baos != null)
                    {
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    td.drain();
                    td.close();
                }
            }
        }

    }
    //鎾斁绫�,鍚屾牱涔熷仛鎴愬唴閮ㄧ被
    class Play implements Runnable
    {
        //鎾斁baos涓殑鏁版嵁鍗冲彲
        public void run() {
            byte bts[] = new byte[10000];
            try {
                int cnt;
                //璇诲彇鏁版嵁鍒扮紦瀛樻暟鎹�
                while ((cnt = ais.read(bts, 0, bts.length)) != -1)
                {
                    if (cnt > 0)
                    {
                        //鍐欏叆缂撳瓨鏁版嵁
                        //灏嗛煶棰戞暟鎹啓鍏ュ埌娣烽鍣�
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
