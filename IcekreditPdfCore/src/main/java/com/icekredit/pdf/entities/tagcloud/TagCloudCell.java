package com.icekredit.pdf.entities.tagcloud;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.EmptyCell;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.JSONAttrGetter;
import com.icekredit.pdf.utils.ResourceLoader;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by icekredit on 11/3/16.
 */
public class TagCloudCell extends BaseCell {
    protected List<Frequency> frequencies;
    protected int parentOccupyColumn;

    private static final int DEFAULT_MIN_FREQUENCY = 0;
    private static final int DEFAULT_MAX_FREQUENCY = 100;

    public TagCloudCell(List<Frequency> frequencies,int parentOccupyColumn) {
        this.frequencies = frequencies;
        this.parentOccupyColumn = parentOccupyColumn;

        PdfPTable wrapperTable = new PdfPTable(12);
        wrapperTable.setWidthPercentage(100);
        wrapperTable.setSplitRows(false);
        wrapperTable.setSplitLate(true);

        try {
            if(this.frequencies.size() != 0){
                List<BaseCell> tagCloudItemCells = new ArrayList<BaseCell>();

                initTagCloudItemCells(tagCloudItemCells);

                PlaceTooImpl placeTool = new PlaceTooImpl(tagCloudItemCells);
                placeTool.initialize();
                placeTool.place();

                for(BaseCell baseCell:placeTool.collection()){
                    wrapperTable.addCell(baseCell);
                }
            } else {
                throw new Exception("抱歉没有数据用于构建标签云!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        this.addElement(wrapperTable);
    }

    private void initTagCloudItemCells(List<BaseCell> tagCloudItemCells) {
        List<Frequency> normalizedFrequencies = new ArrayList<Frequency>();

        normalizePrimitiveFrequencies(normalizedFrequencies);

        for(Frequency frequency:normalizedFrequencies){
            if(frequency.frequency >= DEFAULT_MIN_FREQUENCY
                    && frequency.frequency <= DEFAULT_MAX_FREQUENCY + 1
                    && frequency.content != null
                    && !frequency.content.trim().equals("")){
                tagCloudItemCells.add(getTagCloudItemCell(frequency));
            }
        }
    }

    private BaseCell getTagCloudItemCell(Frequency frequency) {
        Font font = new Font(FontUtils.baseFontChinese,FontUtils.fontSize,Font.NORMAL, BaseColor.WHITE);//new BaseColor(0x00,0x45,0x85,0xff));
        int rowSpan = 0;
        int columnSpan = 0;
        int minFontSize = 6;
        int step = 3;
        
        if(frequency.frequency < 10){
            rowSpan = 1;
            columnSpan = 1;
            font.setSize(minFontSize);
        } else if(frequency.frequency < 20){
            rowSpan = 1;
            columnSpan = 2;
            font.setSize(minFontSize + step * 1);

        }else if(frequency.frequency < 30){
            rowSpan = 1;
            columnSpan = 3;
            font.setSize(minFontSize + step * 2);

        }else if(frequency.frequency < 40){
            rowSpan = 2;
            columnSpan = 2;
            font.setSize(minFontSize + step * 3);

        }else if(frequency.frequency < 60){
            rowSpan = 2;
            columnSpan = 3;
            font.setSize(minFontSize + step * 4);

        }else if(frequency.frequency < 80){
            rowSpan = 2;
            columnSpan = 4;
            font.setSize(minFontSize + step * 5);
        } else {
            rowSpan = 3;
            columnSpan = 3;
            font.setSize(minFontSize + step * 6);
        }

        adjustFontSizeByStringLength(font);

        TagCloudItemCell tagCloudItemCell = new TagCloudItemCell(parentOccupyColumn,frequency.content,font,rowSpan,columnSpan, TagCloudItemCell.DEFAULT_BG_COLOR);
        tagCloudItemCell.setRowSpan(rowSpan);
        tagCloudItemCell.setColumnSpan(columnSpan);

        return tagCloudItemCell;
    }

    /**
     * 根据字符串长度调整字体显示大小，目前未做处理
     * @param font
     */
    private void adjustFontSizeByStringLength(Font font) {

    }

    private void normalizePrimitiveFrequencies(List<Frequency> normalizedFrequencies) {
        int minFrequency = Integer.MAX_VALUE;

        int maxFrequency = Integer.MIN_VALUE;

        for(Frequency frequency:frequencies){
            if(minFrequency > frequency.frequency){
                minFrequency = frequency.frequency;
            }

            if(maxFrequency < frequency.frequency){
                maxFrequency = frequency.frequency;
            }
        }

        if(minFrequency == maxFrequency){
            maxFrequency = maxFrequency * 11;
        }

        float normalizeRatio = (float) Math.pow(maxFrequency - minFrequency + 1,2);

        for (Frequency frequency:frequencies){
            normalizedFrequencies.add(
                    new Frequency((int) Math.ceil(
                            (float) Math.pow(frequency.frequency - minFrequency,2) / normalizeRatio * (DEFAULT_MAX_FREQUENCY - DEFAULT_MIN_FREQUENCY)),frequency.content));
        }
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "results/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);

            List<Frequency> list = new ArrayList<>();

            JSONArray tagCloudJsonObjs = JSONAttrGetter.getJsonArray(ResourceLoader.getJsonStr("tagcloud.json"));
            for(Object obj:tagCloudJsonObjs){
                list.add(new Frequency(JSONAttrGetter.getInteger((JSONObject) obj,"frequency",0),
                        JSONAttrGetter.getString((JSONObject) obj,"content","")));
            }

            TagCloudCell tagCloudCell = new TagCloudCell(list,8);
            tagCloudCell.setColspan(10);

            mainFrame.addCell(new EmptyCell(1));
            mainFrame.addCell(tagCloudCell);
            mainFrame.addCell(new EmptyCell(1));

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class Frequency{
        protected int frequency;
        protected String content;

        public Frequency() {
            this(1,"");
        }

        public Frequency(int frequency, String content) {
            this.frequency = frequency;
            this.content = content;
        }

        @Override
        public String toString() {
            return "Frequency{" +
                    "frequency=" + frequency +
                    ", content='" + content + '\'' +
                    '}';
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
