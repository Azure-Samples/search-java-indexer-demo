package servlets;

import static service.SearchServiceHelper.isSuccessResponse;
import static service.SearchServiceHelper.logMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Document;
import service.SearchServiceClient;
import service.SearchServiceHelper;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Properties _properties;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		 _properties = loadConfigurations(); 
		
		SearchServiceClient searchServiceClient = new SearchServiceClient(_properties);

		try
		{
			// Create an index "features" in the given service
			if (!searchServiceClient.createIndex())
			{
				logMessage("Failed while creating index...");
				return;
			}

			// Create indexer datasource "usgs-datasource"
			if (!searchServiceClient.createDatasource())
			{
				logMessage("Failed while creating indexer datasource...");
				return;
			}

			// Create an indexer using the above datasource and targeting the
			// above index "features"
			if (!searchServiceClient.createIndexer())
			{
				logMessage("Failed while creating indexer...");
				return;
			}

			// Run the indexer and wait until it returns
			if (!searchServiceClient.syncIndexerData())
			{
				logMessage("Failed while running indexer...");
				return;
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String searchString = request.getParameter("SearchQuery");
		JsonArray jsonResult = doSearch(searchString);
		List<Document> docList = jsonToDocument(jsonResult);

		request.setAttribute("DocList", docList);
		request.getRequestDispatcher("Search.jsp").forward(request, response);
	}

	private JsonArray doSearch(String searchString)
	{
		if (searchString == null || searchString.isEmpty())
		{
			searchString = "*";
		}

		try
		{
			URL url = SearchServiceHelper.getSearchURL(_properties, URLEncoder.encode(searchString, java.nio.charset.StandardCharsets.UTF_8.toString()));
			HttpsURLConnection connection = SearchServiceHelper.getHttpURLConnection(url, "GET", _properties.getProperty("SearchServiceApiKey"));

			JsonReader jsonReader = Json.createReader(connection.getInputStream());
			JsonObject jsonObject = jsonReader.readObject();
			JsonArray jsonArray = jsonObject.getJsonArray("value");
			jsonReader.close();

			System.out.println(connection.getResponseMessage());
			System.out.println(connection.getResponseCode());

			if (isSuccessResponse(connection))
			{
				return jsonArray;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private List<Document> jsonToDocument(JsonArray jsonArray)
	{
		List<Document> result = new ArrayList<Document>();

		if (jsonArray == null)
		{
			return result;
		}

		for (JsonValue jsonDoc : jsonArray)
		{
			JsonObject object = (JsonObject) jsonDoc;

			Document document = new Document();
			document.setFeatureName(object.getString("FEATURE_NAME"));
			document.setFeatureClass(object.getString("FEATURE_CLASS"));
			document.setStateAlpha(object.getString("STATE_ALPHA"));
			document.setCountyName(object.getString("COUNTY_NAME"));
			document.setElevationMeter(object.getInt("ELEV_IN_M"));
			document.setElevationFt(object.getInt("ELEV_IN_FT"));
			document.setMapName(object.getString("MAP_NAME"));
			document.setDescription(object.getString("DESCRIPTION"));
			document.setHistory(object.getString("HISTORY"));
			JsonValue dateValue = object.get("DATE_CREATED");
			document.setDateCreated(dateValue == JsonValue.NULL ? " " : dateValue.toString());
			dateValue = object.get("DATE_EDITED");
			document.setDateEdited(dateValue == JsonValue.NULL ? " " : dateValue.toString());

			JsonValue location = object.get("LOCATION");

			if (location != JsonValue.NULL)
			{				
				JsonArray coordinatesArray = ((JsonObject)location).getJsonArray("coordinates");

				if (coordinatesArray != null && coordinatesArray.size() == 2)
				{
					JsonValue latitudeValue = coordinatesArray.get(0);
					JsonValue longitudeValue = coordinatesArray.get(1);

					String latitude = latitudeValue == null ? "" : latitudeValue.toString();
					String longitude = longitudeValue == null ? "" : longitudeValue.toString();

					document.setLatitude(latitude);
					document.setLongitude(longitude);
				}
			}

			result.add(document);
		}

		return result;
	}	

	private Properties loadConfigurations()
	{
		Properties properties = new Properties();
		
		try
		{
			properties.load(new FileInputStream("config.properties"));			
		} catch (IOException e)
		{			
			e.printStackTrace();
		}
		
		return properties;
	}
}
