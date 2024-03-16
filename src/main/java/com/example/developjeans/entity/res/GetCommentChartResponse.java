package com.example.developjeans.entity.res;

import com.example.developjeans.dto.CommentDto;
import com.example.developjeans.dto.PhotoDto;
import com.example.developjeans.entity.Comment;
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
public class GetCommentChartResponse {

    // nextCursor 값이 -1 일 경우 해당 스크롤이 마지막 스크롤
    private static final long LAST_CURSOR = -1L;

    // 클라이언트에게 전달할 데이터
    private List<CommentDto.CommentResponseDto> contents = new ArrayList<>();
    private long totalElements; // 조회 가능한 데이터 총 개수
    private long nextCursor; // 다음 스크롤에서 사용할 커서의 값

    private GetCommentChartResponse(List<CommentDto.CommentResponseDto> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static GetCommentChartResponse of(ScrollPaginationCollection<Comment> commentScroll, long totalElements) {
        GetCommentChartResponse GetCommentChartResponse;
        if (commentScroll.isLastScroll()) {
            GetCommentChartResponse = newLastScroll(commentScroll.getCurrentScrollItems(), totalElements);
        } else {
            GetCommentChartResponse = newScrollHasNext(commentScroll.getCurrentScrollItems(), totalElements, commentScroll.getNextCursor().getId());
        }

        return GetCommentChartResponse;
    }

    private static GetCommentChartResponse newLastScroll(List<Comment> commentScroll, long totalElements) {
        return newScrollHasNext(commentScroll, totalElements, LAST_CURSOR);
    }


    private static GetCommentChartResponse newScrollHasNext(List<Comment> commentScroll, long totalElements, long nextCursor) {
        return new GetCommentChartResponse(getContents(commentScroll), totalElements, nextCursor);
    }

    private static List<CommentDto.CommentResponseDto> getContents(List<Comment> commentScroll) {
        return commentScroll.stream()
                .map(feed -> new CommentDto.CommentResponseDto(feed.getId(), feed.getContent(), feed.getCreatedAt(), feed.getDeclaration(), feed.getLikes_cnt()))
                .collect(Collectors.toList());
    }

}
