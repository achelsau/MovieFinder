define(['text!../pages/persistent_search.html',
        'jquery',
        'commonData',
        'result'],
		function (persistentSearch, $, commonData, result) {
	
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
		
		for (var i = 0; i < response.length; i++) {
			$("#persistentQueries").append("<option value='" + response[i].id + "'>" + response[i].queryString + "</option>");
		}
	}
	
	function bindEventHandlers() {
		$("#searchButton").click(function(e) {
			$.ajax({
			  type : "POST",
			  url: commonData.getConstants().basePath + "query/quickQuery/",
			  headers: { 'Authorization': commonData.getUserData().username + ":" + commonData.getUserData().password },
			  contentType : "text/plain",
			  data : $("#query_string").val()
			}).done( handleSearchResponse );
		});
	}
	
	function handleSearchResponse(response) {
		$("#results_list").empty();
		
		var resultsArray = response.results;
		for (var i = 0; i < resultsArray.length; i++) {
			result.attachResult("#results_list", resultsArray[i]);
		}
	}
	
	return {
		attachBody : attachBody,
		bindEventHandlers : bindEventHandlers
	};
	
});