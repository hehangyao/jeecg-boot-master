package org.jeecg.generator;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象属性copy工具
 */
public class BeanCopierUtils {

    public static Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

    /**
     * copy 对象属性
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        if (null == source) {
            return;
        }
        BeanCopier copier = getCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
    }

    /**
     * 对象转换
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convertObject(Object source, Class<T> targetClass) {
        if (null == source) {
            return null;
        }
        BeanCopier copier = getCopier(source.getClass(), targetClass);
        try {
            T target = targetClass.newInstance();
            copier.copy(source, target, null);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象转换出错", e);
        }
    }

    /**
     * 对象List转换
     *
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> convertList(List<?> sourceList, Class<T> targetClass) {
        List<T> list = new ArrayList();
        if (null == sourceList || sourceList.size() < 1) {
            return list;
        }
        BeanCopier copier = getCopier(sourceList.get(0).getClass(), targetClass);
        for (Object source : sourceList) {
            try {
                T target = targetClass.newInstance();
                copier.copy(source, target, null);
                list.add(target);
            } catch (Exception e) {
                throw new RuntimeException("对象转换出错", e);
            }
        }
        return list;
    }

    private static BeanCopier getCopier(Class<?> sourceClass, Class<?> targetClass) {
        String beanKey = generateKey(sourceClass, targetClass);
        BeanCopier copier = null;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }
        return copier;
    }

    /**
     * bean转Map
     *
     * @param source
     * @return
     */
    public static Map<String, Object> beanToMap(Object source) {
        if (null == source) {
            return null;
        }
        try {
            return BeanMap.create(source);
        } catch (Exception e) {
            throw new RuntimeException("对象转换出错", e);
        }
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

}
