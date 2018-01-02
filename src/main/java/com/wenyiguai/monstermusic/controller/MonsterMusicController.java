package com.wenyiguai.monstermusic.controller;

import com.sun.istack.internal.Nullable;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * Create by FirsTree on 2017/12/28
 */
@Controller
@RequestMapping(value = "/mosterMusic")
public class MonsterMusicController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public String search(String keywords, @Nullable String count, @Nullable String page){
        String url = "http://music.163.com/api/search/suggest/web";
        HttpHeaders headers =  new HttpHeaders();
        headers.set("Origin", "http://music.163.com");
        headers.set("Referer", "http://music.163.com/");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        params.add("s", keywords);
        params.add("limit", count);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        String result = restTemplate.postForObject(url, requestEntity, String.class);
        return result;
    }

    @RequestMapping(value = "lyric", method = RequestMethod.GET)
    public String lyric(String id){
        String url  = "http://music.163.com/api/song/lyric?id="+ id + "&lv=-1";
        HttpHeaders headers =  new HttpHeaders();
        headers.set("Origin", "http://music.163.com");
        headers.set("Referer", "http://music.163.com/");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    public String musicUrl(String id){
        String url = "http://music.163.com/weapi/song/enhance/player/url?csrf_token=";
        HttpHeaders headers =  new HttpHeaders();
        headers.set("Origin", "http://music.163.com");
        headers.set("Referer", "http://music.163.com/");
        headers.set("Charset", "UTF-8");
        headers.set("Accept-Language", "q=0.8,zh-CN;q=0.6,zh;q=0.2");
        headers.set("Cookie", "os=uwp;");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String first_param = "{\"ids\":\"[" + id + "]\",\"br\":192000" + ",\"csrf_token\":\"\"}";

        return null;
    }



}
