package com.wenyiguai.monstermusic.controller;

import com.wenyiguai.monstermusic.utils.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Create by FirsTree on 2017/12/28
 */
@Controller
@RequestMapping(value = "/monsterMusic")
public class MonsterMusicController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    @ResponseBody
//    public String search(String keywords, @Nullable String count, @Nullable String page){
//        String url = "http://music.163.com/api/search/suggest/web";
//        HttpHeaders headers =  new HttpHeaders();
//        headers.set("Origin", "http://music.163.com");
//        headers.set("Referer", "http://music.163.com/");
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
//        params.add("s", keywords);
//        params.add("limit", count);
//        params.add("type", "1");
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//        String result = restTemplate.postForObject(url, requestEntity, String.class);
//        return result;
//    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public String search(String s, Integer pages, Integer limit){
        String response = null;
        try {
            String url = "http://music.163.com/weapi/search/get?csrf_token=";
            String first_param = "{\"s\":\"" + s + "\",\"type\":\"1\"" + ",\"limit\":\"" + limit + "\",\"offset\":\"" + limit*(pages - 1) +  "\",\"csrf_token\":\"\"}";
            response =  getResponse( url,"params=" + URLEncoder.encode(AES.get_params(first_param), "UTF-8") + "&encSecKey=" + AES.get_encSecKey());
        }catch (Exception e){
            logger.error(e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/lyric", method = RequestMethod.GET)
    @ResponseBody
    public String lyric(String id){
        String url  = "http://music.163.com/api/song/lyric?id="+ id + "&lv=-1";
        HttpHeaders headers =  new HttpHeaders();
        headers.set("Origin", "http://music.163.com");
        headers.set("Referer", "http://music.163.com/");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    @RequestMapping(value = "/musicUrl", method = RequestMethod.GET)
    @ResponseBody
    public String musicUrl(String id){
        String response = null;
        try {
            String url = "http://music.163.com/weapi/song/enhance/player/url?csrf_token=";
            String first_param = "{\"ids\":\"[" + id + "]\",\"br\":192000" + ",\"csrf_token\":\"\"}";
            response =  getResponse( url,"params=" + URLEncoder.encode(AES.get_params(first_param), "UTF-8") + "&encSecKey=" + AES.get_encSecKey());
        }catch (Exception e){
            logger.error(e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/playlist", method = RequestMethod.POST)
    @ResponseBody
    public String playList(String id){
        String response = null;
        try {
            String url = "http://music.163.com/weapi/v3/playlist/detail?csrf_token=";
            String first_param = "{\"id\":" + id + ",\"offset\":0" + ",\"total\":true" + ",\"limit\":1000" + ",\"n\":1000" + ",\"csrf_token\":\"\"}";
//            String first_param = "{\"id\":\"" + id + "\",\"offset\":\"0\"" + ",\"total\":\"True\"" + ",\"limit\":\"1000\"" + ",\"n\":\"1000\"" + ",\"csrf_token\":\"\"}";
            response = getResponse(url, "params=" + URLEncoder.encode(AES.get_params(first_param), "UTF-8") + "&encSecKey=" + AES.get_encSecKey());
        }catch (Exception e){
            logger.error(e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    private String getResponse( String uri , String param) {
        String response = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Referer", "http://music.163.com/");
            conn.setRequestProperty("Host", "music.163.com");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Accept-Language", "q=0.8,zh-CN;q=0.6,zh;q=0.2");
            conn.setRequestProperty("Cookie", "os=uwp;");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(param.getBytes());
            outputStream.flush();
            outputStream.close();
            BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
            byte[] bytes = new byte[512];
            int len = -1;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            inputStream.close();
            response = sb.toString();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return response;
    }

}
