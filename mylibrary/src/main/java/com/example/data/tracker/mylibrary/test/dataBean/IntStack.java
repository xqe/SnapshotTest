package com.example.data.tracker.mylibrary.test.dataBean;

/**
 * Bargain-bin pool of integers, for use in avoiding allocations during path crawl
 */
public class IntStack {

    private final int[] mStack;
    private int mStackSize;
    private static final int MAX_INDEX_STACK_SIZE = 256;

    public IntStack() {
        mStack = new int[MAX_INDEX_STACK_SIZE];
        mStackSize = 0;
    }

    public boolean full() {
        return mStack.length == mStackSize;
    }

    /**
     * Pushes a new value, and returns the index you can use to increment and read that value later.
     */
    public int alloc() {
        final int index = mStackSize;
        mStackSize++;
        mStack[index] = 0;
        return index;
    }

    /**
     * Gets the value associated with index. index should be the result of a previous call to alloc()
     */
    public int read(int index) {
        return mStack[index];
    }

    public void increment(int index) {
        mStack[index]++;
    }

    /**
     * Should be matched to each call to alloc. Once free has been called, the key associated with the
     * matching alloc should be considered invalid.
     */
    public void free() {
        mStackSize--;
        if (mStackSize < 0) {
            throw new ArrayIndexOutOfBoundsException(mStackSize);
        }
    }
}