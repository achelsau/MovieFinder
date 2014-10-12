define(function() {
	
	var userData = undefined;
	var protocolAndHost = getProtocolAndHost();
	
	function getConstants() {
		return { 
			basePath : protocolAndHost + '/MovieFinderServer/mf/',
			QUICK_SEARCH : 'quickSearch',
			PERSISTENT_SEARCH : 'persistentSearch'
		};
	}
	
	function getProtocolAndHost() {
		pathArray = window.location.href.split( '/' );
		protocol = pathArray[0];
		host = pathArray[2];
		url = protocol + '//' + host;
		
		return url;
	}
	
	function getUserData() {
		return userData;
	}
	
	function setUserData(usrData) {
		userData = usrData;
	}
	
	return {
		getConstants : getConstants,
		getUserData : getUserData,
		setUserData : setUserData
	};
});