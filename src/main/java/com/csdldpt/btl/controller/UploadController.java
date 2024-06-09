package com.csdldpt.btl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.csdldpt.btl.dto.StoryDTO;
import com.csdldpt.btl.service.StoryService;

@Controller
public class UploadController {
	
	private StoryService storyService;
	
	public UploadController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping("/upload")
    public String showUploadForm(@ModelAttribute("storyDTO") StoryDTO storyDTO) {
        return "index";
    }

    @PostMapping("/uploadFile")
    public String uploadStory(@ModelAttribute("storyDTO") StoryDTO storyDTO, @RequestParam("storyFile") MultipartFile file) {
        this.storyService.saveStory(storyDTO, file);
        return "redirect:/upload";
    }
}
