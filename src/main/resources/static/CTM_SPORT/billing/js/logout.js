


    //µù¥Utoken
    function registerToken(token,userId){
    	    
    	    $.ajax({
			     url: 'http://210.71.197.149:8081/WowzaGetKeyMoudle/SetToken',
			     type: 'POST',
			     data: JSON.stringify({"Token":token,"UserID":userId}),
			     async: false,
			     dataType: "json",
			     contentType: "application/json",
			     error: function(xhr) {},
			     success: function(response) {
			     	  
			     }
			  });
    	
    }