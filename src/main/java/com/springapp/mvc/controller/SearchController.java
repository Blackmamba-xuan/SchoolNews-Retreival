package com.springapp.mvc.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.springapp.mvc.common.Result;
import com.springapp.mvc.common.SearchCache;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SearchController {

	private Directory directory=FSDirectory.open(Paths.get("D://index/google"));
	private Directory fileDirectory=FSDirectory.open(Paths.get("D://index/file"));
    private Map<String,SearchCache> cacheResult=new HashMap<>();//cacheMap缓存

	public SearchController() throws IOException {
	}

	@RequestMapping(value="/hello")
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}

	@RequestMapping(value="/index")
	public String showIndex(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "index";
	}

	@RequestMapping(value="/search")
	public String showSearch(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "search";
	}

	@RequestMapping(value="/result")
	public String showResult(ModelMap model, @RequestParam("key") String key, HttpSession httpSession) throws IOException {
		if(cacheResult.containsKey(key)){
            System.out.println("Cache 命中");
            long startTime=System.currentTimeMillis();
            SearchCache resultCache=cacheResult.get(key);
            resultCache.setSortTime(new Date());//更新最近访问时间
            model.addAttribute("length",resultCache.getDocList().size());
            model.addAttribute("pageNum",resultCache.getDocList().size()/10);
            model.addAttribute("docList",resultCache.getDocList());
            model.addAttribute("key",key);
            long endTime=System.currentTimeMillis();
            model.addAttribute("time",(endTime-startTime));
            httpSession.setAttribute("result",resultCache.getJsonArray());
            httpSession.setAttribute("fileResult",resultCache.getFileArrary());
            return "result";
        }else{
            JsonArray jsonArray=new JsonArray();
            Query query1 = new WildcardQuery(new Term("content","*"+key.toLowerCase()+"*"));
            Query query2 = new WildcardQuery(new Term("title","*"+key.toLowerCase()+"*"));
            int hitsPerPage = 100;
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            BooleanQuery booleanQuery = new BooleanQuery.Builder()
                    .add(query1, BooleanClause.Occur.SHOULD)
                    .add(query2, BooleanClause.Occur.SHOULD).build();
            long startTime=System.currentTimeMillis();
            TopDocs docs = searcher.search(booleanQuery,hitsPerPage);
            long endTime=System.currentTimeMillis();
            ScoreDoc[] hits = docs.scoreDocs;
            System.out.println("Found " + hits.length + " hits.");
            List<Result> docList=new ArrayList<Result>();
            for(int i=0;i<hits.length;i++){
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println("docId:"+docId+" time: "+d.get("time"));
                String content=d.get("content");
                String[] anchor=content.split(key);
                if(anchor.length==1){
                    if(anchor[0].length()>100)
                        anchor[0]=anchor[0].substring(anchor[0].length()-100, anchor[0].length());
                    Result result=new Result(d.get("title"),d.get("type"),d.get("unit"),d.get("link"),anchor[0].trim(),"...",d.get("time"),d.get("clickNum"),hits[i].score);
                    docList.add(result);
                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("title",d.get("title"));
                    jsonObject.addProperty("type",d.get("type"));
                    jsonObject.addProperty("unit",d.get("unit"));
                    jsonObject.addProperty("link",d.get("link"));
                    jsonObject.addProperty("anchor1",anchor[0].trim());
                    jsonObject.addProperty("anchor2","...");
                    jsonObject.addProperty("key",key);
                    jsonObject.addProperty("time",d.get("time"));
                    jsonObject.addProperty("clickNum",d.get("clickNum"));
                    jsonArray.add(jsonObject);
                }else{
                    if(anchor[0].length()>50)
                        anchor[0]=anchor[0].substring(anchor[0].length()-50, anchor[0].length());
                    if(anchor.length>1&&anchor[1].length()>100)
                        anchor[1]=anchor[1].substring(0, 100);
                    Result result=new Result(d.get("title"),d.get("type"),d.get("unit"),d.get("link"),anchor[0],anchor[1],d.get("time"),d.get("clickNum"),hits[i].score);
                    docList.add(result);
                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("title",d.get("title"));
                    jsonObject.addProperty("type",d.get("type"));
                    jsonObject.addProperty("unit",d.get("unit"));
                    jsonObject.addProperty("link",d.get("link"));
                    jsonObject.addProperty("anchor1",anchor[0]);
                    jsonObject.addProperty("anchor2",anchor[1]);
                    jsonObject.addProperty("key",key);
                    jsonObject.addProperty("time",d.get("time"));
                    jsonObject.addProperty("clickNum",d.get("clickNum"));
                    jsonArray.add(jsonObject);
                }

                System.out.println((i + 1) + ". " + d.get("title") + "\t" + d.get("time")+"score: "+hits[i].score);
            }
            //下面是搜索附件部分
            JsonArray fileArrary=new JsonArray();
            IndexReader filereader = DirectoryReader.open(fileDirectory);
            IndexSearcher fileSearcher = new IndexSearcher(filereader);
            Query filequery = new WildcardQuery(new Term("content","*"+key+"*"));
            Query filequery2 = new WildcardQuery(new Term("title","*"+key+"*"));
            BooleanQuery booleanQuery2 = new BooleanQuery.Builder()
                    .add(filequery, BooleanClause.Occur.SHOULD)
                    .add(filequery2,BooleanClause.Occur.SHOULD)
                    .build();
            TopDocs docs2 = fileSearcher.search(booleanQuery2,50);
            ScoreDoc[] filehits = docs2.scoreDocs;
            System.out.println("Found file hits" + filehits.length);

            for(int i=0;i<filehits.length;i++){
                int docId=filehits[i].doc;
                Document d=fileSearcher.doc(docId);
                String content=d.get("content");
                String[] anchor=content.split(key);
                JsonObject object=new JsonObject();
                object.addProperty("title",d.get("title"));
                object.addProperty("key",key);
                if(anchor.length==1){
                    if(anchor[0].length()>100)
                        anchor[0]=anchor[0].substring(anchor[0].length()-100, anchor[0].length());
                    object.addProperty("anchor1",anchor[0]);
                    object.addProperty("anchor2","...");
                    object.addProperty("link",d.get("link"));
                } else{
                    if(anchor[0].length()>50)
                        anchor[0]=anchor[0].substring(anchor[0].length()-50, anchor[0].length());
                    if(anchor.length>1&&anchor[1].length()>100)
                        anchor[1]=anchor[1].substring(0, 100);
                    object.addProperty("anchor1",anchor[0]);
                    object.addProperty("anchor2",anchor[1]);
                    object.addProperty("link",d.get("link"));
                }
                fileArrary.add(object);
            }
            model.addAttribute("length",docList.size());
            model.addAttribute("pageNum",docList.size()/10);
            model.addAttribute("docList",docList);
            model.addAttribute("key",key);
            model.addAttribute("time",(endTime-startTime));
            httpSession.setAttribute("result",jsonArray);
            httpSession.setAttribute("fileResult",fileArrary);
            Date date=new Date();
            SearchCache searchCache=new SearchCache(key,docList.size(),docList.size()/10,docList,jsonArray,fileArrary,date);
            if(cacheResult.size()<20)
                cacheResult.put(key,searchCache);
            else{
                List<Map.Entry<String, SearchCache>> list = new LinkedList<Map.Entry<String, SearchCache>>(cacheResult.entrySet());
                //对缓存结果按最近访问时间进行排序
                Collections.sort(list, new Comparator<Map.Entry<String, SearchCache>>() {
                    public int compare(Map.Entry<String, SearchCache> o1,
                                       Map.Entry<String, SearchCache> o2) {
                        if(o1.getValue().getSortTime().getTime()<o2.getValue().getSortTime().getTime())
                            return -1;
                        else if(o1.getValue().getSortTime().getTime()>o2.getValue().getSortTime().getTime())
                            return 1;
                        else
                            return 0;
                    }
                });
                list.remove(0);//移除最近最久未被访问的元素
                cacheResult=new LinkedHashMap<String, SearchCache>();
                for (Map.Entry<String, SearchCache> entry : list) {
                    cacheResult.put(entry.getKey(), entry.getValue());
                }
                cacheResult.put(key,searchCache);
            }
            return "result";
        }
	}

}