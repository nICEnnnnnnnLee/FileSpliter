package nicelee.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import nicelee.ui.thread.MergeThread;
import nicelee.ui.thread.SplitThread;

public class MainFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton btnSplit = new JButton("分解");
	JButton btnMerge = new JButton("合成");
	JButton btnSplitFileChooser = new JButton("...");
	JButton btnMergeFolderChooser = new JButton("...");
	JTextField folderText = new JTextField();
	JTextField fileText = new JTextField();
	JTextField splitText = new JTextField();
	JTextArea consoleArea = new JTextArea(10, 50);

	public static void main(String[] args) {
		MainFrame log = new MainFrame();
		log.InitUI();
	}

	public void InitUI() {

		// 设置窗口名称
		this.setTitle("文件分割器");
		// 设置窗口大小
		this.setSize(720, 427);
		this.setResizable(false);
		// 设置窗口位于屏幕中央
		this.setLocationRelativeTo(null);
		// 参数为3时，表示关闭窗口则程序退出
		this.setDefaultCloseOperation(3);
		URL iconURL=this.getClass().getResource("/resources/favicon.png");
		ImageIcon icon=new ImageIcon(iconURL);
		this.setIconImage(icon.getImage());

		// 此处使用流式布局FlowLayout，流式布局类似于word的布局
		FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
		// this窗口设置为f1的流式左对齐
		this.setLayout(f1);

		JLabel tmp0 = new JLabel();
		// 设置空JLabel长度大小.
		tmp0.setPreferredSize(new Dimension(80, 90));
		// 将空JLabel添加入窗口
		this.add(tmp0);
		
		URL fileURL=this.getClass().getResource("/resources/title.png"); 
		ImageIcon imag1 = new ImageIcon(fileURL);
		JLabel pic1 = new JLabel(imag1, SwingConstants.LEFT);
		pic1.setPreferredSize(new Dimension(610, 90));
		// 将创建的图片对象添加到 窗口上
		this.add(pic1);

		// 创建一个空的JLabel，它的长度宽度为110,30
		JLabel name1 = new JLabel();
		// 设置空JLabel长度大小
		name1.setPreferredSize(new Dimension(110, 30));
		// 将空JLabel添加入窗口
		this.add(name1);

		JLabel name = new JLabel("目标文件：");
		name.setPreferredSize(new Dimension(80, 30));
		this.add(name);

		// JTextField在窗口上添加一个可输入可见文本的文本框，需要添加的包名为javax.swing.JTextField.
		// 设置文本框大小
		fileText.setPreferredSize(new Dimension(220, 30));
		// 添加到窗口上
		this.add(fileText);

		// JButton创建一个可点击的按钮
		btnSplitFileChooser.addActionListener(this);
		btnSplitFileChooser.setPreferredSize(new Dimension(20, 30));
		this.add(btnSplitFileChooser);
		
		JLabel tl1 = new JLabel("分成份数: ");
		tl1.setPreferredSize(new Dimension(60, 30));
		this.add(tl1);
		
		splitText.setPreferredSize(new Dimension(30, 30));
		splitText.setText("3");
		this.add(splitText);
		
		btnSplit.addActionListener(this);
		btnSplit.setPreferredSize(new Dimension(60, 30));
		this.add(btnSplit);
		
		// 同name1
		JLabel name2 = new JLabel();
		name2.setPreferredSize(new Dimension(20, 30));
		this.add(name2);

		// 同name1
		JLabel name3 = new JLabel();
		name3.setPreferredSize(new Dimension(110, 30));
		this.add(name3);

		// 同name
		JLabel password = new JLabel("目标文件夹：");
		password.setPreferredSize(new Dimension(80, 30));
		this.add(password);
		
		folderText.setPreferredSize(new Dimension(220, 30));
		this.add(folderText);
		
		btnMergeFolderChooser.addActionListener(this);
		btnMergeFolderChooser.setPreferredSize(new Dimension(20, 30));
		this.add(btnMergeFolderChooser);
		
		btnMerge.addActionListener(this);
		btnMerge.setPreferredSize(new Dimension(60, 30));
		this.add(btnMerge);
		// 同name1
		JLabel name4 = new JLabel();
		name4.setPreferredSize(new Dimension(110, 30));
		this.add(name4);

		// 同name1
		JLabel name5 = new JLabel();
		name5.setPreferredSize(new Dimension(90, 30));
		this.add(name5);
		consoleArea.setEditable(false);
		JScrollPane js=new JScrollPane(consoleArea);
		//分别设置水平和垂直滚动条出现方式
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(js);
		
		// 设置窗口可见，此句一定要在窗口属性设置好了之后才能添加，不然无法正常显示
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSplitFileChooser) {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setDialogTitle("请选择要分解的文件...");
			fileChooser.setApproveButtonText("确定");
			fileChooser.showOpenDialog(this);// 显示打开的文件对话框
			File f = fileChooser.getSelectedFile();// 使用文件类获取选择器选择的文件
			if (f != null) {
				String s = f.getAbsolutePath();// 返回路径名
				fileText.setText(s);
				consoleArea.setText("当前选择文件大小约");
				if( f.length() > 1024 ) {
					consoleArea.append(f.length()/1024 + "KB\r\n");
				}else {
					consoleArea.append(f.length() + "B\r\n");
				}
			}
		} else if (e.getSource() == btnMergeFolderChooser) {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogTitle("请选择要合并的文件所在文件夹...");
			fileChooser.setApproveButtonText("确定");
			fileChooser.showOpenDialog(this);// 显示打开的文件对话框
			File f = fileChooser.getSelectedFile();// 使用文件类获取选择器选择的文件
			if (f != null) {
				String s = f.getAbsolutePath();// 返回路径名
				folderText.setText(s);
			}
		}else if (e.getSource() == btnSplit) {
			new SplitThread(this, btnSplit, btnMerge, splitText, fileText, consoleArea).start();
		}else if (e.getSource() == btnMerge) {
			new MergeThread(this, btnSplit, btnMerge, folderText, consoleArea).start();
			
		}
	}
}
