package com.mapu.domain.follow.api;

import com.mapu.domain.follow.api.request.FollowRequestDTO;
import com.mapu.domain.follow.application.FollowService;
import com.mapu.domain.follow.application.response.FollowResponseDTO;
import com.mapu.domain.follow.application.response.FollowListResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    /**
     * 유저 팔로우 API
     * followerId 나
     * followingId 팔로우할 대상
     */
    @PostMapping
    public BaseResponse<FollowResponseDTO> followUser(@AuthenticationPrincipal Long followerId,
                                                      @RequestBody FollowRequestDTO followRequestDTO) {
        System.out.println("sss"+followerId);
        FollowResponseDTO response = followService.followUser(followerId, followRequestDTO.getFollowingId());
        return new BaseResponse<>(response);
    }

    /**
     * 유저 언팔로우 API
     * followerId 나
     * followingId 언팔로우할 대상
     */
    @DeleteMapping
    public BaseResponse<FollowResponseDTO> unfollowUser(@AuthenticationPrincipal Long followerId,
                                                        @RequestBody FollowRequestDTO followRequestDTO) {
        FollowResponseDTO response = followService.unfollowUser(followerId, followRequestDTO.getFollowingId());
        return new BaseResponse<>(response);
    }

    /**
     * 팔로워 목록 조회 API
     * userId 팔로워 목록을 조회할 사용자의 ID
     */
    @GetMapping("/followers/{userId}")
    public BaseResponse<FollowListResponseDTO> getFollowers(@PathVariable Long userId) {
        FollowListResponseDTO response = followService.getFollowers(userId);
        return new BaseResponse<>(response);
    }

    /**
     * 팔로잉 목록 조회 API
     *  userId 팔로잉 목록을 조회할 사용자의 ID
     */
    @GetMapping("/following/{userId}")
    public BaseResponse<FollowListResponseDTO> getFollowing(@PathVariable Long userId) {
        FollowListResponseDTO response = followService.getFollowing(userId);
        return new BaseResponse<>(response);
    }
}

