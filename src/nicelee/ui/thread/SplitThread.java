package nicelee.ui.thread;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nicelee.file.FileSpliter;

public class SplitThread extends Thread{
	JFrame jf;
	JButton btnSplit;
	JButton btnMerge;
	JTextField splitText;
	JTextField fileText;
	JTextArea consoleArea;
	
	public SplitThread(JFrame jf, JButton btnSplit, JButton btnMerge, JTextField splitText, JTextField fileText, JTextArea consoleArea) {
		this.jf = jf;
		this.btnSplit = btnSplit;
		this.btnMerge = btnMerge;
		this.splitText = splitText;
		this.fileText = fileText;
		this.consoleArea = consoleArea;
		this.setName("Thread-Split");
	}
	public void run() {
		int numSplit;
		String destSplit = fileText.getText();
		try {
			numSplit = Integer.parseInt(splitText.getText());
		}catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(jf, "�ָ��������Ϊ����", "����",JOptionPane.WARNING_MESSAGE);
			return;
		}
		btnSplit.setEnabled(false);
		btnMerge.setEnabled(false);
		int result = new FileSpliter(consoleArea).split(destSplit, numSplit);
		switch(result){
		case 1:
			JOptionPane.showMessageDialog(jf, "�ָ�ɹ�", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
		    break;
		case 0:
			JOptionPane.showMessageDialog(jf, "δ֪����", "����",JOptionPane.WARNING_MESSAGE);
		    break;
		case -1:
			JOptionPane.showMessageDialog(jf, "�ļ�������", "����",JOptionPane.WARNING_MESSAGE);
			break;
		case -2:
			JOptionPane.showMessageDialog(jf, "�ļ���Ŀ����", "����",JOptionPane.WARNING_MESSAGE);
			break;
		default:
		    break;
		}
		btnSplit.setEnabled(true);
		btnMerge.setEnabled(true);
	}
}
