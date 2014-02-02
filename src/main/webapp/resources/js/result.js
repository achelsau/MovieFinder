define(['text!../pages/result.html', 'jquery'], function (htmlPage, $) {
	
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
	}
	
	return {
		attachResult : attachResult
	};
	
});