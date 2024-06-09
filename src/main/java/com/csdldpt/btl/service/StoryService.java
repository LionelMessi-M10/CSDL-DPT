package com.csdldpt.btl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csdldpt.btl.dto.StoryDTO;
import com.csdldpt.btl.entity.Story;


@Service
public interface StoryService {

	void saveStory(StoryDTO storyDTO, MultipartFile storyFile);
	List<Story> findFormTwoFile(MultipartFile[] multipartFiles);
}
