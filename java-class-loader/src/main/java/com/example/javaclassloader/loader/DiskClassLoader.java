package com.example.javaclassloader.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jianganwei
 * @program study-v2
 * @description 自定义classloader 加载指定路径的class 复写findClass 方法
 * @date 2019-01-04 14:32
 **/
public class DiskClassLoader extends ClassLoader{
    protected String mLibPath;

    public DiskClassLoader(String path) {
        // TODO Auto-generated constructor stub
        mLibPath = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO Auto-generated method stub

        String fileName = getFileName(name);

        File file = new File(mLibPath,fileName);

        try {
            FileInputStream is = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            is.close();
            bos.close();

            return defineClass(name,data,0,data.length);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    //获取要加载 的class文件名
    private static String getFileName(String name) {
        // TODO Auto-generated method stub
        int index = name.lastIndexOf('.');
        if(index == -1){
            return name+".class";
        }else{
            return name.substring(index+1)+".class";
        }
    }

    public static void main(String[] args) throws Exception{
        while (true){

            Thread.sleep(100);
            DiskClassLoader diskClassLoader=new DiskClassLoader("/home/jianganwei/IdeaProjects/study-v2/java-class-loader/src/main/java/com/example/javaclassloader/loader/demo");
            try {
                //加载class文件
                Class c = diskClassLoader.loadClass("com.example.javaclassloader.loader.demo.demo");

                if(c != null){
                    try {
                        Object obj = c.newInstance();
                        Method method = c.getDeclaredMethod("sout",null);
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
