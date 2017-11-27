import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Excel {
	private WritableWorkbook wwb;
	private WritableSheet ws;
	private int colCount = 0;
	private int rowCount = 1;//第0行为表头，内容行从1开始
	public boolean createNewExcel(String fileName) {
		File f = new File(fileName);
		try {
			WritableWorkbook wwb = Workbook.createWorkbook(f);
			WritableSheet ws = wwb.createSheet("sheet1", 0);
			this.wwb = wwb;
			this.ws = ws;
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void writeHead(String[] heads) {
		for(int i = 0; i < heads.length; i ++) {
			Label l = new Label(i, 0, heads[i]);
			try {
				ws.addCell(l);
			}catch(Exception e) {
				System.out.println("写入单元格失败");
				e.printStackTrace();
				return;
			}
		}
		this.colCount = heads.length;
	}
	
	public void writeContent(String[] content) {
		if(content.length != this.colCount) {
			System.out.println("1");
			return;
		}
		
		for(int i = 0; i < this.colCount; i ++) {
			Label l = new Label(i, rowCount, content[i]);
			try {
				ws.addCell(l);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		rowCount ++;
		
		
	}
	public void writeContent(HashMap content) {
		Iterator i = content.entrySet().iterator();
		int row = 1;
		int col = 1;
		while(i.hasNext()) {
			Map.Entry e = (Map.Entry)i.next();
			String key = e.getKey().toString();
			String value = e.getValue().toString();
			//过滤长度为1的key
			if(key.length() < 2) {
				continue;
			}
			Label keyL = new Label(0, row, key);
			Label valueL = new Label(1, row ++, value);
			try {
				ws.addCell(keyL);
				ws.addCell(valueL);
			}catch(Exception a) {
				a.printStackTrace();
			}
			
		}
	}
	
	
	public void write() {
		try {
			//write()应该只能调用一次，所以要放到最后调用
			wwb.write();
			wwb.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
