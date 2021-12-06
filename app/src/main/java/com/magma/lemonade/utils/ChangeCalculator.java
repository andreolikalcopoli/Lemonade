package com.magma.lemonade.utils;

import android.util.Log;

import com.magma.lemonade.R;
import com.magma.lemonade.models.Change;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ChangeCalculator {
    private ArrayList<Change> changes = new ArrayList<>();
    private int[] amounts = new int[5];
    private int[] images = {R.drawable.ten, R.drawable.twentyfive, R.drawable.dollar, R.drawable.fivedollar, R.drawable.tendollar};
    private String[] texts = {"Dimes: ", "Quarters: ", "Dollars: ", "Five dollars: ", "Ten dollars: "};
    private List<Integer> denominations = Arrays.asList(10, 25, 100, 500, 1000);
    private int min = Integer.MAX_VALUE;

    private void addChange(int img, String text) {
        Change change = new Change(img, text);
        changes.add(change);
    }

    private void setChangesList(ArrayList<Integer> values) {
        changes.clear();
        amounts = new int[5];
        for (int i = 0; i < values.size(); i++) {
            int value = values.get(i);
            amounts[denominations.indexOf(value)]++;
        }

        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] > 0) addChange(images[i], texts[i] + amounts[i]);
        }
    }

    public ArrayList<Change> calculate(double amount) {
        int target = (int) (Math.ceil(amount * 100));
        Log.d("CHANGE_CALC", "Target: " + target);
        coinChange(denominations, new ArrayList<Integer>(), 0, target);
        return changes;
    }

    public void coinChange(List<Integer> nums, ArrayList<Integer> choosen, int start, int target) {
        if (target == 0) {
            if (min > choosen.size()) {
                min = choosen.size();
                setChangesList(choosen);
            }
        } else if (target > 1) {
            for (int i = start; i < nums.size(); i++) {
                choosen.add(nums.get(i));
                coinChange(nums, choosen, i, target - nums.get(i));
                choosen.remove(choosen.size() - 1);
            }
        }
    }
}
