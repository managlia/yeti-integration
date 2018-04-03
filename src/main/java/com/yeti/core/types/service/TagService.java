package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.TagRepository;
import com.yeti.model.general.Tag;
import com.yeti.model.util.Batch;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;
	
	public List<Tag> getAllTags() {
		List<Tag> tags = new ArrayList<Tag>();
		tagRepository.findAll().forEach(tags::add);
		return tags;
	}
	
	public Tag getTag(Integer id) {
		return tagRepository.findOne(id);
	}
	
	public Tag addTag(Tag tag) {
		return tagRepository.save(tag);
	}

	public Tag updateTag(Integer id, Tag tag) {
		return tagRepository.save(tag);
	}

	public void deleteTag(Integer id) {
		tagRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}

	public boolean exists(Integer id) {
		return tagRepository.exists(id);
	}
	
}
