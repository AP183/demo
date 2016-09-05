app.factory("userPersistenceService", [
	"$cookies", function($cookies) {
		
		
		var obj = {};
		obj.authentication = {};
		obj.authentication.isAuth = false;
		obj.authentication.email = "";
		obj.authentication.role = "";
		//obj.authentication.mycart = "";
		
		
		if ($cookies.getObject("authentication"))
		{
			
			var oo = JSON.parse($cookies.get("authentication"));
			//var oo = JSON.parse(o);
			obj.authentication.isAuth = oo.isAuth;
			obj.authentication.email = oo.email;
			obj.authentication.role = oo.role;
			//obj.authentication.mycart = oo.mycart;
			
		} 
		
		obj.setCookieData = function(user) {
			obj.authentication.isAuth = true;
			obj.authentication.email = user.email;
			obj.authentication.role = user.role;
			//obj.authentication.mycart = [];
			$cookies.putObject("authentication", obj.authentication);
		}
		
		obj.getCookieData = function() {
			obj.authentication = $cookies.getObject("authentication");
			return obj.authentication;
		}
		
		obj.clearCookieData = function() {
			obj.authentication = {};
			obj.authentication.isAuth = false;
			$cookies.remove("authentication");
		}
		
		return obj;
	}
]);