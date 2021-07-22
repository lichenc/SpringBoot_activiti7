package com.example.tquan.controller;

import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.IamOauthEntity;
import com.example.tquan.entity.IamOrgEntity;
import com.example.tquan.entity.OrgEntity;
import com.example.tquan.util.IamInterface;
import com.ninghang.core.util.StringUtils;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrgController {
    @Autowired
    private IamOauthEntity iam;
    @Autowired
    private IamOrgEntity org;
    private Log log = LogFactory.getLog(getClass());
    IamInterface iamInterface = new IamInterface();

    /**
     * 获取组织树
     *
     * @param
     * @return
     */
    @RequestMapping("/org")
    @ResponseBody
    public  List<Map> orgSelect(String uuid) throws Exception {
        List<Map> list = new ArrayList<Map>();
        //获取组织信息
        String ifo = iamInterface.oauth(uuid, iam.getKey(), iam.getPassword(), iam.getAddr(), iam.getUsername(), iam.getType(), iam.getCharset());
        if (StringUtils.isEmpty(ifo)) {
            log.info("uuid为空！");
        } else {
            List<NameValuePair> paramAccount = Lists.newArrayList();
            //调用统权的接口，获取账号信息
            paramAccount.add(new BasicNameValuePair("uim-login-user-id", ifo));
            String orgStr = EntityUtils.toString(new UrlEncodedFormEntity(paramAccount, Consts.UTF_8));
            StringBuilder ifOrg = iamInterface.orgSelect(orgStr, org.getAddr(), org.getType());
            JSONArray orgArr = JSONArray.fromObject(ifOrg.toString());
            for (int o = 0; o < orgArr.size(); o++) {
                JSONObject orgJson = JSONObject.fromObject(orgArr.get(o).toString());
                String idPath = orgJson.get("idPath").toString();
                String id = orgJson.get("id").toString();
                int num = idPath.split("/", -1).length - 1;
                if (num == 1) {
                    Map orgEntity = new HashMap();
                    orgEntity.put("title",orgJson.get("name").toString());
                    orgEntity.put("id",orgJson.get("id").toString());
                    orgEntity.put("children",org(orgArr, id,num));
                    list.add(orgEntity);
                }

            }
        }
        return list;
    }

    public List<Map> org(JSONArray orgArr, String id,int num) {
        List<Map> list = new ArrayList<>();
        List<Map> list2 = new ArrayList<>();
        for (int i = 0; i < orgArr.size(); i++) {
            JSONObject orgJson2 = JSONObject.fromObject(orgArr.get(i).toString());
            String idPath2 = orgJson2.get("idPath").toString();
            String id2 = orgJson2.get("id").toString();
            int num2 = idPath2.split("/", -1).length - 1;
            Map orgEntity2 = new HashMap();
            if (num2 == num+1) {
                String parentId2 = idPath2.substring(idPath2.lastIndexOf("/", idPath2.lastIndexOf("/") - 1) + 1, idPath2.lastIndexOf("/", idPath2.lastIndexOf("/")));
                if (id.equals(parentId2)) {
                    orgEntity2.put("title",orgJson2.get("name").toString());
                    orgEntity2.put("id",orgJson2.get("id").toString());
                    if (num==10){
                        return list;
                    }
                    orgEntity2.put("children",org(orgArr, id2,num+1));
                    list2.add(orgEntity2);
                }

            }
        }
        return list2;

    }


}