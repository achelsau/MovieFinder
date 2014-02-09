define(['text!../pages/home.html', 'jquery', 'result', 'constants'], function (htmlPage, $, result, constants) {
	
	function attachBody() {
		$("#body").append(htmlPage);
		
		$("#results_list").empty();
		
		$("#searchButton").click(function(e) {
			$.ajax({
			  type : "POST",
			  url: constants.basePath + "query/quickQuery/",
			  headers: { 'Authorization': 'Ion:abcd' },
			  contentType : "text/plain",
			  data : $("#query_string").val()
			}).done( handleResponse );
		});
	}
	
	function handleResponse(response) {
		$("#results_list").empty();
		
		var resultsArray = response.results;
		for (var i = 0; i < resultsArray.length; i++) {
			result.attachResult("#results_list", resultsArray[i]);
		}
	}
	
	attachBody();
	
	return {
		attachBody : attachBody
	};
	
});