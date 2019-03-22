package com.icekredit.pdf.entities.tagcloud;

import com.icekredit.pdf.entities.BaseCell;

import java.util.List;

/**
 * Created by icekredit on 11/3/16.
 */
public interface PlaceTool {
    /**
     * 初始化用于计算单元格放置方式的矩阵空间
     */
    void initialize();

    /**
     * 将需要放置的单元格按照权重进行排序，大的单元格将被优先放置
     */
    void sort();

    /**
     * 如果当前所有单元格不能完全填充指定矩阵，在后面添加空白的权重为1的单元格，补全，判断
     * 标准是totalWeight % 12 == 0
     */
    void fill();

    /**
     * 为当前单元格查找一个合适的放置位置
     */
    Position findValidPositionForCurrentCell(int currentPlaceIndex);

    /**
     * 循环放置所有单元格，先查找一个位置，然后放置其实就是更新存储了单元格放置方式的矩阵空间
     */
    void place();

    /**
     * 放置当前单元格，并更新存储了单元格放置方式的矩阵空间指定位置
     */
    void consume(int hashCode, Position position, int rowSpan, int columnSpan);

    /**
     * 从存储了单元格放置方式的矩阵空间中收集实际计算出的放置方式，收集规则是
     * 遇到一个新的单元个就append到List中，遇到重复的（即rowSpan，columnSpan可能不为1的情况），
     * 其实这个单元格已经被放置了，不管
     *
     * @return 实际计算出的放置方式
     */
    List<BaseCell> collection();

    class Position{
        public int positionX;
        public int positionY;

        public Position() {
            this(Integer.MIN_VALUE,Integer.MIN_VALUE);
        }

        public Position(int positionX, int positionY) {
            this.positionX = positionX;
            this.positionY = positionY;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "positionX=" + positionX +
                    ", positionY=" + positionY +
                    '}';
        }
    }
}
