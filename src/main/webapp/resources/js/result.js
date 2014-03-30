define(['text!../pages/result.html', 
        'jquery', 
        'commonData'], 
        function (htmlPage, $, commonData) {
	
	function attachResult(parent, data) {
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
		
		bindEventHandlers(result);
	}
	
	function bindEventHandlers(resultPage) {
		resultPage.find(".like_btn").click(function(e) {
			$.ajax({
			  type : "GET",
			  url: commonData.getConstants().basePath + "relevanceFeedback/markIt/" + resultPage.find(".movie_id").val(),
			  headers: { 'Authorization': commonData.getUserData().username + ":" + commonData.getUserData().password },
			  contentType : "text/plain",
			  data : $("#query_string").val()
			}).done( handleLikeResponse );
		});
	}
	
	function handleLikeResponse(response) {
		
	}
	
	return {
		attachResult : attachResult
	};
	
});