package com.example.javaclassloader.loader;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jianganwei
 * @program study-v2
 * @description 热加载classloader
 * @date 2019-01-04 15:19
 **/
public class HotSwapClassLoader extends DiskClassLoader {
    public HotSwapClassLoader(String path) {
        super(path);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class clazz = findLoadedClass(name);
        if (null == clazz) {
            if (name.startsWith("com.example.javaclassloader.loader.demo")) {
                System.out.println("加载类："+name);
                clazz = super.findClass(name);
            } else {
                clazz = getSystemClassLoader().loadClass(name);
            }

        } else {
            System.out.println("缓存");
        }
        if (resolve) {
            super.resolveClass(clazz);
        }

        return clazz;
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader("/home/jianganwei/IdeaProjects/study-v2/java-class-loader/src/main/java/com/example/javaclassloader/loader/demo");

            System.out.println("sys->" + Thread.currentThread().getContextClassLoader());
            Thread.sleep(2000);
            try {
                //加载class文件
                Class c = hotSwapClassLoader.loadClass("com.example.javaclassloader.loader.demo.demo",true);

                if (c != null) {
                    try {
                        Object obj = c.newInstance();
                        Method method = c.getDeclaredMethod("sout", null);
                        //通过反射调用Test类的say方法
                        method.invoke(obj, null);
                    } catch (InstantiationException | IllegalAccessException
                            | NoSuchMethodException
                            | SecurityException |
                            IllegalArgumentException |
                            InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
