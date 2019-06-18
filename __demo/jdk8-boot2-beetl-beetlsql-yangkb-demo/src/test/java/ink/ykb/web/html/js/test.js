(function(){
	 	var aeskey = "QS0xNi1CeXRlLVdhZXJ0cg==";
		 var aeskeyBase64 = CryptoJS.enc.Base64.parse(aeskey).toString(CryptoJS.enc.Utf8);
		 var sign = {};
		 sign.c1 = new Date().getTime();
		 sign.s = aeskeyBase64;
		 sign.c = new Date().getTime();
		 var signStr = JSON.stringify(sign);
		 var key = CryptoJS.enc.Utf8.parse(aeskeyBase64+aeskeyBase64); 
		 var iv = CryptoJS.enc.Utf8.parse(aeskeyBase64);
		 var encrypted = '';
		 var srcs = CryptoJS.enc.Utf8.parse(signStr);
		  var result = CryptoJS.AES.encrypt(srcs, key, {
		      iv: iv,
		      mode: CryptoJS.mode.CBC,
		      padding: CryptoJS.pad.Pkcs7
		  }).toString();

		  var dom =document.createElement("input");
		  dom.id="wbtoken";
		  dom.type="hidden";
		  dom.value=result;
		  document.body.appendChild(dom);
}())