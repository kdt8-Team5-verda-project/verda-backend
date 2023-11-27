package com.verda.BE.chat.controller;

import com.verda.BE.chat.dto.requestDto.ChatMessageRequestDTO;
import com.verda.BE.chat.dto.responseDto.GetPreChatListDTO;
import com.verda.BE.chat.dto.responseDto.GetTargetName;
import com.verda.BE.chat.dto.responseDto.RecieveMessageResponseDTO;
import com.verda.BE.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final ChatService chatService;
    private final SimpMessagingTemplate template;
//    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 유저용 채팅방 이름 가져오기
     * @param roomId
     */
    @Operation(summary = "채팅방 이름 가져오기", description = "")
    @GetMapping("/api/chat/user/{roomId}")
    public GetTargetName getUserChatName(@PathVariable("roomId") long roomId){
        GetTargetName targetName = chatService.getUserChatName(roomId);
        return targetName;
    }

    /**
     * 펀드매니저용 채팅방 이름 가져오기
     * @param roomId
     */
    @Operation(summary = "채팅방 이름 가져오기", description = "")
    @GetMapping("/api/chat/fm/{roomId}")
    @Cacheable(key = "#id", cacheNames = "member")
    public GetTargetName getFmTargetName(@PathVariable("roomId") long roomId){
        GetTargetName targetName = chatService.getFmChatName(roomId);
        return targetName;
    }

    @Operation(summary = "채팅방 입장시 이전 채팅목록 조회", description = "채팅방 입장시 이전 채팅들을 불러옴")
    @GetMapping("/api/chat/{roomId}")
    public GetPreChatListDTO getPreMessage(@PathVariable("roomId") long roomId) {
        GetPreChatListDTO preMessage = chatService.getPreMessage(roomId);
        return preMessage;
    }

    @MessageMapping("/api/chat/room/entered")
    public void entered(@RequestBody ChatMessageRequestDTO chatMessageRequestDTO){
        template.convertAndSend("/sub/chat/room/"+chatMessageRequestDTO.getRoomId(),chatMessageRequestDTO.getContent());
    }

    @MessageMapping("/api/send/messages/{roomId}")
    public void message(@RequestBody ChatMessageRequestDTO chatMessageRequestDTO){
        RecieveMessageResponseDTO recieveMessage = chatService.sendMessage(chatMessageRequestDTO);
        template.convertAndSend("/sub/chat/room/" + chatMessageRequestDTO.getRoomId(), chatMessageRequestDTO.getContent());
    }
    
//    redis를 메시지 브로커로 하는 채팅
//    @MessageMapping("/api/chat")
//    public void message(@RequestBody ChatMessageRequestDTO chatMessageRequestDTO) {
//        RecieveMessageResponseDTO recieveMessage = chatService.sendMessage(chatMessageRequestDTO);
//        redisTemplate.convertAndSend("/sub/api/chat",chatMessageRequestDTO);
//    }
}