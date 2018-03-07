package com.wenyiguai.monstermusic.controller;

import com.wenyiguai.monstermusic.utils.AES;
import com.wenyiguai.monstermusic.utils.Util;
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
    private Util util = new Util();

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public String search(String s, Integer pages, Integer limit){
        String response = null;
        try {
            String url = "http://music.163.com/weapi/search/get?csrf_token=";
            String first_param = "{\"s\":\"" + s + "\",\"type\":\"1\"" + ",\"limit\":\"" + limit + "\",\"offset\":\"" + limit*(pages - 1) +  "\",\"csrf_token\":\"\"}";
            String params = "params=" + URLEncoder.encode(AES.get_params(first_param), "UTF-8") + "&encSecKey=" + AES.get_encSecKey();
            response = util.createWebAPIRequest(url, params);
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
            String params = "params=" + URLEncoder.encode(AES.get_params(first_param), "UTF-8") + "&encSecKey=" + AES.get_encSecKey();
            response =  util.createWebAPIRequest( url, params);
        }catch (Exception e){
            logger.error(e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/playlist/detail", method = RequestMethod.GET)
    @ResponseBody
    public String playList(String id){
        String response = null;
        try {
            String url = "http://music.163.com/weapi/v3/playlist/detail?csrf_token=";
            String first_param = "{\"id\":" + id + ",\"offset\":0" + ",\"total\":true" + ",\"limit\":1000" + ",\"n\":1000" + ",\"csrf_token\":\"\"}";
            String params = "params=" + URLEncoder.encode(AES.get_params(first_param), "UTF-8") + "&encSecKey=" + AES.get_encSecKey();
            response = util.createWebAPIRequest(url, params);
        }catch (Exception e){
            logger.error(e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    @RequestMapping(value = "/playlist/highquality")
    public String playListHighquality(Integer pages, Integer limit){
        String response = null;
        try {
            String url = "http://music.163.com/weapi/playlist/highquality/list";
            String first_param = "{cat:全部,offset:"+(pages-1)*limit + ",limit:" + limit + "csrf_token:''}";
            String params = "params=" + URLEncoder.encode(AES.get_params(first_param),"UTF-8") + "&encSecKey" + AES.get_encSecKey();
            response = util.createWebAPIRequest(url,params);
        }catch (Exception e){
            logger.error(e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

}
