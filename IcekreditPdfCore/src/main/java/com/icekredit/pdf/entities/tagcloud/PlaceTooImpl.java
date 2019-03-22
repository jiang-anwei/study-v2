package com.icekredit.pdf.entities.tagcloud;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.EmptyCell;

import java.util.*;

/**
 * Created by icekredit on 11/3/16.
 */
public class PlaceTooImpl implements PlaceTool {
    protected static final int TOTAL_COLUMN = 12;

    protected List<BaseCell> cellsToPlace;

    protected int[][] placeWayHashCodeArray = null;

    protected static final int NOT_PLACED_MARK_VALUE = Integer.MIN_VALUE;

    protected Map<Integer,BaseCell> map = new HashMap<Integer, BaseCell>();


    @Override
    public void initialize() {
        int totalRow = getTotalRowsCount();

        placeWayHashCodeArray = new int[totalRow][TOTAL_COLUMN];

        for (int index = 0; index < totalRow; index++) {
            for (int position = 0; position < TOTAL_COLUMN; position++) {
                placeWayHashCodeArray[index][position] = NOT_PLACED_MARK_VALUE;
            }
        }
    }

    @Override
    public void sort() {
        Collections.sort(cellsToPlace, new Comparator<BaseCell>() {
            @Override
            public int compare(BaseCell o1, BaseCell o2) {
                return ((TagCloudItemCell) o2).getWeight() - ((TagCloudItemCell)o1).getWeight();
            }
        });
    }

    @Override
    public void fill() {
        int totalRowCount = getTotalRowsCount();

        int cellCountNeedToFill = totalRowCount * TOTAL_COLUMN - getTotalWeight(cellsToPlace);

        if(totalRowCount == 0 || cellCountNeedToFill == 0){
            return;
        }

        //如果只有一行数据，那么做添加单元格操作
        if(totalRowCount == 1){
            for (int index = 0; index < cellCountNeedToFill; index++) {
                cellsToPlace.add(new TagCloudItemCell(8,""));
            }
        } else {    //如果有多行数据
            if(getTotalRowsCount() == getMaxRowSpan()){    //如果最大的行跨度等于最大行数，为了显示最大行跨度的单元格，必须填充数据
                for (int index = 0; index < cellCountNeedToFill; index++) {
                    cellsToPlace.add(new TagCloudItemCell(8,""));
                }
            } else {
                for (int index = 0; index < 12 - cellCountNeedToFill; index++) {
                    cellsToPlace.remove(cellsToPlace.size() - 1);
                }
            }
        }


        /*//如果需要填充的格子数目小于等于4,那么在后面填充单元格
        if(cellCountNeedToFill <= 4){
            for (int index = 0; index < cellCountNeedToFill; index++) {
                cellsToPlace.add(new TagCloudItemCell(8,""));

                System.out.println("add");
            }
        }

        //如果需要填充的格子数目大于4,那么取消填充，转而删除末尾多余的权重为1的单元格，此处设计稍微有点问题，此处默认最后面的单元格权重为一
        if(cellCountNeedToFill > 4){
            for (int index = 0; index < 12 - cellCountNeedToFill; index++) {
                cellsToPlace.remove(cellsToPlace.size() - 1);

                System.out.println("remove");
            }
        }*/
    }

