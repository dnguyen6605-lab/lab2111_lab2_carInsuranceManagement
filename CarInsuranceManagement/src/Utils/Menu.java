package Utils;

import java.util.ArrayList;

public class Menu {
    private String title;
    private ArrayList<String> optionList = new ArrayList<>();
    private final Inputter inputter;

    public Menu(String title,Inputter inputter) {
        this.title = title;
        this.inputter = inputter;
    }

    public void addNewOption(String newOption) {
        optionList.add(newOption);
    }

    public void show() {
        int seq = 1;
        System.out.println("+++++" + title + "+++++");
        for (String string : optionList) {
            System.out.println(seq + ". " + string);
            seq++;
        }
    }

    public int getchoice() {
        return inputter.getAnInteger("Choose an option: ", 1, optionList.size());
    }
}
