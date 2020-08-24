package com.bigdata.es.utils;

import com.bigdata.es.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @ author spencer
 * @ date 2020/6/17 14:25
 */
public class HtmlParseUtil {

    public static void main(String[] args) throws Exception {

        parseJD("elasticsearch").forEach(System.out::println);
    }

    public static List<Content> parseJD(String keyword) throws Exception {
        String url = "https://search.jd.com/Search?keyword=" + keyword;

        //jsoup解析网页，返回就是js页面的Document对象
        Document document = Jsoup.parse(new URL(url), 3000);
        Element element = document.getElementById("J_goodsList");
//        System.out.println(element.html());

        List<Content> contents = new ArrayList<>();

        Elements elements = document.getElementsByTag("li");
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String name = el.getElementsByClass("p-name").eq(0).text();
            String price = el.getElementsByClass("p-price").eq(0).text();

            Content content = new Content();
            if (img != "" && name != "" && price != ""){
                content.setImg(img);
                content.setName(name);
                content.setPrice(price);
                contents.add(content);
            }

        }
        return contents;
    }
}
