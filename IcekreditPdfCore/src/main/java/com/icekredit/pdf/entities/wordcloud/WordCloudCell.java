package com.icekredit.pdf.entities.wordcloud;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.ImageResourceLoader;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.Dimension;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by icekredit on 6/29/16.
 */
public class WordCloudCell extends AlignCenterCell implements PdfPCellEvent {
    protected List<WordFrequency> wordFrequencies;

    protected String backgroundImageName;

    public WordCloudCell(List<WordFrequency> wordFrequencies, String backgroundImageName) {
        this.wordFrequencies = wordFrequencies;
        this.backgroundImageName = backgroundImageName;

        this.setFixedHeight(200);
        this.setCellEvent(this);
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "results/object/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));
            document.open();

            List<WordFrequency> wordFrequencies = new ArrayList<WordFrequency>();

            String s = "Youth is not a time of life; it is a state of mind; it is not a matter of rosy cheeks, red lips and supple knees; it is a matter of the will, a quality of the imagination, a vigor of the emotions; it is the freshness of the deep springs of life. " +
                    "Youth means a temperamental predominance of courage over timidity, of the appetite for adventure over the love of ease. This often exists in a man of 60 more than a boy of 20. Nobody grows old merely by a number of years. We grow old by deserting our ideals. " +
                    "Years may wrinkle the skin, but to give up enthusiasm wrinkles the soul. Worry, fear, self-distrust bows the heart and turns the spirit back to dust. " +
                    "Whether 60 or 16, there is in every human being’s heart the lure of wonders, the unfailing appetite for what’s next and the joy of the game of living. In the center of your heart and my heart, there is a wireless station; so long as it receives messages of beauty, hope, courage and power from man and from the infinite, so long as you are young. " +
                    "When your aerials are down, and your spirit is covered with snows of cynicism and the ice of pessimism, then you’ve grown old, even at 20; but as long as your aerials are up, to catch waves of optimism, there’s hope you may die young at 80. ";

            String[] datas = s.split(" ");

            for (int i = 0; i < datas.length; i++) {
                wordFrequencies.add(new WordFrequency(datas[i % datas.length], ((int) (Math.random() * 10) + 1) * ((int) (Math.random() * 10) + 1)));
            }

            WordCloudCell wordCloudCell = new WordCloudCell(wordFrequencies, "whale_small.png");
            wordCloudCell.setFixedHeight(200);
            wordCloudCell.setColspan(12);

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.addCell(wordCloudCell);

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            Image backgroundImage = Image.getInstance(ImageResourceLoader.getSingleInstance().loadImage(backgroundImageName));

            final Dimension dimension = new Dimension((int) backgroundImage.getWidth(), (int) backgroundImage.getHeight());
            final com.kennycason.kumo.WordCloud wordCloud = new com.kennycason.kumo.WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setPadding(0);
            wordCloud.setBackgroundColor(new Color(0xff, 0xff, 0xff, 0xff));

            wordCloud.setBackground(new PixelBoundryBackground(WordCloudCell.class.getClassLoader().getResourceAsStream(backgroundImageName)));

            Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

            int randomRed = 0;
            int randomGreen = 0;
            int randomBlue = 0;

            int minFrequency = Integer.MAX_VALUE;
            int maxFrequency = Integer.MIN_VALUE;
            int maxGrayValue = 171;

            for (WordFrequency wordFrequency : wordFrequencies) {
                minFrequency = Math.min(wordFrequency.getFrequency(), minFrequency);
                maxFrequency = Math.max(wordFrequency.getFrequency(), maxFrequency);
            }

            if (maxFrequency == minFrequency) {
                maxFrequency++;
            }

            int redReduceRatio = 10;
            int greenReduceRatio = 10;
            int blueReduceRatio = 25;

            for (WordFrequency wordFrequency : wordFrequencies) {
                if (colorMap.containsKey(wordFrequency.getFrequency())) {
                    continue;
                }

                if (colorMap.size() > 30) {
                    break;
                }

                randomRed = (int) (maxGrayValue - (wordFrequency.getFrequency() - minFrequency) * redReduceRatio * Math.random() / (maxFrequency - minFrequency) * maxGrayValue);
                randomGreen = (int) (maxGrayValue - (wordFrequency.getFrequency() - minFrequency) * greenReduceRatio * Math.random() / (maxFrequency - minFrequency) * maxGrayValue);
                randomBlue = (int) (maxGrayValue - (wordFrequency.getFrequency() - minFrequency) * blueReduceRatio * Math.random() / (maxFrequency - minFrequency) * maxGrayValue);

                randomRed = Math.max(randomRed, 0);
                randomGreen = Math.max(randomGreen, 0);
                randomBlue = Math.max(randomBlue, 0);

                randomRed = Math.min(randomRed, maxGrayValue);
                randomGreen = Math.min(randomGreen, maxGrayValue);
                randomBlue = Math.min(randomBlue, maxGrayValue);

                colorMap.put(wordFrequency.getFrequency(), new Color(randomRed, randomGreen, randomBlue));
            }

            Color[] colors = new Color[colorMap.size()];
            int index = 0;
            for (Integer key : colorMap.keySet()) {
                colors[index] = colorMap.get(key);

                index++;
            }

            wordCloud.setColorPalette(new ColorPalette(colors));

            wordCloud.setFontScalar(new LinearFontScalar((int)getMinFontSize(wordFrequencies.size()), (int)getMaxFontSize(wordFrequencies.size())));
            wordCloud.setKumoFont(FontUtils.wordCloudKumoFont);

            wordCloud.setAngleGenerator(new AngleGenerator(new double[]{0 * Math.PI / 4}));
            /*wordCloud.setAngleGenerator(new AngleGenerator(new double[]{
                    0 * Math.PI / 180, 10 * Math.PI / 180,
                    20 * Math.PI / 180,30 * Math.PI / 180,
                    40 * Math.PI / 180,50 * Math.PI / 180,
                    60 * Math.PI / 180,70 * Math.PI / 180,
                    80 * Math.PI / 180,90 * Math.PI / 180,
                    0 * Math.PI / 180, -10 * Math.PI / 180,
                    -20 * Math.PI / 180,-30 * Math.PI / 180,
                    -40 * Math.PI / 180,-50 * Math.PI / 180,
                    -60 * Math.PI / 180,-70 * Math.PI / 180,
                    -80 * Math.PI / 180,-90 * Math.PI / 180}));*/

            wordCloud.build(wordFrequencies);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            wordCloud.writeToStreamAsPNG(byteArrayOutputStream);
            Image image = Image.getInstance(byteArrayOutputStream.toByteArray());

            float primitiveImageWidth = image.getPlainWidth();
            float primitiveImageHeight = image.getPlainHeight();

            float imageHWRatio = primitiveImageHeight / primitiveImageWidth;

            float scaleToWidth = primitiveImageWidth > position.getWidth() * 4 / 5 ? position.getWidth() * 4 / 5 : primitiveImageWidth;
            float scaleToHeight = scaleToWidth * imageHWRatio;

            scaleToHeight = scaleToHeight > position.getHeight() * 0.8f ? position.getHeight() * 0.8f : scaleToHeight;
            scaleToWidth = scaleToHeight / imageHWRatio;

            image.scaleAbsolute(scaleToWidth, scaleToHeight);

            image.setAbsolutePosition((position.getLeft() + position.getRight()) / 2 - scaleToWidth / 2,
                    (position.getTop() + position.getBottom()) / 2 - scaleToHeight / 2);

            ColumnText columnText = new ColumnText(canvases[PdfPTable.BASECANVAS]);
            columnText.setSimpleColumn((position.getLeft() + position.getRight()) / 2 - scaleToWidth / 2 - 10,
                    (position.getTop() + position.getBottom()) / 2 - scaleToHeight / 2 - 10,
                    (position.getLeft() + position.getRight()) / 2 + scaleToWidth / 2 + 10,
                    (position.getTop() + position.getBottom()) / 2 + scaleToHeight / 2 + 10);
            columnText.addElement(image);
            columnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float getMinFontSize(int size) {
        float minFontSize = 15;

        minFontSize += (size - 100) * 1.0 / 20;

        minFontSize = Math.max(minFontSize,15);
        minFontSize = Math.min(minFontSize,20);

        return minFontSize;
    }

    public float getMaxFontSize(int size) {
        float maxFontSize = 30;

        maxFontSize += (size - 100) * 1.0 / 20;
        maxFontSize = Math.max(maxFontSize,30);
        maxFontSize = Math.min(maxFontSize,40);

        return maxFontSize;
    }
}
