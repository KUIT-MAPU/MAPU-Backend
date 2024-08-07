package com.mapu.domain.map.api;

import com.mapu.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    @RequestMapping("/editor")
    @GetMapping
    public BaseResponse<Void> getRecommandEditor() {
        return new BaseResponse<>(null);
    }

    @RequestMapping("/keyword")
    @GetMapping
    public BaseResponse<Void> getRecommandKeyword() {
        return new BaseResponse<>(null);
    }




}
