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
			JOptionPane.showMessageDialog(jf, "分割份数必须为整数", "错误",JOptionPane.WARNING_MESSAGE);
			return;
		}
		btnSplit.setEnabled(false);
		btnMerge.setEnabled(false);
		int result = new FileSpliter(consoleArea).split(destSplit, numSplit);
		switch(result){
		case 1:
			JOptionPane.showMessageDialog(jf, "分割成功", "成功", JOptionPane.INFORMATION_MESSAGE);
		    break;
		case 0:
			JOptionPane.showMessageDialog(jf, "未知错误", "错误",JOptionPane.WARNING_MESSAGE);
		    break;
		case -1:
			JOptionPane.showMessageDialog(jf, "文件不存在", "错误",JOptionPane.WARNING_MESSAGE);
			break;
		case -2:
			JOptionPane.showMessageDialog(jf, "文件数目不对", "错误",JOptionPane.WARNING_MESSAGE);
			break;
		default:
		    break;
		}
		btnSplit.setEnabled(true);
		btnMerge.setEnabled(true);
	}
}
