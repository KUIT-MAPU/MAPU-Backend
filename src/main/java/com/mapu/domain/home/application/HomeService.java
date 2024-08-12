package com.mapu.domain.home.application;

import com.mapu.domain.home.application.response.HomeFollowingMapResponseDTO;
import com.mapu.domain.home.application.response.HomeKeywordMapResponseDTO;
import com.mapu.domain.map.dao.MapRepository;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.follow.dao.FollowRepository;
import com.mapu.domain.map.domain.Map;
import com.mapu.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final MapRepository mapRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    // 사용자가 팔로우하는 사람들의 지도 가져오는
    public List<HomeFollowingMapResponseDTO> getFollowingMaps(Long userId) {
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(userId);
        List<User> followingUsers = userRepository.findAllById(followingIds);

        return followingUsers.stream()
                .map(user -> {
                    // 내가 팔로우하는 사람들 id 목록 가져오기
                    List<Map> userMaps = mapRepository.findByUserId(user.getId());
                    List<HomeFollowingMapResponseDTO.MapDTO> mapDTOs = userMaps.stream()
                            .map(map -> HomeFollowingMapResponseDTO.MapDTO.builder()
                                    .title(map.getMapTitle())
                                    .address(map.getAddress())
                                    .imageUrl(map.getImageUrl())
                                    .build())
                            .collect(Collectors.toList());

                    return HomeFollowingMapResponseDTO.builder()
                            .nickname(user.getNickname())
                            .profileId(user.getProfileId())
                            .userImage(user.getImage())
                            .maps(mapDTOs)
                            .build();
                })
                .collect(Collectors.toList());
    }

    //키워드에 따른 지도 가져오기
    public List<HomeKeywordMapResponseDTO> getMapsByKeywords(List<String> keywords) {

        return keywords.stream()
                .map(keyword -> { //각 키워드에 대해서 처리
                    List<Map> maps = mapRepository.findByKeyword(keyword);
                    List<HomeKeywordMapResponseDTO.KeywordMapDTO> mapDTOs = maps.stream()
                            .map(map -> HomeKeywordMapResponseDTO.KeywordMapDTO.builder()
                                    .nickname(map.getUser().getNickname())
                                    .profileId(map.getUser().getProfileId())
                                    .userImage(map.getUser().getImage())
                                    .mapTitle(map.getMapTitle())
                                    .mapImage(map.getImageUrl())
                                    .build())
                            .collect(Collectors.toList());

                    return HomeKeywordMapResponseDTO.builder()
                            .keyword(keyword)
                            .maps(mapDTOs)
                            .build();
                })
                .collect(Collectors.toList());
    }
}