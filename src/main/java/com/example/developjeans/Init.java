package com.example.developjeans;

import com.example.developjeans.entity.Photo;
import com.example.developjeans.entity.User;
import com.example.developjeans.global.entity.SocialType;
import com.example.developjeans.repository.PhotoLikeRepository;
import com.example.developjeans.repository.PhotoRepository;
import com.example.developjeans.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class Init {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final PhotoLikeRepository photoLikeRepository;

    @PostConstruct
    private void initFirst(){
        initUsers();
        initPhotos();

    }

    @Transactional
    public void initUsers() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setNickName("userNickname" + (i+1));
            user.setPassword("userPs" + (i+1));

            user.setEmail("user" + (i+1) + "@example.com");
            user.setKakaoId(Long.parseLong(String.valueOf(i + 1)));
            user.setRefreshToken("userRefreshToken" + (i+1));
            userRepository.save(user);
        }
    }

    @Transactional
    public void initPhotos(){
        List<User> user = userRepository.findAll();
        if(!user.isEmpty()){
            for(int i = 0; i < 10; i++){
                Photo photo = new Photo();
                photo.setLikes(i + 1);
                photo.setUser(user.get(i));
                photo.setImgUrl("https://najakgil" + (i + 1));
                photoRepository.save(photo);
            }
        }

    }
}
