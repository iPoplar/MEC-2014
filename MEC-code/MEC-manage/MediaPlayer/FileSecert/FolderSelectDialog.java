import  java.awt.BorderLayout;
import  java.awt.Color;
import  java.awt.Component;
import  java.awt.Dimension;
import  java.awt.FlowLayout;
import  java.awt.Frame;
import  java.awt.Toolkit;
import  java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;
import  java.awt.event.MouseAdapter;
import  java.awt.event.MouseEvent;
import  java.io.File;
import  java.io.IOException;

 import  javax.swing.BorderFactory;
import  javax.swing.JButton;
import  javax.swing.JDialog;
import javax.swing.JFrame;
import  javax.swing.JLabel;
import  javax.swing.JPanel;
import  javax.swing.JScrollPane;
import  javax.swing.JTree;
import javax.swing.SwingUtilities;
import  javax.swing.border.Border;
import  javax.swing.event.TreeExpansionEvent;
import  javax.swing.event.TreeExpansionListener;
import  javax.swing.filechooser.FileSystemView;
import  javax.swing.tree.DefaultMutableTreeNode;
import  javax.swing.tree.DefaultTreeModel;
import  javax.swing.tree.TreePath;
import  javax.swing.tree.TreeSelectionModel;

public class FolderSelectDialog extends JDialog  
{
    private boolean isOKClick_ = false;
    private String filepath_ = "" ;
    private JLabel path_  = new JLabel("");            
    private JTree tree_;
    private Border border_ = BorderFactory.createEtchedBorder(Color.white,  new  Color( 148 ,  145 ,  140 ));
    
    public FolderSelectDialog(Frame owner, String title)  
    { 
        super (owner,  true );
        setTitle(title);
        init();
        setToScreenCenter( this );
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    } 
    
    private   void  init()  
    {
        isOKClick_  =   false ;
        JPanel labelPanel  =   new  JPanel( new  FlowLayout(FlowLayout.LEFT));
        labelPanel.setSize( 300 ,  40 );
        JPanel buttonPanel  =   new  JPanel( new  FlowLayout(FlowLayout.RIGHT));
        JLabel current  =   new  JLabel( " Current Selection " );
        labelPanel.setLayout( new  BorderLayout());
        labelPanel.add(current, BorderLayout.NORTH);
        labelPanel.add(path_, BorderLayout.SOUTH);

        File[] roots  =  ( new  PFileSystemView()).getRoots();
        FileNode nod  =   new  FileNode (roots[ 0 ]);
        nod.explore();
        tree_  =   new  JTree( new  DefaultTreeModel(nod));
        tree_.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); 

        JScrollPane sp  =   new  JScrollPane(tree_);
        sp.setBorder(border_);
        
        labelPanel.setBorder(BorderFactory.createEmptyBorder( 0 ,  19 ,  0 ,  0 ));
        JButton buttonOK  =   new  JButton( " OK " );
        buttonOK.setPreferredSize( new  Dimension( 70 ,  25 ));
        JButton buttonCanel  =   new  JButton( " Canel " );
        buttonCanel.setPreferredSize( new  Dimension( 70 ,  25 ));
        buttonPanel.add(buttonOK);
        buttonPanel.add(buttonCanel);
            
