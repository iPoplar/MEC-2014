import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import com.mec.dao.VideoInformation;
import com.mec.dao.VideoKeyWords;
import com.mec.dao.VideoPath;
import com.mec.model.VideoAllInformation;
import com.mec.model.VideoAllPath;

public class SQLServer
{
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private waiter[] waiters;
	
	private int MAX_COUNT = 500;
	private int NO_COUNT = -1;
	private final int PORT = 54199;
	public static final int BUFFER_SIZE = 32*1024;
	
	public SQLServer()
	{
		waiters = new waiter[MAX_COUNT];
		try 
		{
			serverSocket = new ServerSocket(PORT);
			VideoPath.connection();
			VideoInformation.connection();
			VideoKeyWords.connection();
		} catch (IOException e) 
		{
			System.out.println("Class:SQLServer, Method:SQLServer(), �˿��쳣��");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int findWaiters()
	{
		int i;
		
		for(i = 0; waiters[i] != null && i < MAX_COUNT; i++)
			;
		if(i >= MAX_COUNT)
			i = NO_COUNT;
		
		return i;
	}
	
	public void listener()
	{
		while(true)
		{
			System.out.println("�ͻ��������С���");
			try 
			{
				clientSocket = serverSocket.accept();
				System.out.println(clientSocket.getInetAddress() + "�����ˡ���");
				dealErrorMess.threadCount += 1;

				int i = findWaiters();
				
				if(i != NO_COUNT)
				{
					waiters[i] = new waiter(clientSocket, i);
					waiters[i].start();
				}
				else
				{
					return;
				}
			} catch (IOException e) 
			{
				System.out.println("Class:SQLServer,Method:listener(),�ͻ�������ʧ�ܣ�");
			}
		}
	}
	
	public void dealReMess(String pack, String packNum, waiter sleepWaiter)throws IOException
	{
		if(pack.equalsIgnoreCase("false"))
		{
			try 
			{
				sleepWaiter.out.write(downloadFile.filePartMess);
			} catch (IOException e) 
			{
				System.out.println("Class:SQLServer, Method:dealMess(),");
			}
		}
	}
	
	public class waiter extends Thread
	{
		protected Socket socket;
		protected DataInputStream in;
		protected DataOutputStream out;
		protected int index;
		protected String fileName;
		protected boolean ok = true;
		protected long lastPackNum = 0;
		protected long lastLength = 0;
	
		
		public waiter()
		{
		}
		
		public waiter(Socket socket, int index)
		{
			this.socket = socket;
			this.index = index;
			
			try 
			{
				in = new DataInputStream(this.socket.getInputStream());
				out = new DataOutputStream(this.socket.getOutputStream());
			} catch (IOException e) 
			{
				System.out.println("Class:SQLServer,Method:waiter(), ͨ�������쳣��");
			}
		}
		
		public void run()
		{
			while(ok)
			{
				try 
				{
					String mess = this.in.readUTF();
					dealMess(mess);
				} catch (IOException e) 
				{
					ok = false;
					try 
					{
						waiters[index].socket.close();
						waiters[index] = null;
						System.out.println("�ȴ��¿ͻ��˽��롭��");
					} catch (IOException e1) 
					{
						System.out.println(waiters[index].socket + "�쳣�Ͽ���");
					}
				}
			}
		}
		
		public void dealMess(String mess) throws IOException{
			String[] spMess = mess.split("\t");
			try {
				if (spMess[0].equalsIgnoreCase("File")) 
				{
					File f = new File(spMess[1]);
					new downloadFile(this, f);
				} else if (spMess[0].equalsIgnoreCase("Save")) 
				{
					//���ݿ�Ŀ���ѡ����ʵĴ洢·��
					String pth = savaFileMess(spMess[2]);
					//�ֱ�����Ƶ���ڡ���Ŀ��ź͹ؼ���
					uploadFile upFile = new uploadFile();
					String fileName = upFile.receiveFile(this, pth);
					insertData(spMess[2], spMess[1], spMess[3], pth, fileName);
				} 
				else if (spMess[0].equalsIgnoreCase("Exit"))
				{
					waiters[index].socket.close();
					waiters[index] = null;
//					VideoInformation.breakConnection();
//					VideoKeyWords.breakConnection();
				} else 
				{
					System.out.println("����Ҫ����ģ�");
				}
			} catch (IOException e){
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void insertData(String fileNum, String date, String keyWords, String path, String fileName)
		{
			//TODO ��ŵ����ɣ�·����Ҫ�ָ��������·�����ļ����Ʒָ���ļ�����
			//�����������ɵı��ȥ����ؼ��֣������Ƕ�����¼
			try 
			{
				String fileId = fileNum + date.substring(2);
				System.out.println(fileId + "***" + fileId.length());
				String num = "0";
				ArrayList<VideoAllInformation> resultList = new ArrayList<VideoAllInformation>();
				resultList = VideoInformation.FindSameIdVideoInforation(fileId);
				for(int i = 0; i < resultList.size(); i++)
				{
					num =  resultList.get(i).getVideoid().substring(9);
				}
				if(!num.equalsIgnoreCase("0"))
					num = String.valueOf(Integer.parseInt(num)+1);
				fileId += num;
				System.out.println(fileId +"**"+ fileName + "***" + path);
				VideoInformation.addData(fileId, fileName, path);
				String[] temp = keyWords.split("\t");
				for(int i = 0; i < temp.length; i++)
					VideoKeyWords.addKeyword(fileId, temp[i]);
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		private String savaFileMess(String fileNum)
		{
			String path = null;
			try 
			{
				ArrayList<VideoAllPath> resultList = new ArrayList<VideoAllPath>();
				resultList = VideoPath.FindPath(fileNum);
				path = resultList.get(0).getVideopath();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
			return path;
		}

		public void breakConnection()
		{
			try 
			{
				this.socket.close();
			} catch (IOException e) 
			{
				System.out.println("Class:SQLServer,method:breakConnection(), �رն˿��쳣��");
			}
		}
	}
	
	public static void main(String[] args)
	{
		SQLServer s = new SQLServer();
		s.listener();
	}
	
}
