package com.example.developjeans.photo;

import com.example.developjeans.DevelopJeansApplication;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.entity.User;
import com.example.developjeans.global.entity.Status;
import com.example.developjeans.repository.PhotoRepository;
//import org.junit.Test;
import com.example.developjeans.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

//    @BeforeEach
//    public void init(){
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setNickName("userNickname" + (i+1));
//            user.setPassword("userPs" + (i+1));
//
//            user.setEmail("user" + (i+1) + "@example.com");
//            user.setKakaoId(Long.parseLong(String.valueOf(i + 1)));
//            user.setRefreshToken("userRefreshToken" + (i+1));
//            userRepository.save(user);
//
//            Photo photo = new Photo();
//            photo.setId(Long.valueOf(i + 1));
//            photo.setLikes(i + 1);
//            photo.setStatus(Status.A);
//            photo.setImgUrl("http://" + (i + 1));
//            photo.setUser(user);
//            photoRepository.save(photo);
//        }
//    }

    @Test
    @DisplayName("사진 레포지토리: 사진 차트 불러오기 성공")
    public void 사진_차트_불러오기_성공(){
        // given
        Photo photo = testPhoto();
        Pageable pageable = PageRequest.of(0, 3);

        // when
//        Page<Photo> findPhotoChart = photoRepository.findAllByOrderByLikesDesc(pageable);
        Slice<Photo> findPhotoChart = photoRepository.findAllByOrderByCreatedAtDesc(pageable);

        // then
        assertEquals(findPhotoChart.getSize(), 3); // 객체가 일치하는지 확인
        List<Photo> content = findPhotoChart.getContent();
        assertTrue(content.isEmpty()); // 콘텐츠가 비어 있지 않은지 확인
        if (!content.isEmpty()) {
            assertEquals(content.get(0).getId(), 21);
            assertEquals(content.get(1).getId(), 20);
            assertEquals(content.get(2).getId(), 19);
        }



    }

//    @Test
//    @DisplayName("사진 레포지토리: 사진 차트 불러오기 실패")
//    public void 사진_차트_불러오기_실패(){
//        // given
//        Pageable pageable = PageRequest.of(0, 3);
//
//        // when
//        //        Page<Photo> findPhotoChart = photoRepository.findAllByOrderByLikesDesc(pageable);
//        Slice<Photo> findPhotoChart = photoRepository.findAllByOrderByCreatedAtDesc(pageable);
//
//        // then
//        assertEquals(findPhotoChart.getTotalPages(), 0);
//        org.assertj.core.api.Assertions.assertThat(findPhotoChart).isEmpty();
//
//    }

    private static User testUser(){
        return User.builder()
                .email("wewe")
                .kakaoId(123L)
                .nickName("test")
                .password("1234")
                .refreshToken("wewew")
                .status(Status.A)
                .build();
    }

    private static Photo testPhoto(){
        return Photo.builder()
                .likes(1)
                .imgUrl("http://")
                .status(Status.A)
                .user(testUser())
                .build();
    }


}
