package com.icekredit.pdf.utils;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于从json对象中取出指定键对应的属性
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class JSONAttrGetter {
    public static final String DEFAULT_STR_VALUE = "-";

    /**
     * 判断当前Json对象中指定Key对应的json属性是否存在
     *
     * @param dataJsonObj 当前jsonObject
     * @param key         指定json属性对应的key
     * @return 存在当前key返回true，否则返回false
     */
    public static Boolean isAttributeExists(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return false;
        }

        return dataJsonObj.containsKey(key);
    }

    /**
     * 从当前json对象中取出指定key对应的布尔属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 返回指定key对应的布尔属性，如果指定key不存在或者不是一个有效的bool值，返回null
     */
    public static Boolean getBoolean(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        String strValue = getString(dataJsonObj, key);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        if (!REGUtil.REG_PATTERN_FOR_BOOLEAN.matcher(strValue).matches()) {
            return null;
        }

        return Boolean.valueOf(strValue);
    }

    /**
     * 从当前json对象中取出指定key对应的布尔属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 返回指定key对应的布尔属性，如果指定key不存在或者不是一个有效的bool值，返回defaultValue
     */
    public static Boolean getBoolean(JSONObject dataJsonObj, String key, Boolean defaultValue) {
        Boolean actualValue = getBoolean(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的双精度浮点数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 如果指定key不存在或者不是一个有效的数值属性，返回null
     */
    public static Double getDouble(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        String strValue = getString(dataJsonObj, key);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        if (!REGUtil.REG_PATTERN_FOR_NUMBER.matcher(strValue).matches()) {
            return null;
        }

        return Double.valueOf(strValue.replaceAll(REGUtil.INVALID_FLOAT_CHAR_REG, ""));
    }

    /**
     * 从指定Json对象中取出指定Key对应的数值属性值
     *
     * @param dataJsonObj  指定Json对象
     * @param key          指定Key
     * @param defaultValue 默认值
     * @return 如果指定key对应的属性为有效的数值属性，那么返回此值，否则返回默认值defaultValue
     */
    public static Double getDouble(JSONObject dataJsonObj, String key, Double defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的金额属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 如果指定key不存在或者不是一个有效的金额属性，返回null
     */
    public static Double getAmount(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        String strValue = getString(dataJsonObj, key);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        //替换掉非数值字符
        strValue = strValue.replaceAll(REGUtil.INVALID_FLOAT_CHAR_REG, "");

        if (!REGUtil.REG_PATTERN_FOR_NUMBER.matcher(strValue).matches()) {
            return null;
        }

        return Double.valueOf(strValue);
    }

    /**
     * 从指定Json对象中取出指定Key对应的金额属性值
     *
     * @param dataJsonObj  指定Json对象
     * @param key          指定Key
     * @param defaultValue 默认值
     * @return 如果指定key对应的属性为有效的金额属性，那么返回此值，否则返回默认值defaultValue
     */
    public static Double getAmount(JSONObject dataJsonObj, String key, Double defaultValue) {
        Double actualValue = getAmount(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前对象中取出指定键对应的单精度浮点数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的单精度浮点数属性，如果指定key不存在或者不是一个有效的数值属性，返回null
     */
    public static Float getFloat(JSONObject dataJsonObj, String key) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? null : actualValue.floatValue();
    }

    /**
     * 从当前对象中取出指定键对应的单精度浮点数属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的单精度浮点数属性，如果指定key不存在或者不是一个有效的数值属性，返回默认值
     */
    public static Float getFloat(JSONObject dataJsonObj, String key, Float defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue.floatValue();
    }

    /**
     * 从当前对象中取出指定键对应的属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的属性，如果不存在或发生异常返回null
     */
    public static Object get(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty()) {
            return null;
        }

        if (!dataJsonObj.containsKey(key)) {
            return null;
        }

        return dataJsonObj.get(key);
    }

    /**
     * 从当前对象中取出指定键对应的属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的属性，如果不存在返回默认值
     */
    public static Object get(JSONObject dataJsonObj, String key, Object defaultValue) {
        Object actualValue = get(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前对象中取出指定键对应的整数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者指定属性不是一个有效的数值属性那么返回null
     */
    public static Integer getInteger(JSONObject dataJsonObj, String key) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? null : actualValue.intValue();
    }

    /**
     * 从当前对象中取出指定键对应的整数属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者指定属性不是一个有效的数值属性那么返回默认值
     */
    public static Integer getInteger(JSONObject dataJsonObj, String key, Integer defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue.intValue();
    }

    /**
     * 从当前对象中取出指定键对应的长整型整数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者不是一个有效的数值属性那么返回null
     */
    public static Long getLong(JSONObject dataJsonObj, String key) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? null : actualValue.longValue();
    }

    /**
     * 从当前对象中取出指定键对应的长整型整数属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者不是一个有效的数值属性那么返回默认值
     */
    public static Long getLong(JSONObject dataJsonObj, String key, Long defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue.longValue();
    }

    /**
     * 从当前对象中取出指定键对应的json数组
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定key对应的json数组，如果指定对应的属性不存在或者不是json数组返回null
     */
    public static JSONArray getJsonArray(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return new JSONArray();
        }

        Object obj = get(dataJsonObj, key);
        if (obj == null || !(obj instanceof JSONArray)) {
            return new JSONArray();
        }

        return (JSONArray) obj;
    }

    /**
     * 从当前对象中取出指定键对应的json数组
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定key对应的json数组，如果指定对应的属性不存在或者不是Json数组返回默认值
     */
    public static JSONArray getJsonArray(JSONObject dataJsonObj, String key, JSONArray defaultValue) {
        JSONArray actualValue = getJsonArray(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的json对象
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定key对应的json对象属性，如果指定对应的属性不存在或者不是json对象返回null
     */
    public static JSONObject getJsonObject(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        Object obj = get(dataJsonObj, key);
        if (obj == null || !(obj instanceof JSONObject)) {
            return null;
        }

        return (JSONObject) obj;
    }

    /**
     * 从当前json对象中取出指定键对应的json对象
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定key对应的json对象属性，如果指定对应的属性不存在或者不是json对象返回默认值
     */
    public static JSONObject getJsonObject(JSONObject dataJsonObj, String key, JSONObject defaultValue) {
        JSONObject actualValue = getJsonObject(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的字符串
     *
     * @param dataJsonObj 当前jsonObject
     * @param key         指定key
     * @return 指定键对应的字符串，如果不存在返回null
     */
    public static String getString(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        if (!dataJsonObj.containsKey(key)) {
            return null;
        }

        Object obj = get(dataJsonObj, key);

        if (obj == null || obj instanceof JSON) {
            return null;
        }

        return String.valueOf(obj);
    }

    /**
     * 从当前json对象中取出指定键对应的字符串
     *
     * @param dataJsonObj  当前jsonObject
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的字符串，如果不存在或者指定key对应的属性是一个Json对象或者是Json数组返回默认值
     */
    public static String getString(JSONObject dataJsonObj, String key, String defaultValue) {
        String actualValue = getString(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }


    /**
     * 从Json数组中按照指定key值集合取出所有属性，最终返回对应数据的二维列表
     *
     * @param jsonObjs 指定的json数组
     * @param specKeys 指定的key值集合
     * @return 数据二维列表
     */
    public static List<List<String>> getAll(JSONArray jsonObjs, List<String> specKeys) {
        List<List<String>> datas = new ArrayList<>();

        if (jsonObjs == null || jsonObjs.isEmpty() || specKeys == null || specKeys.isEmpty()) {
            return datas;
        }

        JSONObject jsonObj;
        for (Object obj : jsonObjs) {
            jsonObj = (JSONObject) obj;
            datas.add(getAll(jsonObj, specKeys));
        }

        return datas;
    }

    /**
     * 从json数组中取出指定key对应的属性，返回数据一维列表
     *
     * @param jsonObjs json数组
     * @param specKey  指定key
     * @return json数组中指定key对应的属性集合
     */
    public static List<String> getAll(JSONArray jsonObjs, String specKey) {
        List<String> data = new ArrayList<>();

        if (jsonObjs == null || jsonObjs.isEmpty() || StringUtils.isBlank(specKey)) {
            return data;
        }

        JSONObject jsonObj;
        for (Object obj : jsonObjs) {
            jsonObj = (JSONObject) obj;
            data.add(getString(jsonObj, specKey,""));
        }

        return data;
    }

    /**
     * 从Json数组中按照指定key值集合取出所有属性，最终返回对应数据的二维列表
     *
     * @param jsonObjs 指定json数组
     * @param specKeys key值数组
     * @return 数据二维列表
     */
    public static List<List<String>> getAll(JSONArray jsonObjs, String[] specKeys) {
        return getAll(jsonObjs, Arrays.asList(specKeys));
    }

    /**
     * 从json对象中取出指定key值集合对应的json属性
     *
     * @param jsonObj  指定json对象
     * @param specKeys 指定key值集合
     * @return 从当前json对象中取出的属性集合
     */
    public static List<String> getAll(JSONObject jsonObj, List<String> specKeys) {
        List<String> data = new ArrayList<>();

        if (jsonObj == null || jsonObj.isEmpty() || specKeys == null || specKeys.isEmpty()) {
            return data;
        }

        for (String specKey : specKeys) {
            data.add(getString(jsonObj, specKey,""));
        }

        return data;
    }

    /**
     * 从json对象中取出指定key值数组对应的json属性
     *
     * @param jsonObj  指定json对象
     * @param specKeys 指定key值数组
     * @return 从当前json对象中取出的属性集合
     */
    public static List<String> getAll(JSONObject jsonObj, String[] specKeys) {
        return getAll(jsonObj, Arrays.asList(specKeys));
    }

    /**
     * 从Json数组中按照指定key值集合取出所有属性，最终返回对应数据的二维列表，加上取出的条数限制
     *
     * @param jsonObjs json数组
     * @param specKeys 指定key值集合
     * @param limit    条数限制
     * @return 数据二维列表
     */
    public static List<List<String>> getAll(JSONArray jsonObjs, List<String> specKeys, int limit) {
        List<List<String>> datas = new ArrayList<>();

        if (jsonObjs == null || jsonObjs.isEmpty() || specKeys == null || specKeys.isEmpty() || limit < 1) {
            return datas;
        }

        JSONObject jsonObj;
        int index = 0;

        for (Object obj : jsonObjs) {
            jsonObj = (JSONObject) obj;
            datas.add(getAll(jsonObj, specKeys));
            index++;

            if (index >= limit) {
                break;
            }
        }

        return datas;
    }

    /**
     * 从Json数组中按照指定key值集合取出所有属性，最终返回对应数据的二维列表，加上取出的条数限制
     *
     * @param jsonObjs json数组
     * @param specKeys 指定key值数组
     * @param limit    条数限制
     * @return 数据二维列表
     */
    public static List<List<String>> getAll(JSONArray jsonObjs, String[] specKeys, int limit) {
        return getAll(jsonObjs, Arrays.asList(specKeys), limit);
    }

    /**
     * 从json对象中取出指定key值集合对应的json属性，并且加上条数限制
     *
     * @param jsonObj  指定json对象
     * @param specKeys key值集合
     * @param limit    条数限制
     * @return
     */
    public static List<String> getAll(JSONObject jsonObj, List<String> specKeys, int limit) {
        List<String> data = new ArrayList<>();

        if (jsonObj == null || jsonObj.isEmpty() || specKeys == null || specKeys.isEmpty() || limit < 1) {
            return data;
        }

        int index = 0;
        for (String specKey : specKeys) {
            data.add(getString(jsonObj, specKey,""));
            index++;

            if (index >= limit) {
                break;
            }
        }

        return data;
    }

    /**
     * METHOD_DESCRIPTION
     *
     * @param jsonObj  PARAM_DESC
     * @param specKeys PARAM_DESC
     * @param limit    PARAM_DESC
     * @return 从当前json对象中取出的属性集合
     */
    public static List<String> getAll(JSONObject jsonObj, String[] specKeys, int limit) {
        return getAll(jsonObj, Arrays.asList(specKeys), limit);
    }

    /**
     * 从json数组中取出指定key对应的整数属性，先取出字符串，过滤掉无效字符后再进行整数转换
     *
     * @param jsonObjs 指定的jsonArray
     * @param specKey  指定key
     * @return 整数属性的集合
     */
    public static List<Integer> getIntegerFromJsonArray(JSONArray jsonObjs, String specKey) {
        List<Integer> data = new ArrayList<>();

        if (jsonObjs == null || jsonObjs.isEmpty() || StringUtils.isBlank(specKey)) {
            return data;
        }

        JSONObject jsonObj;
        for (Object object : jsonObjs) {
            jsonObj = (JSONObject) object;
            data.add(getInteger(jsonObj, specKey));
        }

        return data;
    }

    /**
     * 将指定json字符串转换为恰当的json数组
     *
     * @param jsonStr json数组格式的字符串
     * @return 转换后得到的json数组，如果转换失败或者发生异常，返回 new JSONArray()，便于遍历
     */
    public static JSONArray getJsonArray(String jsonStr) {
        try {
            return JSONArray.fromObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONArray();
    }

    /**
     * 将指定json字符串转换为恰当的json对象
     *
     * @param jsonStr json对象格式的字符串
     * @return 转换后的json对象，如果转换失败或者发生异常返回null
     */
    public static JSONObject getJsonObject(String jsonStr) {
        try {
            return JSONObject.fromObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从json数组中取出指定key对应的字符串属性集合，添加条数限制
     *
     * @param jsonObjs 指定json数组
     * @param specKey  指定key
     * @param limit    条数限制
     * @return 指定key对应的字符串属性集合，如果当前索引位置发生异常，字符串属性集合的当前位置为JSONAttrGetter.DEFAULT_STR_VALUE “-”
     */
    public static List<String> getStringFromJsonArray(JSONArray jsonObjs, String specKey, int limit) {
        List<String> data = new ArrayList<>();

        if (jsonObjs == null || jsonObjs.isEmpty() || StringUtils.isBlank(specKey)) {
            return data;
        }

        JSONObject jsonObj;
        int index = 0;
        for (Object object : jsonObjs) {
            if (index >= limit) {
                break;
            }

            jsonObj = (JSONObject) object;
            data.add(getString(jsonObj, specKey));
            index++;
        }

        return data;
    }

}
