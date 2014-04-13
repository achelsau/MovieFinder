define(['text!../pages/persistent_search.html',
        'jquery',
        'commonData',
        'result'],
		function (persistentSearch, $, commonData, result) {
	
	var currentQueryTokens = null;
	var currentSelectQuery = null;
	var currentSelectedQueryId = null;
	
	function attachBody() {
		$("#content").remove();
		
		$("#body").append(persistentSearch);
		
		$("#results_list").empty();
		
		populateWithPersistedQueries();
		
		bindEventHandlers();
	}
	
	function populateWithPersistedQueries() {
		$.ajax({
			type : "GET",
			url: commonData.getConstants().basePath + "query/getAllQueries/" + commonData.getUserData().id,
			headers: { 'Authorization': commonData.getUserData().username + ":" + commonData.getUserData().password },
			contentType : "text/plain",
		}).done(handlePopulationWithPersistentQueries);
	}
	
	function handlePopulationWithPersistentQueries(response) {
		console.log(response);
		
		var userData = commonData.getUserData();
		var tokensMap = [];
		for (var i = 0; i < response.length; i++) {
			$("#persistentQueries").append("<option value='" + response[i].id + "'>" + response[i].queryString + "</option>");
			
			tokensMap[response[i].id] = response[i].tokens;
		}
		
		userData.tokensMap = tokensMap;
		
		setCurrentlySelectedQuery();
	}
	
	function setCurrentlySelectedQuery() {
		currentSelectQuery = $("#persistentQueries option:selected").text();
		currentSelectedQueryId = $("#persistentQueries option:selected").val();
	}
	
	function bindEventHandlers() {
		$("#searchButton").click(function(e) {
			$.ajax({
			  type : "POST",
			  url: commonData.getConstants().basePath + "query/searchPersistentQuery/",
			  headers: { 'Authorization': commonData.getUserData().username + ":" + commonData.getUserData().password },
			  contentType : "json/application",
			  data: JSON.stringify({id : currentSelectedQueryId , queryString: currentSelectQuery, tokens : commonData.getUserData().tokensMap[currentSelectedQueryId]})
			}).done( handleSearchResponse );
		});
		
		$("#persistentQueries").change(function (e) {
			setCurrentlySelectedQuery();
		});
	}
	
	function handleSearchResponse(response) {
		$("#results_list").empty();
		
		var resultsArray = response.results;
		var currentResult = null;
		for (var i = 0; i < resultsArray.length; i++) {
			currentResult = new result.Result(commonData.getConstants().PERSISTENT_SEARCH);
			currentResult.attachResult("#results_list", resultsArray[i], response.queryId);
		}
	}
	
	return {
		attachBody : attachBody,
		bindEventHandlers : bindEventHandlers
	};
	
});