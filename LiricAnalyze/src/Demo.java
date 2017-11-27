import java.util.TreeMap;
import com.qcloud.QcloudApiModuleCenter;
import com.qcloud.Module.Cvm;
import com.qcloud.Utilities.Json.JSONObject;
import com.qcloud.Module.Wenzhi;


import java.io.*;

public class Demo {
	private static final String qinggan = "TextSentiment";
	
	
	public static void main(String[] args) {
		
		//TextSentiment t = new TextSentiment();
		//t.execute();
		LexicalAnalysis l = new LexicalAnalysis();
		l.execute(1);
		
	}
	
}