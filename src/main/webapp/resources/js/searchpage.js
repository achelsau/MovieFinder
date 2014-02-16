define(['text!../pages/quick_search.html', 'text!../pages/persistent_search.html', 'jquery', 'result', 'constants'], function (quickSearch, persistentSearch, $, result, constants) {
	
	function attachBody() {
		$("#body").append(quickSearch);
		
		$("#results_list").empty();
		
		bindEventHandlers();
	}
	
	function bindEventHandlers() {
		$("#searchButton").click(function(e) {
			$.ajax({
			  type : "POST",
			  url: constants.basePath + "query/quickQuery/",
			  headers: { 'Authorization': 'Ion:abcd' },
			  contentType : "text/plain",
			  data : $("#query_string").val()
			}).done( handleSearchResponse );
		});
		
		$("#saveButton").click(function(e) {
			$.ajax({
				type: "POST",
				url: constants.basePath + "query/storePersistentQuery/",
				headers: {'Authorization': 'Ion:abcd'},
				contentType: 'json/application',
				data: JSON.stringify({queryString : $("#query_string").val()})
			});
		}).done( handleSaveQueryResponse );
		
		$("#persistentSearchButton").click(function(e) {
			$("#content").remove();
			$("#body").append(persistentSearch);
		});
		
		$("#quickSearchButton").click(function(e) {
			$("#content").remove();
			$("#body").append(quickSearch);
		});
	}
	
	function handleSearchResponse(response) {
		$("#results_list").empty();
		
		var resultsArray = response.results;
		for (var i = 0; i < resultsArray.length; i++) {
			result.attachResult("#results_list", resultsArray[i]);
		}
	}
	
	function handleSaveQueryResponse(response) {
		
	}
	
	attachBody();
	
	return {
		attachBody : attachBody
	};
	
});