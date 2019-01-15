package nicelee.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashSet;

import javax.swing.JTextArea;

public class FileSpliter {

	//StringBuffer consoleStr = new StringBuffer();
	JTextArea consoleArea;
	
	// split - �������� String fileDst, int splitNum
	// merge - һ������ String folderDst
	public static void main(String args[]) {
		System.out.println("-------------------------------");
		System.out.println("�ָ��:");
		System.out.println("������������ String fileDst, int splitNum");
		System.out.println("�ϲ�����:");
		System.out.println("����һ������ String folderDst");
		System.out.println("-------------------------------");
		int cnt = 0;
		for(String arg: args) {
			cnt++;
			System.out.println("��" + cnt +"������Ϊ:");
			System.out.println(arg);
		}
		System.out.println("-------------------------------");
		if( args!=null && args.length==1 ) {
			int result = new FileSpliter().merge(args[0]);
			switch(result){
			case 0:
				System.out.println("�ϲ��ɹ�");
			    break;
			case -1:
				System.out.println("�ļ��в�����");
				break;
			case -2:
				System.out.println("����part�ļ�������");
				break;
			case -3:
				System.out.println("����part�ļ��ѱ��ƻ�");
				break;
			default:
			    break;
			}
			
		}else if( args!=null && args.length==2 ){
			int num;
			try {
				num = Integer.parseInt(args[1]);
			}catch (NumberFormatException ex) {
				System.out.println("�ָ��������Ϊ����");
				return;
			}
			
			int result = new FileSpliter().split(args[0], num);
			switch(result){
			case 1:
				System.out.println("�ָ�ɹ�");
			    break;
			case 0:
				System.out.println("δ֪����");
			    break;
			case -1:
				System.out.println("�ļ�������");
				break;
			case -2:
				System.out.println("�ļ���Ŀ����");
				break;
			default:
			    break;
			}
		}else {
			System.out.println("��������");
		}
		// new FileSpliter().split("D:\\Work\\test.txt", 3);
		//new FileSpliter(null).merge("D:\\Work\\split");
	}

	public FileSpliter(JTextArea consoleArea) {
		this.consoleArea = consoleArea;
	}
	public FileSpliter() {
	}

	void print(String str) {
		System.out.println(str);
		//consoleStr.append(str).append("\r\n");
		if (consoleArea != null) {
			consoleArea.append(str);
			consoleArea.append("\r\n");
		}
	}

	// ������
	byte[] buff = new byte[1024 * 1024];

	/**
	 * int total
	 * 
	 * @param fileName
	 * @param part
	 * @return return 1;// �ɹ� return 0;// δ֪���� return -1;// �ļ������� return -2;//
	 *         �ļ���Ŀ����
	 * @throws IOException
	 */
	public int split(String fileName, int part) {
		if (part < 2) {
			return -2;// �ļ���Ŀ����
		}
		File file = new File(fileName);
		if (!(file.exists() && file.isFile())) {
			return -1;// �ļ�������
		}
		long len = file.length() % part == 0 ? file.length() / part : file.length() / part + 1;
		return split(file, len, part);
	}

	/**
	 * int total
	 * 
	 * @param fileName
	 * @param part
	 * @return return 1;// �ɹ� return 0;// δ֪���� return -1;// �ļ������� return -2;//
	 *         �ļ���Ŀ����
	 * @throws IOException
	 */
	public int split(String fileName, long len) {

		File file = new File(fileName);
		if (!(file.exists() && file.isFile())) {
			return -1;// �ļ�������
		}
		int part = (int) (file.length() % len == 0 ? file.length() / len : file.length() / len + 1);
		// long len = file.length() % part == 0 ? file.length() / part : file.length() /
		// part + 1;
		return split(file, len, part);

	}

