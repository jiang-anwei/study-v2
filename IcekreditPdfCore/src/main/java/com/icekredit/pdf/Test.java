package com.icekredit.pdf;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.entities.KeyValueCell;
import com.icekredit.pdf.service.PdfReportService;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Created by icekredit on 7/25/16.
 */
public class Test {
    private static PdfReportService entPdfReportService = null;

    private static void testImageShow() {
        try {
            String imageUrl = "http://120.26.110.79:54322/api/companyAPI/trademark?token_id=278446ea8c77ad49cbc6&fileName=data/assets/trademark/34cd66dbc49c927bfbadd029afdd8c62.jpg";

            String destFileStr = "output/report.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));
            document.open();

            PdfContentByte canvas = pdfWriter.getDirectContent();

            Image image = Image.getInstance(imageUrl);
            image.setAbsolutePosition(0, 0);
            image.scaleAbsolute(100, 300);

            canvas.addImage(image);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class MyThreadLocal<Integer> extends ThreadLocal<Integer> {
        private static MyThreadLocal<java.lang.Integer> myThreadLocal = null;

        public static MyThreadLocal<java.lang.Integer> getSingleInstance() {
            if (myThreadLocal == null) {
                myThreadLocal = new MyThreadLocal<java.lang.Integer>();
            }

            return myThreadLocal;
        }
    }


    public static class MyRunnable implements Runnable {
        protected MyThreadLocal myThreadLocal = null;

        int max;

        public MyRunnable(int max) {
            this.max = max;
            this.myThreadLocal = new MyThreadLocal();
        }

        @Override
        public void run() {
            int index = 0;

            while (index < 100) {
                myThreadLocal.set(index);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                index++;
            }

            System.out.println(myThreadLocal.get());
        }
    }

    public static void testThreadLocal() {
        try {
            Thread thread1 = new Thread(new MyRunnable(100));
            Thread thread2 = new Thread(new MyRunnable(50));

            thread1.start();
            thread2.start();

            thread1.join(); //wait for thread 1 to terminate
            thread2.join(); //wait for thread 2 to terminate
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testPdfOutline() {
        try {
            String destFileStr = "result/pdfoutline.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));
            pdfWriter.setViewerPreferences(PdfWriter.PageModeUseOutlines);

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            AlignCenterCell alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文First", FontUtils.chineseFont).setLocalDestination(new String("中文First".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文First1", FontUtils.chineseFont).setLocalDestination(new String("中文First1".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文First12", FontUtils.chineseFont).setLocalDestination(new String("中文First12".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();


            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Second", FontUtils.chineseFont).setLocalDestination(new String("中文Second".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Second1", FontUtils.chineseFont).setLocalDestination(new String("中文Second1".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Second12", FontUtils.chineseFont).setLocalDestination(new String("中文Second12".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();


            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Three", FontUtils.chineseFont).setLocalDestination(new String("中文Three".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Three1", FontUtils.chineseFont).setLocalDestination(new String("中文Three1".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Three12", FontUtils.chineseFont).setLocalDestination(new String("中文Three12".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();


            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Four", FontUtils.chineseFont).setLocalDestination(new String("中文Four".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Four1", FontUtils.chineseFont).setLocalDestination(new String("中文Four1".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文Four12", FontUtils.chineseFont).setLocalDestination(new String("中文Four12".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();


            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("Five", FontUtils.chineseFont).setLocalDestination(new String("Five".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("Five1", FontUtils.chineseFont).setLocalDestination(new String("Five1".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();

            mainFrame = new PdfPTable(12);
            alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("Five12", FontUtils.chineseFont).setLocalDestination(new String("Five12".getBytes("ISO-8859-1"), "utf-8"))));
            alignCenterCell.setColspan(12);
            mainFrame.addCell(alignCenterCell);
            document.add(mainFrame);
            document.newPage();


            PdfOutline root = pdfWriter.getRootOutline();

            PdfOutline pdfOutline = new PdfOutline(root, PdfAction.gotoLocalPage(new String("中文First".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文First".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文First1".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文First1".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文First12".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文First12".getBytes(), "utf-8"));

            pdfOutline = new PdfOutline(root, PdfAction.gotoLocalPage(new String("中文Second".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Second".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文Second1".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Second1".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文Second12".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Second12".getBytes(), "utf-8"));

            pdfOutline = new PdfOutline(root, PdfAction.gotoLocalPage(new String("中文Three".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Three".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文Three1".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Three1".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文Three12".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Three12".getBytes(), "utf-8"));

            pdfOutline = new PdfOutline(root, PdfAction.gotoLocalPage(new String("中文Four".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Four".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文Four1".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Four1".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("中文Four12".getBytes("ISO-8859-1"), "utf-8"), false), new String("中文Four12".getBytes(), "utf-8"));

            pdfOutline = new PdfOutline(root, PdfAction.gotoLocalPage(new String("Five".getBytes("ISO-8859-1"), "utf-8"), false), new String("Five".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("Five1".getBytes("ISO-8859-1"), "utf-8"), false), new String("Five1".getBytes(), "utf-8"));
            pdfOutline = new PdfOutline(pdfOutline, PdfAction.gotoLocalPage(new String("Five12".getBytes("ISO-8859-1"), "utf-8"), false), new String("Five12".getBytes(), "utf-8"));


            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testkeyValueCell() {
        try {
            String destFileStr = "result/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            KeyValueCell cell = new KeyValueCell("猪脚城市", " \n \n \n \n \n \n");
            cell.setColspan(4);
            mainFrame.addCell(cell);

            KeyValueCell keyValueCell = new KeyValueCell("", "四川省成都市    四川省成都市\n四川省成都市 四川省成都市\n四川省成都市 四川省成都市\n四川省成都市 四川省成都市\n四川省成都市 四川省成都市\n", "", "", 1, 4,
                    FontUtils.baseFontChinese.getWidthPoint("", 10));
            keyValueCell.setColspan(4);
            keyValueCell.setRowspan(keyValueCell.getLinesCount());

            KeyValueCell keyValueCell1 = new KeyValueCell("", "四川省成都市     四川省成都市\n四川省成都市    四川省成都市\n四川省成都市    四川省成都市\n\n\n", "", "", 1, 4,
                    FontUtils.baseFontChinese.getWidthPoint("", 10));
            keyValueCell1.setRowspan(keyValueCell1.getLinesCount());
            keyValueCell1.setColspan(4);

            int rowSpan = keyValueCell.getLinesCount();
            int rowSpan1 = keyValueCell1.getLinesCount();

            if (rowSpan == rowSpan1) {
                mainFrame.addCell(keyValueCell);
                mainFrame.addCell(keyValueCell1);
            } else if (rowSpan > rowSpan1) {
                mainFrame.addCell(keyValueCell);
                mainFrame.addCell(keyValueCell1);

                KeyValueCell emptyCell = new KeyValueCell("", "");
                emptyCell.setColspan(4);
                emptyCell.setRowspan(rowSpan - rowSpan1);
                mainFrame.addCell(emptyCell);
            }

            AlignCenterCell alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("ss")));
            alignCenterCell.setColspan(12);
//            mainFrame.addCell(alignCenterCell);

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void helloWorld() {
        System.out.println(randomString(-229985452) + "," + randomString(-147909649));
    }

    public static String randomString(int i) {
        Random ran = new Random(i);

        StringBuilder sb = new StringBuilder();

        while (true) {
            int nextRandomIntValue = ran.nextInt(27);

            if (nextRandomIntValue == 0) {
                break;
            }

            sb.append((char) ('`' + nextRandomIntValue));
        }

        return sb.toString();
    }
}
