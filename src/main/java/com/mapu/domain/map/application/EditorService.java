package com.mapu.domain.map.application;

import com.mapu.domain.map.application.response.EditorListResponseDTO;
import com.mapu.domain.map.dao.EditorRepository;
import com.mapu.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EditorService {

    private final EditorRepository editorRepository;

    public List<EditorListResponseDTO> getRecommendedEditors(Long userId, int limit) {
        List<User> editors = editorRepository.findRandomEditorsExcludingFollowed(userId, limit);
        return convertToDTOList(editors);
    }

    public List<EditorListResponseDTO> getRandomEditors(int limit) {
        List<User> editors = editorRepository.findRandomEditors(limit);
        return convertToDTOList(editors);
    }

    private List<EditorListResponseDTO> convertToDTOList(List<User> users) {
        return users.stream()
                .map(EditorListResponseDTO::new)
                .collect(Collectors.toList());
    }
}