package com.mapu.domain.follow.api;

import com.mapu.domain.follow.api.request.FollowRequestDTO;
import com.mapu.domain.follow.application.FollowService;
import com.mapu.domain.follow.application.response.FollowListResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.global.jwt.dto.JwtUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    /**
     * 유저 팔로우 API
     * followerId 나
     * followingId 팔로우할 대상
     */
    @PostMapping("/follow")
    public BaseResponse<Void> followUser(@AuthenticationPrincipal JwtUserDto jwtUserDto,
                                                      @RequestBody FollowRequestDTO followRequestDTO) {
        Long followerId = Long.parseLong(jwtUserDto.getName());
        followService.followUser(followerId, followRequestDTO.getFollowingId());

        return new BaseResponse<>(null);
    }

    /**
     * 유저 언팔로우 API
     * followerId 나
     * followingId 언팔로우할 대상
     */
    @PostMapping("/unfollow")
    public BaseResponse<Void> unfollowUser(@AuthenticationPrincipal JwtUserDto jwtUserDto,
                                                        @RequestBody FollowRequestDTO followRequestDTO) {
        String followerId = jwtUserDto.getName();
        followService.unfollowUser(Long.parseLong(followerId), followRequestDTO.getFollowingId());
        return new BaseResponse<>(null);
    }

    /**
     * 팔로워 목록 조회 API
     * userId 팔로워 목록을 조회할 사용자의 ID
     */
    @GetMapping("/follower")
    public BaseResponse<FollowListResponseDTO> getFollowers(@AuthenticationPrincipal JwtUserDto jwtUserDto) {
        Long userId = Long.parseLong(jwtUserDto.getName());
        FollowListResponseDTO response = followService.getFollowers(userId);
        return new BaseResponse<>(response);
    }

    /**
     * 팔로잉 목록 조회 API
     *  userId 팔로잉 목록을 조회할 사용자의 ID
     */
    @GetMapping("/following")
    public BaseResponse<FollowListResponseDTO> getFollowing(@AuthenticationPrincipal JwtUserDto jwtUserDto) {
        Long userId = Long.parseLong(jwtUserDto.getName());
        FollowListResponseDTO response = followService.getFollowing(userId);
        return new BaseResponse<>(response);
    }
}

