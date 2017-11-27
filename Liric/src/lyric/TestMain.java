package lyric;

import java.util.Iterator;
import java.util.Scanner;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URLDecoder;
public class TestMain {
	//static String startUrl = "http://music.baidu.com/search/lrc?key=%E8%AE%B8%E5%B5%A9&start=0&size=20&third_type=0";//百度许嵩歌词---第一页
	static String basicUrl = "http://music.baidu.com";
	static String filePath = "C:\\Users\\fan\\Desktop\\项目清单\\Liric\\files\\";
	static String singer;
	static String lyricSearchUrl = "http://music.baidu.com/search/lrc?key=";//百度音乐歌词搜索页面 key= 后面填写要搜索的人名
	
	public static void main(String[] args) {
		System.out.println("-----一键歌词获取------");
		System.out.println("-----数据来源：百度音乐-----");
		System.out.print("请输入歌词存储目录（如 f:/歌词）,如果该目录不存在，会自动创建：");
		Scanner scan = new Scanner(System.in);
		filePath = scan.nextLine() + "/";
		System.out.print("输入singer：");
		singer = scan.nextLine();
		setSinger(singer);
		execute();
	}
	
	public static void setSinger(String name) {
		try {
			lyricSearchUrl += URLEncoder.encode(name,"utf-8");
			singer = name;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void execute() {
		int fileCount = 0;
		while(true) {

			try {
				System.out.println("正在爬取页面" + lyricSearchUrl);
				Document doc = Jsoup.connect(lyricSearchUrl).get();
				Elements li = doc.select("li.clearfix");
				Iterator<Element> i = li.iterator();
				while(i.hasNext()) {
					Element tmp = i.next(); 
					//获取歌曲容器
					Element lyricContant = tmp.selectFirst(".lrc-content");
					//获取歌曲名字
					String title = tmp.selectFirst(".song-title").selectFirst("a").text();
					//获取p标签下的歌词
					Element p = lyricContant.getElementsByTag("p").get(0);
					//下面这三行代码写的我无地自容，但是谁让你Jsoup不支持<br>解析？？
					String lyric = p.toString().replaceAll("<br>", "vanmesure").replaceAll("<em>", " ").replaceAll("</em>", " ");
					p =  Jsoup.parse(lyric);
					lyric = p.text().replaceAll("vanmesure", "\n\r");
					
					try {
						//生成文件
						File f= new File(filePath + singer + "/" + title + ".txt");
						File fDirs = new File(f.getParent());
						if(!fDirs.exists()) {
							fDirs.mkdirs();
						}
					
						if(f.createNewFile()) {
							FileWriter fw = new FileWriter(f);
							fw.write(lyric);;
							fw.flush();
							fw.close();
							fileCount += 1;
						}
						System.out.println("成功写入<" + title + ">");
					}catch(Exception e) {
						System.out.println("尝试写入文件错误，请检查目录格式。");
					}
					
				}
				
				//page-navigator-next为 ‘下一页’ 的类名  
				//注意href为相对路径
				//newtHref的实例为  \t    \t/search/lrc?key=%E8%AE%B8%E5%B5%A9&start=20&size=20&third_type=0\n
				//需要做一下处理
				String nextHref;
				try {
					nextHref = doc.getElementsByClass("page-navigator-next").get(0).attr("href");
				}catch(Exception e) {
					break;
				}
				//去掉前缀和\n
				nextHref = nextHref.replaceAll("\\t    \\t", "");
				nextHref = nextHref.replaceAll("\n", "");
				lyricSearchUrl = basicUrl + nextHref;
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("\n\r写入完成！ 总共爬取了" + fileCount + "首歌曲");
	}
	
	
}

