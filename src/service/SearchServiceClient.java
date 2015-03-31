package service;

import static service.SearchServiceHelper.isSuccessResponse;
import static service.SearchServiceHelper.logMessage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

/**
 * This class is responsible for implementing HTTP operations for creating Index, creating indexer, creating indexer datasource, ...  
 * 
 */
public class SearchServiceClient
{
	private final String _apiKey;
	private final Properties _properties;

	public SearchServiceClient(Properties properties)
	{
		_apiKey = properties.getProperty("SearchServiceApiKey");
		_properties = properties;
	}

	public boolean createIndex() throws IOException
	{
		logMessage("\n Creating Index...");

		URL url = SearchServiceHelper.getCreateIndexURL(_properties);

		HttpsURLConnection connection = SearchServiceHelper.getHttpURLConnection(url, "PUT", _apiKey);
		connection.setDoOutput(true);

		// Index definition
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
		outputStreamWriter.append("{\"fields\":[");
		outputStreamWriter
				.append("{\"name\": \"FEATURE_ID\"	, \"type\": \"Edm.String\"			, \"key\": true	, \"searchable\": false, 	\"filterable\": false, \"sortable\": false, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"FEATURE_NAME\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"FEATURE_CLASS\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"STATE_ALPHA\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"STATE_NUMERIC\"	, \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"COUNTY_NAME\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"COUNTY_NUMERIC\", \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"LOCATION\"		, \"type\": \"Edm.GeographyPoint\"	, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"ELEV_IN_M\"		, \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"ELEV_IN_FT\"	, \"type\": \"Edm.Int32\"			, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"MAP_NAME\"		, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"DESCRIPTION\"	, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": false, \"sortable\": false, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"HISTORY\"		, \"type\": \"Edm.String\"			, \"key\": false, \"searchable\": true, 	\"filterable\": false, \"sortable\": false, 	\"facetable\": false, \"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"DATE_CREATED\"	, \"type\": \"Edm.DateTimeOffset\"	, \"key\": false, \"searchable\": false, 	\"filterable\": true,  \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true},");
		outputStreamWriter
				.append("{\"name\": \"DATE_EDITED\"	, \"type\": \"Edm.DateTimeOffset\"	, \"key\": false, \"searchable\": false, 	\"filterable\": true, \"sortable\": true, 	\"facetable\": true, 	\"retrievable\": true}");
		outputStreamWriter.append("]}");
		outputStreamWriter.close();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		return isSuccessResponse(connection);
	}

	public boolean createDatasource() throws IOException
	{
		logMessage("\n Creating Indexer Data Source...");

		URL url = SearchServiceHelper.getCreateIndexerDatasourceURL(_properties);
		HttpsURLConnection connection = SearchServiceHelper.getHttpURLConnection(url, "PUT", _apiKey);
		connection.setDoOutput(true);

		String dataSourceRequestBody = "{ 'description' : 'USGS Dataset','type' : '" + _properties.getProperty("DataSourceType") + "','credentials' : " + _properties.getProperty("DataSourceConnectionString") + ",'container' : { 'name' : '" + _properties.getProperty("DataSourceTable") + "' }} ";
		
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
		outputStreamWriter.write(dataSourceRequestBody);
		outputStreamWriter.close();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		return isSuccessResponse(connection);
	}

	public boolean createIndexer() throws IOException
	{
		logMessage("\n Creating Indexer...");

		URL url = SearchServiceHelper.getCreateIndexerURL(_properties);
		HttpsURLConnection connection = SearchServiceHelper.getHttpURLConnection(url, "PUT", _apiKey);
		connection.setDoOutput(true);
		
		String indexerRequestBody = "{ 'description' : 'USGS data indexer', 'dataSourceName' : '" + _properties.get("DataSourceName")
				+ "', 'targetIndexName' : '" + _properties.get("IndexName") + "' ,'parameters' : { 'maxFailedItems' : 10, 'maxFailedItemsPerBatch' : 5, 'base64EncodeKeys': false }}";

		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
		outputStreamWriter.write(indexerRequestBody);
		outputStreamWriter.close();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		return isSuccessResponse(connection);
	}

	public boolean syncIndexerData() throws IOException, InterruptedException
	{
		logMessage("\n Syncing data...");

		// Run indexer
		URL url = SearchServiceHelper.getRunIndexerURL(_properties);
		HttpsURLConnection connection = SearchServiceHelper.getHttpURLConnection(url, "POST", _apiKey);
		connection.setRequestProperty("Content-Length", "0");
		connection.setDoOutput(true);
		connection.getOutputStream().flush();

		System.out.println(connection.getResponseMessage());
		System.out.println(connection.getResponseCode());

		if (!isSuccessResponse(connection))
		{
			return false;
		}

		// Check indexer status
		logMessage("Synchronization running...");

		boolean running = true;
		URL statusURL = SearchServiceHelper.getIndexerStatusURL(_properties);
		connection = SearchServiceHelper.getHttpURLConnection(statusURL, "GET", _apiKey);

		while (running)
		{
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				return false;
			}

			JsonReader jsonReader = Json.createReader(connection.getInputStream());
			JsonObject responseJson = jsonReader.readObject();

			if (responseJson != null)
			{
				JsonObject lastResultObject = responseJson.getJsonObject("lastResult");

				if (lastResultObject != null)
				{
					String inderxerStatus = lastResultObject.getString("status");

					if (inderxerStatus.equalsIgnoreCase("inProgress"))
					{
						logMessage("Synchronization running...");
						Thread.sleep(1000);

					} else
					{
						running = false;
						logMessage("Synchronized " + lastResultObject.getInt("itemsProcessed") + " rows...");
					}
				}
			}
		}

		return true;
	}
}
