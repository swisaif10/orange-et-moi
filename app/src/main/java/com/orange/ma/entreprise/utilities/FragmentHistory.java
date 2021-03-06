package com.orange.ma.entreprise.utilities;

import java.util.ArrayList;

public class FragmentHistory {

    private ArrayList<Integer> stackArr;

    public FragmentHistory() {
        stackArr = new ArrayList<>();
    }

    public void push(int entry) {

        if (isAlreadyExists(entry)) {
            stackArr.remove(stackArr.indexOf(entry));
        }
        stackArr.add(entry);
    }

    private boolean isAlreadyExists(int entry) {
        return (stackArr.contains(entry));
    }

    public int pop() {

        int entry = -1;
        if (!isEmpty()) {

            entry = stackArr.get(stackArr.size() - 1);

            stackArr.remove(stackArr.size() - 1);
        }
        return entry;
    }

    public int popPrevious() {

        int entry = 0;

        if (!isEmpty()) {
            entry = stackArr.get(stackArr.size() - 2);
            stackArr.remove(stackArr.size() - 1);
        }
        return entry;
    }

    public int peek() {
        if (!isEmpty()) {
            return stackArr.get(stackArr.size() - 1);
        }

        return -1;
    }

    public boolean isEmpty() {
        return stackArr.isEmpty();
    }

    public int getStackSize() {
        return stackArr.size();
    }

    public void emptyStack() {

        stackArr.clear();
    }
}
