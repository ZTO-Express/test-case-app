package com.zto.testcase.service.impl;

import com.zto.testcase.dto.TcFileDto;
import com.zto.testcase.dto.TcPlanCaseListDTO;
import com.zto.testcase.enums.TestCasePriorityEnum;
import com.zto.testcase.mapper.testcase.TcFileMapper;
import com.zto.testcase.mapper.testcase.TcPlanCaseResultMapper;
import com.zto.testcase.mapper.testcase.TcTestCaseModuleMapper;
import com.zto.testcase.model.ExcelColumn;
import com.zto.testcase.model.TcCaseType;
import com.zto.testcase.model.TcFile;
import com.zto.testcase.model.TcTestCaseModule;
import com.zto.testcase.model.TcTestcase;
import com.zto.testcase.model.TcTestcaseStep;
import com.zto.testcase.service.FileUploadService;
import com.zto.testcase.service.ModuleService;
import com.zto.testcase.service.TestCaseService;
import com.zto.testcase.service.TestConfigService;
import com.zto.testcase.util.ExcelUtils;
import com.zto.testcase.util.FileUploadUtils;
import com.zto.testcase.util.FileUtils;
import com.zto.testcase.util.RegexUtils;
import com.zto.testcase.util.XMindUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.exception.ExtCertPathBuilderException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: wxc
 * @Description:文件上传服务实现类
 * @Date: Create in 上午9:51 2021/3/16
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    TcFileMapper tcFileMapper;

    @Resource
    TcTestCaseModuleMapper tcTestCaseModuleMapper;

    @Resource
    ModuleService moduleService;

    @Resource
    TestCaseService testCaseService;

    @Resource
    TestConfigService testConfigService;

    @Resource
    TcPlanCaseResultMapper tcPlanCaseResultMapper;


    @Override
    public TcFile upload(MultipartFile file, String baseDir, TcFileDto tcFileDto) throws Exception {
        //上传文件不能为空
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || "".equals(originalFileName)) {
            throw new Exception("上传文件不能为空");
        }

        String fileLocation;
        //未指定baseDir上传默认地址
        if (baseDir != null) {
            fileLocation = FileUploadUtils.upload(baseDir, file);
        } else {
            fileLocation = FileUploadUtils.upload(file);
        }
        TcFile tcFile = new TcFile();
        tcFile.setName(originalFileName);
        tcFile.setPath(fileLocation);
        tcFile.setCreateTime(new Date());
        tcFile.setUpdateTime(new Date());
        tcFile.setCreateUser(tcFileDto.getUser());
        tcFile.setUpdateUser(tcFileDto.getUser());
        tcFile.setType(tcFileDto.getType());
        //如果type=2.则是用例执行结果附件，需要取tc_plan_case_result表的ID，
        if(tcFileDto.getType()==2){
            //获取ID,传planID和caseId---同一条用例可以加到多个plan里面，两个条件锁定唯一
//            TcPlanCaseListDTO castInfo=tcPlanCaseResultMapper.getCaseByPlanIdAndCaseId(tcFileDto.getPlanId(),tcFileDto.getCaseId());
            tcFile.setRelationId(tcFileDto.getPlanCaseId());
        }else{
            tcFile.setRelationId(tcFileDto.getCaseId());
        }

        tcFileMapper.insertAndGetId(tcFile);
        return tcFile;
    }

    @Override
    public List<TcFile> uploadFiles(MultipartFile[] files, String baseDir, TcFileDto tcFileDto) throws Exception {
        //限制一次最多上传5个文件
        if (files.length > 5) {
            throw new Exception("批量上传文件数量最多为5个");
        }
        List<TcFile> list = new ArrayList<>();
        for (MultipartFile file : files) {
            TcFile tcFile = upload(file, null, tcFileDto);
            list.add(tcFile);
        }
        return list;
    }

    @Override
    public String download(HttpServletResponse response, Integer id) throws IOException {
        TcFile tcFile = tcFileMapper.selectByPrimaryKey(id);
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");        //这边可以设置文件下载时的名字，我这边用的是文件原本的名字，可以根据实际场景设置
        //尝试解决中文乱码，chrome下载可以，但是postman还是乱码
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(tcFile.getName(), "UTF-8"));
        FileUtils.writeBytes(tcFile.getPath(), response.getOutputStream());
        return null;
    }

    @Override
    public TcFile download(Integer id) {
        TcFile tcFile = tcFileMapper.selectByPrimaryKey(id);
        return tcFile;
    }

    @Override
    public String delete(Integer id) {
        TcFile tcFile = tcFileMapper.selectByPrimaryKey(id);
        log.info("download获取数据库实体==" + tcFile);
        String path = tcFile.getPath();
        FileUtils.deleteFile(path);
        tcFileMapper.deleteByPrimaryKey(id);
        return null;
    }

    @Override
    public String uploadTemplate(MultipartFile file, Integer type) throws Exception {
        String fileName = "";
        if (type != null && type == 1) {
            fileName = "ExcelTemplate";
        } else if (type != null && type == 2) {
            fileName = "XmindTemplate";
        } else {
            throw new Exception("type错误！");
        }
        String fileLocation = FileUploadUtils.upload(file, fileName);
        return fileLocation;
    }

    @Override
    public String downloadTemplate(HttpServletResponse response, Integer type) throws Exception {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");        //这边可以设置文件下载时的名字，我这边用的是文件原本的名字，可以根据实际场景设置
        //尝试解决中文乱码，chrome下载可以，但是postman还是乱码
        String fileName = "";
        if (type != null && type == 1) {
            fileName = "ExcelTemplate.xlsx";
        } else if(type != null && type == 2){
            fileName = "XmindTemplate.xmind";
        }else{
            throw new Exception("type错误！！！");
        }

        String filePath = FileUploadUtils.DEFAULT_BASE_FILE + File.separator + fileName;
        log.info("filePath==" + filePath);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        FileUtils.writeBytes(filePath, response.getOutputStream());
        return null;
    }

    @Override
    public String importExcel(MultipartFile file, String user) throws Exception {
        List<ExcelColumn> lists = ExcelUtils.getList(file.getInputStream(), file.getOriginalFilename());
        log.info("-----------excel转化成list==" + lists);
        //校验list是否为空
        if (null == lists || lists.size() == 0) {
            throw new Exception("请检查数据是否为空");
        }
        //用例导入
        caseImport(lists, user);
        return "导入成功！";
    }

    @Override
    public String importXMind(MultipartFile file, String user) throws Exception {
        List<ExcelColumn> lists = XMindUtils.getListNew(file.getInputStream());
        log.info("-----------xmind转化成list==" + lists);
        //校验list是否为空
        if (null == lists || lists.size() == 0) {
            throw new Exception("请检查数据是否为空");
        }
        //用例导入
        caseImport(lists, user);
        return "导入成功！";
    }

    /***
     *
     * @param list [科技与信息中心, 技术保障部test, 测试部wxc, 测试创新部, 自动化测试]
     * @param user wxc
     * @param user 当前module：tcTestCaseModule
     * @return 返回最后一层的module对象
     */
    private TcTestCaseModule checkModules(List<String> list, String user, TcTestCaseModule tcTestCaseModule) {
        if (list.size() == 1) {
            //System.out.println("最后一层的module" + tcTestCaseModule);
            return tcTestCaseModule;
        } else {
            //System.out.println("删除之前的list="+list);
            list.remove(0);
            //System.out.println("删除之后的list="+list);
            //每次取出list(上一层module的子元素list)中的第一个判断是否存在
            List<TcTestCaseModule> subList = tcTestCaseModuleMapper.querySubModule(tcTestCaseModule);
            //当前moduleList
            List<TcTestCaseModule> moduleList = subList.stream()
                    .filter(a -> a.getName().equals(list.get(0)) && a.getLevel() == (tcTestCaseModule.getLevel() + 1))
                    .collect(Collectors.toList());
            //把第一个元素去除掉，调用checkModules()

            if (moduleList != null && moduleList.size() > 0) {
                log.info("存在子模块:" + list.get(0) + "，继续判断下一层子模块");
                return checkModules(list, user, moduleList.get(0));
            } else {
                log.info("不存在子子模块:" + list.get(0) + "，马上为您创建,接下来都需要递归创建了");
                //递归创建模块
                return reCreateModule(list, user, tcTestCaseModule);
            }
        }
    }

    private TcTestCaseModule reCreateModule(List<String> list, String user, TcTestCaseModule tcTestCaseModule) {
        if (list.size() == 0) {
            log.info("创建完成");
            return tcTestCaseModule;
        } else {
            //每次list中的第一个
            TcTestCaseModule moduleNew = new TcTestCaseModule();
            moduleNew.setParentId(tcTestCaseModule.getId());
            moduleNew.setName(list.get(0));
            moduleNew.setStatus((byte) 1);
            moduleNew.setLevel(tcTestCaseModule.getLevel());
            moduleNew.setUser(user);
            moduleService.addModule(moduleNew);
            //模块接口，创建传1.数据库自增1，所以这里我手动加了1
            moduleNew.setLevel(moduleNew.getLevel() + 1);
            log.info("递归调用成功，新模块对象：" + moduleNew);
            //创建完递归调用
            list.remove(0);
            return reCreateModule(list, user, moduleNew);
        }
    }

    //用例导入s
    private void caseImport(List<ExcelColumn> lists, String user) throws Exception {
        for (int i = 0; i < lists.size(); i++) {
            ExcelColumn excelColumn = lists.get(i);
            //如果第一行内容是contains=用例所属, caseName=用例名称 ..代表excel，0直接跳过
            if ("用例所属".equals(excelColumn.getContains()) && "用例名称".equals(excelColumn.getCaseName())) {
                continue;
            }
            if (StringUtils.isBlank(excelColumn.getCaseName())) {
                throw new Exception("用例名称不能为空，请检查！");
            }
            //用例所属 string分割
            String[] str = excelColumn.getContains().split("->");
            //数组转化成list,Arrays的内部类ArrayList和java.util.ArrayLis
            List listStr = Arrays.asList(str);
            List<String> list = new ArrayList(listStr);
            //System.out.println(list.get(0));
            //判断第一个是不是科技与信息中心，不是的话提示
            if (!"科技与信息中心".equals(list.get(0).trim())) {
                throw new Exception("用例归属第一级必须是'科技与信息中心',请检查!");
            }
            TcTestCaseModule queryCondition = new TcTestCaseModule();
            //第一个查的肯定是"科技与信息中心"，level=1
            queryCondition.setName("科技与信息中心");
            //第一个解析的是level=1
            queryCondition.setLevel(1);
            queryCondition.setStatus((byte) 1);
            queryCondition.setParentId(0);
            //第一层module
            TcTestCaseModule module1 = tcTestCaseModuleMapper.getModuleByCondition(queryCondition);
            //递归判断module是否存在，不存在则校验
            TcTestCaseModule currentModule = checkModules(list, user, module1);
            //创建用例
            TcTestcase testcase = new TcTestcase();
            //用例名称
            testcase.setName(excelColumn.getCaseName());
            //优先级
            testcase.setPriority(TestCasePriorityEnum.getCode(excelColumn.getPriority()));
            //用例类型,从枚举取变成，从数据库取，默认是 1.功能测试
            //获取用例类型
            List<TcCaseType> typeList = testConfigService.listAllType();
            //获取用例类型匹配的数组
            List<TcCaseType> typeListNew = typeList.stream().filter(a -> a.getName().equals(excelColumn.getCaseType()))
                    .collect(Collectors.toList());
            testcase.setType(typeListNew.size() == 0 ? 1 : typeListNew.get(0).getId());

            //用例标签
            //获取数据库的tag
            List<TcCaseType> tagList = testConfigService.listAllTag();
            //获取用例类型匹配的数组,先解析str:移动端，接口
            //先判断tag
            if (StringUtils.isNotBlank(excelColumn.getTag())) {
                String[] tags = excelColumn.getTag().split("，");
                //遍历每一个tag,存在的话获取id ,再拼接字符串
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < tags.length; k++) {
                    String tag = tags[k];
                    List<TcCaseType> tagListNew = tagList.stream().filter(a -> a.getName().equals(tag))
                            .collect(Collectors.toList());
                    if (tagListNew.size() > 0) {
                        if (k == tags.length - 1) {
                            sb.append(tagListNew.get(0).getId());
                        } else {
                            sb.append(tagListNew.get(0).getId()).append(",");
                        }
                    }
                }
                testcase.setTag(sb.toString());
            }

            //System.out.println("TAGS=="+testcase.getTag());
            //前置条件
            testcase.setPrecondition(excelColumn.getPrecondition());
            //步骤描述
            List<TcTestcaseStep> steps = new ArrayList<>();
            //解析步骤描述（1.。。。2.。。。）
            List<String> stepsList = RegexUtils.getListByReg(excelColumn.getSteps());
            //解析预期结果（1.。。。2.。。。）
            List<String> expectResultList = RegexUtils.getListByReg(excelColumn.getExpectResult());
            for (int k = 0; k < stepsList.size(); k++) {
                TcTestcaseStep step = new TcTestcaseStep();
                step.setStepNumber(k + 1);
                step.setStepDesc(stepsList.get(k));
                //预期结果,如果预期结果为空，或者不存在，设置"",预期结果多余步骤，直接省略
                if (k >= expectResultList.size()) {
                    step.setExpectResult("");
                } else {
                    step.setExpectResult(expectResultList.get(k));
                }
                steps.add(step);
            }
            testcase.setTcTestcaseStepList(steps);
            //备注
            testcase.setComment(excelColumn.getRemark());

            //所属模块id
            testcase.setModuleId(currentModule.getId());
            //用例状态默认设置=1，状态：1-正常，0-删除，2-停用
            testcase.setStatus(1);
            testcase.setUser(user);
            testCaseService.addTestCase(testcase);
        }
    }
}
