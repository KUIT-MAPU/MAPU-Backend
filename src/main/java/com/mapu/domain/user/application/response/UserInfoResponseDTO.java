package com.mapu.domain.user.application.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDTO {
    String nickname;
    String profileId;
    String imgUrl;
    int mapCnt;
    int followerCnt;
    int followingCnt;

    @Builder
    public UserInfoResponseDTO(String nickname, String profileId, String imgUrl, int mapCnt, int followerCnt, int followingCnt) {
        this.nickname = nickname;
        this.profileId = profileId;
        this.imgUrl = imgUrl;
        this.mapCnt = mapCnt;
        this.followerCnt = followerCnt;
        this.followingCnt = followingCnt;
    }
}

