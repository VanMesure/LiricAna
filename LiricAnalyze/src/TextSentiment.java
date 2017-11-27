import com.qcloud.Utilities.Json.JSONObject;
import java.io.*;

public class TextSentiment {
	private String fileDirPath = "C:\\Users\\fan\\Desktop\\项目清单\\Liric\\files\\赵雷";
	private int fileCount = 0;//用来记录当前获取到了第几首歌曲
	private File[] fileList;
	public TextSentiment() {
		File f= new File(fileDirPath);
		//获取总的文件
		fileList = f.listFiles();
	}
	
	public void execute() {
		Excel e = new Excel();
		String[] head = {"歌曲名","正向情感概率"};
		e.createNewExcel("C:\\Users\\fan\\Desktop\\项目清单\\LiricAnalyze\\赵雷歌词情感值分析.xls");
		e.writeHead(head);//写入表头
		
		//循环调用api
		for(int i = 0; i < fileList.length; i ++) {
			String lyric = getLyric();
			Api api = new Api("TextSentiment");
			String[] content = new String[2];
			//调用api
			JSONObject jsonResult = api.analyze(lyric);
			//写入文件名
			content[0] = fileList[fileCount - 1].getName();
			//成功时候code字段返回0，获取正面感情概率
			if(jsonResult.getInt("code") == 0) {
				content[1] = Double.toString(jsonResult.getDouble("positive"));
			}else {
				//如果不成功，做撤回处理
				content[1] = "";
				fileCount --;
				i --;
			}
			//将content写入表格
			e.writeContent(content);			
		}
		e.write();
		System.out.println("处理完毕");
	}
	
	private String getLyric() {
		File f = fileList[fileCount];
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String lyric = "";
			String tmp;
			while((tmp = br.readLine()) != null) {
				lyric += tmp;
			}
			br.close();
			fileCount ++;
			System.out.println(lyric);
			return lyric;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		
	}
	
	
}
