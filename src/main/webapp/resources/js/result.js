define(['text!../pages/result.html', 
        'jquery', 
        'commonData'], 
        function (htmlPage, $, commonData) {
	
	function Result() {
	
		var currentQueryId = null;
		var currentMovieId = null;
		
		function attachResult(parent, data, queryId) {
			var result = $(htmlPage);
			
			$(parent).append(result);	
			
			var title = data.title;
			if (title.length > 84) {
				title = data.title.substring(0, 84) + "...";
			}
			result.find(".title").text(title);
			
			var description = data.description;
			if (description.length > 502) {
				description = description.substring(0, 502) + "...";
			}
			
			result.find(".description").text(description);
			result.find(".movieImage").attr("src", data.remotePicture);
			result.find(".movie_id").val(data.id);
			currentMovieId = data.id;
			
			currentQueryId = queryId;
			
			bindEventHandlers(result);
		}
		
		function bindEventHandlers(resultPage) {
			resultPage.find(".like_btn").click(function(e) {
				$.ajax({
				  type : "POST",
				  url: commonData.getConstants().basePath + "relevanceFeedback/markIt/",
				  headers: { 'Authorization': commonData.getUserData().username + ":" + commonData.getUserData().password },
				  contentType : "json/application",
				  data : JSON.stringify({persistentQueryId : currentQueryId, relevantMovieId: currentMovieId})
				}).done( handleLikeResponse );
			});
		}
		
		function handleLikeResponse(response) {
			alert("Like Given");
	
			var userData = commonData.getUserData();
			userData.tokensMap[currentQueryId] = response;
		}
		
		return {
			attachResult : attachResult
		};
		
	}
	
	return {
		Result : Result
	};
	
});