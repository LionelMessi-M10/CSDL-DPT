package com.csdldpt.btl.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csdldpt.btl.dto.StoryDTO;
import com.csdldpt.btl.entity.Keyword;
import com.csdldpt.btl.entity.Story;
import com.csdldpt.btl.repository.KeywordRepository;
import com.csdldpt.btl.repository.StoryRepository;
import com.csdldpt.btl.service.StoryService;
import com.csdldpt.btl.utils.TFIDF;

import jakarta.transaction.Transactional;

@Service
public class StoryServiceImpl implements StoryService{

	private final StoryRepository storyRepository;
	private final KeywordRepository keywordRepository;

	public StoryServiceImpl(StoryRepository storyRepository, KeywordRepository keywordRepository) {
		this.storyRepository = storyRepository;
		this.keywordRepository = keywordRepository;
	}

	public boolean isKeyword(String key){
		for(int i = 0; i < key.length(); ++i){
			if(!Character.isAlphabetic(key.charAt(i))){
				return false;
			}
		}
		return true;
	}

	@Override
	@Transactional
	public void saveStory(StoryDTO storyDTO, MultipartFile storyFile) {
		Story story = new Story();
		String[] arr = storyFile.getOriginalFilename().split(" - ");

		String author = "", title = "";

		for(int i = 0; i < arr.length - 1; ++i){
			title += arr[i];
		}

		for(int i = 0; i < arr[arr.length - 1].length() - 4; ++i){
			author += arr[arr.length - 1].charAt(i);
		}

		story.setTitle(title);
		story.setAuthor(author);
		story.setCategory(storyDTO.getCategory());

		Map<String, Double> map = TFIDF.calculateTFIDFFromMultipartFile(storyFile);

		List<Keyword> list = new ArrayList<>();

		for(Entry<String, Double> it : map.entrySet()){
			Keyword keyword = new Keyword();
			keyword.setKeyword(it.getKey());
			keyword.setFrequency(it.getValue());
			keyword.setStory(story);
			if(keyword.getFrequency() > 0 && isKeyword(keyword.getKeyword())) list.add(keyword);
		}
		story.setKeywords(list);
		storyRepository.save(story);
	}

	@Override
	public List<Story> findFormTwoFile(MultipartFile[] multipartFiles) {
		List<Story> stories = this.storyRepository.findAll();
		List<Story> result = new ArrayList<>();

		Map<String, Double> input = new HashMap<>();

		for(MultipartFile file : multipartFiles){
			for(Entry<String, Double> it : TFIDF.calculateTFIDFFromMultipartFile(file).entrySet()){
				if(it.getValue() > 0){
					input.put(it.getKey(), it.getValue());
				}
			}
		}

		Map<Long, Double> fre = new TreeMap<>();
		Double max1 = -1e18, max2 = -1e18, max3 = -1e18;

		for(Story it : stories){
			List<Keyword> keywords = it.getKeywords();
			Double frequentcy = 1.0;
			for(Keyword key : keywords){
				if(input.containsKey(key.getKeyword())){
					frequentcy *= key.getFrequency();
				}
			}
			if(frequentcy > max1){
				max3 = max2;
				max2 = max1;
				max1 = frequentcy;
			}
			else if (frequentcy > max2) {
				max3 = max2;
				max2 = frequentcy;
			} 
			else if (frequentcy > max3) {
					max3 = frequentcy;
			}
			fre.put(it.getId(), frequentcy);
		}

		for(Entry<Long, Double> it : fre.entrySet()){
			if(max1 == it.getValue()){
				result.add(this.storyRepository.findById(it.getKey()).get());
			}
			else if(max2 == it.getValue()){
				result.add(this.storyRepository.findById(it.getKey()).get());
			}
			else if(max3 == it.getValue()){
				result.add(this.storyRepository.findById(it.getKey()).get());
			}
		}

		return result;
	}
}