	int split(File file, long len, int part) {
		// �½�split�ļ���
		File folder = file.getParentFile();
		File splitFolder = new File(folder, "split");
		if (!splitFolder.exists()) {
			new File(folder, "split").mkdir();
		}
		Date start = new Date();
		print("��ǰ���ڷֽ�" + file.getName());
		try {
			// ��ȡ�ļ�,������
			RandomAccessFile originRAF = new RandomAccessFile(file, "r");
			for (int i = 0; i < part; i++) {
				originRAF.seek(len * i);
				print("���ڷֽ��" + (i+1) +"�����ļ�");
				File partFile = new File(splitFolder, file.getName() + ".part" + i);
				if (partFile.exists()) {
					partFile.delete();
				}
				// дpart�ļ�
				RandomAccessFile raf = new RandomAccessFile(partFile, "rw");
				// 0~4���ֽ�:�ܷ���;5~8���ֽ�:��ǰ����;
				raf.writeInt(part);
				raf.writeInt(i);
				int count = 0;
				int lenRead = originRAF.read(buff);
				while (lenRead != -1) {
					// print("��ȡ�ֽ�"+lenRead);
					if (lenRead >= len - count) {
						raf.write(buff, 0, (int) (len - count));
						break;
					}
					raf.write(buff, 0, lenRead);
					lenRead = originRAF.read(buff);
					count += lenRead;
				}
				raf.close();
				print("��" + (i+1) +"�����ļ������");
			}
			originRAF.close();
			print(file.getName() + "�ֽ�ɹ�");
			Date end = new Date();
			long duration = (end.getTime() - start.getTime());
			if (duration > 1000000) {
				print("��ʱ" + (duration) / 1000 + "s");
			} else {
				print("��ʱ" + (duration) + "ms");
			}
			return 1;
		} catch (Exception e) {
			return 0;// δ֪����
		}
	}

	/**
	 * 
	 * @param fileFolder
	 * @return return 0;// �ɹ� return -1;// �ļ��в����� return -2;// �ļ��Ѳ����� return -3;//
	 *         �ļ��ѱ��ƻ�
	 */
	@SuppressWarnings("resource")
	public int merge(String fileFolder) {

		File folder = new File(fileFolder);
		if (!(folder.exists() && folder.isDirectory())) {
			return -1;// �ļ��в�����
		}
		File[] files = folder.listFiles();
		HashSet<String> fileSet = new HashSet<String>();
		for (File file : files) {
			if (file.isFile() && file.getName().matches(".*\\.part[0-9]+$")) {
				String fileName = file.getName().replaceAll("\\.part[0-9]+$", "");
				fileSet.add(fileName);
			}
		}
		for (String fileName : fileSet) {
			Date start = new Date();
			print("��ǰ���ںϳ�" + fileName);
			File file = new File(folder, fileName);
			if (file.exists()) {
				file.delete();
			}
			try {
				RandomAccessFile destRAF = new RandomAccessFile(file, "rw");
				print("���ںϲ���" + (1) +"�����ļ�");
				// ��ȡ��һ���ļ�
				File partFile = new File(folder, fileName + ".part0");
				RandomAccessFile partRAF = new RandomAccessFile(partFile, "r");
				int total = partRAF.readInt();
				int current = partRAF.readInt();
				if (current != 0) {
					print("�ļ��ѱ��ƻ�");
					return -3;// �ļ��ѱ��ƻ�
				}
				
				int len = partRAF.read(buff);
				while (len != -1) {
					destRAF.write(buff, 0, len);
					len = partRAF.read(buff);
				}
				partRAF.close();
				print("��" + (+1) +"�����ļ������");
				for (int i = 1; i < total; i++) {
					print("���ںϲ���" + (i+1) +"�����ļ�");
					partFile = new File(folder, fileName + ".part" + i);
					partRAF = new RandomAccessFile(partFile, "r");
					int totalRead = partRAF.readInt();
					int currentRead = partRAF.readInt();
//					System.out.printf("�ܹ�%d,���ļ�����%d\r\n",total, totalRead);
//					System.out.printf("��ǰ%d,���ļ�����%d\r\n",i, currentRead);
					if (totalRead != total || currentRead != i) {
						print("�ļ��ѱ��ƻ�");
						return -3;// �ļ��ѱ��ƻ�
					}
					len = partRAF.read(buff);
					while (len != -1) {
						destRAF.write(buff, 0, len);
						len = partRAF.read(buff);
					}
					partRAF.close();
					print("��" + (i+1) +"�����ļ������");
				}
				print(fileName + "�ϳɳɹ�");
				Date end = new Date();
				long duration = (end.getTime() - start.getTime());
				if (duration > 1000000) {
					print("��ʱ" + (duration) / 1000 + "s");
				} else {
					print("��ʱ" + (duration) + "ms");
				}
				destRAF.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return -2;// �ļ��Ѳ�����
			} catch (IOException e) {
				e.printStackTrace();
				return -3;// �ļ��ѱ��ƻ�
			}
		}

		return 0;
	}

}
