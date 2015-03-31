package model;

public class Document
{
	private String _featureName = "";
	private String _featureClass = "";
	private String _stateAlpha = "";
	private String _countyName = "";
	private String _longitude = "";
	private String _latitude = "";
	private String _mapName = "";
	private Integer _elevationMeter = 0;
	private Integer _elevationFt = 0;
	private String _description = "";
	private String _history = "";
	private String _dateCreated = "";
	private String _dateEdited = "";

	public String getFeatureName()
	{
		return _featureName;
	}

	public void setFeatureName(String featureName)
	{
		_featureName = featureName;
	}

	public String getFeatureClass()
	{
		return _featureClass;
	}

	public void setFeatureClass(String featureClass)
	{
		_featureClass = featureClass;
	}

	public String getStateAlpha()
	{
		return _stateAlpha;
	}

	public void setStateAlpha(String stateAlpha)
	{
		_stateAlpha = stateAlpha;
	}

	public String getCountyName()
	{
		return _countyName;
	}

	public void setCountyName(String countyName)
	{
		_countyName = countyName;
	}

	public String getLongitude()
	{
		return _longitude;
	}

	public void setLongitude(String longitude)
	{
		_longitude = longitude;
	}

	public String getLatitude()
	{
		return _latitude;
	}

	public void setLatitude(String latitude)
	{
		_latitude = latitude;
	}

	public Integer getElevationMeter()
	{
		return _elevationMeter;
	}

	public void setElevationMeter(Integer elevationMeter)
	{
		_elevationMeter = elevationMeter;
	}

	public Integer getElevationFt()
	{
		return _elevationFt;
	}

	public void setElevationFt(Integer elevationFt)
	{
		_elevationFt = elevationFt;
	}

	public String getMapName()
	{
		return _mapName;
	}

	public void setMapName(String mapName)
	{
		_mapName = mapName;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}

	public String getHistory()
	{
		return _history;
	}

	public void setHistory(String history)
	{
		_history = history;
	}

	public String getDateCreated()
	{
		return _dateCreated;
	}

	public void setDateCreated(String dateCreated)
	{
		_dateCreated = dateCreated;
	}

	public String getDateEdited()
	{
		return _dateEdited;
	}

	public void setDateEdited(String dateEdited)
	{
		_dateEdited = dateEdited;
	}
}
