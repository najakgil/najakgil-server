package com.example.developjeans.service;


import com.example.developjeans.dto.PhotoDto;


import com.example.developjeans.entity.Photo;
import com.example.developjeans.entity.PhotoLike;
import com.example.developjeans.entity.User;
import com.example.developjeans.exception.BusinessLogicException;
import com.example.developjeans.exception.ExceptionCode;
import com.example.developjeans.global.config.aws.S3Service;
import com.example.developjeans.global.config.scroll.ScrollPaginationCollection;
import com.example.developjeans.global.entity.Status;
import com.example.developjeans.mapper.PhotoMapper;
import com.example.developjeans.repository.PhotoLikeRepository;
import com.example.developjeans.repository.PhotoRepository;
import com.example.developjeans.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    private final PhotoLikeRepository photoLikeRepository;

    private final UserRepository userRepository;
    //private final AmazonS3 amazonS3;

    private final S3Service s3Service;

    private final PhotoMapper photoMapper;


    //@Value("${cloud.aws.s3.bucket}")
    //private String s3BucketUrl; // Amazon S3 버킷의 URL (https://<버킷이름>.s3.<리전>.amazonaws.com)


    public Long uploadFile(MultipartFile image, Long userId) {

        try {
            /**
            String fileName = image.getOriginalFilename();
            amazonS3.putObject(new PutObjectRequest("najakgil", fileName, image.getInputStream(), null));

            Photo createdPhoto = createPhoto(fileName);
            if (createdPhoto != null) {
                return new SavePhotoRes(createdPhoto.getId());
            } else {
                return null;
            } */
            // 인가 구현
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            String u = principal.getName();

            if (u == null || u.isEmpty()) {
                throw new BusinessLogicException(ExceptionCode.EMPTY_JWT);
            }

            Long id = Long.parseLong(u);

            if (!id.equals(userId)) {
                throw new BusinessLogicException(ExceptionCode.INVALID_USER_JWT);
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 유저 ID: " + userId));

            String S3Url = s3Service.uploadImage(image);
            Photo photo = Photo.builder()
                    .imgUrl(S3Url)
                    .user(user)
                    .status(Status.A)
                    .likes(0).build();
            photoRepository.save(photo);

//            return new SavePhotoRes(photo.getId());
            return photoMapper.toResponseDto(photo).getId();
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.SERVER_ERROR);
        }


    }

    
    @Transactional(readOnly = true)
    public List<PhotoDto.PhotoResponseDto> getAllImages(Long userId) {

        try {
            // 인가 구현
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            String u = principal.getName();

            if (u == null || u.isEmpty()) {
                throw new BusinessLogicException(ExceptionCode.EMPTY_JWT);
            }

            Long id = Long.parseLong(u);

            if (!id.equals(userId)) {
                throw new BusinessLogicException(ExceptionCode.INVALID_USER_JWT);
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 유저 ID: " + userId));

            List<Photo> photoList = photoRepository.findByUserId(userId);
            List<PhotoDto.PhotoResponseDto> getPhotoResList = new ArrayList<>();

            for(Photo photo: photoList){
                PhotoDto.PhotoResponseDto photoGetDto = photoMapper.toResponseDto(photo);
                getPhotoResList.add(photoGetDto);
//            GetPhotoRes getPhotoRes = new GetPhotoRes(photo.getId(), photo.getImgUrl());
//            getPhotoResList.add(getPhotoRes);
            }
            return getPhotoResList;
        } catch (Exception e){
            throw new BusinessLogicException(ExceptionCode.SERVER_ERROR);
        }

        //return photoRepository.findAll();
    }

    /**
    public Photo createPhoto(String imgName){
        String imageUrl = imgName; // 이미지 URL 생성

        Photo photo = new Photo();

        photo.setUser(User.builder().id(1L).build());
        photo.setStatus(Status.A);
        photo.setLikes(0);

        photo.setImgUrl(imageUrl);
        return photoRepository.save(photo);
    } */

    public PhotoDto.PhotoLikeDto likePhoto(Long photoId, Long userId){
        try{
            // 인가 구현
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            String u = principal.getName();

            if (u == null || u.isEmpty()) {
                throw new BusinessLogicException(ExceptionCode.EMPTY_JWT);
            }

            Long id = Long.parseLong(u);

            if (!id.equals(userId)) {
                throw new BusinessLogicException(ExceptionCode.INVALID_USER_JWT);
            }

            User _user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 유저 ID: " + userId));

            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 사진 ID: " + photoId));

            User user = User.builder()
                    .id(userId)
                    .build();

            Optional<PhotoLike> optionalLike = photoLikeRepository.findByPhotoAndUser(photo, user);

            if(optionalLike.isPresent()){
                PhotoLike photoLike = optionalLike.get();
                photoLikeRepository.delete(photoLike);
                photo.setLikes(photo.getLikes() - 1);
                String message = "좋아요 취소";
//            return new PhotoLikeRes(photoId, photo.getLikes(), message);
                PhotoDto.PhotoResponseDto photoResponseDto = photoMapper.toResponseDto(photo);
                return new PhotoDto.PhotoLikeDto(photoResponseDto.getId(), photoResponseDto.getLikes(), message);
            }

        /*
        if(photoLikeRepository.existsByPhotoAndUserAndStatus(photo, user, Status.A)) {
            photoLikeRepository.deleteById(photo);
            photo.setLikes(photo.getLikes() - 1);
            System.out.println("좋아요가 취소되었습니다.");
            return new PhotoLikeRes(photoId, photo.getLikes());
        } */

            PhotoLike photoLike = PhotoLike
                    .builder()
                    .user(user)
                    .photo(photo)
                    .status(Status.A)
                    .build();

            photoLikeRepository.save(photoLike);
            photo.setLikes(photo.getLikes() + 1);

            String message = "좋아요";
            PhotoDto.PhotoResponseDto photoResponseDto = photoMapper.toResponseDto(photo);
            return new PhotoDto.PhotoLikeDto(photoResponseDto.getId(), photoResponseDto.getLikes(), message);
//        return new PhotoLikeRes(photoId, photo.getLikes(), message);
        } catch (Exception e){
            throw new BusinessLogicException(ExceptionCode.DATABASE_ERROR);
        }




    }

//    @Transactional(readOnly = true)
//    public Page<> getChart(String standard, int size, Long lastPageId) {
//
//        PageRequest pageRequest = PageRequest.of(0, size + 1);
//        Page<Photo> page = photoRepository.findAllByOrderByCreatedAtDesc(lastPageId, pageRequest);
//        List<Photo> photos = page.getContent();
//
//        ScrollPaginationCollection<Photo> photoCursor = ScrollPaginationCollection.of(photos, size);
//        GetChartRes getChartRes = G
//
//
//    }

    @Transactional(readOnly = true)
    public List<PhotoDto.PhotoChartDto> getChart(String standard, int size){

        if(standard.equals("likes")){
            Slice<Photo> findPhotoLikes = photoRepository.findAllByOrderByLikesDesc(PageRequest.of(0, size));
            return createList(findPhotoLikes);
        }
        else if(standard.equals("latest")){
            Slice<Photo> findPhotoLatest = photoRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, size));
            return createList(findPhotoLatest);
        } else {
            throw new BusinessLogicException(ExceptionCode.INVALID_SORT);
        }



    }

    public static List<PhotoDto.PhotoChartDto> createList(Slice<Photo> photos){
        // page를 list로 변환
        List<PhotoDto.PhotoChartDto> photoChartDtos = new ArrayList<>();
        for(Photo photo: photos){
            photoChartDtos.add(PhotoDto.PhotoChartDto.builder()
                    .photo_id(photo.getId())
                    .imgUrl(photo.getImgUrl())
                    .likes(photo.getLikes()).build());
        }

        return photoChartDtos;

    }




//    @Transactional(readOnly = true)
//    public Page<GetChartRes> getChart(String starndard, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        if(starndard.equals("likes")){
//            return photoRepository.findAllByOrderByLikesDesc(pageable);
//        }
//        else if(starndard.equals("createdAt")){
//            return photoRepository.findAllByOrderByCreatedAtDesc(pageable);
//        }
//        else {
//            throw new IllegalArgumentException("Invalid sorting standard. Supported standards are 'likes' and 'createdAt'");
//        }
//
//    }


}
