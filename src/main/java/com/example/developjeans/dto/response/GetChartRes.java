//package com.example.developjeans.dto.response;
//
//import com.example.developjeans.dto.PhotoDto;
//import com.example.developjeans.entity.Photo;
//import com.example.developjeans.global.config.scroll.ScrollPaginationCollection;
//import lombok.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@ToString
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class GetChartRes {
//
//    private static final long LAST_CURSOR = -1L;
//
//    // 클라이언트에게 전달할 데이터
//    private List<PhotoDto.PhotoChartDto> contents = new ArrayList<>();
//    private long totalElements;
//    private long nextCursor;
//
//    private GetChartRes(List<PhotoDto.PhotoChartDto> contents, long totalElements, long nextCursor) {
//        this.contents = contents;
//        this.totalElements = totalElements;
//        this.nextCursor = nextCursor;
//    }
//
//    public static GetChartRes of(ScrollPaginationCollection<Photo> photoScroll, long totalElements) {
//        GetChartRes getChartRes;
//        if (photoScroll.isLastScroll()) {
//            getChartRes = newLastScroll(photoScroll.getCurrentScrollItems(), totalElements);
//        } else {
//            getChartRes = newScrollHasNext(photoScroll.getCurrentScrollItems(), totalElements, photoScroll.getNextCursor().getId());
//        }
//
//        return getChartRes;
//    }
//
//    private static GetChartRes newLastScroll(List<Photo> photoScroll, long totalElements) {
//        return newScrollHasNext(photoScroll, totalElements, LAST_CURSOR);
//    }
//
//
//    private static GetChartRes newScrollHasNext(List<Photo> photoScroll, long totalElements, long nextCursor) {
//        return new GetChartRes(getContents(photoScroll), totalElements, nextCursor);
//    }
//
//    private static List<PhotoDto.PhotoChartDto> getContents(List<Photo> photoScroll) {
//        return photoScroll.stream()
//                .map(feed -> new PhotoDto.PhotoChartDto(feed.getId(), feed.getImgUrl(), feed.getLikes()))
//                .collect(Collectors.toList());
//    }
//
//
//}
