package io.macgyver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

public class RequestUtil {

	public static Map<String,String> parseFormBody(RecordedRequest rr) {
		return parseFormBody(rr.getUtf8Body());
	}
	public static Map<String, String> parseFormBody(String body) {
		return parseFormBody(body,"UTF8");
	}
	
	public static Map<String, String> parseFormBody(String body, String encoding) {
		
		try {
			Map<String, String> m = Maps.newHashMap();
			
			for (String kv : Splitter.on("&").omitEmptyStrings().split(Strings.nullToEmpty(body))) {

				List<String> kvList = Splitter.on("=").omitEmptyStrings()
						.splitToList(kv);

				if (kvList.size() == 2) {
					m.put(URLDecoder.decode(kvList.get(0), encoding),
							URLDecoder.decode(kvList.get(1), encoding));
				}

			}

			return m;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
