package com.zto.testcase.controller.sysmanage;

import com.zto.testcase.dto.TcFileDto;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.FileUploadService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wxc
 * @Description:文件相关接口
 * @Date: Create in 上午9:46 2021/3/16
 */
@RequestMapping("/file")
@Api(value = " 文件相关 接口文档")
@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Resource
    FileUploadService fileUploadService;

    /***
     * 上传文件
     * @param file
     * @param tcFileDto
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(@RequestParam("file")MultipartFile file, TcFileDto tcFileDto){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>upload request : {}",tcFileDto);
            result = Result.success(fileUploadService.upload(file,null,tcFileDto));
            logger.info(">>>>>upload request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>upload request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    /***
     * 上传文件
     * @param files
     * @param tcFileDto
     * @return
     */
    @RequestMapping(value = "/batchUpload", method = RequestMethod.POST)
    public Result uploadFiles(@RequestParam("files")MultipartFile[] files, TcFileDto tcFileDto){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>batchUpload request : {}",tcFileDto);
            result = Result.success(fileUploadService.uploadFiles(files,null,tcFileDto));
            logger.info(">>>>>batchUpload request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>batchUpload request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }


    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public Result download(@RequestParam Integer id,HttpServletResponse response){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>download request : {}",id);
            result = Result.success(fileUploadService.download(response,id));
            logger.info(">>>>>download request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>download request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }
    /***
     * 下载
     * @param id
     * @return 直接根据id返回路径
     */
    @RequestMapping(value = "/getDownloadPath", method = RequestMethod.GET)
    public Result downloadPath(@RequestParam Integer id){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>download request : {}",id);
            result = Result.success(fileUploadService.download(id));
            logger.info(">>>>>download request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>download request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    /***
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result delete(@RequestParam Integer id){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>delete request : {}",id);
            result = Result.success(fileUploadService.delete(id));
            logger.info(">>>>>delete request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>delete request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    /***
     * 上传用例导入模板-excel
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadTemplate", method = RequestMethod.POST)
    public Result uploadTemplate(@RequestParam("file")MultipartFile file, @RequestParam Integer type){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>uploadTemplate request : {}",type);
            result = Result.success(fileUploadService.uploadTemplate(file,type));
            logger.info(">>>>>uploadTemplate request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>uploadTemplate request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }


    /***
     * 用例导出模板下载-excel
     * @param response
     * @return
     */
    @RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
    public Result downloadTemplate(HttpServletResponse response,@RequestParam Integer type){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>downloadTemplate request : {}",type);
            result = Result.success(fileUploadService.downloadTemplate(response,type));
            logger.info(">>>>>downloadTemplate request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>downloadTemplate request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }


    /***
     * 导入excel
     * @param file
     * @param
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result importExcel(@RequestParam("file")MultipartFile file, @RequestParam String user){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>importExcel request : {}",user);
            result = Result.success(fileUploadService.importExcel(file,user));
            logger.info(">>>>>importExcel request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>importExcel request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), e.getMessage());
        }
        return result;
    }

    /***
     * 导入XMind
     * @param file
     * @param
     * @return
     */
    @RequestMapping(value = "/importXmind", method = RequestMethod.POST)
    public Result importXmind(@RequestParam("file")MultipartFile file, @RequestParam String user){
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>importXMind request : {}",user);
            result = Result.success(fileUploadService.importXMind(file,user));
            logger.info(">>>>>importXMind request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>importXMind request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), e.getMessage());
        }
        return result;
    }
}
