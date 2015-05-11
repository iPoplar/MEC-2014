import java.awt.Color;
import java.awt.Dimension;  
import java.awt.Image;
import java.awt.Toolkit;
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
  
public class TimeDialog 
{  
	private static String Message = null;
	
    private static int Secends = 0;
    
    private static JLabel label_1 = new JLabel();
    private static JLabel label_2 = new JLabel();
   // private static JLabel label_3 = new JLabel();

 
    private static JDialog Dialog = null;
    
    public static void showDialog(JFrame father, String TitleMessage, int sec) 
    {  
        Message = TitleMessage;
 
        Secends = sec;
        
        label_1.setBounds(60,20,250,20);
        //label_1.setBorder(BorderFactory.createLineBorder(Color.red));


        label_2.setBounds(100, 40, 200, 20);
        //label_2.setBorder(BorderFactory.createLineBorder(Color.green));
        Toolkit tk = Toolkit.getDefaultToolkit();  
        Image image = tk.createImage("/resource/xxx.jpg");
        String src  = "/img/www2.jpg";
        ImageIcon image = new ImageIcon(Dialog.getClass().getResource(src));
        JLabel label_3 = new JLabel(image);
        label_3.setBounds(5, 5, 50, 50);
        label_3.setBorder(BorderFactory.createLineBorder(Color.yellow));
       
        
        
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        
        Dialog = new JDialog(father, true);
        Dialog.setTitle(TitleMessage);
        Dialog.setLayout(null);
        Dialog.add(label_1);  
        Dialog.add(label_2);
        Dialog.add(label_3);

        s.scheduleAtFixedRate
        (
        	new Runnable() 
        	{  
	            public void run() 
	            {  
	                TimeDialog.Secends--;
	                
	                if(TimeDialog.Secends == 0)
	                {  
	                    TimeDialog.Dialog.dispose();  
	                }
	                else 
	                { 
	                    label_1.setText("提示：已检测到您在另一台电脑上登陆!!!");

	                    label_2.setText("本窗口将在"+Secends+"秒后自动关闭");
	                }  
	            }  
        	},
        1, 1, TimeUnit.SECONDS); 
        
        Dialog.pack();  
        Dialog.setSize(new Dimension(350,130));  
        Dialog.setLocationRelativeTo(father);  
        Dialog.setVisible(true);  
    }  
}  
