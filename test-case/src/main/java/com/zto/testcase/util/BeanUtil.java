package com.zto.testcase.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BeanUtil {
    /**
     * 去掉bean中所有属性为字符串的前后空格
     * @param bean
     * @throws Exception
     */
    public static void trimAttributeValue(Object bean) {
        if(bean!=null){
            //获取所有的字段包括public,private,protected,private
            Field[] fields = bean.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                if(Modifier.isFinal(f.getModifiers())){
                    continue;
                }
                f.setAccessible(true);
                if (f.getType().equals(String.class)) {
                    try{
                        Object value = f.get(bean);

                        if (value == null){
                            continue;
                        }

                        f.set(bean,value.toString().trim());
                    }catch (Exception e){
                        throw new RuntimeException("bean 属性赋值失败",e);
                    }
                }
            }
        }
    }

    /**
     * 复制参数名称相同且的值
     * @param from
     * @param to
     */
    public static void copyAttributeValue(Object from,Object to) {
        if(to!=null){
            //获取所有的字段包括public,private,protected,private
            Field[] fields = to.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field toF = fields[i];
                if(Modifier.isFinal(toF.getModifiers())){
                    continue;
                }
                toF.setAccessible(true);
                Field fromF;
                try {
                    fromF = from.getClass().getDeclaredField(toF.getName());
                } catch (NoSuchFieldException e) {
                    continue;
                }
                if(fromF == null){
                    continue;
                }
                fromF.setAccessible(true);

                if(fromF.getType().equals(toF.getType())){
                    try {
                        toF.set(to,fromF.get(from));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("bean 属性赋值失败",e);
                    }
                }
            }
        }
    }
}