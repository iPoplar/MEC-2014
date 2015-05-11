
import java.awt.BorderLayout; 
import java.awt.Component; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.Panel; 
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException; 
import java.net.URL; 
 
import javax.media.CannotRealizeException; 
import javax.media.ControllerEvent; 
import javax.media.ControllerListener; 
import javax.media.EndOfMediaEvent; 
import javax.media.Manager; 
import javax.media.MediaLocator; 
import javax.media.NoPlayerException; 
import javax.media.Player; 
import javax.media.PrefetchCompleteEvent; 
import javax.media.RealizeCompleteEvent; 
import javax.media.Time; 
 
@SuppressWarnings({ "restriction", "unused" }) 
public class JMFSample implements ControllerListener 
{ 
	
	
	public JMFSample(String path)
	{
		//File file = new File(path);
		//secert(file);
		play(path);
		
	}
	
  /* public static void main(String[] args) 
    { 
        //JMFSample sp = new JMFSample(0 LY-51S���.mpg.mec);
	   new JMFSample("F:/0 LY-51S���.mpg.mec");
       
    } */
    
    private Player mediaPlayer; 
    private Frame f; 
    private Player player; 
    private Panel panel; 
    private Component visual; 
    private Component control = null; 
    
    public void play(String path)
    { 
        f = new Frame("JMF Sample1"); 
        f.addWindowListener(new WindowAdapter() 
        { 
            public void windowClosing(WindowEvent we) { 
                if(player != null) { 
                    player.close(); 
                } 
                System.exit(0); 
            } 
        }); 
        f.setSize(500,400); 
 
        f.setVisible(true); 
        URL url = null; 
        try { 
            //׼��һ��Ҫ���ŵ���Ƶ�ļ���URL 
         //   url = new URL("file:/D:/������/javaFM/bin/����SD����ʾͼƬ.mpg"); 
            url = new URL("file:/" + path);
            //url = new URL("file:/F:/������Ƶ/����SD����ʾͼƬ.avi"); 

        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        }        
        try { 
            //ͨ������Manager��createPlayer����������һ��Player�Ķ��� 
            //���������ý�岥�ŵĺ��Ŀ��ƶ��� 
            player = Manager.createPlayer(url); 
        } catch (NoPlayerException e1) { 
            e1.printStackTrace(); 
        } catch (IOException e1) { 
            e1.printStackTrace(); 
        }  
 
        //��player����ע����������ܸ�ż������¼�������ʱ��ִ����صĶ��� 
        player.addControllerListener(this); 
        
        //��player���������ص���Դ���� 
        player.realize(); 
    } 
    
    private int videoWidth = 0; 
    private int videoHeight = 0; 
    private int controlHeight = 30; 
    private int insetWidth = 10; 
    private int insetHeight = 30; 
    
    //����player������¼� 
    public void controllerUpdate(ControllerEvent ce) { 
        if (ce instanceof RealizeCompleteEvent) { 
            //playerʵ������ɺ����player����ǰԤ���� 
            player.prefetch(); 
        } else if (ce instanceof PrefetchCompleteEvent) { 
            if (visual != null) 
                return; 
 
            //ȡ��player�еĲ�����Ƶ����������õ���Ƶ���ڵĴ�С 
            //Ȼ�����Ƶ���ڵ������ӵ�Frame�����У� 
            if ((visual = player.getVisualComponent()) != null) { 
                Dimension size = visual.getPreferredSize(); 
                videoWidth = size.width; 
                videoHeight = size.height; 
                f.add(visual); 
            } else { 
                videoWidth = 320; 
            } 
            
            //ȡ��player�е���Ƶ���ſ�������������Ѹ������ӵ�Frame������ 
            if ((control = player.getControlPanelComponent()) != null) { 
                controlHeight = control.getPreferredSize().height; 
                f.add(control, BorderLayout.SOUTH); 
            } 
            
            //�趨Frame���ڵĴ�С��ʹ��������Ƶ�ļ���Ĭ�ϴ�С 
            f.setSize(videoWidth + insetWidth, videoHeight + controlHeight + insetHeight); 
            f.validate(); 
            
            //������Ƶ���������ʼ���� 
            player.start(); 
            //mediaPlayer.start(); 
        } else if (ce instanceof EndOfMediaEvent) { 
            //��������Ƶ��ɺ󣬰�ʱ��������ָ�����ʼ�����ٴ����¿�ʼ���� 
            player.setMediaTime(new Time(0)); 
            //player.start(); 
        } 
    } 
 
	/*private void secert(File file) 
	{
		InputStream in = null;
		OutputStream os = null;
		DataInputStream dis = null;
		String pa;
	    try 
	    {
	    	// һ�ζ�����ֽ�
	    	int byteread= 32768;
	    	int index = 0;
	    	int j = 0;
	    	int k = 1;
	    	int lengthofkey;
	    	String key1;
	    	int key[];
		    byte[] tempbytes = new byte[byteread];
		    in = new FileInputStream(file);
		    dis = new DataInputStream(in);
		    System.out.println(file.getName().substring(0, file.getName().length() - 4));
		    pa = "E:/" + file.getName().substring(0, file.getName().length() - 4);
		    System.out.println(pa);
	        os = new FileOutputStream(pa);
	         
	        lengthofkey = dis.readUnsignedByte();
	        byte[] key3 = new byte[lengthofkey];
	        in.read(key3);
	        key1 = new String(key3);
			String[] prikey = key1.replaceAll(" +", " ").split(" ");
			if(prikey.length == 0)
			{
				key= new int[prikey.length];
				for(int i=0; i<prikey.length; i++)
				{
					key[i] = Integer.parseInt(prikey[i]);
					System.out.println(key[i]);
				}
			}
			else
				key = new int[] {1,2,3,4,5};
	        //�������ֽڵ��ֽ������У�bytereadΪһ�ζ�����ֽ���
	        while (in.read(tempbytes,0,byteread) != -1) 
	        {
	        	if(index >= key.length)
	        		index = 0;
	        	tempbytes[0] = (byte) (Integer.parseInt(tempbytes[0] + "") ^ key[index]);
	        	os.write(tempbytes);
	        	index++;
	        	j++;
	        }
	        in.close();
	        os.close();
	        play(pa);
	    }catch (Exception e1) 
	    {
	    	e1.printStackTrace();
	    } finally
	    {
	    	if (in != null) 
	    	{
	    		try 
	    		{
	    			in.close();
	            }
	    		catch (IOException e1) 
	    		{
	            }
	    	}
	    }
	}*/
    
} 