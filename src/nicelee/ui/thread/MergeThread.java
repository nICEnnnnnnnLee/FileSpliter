package nicelee.ui.thread;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nicelee.file.FileSpliter;

public class MergeThread extends Thread{
	JFrame jf;
	JButton btnSplit;
	JButton btnMerge;
	JTextField folderText;
	JTextArea consoleArea;
	
	public MergeThread(JFrame jf, JButton btnSplit, JButton btnMerge, JTextField folderText, JTextArea consoleArea) {
		this.jf = jf;
		this.btnSplit = btnSplit;
		this.btnMerge = btnMerge;
		this.folderText = folderText;
		this.consoleArea = consoleArea;
		this.setName("Thread-Merge");
	}
	public void run() {
		String destMerge = folderText.getText();
		btnSplit.setEnabled(false);
		btnMerge.setEnabled(false);
		int result = new FileSpliter(consoleArea).merge(destMerge);
		switch(result){
		case 0:
			JOptionPane.showMessageDialog(jf, "�ϲ��ɹ�", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
			try {
				//Runtime.getRuntime().exec(destSplit);
				Desktop.getDesktop().open(new File(destMerge));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    break;
		case -1:
			JOptionPane.showMessageDialog(jf, "�ļ��в�����", "����",JOptionPane.WARNING_MESSAGE);
			break;
		case -2:
			JOptionPane.showMessageDialog(jf, "����part�ļ�������", "����",JOptionPane.WARNING_MESSAGE);
			break;
		case -3:
			JOptionPane.showMessageDialog(jf, "����part�ļ��ѱ��ƻ�", "����",JOptionPane.WARNING_MESSAGE);
			break;
		default:
		    break;
		}
		btnSplit.setEnabled(true);
		btnMerge.setEnabled(true);
	}
}
