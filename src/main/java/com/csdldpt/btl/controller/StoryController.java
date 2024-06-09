package com.csdldpt.btl.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.csdldpt.btl.entity.Story;
import com.csdldpt.btl.service.StoryService;


@Controller
public class StoryController {

    @Autowired
    private StoryService storyService;

    @GetMapping("/storyFind")
    public String find(){
        return "story";
    }

    @PostMapping("/storyFind")
    public String postMethodName(@RequestParam("storyFile") MultipartFile[] multipartFiles, Model model) {
        List<Story> stories = this.storyService.findFormTwoFile(multipartFiles);
        model.addAttribute("stories", stories);
        return "story";
    }
    

}
