package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.CampaignClassificationTypeRepository;
import com.yeti.model.campaign.CampaignClassificationType;
import com.yeti.model.util.Batch;

@Service
public class CampaignClassificationTypeService {
	
	@Autowired
	private CampaignClassificationTypeRepository campaignClassificationTypeRepository;
	
	public List<CampaignClassificationType> getAllCampaignClassificationTypes() {
		List<CampaignClassificationType> campaignClassificationTypes = new ArrayList<CampaignClassificationType>();
		campaignClassificationTypeRepository.findAll().forEach(campaignClassificationTypes::add);
		return campaignClassificationTypes;
	}
	
	public CampaignClassificationType getCampaignClassificationType(Integer id) {
		return campaignClassificationTypeRepository.findOne(id);
	}
	
	public CampaignClassificationType addCampaignClassificationType(CampaignClassificationType campaignClassificationType) {
		return campaignClassificationTypeRepository.save(campaignClassificationType);
	}

	public CampaignClassificationType updateCampaignClassificationType(Integer id, CampaignClassificationType campaignClassificationType) {
		return campaignClassificationTypeRepository.save(campaignClassificationType);
	}

	public void deleteCampaignClassificationType(Integer id) {
		campaignClassificationTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return campaignClassificationTypeRepository.exists(id);
	}
	
}
