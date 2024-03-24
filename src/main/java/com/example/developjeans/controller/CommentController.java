package com.example.developjeans.controller;

import com.example.developjeans.dto.CommentDto;
import com.example.developjeans.dto.PhotoDto;
import com.example.developjeans.global.config.response.BaseResponse;
import com.example.developjeans.service.CommentService;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@Tag(name = "댓글")
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    /*
    댓글 작성 API
     */
    @Operation(summary = "댓글 작성하기", description = "이벤트 페이지에서 댓글 작성 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = CommentDto.CommentSaveResponseDto.class))),
    })
    @PostMapping()
    public BaseResponse<?> createComment(@RequestBody String content){
        log.info("댓글 작성 API 실행");
        return new BaseResponse<>("commentId:" + commentService.createComment(content));
    }

    /*
    댓글 조회(인기순, 최신순) API
     */
    @Operation(summary = "댓글 조회하기", description = "이벤트 페이지에서 댓글 조회 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = CommentDto.CommentResponseDto.class))),
    })
    @GetMapping("/chart")
    public BaseResponse<?> getChart(@RequestParam String sort,
                                    @RequestParam int size,
                                    @RequestParam Long lastCommentId){
        return new BaseResponse<>(commentService.getChart(sort, size, lastCommentId));
    }

    /*
    댓글 좋아요 API
     */
    @Operation(summary = "댓글 좋아요 하기", description = "이벤트 페이지에서 댓글 좋아요 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = CommentDto.CommentLikeResponseDto.class))),
    })
    @PostMapping("/likes")
    public BaseResponse<?> postLike(@RequestParam Long commentId){
        return new BaseResponse<>("좋아요 개수: " + commentService.createLikes(commentId));
    }

    /*
    댓글 좋아요 취소 API
     */
    @Operation(summary = "댓글 좋아요 취소 하기", description = "이벤트 페이지에서 댓글 좋아요 취소 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = CommentDto.CommentLikeResponseDto.class))),
    })
    @PatchMapping("/likes")
    public BaseResponse<?> deleteLike(@RequestParam Long commentId){
        return new BaseResponse<>("좋아요 개수: " + commentService.deleteLikes(commentId));
    }



    /*
    댓글 신고 API
     */
    @Operation(summary = "댓글 신고 하기", description = "이벤트 페이지에서 댓글 신고 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = CommentDto.CommentBlameDto.class))),
    })
    @PostMapping("/reports")
    public BaseResponse<?> createReport(@RequestParam Long commentId){
        return new BaseResponse<>("신고 횟수: " + commentService.createReports(commentId));
    }

    /*
    댓글 신고 취소 API
     */
    @Operation(summary = "댓글 신고 취소 하기", description = "이벤트 페이지에서 댓글 신고 취소 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = CommentDto.CommentBlameDto.class))),
    })
    @PatchMapping("/reports")
    public BaseResponse<?> deleteReport(@RequestParam Long commentId){
        return new BaseResponse<>("신고 횟수: " + commentService.deleteReports(commentId));
    }

}
