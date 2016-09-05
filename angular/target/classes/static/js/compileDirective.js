app.directive('compileTemplate', function($compile, $parse){
    return {
        link: function(scope, element, attr){
            var parsed = $parse(attr.ngBindHtml);
            function getStringValue() { return (parsed(scope) || '').toString(); }

            //Recompile if the template changes
            scope.$watch(getStringValue, function() {
                $compile(element, null, -9999)(scope);  //The -9999 makes it skip directives so that we do not recompile ourselves
            });
        }         
    }
});

app.directive('myTemplate', function($compile) {
	  
	  function linker($scope, $element) {
	    $scope.model = {
	      data: 'Snoopy'
	    };
	    
	    var string = 'Model: <pre>{{ model.data | json }}</pre>';
	    if ($scope.html) {
	      $element.empty().append($compile($scope.html + string)($scope));
	    }
	  }
	  
	  return {
	    link: linker,
	    template: '<div></div>',
	    scope: {
	      html: '='
	    }
	  };
});