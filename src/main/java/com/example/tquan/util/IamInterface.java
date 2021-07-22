package com.example.tquan.util;

import com.beust.jcommander.internal.Lists;
import net.sf.json.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.List;

public class IamInterface {
    /**
     * iam认证
     * @return
     * @throws Exception
     */
    public String oauth (String uuid,String key,String pwd,String addr,String user,String type,String charset)
            throws Exception {
        PublicKey publicKey = RsaUtil.string2PublicKey(key);
        //用公钥加密
        byte[] publicEncrypt = RsaUtil.publicEncrypt(pwd.getBytes(), publicKey);
        //加密后的内容为Base64编码
        String rsa = RsaUtil.byte2Base64(publicEncrypt);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr);
        StringBuilder result = new StringBuilder();
        //组装数据
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", user));
        params.add(new BasicNameValuePair("password", rsa));
        //转换为键值对
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type + charset);
        HttpResponse response;
        response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    instream));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                String str2 = new String(temp.getBytes(), "utf-8");
                result.append(str2).append("\r\n");
            }
            br.close();
            JSONObject resultJson = JSONObject.fromObject(result.toString());
            if (resultJson.get("success").toString().equals("true")) {
                System.out.println("获取到的uid信息,msg:" + resultJson.get("msg"));
                uuid = resultJson.get("msg").toString();
                return uuid;
            } else {
                System.out.println("获取到的uid信息,msg:" + resultJson.get("msg"));
                return uuid;
            }
        }
        return uuid;
    }


    /**
     * 账号添加
     */

    public String accountSave (String str, String addr, String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr+"save");
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                br.close();
                JSONObject resultJson = JSONObject.fromObject(result.toString());
                if (resultJson.get("success").toString().equals("true")) {


                    return resultJson.get("msg").toString();
                } else {

                    return resultJson.get("msg").toString();
                }
            }
        } catch (ClientProtocolException e) {

        }
        return null;
    }
    /**
     * 账号修改
     */

    public String accountUpdate (String str, String addr, String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr+"edit");
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                br.close();
                JSONObject resultJson = JSONObject.fromObject(result.toString());
                if (resultJson.get("success").toString().equals("true")) {


                    return resultJson.get("msg").toString();
                } else {

                    return resultJson.get("msg").toString();
                }
            }
        } catch (ClientProtocolException e) {

        }
        return null;
    }
    /**
     * 账号启动
     */

    public String accountStart (String str, String addr, String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr+"enabled");
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                br.close();
                JSONObject resultJson = JSONObject.fromObject(result.toString());
                if (resultJson.get("success").toString().equals("true")) {


                    return resultJson.get("msg").toString();
                } else {

                    return resultJson.get("msg").toString();
                }
            }
        } catch (ClientProtocolException e) {

        }
        return null;
    }

    /**
     * 根据应用和帐号查询
     * @return
     */
    /*  @RequestMapping("/accountOpen")*/
    public StringBuilder accountSelect (String strAccount,String addr,String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr+"findList");
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(strAccount, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                if (br!=null){
                    br.close();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据帐号ID查询
     * @return
     */
    /*  @RequestMapping("/accountOpen")*/
    public StringBuilder accountSelectId (String strAccount,String addr,String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr+"findById");
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(strAccount, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                if (br!=null){
                    br.close();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 用户查询
     * @return
     */
    /*  @RequestMapping("/accountOpen")*/
    public StringBuilder userSelect (String strUser,String addr,String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr);
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(strUser, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                if (br!=null){
                    br.close();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 用户更新
     */
    /*  @RequestMapping("/accountOpen")*/
    public void userUpdate (String str,String addr,String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr);
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                br.close();
                JSONObject resultJson = JSONObject.fromObject(result.toString());
                if (resultJson.get("success").toString().equals("true")) {
                    System.out.println("======= 新增账号结果：msg:" + resultJson.get("msg"));
                    /* return new ResultCode(SUCCESS,resultJson.get("message").toString());*/
                } else {
                    System.out.println("======= 新增账号结果：" + resultJson.get("msg"));
                    /* return new ResultCode(FAIL,resultJson.get("message").toString());*/
                }
            }
        } catch (ClientProtocolException e) {
            /* return new ResultCode(FAIL, e.getMessage());*/
        } catch (IOException e) {
            /*return new ResultCode(FAIL, e.getMessage());*/
        }
        /* return new ResultCode(FAIL, "新增账号请求失败");*/
    }

    /**
     * 组织查询
     * @return
     */
    /*  @RequestMapping("/accountOpen")*/
    public StringBuilder orgSelect (String strOrg,String addr,String type) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addr);
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(strOrg, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", type);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                if (br!=null){
                    br.close();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
