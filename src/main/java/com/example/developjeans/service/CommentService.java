package com.example.developjeans.service;

import com.example.developjeans.dto.CommentDto;
import com.example.developjeans.entity.Comment;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.entity.res.GetChartResponse;
import com.example.developjeans.entity.res.GetCommentChartResponse;
import com.example.developjeans.exception.BusinessLogicException;
import com.example.developjeans.exception.ExceptionCode;
import com.example.developjeans.global.config.scroll.ScrollPaginationCollection;
import com.example.developjeans.global.entity.Status;
import com.example.developjeans.mapper.CommentMapper;
import com.example.developjeans.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public long createComment(String content){

        Comment comment = Comment.builder()
                .content(content)
                .status(Status.A)
                .likes_cnt(0)
                .declaration(0).build();

        Comment saveComment = commentRepository.save(comment);

        CommentDto.CommentSaveResponseDto commentSaveResponseDto = new CommentDto.CommentSaveResponseDto();
        commentSaveResponseDto.setId(saveComment.getId());
        return commentSaveResponseDto.getId();
    }

    public GetCommentChartResponse getChart(String sort, int size, Long lastPageId){
        // 페이지 요청 객체 생성
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        Page<Comment> page;
        if ("likes".equals(sort) || "latest".equals(sort)) {
            // 정렬 기준에 따라 쿼리 실행
            page = "likes".equals(sort) ?
                    commentRepository.findAllByOrderByLikesDesc(lastPageId, pageRequest) :
                    commentRepository.findAllByOrderByCreatedAtDesc(lastPageId,pageRequest);
        } else {
            // 유효하지 않은 sort값이 온 경우 예외 처리
            throw new BusinessLogicException(ExceptionCode.INVALID_SORT);
        }

        // 페이지의 내용과 전체 엔티티 수를 사용하여 응답 생성
        List<Comment> comments = page.getContent();
        ScrollPaginationCollection<Comment> commentCursor = ScrollPaginationCollection.of(comments, size);
        long totalElements = commentRepository.count();
        return GetCommentChartResponse.of(commentCursor, totalElements);
    }

    @Transactional
    public CommentDto.CommentLikeResponseDto createLikes(Long commentId){

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.like(comment, comment.getLikes_cnt());
        return new CommentDto.CommentLikeResponseDto(comment.getLikes_cnt(), "좋아요 성공");
    }

    @Transactional
    public CommentDto.CommentLikeResponseDto deleteLikes(Long commentId){

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.unlike(comment, comment.getLikes_cnt());

        if(comment.getLikes_cnt() < 0){
            return new CommentDto.CommentLikeResponseDto(0, "좋아요는 음수가 될 수 없습니다.");
        }

        return new CommentDto.CommentLikeResponseDto(comment.getLikes_cnt(), "좋아요 취소 완료");
    }

    @Transactional
    public CommentDto.CommentBlameDto createReports(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.reports(comment, comment.getDeclaration());

        if(comment.getDeclaration() > 9){
            commentRepository.delete(comment);
            String message = "신고 횟수 10회로 댓글이 삭제되었습니다.";
            return new CommentDto.CommentBlameDto(0, message);
        }

        String message = "신고가 완료되었습니다.";

        return new CommentDto.CommentBlameDto(comment.getDeclaration(), message);
    }

    @Transactional
    public CommentDto.CommentBlameDto deleteReports(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.unreports(comment, comment.getDeclaration());

        if(comment.getDeclaration() < 0){
            String message = "신고 횟수 0";
            return new CommentDto.CommentBlameDto(0, message);
        }

        return new CommentDto.CommentBlameDto(comment.getDeclaration(), "신고가 취소되었습니다.");
    }
}
