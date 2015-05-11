import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

public class dealErrorMess 
{
	private Queue<String> queueList = new LinkedList<String>();
	private File file;
	private final int BUFFER_SIZE = 32 * 1024;
	protected static int threadCount = 1;
	
	public dealErrorMess(Queue<String> queueList, File file)
	{
		this.queueList = queueList;
		this.file = file;
	}
	
	public void sendMess(SQLServer.waiter waiter) throws FileNotFoundException
	{
		RandomAccessFile fileWriter = new RandomAccessFile(file, "r");
		String num = null;
		byte[] mess = new byte[BUFFER_SIZE + 32];
		
		try 
		{
			if(!queueList.isEmpty())
			{
				while((num = queueList.poll())!= null)
				{
					System.out.println("jsdkdshjgsdfghsks" + num);
					if(Integer.parseInt(num) != waiter.lastPackNum)
					{
						fileWriter.seek(Integer.parseInt(num) * BUFFER_SIZE);
						fileWriter.read(mess, 32, BUFFER_SIZE);
						for(int i = 0;  i < 16; i++)
							mess[i] = mess[i+32];//数据的前16个字节
						
						for(int i = 0; i < 5; i++)//发送的长度
							mess[i+16] = Byte.parseByte(String.valueOf(BUFFER_SIZE).substring(i, 1+i));
						for(int i =0; i < 5; i++)
						{
							System.out.println("*********************" + mess[i+16] + "*********" + (BUFFER_SIZE));
						}
						for(int i = 0; i < 6 && i < num.length(); i++)//发送的包编号
							mess[i+21] = Byte.parseByte(num.substring(i, i+1));
						for(int i =0; i < 6; i++)
						{
							System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%" + mess[i+21] + "%%%%%%%%%%%" + num);
						}
					}
					else
					{
						fileWriter.seek(Integer.parseInt(num) * BUFFER_SIZE);
						int length = (int) (file.length()-Integer.parseInt(num)*BUFFER_SIZE);
						fileWriter.read(mess, 32, length);
						for(int i = 0; i < 16 && i < length; i++)
						{
							mess[i] = mess[i+32];
						}
						for(int i = 0; i < 5 && i < String.valueOf(length).length(); i++)
						{
							mess[i+16] = Byte.parseByte(String.valueOf(length).substring(i, i+1));
						}
						for(int i = 0; i < 6 && i < num.length(); i++)
						{
							mess[i+21] = Byte.parseByte(num.substring(i, i+1));
						}
					}
					waiter.out.write(mess);
					waiter.out.flush();
					String str = waiter.in.readUTF();
					
					if(str.split("\t")[1].equalsIgnoreCase("false"))
						queueList.add(num);
				}
			}
		} catch (NumberFormatException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void sendMess(ClientSocket waiter) throws FileNotFoundException
	{
		RandomAccessFile fileWriter = new RandomAccessFile(file, "r");
		String num = null;
		byte[] mess = new byte[BUFFER_SIZE + 32];
		
		try 
		{
			if(!queueList.isEmpty())
			{
				while((num = queueList.poll())!= null)
				{
					System.out.println("jsdkdshjgsdfghsks" + num);
					if(Integer.parseInt(num) != file.length()/BUFFER_SIZE)
					{
						fileWriter.seek(Integer.parseInt(num) * BUFFER_SIZE);
						fileWriter.read(mess, 32, BUFFER_SIZE);
						for(int i = 0;  i < 16; i++)
							mess[i] = mess[i+32];//数据的前16个字节
						
						for(int i = 0; i < 5; i++)//发送的长度
							mess[i+16] = Byte.parseByte(String.valueOf(BUFFER_SIZE).substring(i, 1+i));
						for(int i =0; i < 5; i++)
						{
							System.out.println("*********************" + mess[i+16] + "*********" + (BUFFER_SIZE));
						}
						for(int i = 0; i < 6 && i < num.length(); i++)//发送的包编号
							mess[i+21] = Byte.parseByte(num.substring(i, i+1));
						for(int i =0; i < 6; i++)
						{
							System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%" + mess[i+21] + "%%%%%%%%%%%" + num);
						}
					}
					else
					{
						fileWriter.seek(Integer.parseInt(num) * BUFFER_SIZE);
						int length = (int) (file.length()-Integer.parseInt(num)*BUFFER_SIZE);
						fileWriter.read(mess, 32, length);
						for(int i = 0; i < 16 && i < length; i++)
						{
							mess[i] = mess[i+32];
						}
						for(int i = 0; i < 5 && i < String.valueOf(length).length(); i++)
						{
							mess[i+16] = Byte.parseByte(String.valueOf(length).substring(i, i+1));
						}
						for(int i = 0; i < 6 && i < num.length(); i++)
						{
							mess[i+21] = Byte.parseByte(num.substring(i, i+1));
						}
					}
					waiter.out.write(mess);
					waiter.out.flush();
					String str = waiter.in.readUTF();
					
					if(str.split("\t")[1].equalsIgnoreCase("false"))
						queueList.add(num);
				}
			}
		} catch (NumberFormatException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
