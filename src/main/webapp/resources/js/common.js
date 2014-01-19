// Configure loading modules from the lib directory,
// except for 'app' ones, which are in a sibling
// directory.
require.config({
    baseUrl: 'resources/js/',
    paths : {
    	jquery: 'lib/jquery'
    }
});