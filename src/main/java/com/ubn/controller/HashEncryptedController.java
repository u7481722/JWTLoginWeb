package com.ubn.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ubn.dto.HashDTO;

@RestController
public class HashEncryptedController {
	private static final Logger logger = Logger.getLogger(HashEncryptedController.class);
	private static final String WOWZA_PREFIX = "wowzatoken";
	private static final String HASH_ALGORITHM = "SHA-256";

	@RequestMapping(value = { "/getHash" }, method = { RequestMethod.POST })
	public @ResponseBody String getHash(HttpServletRequest request, @RequestBody HashDTO dto)
			throws NoSuchAlgorithmException {
		String sharedSecret = "d5e34106935ff777";
		String contentPath = dto.getContextPath();
		String ip = request.getRemoteAddr();

		SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date sTime = null;
		Date eTime = null;
		try {
			sTime = simple.parse(dto.getStartTime());
			eTime = simple.parse(dto.getEndTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
		startTime.setTime(sTime);

		Calendar endTime = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
		endTime.setTime(eTime);

		String starttime = String.valueOf(startTime.getTimeInMillis() / 1000L);
		String endtime = String.valueOf(endTime.getTimeInMillis() / 1000L);

		String strHash = getHash(contentPath, sharedSecret, ip, starttime, endtime, new String[0]);

		StringBuilder strResult = new StringBuilder();
		strResult.append("wowzatoken").append("starttime=").append(starttime).append("&").append("wowzatoken")
				.append("endtime=").append(endtime).append("&").append("wowzatoken").append("hash=").append(strHash);

		logger.info(strResult.toString());

		return strResult.toString();
	}

	private static String getHash(String contentPath, String sharedSecret, String ip, String starttime, String endtime,
			String... customParameterAry) throws NoSuchAlgorithmException {
		String text = getSortString(contentPath, sharedSecret, ip, starttime, endtime, customParameterAry);
		logger.info("text=" + text);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		String strResult = Base64.encodeBase64String(hash);
		String strUrlSafeResult = strResult.replaceAll("\\+", "-").replaceAll("\\/", "_");
		return strUrlSafeResult;
	}

	private static String getSortString(String contentPath, String sharedSecret, String ip, String starttime,
			String endtime, String... customParameterAry) {
		List<String> list = new ArrayList<String>();
		list.add(sharedSecret);
		if (StringUtils.isNotEmpty(starttime)) {
			list.add("wowzatokenstarttime=" + starttime);
		}
		if (StringUtils.isNotEmpty(endtime)) {
			list.add("wowzatokenendtime=" + endtime);
		}
		if (StringUtils.isNotEmpty(ip)) {
			list.add(ip);
		}
		if (customParameterAry.length > 0) {
			String[] arrayOfString;
			int j = (arrayOfString = customParameterAry).length;
			for (int i = 0; i < j; i++) {
				String strCustomParameter = arrayOfString[i];
				list.add(strCustomParameter);
			}
		}
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			String str = (String) list.get(i);
			System.out.println("i=" + i + ",str=" + str);

			sb.append(i == 0 ? "" : "&").append(str);
		}
		return contentPath + "?" + sb.toString();
	}

}
