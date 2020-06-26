package org.nepalehr.imisintegration.service;

import org.nepalehr.imisintegration.pojo.InsuranceInformation;

public interface ImisConnectService {

	String getAccountInformation(String patientUUID);

	String insuranceCardValid(String nhisNumber);

	String eligibilityRequest(String nhisNumber);

	Boolean isInsuranceNumberUnique(String nhisNumber);
}
