package com.miao.sms;


import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SMSTest {
    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        //头文件中设置转码
        post.addRequestHeader("Content-Type",  "application/x-www-form-urlencoded;charset=gbk");
        NameValuePair[] data = {
                new NameValuePair("Uid", "taozi0126"),
                new NameValuePair("key", "743bab61780adb3lf5h"),
                new NameValuePair("smsMob", "15738121141"),
                new NameValuePair("smsText", "初冬的天气变化无常，如同小孩闹情绪。一会晴空万里，一会雨雪交加，" +
                        "一会寒风扬起。朋友赶快添加衣服保暖防寒，过个舒坦的冬天。愿你好身体幸福美满!")
        };
        post.setRequestBody(data);
        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        for(Header header : headers){
            System.out.println("----- : "+header.toString());
        }
        //服务器返回码
        System.out.println("++++++ : "+statusCode);
        //短信返还给客户端返回码
        System.out.println("result : "+result);
    }
}
