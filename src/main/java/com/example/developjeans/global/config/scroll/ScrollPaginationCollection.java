package com.example.developjeans.global.config.scroll;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrollPaginationCollection<T> {
    private final List<T> itemsWithNextCursor; // 현재 스크롤의 요소 + 다음 스크롤의 요소 1개 (다음 스크롤이 있는지 확인을 위한)
    private final int countPerScroll; // 스크롤 1회에 조회할 데이터 개수


    public static <T> ScrollPaginationCollection<T> of(List<T> itemsWithNextCursor, int size) {
        return new ScrollPaginationCollection<>(itemsWithNextCursor, size);
    }

    /**
     * 현재 스크롤이 마지막 스크롤인지 확인하는 메서드
     * 쿼리로 데이터를 조회한 결과 countPerScroll 의 숫자 이하로 조회되면 마지막 스크롤이라고 판단
     */
    public boolean isLastScroll() {
        return this.itemsWithNextCursor.size() <= countPerScroll;
    }

    /**
     * 마지막 스크롤일 경우 itemsWithNextCursor를 return
     * 마지막 스크롤이 아닐 경우 다음 스크롤의 데이터 1개를 제외하고 return
     * @return
     */
    public List<T> getCurrentScrollItems() {
        if (isLastScroll()) {
            return this.itemsWithNextCursor;
        }
        return this.itemsWithNextCursor.subList(0, countPerScroll);
    }

    /**
     * 현재 스크롤의 데이터 중 마지막 데이터를 cursor로 사용하고 이를 return
     * @return
     */
    public T getNextCursor() {
        return itemsWithNextCursor.get(countPerScroll - 1);
    }
}
