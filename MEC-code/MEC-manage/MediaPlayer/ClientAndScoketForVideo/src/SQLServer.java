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
			System.out.println("Class:SQLServer, Method:SQLServer(), 端口异常！");
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
			System.out.println("客户端侦听中……");
			try 
			{
				clientSocket = serverSocket.accept();
				System.out.println(clientSocket.getInetAddress() + "接入了……");
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
				System.out.println("Class:SQLServer,Method:listener(),客户端连接失败！");
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
				System.out.println("Class:SQLServer,Method:waiter(), 通道建立异常！");
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
						System.out.println("等待新客户端接入……");
					} catch (IOException e1) 
					{
						System.out.println(waiters[index].socket + "异常断开！");
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
					//根据科目编号选择合适的存储路径
					String pth = savaFileMess(spMess[2]);
					//分别处理视频日期、科目编号和关键字
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
					System.out.println("这里要处理的！");
				}
			} catch (IOException e){
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void insertData(String fileNum, String date, String keyWords, String path, String fileName)
		{
			//TODO 编号的生成，路径需要分割出来（把路径和文件名称分割）、文件名称
			//根据上面生成的编号去插入关键字，可能是多条记录
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
				System.out.println("Class:SQLServer,method:breakConnection(), 关闭端口异常！");
			}
		}
	}
	
	public static void main(String[] args)
	{
		SQLServer s = new SQLServer();
		s.listener();
	}
	
}
