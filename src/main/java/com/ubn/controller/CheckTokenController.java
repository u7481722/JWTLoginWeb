package com.ubn.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubn.dto.PayloadDTO;
import com.ubn.dto.StatusDTO;
import com.ubn.dto.TokenDTO;

@CrossOrigin(maxAge = 3600)
@RestController
public class CheckTokenController {
	private static final Logger logger = Logger.getLogger(CheckTokenController.class);
	private String clientSecret = "test123";

	@RequestMapping(value = { "/setSecretKey" }, method = { RequestMethod.GET })
	public @ResponseBody String setSecretKey() {
		this.clientSecret = "test456";
		return this.clientSecret;
	}

	@RequestMapping(value = { "/genToken" }, method = { RequestMethod.POST })
	public @ResponseBody StatusDTO genToken(@RequestBody PayloadDTO payloadDTO) {
		StatusDTO statusDTO = new StatusDTO();
		try {
			long now = System.currentTimeMillis() / 1000L;
			logger.debug(this.clientSecret);
			JWTSigner singer = new JWTSigner(this.clientSecret);

			HashMap<String, Object> claims = new HashMap<String, Object>();
			claims.put("iss", payloadDTO.getIss());
			claims.put("lat", Long.valueOf(now));
			//claims.put("exp", Long.valueOf(now + 60*60*24)); // token過期時間1天(以秒來記)
			claims.put("company", payloadDTO.getCompany());
			claims.put("awesome", Boolean.valueOf(payloadDTO.isAwesome()));
			claims.put("account", payloadDTO.getAccount());
			claims.put("deviceId", payloadDTO.getDeviceId());
			claims.put("userId", payloadDTO.getUserId());

			String token = singer.sign(claims);

			statusDTO.setReturnCode(0);
			statusDTO.setReturnMessage("success");
			statusDTO.setReturnToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			statusDTO.setReturnCode(-1);
			statusDTO.setReturnMessage("fail");
		}
		return statusDTO;
	}

	@RequestMapping(value = { "/checkToken" }, method = { RequestMethod.POST })
	@ResponseBody
	public StatusDTO checkToken(@RequestBody TokenDTO tokenDTO) throws IOException, InvalidKeyException,
			JWTVerifyException, NoSuchAlgorithmException, SignatureException, IllegalStateException {

		StatusDTO statusDTO = new StatusDTO();

		String token = tokenDTO.getToken();

		logger.debug(token);
		if ("".equals(token)) {
			statusDTO.setReturnCode(1);
			statusDTO.setReturnMessage("Empty token!");
		} else {
			try {
				String[] pieces = token.split("\\.");
				if (pieces.length == 3) {
					byte[] byteArray = Base64.decodeBase64(pieces[1]);
					String payload_json = new String(byteArray);

					System.out.println(payload_json);

					HashMap<String, Object> map = new HashMap<String, Object>();
					ObjectMapper mapper = new ObjectMapper();
					map = (HashMap) mapper.readValue(payload_json, new TypeReference<Map<String, String>>() {
					});
					System.out.println(map.get("company"));
				}
				Map<String, Object> decodedPayload = new JWTVerifier(this.clientSecret, "audience").verify(token);

				statusDTO.setReturnCode(0);
				statusDTO.setReturnMessage("success");
			} catch (NoSuchAlgorithmException ex) {
				statusDTO.setReturnCode(-1);
				statusDTO.setReturnMessage("fail:" + ex);
			} catch (InvalidKeyException ex) {
				statusDTO.setReturnCode(-1);
				statusDTO.setReturnMessage("fail:" + ex);
			} catch (IOException ex) {
				statusDTO.setReturnCode(-1);
				statusDTO.setReturnMessage("fail:" + ex);
			} catch (JWTVerifyException ex) {
				statusDTO.setReturnCode(-1);
				statusDTO.setReturnMessage("fail:" + ex);
			} catch (SignatureException signatureException) {
				System.err.println("Invalid signature!");
				statusDTO.setReturnCode(-1);
				statusDTO.setReturnMessage("fail");
			} catch (IllegalStateException illegalStateException) {
				System.err.println("Invalid Token! " + illegalStateException);
				statusDTO.setReturnCode(-1);
				statusDTO.setReturnMessage("fail");
			}
		}
		logger.info("This is [info] checkToken.....");
		logger.debug("This is [debug] checkToken.....");

		return statusDTO;
	}

}
