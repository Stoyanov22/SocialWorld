package com.socialworld.web.service;

import com.socialworld.web.entity.Chat;

import java.util.List;


public interface ChatService {

    void addChat(Chat chat);

    List<Chat> getChat(String fromUserId, String toUserId);
}
