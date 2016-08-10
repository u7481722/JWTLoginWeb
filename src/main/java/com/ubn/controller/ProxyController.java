package com.ubn.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.ubn.constant.ConfingConstants;
import com.ubn.dto.ReturnDTO;
import com.ubn.dto.SetTokenDTO;

@RestController
public class ProxyController {
	
	private static final Logger logger = Logger.getLogger(ProxyController.class);
	private RestTemplate restTemplate = new RestTemplate();
	private String url = ConfingConstants.DOMAIN_URL +"/"+ ConfingConstants.SERVICE_NAME;

	@RequestMapping(value = {"/Key/{m3u8Name}"}, method = {RequestMethod.GET})
	public void Keys(@PathVariable("m3u8Name") String strM3U8Name, @RequestParam("Token") String strToken,
			@RequestParam("UserId") String strUserId, HttpServletResponse response) throws IOException {
		
		String _url = this.url + "/Key/" + strM3U8Name + "?Token=" + strToken + "&UserId=" + strUserId;
		logger.info("get key url==>" + _url);
		byte[] b = restTemplate.getForObject(_url, byte[].class);
		logger.info("get b==>" + b);
		response.getOutputStream().write(b);
		
	}

	@RequestMapping(value = {"/SetToken"}, method = {RequestMethod.POST})
	public ReturnDTO setToken(@RequestBody SetTokenDTO requestDTO) {
		return  restTemplate.postForObject(url + "/SetToken", requestDTO, ReturnDTO.class);
	}

}
