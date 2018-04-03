package com.yeti.core.types.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.types.service.TagService;
import com.yeti.model.general.Tag;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Tag.class)
@RequestMapping(value = "/Tags", produces = "application/hal+json")
public class TagController {

	@Autowired
	private TagService tagService;

	@GetMapping
	public ResponseEntity<List<Resource<Tag>>> getAllTags() {
		List<Tag> tags = tagService.getAllTags();
		if( tags != null ) {
			List<Resource<Tag>> returnTags = new ArrayList<Resource<Tag>>();
			for( Tag tag : tags ) {
				returnTags.add(getTagResource(tag));
			}
			return ResponseEntity.ok(returnTags);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Tag>> getTag(@PathVariable Integer id) {
		Tag tag = tagService.getTag(id);
		if( tag == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getTagResource(tag));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Tag>> addTag(@RequestBody Tag tag, HttpServletRequest request ) {
		Tag newTag = tagService.addTag(tag);
		if( newTag != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newTag.getTagId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Tag>> updateTag(@RequestBody Tag tag, @PathVariable Integer id) {
		if( !tagService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			tagService.updateTag(id, tag);
			Tag updatedTag = tagService.updateTag(id, tag);
			if( updatedTag != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Tag>> deleteTag(@PathVariable Integer id) {
		if( !tagService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			tagService.deleteTag(id);
			if( !tagService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		tagService.processBatchAction(batch);
	}
	
	private Resource<Tag> getTagResource(Tag a) {
	    Resource<Tag> resource = new Resource<Tag>(a);
	    resource.add(linkTo(methodOn(TagController.class).getTag(a.getTagId())).withSelfRel());
	    return resource;
	}

}








