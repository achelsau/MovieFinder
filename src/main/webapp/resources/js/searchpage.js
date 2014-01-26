define(['text!../pages/home.html', 'jquery', 'result'], function (htmlPage, $, result) {
	
	function attachBody() {
		$("#body").append(htmlPage);
		for (var i = 0; i < 15; i++) {
			result.attachResult("#results_list");
		}
	}
	
	attachBody();
	
	return {
		attachBody : attachBody
	};
	
});