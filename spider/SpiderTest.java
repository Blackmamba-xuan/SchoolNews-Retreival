package com.blackmamba.IrStudy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.hu.HungarianLightStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderTest {

	public static void main(String[] args) throws IOException, ParseException {
		String url = "http://www.szu.cn/board/?dayy=90%23%C8%FD%B8%F6%D4%C2&search_type=post_time&keyword=+&keyword_user=&searchb1=%CB%D1%CB%F7";
		Connection connection = Jsoup.connect(url);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Document document=connection.timeout(100000).get();
		File file = new File("C:/Users/kobe_xuan/Desktop/szu.html");
		Document document = Jsoup.parse(file, "GBK");
		// System.out.println(document.html());
		Elements table = document.select("table[style]");
		Elements trList = table.select("tr");
		trList.remove(0);
		trList.remove(0);
		System.out.println("====================获取table===================");
		System.out.println(table.size());
		System.out.println("====================获取tr==================");
		System.out.println(trList.size());
		String encoding = "utf8";
 		File stopfile = new File("D:/eclipseProject/IrStudy/src/main/java/com/blackmamba/IrStudy/english.stop2.txt");
 		ArrayList<String> stopList = new ArrayList<String>();
 		CharArraySet stopWords=null;
 		if (file.isFile() && file.exists()) {
 			InputStreamReader read = new InputStreamReader(new FileInputStream(stopfile), encoding);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String stopWord = null;
			while ((stopWord = bufferedReader.readLine()) != null)
				stopList.add(stopWord);
			read.close();
			stopWords = new CharArraySet(0, false);
			for(String word:stopList)
				stopWords.add(word);
 		}
 		CJKAnalyzer analyzer = new CJKAnalyzer(stopWords);
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriterConfig indexWriterConfig1 = new IndexWriterConfig(analyzer);
		indexWriterConfig1.setOpenMode(OpenMode.CREATE_OR_APPEND);
		Directory directory = null;
		Directory fileDirectory = null;
		IndexWriter indexWriter = null;
		IndexWriter fileIndexWriter = null;
		try {
			directory = FSDirectory.open(Paths.get("D://index/google"));
			fileDirectory = FSDirectory.open(Paths.get("D://index/file"));
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			fileIndexWriter = new IndexWriter(fileDirectory, indexWriterConfig1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Element element : trList) {
			String type = element.select("td").get(1).text();// 新闻类型
			String unit = element.select("td").get(2).text();// 发文单位
			String title = element.select("td").get(3).text();// 新闻标题
			title.replaceAll("[[#&;]]", "");
			String link = element.select("td").get(3).select("a").attr("href");// 新闻链接
			String time;
			if (element.select("td").size() > 5)
				time = element.select("td").get(5).text();// 发文时间
			else
				time = "2016-0-0";
			System.out.println(title.toLowerCase());//标题转换为小写
			Date date = sdf.parse(time);
			System.out.println(date);
			String strTime = date.getTime() + "";
			strTime = strTime.substring(0, 10);
			int sortTime = Integer.parseInt(strTime);
			Map<String, String> result = spiderArticle(link, fileIndexWriter, time, sortTime);
			org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
			doc.add(new StringField("type", type, Store.YES));
			doc.add(new StringField("unit", unit, Store.YES));
			doc.add(new StringField("title", title.toLowerCase(), Store.YES));//转小写
			doc.add(new StringField("time", time, Store.YES));
			doc.add(new StringField("link", link, Store.YES));
			doc.add(new StringField("content", result.get("body").toLowerCase(), Store.YES));//转小写
			doc.add(new StringField("clickNum", result.get("clickNum"), Store.YES));
			doc.add(new NumericDocValuesField("sortTime", sortTime));
			indexWriter.addDocument(doc);
			/*
			 * writer.write(type+"#"+unit+"#"+title+"#"+link+"#"+time);
			 * writer.newLine();
			 */
		}
		fileIndexWriter.commit();
		fileIndexWriter.close();
		fileDirectory.close();
		indexWriter.commit();
		// 关闭资源
		indexWriter.close();
		directory.close();

	}

	public static Map<String, String> spiderArticle(String param, IndexWriter fileIndexWriter, String time,
			int sortTime) throws IOException {

		Map<String, String> result = new HashMap<String, String>();
		String url = "" + param;
		Connection connection = Jsoup.connect(url);
		Document document = connection.timeout(50000).get();
		Element tb = document.select("table[cellpadding=4]").get(0);
		String tbody = tb.select("tbody").get(0).text();
		/*
		 * System.out.println("===========body=============");
		 * System.out.println(tbody);
		 */
		byte[] strByte = tbody.getBytes();
		if (strByte.length > 32700) {
			System.out.println(tbody.length());
			tbody = tbody.substring(0, 10000);

		}
		tbody.replaceAll("[#&;]", "");
		result.put("body", tbody.trim());

		Elements tr = tb.select("tbody").first().select("td");
		if (tr.size() > 1) {
			Element td = tr.get(tr.size() - 2);
			String txt = td.text();
			String[] strs = txt.split("点击数:");
			/*
			 * System.out.println(strs.length);
			 * System.out.println(strs[1].replace("）","").trim());
			 */
			if (strs.length > 1)
				result.put("clickNum", strs[1].replace("）", "").trim());
			else
				result.put("clickNum", "0");
		} else
			result.put("clickNum", "0");
		// 下面是分析附件部分
		Element table = document.select("table[cellpadding=4]").get(0).select("table").last();
		Elements aList = table.select("td").get(0).select("a");
		for (int i = 0; i < aList.size(); i++) {
			Element a = aList.get(i);
			System.out.println(a.attr("href"));
			String href = new String(URLEncoder.encode(a.attr("href"), "utf-8").getBytes());
			System.out.println(href);
			// Document
			// tempDoc=Jsoup.connect("http://www1.szu.edu.cn/board/"+href).get();
			URL url1 = new URL("http://www1.szu.edu.cn/board/" + href);
			String fileType = "";
			if (a.text().indexOf(".") != -1)
				fileType = a.text().substring(a.text().indexOf("."));
			try {

				if (fileType.equals(".xlsx")) {
					URLConnection conn = url1.openConnection();
					InputStream inStream = conn.getInputStream();
					System.out.println(a.text());
					FileOutputStream fs = new FileOutputStream("D:/tempFile/" + a.text());
					int bytesum = 0;
					int byteread = 0;
					byte[] buffer = new byte[1204];
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread;
						fs.write(buffer, 0, byteread);
					}
					fs.close();
					inStream.close();

					// System.out.println(fileType);
					InputStream is = new FileInputStream("D:/tempFile/" + a.text());
					XSSFWorkbook workbook = new XSSFWorkbook(is);
					XSSFSheet sheet = workbook.getSheetAt(0);
					String content = "";
					for (Row row : sheet) {
						for (Cell cell : row) {
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								content += cell.getRichStringCellValue().getString() + " ";
								// System.out.print(cell.getRichStringCellValue().getString());
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (DateUtil.isCellDateFormatted(cell)) {
									content += String.valueOf(cell.getDateCellValue()) + " ";
									// System.out.print(String.valueOf(cell.getDateCellValue()));
								} else {
									content += cell.getNumericCellValue() + " ";
									// System.out.print(cell.getNumericCellValue());
								}
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								content += cell.getBooleanCellValue() + " ";
								// System.out.print(cell.getBooleanCellValue());
								break;
							default:
							}
						}
					}
					org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
					doc.add(new StringField("title", a.text().toLowerCase(), Store.YES));
					doc.add(new StringField("content", content.toLowerCase(), Store.YES));
					doc.add(new StringField("link", url1.toString(), Store.YES));
					doc.add(new StringField("time", time, Store.YES));
					doc.add(new NumericDocValuesField("sortTime", sortTime));
					fileIndexWriter.addDocument(doc);

				} else if (fileType.equals(".xls")) {
					URLConnection conn = url1.openConnection();
					InputStream inStream = conn.getInputStream();
					System.out.println(a.text());
					FileOutputStream fs = new FileOutputStream("D:/tempFile/" + a.text());
					int bytesum = 0;
					int byteread = 0;
					byte[] buffer = new byte[1204];
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread;
						fs.write(buffer, 0, byteread);
					}
					fs.close();
					inStream.close();

					// System.out.println(fileType);
					InputStream is = new FileInputStream("D:/tempFile/" + a.text());
					POIFSFileSystem poiFs = new POIFSFileSystem(is);
					HSSFWorkbook hWorkbook = new HSSFWorkbook(poiFs);
					HSSFWorkbook wb = new HSSFWorkbook(poiFs);
					HSSFSheet sheet = wb.getSheetAt(0);
					String content = "";
					for (Row row : sheet) {
						for (Cell cell : row) {
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								// System.out.print(cell.getRichStringCellValue().getString());
								content += cell.getRichStringCellValue().getString();
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (DateUtil.isCellDateFormatted(cell)) {
									// System.out.print(String.valueOf(cell.getDateCellValue()));
									content += String.valueOf(cell.getDateCellValue());
								} else {
									// System.out.print(cell.getNumericCellValue());
									content += cell.getNumericCellValue();
								}
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								// System.out.print(cell.getBooleanCellValue());
								content += cell.getBooleanCellValue();
								break;
							default:
							}
						}
					}
					org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
					doc.add(new StringField("title", a.text().toLowerCase(), Store.YES));
					doc.add(new StringField("content", content.toLowerCase(), Store.YES));
					doc.add(new StringField("link", url1.toString(), Store.YES));
					doc.add(new StringField("time", time, Store.YES));
					doc.add(new NumericDocValuesField("sortTime", sortTime));
					fileIndexWriter.addDocument(doc);
				} else if (fileType.equals(".doc")) {
					URLConnection conn = url1.openConnection();
					InputStream inStream = conn.getInputStream();
					System.out.println(a.text());
					FileOutputStream fs = new FileOutputStream("D:/tempFile/" + a.text());
					int bytesum = 0;
					int byteread = 0;
					byte[] buffer = new byte[1204];
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread;
						fs.write(buffer, 0, byteread);
					}
					fs.close();
					inStream.close();

					// System.out.println(fileType);
					InputStream is = new FileInputStream("D:/tempFile/" + a.text());
					HWPFDocument doc = new HWPFDocument(is);
					// System.out.println(doc.getDocumentText());
					org.apache.lucene.document.Document doc1 = new org.apache.lucene.document.Document();
					doc1.add(new StringField("title", a.text().toLowerCase(), Store.YES));
					doc1.add(new StringField("content", doc.getDocumentText().toLowerCase(), Store.YES));
					doc1.add(new StringField("link", url1.toString(), Store.YES));
					doc1.add(new StringField("time", time, Store.YES));
					doc1.add(new NumericDocValuesField("sortTime", sortTime));
					fileIndexWriter.addDocument(doc1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return result;
	}

}
