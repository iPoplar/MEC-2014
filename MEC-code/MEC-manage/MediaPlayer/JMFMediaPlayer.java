
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.media.ControllerClosedEvent;
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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


@SuppressWarnings({ "restriction", "serial" })
public class JMFMediaPlayer extends JFrame implements ActionListener,
        ControllerListener, ItemListener  
{
    // JMF�Ĳ�����
    Player player;
    // ����������Ƶ����Ϳ������
    Component vedioComponent;
    Component controlComponent;
    // ��ʾ�Ƿ��ǵ�һ�δ򿪲�����
    boolean first = true;
    // ��ʾ�Ƿ���Ҫѭ��
    boolean loop = false;
    // �ļ���ǰĿ¼
    String currentDirectory;
    private File file;
 
    // ���췽��
    public JMFMediaPlayer(String title) 
    {
        super(title);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            
            {
                // �û��������ϵͳ�˵��Ĺرհ�ť
                // ����dispose��ִ��windowClosed
                dispose();
            }
 
            public void windowClosed(WindowEvent e)
            {
                if (player != null) 
                {
                    // �ر�JMF����������
                    player.close();
                }
                System.exit(0);
            }
        }); 
        // �����������Ĳ˵�
        JMenu fileMenu = new JMenu("�ļ�");
        JMenuItem openMemuItem = new JMenuItem("��");
        openMemuItem.addActionListener(this);
        fileMenu.add(openMemuItem);
        // ���һ���ָ���
        fileMenu.addSeparator();
        // ����һ����ѡ��˵���
        JCheckBoxMenuItem loopMenuItem = new JCheckBoxMenuItem("ѭ��", false);
        loopMenuItem.addItemListener(this);
        fileMenu.add(loopMenuItem);
        fileMenu.addSeparator();
        JMenuItem exitMemuItem = new JMenuItem("�˳�");
        exitMemuItem.addActionListener(this);
        fileMenu.add(exitMemuItem);
 
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        this.setSize(200, 200);
 
        try 
        {
            // ���ý������ۣ�Ϊϵͳ���
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setVisible(true);
    }
 
    /**
     * ʵ����ActionListener�ӿڣ���������Ļ�¼�
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("�˳�")) {
            // ����dispose�Ա�ִ��windowClosed
            dispose();
            return;
        }
        if (player != null) 
        {
            // �ر��Ѿ�����JMF����������
            player.close();
        }
        try 
        {
            // ����һ����ѡ���ļ��Ĳ�����
            //player = Manager.createPlayer(new MediaLocator(�˴��ĳ�Ӧ�����ļ�·��));
        } catch (java.io.IOException e2) {
            System.out.println(e2);
            return;
        } catch (NoPlayerException e2) {
            e2.printStackTrace();
            System.out.println("�����ҵ�������.");
            return;
        }
        if (player == null) {
            System.out.println("�޷�����������.");
            return;
        }
        first = false;
        // �������Ŀ����¼�����
        player.addControllerListener(this);
        // Ԥ���ļ�����
        player.prefetch();
    }
 
    /**
     * ʵ��ControllerListener�ӿڵķ��������������Ŀ����¼�
     */
    public void controllerUpdate(ControllerEvent e) {
        // ����player.close()ʱControllerClosedEvent�¼����֡�
        // ��������Ӿ���������ò���Ӧ�ò����Ϊһ�������
        // ���ǶԿ�����岿��Ҳִ��ͬ���Ĳ�����
        if (e instanceof ControllerClosedEvent) {
            if (vedioComponent != null) {
                this.getContentPane().remove(vedioComponent);
                this.vedioComponent = null;
            }
            if (controlComponent != null) {
                this.getContentPane().remove(controlComponent);
                this.controlComponent = null;
            }
            return;
        }
        // �����ý���ļ�����β���¼�
        if (e instanceof EndOfMediaEvent) {
            if (loop) 
            {
                // �������ѭ���������¿�ʼ����
                player.setMediaTime(new Time(0));
                player.start();
            }
            return;
        }
        // ����ǲ�����Ԥ���¼�
        if (e instanceof PrefetchCompleteEvent) {
            // ����������
            player.start();
            return;
        }
        // ������ļ�����ȫ�¼�������ʾ��Ƶ����Ϳ��������
        if (e instanceof RealizeCompleteEvent) {
            vedioComponent = player.getVisualComponent();
            if (vedioComponent != null) {
                this.getContentPane().add(vedioComponent);
            }
            controlComponent = player.getControlPanelComponent();
            if (controlComponent != null) {
                this.getContentPane().add(controlComponent, BorderLayout.SOUTH);
            }
            this.pack();
        }
    }
    
    public void getffilepath()
    {
    	//TODO �ӷ����������ļ�·��
    }
 
	private void secert(File file) 
	{

		InputStream in = null;
		OutputStream os = null;
		DataInputStream dis = null;
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
	        os = new FileOutputStream("e:\\" + file.getName().substring(0, file.getName().length() - 4));
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
	}
    // ����ѭ������ѡ��˵���ĵ���¼�
    public void itemStateChanged(ItemEvent e) 
    {
        loop = !loop; 
    }
 
    public static void main(String[] args) 
    {
        new JMFMediaPlayer("JMFý�岥����");
    }
}
