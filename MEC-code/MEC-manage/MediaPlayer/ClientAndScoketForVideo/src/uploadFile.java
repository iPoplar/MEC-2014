import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class uploadFile 
{
	public uploadFile()
	{
	}
	
	public String receiveFile(SQLServer.waiter waiter, String savePath)throws Exception
	{
		String name = null;
		try 
		{
			waiter.in = new DataInputStream(new BufferedInputStream(waiter.socket.getInputStream()));
            //本地保存路径，文件名会自动从服务器端继承而来。
            int bufferSize = 32*1024;
            byte[] buf = new byte[bufferSize + 32];
            long len=0;
            int count = 0;
            boolean ok = true;
           
            name = waiter.in.readUTF();//需要先发送文件名称
            savePath += name;
//            DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(savePath))));
            len = waiter.in.readLong();//需要发送文件长度
            RandomAccessFile fileOut = new RandomAccessFile(savePath, "rw");
            System.out.println("GJJJJJJJJJJJJ" + len);
            fileOut.setLength(len);
           
            System.out.println("文件的长度为:" + len + "\n");
            System.out.println("开始接收文件!" + "\n");
                    
            while (count++ < len / bufferSize) 
            {
            	SQLServer.waiter.sleep((long)(Math.random()*(dealErrorMess.threadCount*20 + 30))+50);
                int read = 0;
                if (waiter.in != null)
                {
                	read = waiter.in.read(buf);
                	System.out.println("接受的长度" + read);
                }
                int i, j;
            	int length = 0;                                                       
            	int num = 0;
            	String temp = "";
            	
            	for(j =0, temp =""; j< 5 && j < String.valueOf(read-32).length(); j++)
            	{
            		temp += buf[j+16];
            	}
            	length = Integer.parseInt(temp);
            	for(j = 0, temp = ""; j < 6 && j < String.valueOf(count-1).length(); j++)
            	{
            		temp += buf[j+21];
            	}
            	num = Integer.parseInt(String.valueOf(temp));
            	System.out.println("&&&&&&&&&&&&&&&&&&&&" + num);
            	for(i = 0; i < 16 && buf[i] == buf[i+32]; i++)
            	;
            	if(i < 16 || (read < length))
            	{
            		byte[] mess = null;
            		waiter.out.writeUTF("Wake\tfalse\t" + num);
            		waiter.in.read(mess, 0, length-read);
            	}
            	else
            	{
            		System.out.println("^^^^^^^^^^^^^");
            		waiter.out.writeUTF("Wake\ttrue\t" + num);
            		fileOut.seek(num*bufferSize);
            		fileOut.write(buf, 32, read-32);
            	}
            }
            if(len%bufferSize != 0)
            {
            	int read = 0;
            	read = waiter.in.read(buf);
            	
            	int i, j;
            	long length = 0;
            	int num = 0;
            	String temp = "";
            	for(j =0, temp =""; j< 5 && j < String.valueOf(len%bufferSize).length(); j++)
            	{
            		temp += buf[j+16];
            	}
            	length = Integer.parseInt(temp);
            	for(j = 0, temp =""; j < 6 && j < String.valueOf(count-1).length(); j++)
            	{
            		temp += buf[j+21];
            	}
            	num = Integer.parseInt(temp);
            	for(i = 0; i < 16 && buf[i] == buf[i+32]; i++)
            	;
            	if(i < 16 || (read < length))
            	{
            		byte[] mess = null;
            		waiter.out.writeUTF("Wake\tfalse\t" + num);
            		waiter.in.read(mess, 0, (int) (length-read));
            	}
            	else
            	{
            		waiter.out.writeUTF("Wake\ttrue\t" + num);
            		fileOut.seek(num*bufferSize);
            		fileOut.write(buf, 32, (int) (len%bufferSize));
            	}
            }
            System.out.println("接收完成，文件存为" + savePath + "\n");
		} catch (IOException e)
		{
			if (waiter.in != null)
				waiter.in.close();
			if(waiter.socket != null)
				waiter.socket.close();
            throw e;
		}
		return name;
	}
}
