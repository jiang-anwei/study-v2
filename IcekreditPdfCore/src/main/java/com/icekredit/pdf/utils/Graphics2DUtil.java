package com.icekredit.pdf.utils;

import com.icekredit.pdf.entities.Point;

/**
 * 二维图像处理工具类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class Graphics2DUtil {
    public static void main(String[] args) {
        Point point = new Point(10, 10);

        Graphics2DUtil.rotateVector(point, (float) (Math.PI / 4));
    }

    /**
     * 参照（0,0）旋转指定向量
     *
     *
     * @param point 指定向量
     * @param rotateAngle 旋转角度
     */
    public static void rotateVector(Point point, float rotateAngle) {
        float tempX = point.x;
        float tempY = point.y;

        point.x = (float) (tempX * Math.cos(rotateAngle) + tempY * Math.sin(rotateAngle));
        point.y = (float) (tempY * Math.cos(rotateAngle) - tempX * Math.sin(rotateAngle));
    }

    /**
     * 参照referPoint旋转向量
     *
     *
     * @param point 指定向量
     * @param referPoint 参考点
     * @param rotateAngle 旋转角度
     */
    public static void rotateVector(Point point, Point referPoint, float rotateAngle) {
        float tempX = point.x - referPoint.x;
        float tempY = point.y - referPoint.y;

        point.x = (float) (tempX * Math.cos(rotateAngle) + tempY * Math.sin(rotateAngle)) + referPoint.x;
        point.y = (float) (tempY * Math.cos(rotateAngle) - tempX * Math.sin(rotateAngle)) + referPoint.y;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
