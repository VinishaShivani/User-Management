package com.trb.allocationservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trb.allocationservice.dao.AllocationMapper;
import com.trb.allocationservice.dao.ObjQuestionModAnsRepo;
import com.trb.allocationservice.dao.SmeActionsRepo;
import com.trb.allocationservice.dao.objectedAllocationRemarksRepo;
import com.trb.allocationservice.dto.Level;
import com.trb.allocationservice.dto.LevelDTO;
import com.trb.allocationservice.dto.RemarksDTO;
import com.trb.allocationservice.dto.ResponseDTO;
import com.trb.allocationservice.dto.SmeActionDTO;
import com.trb.allocationservice.entity.ObjectedDocumentModel;
import com.trb.allocationservice.entity.ObjectedQuestionRemarksModel;
import com.trb.allocationservice.entity.ObjectionQuesModAns;
import com.trb.allocationservice.service.SmeActionServices;
import com.trb.allocationservice.util.AppConstants;

@Service
public class SmeActionServiceImpl implements SmeActionServices {

	@Autowired
	SmeActionsRepo smeActionsRepo;

	@Autowired
	objectedAllocationRemarksRepo AllocationRemarksRepo;

	@Autowired
	AllocationMapper mapper;
	
	@Autowired
	ObjQuestionModAnsRepo objQuestionModAnsRepo;

	@Override
	public ResponseDTO getSmeActions(Long QuestionId,boolean isAdmin,Long levelId) {
		
		String currentStatus =isAdmin?null:"N";
		Long inputlevelId = isAdmin?null:levelId;
		List<Level> objalloc = smeActionsRepo.getSmeDetailsByobjectedQuestionsPk(QuestionId,currentStatus,inputlevelId);
		ResponseDTO response = new ResponseDTO();
		if (!objalloc.isEmpty()) {
			SmeActionDTO sme = new SmeActionDTO();
			List<LevelDTO> lvl = new ArrayList<>();
			objalloc.stream().forEach(e -> {
				List<String> docs = null;
				List<String> files = null;
				if (e.getObjrmk() != null) {
					ObjectedQuestionRemarksModel remarkModel = AllocationRemarksRepo.findByObjallocpk(e.getobjallocId());
					List<String> documentPaths = remarkModel.getDocuments().stream().map(ObjectedDocumentModel::getDocumentPath).collect(Collectors.toList());
					List<String> fileNames = getListOfFiles(documentPaths);
					docs = documentPaths;files = fileNames; 
					List<ObjectionQuesModAns> objectionQuesModAns =  objQuestionModAnsRepo.findByObjquesIdAndObjallocPK(e.getquestionId().intValue(),e.getobjallocId().intValue());
					StringBuilder sb=new StringBuilder();
					objectionQuesModAns.forEach(opt -> {
						String options = AppConstants.options.get(opt.getModifyoptionId());
						sb.append(options).append(",");
					});
					String resSelectedBySme=sb.toString();
					if (resSelectedBySme.endsWith(",")) {
						resSelectedBySme = resSelectedBySme.substring(0, resSelectedBySme.length() - 1);
						}
					LevelDTO lv = mapper.smeActionDTO(e.getuserId(), e.getsmeNo(), e.getsmefirstname(),e.getlastname(), e.getlevel(),e.getstatus(),e.getAllocatedDt(),e.getActionDt(), e.getAction(),
							      remarkModel.getSubject(), remarkModel.getStandard(),remarkModel.getPageNumber(), remarkModel.getRemarks(),remarkModel.getAuthor(),remarkModel.getReferenceUrl(), docs, files, resSelectedBySme);
				  	lvl.add(lv);
				} else {
					LevelDTO lv = mapper.smeActionDTO(e.getuserId(), e.getsmeNo(), e.getsmefirstname(),e.getlastname(), e.getlevel(),e.getstatus(),e.getAllocatedDt(),e.getActionDt(),null,null,null,null,null,null,null, docs, files,null);
					lvl.add(lv);
				}
				sme.setObjQuesId(e.getquestionId());
				sme.setDate(e.getDate());
			});
			sme.setLevel(lvl);
			response.setStatusCode("SUCC-03");
			response.setMessage("Success");
			response.setResponseContent(sme);
		} else {
			response.setStatusCode("ERROR-03");
			response.setMessage("List is Empty");
		}
		return response;

	}

	private List<String> getListOfFiles(List<String> documentPaths) {
		// TODO Auto-generated method stub
		List<String> fileList = new ArrayList<>();
		for (String str : documentPaths) {
			String[] splittedStr = str.split("/");
			System.out.print("fileList:" + splittedStr);
			fileList.add(splittedStr[splittedStr.length - 1]);
		}
		return fileList;
	}

   
}