    @Override
    public Position findValidPositionForCurrentCell(int currentPlaceIndex) {
        BaseCell currentCell = this.cellsToPlace.get(currentPlaceIndex);

        int rowSpan = getRowSpan(currentCell);
        int columnSpan = getColumnSpan(currentCell);

        int totalColumn = TOTAL_COLUMN;
        int totalRow = getTotalRowsCount();

        int centerXPosition = Math.min((totalColumn - 1) / 2,totalColumn - getMaxColumnSpan()) - 1;
        int centerYPosition = Math.min((totalRow - 1) / 2,totalRow - getMaxRowSpan());

        Position validPosition = new Position();

        boolean isFound = false;

        int maxScale = Math.max((totalColumn + 2) / 2, (totalRow + 1) / 2);

        int currentXPosition = 0;
        int currentYPosition = 0;
        for (int scale = 0; scale <= maxScale && !isFound; scale++) {
            if (scale == 0) {
                validPosition.positionX = centerXPosition;
                validPosition.positionY = centerYPosition;

                isFound = !hasConflict(validPosition,rowSpan,columnSpan);
            }

            //右
            currentXPosition = centerXPosition + scale;
            for (currentYPosition = centerYPosition - scale; currentYPosition <= centerYPosition + scale && !isFound; currentYPosition++) {
                if (currentXPosition < 0 || currentXPosition > totalColumn - 1){
                    continue;
                }

                if(currentYPosition < 0 || currentYPosition > totalRow - 1){
                    continue;
                }

                if(placeWayHashCodeArray[currentYPosition][currentXPosition] == Integer.MIN_VALUE
                        && (currentXPosition + columnSpan) <= totalColumn
                        && (currentYPosition + rowSpan) <= totalRow){
                    validPosition.positionX = currentXPosition;
                    validPosition.positionY = currentYPosition;

                    isFound = !hasConflict(validPosition,rowSpan,columnSpan);
                }
            }

            //下
            currentYPosition = centerYPosition + scale;
            for (currentXPosition = centerXPosition + scale; currentXPosition >= centerXPosition - scale && !isFound; currentXPosition--) {
                if(currentYPosition < 0 || currentYPosition > totalRow - 1){
                    continue;
                }

                if(currentXPosition < 0 || currentXPosition > totalColumn - 1){
                    continue;
                }

                if(placeWayHashCodeArray[currentYPosition][currentXPosition] == Integer.MIN_VALUE
                        && (currentXPosition - columnSpan) >= -1
                        && (currentYPosition + rowSpan) <= totalRow){
                    validPosition.positionX = currentXPosition - columnSpan + 1;
                    validPosition.positionY = currentYPosition;

                    isFound = !hasConflict(validPosition,rowSpan,columnSpan);
                }
            }

            //左
            currentXPosition = centerXPosition - scale;
            for (currentYPosition = centerYPosition + scale; currentYPosition >= centerYPosition - scale && !isFound; currentYPosition--) {
                if (currentXPosition < 0 || currentXPosition > totalColumn - 1){
                    continue;
                }

                if(currentYPosition < 0 || currentYPosition > totalRow - 1){
                    continue;
                }

                if(placeWayHashCodeArray[currentYPosition][currentXPosition] == Integer.MIN_VALUE
                        && (currentXPosition - columnSpan) >= -1
                        && (currentYPosition - rowSpan) >= -1){
                    validPosition.positionX = currentXPosition - columnSpan + 1;
                    validPosition.positionY = currentYPosition - rowSpan + 1;

                    isFound = !hasConflict(validPosition,rowSpan,columnSpan);
                }
            }

            //上
            currentYPosition = centerYPosition - scale;
            for (currentXPosition = centerXPosition - scale; currentXPosition <= centerXPosition + scale && !isFound; currentXPosition++) {
                if(currentYPosition < 0 || currentYPosition > totalRow - 1){
                    continue;
                }

                if (currentXPosition < 0 || currentXPosition > totalColumn - 1){
                    continue;
                }

                if(placeWayHashCodeArray[currentYPosition][currentXPosition] == Integer.MIN_VALUE
                        && (currentXPosition + columnSpan) <= totalColumn
                        && (currentYPosition - rowSpan) >= -1){
                    validPosition.positionX = currentXPosition;
                    validPosition.positionY = currentYPosition - rowSpan + 1;


                    isFound = !hasConflict(validPosition,rowSpan,columnSpan);
                }
            }
        }

        return validPosition;
    }

    private int getTotalRowsCount() {
        int totalRowsCount = (getTotalWeight(cellsToPlace) + TOTAL_COLUMN - 1) / TOTAL_COLUMN;

        int maxRowSpan = getMaxRowSpan();

        return Math.max(totalRowsCount,maxRowSpan);
    }

    private int getMaxRowSpan() {
        int maxRowSpan = Integer.MIN_VALUE;
        for(BaseCell baseCell:cellsToPlace){
            if(maxRowSpan < baseCell.getRowspan()){
                maxRowSpan = baseCell.getRowspan();
            }
        }

        return maxRowSpan;
    }

    private int getMaxColumnSpan() {
        int maxColumnSpan = Integer.MIN_VALUE;

        for(BaseCell baseCell:cellsToPlace){
            if(maxColumnSpan < baseCell.getColspan()){
                maxColumnSpan = baseCell.getColspan();
            }
        }

        return maxColumnSpan;
    }

