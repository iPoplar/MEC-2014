import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MecClient extends Thread
{
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	protected String str = "";
	protected boolean stop = true;
	
	public MecClient() throws Exception
	{
		try 
		{
			socket = new Socket("192.168.1.5", 54199);
			in= new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) 
		{
			throw new Exception("Class:MecClient,Method:MecClient(),杩炴帴鏈嶅姟鍣ㄥけ璐ワ紒");
		} 
	}
	
	public void sendMess(String SQLString) throws IOException
	{
		this.start();
		
		try 
		{
			byte[] buff = SQLString.getBytes("UTF-8");
			for(int i = 0; i < buff.length; i++)
				System.out.print(buff[i]);
			out.write(buff, 0, buff.length);
		} catch (IOException e) 
		{
			socket.close();
			throw new IOException("Class:MecClient,Method:sendMess(),鍙戦�淇℃伅澶辫触锛�");
		}
	}
	
	public void run()
	{
		
		while(stop)
		{
			try 
			{
				System.out.printf("寮�鎺ユ敹鐨勬暟鎹甛n");
				byte[] buff = new byte[256];
				int len1 = in.read(buff);
				
				if(buff[len1-1] == '\r')
				{
					stop = false;
				}
				str += new String(buff, 0, len1);
				System.out.printf("鎺ユ敹鐨勬暟鎹�str=[%s]\n",str);
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(new JFrame("娓╅Θ鎻愮ず"),"Class:MecClient,Method:run(),鎺ュ彈鏁版嵁寮傚父锛�");
			}
		}
	}

	
}
