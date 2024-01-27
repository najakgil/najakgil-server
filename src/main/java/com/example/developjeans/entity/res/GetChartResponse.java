package com.example.developjeans.entity.res;

import com.example.developjeans.dto.PhotoDto;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.global.config.scroll.ScrollPaginationCollection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetChartResponse {

    // nextCursor 값이 -1 일 경우 해당 스크롤이 마지막 스크롤
    private static final long LAST_CURSOR = -1L;

    // 클라이언트에게 전달할 데이터
    private List<PhotoDto.PhotoChartDto> contents = new ArrayList<>();
    private long totalElements; // 조회 가능한 데이터 총 개수
    private long nextCursor; // 다음 스크롤에서 사용할 커서의 값

    private GetChartResponse(List<PhotoDto.PhotoChartDto> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static GetChartResponse of(ScrollPaginationCollection<Photo> photoScroll, long totalElements) {
        GetChartResponse GetChartResponse;
        if (photoScroll.isLastScroll()) {
            GetChartResponse = newLastScroll(photoScroll.getCurrentScrollItems(), totalElements);
        } else {
            GetChartResponse = newScrollHasNext(photoScroll.getCurrentScrollItems(), totalElements, photoScroll.getNextCursor().getId());
        }

        return GetChartResponse;
    }

    private static GetChartResponse newLastScroll(List<Photo> photoScroll, long totalElements) {
        return newScrollHasNext(photoScroll, totalElements, LAST_CURSOR);
    }


    private static GetChartResponse newScrollHasNext(List<Photo> photoScroll, long totalElements, long nextCursor) {
        return new GetChartResponse(getContents(photoScroll), totalElements, nextCursor);
    }

    private static List<PhotoDto.PhotoChartDto> getContents(List<Photo> photoScroll) {
        return photoScroll.stream()
                .map(feed -> new PhotoDto.PhotoChartDto(feed.getId(), feed.getImgUrl(), feed.getLikes()))
                .collect(Collectors.toList());
    }


}