    private boolean hasConflict(Position validPosition, int rowSpan, int columnSpan) {
        for(int index =0 ;index < rowSpan;index ++){
            for(int position = 0;position < columnSpan;position ++){

                //如果rowSpan × columnSpan这个矩形内存在已经被分配的空间，那么表明存在冲突
                if(placeWayHashCodeArray[validPosition.positionY + index][validPosition.positionX + position] != Integer.MIN_VALUE){
                    return true;
                }
            }
        }

        return false;
    }

    private static int getRowSpan(BaseCell currentCell) {
        if (currentCell instanceof TagCloudItemCell) {
            return ((TagCloudItemCell) currentCell).getRowSpan();
        }

        if (currentCell instanceof EmptyCell) {
            return 1;
        }

        return 0;
    }

    private static int getColumnSpan(BaseCell currentCell) {
        if (currentCell instanceof TagCloudItemCell) {
            return ((TagCloudItemCell) currentCell).getColumnSpan();
        }

        if (currentCell instanceof EmptyCell) {
            return 1;
        }

        return 0;
    }

    @Override
    public void consume(int hashCode, Position consumedPosition, int rowSpan, int columnSpan) {
        for(int index = 0;index < rowSpan;index ++){
            for(int position = 0;position < columnSpan;position ++){
                if(consumedPosition.positionY + index == 14 || consumedPosition.positionY + index == 15){
                    if(consumedPosition.positionX + position >= 1 && consumedPosition.positionX + position <= 3){
                        System.out.println(hashCode + " " + ((TagCloudItemCell)map.get(hashCode)).getTag());
                    }
                }

                placeWayHashCodeArray[consumedPosition.positionY + index][consumedPosition.positionX + position] = hashCode;
            }
        }
    }

    @Override
    public void place() {
        this.sort();
        this.fill();

        int index = 0;
        for(BaseCell baseCell:cellsToPlace){
            consume(baseCell.hashCode(),findValidPositionForCurrentCell(index),getRowSpan(baseCell),getColumnSpan(baseCell));

            index ++;
        }
    }

    @Override
    public List<BaseCell> collection() {
        List<BaseCell> placedTagCloudItemsCells = new ArrayList<BaseCell>();

        Map<Integer,BaseCell> map = new HashMap<Integer, BaseCell>();
        for(BaseCell baseCell:cellsToPlace){
            if(((TagCloudItemCell)baseCell).getTag().trim().equals("")){
                map.put(baseCell.hashCode(),new EmptyCell(1));

                continue;
            }

            map.put(baseCell.hashCode(),baseCell);
        }

        List<Integer> orderedCellsPlaceWayList = new ArrayList<Integer>();

        int totalRow = getTotalRowsCount();
        int totalColumn = TOTAL_COLUMN;
        for(int index = 0;index < totalRow;index ++){
            for (int position = 0;position < totalColumn;position ++){
                if(index == 15){
                    System.out.println();
                }

                if(!orderedCellsPlaceWayList.contains(placeWayHashCodeArray[index][position])
                        && map.containsKey(placeWayHashCodeArray[index][position])){
                    orderedCellsPlaceWayList.add(placeWayHashCodeArray[index][position]);
                }
            }
        }

        for(Integer key:orderedCellsPlaceWayList){
            placedTagCloudItemsCells.add(map.get(key));
        }

        return placedTagCloudItemsCells;
    }

    private static int getTotalWeight(List<BaseCell> cellsToPlace) {
        try {
            int sum = 0;
            for(BaseCell baseCell:cellsToPlace){
                sum += ((TagCloudItemCell) baseCell).getWeight();
            }

            return sum;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.MIN_VALUE;
    }

    public PlaceTooImpl(List<BaseCell> cellsToPlace) {
        this.cellsToPlace = cellsToPlace;

        for(BaseCell baseCell:cellsToPlace){
            if(((TagCloudItemCell)baseCell).getTag().trim().equals("")){
                map.put(baseCell.hashCode(),new EmptyCell(1));

                continue;
            }

            map.put(baseCell.hashCode(),baseCell);
        }
    }

    public static void main(String[] args) {
        List<BaseCell> tagCloudItemCells = new ArrayList<BaseCell>();

        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",2,2));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,1));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,1));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));

        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));

        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,1));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,1));


        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,1));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));

        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,1));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));

        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));
        tagCloudItemCells.add(new TagCloudItemCell(8,"文字文字文字",1,2));

        PlaceTooImpl placeTool = new PlaceTooImpl(tagCloudItemCells);
        placeTool.initialize();
        placeTool.place();
    }
}
