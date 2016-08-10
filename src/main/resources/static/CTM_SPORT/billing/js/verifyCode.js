        
        function createCode() {
            code = "";
            var codeLength = 3; //驗證碼的長度
            var checkCode = document.getElementById("checkCode");
            //所有候選組成驗證碼的字符，當然也可以用中文的
          //  var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
          //  'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
          //  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'); 
          var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            for (var i = 0; i < codeLength; i++) 
            {
            	  //var charNum = Math.floor(Math.random() * 52);
                var charNum = Math.floor(Math.random() * 10);
                code += codeChars[charNum];
            }
            if (checkCode) 
            {
                checkCode.className = "code";
                checkCode.innerHTML = code;
            }
        }
        
        
        function validateCode() 
        {
            var inputCode = document.getElementById("inputCode").value;
            if (inputCode.length <= 0) {
            		resultCode="none";
                showHint("Please enter verification code");
            }else if (inputCode.toUpperCase() != code.toUpperCase()) {
            	resultCode="error";
                showHint("The verification code you have entered is in err.");
                createCode();
            }else {
                resultCode="ok";
            }        
        }    