        buttonCanel.addActionListener
        ( 
        		new  ActionListener()  
        		{
        			public void actionPerformed(ActionEvent e)  
        			{
        				isOKClick_  =   false ;
        				FolderSelectDialog. this .hide();
        			} 
        		} 
        );        
        buttonOK.addActionListener
        (
        		new  ActionListener()  
        		{
        			public void actionPerformed(ActionEvent e)  
        			{
        				isOKClick_  =   true ;
        				FolderSelectDialog. this .hide();
        			} 
        		}
        );
        tree_.setShowsRootHandles( true );
        tree_.addTreeExpansionListener
        (
        	new  TreeExpansionListener()  
        	{
        		public void treeCollapsed(TreeExpansionEvent e)  
        		{} 
        		public void  treeExpanded (TreeExpansionEvent e)  
        		{
        			TreePath path  =  e.getPath();
        			FileNode node  =  (FileNode)path.getLastPathComponent();
        			if  ( ! node.isExplored())  
        			{
        				DefaultTreeModel model  =  ((DefaultTreeModel) tree_.getModel());
        				node.explore();
        				model.nodeStructureChanged(node);
        			} 
        		}                
        	}
        );
        tree_.addMouseListener
        (
        	new  MouseAdapter()  
        	{
        		public   void  mousePressed(MouseEvent e)  
        		{
        			JTree tree  =  (JTree)e.getSource();
        			int  row  =  tree.getRowForLocation(e.getX(), e.getY());
        			if  (row  ==   - 1 )  
        			{
        				return ;
        			}  
        			TreePath path  =  tree.getPathForRow(row);
        			if  (path  ==   null )  
        			{
        				return ;
        			} 
        			FileNode node  =  (FileNode)path.getLastPathComponent();
        			if  (node  ==   null )  
        			{
        				return ;
        			}
        			filepath_  =  node.getString();
        			path_.setText(filepath_);
        		} 
        	}
        );        
        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(labelPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        setSize( 350 ,  400 );
        this .setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    } 
    
    public boolean isOKClick()  
    {
         return  isOKClick_;
    } 
    
    public String getSelectPath()  
    {
         return  filepath_;
    } 
    
    public void setToScreenCenter(Component component)  
    {
        Toolkit kit  =  Toolkit.getDefaultToolkit();
        Dimension screenSize  =  kit.getScreenSize();
        int  screenHeight  =  screenSize.height;
        int  screenWidth   =  screenSize.width;
        component.setLocation((screenWidth  -  component.getWidth())  /   2 , (screenHeight  -  component.getHeight())  /   2 );
    } 
    
    class FileNode extends DefaultMutableTreeNode 
    {
    	private   static   final   long  serialVersionUID  =   - 6700441318091474795L ;
    	private   boolean  explored_  =   false ;
    	
    	public FileNode(File file)  
    	{
    		setUserObject (file);
        } 
    	public boolean getAllowChildren()  
    	{
    		return isDirectory();
        } 
        public boolean isLeaf()  
        {
        	return !isDirectory();
        } 
 
        public File getFile()
        {
        	return (File)getUserObject();
        } 
        public boolean isExplored()  
        {
             return explored_;
        } 
        public boolean isDirectory()  
        {
        	File file  =  getFile();
        	return  file.isDirectory();
        } 
        public  String toString()  
        {
        	File file  =  getFile ();
            String filename  =  file.toString();
            int  index  =  filename.lastIndexOf( " \\ " );
            return  (index  !=   - 1   &&  index  !=  filename.length()  -   1 )  ? filename.substring(index  +   1 ) :filename;
        } 
        public  String getString()  
        {
            File file  =  getFile ();
            String filename  =  file.getAbsolutePath();
            return  filename;
        } 
        public   void  explore()  
        {
             if  ( ! isDirectory())  
             {
            	 return ;    
             }         
             if  ( ! isExplored())  
             {
                File file  =  getFile ();
                File [] children  =  file.listFiles();
                for  ( int  i  =   0 ; i  <  children.length;  ++ i)  
                {
                    if  (children[i].isDirectory())  
                    {
                       add( new  FileNode (children[i]));
                    } 
                }  
                explored_  =   true ;
             } 
        }  
    } 
    
    class  PFileSystemView  extends  FileSystemView  
    {
    	public  File createNewFolder(File containingDir)  throws  IOException  
    	{
             return   null ;
        } 
    } 
   
    public static void main(String[] args) 
    {
    	JFrame f = new JFrame("test");
   	 	JButton bt = new JButton("Check");
   	 	bt.addActionListener
   	 	(
   	 			new ActionListener() 
   	 			{
   	 				public void actionPerformed(ActionEvent e) 
   	 				{
   	 					FolderSelectDialog d = new FolderSelectDialog((JFrame) SwingUtilities.getWindowAncestor((JButton) e.getSource()), "FOLDER");
   	 					d.show();	
   	 					if (d.isOKClick()) 
   	 					{
   	 						System.out.println(d.getSelectPath());
   	 					}
   	 				}
   	 			}
   	 	);
    	f.setContentPane(bt);
    	f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	f.show();
    	f.pack();
      } 
} 
