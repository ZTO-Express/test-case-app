package com.zto.testcase.controller.basic;

import com.zto.testcase.response.Result;
import com.zto.testcase.service.BusinessService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;


    /**
     * 获取所有业务线信息
     *
     * @return Object
     */
//    @RequestMapping(value = "/getBusinessLineList")
//    @ResponseBody
//    public Result getBusinessLineList() {
//        Result result = new Result();
//        result.setData(businessLineService.getBusinessLineList());
//        return result;
//    }

    /**
     * 获取所有部门树形信息
     *
     * @return
     */
    @RequestMapping(value = "/getBusinessLineTree")
    @ResponseBody
    public Result getBusinessLineTree() {
        return Result.success(businessService.getBusinessLineTree());
    }

    @RequestMapping(value = "/getProjectListByBid")
    @ResponseBody
    public Result getProjectListByBid(Long id) {
        return Result.success(businessService.getProjectListByBid(id));
    }

    @RequestMapping("/getVersionByPid")
    public Result getVersionByProjectId(Integer pId) {
        return Result.success(businessService.getVersionByPid(pId));
    }

}
