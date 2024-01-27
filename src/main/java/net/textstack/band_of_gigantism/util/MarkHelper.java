package net.textstack.band_of_gigantism.util;

import net.textstack.band_of_gigantism.item.base.MarkItem;

import java.util.ArrayList;
import java.util.List;

public class MarkHelper {
    private static final List<MarkItem> marks = new ArrayList<>();


    public static List<MarkItem> getMarks() {
        return marks;
    }

    public static void addMark(MarkItem mark) {
        marks.add(mark);
    }
}
