package com.example.tquan.controller;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.RepositoryService;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by chenjin on 2021/7/12 10:59
 */
@Controller
public class FileUploadController {


    private Log log = LogFactory.getLog(getClass());

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 上传流程图
     * @param uploadFile
     * @param req
     * @return
     */
    @PostMapping("/procdefUpload")
    public String procdefUpload(MultipartFile uploadFile, HttpServletRequest req){


        try{
            if (uploadFile!=null){
                //获取路径
                String realPath=getUploadPath();
                //获取文件名
                String oldName = uploadFile.getOriginalFilename();
                String name=oldName.substring(oldName.length()-4);
                //判断是不是xml文件
                if (name.equals(".xml")){
                    uploadFile.transferTo(new File(realPath,oldName));
                    String fileName=realPath+"\\"+oldName;
                    fileName=fileName.replace("\\","/");
                    oldName=oldName.replace(".xml",".bpmn");
                    if(fileName!=null && fileName!=""){
                        //读取资源作为一个输入流
                        FileInputStream bpmnfileInputStream = new FileInputStream(fileName);
                        RepositoryService repositoryService = processEngine.getRepositoryService();
                        DeploymentBuilder builder = repositoryService.createDeployment();
                        builder.addInputStream(oldName,bpmnfileInputStream);
                        builder.deploy();
                    }
                    req.setAttribute("info","200");
                    return "/procdefPage";
                }else{
                    log.info("==========================请上传xml文件！");
                }
            }else {
                log.info("==========================上传文件为空！");
            }

        }catch (Exception e){
            e.printStackTrace();
            req.setAttribute("info","500");
        }

        return "/procdefPage";
    }
    /**
     * 获取当前系统路径
     */
    private String getUploadPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());//默认是target/classes/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "static/bpmn/");
        if (!upload.exists()) upload.mkdirs();
        return upload.getAbsolutePath();
    }


}
