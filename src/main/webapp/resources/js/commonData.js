define(function() {
	
	var userData = undefined;
	
	function getConstants() {
		return { 
			basePath : 'https://localhost:8443/MovieFinderServer_git/mf/',
			QUICK_SEARCH : 'quickSearch',
			PERSISTENT_SEARCH : 'persistentSearch'
		};
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