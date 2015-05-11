import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.mec.dao.VideoInformation;
import com.mec.dao.VideoKeyWords;
import com.mec.dao.VideoPath;
import com.mec.model.VideoAllInformation;

public class ClientTest 
{
    private static ClientSocket cs;
    private static String ip = "localhost";// ���óɷ�����IP
    private static int port = 54199;
    private static int bufferSize = 32*1024;

    public ClientTest() throws Exception
    {
        try 
        {
            createConnection();
            VideoInformation.connection();
            VideoPath.connection();
            VideoKeyWords.connection();
        } catch (Exception ex) 
        {
           ex.printStackTrace();
           throw ex;
        }
    }

    private boolean createConnection() throws Exception//���ӷ�����
    {
        cs = new ClientSocket(ip, port);
        try 
        {
            cs.CreateConnection();
            System.out.print("���ӷ������ɹ�!" + "\n");
            return true;
        } catch (Exception e) 
        {
            System.out.print("���ӷ�����ʧ��!" + "\n");
            return false;
        }

    }
    
    public void sendUpMess(String filePath, String fileDate, String subjectNum, String keyWords) throws IOException
    {
    	try
    	{
    		cs.out.writeUTF("Save\t" + fileDate + "\t" + subjectNum + "\t" + keyWords);
    		uploadFile(filePath);
    	}catch(IOException e)
    	{
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    private void uploadFile(String filePath) throws IOException//�ϴ��ļ�
    {
    	File file = new File(filePath);
    	long lastLength;
    	long lastPackNum;
    	try 
    	{
			cs.out.writeUTF(file.getName());
			cs.out.flush();
			cs.out.writeLong(file.length());
			cs.out.flush();
			
			MappedByteBuffer inputBuffer = new RandomAccessFile(file, "r")
        		.getChannel().map(FileChannel.MapMode.READ_ONLY,
                          0, file.length());
			byte[] dst = new byte[bufferSize + 32];
			lastLength = file.length()%bufferSize;
			lastPackNum = file.length()/bufferSize;
			Queue<String> queueList = new LinkedList<String>();
			
			for(int offset = 0; offset < inputBuffer.capacity(); offset += bufferSize)
			{
				if (inputBuffer.capacity() - offset >= bufferSize) 
				{
					for (int i = 32; i < bufferSize + 32; i++)
						dst[i] = inputBuffer.get(offset + i-32);//����
					for(int i = 0;  i < 16; i++)
						dst[i] = dst[i+32];//���ݵ�ǰ16���ֽ�
					
					for(int i = 0; i < 5 ; i++)//���͵ĳ���
						dst[i+16] = Byte.parseByte(String.valueOf(bufferSize).substring(i, 1+i));
					for(int i = 0; i < 6 && i < String.valueOf(offset/bufferSize).length(); i++)//���͵İ����
						dst[i+21] = Byte.parseByte(String.valueOf(offset/bufferSize).substring(i, i+1));
					cs.out.write(dst);
					cs.out.flush();
					String mess = cs.in.readUTF();
					if(mess.split("\t")[1].equalsIgnoreCase("false"))
					{
						queueList.add(mess.split("\t")[2]);
					}
				}
				else 
				{
					for (int i = 32; i < inputBuffer.capacity() - offset + 32; i++)
						dst[i] = inputBuffer.get(offset + i-32);
					for(int i = 0;  i < 16 && i < inputBuffer.capacity()-offset; i++)
						dst[i] = dst[i+32];//���ݵ�ǰ16���ֽ�
					for(int i = 0; i < 5 && i < String.valueOf(lastLength).length(); i++)//���͵ĳ���
						dst[i+16] = Byte.parseByte(String.valueOf(lastLength).substring(i, 1+i));
					for(int i = 0; i < 6 && i < String.valueOf(lastPackNum).length(); i++)//���͵İ����
						dst[i+21] = Byte.parseByte(String.valueOf(lastPackNum).substring(i, i+1));
					//�м����5����λ
					cs.out.write(dst);
					cs.out.flush();
					String mess = cs.in.readUTF(); 
					if(mess.split("\t")[1].equalsIgnoreCase("false"))
					{
						queueList.add(mess.split("\t")[2]);
					}
				}
			}
			dealErrorMess sender = new dealErrorMess(queueList, file);
			sender.sendMess(cs);
			JOptionPane.showMessageDialog(new JFrame("��ܰ��ʾ"), "�ϴ��ɹ���");
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(new JFrame("��ܰ��ʾ"), "�����������⣬�ϴ�ʧ�ܣ����Ժ����ԣ�");
			System.exit(0);
			throw e;
		}
    }
    
    public void sendMessage(String fileName) throws SQLException, IOException//����������Ƶ�ļ����� 
    {
    	try 
    	{
    		ArrayList<VideoAllInformation> resultList = new ArrayList<VideoAllInformation>(); 
    		resultList = VideoInformation.FindPathVideoInformationAN(fileName);
    		cs.out.writeUTF("File\t" + resultList.get(0).getVideopath() + fileName);
    		getMessage();
    	} catch (IOException e) 
    	{
    		System.out.print("������Ϣʧ��!" + "\n");
    		throw e;
    	}
    }

    private void getMessage() throws IOException //������Ƶ������������ָ��·����
    {
        if(cs == null)
            return;
        DataInputStream inputStream = null;
        
        try 
        {
        	System.out.println("��ʼ��������");
            inputStream = cs.getMessageStream();
        } catch (Exception e) 
        {
            System.out.print("������Ϣ�������\n");
            return;
        }


        //���ر���·�����ļ������Զ��ӷ������˼̳ж�����
        String savePath = "F:\\";//�������ļ����غ󱣴��·��
        byte[] buf = new byte[bufferSize + 32];
        long len=0;
        int count = 0;
       
        try 
        {    
        	savePath += inputStream.readUTF();
        	System.out.println(savePath);
        	len = inputStream.readLong();
        	
        	RandomAccessFile fileOut = new RandomAccessFile(savePath, "rw");
        	fileOut.setLength(len);
            System.out.println("�ļ��ĳ���Ϊ:" + len + "\n");
            System.out.println("��ʼ�����ļ�!" + "\n");
            
            while (count++ < (len / bufferSize)) 
            {
            	Thread.sleep((long)(Math.random()*(dealErrorMess.threadCount*20 + 30))+50);
                int read = 0;
                if (inputStream != null)
                {
                	read = inputStream.read(buf);
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
            	for(i = 0; i < 16 && buf[i] == buf[i+32]; i++)
            	;
            	if(i < 16 || (read < length))
            	{
            		byte[] mess = null;
            		cs.out.writeUTF("Wake\tfalse\t" + num);
            		cs.in.read(mess, 0, length-num);
            	}
            	else
            	{
            		cs.out.writeUTF("Wake\ttrue\t" + num);
            		fileOut.seek(num*bufferSize);
            		fileOut.write(buf, 32, read-32);
            	}
            }
            if(len%bufferSize != 0)
            {
            	int read = 0;
            	read = inputStream.read(buf);
            	
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
            		cs.out.writeUTF("Wake\tfalse\t" + num);
            		cs.in.read(mess, 0, (int) (length-num));
            	}
            	else
            	{
            		cs.out.writeUTF("Wake\ttrue\t" + num);
            		fileOut.seek(num*bufferSize);
            		fileOut.write(buf, 32, (int) (len%bufferSize));
            	}
            }
            System.out.println("������ɣ��ļ���Ϊ" + savePath + "\n");
            cs.out.writeUTF("Exit\tbye");
            fileOut.close();
        } catch (Exception e) 
        {
            JOptionPane.showMessageDialog(new JFrame("��ܰ��ʾ"), "�����������⣬����ʧ�ܣ����Ժ����ԣ�");
            cs.in.close();
			cs.out.close();
			System.exit(0);
            return;
        }
    }

    public static void main(String arg[])
    {
    	int num = 0;
    	for(int i = 0; i < 5; i++)
    	{
    		try {
    			ClientTest df = new ClientTest();
    			df.sendMessage("2014-04-12 �ٽ���Դ.avi");
//    			df.sendUpMess("F:/���/src/2014-04-12 �ٽ���Դ.avi", "20140412", "002", "�ٽ�\t��Դ");
    		} catch (Exception e) {
    			e.printStackTrace();
    			return;
    		}
    	}
    	System.out.println("����ʧ����"+num+"��");
    }
   
}
