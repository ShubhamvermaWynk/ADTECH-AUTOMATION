package com.airtel.teams.common;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Record;

import com.airtel.common.dto.request.DbRequestDto;
import com.airtel.common.dto.response.NegResponseDTO;
import com.airtel.helper.common.BaseManager;
import com.airtel.helper.data.DataHelper;
import com.airtel.helper.report.ComparatorReportGenerator;
import com.airtel.helper.report.FileHelper;
import com.airtel.helper.report.ReportHelper;

import com.airtel.adtech.constants.enums.DbType;
import com.airtel.validator.GenericComparator;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flipkart.zjsonpatch.JsonDiff;
import com.google.gson.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.testng.Assert;
import org.testng.Reporter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class CommonApi extends BaseManager {

	FileHelper fileHelper = CommonFrameworkObjectStore.getFileHelperObject();
	ReportHelper reporter = CommonFrameworkObjectStore.getReportHelperObject();
	GenericComparator genericComparator = CommonFrameworkObjectStore.getGenericComparatorObject();

	final static String EXPECTED_RESULT = "expectedMap";
	final static String ACTUAL_RESULT = "actualMap";

	static final String OPERATION = "op";
	static final String DIFFERENCE_PATH = "path";
	static final String DIFFERENCE_VALUE = "value";
	static final String FAILURE_LOG_START = "";
	static final String FAILURE_LOG_END = "";

	public Response getDevicesResponseWithHeadersIgnoreSecurity(String serverInitials, Map<String, String> queryParams,
			String path, boolean checkStatus, boolean isComsiteHeader, String ivUser, List<Header> extraHeaders,
			String uniqueidentifier) {

		String serviceUrl = serverInitials + path;
		List<Header> requestHeader = generateHeader(isComsiteHeader, ivUser, uniqueidentifier);

		if (extraHeaders != null) {
			for (Header header : extraHeaders) {
				requestHeader.add(header);
			}
		}

		Response responseObj = getResponseWithHeaders(queryParams, requestHeader, serviceUrl, checkStatus);

		return responseObj;
		
	}

	public static List<Header> generateHeader(boolean isComsiteHeader, String ivUser, String uniqueidentifier) {
		List<Header> requestHeader = new ArrayList<Header>();
		requestHeader.add(new Header("googleCookie", "google.com"));
		requestHeader.add(new Header("userId", uniqueidentifier));
		if (isComsiteHeader && ivUser != null) {
			requestHeader.add(new Header("requesterid", "comsite"));
			requestHeader.add(new Header("iv-User", ivUser));
			requestHeader.add(new Header("adsHeader", RandomStringUtils.randomAlphanumeric(10)));
		}
		if (isComsiteHeader) {
			requestHeader.add(new Header("requesterid", "comsite"));
			requestHeader.add(new Header("adsHeader", "adsHeader"));
		} else {
			requestHeader.add(new Header("iv-User", ivUser));
			requestHeader.add(new Header("adsHeader", RandomStringUtils.randomAlphanumeric(10)));
		}

		return requestHeader;
	}

	public void printMapValues(Map<String, String> numberType) {
		for (Map.Entry<String, String> entry : numberType.entrySet()) {
			String number = entry.getKey();
			String billplantype = entry.getValue();

		}
	}

	public String[] getFilterSubsets(String set[]) {
		int n = set.length;
		int number = (int) (Math.pow(2, n));
		String filterCombinations[] = new String[number - 1];

		// Run a loop for printing all 2^n
		// subsets one by one
		for (int i = 0; i < (1 << n); i++) {
			String params = "";

			// Print current subset
			for (int j = 0; j < n; j++)

				// (1<<j) is a number with jth bit 1
				// so when we 'and' them with the
				// subset number we get which numbers
				// are present in the subset and which
				// are not
				if ((i & (1 << j)) > 0) {
					params = params + set[j] + ",";
				}
			if (!params.equals("")) {
				params = params.substring(0, params.length() - 1);
				filterCombinations[i - 1] = params;
			}

		}
		return filterCombinations;
	}

	public List<Header> getChannelHeader() {
		List<Header> channelHeaders = new ArrayList<>();
		channelHeaders.add(new Header("X-ADF-CHANNEL", "AIRTEL_STORE"));
		return channelHeaders;
	}

	public static boolean compareMap(Map p, Map q) {
		boolean flag = true;
		if (p == q)
			return true;
		if (p.size() != q.size()) {
			ReportHelper.logValidationFailure("Maps Size Not Equal", "Map Size for map1:" + p.size(),
					"Map Size for Map 2:" + q.size(), "Map Size of both the maps are unequal");
			return false;
		}
		if (p == null || q == null)
			flag = flag && false;

		for (Object k : p.keySet()) {
			boolean found = false;
			Object qVal = null;
			try {
				if (q.containsKey(k)) {
					qVal = q.get(k);
					found = true;
				}
			} catch (NullPointerException ex) {
				ReportHelper.logValidationFailure(String.valueOf(k), "No Exception", ex.toString(),
						"Exception while fetching value for key='" + String.valueOf(k) + "'");
				return false;
			}

			if (found) {
				Object pVal = p.get(k);
				if (pVal == qVal)
					continue; // null or same reference
				if (qVal == null || pVal == null) {
					ReportHelper.logValidationFailure(String.valueOf(k), "No Null Value",
							"Value in map1:" + String.valueOf(pVal) + ";Value in map2=" + String.valueOf(qVal),
							"Null Value for key='" + String.valueOf(k) + "'");
					flag = flag && false;
				} else {
					if (!pVal.equals(qVal)) {
						ReportHelper.logValidationFailure(String.valueOf(k), String.valueOf(pVal), String.valueOf(qVal),
								"Value mismatch for key='" + String.valueOf(k) + "'");
						flag = flag && false;
					}
				}
			} else {
				ReportHelper.logValidationFailure(String.valueOf(k), "Key Should be Present in Map2",
						"Key Not Present in Map2", "Key Not Present in Map2 for key='" + String.valueOf(k) + "'");
				flag = flag && false;
			}

		}
		return flag;
	}

	public Response getPostResponse(String body, String serverInitials, String path, boolean checkStatus,
			boolean isComsiteHeader, String ivUser, List<Header> extraHeaders, String uniqueidentifier) {
		String serviceUrl = serverInitials + path;
		List<Header> requestHeader = generateHeader(isComsiteHeader, ivUser, uniqueidentifier);

		if (extraHeaders != null) {
			for (Header header : extraHeaders) {
				requestHeader.add(header);
			}
		}
		Response responseObj = postResponseWithHeaders(body, requestHeader, ContentType.JSON, serviceUrl, checkStatus);
		return responseObj;
	}

	public static String uniqueidentifier() {
		String uniqueidentifier = RandomStringUtils.randomAlphanumeric(10);
		return uniqueidentifier;

	}

	public Response getPostResponseWithStatusCode(String body, String serverInitials, String path,
			boolean isComsiteHeader, int expectedStatusCode, String ivUser, List<Header> extraHeaders,
			String uniqueidentifier) {

		String serviceUrl = serverInitials + path;

		List<Header> requestHeader = generateHeader(isComsiteHeader, ivUser, uniqueidentifier);

		if (extraHeaders != null) {
			for (Header header : extraHeaders) {
				requestHeader.add(header);
			}
		}
		Response responseObj = postResponseWithHeaders(body, requestHeader, ContentType.JSON, serviceUrl,
				expectedStatusCode, null, null, true);
		return responseObj;
	}

	public Response patchResponseWithStatusCode(String body, String serverInitials, String path,
			boolean isComsiteHeader, int expectedStatusCode, String ivUser, List<Header> extraHeaders,
			Map<String, String> queryParams, String uniqueidentifier) {

		String serviceUrl = serverInitials + path;

		List<Header> requestHeader = generateHeader(isComsiteHeader, ivUser, uniqueidentifier);

		if (extraHeaders != null) {
			for (Header header : extraHeaders) {
				requestHeader.add(header);
			}
		}
		Response responseObj = patchResponseWithHeaders(body, requestHeader, ContentType.JSON, serviceUrl,
				expectedStatusCode, null, null, queryParams, true);
		return responseObj;
	}

	public Response getPostResponseWithStatusCodeandParams(String body, String serverInitials, String path,
			boolean isComsiteHeader, int expectedStatusCode, String ivUser, List<Header> extraHeaders,
			Map<String, String> queryParams, String uniqueidentifier) {

		String serviceUrl = serverInitials + path;

		List<Header> requestHeader = generateHeader(isComsiteHeader, ivUser, uniqueidentifier);

		if (extraHeaders != null) {
			for (Header header : extraHeaders) {
				requestHeader.add(header);
			}
		}
		Response responseObj = postResponseWithQueryParams(body, requestHeader, ContentType.JSON, serviceUrl,
				expectedStatusCode, queryParams, null, true);
		return responseObj;
	}

	/**
	 * This method will be used for getting response of POST request with headers.
	 * This method can pass/fail non-200 HTTP status code based on checkStatus field
	 * value as false/true.
	 *
	 * @param body
	 * @param headers
	 * @param contentType
	 * @param url
	 * @param statusCode
	 * @param formParams
	 * @param multipartFile
	 * @return
	 */
	public Response postResponseWithHeaders(String body, List<Header> headers, ContentType contentType, String url,
			int statusCode, Map<String, String> formParams, Map<String, List<File>> multipartFile,
			Boolean disableDefaultCharset) {
		return fetchApiResponse(url, "POST", body, null, headers, contentType, statusCode, formParams, multipartFile,
				disableDefaultCharset);
	}

	public Response patchResponseWithHeaders(String body, List<Header> headers, ContentType contentType, String url,
			int statusCode, Map<String, String> formParams, Map<String, List<File>> multipartFile,
			Map<String, String> queryParams, Boolean disableDefaultCharset) {
		return fetchApiResponse(url, "PATCH", body, queryParams, headers, contentType, statusCode, formParams,
				multipartFile, disableDefaultCharset);
	}

	public Response postResponseWithQueryParams(String body, List<Header> headers, ContentType contentType, String url,
			int statusCode, Map<String, String> queryParams, Map<String, List<File>> multipartFile,
			Boolean disableDefaultCharset) {
		return fetchApiResponse(url, "POST", body, queryParams, headers, contentType, statusCode, null, multipartFile,
				disableDefaultCharset);
	}

	public Response getPostResponseWithStatusCode(String body, String serverInitials, String path,
			boolean isComsiteHeader, int expectedStatusCode, String ivUser, List<Header> extraHeaders,
			String uniqueidentifier, Map<String, String> formParams) {

		String serviceUrl = serverInitials + path;

		List<Header> requestHeader = generateHeader(isComsiteHeader, ivUser, uniqueidentifier);

		if (extraHeaders != null) {
			for (Header header : extraHeaders) {
				requestHeader.add(header);
			}
		}
		Response responseObj = postResponseWithHeaders(body, requestHeader, ContentType.URLENC, serviceUrl,
				expectedStatusCode, formParams);
		return responseObj;
	}

	public Response getPostResponseWithStatusCodeMultipart(String body, String serverInitials, String path,
			boolean isComsiteHeader, int expectedStatusCode, String ivUser, List<Header> extraHeaders,
			String uniqueidentifier, Map<String, String> formParams, Map<String, List<File>> multipartFile) {

		String serviceUrl = serverInitials + path;

		List<Header> requestHeader = generateHeader(isComsiteHeader, ivUser, uniqueidentifier);

		if (extraHeaders != null) {
			for (Header header : extraHeaders) {
				requestHeader.add(header);
			}
		}
		requestHeader.add(new Header("Content-Type", "multipart/form-data"));
		Response responseObj = postResponseWithHeaders(body, requestHeader, null, serviceUrl, expectedStatusCode,
				formParams, multipartFile, null);
		return responseObj;
	}

	public List<JSONObject> getdatafrommongo(String serverInitials, Integer port, Map<String, String> map,
			String dataBaseName, String collectionName) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = getMongoClient(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeys(map);
			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);
			jsondata = parseMongoDocuments(dataobj);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}
	
	
	
	 public BasicDBObject prepareMongoQueryFromMultipleKeyswithRegex(Map<String, String> map,String status) {
	      BasicDBObject andQuery = new BasicDBObject();
		 BasicDBList basicDBList = new BasicDBList();      
		 Iterator var4 = map.entrySet().iterator();  
	  
		 while(var4.hasNext()) {
		       Entry<String, String> entry = (Entry)var4.next();
		      
		       basicDBList.add(new BasicDBObject((String)entry.getKey(), new BasicDBObject("$regex", entry.getValue())
		    	        .append("$options", "i")));
		       
		       }
	 
	  
		     andQuery.put("$or", basicDBList);
		     
		     if(status!=null) {
		     andQuery.put("status", status);}
		     
		     
		      return andQuery;
		     }
	 
	 
	 public BasicDBObject prepareMongoQueryFromMultipleKeyswithRegexAndExists(Map<String, String> map,String status, String existsFilter) {
	      BasicDBObject andQuery = new BasicDBObject();
		 BasicDBList basicDBList = new BasicDBList();      
		 Iterator var4 = map.entrySet().iterator();  
	  
		 while(var4.hasNext()) {
		       Entry<String, String> entry = (Entry)var4.next();
		      
		       basicDBList.add(new BasicDBObject((String)entry.getKey(), new BasicDBObject("$regex", entry.getValue())
		    	        .append("$options", "i")));
		       
		       }
	 
	  
		     andQuery.put("$or", basicDBList);
		     
		     if(status!=null) {
		     andQuery.put("status", status);}
		     
		     if(existsFilter!=null) {
		     andQuery.put(existsFilter, new BasicDBObject("$exists", true));}
		     
		     
		      return andQuery;
		     }

	public List<JSONObject> getdatafrommongo(String[] serverInitials, Integer port, Map<String, String> map,
			String dataBaseName, String collectionName) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeys(map);
			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);
			jsondata = parseMongoDocuments(dataobj);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}

	public void insertNewDocumentIntoMongoCollection(String[] serverInitials, Integer port, String dataBaseName,
			String collectionName, Document newDocument) {

		// Creating a MongoDB client
		MongoClient mongoClient = null;
		try {

			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);

			// Inserting the document into the collection
			collection.insertOne(newDocument);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}

	public JSONObject convertStringToJSONObject(String str) {
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(str);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return json;
	}

	public void updateMongoData(String serverInitials, Integer port, String dataBaseName, String collectionName,
			String searchKey, String searchValue, Object updatedObj) {
		MongoClient mongoClient = null;

		try {
			mongoClient = getMongoClient(serverInitials, port);
			updateMongoDocument(mongoClient, collectionName, dataBaseName, searchKey, searchValue, updatedObj);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}

	public void updateMongoData(String[] serverInitials, Integer port, String dataBaseName, String collectionName,
			String searchKey, String searchValue, Object updatedObj) {
		MongoClient mongoClient = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			updateMongoDocument(mongoClient, collectionName, dataBaseName, searchKey, searchValue, updatedObj);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}

	public void updateMongoData(String[] serverInitials, Integer port, String dataBaseName, String collectionName,
			String searchKey, String searchValue, Map updatedObj) {
		MongoClient mongoClient = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			updateMongoDocument(mongoClient, collectionName, dataBaseName, searchKey, searchValue, updatedObj);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}

	public List<JSONObject> getdatafrommongo(String serverInitials, Integer port, String dataBaseName,
			String collectionName, Map<String, Object> map) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {

			mongoClient = getMongoClient(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeysObject(map);
			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);
			jsondata = parseMongoDocuments(dataobj);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}

	public List<JSONObject> getdatafrommongo(String[] serverInitials, Integer port, String dataBaseName,
			String collectionName, Map<String, Object> map) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeysObject(map);
			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);
			jsondata = parseMongoDocuments(dataobj);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}

	public BasicDBObject prepareMongoQueryFromMultipleKeysObject(Map<String, Object> map) {

		BasicDBObject andQuery = new BasicDBObject();
		BasicDBList basicDBList = new BasicDBList();
		for (Map.Entry<String, Object> entry : map.entrySet())
			basicDBList.add(new BasicDBObject(entry.getKey(), entry.getValue()));

		andQuery.put("$and", basicDBList);
		System.out.println("andQuery" + andQuery);
		return andQuery;
	}

	public boolean deleteKeyFromCache(AerospikeClient client, String key, String nameSpace, String setName) {
		try {
			boolean deleteStatus = true;

			String keys[] = key.split(",");
			for (String keyString : keys) {
				deleteStatus = deleteStatus & (deleteRecordForAkey(client, nameSpace, setName, keyString));
			}
			return deleteStatus;
		} catch (Exception e) {
			client.close();
			ReportHelper.reporterLogging(false, "exception in Common api getDataFromCacheByKey:" + e);
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		return false;
	}

	public List<Map<String, Object>> getDataFromCacheByKey(AerospikeClient client, String key, String nameSpace,
			String set, String binName) {
		try {
			List<String> keys = new ArrayList<>();
			keys.add(key);

			Record[] records = getMultipleRecordFromPKList(client, nameSpace, set, keys, binName);
			List<Map<String, Object>> aerospikeData = parseMultipleRecord(records);
			return aerospikeData;
		} catch (Exception e) {
			client.close();
			ReportHelper.reporterLogging(false, "exception in Common api getDataFromCacheByKey:" + e);
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		return null;
	}

	public Properties getConfigPropertyObject(String environment) {
		FileInputStream stream = null;
		Properties propertyFile = new Properties();
		try {
			stream = new FileInputStream(
					System.getProperty("user.dir") + "/config/config_" + environment + ".properties");
			try {
				propertyFile.load(stream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ReportHelper.logValidationFailure("File Not Found Exception", "true", "false", e.toString());
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			ReportHelper.logValidationFailure("File Not Found Exception", "true", "false", e.toString());
			e.printStackTrace();
		}
		return propertyFile;
	}

	public Properties getTestDataConfigPropertyObject(String moduleName) {
		FileInputStream stream = null;
		Properties propertyFile = new Properties();
		try {
			stream = new FileInputStream(System.getProperty("user.dir") + "/config/" + moduleName + "/" + moduleName
					+ "TestData.properties");
			try {
				propertyFile.load(stream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ReportHelper.logValidationFailure("File Not Found Exception", "true", "false", e.toString());
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			ReportHelper.logValidationFailure("File Not Found Exception", "true", "false", e.toString());
			e.printStackTrace();
		}
		return propertyFile;
	}

	public void closeDbConnection(Connection connection) {
		try {

			connection.close();

		} catch (SQLException e) {
			ReportHelper.logValidationFailure("Exception Occured", "No Exception",
					"Exception:" + e.getStackTrace().toString(), "Exception " + e.toString() + " Occured");
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	public boolean deletefrommongo(String serverIp, Integer port, String key, Object value, String dataBaseName,
			String collectionName) {

		MongoClient mongoClient = getMongoClient(serverIp, port);
		boolean deleteddocument = findMongoDocumentAndDeleteIt(mongoClient, key, value, dataBaseName, collectionName);

		return deleteddocument;
	}

	public boolean deletefrommongo(String[] serverIp, Integer port, String key, Object value, String dataBaseName,
			String collectionName) {

		MongoClient mongoClient = addMultiServer(serverIp, port);
		boolean deleteddocument = findMongoDocumentAndDeleteIt(mongoClient, key, value, dataBaseName, collectionName);

		return deleteddocument;
	}

	public String generateHash(String input) {
		final StringBuilder hash = new StringBuilder();

		try {
			final MessageDigest sha = MessageDigest.getInstance("SHA-1");
			final byte[] hashedBytes = sha.digest(input.getBytes());
			final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
					'h', 'i', 'j', 'k', 'A', 'B', 'C', 'D', 'Z' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				final byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (final NoSuchAlgorithmException ex) {
			ReportHelper.logValidationFailure("hash function", "no exception", ex.toString(),
					"exception while generating hash function");
			ex.printStackTrace();
			Assert.assertTrue(false);
		}

		return hash.toString();
	}

	public void compareObjects(Object expectedData, Object actualData, String propertyFilePath) {
		if (!expectedData.equals(actualData)) {
			String stableResp = convertToJson(expectedData);
			String qaResp = convertToJson(actualData);
			boolean comparatorStatus = compareJson(stableResp, qaResp, propertyFilePath);
			Assert.assertTrue(comparatorStatus);
		}
	}

	public void compareObjectswithObjectMapper(Object expectedData, Object actualData, String propertyFilePath) {
		if (!expectedData.equals(actualData)) {
			String stableResp = convertToJsonwithObjectMapper(expectedData);
			System.out.println("expectedResp" + stableResp);
			String qaResp = convertToJsonwithObjectMapper(actualData);
			System.out.println("actualResp" + qaResp);
			boolean comparatorStatus = compareJson(stableResp, qaResp, propertyFilePath);
			Assert.assertTrue(comparatorStatus);
		}
	}

	public Channel publishmsg(String host, String username, String password) throws IOException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setUsername(username);
		factory.setPassword(password);
		com.rabbitmq.client.Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		return channel;
	}

	public void validateinvalidResponse(String errorMessage, String errorCode, String display,
			NegResponseDTO negResponseDTO) {

		if (!(negResponseDTO.getErrorCode().equals(errorCode) && negResponseDTO.getErrorMessage().equals(errorMessage))
				&& negResponseDTO.getHttpStatus().equals("400") && negResponseDTO.getDisplayMessage().equals(display)) {
			ReportHelper.logValidationFailure(
					"Code/Mesage", errorCode + " " + errorMessage + " " + display, negResponseDTO.getErrorCode() + " "
							+ negResponseDTO.getErrorMessage() + negResponseDTO.getDisplayMessage(),
					"Code/Mesage mismatch");
			Assert.assertTrue(false);

		}

	}

	public static String convertToJsonwithObjectMapper(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.findAndRegisterModules();

		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		try {
			String str = mapper.writeValueAsString(obj);

			return str;

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	public <T> T convertFromJsonwithObjectMapperwithclass(String jsonData, Class<T> classType) {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		try {

			return mapper.readValue(jsonData, classType);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public <T> T convertFromJson(String jsonData, TypeReference<T> type) {
		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		try {

			return mapper.readValue(jsonData, type);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public List<JSONObject> getdatafrommongowithObjectmapper(String serverInitials, Integer port,
			Map<String, String> map, String dataBaseName, String collectionName) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = getMongoClient(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeys(map);

			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);

			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}

	public List<JSONObject> getdatafrommongowithObjectmapperObject(String[] serverInitials, Integer port,
			Map<String, Object> map, String dataBaseName, String collectionName) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeysObject(map);

			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);

			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}

	public List<JSONObject> getdatafrommongowithObjectmapper(String[] serverInitials, Integer port,
			Map<String, String> map, String dataBaseName, String collectionName) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeys(map);

			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);

			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}

	
	public List<JSONObject> getSorteddatafrommongowithObjectmapper(String[] serverInitials, Integer port,
			Map<String, Object> map, String dataBaseName, String collectionName, String sortKey, int sortValue,int pageNo,
			int limit) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeysObject(map);
			System.out.println("basicdbobject"+ basicdbobject);
			List<Object> dataobj = this.getResultFromMongo(collection, basicdbobject, sortKey, sortValue, pageNo,limit);

			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}

	
	
	public List<JSONObject> getdatafrommongowithRegexObjectMapper(String[] serverInitials, Integer port,
			Map<String, String> map, String dataBaseName, String collectionName, String sortKey, int sortValue,int pageNo,
			int limit,String status) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeyswithRegex(map,status);
			List<Object> dataobj = this.getResultFromMongo(collection, basicdbobject, sortKey, sortValue, pageNo,limit);
		
			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}
	
	public List<JSONObject> getdatafrommongowithRegexandExistsFilterObjectMapper(String[] serverInitials, Integer port,
			Map<String, String> map, String dataBaseName, String collectionName, String sortKey, int sortValue,int pageNo,
			int limit,String status,String existsKey) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeyswithRegexAndExists(map,status,existsKey);
			List<Object> dataobj = this.getResultFromMongo(collection, basicdbobject, sortKey, sortValue, pageNo,limit);
		
			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}
	
	public List<JSONObject> getdatafrommongowithRegexObjectMapper(String[] serverInitials, Integer port,
			Map<String, String> map, String dataBaseName, String collectionName,String status,String existsKey) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);
			BasicDBObject basicdbobject = prepareMongoQueryFromMultipleKeyswithRegexAndExists(map,status,existsKey);
			
			System.out.println("query"+ basicdbobject);
		
			
			List<Object> dataobj = getResultFromMongo(collection, basicdbobject);
		
			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		return jsondata;

	}
	
	public List<Object> getResultFromMongo(MongoCollection mongoCollection, BasicDBObject searchQuery, String sortKey,
			int sortValue, int pageNo,int limit) {
		FindIterable findIterable;
		
		if( limit!=0)
		{
			
			 findIterable = mongoCollection.find(searchQuery).sort(new BasicDBObject(sortKey, sortValue)).skip(pageNo)
						.limit(limit);
		}
	
		else
		{
		 findIterable = mongoCollection.find(searchQuery).sort(new BasicDBObject(sortKey, sortValue));
		}
		MongoCursor mongoCursor = findIterable.iterator();
		ArrayList mongoAsObjectResult = new ArrayList();

		while (mongoCursor.hasNext()) {
			mongoAsObjectResult.add(mongoCursor.next());
		}
		return mongoAsObjectResult;
	}

	public List<JSONObject> parseMongoDocumentswithObjectMapper(List<Object> resultFromMongoAsObject) {

		List<JSONObject> jsonObjectListOfMongo = new ArrayList<>();
		Iterator<Object> mongoResultIterator = resultFromMongoAsObject.iterator();
		while (mongoResultIterator.hasNext()) {
			JSONObject jsonObject = new JSONObject(convertToJsonwithObjectMapper(mongoResultIterator.next()));
			jsonObjectListOfMongo.add(jsonObject);
		}

		return jsonObjectListOfMongo;
	}

	public Connection createDbConnection(DbRequestDto dbRequestDTO, DbType dbType) {
		Connection con = null;
		String dbServerIp = dbRequestDTO.getIp();
		String userName = dbRequestDTO.getUsername();
		String password = dbRequestDTO.getPassword();
		String port = dbRequestDTO.getPort();
		String sid = dbRequestDTO.getSid();

		try {
			String connectionString = null;
			if (dbType.name().equals("ORACLE")) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connectionString = "jdbc:oracle:thin:@" + dbServerIp + ":" + port + ":" + sid;
			} else if (dbType.name().equals("MYSQL")) {
				Class.forName("com.mysql.jdbc.Driver");
				connectionString = "jdbc:mysql://" + dbServerIp + ":" + port + "/" + sid;
			}

			con = DriverManager.getConnection(connectionString, userName, password);
		} catch (Exception e) {
			Reporter.log(e.getLocalizedMessage());
		}
		return con;
	}

	/**
	 * @param connection
	 * @param query
	 * @return int - This output will be number in int which Specifies number of
	 *         rows affected by The Query.The output will be -1 in case of any
	 *         Exception.The Output will be 1 in case of 1 row is affected.
	 */
	public int updateDeleteDataFromDb(Connection connection, String query, boolean commitRequired) {
		int result = 0;

		try {
			Statement stmt = connection.createStatement();
			result = stmt.executeUpdate(query);

			if (commitRequired) {
				connection.commit();
			}
		} catch (SQLException var4) {
			ReportHelper.logValidationFailure("Couldn't get db connection", "DB connection", var4.getMessage(),
					"DB connection failure");
			return -1;
		}

		return result;
	}

	private RequestSpecification prepareRequestParams(Map<String, String> queryParams, List<Header> headers,
			String body, ContentType contentType, Map<String, String> formParams,
			Map<String, List<File>> multipartFile) {
		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false);
		if (null != queryParams) {
			request.queryParams(queryParams);
		}

		if (null != formParams) {
			request.formParams(formParams);
		}

		if (null != multipartFile) {
			for (String key : multipartFile.keySet()) {
				List<File> fileList = multipartFile.get(key);
				for (File file : fileList) {
					request.multiPart(key, file);
				}

			}
		}

		if (headers != null)
			request.headers(new Headers(headers));
		if (body != null && !body.isEmpty()) {
			request.body(body);
		}
		if (contentType != null)
			request.contentType(contentType);

		return request;
	}

	/**
	 * Generic method to fetch response for different type of API request
	 *
	 * @param requestUrl
	 * @param requestType
	 * @param body
	 * @param queryParams
	 * @param headers
	 * @param statusCode
	 * @return
	 */
	public Response fetchApiResponse(String requestUrl, String requestType, String body,
			Map<String, String> queryParams, List<Header> headers, ContentType contentType, int statusCode,
			Map<String, String> formParams, Map<String, List<File>> multipartFile, Boolean disableDefaultCharset) {

		Response apiResponse = null;
		String requestLog = null;
		RequestSpecification apiRequest = prepareRequestParams(queryParams, headers, body, contentType, formParams,
				multipartFile);

		if (disableDefaultCharset != null && disableDefaultCharset)
			apiRequest.config(new RestAssuredConfig().encoderConfig(
					EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));

		requestLog = "Request body: " + body;

		if (queryParams != null)
			requestLog = requestLog + "Query Params:" + queryParams.toString();

		String headerData = null;
		if (headers != null) {
			headerData = "";
			for (Header h : headers) {
				headerData = headerData + h.getName() + " : " + h.getValue();
			}
		}

		switch (requestType) {
		case "GET":
			apiResponse = apiRequest.get(requestUrl);
			break;
		case "POST":
			apiResponse = apiRequest.post(requestUrl);
			break;
		case "PUT":
			apiResponse = apiRequest.put(requestUrl);
			break;
		case "PATCH":
			apiResponse = apiRequest.patch(requestUrl);
			break;
		case "DELETE":
			apiResponse = apiRequest.delete(requestUrl);
			break;
		default:
			apiResponse = apiRequest.head(requestUrl);
			break;
		}

		long responseTime = apiResponse.getTime();
		logRequestResponse(requestLog, formatResponse(apiResponse), requestUrl, responseTime, headerData);
		if (apiResponse.getStatusCode() != statusCode) {
			ReportHelper.logValidationFailure("HTTP Status code not " + Integer.toString(statusCode),
					Integer.toString(statusCode), Integer.toString(apiResponse.getStatusCode()),
					"HTTP status check failure");
			Assert.assertTrue(false);
		}
		return apiResponse;
	}

	private void logRequestResponse(String request, String response, String url, long responseTime, String headers) {
		String requestFilePath = "";
		if (request != null)
			requestFilePath = fileHelper.createRequestJsonFile(request, "");
		String responseFilePath = fileHelper.createResponseJsonFile(response, "");
		String reportFolder = reporter.getResultFileStringPath();
		reporter.appendresultHTMLReport(reportFolder, url, "<a href='" + requestFilePath + "'>Request Data" + "</a>",
				"<a href='" + responseFilePath + "'>Response Data" + "</a>",
				"Response Time(in msec) :- " + String.valueOf(responseTime), "", "", headers);
	}

	/**
	 * Format JSON and pretty print the response
	 *
	 * @param apiResponse
	 * @return
	 */
	private String formatResponse(Response apiResponse) {
		String responseString = null;

		if (apiResponse.contentType().contains(ContentType.JSON.toString()) && apiResponse.asString() != null) {
			com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
			JsonElement jsonElement = parser.parse(apiResponse.asString());
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (jsonElement instanceof JsonObject) {
				JsonObject json = jsonElement.getAsJsonObject();
				responseString = gson.toJson(json);
			} else if (jsonElement instanceof JsonArray) {
				JsonArray json = jsonElement.getAsJsonArray();
				responseString = gson.toJson(json);
			}
		} else
			responseString = apiResponse.asString();
		return responseString;
	}

	public boolean mappedResponseValidator(String expectedJson, String actualJson, String propertyFilePath,
			String testCaseName, boolean isDataTypeComparisonRequired) {
		Map<String, String> mapperProperties = new HashMap<>();
		Map<String, Map<String, Object>> resultMap;

		if (testCaseName != null) {
			Map<String, String> masterMappings = fetchAllProperties(propertyFilePath);
			for (Map.Entry<String, String> masterMapping : masterMappings.entrySet()) {
				if (masterMapping.getKey().startsWith(testCaseName)) {
					String[] responseMap = masterMapping.getValue().split("=");
					mapperProperties.put(responseMap[0], responseMap[1]);
				}
			}
		} else {
			mapperProperties = fetchAllProperties(propertyFilePath);
		}

		if (isDataTypeComparisonRequired) {
			resultMap = genericComparator.fieldMapper(expectedJson, actualJson, mapperProperties);

		} else {
			resultMap = genericComparator.fieldMapperString(expectedJson, actualJson, mapperProperties);
		}

		String expectedResp = convertToJson(resultMap.get(EXPECTED_RESULT));
		String actualResp = convertToJson(resultMap.get(ACTUAL_RESULT));

		return compareJson(expectedResp, actualResp, null, null, "move");
	}

	boolean compareJson(String responseExpected, String responseActual, String request, String propFilePath,
			String ignoreOperation) {
		ComparatorReportGenerator compareHtmlObj = new ComparatorReportGenerator();

		try {
			String compareFilePath = null;
			String linkCompareFilePath = null;
			if (request == null) {
				compareFilePath = compareHtmlObj.createCompartarReport();
			} else {
				compareFilePath = compareHtmlObj.createCompartorReport(request);
			}

			linkCompareFilePath = "./comparatorReport/" + compareFilePath.split("comparatorReport")[1];
			int differenceCounter = 0;
			ObjectMapper objMapper = new ObjectMapper();
			JsonNode responseExp = objMapper.readTree(responseExpected);
			JsonNode responseAct = objMapper.readTree(responseActual);
			JsonNode jsonDiff = JsonDiff.asJson(responseExp, responseAct);
			Set<String> dndCompareValues = null;
			if (propFilePath != null && !propFilePath.isEmpty()) {
				dndCompareValues = (new DataHelper()).getAllProperty(propFilePath);
			}

			for (int diffCount = 0; diffCount < jsonDiff.size(); ++diffCount) {
				String diffPath = jsonDiff.get(diffCount).get("path").asText();
				String operationType = jsonDiff.get(diffCount).get("op").asText();
				if ((dndCompareValues == null || !ignoreFieldOperation(dndCompareValues, diffPath))
						&& (ignoreOperation == null || !operationType.equalsIgnoreCase(ignoreOperation))) {
					++differenceCounter;
					JsonNode diffValueJsonNode = jsonDiff.get(diffCount).get("value");
					String diffValue = (String) Optional.ofNullable(diffValueJsonNode).map((dataValue) -> {
						return diffValueJsonNode.asText();
					}).orElse(null);
					String parentValue = (String) Optional.ofNullable(responseExp.at(diffPath)).map((value) -> {
						return responseExp.at(diffPath).asText();
					}).orElse(null);
					logFailureInReporter(compareFilePath, operationType.toUpperCase(), diffPath, diffValue,
							parentValue);
				}
			}

			if (differenceCounter > 0) {
				compareHtmlObj.appendFinalHTMLReport(compareFilePath);
				String differenceHTMLLink = "<a href='" + linkCompareFilePath + "'>Differences Link</a>";
				ReportHelper.reporterLogging(false, differenceHTMLLink);
				return false;
			}
		} catch (Exception var21) {
			ReportHelper.logValidationFailure("Exception in comparison", "", var21.getMessage(), "Exception");
		}
		return true;
	}

	private boolean ignoreFieldOperation(Set<String> ignoreProps, String field) {
		boolean ignoreField = false;
		for (String ignoreProperty : ignoreProps) {
			if (Pattern.matches(ignoreProperty, field)) {
				ignoreField = true;
				break;
			}
		}
		return ignoreField;
	}

	private void logFailureInReporter(String compareFilePath, String operationType, String differencePath,
			String differenceValue, String parentValue) {
		ComparatorReportGenerator compareObj = new ComparatorReportGenerator();

		compareObj.appendresultHTMLReport(compareFilePath, FAILURE_LOG_START + operationType + FAILURE_LOG_END,
				FAILURE_LOG_START + differencePath + FAILURE_LOG_END, FAILURE_LOG_START + parentValue + FAILURE_LOG_END,
				FAILURE_LOG_START + differenceValue + FAILURE_LOG_END);
	}

	public Map<String, Claim> getContentFromJwt(Algorithm defAlgo, String issuer, String token,
			int failureExponentialCounterInSec) {
		JWTVerifier verifier = JWT.require(defAlgo).withIssuer(issuer).build();
		DecodedJWT jwt = null;
		try {
			jwt = verifier.verify(token);
		} catch (Exception e) {
			System.out.println("going to sleep for :" + failureExponentialCounterInSec + " sec");
			System.out.println(e.getMessage());
			if (failureExponentialCounterInSec < 10) {
				try {
					Thread.sleep(failureExponentialCounterInSec * 1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				failureExponentialCounterInSec = failureExponentialCounterInSec * 2;
				return getContentFromJwt(defAlgo, issuer, token, failureExponentialCounterInSec);
			} else {
				ReportHelper.logValidationFailure("token", "valid token", "invalid token/expired", "token is expired");
				Assert.assertTrue(false);
			}
		}
		return jwt.getClaims();
	}

	public List<JSONObject> getdatafrommongowithObjectmapperWithoutQuery(String serverInitials, Integer port,
			String dataBaseName, String collectionName) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = getMongoClient(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);

			List<Object> dataobj = this.getResultFromMongoWithoutQuery(collection);

			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
		return jsondata;
	}

	public List<JSONObject> getdatafrommongowithObjectmapperWithoutQuery(String[] serverInitials, Integer port,
			String dataBaseName, String collectionName) {
		MongoClient mongoClient = null;
		List<JSONObject> jsondata = null;

		try {
			mongoClient = addMultiServer(serverInitials, port);
			MongoCollection collection = getCollection(mongoClient, dataBaseName, collectionName);

			List<Object> dataobj = this.getResultFromMongoWithoutQuery(collection);

			jsondata = parseMongoDocumentswithObjectMapper(dataobj);

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
		return jsondata;
	}

	public MongoClient addMultiServer(String[] serverInitials, Integer port) {

		MongoClient mongoClient = null;
		for (String server : serverInitials) {
			mongoClient = new MongoClient(new ServerAddress(server, port));
		}
		return mongoClient;
	}

	public List<Object> getResultFromMongoWithoutQuery(MongoCollection mongoCollection) {
		FindIterable findIterable = mongoCollection.find();
		MongoCursor mongoCursor = findIterable.iterator();
		ArrayList mongoAsObjectResult = new ArrayList();

		while (mongoCursor.hasNext()) {
			mongoAsObjectResult.add(mongoCursor.next());
		}
		return mongoAsObjectResult;
	}

	public static void produceKafkaEvent(String kafkaIp, int kafkaport, String topic, String key, Object value) {

		Properties props = new Properties();
		props.put("bootstrap.servers", kafkaIp + ":" + kafkaport);

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.findAndRegisterModules();

		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		// Producer<Object, Object> producer = new KafkaProducer<Object, Object>(props);

		Producer<String, Object> producer = new KafkaProducer<>(props, new StringSerializer(),
				new JsonSerializer(mapper));

		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(topic, key, value);
		try {
			System.out.println("record" + record);
			producer.send(record).get();
		}

		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		producer.close();
	}

	public int updateInsertDataIntoDb(Connection connection, String query, boolean commitRequired) {
		int result = 0;

		try {
			Statement stmt = connection.createStatement();
			result = stmt.executeUpdate(query);

			if (commitRequired) {
				connection.commit();
			}
		} catch (SQLException var4) {
			ReportHelper.logValidationFailure("Couldn't get db connection", "DB connection", var4.getMessage(),
					"DB connection failure");
			return -1;
		}
		return result;
	}

	public static void produceKafkaEventasString(String kafkaIp, int kafkaport, String topic, String key,
			String value) {

		Properties props = new Properties();
		props.put("bootstrap.servers", kafkaIp + ":" + kafkaport);

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

		props.put(ProducerConfig.ACKS_CONFIG, "all");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);
		try {
			System.out.println("record" + record);
			producer.send(record).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		producer.close();
	}
	
	public  boolean keyexists(String response, List<String> keys)
	{
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(response).getAsJsonObject();
		
		for(String key:keys) {
			return jsonObject.has(key);
		}
		return false;
		

	}
}
	
	
