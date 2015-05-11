import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class downloadFile
{
	private static int BUFFER_SIZE = 32* 1024;
	protected static byte[] filePartMess;
	
	public downloadFile(SQLServer.waiter waiters, File file) throws IOException
	{
		try {
			sendFileMess(waiters, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(waiters.socket + "�ͻ����쳣�Ͽ���");
			throw e;
		}
	}
	
	private synchronized void sendFileMess(SQLServer.waiter waiter, File file) 
		throws FileNotFoundException, IOException
		{
		try 
		{
			waiter.out.writeUTF(file.getName());
			System.out.println(file.getName());
			waiter.out.flush();
			waiter.out.writeLong(file.length());
			waiter.out.flush();
			waiter.lastPackNum = file.length()/BUFFER_SIZE;
			waiter.lastLength = file.length()%BUFFER_SIZE;
			MappedByteBuffer inputBuffer;
			
			inputBuffer = new RandomAccessFile(file, "r")
				.getChannel().map(FileChannel.MapMode.READ_ONLY,
			                  0, file.length());
			
			byte[] dst = new byte[BUFFER_SIZE + 32];
			Queue<String> queueList = new LinkedList<String>();
			
			for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE)
			{
				if (inputBuffer.capacity() - offset >= BUFFER_SIZE) 
				{
					for (int i = 32; i < BUFFER_SIZE + 32; i++)
						dst[i] = inputBuffer.get(offset + i-32);//����
					for(int i = 0;  i < 16; i++)
						dst[i] = dst[i+32];//���ݵ�ǰ16���ֽ�
					
					for(int i = 0; i < 5 && i < String.valueOf(BUFFER_SIZE).length(); i++)//���͵ĳ���
						dst[i+16] = Byte.parseByte(String.valueOf(BUFFER_SIZE).substring(i, 1+i));
					for(int i = 0; i < 6 && i < String.valueOf(offset/BUFFER_SIZE).length(); i++)//���͵İ����
						dst[i+21] = Byte.parseByte(String.valueOf(offset/BUFFER_SIZE).substring(i, i+1));
					waiter.out.write(dst);
					waiter.out.flush();
					String mess = waiter.in.readUTF();
					if(mess.split("\t")[1].equalsIgnoreCase("false"))
						//TODO ������ʧ�ܵı��
					{
						queueList.add(mess.split("\t")[2]);
					}
				} 
				else if (inputBuffer.capacity() - offset < BUFFER_SIZE)
				{
					for (int i = 32; i < inputBuffer.capacity() - offset + 32; i++)
						dst[i] = inputBuffer.get(offset + i-32);
					for(int i = 0;  i < 16 && i < inputBuffer.capacity()-offset; i++)
						dst[i] = dst[i+32];//���ݵ�ǰ16���ֽ�
					for(int i = 0; i < 5 && i < String.valueOf(waiter.lastLength).length(); i++)//���͵ĳ���
						dst[i+16] = Byte.parseByte(String.valueOf(waiter.lastLength).substring(i, 1+i));
					for(int i = 0; i < 6 && i < String.valueOf(waiter.lastPackNum).length(); i++)//���͵İ����
						dst[i+21] = Byte.parseByte(String.valueOf(waiter.lastPackNum).substring(i, i+1));
					//�м����5����λ
					waiter.out.write(dst);
					waiter.out.flush();
					String mess = waiter.in.readUTF();
					if(mess.split("\t")[1].equalsIgnoreCase("false"))
					{
						queueList.add(mess.split("\t")[2]);
					}
				}
			}
			System.out.println("��������");
			System.out.println("��ǰ���߳�����" + Thread.activeCount());
			//������߳�������̬������ʱ��
			//�������⣺���ܵ��·���������������cut��һ���ֿͻ���
			dealErrorMess sender = new dealErrorMess(queueList, file);
			sender.sendMess(waiter);
		} catch (FileNotFoundException e) 
		{
			System.out.println("Class:SQLServer,Method:sendFileMess(),�ļ�δ�ҵ���");
			throw e;
		} catch (IOException e) 
		{
			throw e;
		}
	}
}
