define(['quickSearchPage', 
        'persistentSearchPage', 
        'jquery', 
        'commonData'], 
        function (quickSearchPage, persistentSearch, $, commonData) {
	
	function attachBody() {
		// mock call
		commonData.setUserData({id:'19', username : 'Ion', password : 'abcd'});
		
		bindEventHandlers();
		
		quickSearchPage.attachBody();
	}
	
	function bindEventHandlers() {
		
		$("#persistentSearchButton").click(function(e) {
			persistentSearch.attachBody();
		});
		
		$("#quickSearchButton").click(function(e) {
			quickSearchPage.attachBody();
		});
	}
	
	attachBody();
	
	return {
		attachBody : attachBody
	};
	
});