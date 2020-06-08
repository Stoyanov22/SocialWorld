package com.socialworld.web.controller;

import com.socialworld.web.entity.Chat;
import com.socialworld.web.entity.User;
import com.socialworld.web.service.ChatService;
import com.socialworld.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/chat/{userId}"}, method = RequestMethod.GET)
    public ModelAndView chat(HttpSession session, @PathVariable String userId) {
        ModelAndView model = new ModelAndView();
        if (Objects.isNull(session.getAttribute("uid"))) {
            model.setViewName("home/index");
            return model;
        } else {
            User fromUser = userService.getUserById(session.getAttribute("uid").toString());
            User toUser = userService.getUserById(userId);
            if(fromUser != null && toUser != null){
                List<Chat> chat = chatService.getChat(fromUser.getId(), toUser.getId());
                model.addObject("chat", chat);
                model.addObject("fromUser", fromUser);
                model.addObject("toUser", toUser);
                model.setViewName("user/chat");
            } else {
                model.setViewName("home/index");
            }
            return model;
        }
    }

    @RequestMapping(value = {"/chat"}, method = RequestMethod.POST)
    public void chat(HttpSession session, @RequestParam String content, @RequestParam String toUserId) {
        String userId = session.getAttribute("uid").toString();
        Date date = new Date();
        Chat chat = new Chat(userId + toUserId + date.getTime(), content, date, userId, toUserId);
        chatService.addChat(chat);
    }
}
