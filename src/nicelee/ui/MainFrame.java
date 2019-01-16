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
	JButton btnSplit = new JButton("�ֽ�");
	JButton btnMerge = new JButton("�ϳ�");
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

		// ���ô�������
		this.setTitle("�ļ��ָ���");
		// ���ô��ڴ�С
		this.setSize(720, 427);
		this.setResizable(false);
		// ���ô���λ����Ļ����
		this.setLocationRelativeTo(null);
		// ����Ϊ3ʱ����ʾ�رմ���������˳�
		this.setDefaultCloseOperation(3);
		URL iconURL=this.getClass().getResource("/resources/favicon.png");
		ImageIcon icon=new ImageIcon(iconURL);
		this.setIconImage(icon.getImage());

		// �˴�ʹ����ʽ����FlowLayout����ʽ����������word�Ĳ���
		FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
		// this��������Ϊf1����ʽ�����
		this.setLayout(f1);

		JLabel tmp0 = new JLabel();
		// ���ÿ�JLabel���ȴ�С.
		tmp0.setPreferredSize(new Dimension(80, 90));
		// ����JLabel����봰��
		this.add(tmp0);
		
		URL fileURL=this.getClass().getResource("/resources/title.png"); 
		ImageIcon imag1 = new ImageIcon(fileURL);
		JLabel pic1 = new JLabel(imag1, SwingConstants.LEFT);
		pic1.setPreferredSize(new Dimension(610, 90));
		// ��������ͼƬ������ӵ� ������
		this.add(pic1);

		// ����һ���յ�JLabel�����ĳ��ȿ��Ϊ110,30
		JLabel name1 = new JLabel();
		// ���ÿ�JLabel���ȴ�С
		name1.setPreferredSize(new Dimension(110, 30));
		// ����JLabel����봰��
		this.add(name1);

		JLabel name = new JLabel("Ŀ���ļ���");
		name.setPreferredSize(new Dimension(80, 30));
		this.add(name);

		// JTextField�ڴ��������һ��������ɼ��ı����ı�����Ҫ��ӵİ���Ϊjavax.swing.JTextField.
		// �����ı����С
		fileText.setPreferredSize(new Dimension(220, 30));
		// ��ӵ�������
		this.add(fileText);

		// JButton����һ���ɵ���İ�ť
		btnSplitFileChooser.addActionListener(this);
		btnSplitFileChooser.setPreferredSize(new Dimension(20, 30));
		this.add(btnSplitFileChooser);
		
		JLabel tl1 = new JLabel("�ֳɷ���: ");
		tl1.setPreferredSize(new Dimension(60, 30));
		this.add(tl1);
		
		splitText.setPreferredSize(new Dimension(30, 30));
		splitText.setText("3");
		this.add(splitText);
		
		btnSplit.addActionListener(this);
		btnSplit.setPreferredSize(new Dimension(60, 30));
		this.add(btnSplit);
		
		// ͬname1
		JLabel name2 = new JLabel();
		name2.setPreferredSize(new Dimension(20, 30));
		this.add(name2);

		// ͬname1
		JLabel name3 = new JLabel();
		name3.setPreferredSize(new Dimension(110, 30));
		this.add(name3);

		// ͬname
		JLabel password = new JLabel("Ŀ���ļ��У�");
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
		// ͬname1
		JLabel name4 = new JLabel();
		name4.setPreferredSize(new Dimension(110, 30));
		this.add(name4);

		// ͬname1
		JLabel name5 = new JLabel();
		name5.setPreferredSize(new Dimension(90, 30));
		this.add(name5);
		consoleArea.setEditable(false);
		JScrollPane js=new JScrollPane(consoleArea);
		//�ֱ�����ˮƽ�ʹ�ֱ���������ַ�ʽ
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(js);
		
		// ���ô��ڿɼ����˾�һ��Ҫ�ڴ����������ú���֮�������ӣ���Ȼ�޷�������ʾ
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSplitFileChooser) {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setDialogTitle("��ѡ��Ҫ�ֽ���ļ�...");
			fileChooser.setApproveButtonText("ȷ��");
			fileChooser.showOpenDialog(this);// ��ʾ�򿪵��ļ��Ի���
			File f = fileChooser.getSelectedFile();// ʹ���ļ����ȡѡ����ѡ����ļ�
			if (f != null) {
				String s = f.getAbsolutePath();// ����·����
				fileText.setText(s);
				consoleArea.setText("��ǰѡ���ļ���СԼ");
				if( f.length() > 1024 ) {
					consoleArea.append(f.length()/1024 + "KB\r\n");
				}else {
					consoleArea.append(f.length() + "B\r\n");
				}
			}
		} else if (e.getSource() == btnMergeFolderChooser) {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogTitle("��ѡ��Ҫ�ϲ����ļ������ļ���...");
			fileChooser.setApproveButtonText("ȷ��");
			fileChooser.showOpenDialog(this);// ��ʾ�򿪵��ļ��Ի���
			File f = fileChooser.getSelectedFile();// ʹ���ļ����ȡѡ����ѡ����ļ�
			if (f != null) {
				String s = f.getAbsolutePath();// ����·����
				folderText.setText(s);
			}
		}else if (e.getSource() == btnSplit) {
			new SplitThread(this, btnSplit, btnMerge, splitText, fileText, consoleArea).start();
		}else if (e.getSource() == btnMerge) {
			new MergeThread(this, btnSplit, btnMerge, folderText, consoleArea).start();
			
		}
	}
}
