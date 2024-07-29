package com.mapu.domain.follow.api;

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

    // @AuthenticationPrincipal: 현재 인증된 사용자의 ID를 주입?
    // @PathVariable: URL 경로에서 변수 값을 추출

    /**
     * 유저 팔로우 API
     * @param followerId 사용자의 ID  => 나
     * @param userId 팔로우할 대상 사용자의 ID
     */
    @PostMapping("/{userId}")
    public BaseResponse<FollowResponseDTO> followUser(@AuthenticationPrincipal Long followerId,
                                                      @PathVariable Long userId) {
        FollowResponseDTO response = followService.followUser(followerId, userId);
        return new BaseResponse<>(response);
    }

    /**
     * 유저 언팔로우 API
     */
    @DeleteMapping("/{userId}")
    public BaseResponse<FollowResponseDTO> unfollowUser(@AuthenticationPrincipal Long followerId,
                                                        @PathVariable Long userId) {
        FollowResponseDTO response = followService.unfollowUser(followerId, userId);
        return new BaseResponse<>(response);
    }
    /**
     * 팔로워 목록 조회 API
     */
    @GetMapping("/followers/{userId}")
    public BaseResponse<FollowListResponseDTO> getFollowers(@PathVariable Long userId) {
        FollowListResponseDTO response = followService.getFollowers(userId);
        return new BaseResponse<>(response);
    }

    /**
     * 팔로잉 목록 조회 API
     */
    @GetMapping("/following/{userId}")
    public BaseResponse<FollowListResponseDTO> getFollowing(@PathVariable Long userId) {
        FollowListResponseDTO response = followService.getFollowing(userId);
        return new BaseResponse<>(response);
    }
}