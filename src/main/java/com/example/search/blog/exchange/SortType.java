package com.example.search.blog.exchange;

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

    public String toNaverSort() {
        if (this == SortType.RECENCY) {
            return "date";
        }
        return "sim";
    }
}