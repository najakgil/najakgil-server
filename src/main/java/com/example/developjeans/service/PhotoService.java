package com.example.developjeans.service;


import com.example.developjeans.dto.PhotoDto;
import com.example.developjeans.dto.response.GetChartRes;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.entity.PhotoLike;
import com.example.developjeans.entity.User;
import com.example.developjeans.global.config.response.BaseException;
import com.example.developjeans.global.config.response.BaseResponseStatus;
import com.example.developjeans.global.entity.Status;
import com.example.developjeans.mapper.PhotoMapper;
import com.example.developjeans.repository.PhotoLikeRepository;
import com.example.developjeans.repository.PhotoRepository;
import com.example.developjeans.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 유저 ID: " + userId));

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
            //return "File upload failed: " + e.getMessage();
            e.printStackTrace();
            return null;
        }


    }

    
    @Transactional(readOnly = true)
    public List<PhotoDto.PhotoResponseDto> getAllImages(Long userId) {
        List<Photo> photoList = photoRepository.findByUserId(userId);
        List<PhotoDto.PhotoResponseDto> getPhotoResList = new ArrayList<>();

        for(Photo photo: photoList){
            PhotoDto.PhotoResponseDto photoGetDto = photoMapper.toResponseDto(photo);
            getPhotoResList.add(photoGetDto);
//            GetPhotoRes getPhotoRes = new GetPhotoRes(photo.getId(), photo.getImgUrl());
//            getPhotoResList.add(getPhotoRes);
        }
        return getPhotoResList;
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

    public PhotoDto.PhotoLikeDto likePhoto(Long photoId, Long userId) throws BaseException {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

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

    }


    @Transactional(readOnly = true)
    public Page<GetChartRes> getChart(String starndard, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(starndard.equals("likes")){
            return photoRepository.findAllByOrderByLikesDesc(pageable);
        }
        else if(starndard.equals("createdAt")){
            return photoRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        else {
            throw new IllegalArgumentException("Invalid sorting standard. Supported standards are 'likes' and 'createdAt'");
        }

    }


}
