define(['text!../pages/quick_search.html',
        'jquery',
        'commonData',
        'result'],
		function (quickSearch, $, commonData, result) {
	
	function attachBody() {
		$("#content").remove();
		
		$("#body").append(quickSearch);
		
		$("#results_list").empty();
		
		bindEventHandlers();
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
		
		$("#saveButton").click(function(e) {
			$.ajax({
				type: "POST",
				url: commonData.getConstants().basePath + "query/storePersistentQuery/",
				headers: {'Authorization': commonData.getUserData().username + ":" + commonData.getUserData().password },
				contentType: 'json/application',
				data: JSON.stringify({queryString : $("#query_string").val()})
			}).done( handleSaveQueryResponse );
		});
	}
	
	function handleSearchResponse(response) {
		$("#results_list").empty();
		
		var resultsArray = response.results;
		var currentResult = null;
		for (var i = 0; i < resultsArray.length; i++) {
			currentResult = new result.Result();
			currentResult.attachResult("#results_list", resultsArray[i], response.queryId);
		}
	}
	
	function handleSaveQueryResponse(response) {
		
	}
	
	return {
		attachBody : attachBody,
		bindEventHandlers : bindEventHandlers
	};
	
});