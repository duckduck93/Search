package com.example.search.util;

public enum SortType {
    /**
     * 정확도
     */
    ACCURACY,
    /**
     * 최신순
     */
    RECENCY;

    public String toKakaoSort() {
        if (this == SortType.RECENCY) {
            return "recency";
        }
        return "accuracy";
    }
}