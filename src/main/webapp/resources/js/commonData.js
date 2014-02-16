define(function() {
	
	var userData = undefined;
	
	function getConstants() {
		return { 
			basePath : 'https://localhost:8443/MovieFinderServer/mf/'
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