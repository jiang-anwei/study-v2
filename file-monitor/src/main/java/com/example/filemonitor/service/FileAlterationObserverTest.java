package com.example.filemonitor.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by 蒋安维 on 2014/6/1.
 */
public class FileAlterationObserverTest {
private static Logger logger= LoggerFactory.getLogger(FileAlterationObserverTest.class);
    public static void main(String[] args) throws Exception {


        FileAlterationObserverTest fileAlter = new FileAlterationObserverTest();
        fileAlter.test();
        String filePath = "/home/jianganwei/logs/demo/";
        File file = new File(filePath);
        FileUtils.deleteDirectory(file);
        file.mkdirs();
        //File txtFile = new File(filePath+File.separator+System.currentTimeMillis()+".txt");
        //FileUtils.touch(txtFile);
//        File txtFile = new File("C:/Users/yezi/Desktop/test/1401629335839.txt");
//        txtFile.deleteOnExit();
//        FileUtils.touch(txtFile);

//        File newFile = new File(filePath+File.separator+System.currentTimeMillis());
//        newFile.mkdirs();

        boolean flag = true;
        while (flag) {
            /***测试文件的变化代码*/
//            File newFileTxt = new File(filePath+File.separator+System.currentTimeMillis()+".txt");
//            FileUtils.touch(newFileTxt);
//            Thread.sleep(2000);
//            FileUtils.write(newFileTxt,"1",true);
//            Thread.sleep(2000);
//            newFileTxt.delete();
            Thread.sleep(1000);


            /****测试文件夹的变化代码***/
//            File newFile = new File(filePath+File.separator+System.currentTimeMillis());
//            newFile.mkdir();
//            Thread.sleep(2000);
//            File newFileTxt = new File(newFile.getAbsolutePath()+File.separator+System.currentTimeMillis()+".txt");
//            FileUtils.touch(newFileTxt);
        }
    }

    public void test() throws Exception {
        String filePath = "/home/jianganwei/logs/demo/";
        FileFilter filter = FileFilterUtils.and(new MyFileFilter());
        FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(filePath, filter);
        fileAlterationObserver.addListener(new FileAlterationListenerAdaptor() {

            @Override
            public void onStart(FileAlterationObserver observer) {
                logger.info("start on file {}");
                super.onStart(observer);
            }

            @Override
            public void onDirectoryDelete(File directory) {
                logger.info("delete file {}",directory.getAbsolutePath());
                super.onDirectoryDelete(directory);
            }

            @Override
            public void onDirectoryCreate(File directory) {
                logger.info("create file {}",directory.getAbsolutePath());
                super.onDirectoryCreate(directory);
            }

            @Override
            public void onDirectoryChange(File directory) {
                logger.info("change file {}",directory.getAbsolutePath());
                super.onDirectoryChange(directory);
            }

            @Override
            public void onFileCreate(File file) {
                logger.info("file create {}",file.getAbsolutePath());
                super.onFileCreate(file);
            }

            @Override
            public void onFileDelete(File file) {
                logger.info("file delete {}",file.getAbsolutePath());
                super.onFileDelete(file);
            }

            @Override
            public void onFileChange(File file) {
                logger.info("file change {}" + file.getAbsolutePath());
                super.onFileChange(file);
            }
        });
        FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(1000);
        fileAlterationMonitor.addObserver(fileAlterationObserver);
        fileAlterationMonitor.start();
    }
}

/***
 * 自定义的文件过滤器
 */
class MyFileFilter implements IOFileFilter {

    @Override
    public boolean accept(File file) {

        return true;
    }

    @Override
    public boolean accept(File dir, String name) {

        return true;
    }

}