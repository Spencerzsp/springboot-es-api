package com.bigdata.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @ description: 测试获取接口数据
 * @ author: spencer
 * @ date: 2020/9/24 15:50
 */
public class GetInterfaceData {
//    private static final LOGER = LoggerFactory.getLogger(GetInterfaceData.class)

    private static final Logger LOGGER = LoggerFactory.getLogger(GetInterfaceData.class);
    public static void main(String[] args) throws IOException {

//        System.out.println(getData());

        LOGGER.info(getData());
    }

    /**
     * 获取接口数据
     * @return
     * @throws IOException
     */
    public static String getData() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://localhost:8080/search";
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        HttpEntity httpEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpEntity);

//        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray jsonArray = JSONObject.parseArray(result);
//        System.out.println(jsonArray.getString(1));

        for (int i = 0; i < jsonArray.size(); i++) {
            String jsonArrayString = jsonArray.getString(i);
            JSONObject jsonObject = JSONObject.parseObject(jsonArrayString);
            String name = jsonObject.getString("name");

            System.out.println(name);
        }

        return result;
    }
}
