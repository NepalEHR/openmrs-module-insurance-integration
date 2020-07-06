package org.nepalehr.imisintegration.web.controller;

import java.io.IOException;

import org.nepalehr.imisintegration.bed.scheduler.service.IpdBedSalesQuotationService;
import org.nepalehr.imisintegration.pojo.InsuranceInformation;
import org.nepalehr.imisintegration.service.ImisConnectService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/imis")
public class ImisIntegrationController extends BaseRestController {

	@Autowired
	ImisConnectService imisConnectService;

	private IpdBedSalesQuotationService service;

	@Autowired
	public void setIPdBedSalesQuotation(IpdBedSalesQuotationService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/eligibility/{nhisNumber}", produces = "application/json")
	@ResponseBody
	public String getEligbility(@PathVariable("nhisNumber") String nhisNumber) throws IOException {
		return imisConnectService.eligibilityRequest(nhisNumber);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/validate/{nhisNumber}", produces = "application/json")
	@ResponseBody
	public String checkValidity(@PathVariable("nhisNumber") String nhisNumber) {
		return imisConnectService.insuranceCardValid(nhisNumber);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{patientUUID}", produces = "application/json")
	@ResponseBody
	public String getInformation(@PathVariable("patientUUID") String patientUUID) {
		return imisConnectService.getAccountInformation(patientUUID);
	}


	@RequestMapping(method = RequestMethod.GET, value = "/unique/{nhisNumber}", produces = "application/json")
	@ResponseBody
	public Boolean checkNhisNumber(@PathVariable("nhisNumber") String nhisNumber) {
		return imisConnectService.isInsuranceNumberUnique(nhisNumber);
	}
}
