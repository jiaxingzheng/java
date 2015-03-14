package OperatingSyetem;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.datatransfer.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import com.sun.org.apache.xml.internal.utils.StringVector;

public class FileSystemUI {

	private JFrame frame;
	private JTree tree;
	private JTable table;
	private JPopupMenu tablePopupMenu;
	private JPopupMenu popupMenu;
	private String nameOfFileToCopy;
	private String pathOfFileToCopy;
	private String nameOfFileToMove;
	private String pathOfFileToMove;
	private Icon draggedIcon;
	private JScrollPane scrollTablePane;
	private JScrollPane scrollTreePane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileSystemUI window = new FileSystemUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FileSystemUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 714, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		// frame.setLayout(new
		// BoxLayout(frame.getContentPane(),BoxLayout.X_AXIS));
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		scrollTreePane = new JScrollPane();
		scrollTreePane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// frame.add(scrollTreePane);
		scrollTreePane.setBorder(null);

		tree = new JTree();
		scrollTreePane.getViewport().add(tree);

		table = new JTable();
		scrollTablePane = new JScrollPane();
		scrollTablePane.getViewport().add(table);
	
		// frame.add(scrollTreePane);

		splitPane.setLeftComponent(scrollTreePane);
		splitPane.setRightComponent(scrollTablePane);
		splitPane.setDividerLocation(250);
		frame.add(splitPane);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		ImageIcon icon = new ImageIcon("addFolder.png");
		icon.setImage(icon.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
		JMenuItem menuItem = new JMenuItem("新建文件夹",icon);
		menuItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				createFolder();
			}
		});
		ImageIcon icon1 = new ImageIcon("addFile.png");
		icon1.setImage(icon1.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
		JMenuItem menuItem1 = new JMenuItem("新建文档",icon1);
		menuItem1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					createFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mnMenu.add(menuItem1);
		mnMenu.add(menuItem);
		
		tablePopupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem("删除");
		JMenuItem item1 = new JMenuItem("拷贝");
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				delete();
			}
		});
		item1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				copyFile();
			}
		});
		tablePopupMenu.add(item);
		tablePopupMenu.add(item1);
		
		popupMenu = new JPopupMenu();
		JMenuItem item3 = new JMenuItem("粘贴");
		item3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				pasteFile();
			}
		});
		popupMenu.add(item3);
		
	scrollTablePane.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getButton()==3){
					popupMenu.show(scrollTablePane,e.getX(),e.getY());
				}
			}
			
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		initData();
	}

	private void initData() {
		tree.setCellRenderer(new MyTreeCellRenderer());
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("PC");
		((DefaultTreeModel) tree.getModel()).setRoot(root);

		DragSource dragSource = DragSource.getDefaultDragSource();
		DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(table, DnDConstants.ACTION_MOVE, new DragGestureListener() {
			
			public void dragGestureRecognized(DragGestureEvent dge) {
				// TODO Auto-generated method stub
				Toolkit tk = Toolkit.getDefaultToolkit();
				Dimension dim = tk.getBestCursorSize(draggedIcon.getIconWidth(),draggedIcon.getIconHeight()); 
				BufferedImage buff = new BufferedImage(dim.width,dim.height,BufferedImage.TYPE_INT_ARGB);
				draggedIcon.paintIcon(table,buff.getGraphics(),0,0);
				if(DragSource.isDragImageSupported()) {    
					Transferable tr = new StringSelection(pathOfFileToMove); 
					dge.startDrag(DragSource.DefaultMoveDrop,buff,new Point(0,0),tr,new DragSourceListener() {
						
						public void dropActionChanged(DragSourceDragEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
						public void dragOver(DragSourceDragEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
						public void dragExit(DragSourceEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
						public void dragEnter(DragSourceDragEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
						public void dragDropEnd(DragSourceDropEvent arg0) {
							// TODO Auto-generated method stub
							
						}
					});
					} 		
			}
		});
		DropTarget drogTarget = new DropTarget(tree,DnDConstants.ACTION_MOVE,new DropTargetListener() {
			
			public void dropActionChanged(DropTargetDragEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void drop(DropTargetDropEvent dte) {
				// TODO Auto-generated method stub
				TreePath path = tree.getPathForLocation(dte.getLocation().x, dte.getLocation().y);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getPathComponent(2);
				String str = getCurrentNodePath(node);
				str+=nameOfFileToMove;
		
				File t = new File(str);
				File s = new File(pathOfFileToMove);
				fileChannelCopy(s, t);
				s.delete();
				showFiles(node);
			
			}
			
			public void dragOver(DropTargetDragEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void dragExit(DropTargetEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void dragEnter(DropTargetDragEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		table.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
		
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getButton()==3){
					tablePopupMenu.show(table,e.getX(),e.getY());
					table.clearSelection();
					DefaultListSelectionModel model = (DefaultListSelectionModel)table.getSelectionModel();	
					model.setSelectionInterval(table.rowAtPoint(new Point(e.getX(),e.getY())), table.rowAtPoint(new Point(e.getX(),e.getY())));
				}
				if(e.getButton()==1){
					if(table.isRowSelected(table.rowAtPoint(new Point(e.getX(),e.getY())))){
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
								.getLastSelectedPathComponent();
						String str = getCurrentNodePath(node);
						nameOfFileToMove= table.getModel().getValueAt(table.rowAtPoint(new Point(e.getX(),e.getY())), 0).toString();
						pathOfFileToMove=str+nameOfFileToMove;
						File file = new File(str);
						FileSystemView fsv = new JFileChooser().getFileSystemView();
						draggedIcon = fsv.getSystemIcon(file);
						
					
					}
				}
				
				
			}
			
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		File[] roots = File.listRoots();
		for (int i = 0; i < roots.length; i++) {
			root.add(new DefaultMutableTreeNode(roots[i]));
			// createTree(root,roots[i].getPath());
		}
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent arg0) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				if (node == null)
					return;
				if (node == tree.getModel().getRoot()) {
					((DefaultTableModel) table.getModel()).setRowCount(0);
					table.getTableHeader().setVisible(false);
				}
				showFiles(node);
			}
		});
		tree.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
					
			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 1) {// 点1下生成子树，再点1下展开，其实就相当于双击显示子目录
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
							.getLastSelectedPathComponent();
					if (node == null)
						return;
					if(!node.isRoot())
							node.removeAllChildren();
					createSubTree(node);
				
				}
			}
		});
	}
	private void pasteFile(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		String str = getCurrentNodePath(node);
		str+=nameOfFileToCopy;
		File t = new File(str);
		File s = new File(pathOfFileToCopy);
		fileChannelCopy(s, t);
		showFiles(node);
	}
	private void copyFile(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		String str = getCurrentNodePath(node);
		nameOfFileToCopy=table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
		pathOfFileToCopy=str+nameOfFileToCopy;
	}
	private void createFile() throws IOException{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		String str = getCurrentNodePath(node);
		str+="新建文档.txt";
		File file = new File(str);
		file.createNewFile();
		showFiles(node);
		
		
	}
	private void createFolder(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		String str = getCurrentNodePath(node);
		str+="新建文件夹";
		File file = new File(str);
		file.mkdir();
		showFiles(node);
		node.removeAllChildren();
		createSubTree(node);
	}
	private void delete(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		String str = getCurrentNodePath(node);
		
		str+=table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
		File file = new File(str);
		if(file.isDirectory()){
			deleteFolder(file);
			node.removeAllChildren();
			createSubTree(node);
		}
		else 
			file.delete();
		showFiles(node);
		
	}
	private void deleteFolder(File file){
	    if (!file.exists() || !file.isDirectory()) {  
	        return;
	    }  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = file.listFiles();  
	    if(files==null)
	    	return;
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            files[i].delete();
	        } //删除子目录  
	        else {  
	           deleteFolder(files[i]);
	        }  
	    }  
	
	    //删除当前目录  
	    file.delete();
	}
	private void showFiles(DefaultMutableTreeNode node) {
		String str = getCurrentNodePath(node);
		File file = new File(str);
		File[] roots = file.listFiles();
		if (roots == null)
			return;

		Object[] headers = { "名称", "修改日期", "类型", "大小" };
		DefaultTableModel model = new DefaultTableModel(null, headers) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] strArray = new String[4];
		for (int i = 0; i < roots.length; i++) {
			strArray[0] = roots[i].getName();
			Date modifiedTime = new Date(roots[i].lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:MM");
			strArray[1] = sdf.format(modifiedTime);
			JFileChooser chooser = new JFileChooser();
			strArray[2] = chooser.getTypeDescription(roots[i]);
			strArray[3] = String.valueOf(roots[i].length());
			model.addRow(strArray);
		}
		table.setModel(model);
		table.getTableHeader().setVisible(true);
		table.getColumnModel().getColumn(0)
				.setCellRenderer(new MyTableCellRenderer());// 设置图标

	}

	private void createSubTree(DefaultMutableTreeNode node) {
		String str = getCurrentNodePath(node);
		File file = new File(str);
		File[] roots = file.listFiles();
		if (roots == null)
			return;
		for (int i = 0; i < roots.length; i++) {
			if (!roots[i].isDirectory())
				continue;
			DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(
					roots[i].getName());
			node.add(subNode);
		}
		
		tree.repaint();
		tree.updateUI();
		 scrollTreePane.updateUI();
		 

	}

	private String getCurrentNodePath(DefaultMutableTreeNode node) {
		StringVector stringVector = new StringVector();
		DefaultMutableTreeNode tempNode = node;
		while (tempNode != null) {
			String str = tempNode.toString();
			stringVector.push(str);
			tempNode = (DefaultMutableTreeNode) tempNode.getParent();
		}
		String str = "";
		for (int i = stringVector.getLength() - 2; i >= 0; i--) {
			str += stringVector.elementAt(i);
			if (i != stringVector.getLength() - 2)
				str += "//";
		}
		return str;
	}

	// private void createTree(DefaultMutableTreeNode treeRoot,String fileRoot){
	//
	// File file = new File(fileRoot);
	// FileFilter filter = new FileFilter() {
	//
	// public boolean accept(File file) {
	// // TODO Auto-generated method stub
	// if(file.isDirectory())
	// return true;
	// return false;
	// }
	// };
	// File[] roots = file.listFiles();
	// if(roots==null)
	// return;
	// for(int i=0;i<roots.length;i++){
	// if(!roots[i].isDirectory())
	// continue;
	// DefaultMutableTreeNode node = new
	// DefaultMutableTreeNode(roots[i].getName());
	// treeRoot.add(node);
	// createTree(node,roots[i].getPath());
	// }
	//
	// }
	private void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class MyTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public java.awt.Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			// TODO Auto-generated method stub
			if (column == 0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				String str = getCurrentNodePath(node);
				str += value;
				File file = new File(str);
				FileSystemView fsv = new JFileChooser().getFileSystemView();
				Icon icon = fsv.getSystemIcon(file);
				setIcon(icon);

			}
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}

	}

}

class MyTreeCellRenderer extends DefaultTreeCellRenderer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public java.awt.Component getTreeCellRendererComponent(JTree tree,
			Object value, boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (value.toString().equals("PC")) {
			ImageIcon icon = new ImageIcon("computer.png");
			icon.setImage(icon.getImage().getScaledInstance(24, 24,
					Image.SCALE_DEFAULT));
			setIcon(icon);
		} else if (leaf) {
			ImageIcon icon = new ImageIcon("emptyFolder.png");
			icon.setImage(icon.getImage().getScaledInstance(24, 24,
					Image.SCALE_DEFAULT));
			setIcon(icon);
		} else if (expanded) {
			ImageIcon icon = new ImageIcon("openedFolder.png");
			icon.setImage(icon.getImage().getScaledInstance(24, 24,
					Image.SCALE_DEFAULT));
			setIcon(icon);
		} else {
			ImageIcon icon = new ImageIcon("closedFolder.png");
			icon.setImage(icon.getImage().getScaledInstance(24, 24,
					Image.SCALE_DEFAULT));
			setIcon(icon);
		}
		return this;
	}
}
