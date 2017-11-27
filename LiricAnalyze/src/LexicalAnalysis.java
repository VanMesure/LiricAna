import java.io.*;

import java.util.List;
import java.util.*;
import com.qcloud.Utilities.Json.JSONArray;
import com.qcloud.Utilities.Json.JSONObject;
public class LexicalAnalysis {
	private File lyricFileDir = new File("C:\\Users\\fan\\Desktop\\项目清单\\Liric\\files\\许嵩");
	private int count = 0;
	private int fileNum = lyricFileDir.list().length;
	public void execute(int mode) {
		HashMap<String, Integer> result = new HashMap<>();
		
		Api api = new Api("LexicalAnalysis");
		//循环调用api
		while(count < fileNum) {
			String content = getLyric().toString();
			//两种模式 0更精细  1不精细
			JSONObject jsonResult = api.analyze(content, 2097152, mode);
			
			//判断一下调用是否成功
			if(jsonResult.getInt("code") == 0) {
				//取得全部的tokens
				JSONArray j = jsonResult.getJSONArray("tokens");
				//取得每个token的word字段
				for(int i = 0; i < j.length(); i ++) {
					String word = j.getJSONObject(i).getString("word");
					if(result.get(word) != null) {
						//如果已经存在，则直接传入新key覆盖
						result.put(word, new Integer(result.get(word) + 1));
					}else {
						//如果不存在
						result.put(word, new Integer(1));
					}
					System.out.println("获取到 :  " + word  + "   value: " + result.get(word));
				}
			}else {
				//如果调用不成功，给count-1，下次调用getLyric()的时候，会重新获取这首歌
				count --;
			}
		}
		System.out.println("获取数据完毕，共获取" + count + "条数据，开始进行写入");
		
		//开始写入excel
		Excel e = new Excel();
		e.createNewExcel("C:\\Users\\fan\\Desktop\\项目清单\\LiricAnalyze\\许嵩歌词分词统计(模糊+过滤).xls");
		String[] headers = {"分词","出现次数"};
		e.writeHead(headers);
		e.writeContent(result);
		e.write();
	}
	
	private StringBuffer getLyric() {
		StringBuffer content = new StringBuffer();
		File[] files = lyricFileDir.listFiles();
		File f = files[count];
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String tmp;
			while((tmp = br.readLine()) != null) {
				content.append(tmp);
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		count ++;
		return content;
	}
}


