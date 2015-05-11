import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientSocket extends Thread
{
    private String ip;

    private int port;

    private Socket socket = null;

    protected DataOutputStream out = null;
    protected DataInputStream in = null;

    DataInputStream getMessageStream = null;

    public ClientSocket(String ip, int port) 
    {
        this.ip = ip;
        this.port = port;
    }

    public void CreateConnection() throws Exception
    {
        try 
        {
            socket = new Socket(ip, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (Exception e) 
        {
            e.printStackTrace();
            if (socket != null)
                socket.close();
            throw e;
        } finally 
        {
        }
    }
    
    public DataInputStream getMessageStream() throws Exception 
    {
        try 
        {
            getMessageStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            return getMessageStream;
        } catch (Exception e) 
        {
            e.printStackTrace();
            if (getMessageStream != null)
            	getMessageStream.close();
            throw e;
        } finally 
        {
        }
    }

    public void shutDownConnection() 
    {
        try 
        {
            if(out != null)
                out.close();
            if(getMessageStream != null)
                getMessageStream.close();
            if(socket != null)
                socket.close();
        } catch (Exception e) 
        {
        	System.out.println("Class:ClientSocket,Method:shutDownConnection(),πÿ±’“Ï≥£");
        	System.gc();
        }
    }
}
