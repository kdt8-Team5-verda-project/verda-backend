package com.verda.BE.chat.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class chatRoomEnterRequestDto {
    private long fmId;
    private long userId;
    private long postId;
}
