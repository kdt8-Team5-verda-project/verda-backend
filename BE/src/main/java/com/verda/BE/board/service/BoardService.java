package com.verda.BE.board.service;

import com.verda.BE.board.dto.requestdto.BoardCreateRequestDTO;
import com.verda.BE.board.dto.requestdto.BoardUpdateRequestDTO;
import com.verda.BE.board.dto.responsedto.BoardListResponseDTO;
import com.verda.BE.board.dto.responsedto.BoardResponseDTO;
import com.verda.BE.board.entity.UserPostEntity;
import com.verda.BE.board.repository.BoardRepository;
import com.verda.BE.login.entity.UserEntity;
import com.verda.BE.login.repository.KakaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.user.UserRegistryMessageHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BoardService {
    @Autowired
    private KakaoRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;


//    @Transactional
//    public BoardResponseDTO createBoard(BoardRequestDTO requestDTO, UserEntity userEntity) {
//        UserPostEntity userPostEntity = UserPostEntity.builder()
//                .title(requestDTO.getTitle())
//                .content(requestDTO.getContent())
//                .createdAt(new Timestamp(System.currentTimeMillis()))
//                .userEntity(userEntity)
//                .build();
//
//        UserPostEntity savedPost = boardRepository.save(userPostEntity);
//
//        // UserPostEntity를 BoardResponseDTO로 변환하는 메서드 호출
//        return mapToResponseDTO(savedPost);
//    }
//
//    private BoardResponseDTO mapToResponseDTO(UserPostEntity postEntity) {
//        return new BoardResponseDTO(postEntity);
//    }

//    public String createPost(BoardRequestDTO requestDTO){
//        UserPostEntity boards = UserPostEntity.builder()
//                .title(requestDTO.getTitle())
//                .content(requestDTO.getContent())
//                .build();
//
//        UserPostEntity createPost = boardRepository.save(boards);
//
//        return createPost.getTitle();
//    }

    //    게시물 생성
    @Transactional
    public Long create(BoardCreateRequestDTO requestDto,Long userId) {
        // UserRepository를 통해 userId에 해당하는 UserEntity를 가져옴
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // BoardCreateRequestDTO와 UserEntity를 사용하여 UserPostEntity 생성
        UserPostEntity userPostEntity = UserPostEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .userEntity(userEntity)
                .build();

        // 게시물 저장
        UserPostEntity savedPost = boardRepository.save(userPostEntity);

        return savedPost.getPostId();

//        return boardRepository.save(requestDto.toEntity()).getPostId();
    }

    //    게시물 수정
    @Transactional
    public Long update(Long id, BoardUpdateRequestDTO requestDto) {
        UserPostEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        board.update(requestDto.getTitle(),
                requestDto.getContent());

        return id;
    }

    //    게시물 조회
//    @Transactional(readOnly = true)
    @Transactional
    public BoardResponseDTO searchById(Long postId) {
        UserPostEntity board = boardRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        return new BoardResponseDTO(board);
    }

//    @Transactional
//    public List<BoardListResponseDTO> searchAllDesc() {
//        return boardRepository.findAllByOrderByCreatedAtDesc().stream()
//                .map(BoardListResponseDTO::new)
//                .collect(Collectors.toList());
//    }
    public List<BoardListResponseDTO> fetchPostPagesBy(Long lastPostId, int size){
        PageRequest pageRequest = PageRequest.of(0,size); // page는 0으로 고정,
        Page<UserPostEntity> entityPage=boardRepository.findByPostIdLessThanOrderByPostIdDesc(lastPostId,pageRequest);
        List<UserPostEntity> entityList = entityPage.getContent();

        return entityList.stream()
                .map(BoardListResponseDTO::new)
                .collect(Collectors.toList());
    }


    //    게시물 삭제
    @Transactional
    public void delete(Long id) {
        UserPostEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        boardRepository.delete(board);
    }

}
