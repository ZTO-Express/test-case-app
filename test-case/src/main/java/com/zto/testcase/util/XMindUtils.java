package com.zto.testcase.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zto.testcase.dto.XMindNodeModel;
import com.zto.testcase.model.ExcelColumn;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.xmind.core.*;
import org.xmind.core.io.ByteArrayStorage;
import org.xmind.core.marker.IMarkerRef;

import java.io.*;
import java.util.*;

/**
 * @Author: wxc
 * @Description:
 * @Date: Create in 下午3:04 2021/4/25
 */
public class XMindUtils {
    private static final Logger logger = LoggerFactory.getLogger(XMindUtils.class);

    public static final String CONTENT_JSON = "content.json";
    //当前文件位置
    private static final String CURRENT_PATH = System.getProperty("user.dir");
    //定义分隔符,随便定义了
    private static final String separation = "#####";

    //老的工具xmind转换
    public static List<ExcelColumn> getList(InputStream is) throws Exception {
        IWorkbookBuilder builder = Core.getWorkbookBuilder();//初始化builder
        IWorkbook workbook = null;
        try {
            workbook = builder.loadFromStream(is);//打开XMind文件
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        ISheet defSheet = workbook.getPrimarySheet();//获取主Sheet
        ITopic topic = defSheet.getRootTopic();
        List<ITopic> requirements = topic.getAllChildren();
        List<ExcelColumn> caseList = new ArrayList<>();
        for (ITopic requirement : requirements) {
            for (Iterator iter = requirement.getAllChildrenIterator(); iter.hasNext(); ) {
                ITopic testcase = (ITopic) iter.next();
                ExcelColumn testCaseBean = new ExcelColumn();
                String caseSuit = topic.getTitleText() + "->" + requirement.getTitleText();
                testCaseBean.setContains(caseSuit);
                //这里都没有步骤，步骤全部默认用例名，结果直接给“验证结果”
                if (testcase.getAllChildren().size() == 0) {
                    //判断一下是否打标
                    Iterator<IMarkerRef> marksIter = testcase.getMarkerRefs().iterator();
                    if (marksIter.hasNext() == Boolean.TRUE) {
                        testCaseBean.setPriority("高");
                        testCaseBean.setCaseType("冒烟测试");
                    } else {
                        testCaseBean.setPriority("中");
                        testCaseBean.setCaseType("功能测试");
                    }
                    testCaseBean.setCaseName(testcase.getTitleText());
                    testCaseBean.setSteps(testcase.getTitleText());
                    testCaseBean.setExpectResult("验证结果");
                    caseList.add(testCaseBean);
                } else {
                    //遍历-第二层
                    for (int i = 0; i < testcase.getAllChildren().size(); i++) {
                        String value2 = testcase.getAllChildren().get(i).getTitleText();
                        //再判断是否有第三层,如果没有第三层，直接写默认值
                        if (testcase.getAllChildren().get(i).getAllChildren().size() == 0) {
                            //如果只有第二层，先判断是否包含步骤：期望
                            //判断一下是否打标
                            Iterator<IMarkerRef> marksIter = testcase.getMarkerRefs().iterator();
                            if (marksIter.hasNext() == Boolean.TRUE) {
                                testCaseBean.setPriority("高");
                                testCaseBean.setCaseType("冒烟测试");
                            } else {
                                testCaseBean.setPriority("中");
                                testCaseBean.setCaseType("功能测试");
                            }
                            if (value2.contains("步骤：")) {
                                ExcelColumn testCaseBeanNew = new ExcelColumn();
                                //属性copy
                                BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                String caseName = testcase.getTitleText();
                                testCaseBeanNew.setCaseName(caseName);
                                testCaseBeanNew.setSteps(value2.split("步骤：")[1]);
                                testCaseBeanNew.setExpectResult("验证结果");
                                caseList.add(testCaseBeanNew);
                            } else if (value2.contains("期望：")) {
                                ExcelColumn testCaseBeanNew = new ExcelColumn();
                                //属性copy
                                BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                String caseName = testcase.getTitleText();
                                testCaseBeanNew.setCaseName(caseName);
                                testCaseBeanNew.setSteps(caseName);
                                testCaseBeanNew.setExpectResult(value2.split("期望：")[1]);
                                caseList.add(testCaseBeanNew);
                            } else {
                                Iterator<IMarkerRef> marksIter2 = testcase.getAllChildren().get(i).getMarkerRefs().iterator();
                                if (marksIter2.hasNext() == Boolean.TRUE) {
                                    testCaseBean.setPriority("高");
                                    testCaseBean.setCaseType("冒烟测试");
                                } else {
                                    testCaseBean.setPriority("中");
                                    testCaseBean.setCaseType("功能测试");
                                }
                                //只有第二层，继续拼接，用例名称也拼接“=”
                                ExcelColumn testCaseBeanNew = new ExcelColumn();
                                //属性copy
                                BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                String caseName = testcase.getTitleText() + "-" + testcase.getAllChildren().get(i).getTitleText();
                                testCaseBeanNew.setCaseName(caseName);
                                testCaseBeanNew.setSteps(caseName);
                                testCaseBeanNew.setExpectResult("验证结果");
                                caseList.add(testCaseBeanNew);
                            }

                        } else {
                            for (int j = 0; j < testcase.getAllChildren().get(i).getAllChildren().size(); j++) {
                                //判断是否有第四层
                                if (testcase.getAllChildren().get(i).getAllChildren().get(j).getAllChildren().size() == 0) {
                                    //首先判断第三层是否包含：步骤： 或者 期望：
                                    //第三列的值：
                                    String value22 = testcase.getAllChildren().get(i).getTitleText();
                                    String value3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getTitleText();
                                    //如果有第三层,且第三层不包括步骤或者期望
                                    if (!(value3.contains("步骤：") || value3.contains("期望："))) {
                                        Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getMarkerRefs().iterator();
                                        if (marksIter3.hasNext() == Boolean.TRUE) {
                                            testCaseBean.setPriority("高");
                                            testCaseBean.setCaseType("冒烟测试");
                                        } else {
                                            testCaseBean.setPriority("中");
                                            testCaseBean.setCaseType("功能测试");
                                        }
                                        ExcelColumn testCaseBeanNew3 = new ExcelColumn();
                                        BeanUtils.copyProperties(testCaseBean, testCaseBeanNew3);
                                        String caseName = testcase.getTitleText() + "-" + testcase.getAllChildren().get(i).getTitleText() + "-" + testcase.getAllChildren().get(i).getAllChildren().get(j).getTitleText();
                                        testCaseBeanNew3.setCaseName(caseName);
                                        testCaseBeanNew3.setSteps(caseName);
                                        testCaseBeanNew3.setExpectResult("验证结果");
                                        caseList.add(testCaseBeanNew3);
                                    } else if (value3.contains("步骤：") && !(value3.contains("期望："))) {//如果包含步骤，不包含期望
                                        ExcelColumn testCaseBeanNew = new ExcelColumn();
                                        //Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getMarkerRefs().iterator();
                                        Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getMarkerRefs().iterator();
                                        if (marksIter3.hasNext() == Boolean.TRUE) {
                                            testCaseBean.setPriority("高");
                                            testCaseBean.setCaseType("冒烟测试");
                                        } else {
                                            testCaseBean.setPriority("中");
                                            testCaseBean.setCaseType("功能测试");
                                        }
                                        //属性copy
                                        BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                        String caseName = testcase.getTitleText() + "-" + testcase.getAllChildren().get(i).getTitleText();
                                        testCaseBeanNew.setCaseName(caseName);
                                        testCaseBeanNew.setSteps(value3.split("步骤：")[1]);
                                        testCaseBeanNew.setExpectResult("验证结果");
                                        caseList.add(testCaseBeanNew);
                                    } else if (!value22.contains("步骤：")) {//不包含步骤，但是包含期望,这里还要判断value22是否等于包含步骤
                                        ExcelColumn testCaseBeanNew = new ExcelColumn();
                                        Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getMarkerRefs().iterator();
                                        if (marksIter3.hasNext() == Boolean.TRUE) {
                                            testCaseBean.setPriority("高");
                                            testCaseBean.setCaseType("冒烟测试");
                                        } else {
                                            testCaseBean.setPriority("中");
                                            testCaseBean.setCaseType("功能测试");
                                        }
                                        //属性copy
                                        BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                        String caseName = testcase.getTitleText() + "-" + value22;
                                        testCaseBeanNew.setCaseName(caseName);
                                        testCaseBeanNew.setSteps(caseName);
                                        testCaseBeanNew.setExpectResult(value3.split("期望：")[1]);
                                        caseList.add(testCaseBeanNew);
                                    } else {
                                        ExcelColumn testCaseBeanNew = new ExcelColumn();
                                        Iterator<IMarkerRef> marksIter3 = testcase.getMarkerRefs().iterator();
                                        if (marksIter3.hasNext() == Boolean.TRUE) {
                                            testCaseBean.setPriority("高");
                                            testCaseBean.setCaseType("冒烟测试");
                                        } else {
                                            testCaseBean.setPriority("中");
                                            testCaseBean.setCaseType("功能测试");
                                        }
                                        //属性copy
                                        BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                        String caseName = testcase.getTitleText();
                                        testCaseBeanNew.setCaseName(caseName);
                                        testCaseBeanNew.setSteps(value22.split("步骤：")[1]);
                                        testCaseBeanNew.setExpectResult(value3.split("期望：")[1]);
                                        caseList.add(testCaseBeanNew);
                                    }
                                } else {//如果有第四层
                                    String value22 = testcase.getAllChildren().get(i).getTitleText();
                                    String value3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getTitleText();
                                    for (int k = 0; k < testcase.getAllChildren().get(i).getAllChildren().get(j).getAllChildren().size(); k++) {
                                        String value4 = testcase.getAllChildren().get(i).getAllChildren().get(j).getAllChildren().get(k).getTitleText();
                                        //判断是否有第五层
                                        if (testcase.getAllChildren().get(i).getAllChildren().get(j).getAllChildren().get(k).getAllChildren().size() == 0) {
                                            //如果第四层包含期望
                                            if (value4.contains("期望：") && value3.contains("步骤：")) {
                                                ExcelColumn testCaseBeanNew = new ExcelColumn();
                                                Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getMarkerRefs().iterator();
                                                if (marksIter3.hasNext() == Boolean.TRUE) {
                                                    testCaseBean.setPriority("高");
                                                    testCaseBean.setCaseType("冒烟测试");
                                                } else {
                                                    testCaseBean.setPriority("中");
                                                    testCaseBean.setCaseType("功能测试");
                                                }
                                                //属性copy
                                                BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                                String caseName = testcase.getTitleText() + "-" + value22;
                                                testCaseBeanNew.setCaseName(caseName);
                                                testCaseBeanNew.setSteps(value3.split("步骤：")[1]);
                                                testCaseBeanNew.setExpectResult(value4.split("期望：")[1]);
                                                caseList.add(testCaseBeanNew);
                                            } else if (value4.contains("步骤：")) {
                                                ExcelColumn testCaseBeanNew = new ExcelColumn();
                                                Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getMarkerRefs().iterator();
                                                if (marksIter3.hasNext() == Boolean.TRUE) {
                                                    testCaseBean.setPriority("高");
                                                    testCaseBean.setCaseType("冒烟测试");
                                                } else {
                                                    testCaseBean.setPriority("中");
                                                    testCaseBean.setCaseType("功能测试");
                                                }
                                                //属性copy
                                                BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                                String caseName = testcase.getTitleText() + "-" + value22 + "-" + value3;
                                                testCaseBeanNew.setCaseName(caseName);
                                                testCaseBeanNew.setSteps(value4.split("步骤：")[1]);
                                                testCaseBeanNew.setExpectResult("验证结果");
                                                caseList.add(testCaseBeanNew);
                                            } else if (value4.contains("期望：")) {
                                                ExcelColumn testCaseBeanNew = new ExcelColumn();
                                                Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getMarkerRefs().iterator();
                                                if (marksIter3.hasNext() == Boolean.TRUE) {
                                                    testCaseBean.setPriority("高");
                                                    testCaseBean.setCaseType("冒烟测试");
                                                } else {
                                                    testCaseBean.setPriority("中");
                                                    testCaseBean.setCaseType("功能测试");
                                                }
                                                //属性copy
                                                BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                                String caseName = testcase.getTitleText() + "-" + value22 + "-" + value3;
                                                testCaseBeanNew.setCaseName(caseName);
                                                testCaseBeanNew.setSteps(caseName);
                                                testCaseBeanNew.setExpectResult(value4.split("期望：")[1]);
                                                caseList.add(testCaseBeanNew);
                                            }
                                        } else {//如果过有第五层，第五层只能是期望，第四层只能步骤，否则异常，舍弃
                                            value22 = testcase.getAllChildren().get(i).getTitleText();
                                            value3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getTitleText();
                                            value4 = testcase.getAllChildren().get(i).getAllChildren().get(j).getAllChildren().get(k).getTitleText();
                                            for (int l = 0; l < testcase.getAllChildren().get(i).getAllChildren().get(j).getAllChildren().get(k).getAllChildren().size(); l++) {
                                                String value5 = testcase.getAllChildren().get(i).getAllChildren().get(j).getAllChildren().get(k).getAllChildren().get(l).getTitleText();
                                                //如果4不包含步骤，5不包含期望，直接舍弃
                                                if (value4.contains("步骤：") && value5.contains("期望：")) {
                                                    ExcelColumn testCaseBeanNew = new ExcelColumn();
                                                    Iterator<IMarkerRef> marksIter3 = testcase.getAllChildren().get(i).getAllChildren().get(j).getMarkerRefs().iterator();
                                                    if (marksIter3.hasNext() == Boolean.TRUE) {
                                                        testCaseBean.setPriority("高");
                                                        testCaseBean.setCaseType("冒烟测试");
                                                    } else {
                                                        testCaseBean.setPriority("中");
                                                        testCaseBean.setCaseType("功能测试");
                                                    }
                                                    //属性copy
                                                    BeanUtils.copyProperties(testCaseBean, testCaseBeanNew);
                                                    String caseName = testcase.getTitleText() + "-" + value22 + "-" + value3;
                                                    testCaseBeanNew.setCaseName(caseName);
                                                    testCaseBeanNew.setSteps(value4.split("步骤：")[1]);
                                                    testCaseBeanNew.setExpectResult(value5.split("期望：")[1]);
                                                    caseList.add(testCaseBeanNew);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return caseList;
    }

    //新工具转换-新算法
    public static List<ExcelColumn> getListNew(InputStream is) throws Exception {
        Object obj = readXMindIS(is);
        XMindNodeModel node = JSONObject.parseObject(obj.toString(), XMindNodeModel.class);
        //System.out.println(node);
        Stack<XMindNodeModel> pathStack = new Stack();
        List<ExcelColumn> listCol = new ArrayList();
        List<ExcelColumn> columnList = iteratorNode(node, pathStack, listCol);
        //System.out.println(columnList);
        return columnList;
    }

    /**
     * 想读XMind，读就完了
     */
    public static Object readXMind(File file) {
        String res = null;
        Object contents = null;
        try {
            res = extract(file);
            if (isXMindNetwork(res, file)) {
                //contents = (getXMindNetworkContent(file, res));
                logger.error("暂时不支持该格式，请联系管理员");
                throw new Exception("暂时不支持该格式，请联系管理员");
            } else {
                contents = getXMind8Content(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 删除生成的文件夹
            if (res != null) {
                File dir = new File(res);
                deleteDir(dir);
            }
            // 删除零时文件
//            if (file != null) {
//                file.delete();
//            }
        }
        return contents;
    }

    /**
     * 想读XMind，xmind8接受流
     */
    public static Object readXMindIS(InputStream is) {
        String res = null;
        Object contents = null;
        try {
            contents = getXMind8ContentIS(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 删除生成的文件夹
            if (res != null) {
                File dir = new File(res);
                deleteDir(dir);
            }
            // 删除零时文件
//            if (file != null) {
//                file.delete();
//            }
        }
        return contents;
    }

    /**
     * 填充节点
     *
     * @param iTopic
     * @return 一个节点
     */
    public static XMindNodeModel fillNode(ITopic iTopic) {
        XMindNodeModel newNode = new XMindNodeModel();
        try {
            //内容
            newNode.setContent(iTopic.getTitleText());
            //链接
            newNode.setLink(iTopic.getHyperlink());
            //标签
            Set<String> labels = iTopic.getLabels();
            if (labels != null && labels.size() != 0) {
                newNode.setLabels(labels);
            }
            //图片,暂时不支持吧
            //IImage image = iTopic.getImage();
            //newNode.setIImage(image.getSource());
            //如果有图片，这个不需要填充了，并且用例名称没有

            //批注
            Set<IMarkerRef> markers = iTopic.getMarkerRefs();
            if (markers != null && markers.size() != 0 ) {
              for(IMarkerRef iMarkerRef:markers){
                  //这里定死 优先级为1的打标 priority-1
                  if("priority-1".equals(iMarkerRef.getMarkerId())){
                      newNode.setMarkers(true);//如果有批注，则给个高优先级
                  }
              }

            }
            //newNode.setMarkers(marksIter.next().toString());
            //备注
            IPlainNotesContent content = (IPlainNotesContent) iTopic.getNotes().getContent(INotes.PLAIN);
            if (content != null) {
                newNode.setComment(content.getTextContent());
            }
        } catch (Exception e) {
            logger.error("*****填充node出错*****");
            e.printStackTrace();
        }

        return newNode;
    }

    /**
     * 填充节点
     *
     * @param attachedObject
     * @return 一个节点
     */
    public static XMindNodeModel fillNodeNetwork(JSONObject attachedObject) {
        XMindNodeModel newNode = new XMindNodeModel();
        try {
            JSONObject notes = attachedObject.getJSONObject("notes");
            if (notes != null) {
                JSONObject plain = notes.getJSONObject("plain");
                if (plain != null) {
                    newNode.setComment(plain.getString("content"));
                }
            }
            newNode.setContent(attachedObject.getString("title"));
            newNode.setLabels(Collections.singleton(attachedObject.getString("labels")));
            //newNode.setMarkers(Collections.singleton(attachedObject.getString("markers")));
        } catch (Exception e) {
            logger.error("*****填充nodeNetwork出错*****");
            e.printStackTrace();
        }
        return newNode;
    }

    /**
     * 建立文件内容树
     *
     * @param file
     * @return json串
     */
    public static Object getXMind8Content(File file) {
        //初始化builder
        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IWorkbook workbook = null;
        try {
            //打开XMind文件
            workbook = builder.loadFromFile(file, new ByteArrayStorage(), null);
        } catch (IOException | CoreException e) {
            e.printStackTrace();
        }

        if (workbook == null) {
            return "文件为空或格式不匹配";
        }

        //获取主Sheet
        ISheet defSheet = workbook.getPrimarySheet();
        //获取根Topic
        ITopic rootTopic = defSheet.getRootTopic();
        //遍历树
        Queue<Object> iTopicQueue = new LinkedList<>();
        iTopicQueue.add(rootTopic);
        //新树
        XMindNodeModel xMindNodeRoot = new XMindNodeModel();
        xMindNodeRoot.setContent(rootTopic.getTitleText());
        xMindNodeRoot.setLevel(1);
        iTopicQueue.add(xMindNodeRoot);
        while (!iTopicQueue.isEmpty()) {
            ITopic oldTreeNode = (ITopic) iTopicQueue.poll();
            XMindNodeModel treeNode = (XMindNodeModel) iTopicQueue.poll();
            if (treeNode == null) {
                return null;
            }
            Integer level = treeNode.getLevel() + 1;
            List<XMindNodeModel> xMindNodeModels = new ArrayList<>();
            for (ITopic oneChild : oldTreeNode.getAllChildren()) {
                XMindNodeModel newNode = XMindUtils.fillNode(oneChild);
                //如果没有content,去除
                if (StringUtils.isNotBlank(newNode.getContent())) {
                    newNode.setLevel(level);
                    xMindNodeModels.add(newNode);
                    iTopicQueue.add(oneChild);
                    iTopicQueue.add(newNode);
                }
            }
            treeNode.setChildren(xMindNodeModels);
        }
        return JSON.toJSON(xMindNodeRoot);
    }

    public static Object getXMind8ContentIS(InputStream is) {
        //初始化builder
        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IWorkbook workbook = null;
        try {
            //打开XMind文件
            workbook = builder.loadFromStream(is, new ByteArrayStorage(), null);
        } catch (IOException | CoreException e) {
            e.printStackTrace();
        }

        if (workbook == null) {
            return "文件为空或格式不匹配";
        }

        //获取主Sheet
        ISheet defSheet = workbook.getPrimarySheet();
        //获取根Topic
        ITopic rootTopic = defSheet.getRootTopic();
        //遍历树
        Queue<Object> iTopicQueue = new LinkedList<>();
        iTopicQueue.add(rootTopic);
        //新树
        XMindNodeModel xMindNodeRoot = new XMindNodeModel();
        xMindNodeRoot.setContent(rootTopic.getTitleText());
        xMindNodeRoot.setLevel(1);
        iTopicQueue.add(xMindNodeRoot);
        while (!iTopicQueue.isEmpty()) {
            ITopic oldTreeNode = (ITopic) iTopicQueue.poll();
            XMindNodeModel treeNode = (XMindNodeModel) iTopicQueue.poll();
            if (treeNode == null) {
                return null;
            }
            Integer level = treeNode.getLevel() + 1;
            List<XMindNodeModel> xMindNodeModels = new ArrayList<>();
            for (ITopic oneChild : oldTreeNode.getAllChildren()) {
                XMindNodeModel newNode = XMindUtils.fillNode(oneChild);
                //如果没有content,去除
                if (StringUtils.isNotBlank(newNode.getContent())) {
                    newNode.setLevel(level);
                    xMindNodeModels.add(newNode);
                    iTopicQueue.add(oneChild);
                    iTopicQueue.add(newNode);
                }
            }
            treeNode.setChildren(xMindNodeModels);
        }
        return JSON.toJSON(xMindNodeRoot);
    }

    /**
     * 网上版本 建立文件内容树
     *
     * @param file
     * @return json串
     */
    public static Object getXMindNetworkContent(File file, String extractFileDir)
            throws IOException {
        List<String> keys = new ArrayList<>();
        keys.add(CONTENT_JSON);
        Map<String, String> map = getContents(keys, file, extractFileDir);
        String content = map.get(CONTENT_JSON);
        JSONArray jsonArray = JSONArray.parseArray(content);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        JSONObject rootTopic = jsonObject.getJSONObject("rootTopic");

        //遍历树
        Queue<Object> iTopicQueue = new LinkedList<>();
        iTopicQueue.add(rootTopic);
        //新树
        XMindNodeModel xMindNodeRoot = new XMindNodeModel();
        xMindNodeRoot.setContent(rootTopic.getString("title"));
        xMindNodeRoot.setLevel(1);
        iTopicQueue.add(xMindNodeRoot);
        while (!iTopicQueue.isEmpty()) {
            JSONObject oldTreeNode = (JSONObject) iTopicQueue.poll();
            XMindNodeModel treeNode = (XMindNodeModel) iTopicQueue.poll();
            if (treeNode == null) {
                return null;
            }
            Integer level = treeNode.getLevel() + 1;
            List<XMindNodeModel> xMindNodeModels = new ArrayList<>();
            JSONObject children = oldTreeNode.getJSONObject("children");
            if (children == null) {
                continue;
            }
            JSONArray attachedArray = children.getJSONArray("attached");
            if (attachedArray == null) {
                continue;
            }
            for (Object attached : attachedArray) {
                JSONObject attachedObject = (JSONObject) attached;
                XMindNodeModel newNode = XMindUtils.fillNodeNetwork(attachedObject);
                newNode.setLevel(level);
                xMindNodeModels.add(newNode);
                iTopicQueue.add(attachedObject);
                iTopicQueue.add(newNode);
            }
            treeNode.setChildren(xMindNodeModels);
        }
        return JSON.toJSON(xMindNodeRoot);
    }

    /**
     * 判断是否有CONTENT_JSON文件
     */
    private static boolean isXMindNetwork(String res, File file) {
        // 解压
        File parent = new File(res);
        if (parent.isDirectory()) {
            String[] files = parent.list(new FileFilter());
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                if (files[i].equals(CONTENT_JSON)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回解压后的文件夹名字
     */
    public static String extract(File file) throws IOException, ArchiveException {
        Expander expander = new Expander();
        String destFileName = CURRENT_PATH + File.separator + "XMind" + System.currentTimeMillis();
        expander.expand(file, new File(destFileName));
        return destFileName;
    }

    /**
     * 找CONTENT_JSON文件
     */
    public static Map<String, String> getContents(List<String> subFileNames, File file, String extractFileDir)
            throws IOException {
        String destFilePath = extractFileDir;
        Map<String, String> map = new HashMap<>(16);
        File destFile = new File(destFilePath);
        if (destFile.isDirectory()) {
            String[] res = destFile.list(new FileFilter());
            for (int i = 0; i < Objects.requireNonNull(res).length; i++) {
                if (subFileNames.contains(res[i])) {
                    String s = destFilePath + File.separator + res[i];
                    String content = getFileContent(s);
                    map.put(res[i], content);
                }
            }
        }
        return map;
    }

    /**
     * 返回CONTENT_JSON文件内容
     */
    public static String getFileContent(String fileName) throws IOException {
        File file;
        try {
            file = new File(fileName);
        } catch (Exception e) {
            throw new RuntimeException("找不到该文件");
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReder = new BufferedReader(fileReader);
        StringBuilder stringBuffer = new StringBuilder();
        while (bufferedReder.ready()) {
            stringBuffer.append(bufferedReder.readLine());
        }
        // 打开的文件需关闭，在unix下可以删除，否则在windows下不能删除（file.delete())
        bufferedReder.close();
        fileReader.close();
        return stringBuffer.toString();
    }

    /**
     * 过滤器
     */
    static class FileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            // String的 endsWith(String str)方法 筛选出以str结尾的字符串
            if (name.endsWith(".xml") || name.endsWith(".json")) {
                return true;
            }
            return false;
        }
    }

    /**
     * 删除生成的文件夹
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 将tree结构数据转成List结构
     *
     * @param list
     * @return
     */
    public static void treeToList(XMindNodeModel node, List<XMindNodeModel> list) {
        if (list == null) {
            list = new ArrayList();
        }
        //设置当前节点的必要数据
        XMindNodeModel nodeValue = new XMindNodeModel(node.getContent(), node.getComment(), node.getLabels(), node.getMarkers(), node.getLink(), node.getLevel(), node.getIImage(), node.getChildren());
        list.add(nodeValue);
        //递归遍历子节点
        if (node.getChildren().size() > 0) {
            for (int i = 0; i < node.getChildren().size(); i++) {
                XMindNodeModel node_ = node.getChildren().get(i);
                treeToList(node_, list);
            }
        }

    }

    public static List<String> listToList(List<XMindNodeModel> list) {
        List listNew = new ArrayList<>();
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            //第二层以后开始拼接
            if (list.get(i).getLevel() > 2) {
                //如果有子节点，则暂存一下
                if (list.get(i).getChildren().size() != 0) {
                    //子层级才添加
                    if (list.get(i).getLevel() >= list.get(i - 1).getLevel()) {
                        str = str + list.get(i).getContent();
                    }
                } else {
                    listNew.add(list.get(i).getContent());
                    listNew.add(str);
                    str = "";
                }
            }

        }
        return listNew;
    }

    //打印出路径
    private static void print(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            XMindNodeModel n = (XMindNodeModel) it.next();
            System.out.print(n.getContent() + "|@#$|");
        }
        System.out.println();
    }

    //保存全路径
    private static String getPath(List list) {
        StringBuilder sb = new StringBuilder();
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//            XMindNodeModel n = (XMindNodeModel) it.next();
//            sb.append(n.getContent() + "|@#$|");
//            //System.out.print(n.getContent() + "|@#$|");
//        }
//        //System.out.println();
        for (int i = 0; i < list.size(); i++) {
            XMindNodeModel n = (XMindNodeModel) list.get(i);
            if (i == list.size() - 1) {
                sb.append(n.getContent());
            } else {
                sb.append(n.getContent());
                sb.append(separation);
            }
        }
        return sb.toString();
    }

    private static List<ExcelColumn> iteratorNode(XMindNodeModel n, Stack<XMindNodeModel> pathStack, List<ExcelColumn> listCol) throws Exception {
        pathStack.push(n);//入栈
        List childList = n.getChildren();
        if (childList.size() == 0) {//没有孩子，说明是叶子节点
            List list = new ArrayList();
            Iterator stackIt = pathStack.iterator();
            while (stackIt.hasNext()) {
                list.add(stackIt.next());
            }
            ExcelColumn excelColumn = new ExcelColumn();
            //备注
            excelColumn.setRemark(n.getComment());
            //标签
            if (n.getLabels() != null && n.getLabels().size() != 0) {
                excelColumn.setTag(n.getLabels().toString());
            }
            //标注打标，如果父级有标注，子集全部标记
            Boolean isMark=false;
            for(int i=0;i<list.size();i++){
                XMindNodeModel node=(XMindNodeModel)list.get(i);
                if(node.getMarkers()!=null && node.getMarkers()){
                    isMark=true;
                }
            }
            //markers根据是否有标注，来默认设置
            if (isMark) {
                excelColumn.setCaseType("冒烟测试");
                excelColumn.setPriority("高");
            } else {
                excelColumn.setCaseType("功能测试");
                excelColumn.setPriority("中");
            }
            //内容过滤
            filterContent(excelColumn, getPath(list));
            //excelColumn.setCaseName(getPath(list));
            listCol.add(excelColumn);
        } else {
            Iterator it = childList.iterator();
            while (it.hasNext()) {
                XMindNodeModel child = (XMindNodeModel) it.next();
                iteratorNode(child, pathStack, listCol);//深度优先，进入递归
                pathStack.pop();//回溯时候出站
            }
        }
        return listCol;
    }

    //xmind内容过滤
    private static void filterContent(ExcelColumn excelColumn, String content) throws Exception {
        //全路径里面有的内容，根据分隔符截取
        String[] strs = content.split(separation);
        //前两层定义为用例所属
        String contains = strs[0] + "->" + strs[1];
        excelColumn.setContains(contains);
        //第三层开始判断 是否 包含 前置条件：，步骤描述：，包含预期结果：
        StringBuilder sbPrecondition = new StringBuilder();//前置条件
        StringBuilder sbSteps = new StringBuilder();//步骤描述
        StringBuilder sbExpect = new StringBuilder();//预期结果
        StringBuilder sbCaseName = new StringBuilder();//用例描述
        if(strs.length<2){
            throw new Exception("模板层级小于2，请检查模板！！！");
        }
        for (int i = 2; i < strs.length; i++) {
            if (strs[i].contains("前置条件：")||strs[i].contains("前置条件:")) {
                //去掉第一个回车
                strs[i]=strs[i].replaceFirst("\\r\\n|\\n|\\r","");
                sbPrecondition.append(strs[i]);
                sbPrecondition.append(System.getProperty("line.separator"));
            } else if (strs[i].contains("步骤描述：")||strs[i].contains("步骤描述:")) {
                sbSteps.append(strs[i]);
            } else if (strs[i].contains("预期结果：")|strs[i].contains("预期结果:")) {
                sbExpect.append(strs[i]);
            } else {//不包含那些的，都拼接成用例名称
                sbCaseName.append(strs[i]);
                sbCaseName.append("-");
            }
        }
        //设置对象excelColumn
        //处理：用例名称，最后一个"-"去掉
        String caseName = sbCaseName.toString().trim().substring(0, sbCaseName.toString().length() - 1);
        excelColumn.setCaseName(caseName);
        //处理：前置条件：替换成空，支持多个前置条件，','
        if (StringUtils.isNotBlank(sbPrecondition)) {
            String precondition = sbPrecondition.toString().trim().replace("前置条件：", "");
            excelColumn.setPrecondition(precondition);
        }
        if (StringUtils.isNotBlank(sbSteps)) {
            //处理：步骤描述：替换成空，多个步骤描述，暂时不支持
            String steps = sbSteps.toString().trim().replace("步骤描述：", "").replace("步骤描述:","");
            excelColumn.setSteps(steps);
        } else {//没有写步骤，把用例名称设置为步骤
            excelColumn.setSteps(caseName);
        }
        if (StringUtils.isNotBlank(sbExpect)) {
            //处理：预期结果： 替换成空，多个预期结果暂时不支持
            String expect = sbExpect.toString().replace("预期结果：", "").replace("预期结果:","");;
            excelColumn.setExpectResult(expect);
        } else {
            excelColumn.setExpectResult("请检查预期结果");
        }
    }

    public static void main(String[] args) throws Exception {
        //以前的XMind
        String fileName = "测试模板.xmind";
        //现在的Xmind
        String filePath = "/Users/wuxiaochen/Downloads/" + fileName;
        //String fileName = "doc/xmind模板.xmind";
        // String fileName = "doc/测试模板.xmind";

//        System.out.println(fileName);
//        String res = XmindParser.parseJson(fileName);
//
//        //树转化为List
//        XmindNode xmindNode = treetolist(res);
//        System.out.println(xmindNode);
//
//        List listCase = listConvert(xmindNode,null);
//        System.out.println(listCase);
        Object obj = readXMind(new File(filePath));
        System.out.println(obj);
        XMindNodeModel node = JSONObject.parseObject(obj.toString(), XMindNodeModel.class);
        System.out.println(node);
        Stack<XMindNodeModel> pathStack = new Stack();
        List<ExcelColumn> listCol = new ArrayList();
        List<ExcelColumn> columnList = iteratorNode(node, pathStack, listCol);
        System.out.println(columnList);
    }

}
