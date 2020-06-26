package org.nepalehr.imisintegration.service.impl;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.nepalehr.imisintegration.constants.ImisIntegrationProperties;
import org.nepalehr.imisintegration.pojo.InsuranceInformation;
import org.nepalehr.imisintegration.pojo.OpenImisAccountInformation;
import org.nepalehr.imisintegration.service.ImisConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@Service
public class ImisConnectServiceImpl implements ImisConnectService {

	Logger LOGGER = Logger.getLogger(ImisConnectServiceImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	private ImisIntegrationProperties properties;

	public void setImisIntegrationProperties(ImisIntegrationProperties properties) {
		this.properties = properties;
	}

	RestTemplate restTemplate = new RestTemplate();

	@Override
	public String getAccountInformation(String patientUUID) {
		// TODO make call to imis connect using httpclient
		/*
		 * OpenImisAccountInformation openImisAccountInformation = new
		 * OpenImisAccountInformation();
		 * openImisAccountInformation.setCardType("Normal");
		 * openImisAccountInformation.setFirstName("Paul");
		 * openImisAccountInformation.setLastName("Pogba");
		 * openImisAccountInformation.setNhisNumber(nhisNumber);
		 * openImisAccountInformation.setRemainingAmount(54009.23); try {
		 * openImisAccountInformation.setValidFrom(new
		 * SimpleDateFormat("yyyy/MM/dd").parse("2019/01/01"));
		 * openImisAccountInformation.setValidTill(new
		 * SimpleDateFormat("yyyy/MM/dd").parse("2019/12/31")); } catch (ParseException
		 * e) { LOGGER.error(e); e.printStackTrace(); }
		 */
		// patientUUID = "0506d06c-7916-4ae8-bb7e-2b7553679ff8";
		String name = "NHIS Primary Contact Point";
		OpenImisAccountInformation openImisAccountInformation = new OpenImisAccountInformation();
		openImisAccountInformation.setPuuid(patientUUID);
		String queryString = "SELECT pa.attributeType.name as name , pa.value as value FROM PersonAttribute pa WHERE pa.attributeType.name = :name and pa.person.uuid = :id";
		Query query = sessionFactory.getCurrentSession().createQuery(queryString);
		query.setParameter("id", openImisAccountInformation.getPuuid());
		query.setParameter("name", name);
		List<Object[]> results = query.list();
		InsuranceInformation eq = new InsuranceInformation();
		for (Object[] result : results) {
			eq.setValue((String) result[1]);
			eq.setName((String) result[0]);
		}
		return new Gson().toJson(eq);
	}

	@Override
	public String eligibilityRequest(String nhisNumber) {
		// String plainCreds = properties.getImisConnectUser() + ":" +
		// properties.getImisConnectPassword();
		String plainCreds = "admin" + ":" + "haha";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);

		HttpEntity<String> request = new HttpEntity<String>(headers);
		// String url = properties.getImisConnectUri() + "/check/eligibility/" +
		// nhisNumber;
		String url = "http://192.168.33.10:8092/insurance-integration/" + "/check/eligibility/" + nhisNumber;

		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		String response = responseEntity.getBody();
		LOGGER.error(response);
		return response;
	}

	@Override
	public String insuranceCardValid(String nhisNumber) {
		// String plainCreds = properties.getImisConnectUser() + ":" +
		// properties.getImisConnectPassword();
		String plainCreds = "admin" + ":" + "haha";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);

		HttpEntity<String> request = new HttpEntity<String>(headers);

		// String url = properties.getImisConnectUri() + "get/insuree/" + nhisNumber;
		String url = "http://192.168.33.10:8092/insurance-integration/" + "get/insuree/" + nhisNumber;

		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		String response = responseEntity.getBody();
		LOGGER.error(response);
		return response;
	}

	@Override
	public Boolean isInsuranceNumberUnique(String nhisNumber) {
		System.out.println("from implementation 1");
		boolean isUnique = true;
		String type = "NHIS Number";
		OpenImisAccountInformation openImisAccountInformation = new OpenImisAccountInformation();
		openImisAccountInformation.setNhisNumber(nhisNumber);
		String queryString = "SELECT  pa.attributeType.name  FROM PersonAttribute pa"
				+ " WHERE pa.attributeType.name = :name AND pa.value = :nhisNumber";
		Query query = sessionFactory.getCurrentSession().createQuery(queryString);
		query.setParameter("nhisNumber", openImisAccountInformation.getNhisNumber());
		query.setParameter("name", type);
		// String anc = Integer.toString(query.list().size());
		if (query.list() != null && query.list().size() == 1)
			isUnique = false;
		else {
			isUnique = true;
		}
		return isUnique;
	}
}
