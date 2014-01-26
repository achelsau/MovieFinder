define(['text!../pages/result.html', 'jquery'], function (htmlPage, $) {
	
	function attachResult(parent) {
		$(parent).append(htmlPage);
	}
	
	return {
		attachResult : attachResult
	};
	
});