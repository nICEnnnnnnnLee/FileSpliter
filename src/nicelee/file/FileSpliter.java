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
	
	// split - 两个参数 String fileDst, int splitNum
	// merge - 一个参数 String folderDst
	public static void main(String args[]) {
		System.out.println("-------------------------------");
		System.out.println("分割功能:");
		System.out.println("输入两个参数 String fileDst, int splitNum");
		System.out.println("合并功能:");
		System.out.println("输入一个参数 String folderDst");
		System.out.println("-------------------------------");
		int cnt = 0;
		for(String arg: args) {
			cnt++;
			System.out.println("第" + cnt +"个参数为:");
			System.out.println(arg);
		}
		System.out.println("-------------------------------");
		if( args!=null && args.length==1 ) {
			int result = new FileSpliter().merge(args[0]);
			switch(result){
			case 0:
				System.out.println("合并成功");
			    break;
			case -1:
				System.out.println("文件夹不存在");
				break;
			case -2:
				System.out.println("部分part文件不存在");
				break;
			case -3:
				System.out.println("部分part文件已被破坏");
				break;
			default:
			    break;
			}
			
		}else if( args!=null && args.length==2 ){
			int num;
			try {
				num = Integer.parseInt(args[1]);
			}catch (NumberFormatException ex) {
				System.out.println("分割份数必须为整数");
				return;
			}
			
			int result = new FileSpliter().split(args[0], num);
			switch(result){
			case 1:
				System.out.println("分割成功");
			    break;
			case 0:
				System.out.println("未知错误");
			    break;
			case -1:
				System.out.println("文件不存在");
				break;
			case -2:
				System.out.println("文件数目不对");
				break;
			default:
			    break;
			}
		}else {
			System.out.println("参数错误");
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

	// 缓冲区
	byte[] buff = new byte[1024 * 1024];

	/**
	 * int total
	 * 
	 * @param fileName
	 * @param part
	 * @return return 1;// 成功 return 0;// 未知错误 return -1;// 文件不存在 return -2;//
	 *         文件数目不对
	 * @throws IOException
	 */
	public int split(String fileName, int part) {
		if (part < 2) {
			return -2;// 文件数目不对
		}
		File file = new File(fileName);
		if (!(file.exists() && file.isFile())) {
			return -1;// 文件不存在
		}
		long len = file.length() % part == 0 ? file.length() / part : file.length() / part + 1;
		return split(file, len, part);
	}

	/**
	 * int total
	 * 
	 * @param fileName
	 * @param part
	 * @return return 1;// 成功 return 0;// 未知错误 return -1;// 文件不存在 return -2;//
	 *         文件数目不对
	 * @throws IOException
	 */
	public int split(String fileName, long len) {

		File file = new File(fileName);
		if (!(file.exists() && file.isFile())) {
			return -1;// 文件不存在
		}
		int part = (int) (file.length() % len == 0 ? file.length() / len : file.length() / len + 1);
		// long len = file.length() % part == 0 ? file.length() / part : file.length() /
		// part + 1;
		return split(file, len, part);

	}

	int split(File file, long len, int part) {
		// 新建split文件夹
		File folder = file.getParentFile();
		File splitFolder = new File(folder, "split");
		if (!splitFolder.exists()) {
			new File(folder, "split").mkdir();
		}
		Date start = new Date();
		print("当前正在分解" + file.getName());
		try {
			// 读取文件,并复制
			RandomAccessFile originRAF = new RandomAccessFile(file, "r");
			for (int i = 0; i < part; i++) {
				originRAF.seek(len * i);
				print("正在分解第" + (i+1) +"部分文件");
				File partFile = new File(splitFolder, file.getName() + ".part" + i);
				if (partFile.exists()) {
					partFile.delete();
				}
				// 写part文件
				RandomAccessFile raf = new RandomAccessFile(partFile, "rw");
				// 0~4个字节:总份数;5~8个字节:当前序数;
				raf.writeInt(part);
				raf.writeInt(i);
				int count = 0;
				int lenRead = originRAF.read(buff);
				while (lenRead != -1) {
					// print("读取字节"+lenRead);
					if (lenRead >= len - count) {
						raf.write(buff, 0, (int) (len - count));
						break;
					}
					raf.write(buff, 0, lenRead);
					lenRead = originRAF.read(buff);
					count += lenRead;
				}
				raf.close();
				print("第" + (i+1) +"部分文件已完成");
			}
			originRAF.close();
			print(file.getName() + "分解成功");
			Date end = new Date();
			long duration = (end.getTime() - start.getTime());
			if (duration > 1000000) {
				print("用时" + (duration) / 1000 + "s");
			} else {
				print("用时" + (duration) + "ms");
			}
			return 1;
		} catch (Exception e) {
			return 0;// 未知错误
		}
	}

	/**
	 * 
	 * @param fileFolder
	 * @return return 0;// 成功 return -1;// 文件夹不存在 return -2;// 文件已不存在 return -3;//
	 *         文件已被破坏
	 */
	@SuppressWarnings("resource")
	public int merge(String fileFolder) {

		File folder = new File(fileFolder);
		if (!(folder.exists() && folder.isDirectory())) {
			return -1;// 文件夹不存在
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
			print("当前正在合成" + fileName);
			File file = new File(folder, fileName);
			if (file.exists()) {
				file.delete();
			}
			try {
				RandomAccessFile destRAF = new RandomAccessFile(file, "rw");
				print("正在合并第" + (1) +"部分文件");
				// 读取第一个文件
				File partFile = new File(folder, fileName + ".part0");
				RandomAccessFile partRAF = new RandomAccessFile(partFile, "r");
				int total = partRAF.readInt();
				int current = partRAF.readInt();
				if (current != 0) {
					print("文件已被破坏");
					return -3;// 文件已被破坏
				}
				
				int len = partRAF.read(buff);
				while (len != -1) {
					destRAF.write(buff, 0, len);
					len = partRAF.read(buff);
				}
				partRAF.close();
				print("第" + (+1) +"部分文件已完成");
				for (int i = 1; i < total; i++) {
					print("正在合并第" + (i+1) +"部分文件");
					partFile = new File(folder, fileName + ".part" + i);
					partRAF = new RandomAccessFile(partFile, "r");
					int totalRead = partRAF.readInt();
					int currentRead = partRAF.readInt();
//					System.out.printf("总共%d,从文件读出%d\r\n",total, totalRead);
//					System.out.printf("当前%d,从文件读出%d\r\n",i, currentRead);
					if (totalRead != total || currentRead != i) {
						print("文件已被破坏");
						return -3;// 文件已被破坏
					}
					len = partRAF.read(buff);
					while (len != -1) {
						destRAF.write(buff, 0, len);
						len = partRAF.read(buff);
					}
					partRAF.close();
					print("第" + (i+1) +"部分文件已完成");
				}
				print(fileName + "合成成功");
				Date end = new Date();
				long duration = (end.getTime() - start.getTime());
				if (duration > 1000000) {
					print("用时" + (duration) / 1000 + "s");
				} else {
					print("用时" + (duration) + "ms");
				}
				destRAF.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return -2;// 文件已不存在
			} catch (IOException e) {
				e.printStackTrace();
				return -3;// 文件已被破坏
			}
		}

		return 0;
	}

}
