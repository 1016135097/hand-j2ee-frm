package hand.framework.ebs.util;

//import oracle.apps.fnd.security.HMAC;

public class UrlUtils {

	private static String MAC_EQ = "oas=";

	private static String AND_MAC_EQ = "&" + MAC_EQ;

	private static String QUES_MAC_EQ = "?" + MAC_EQ;

	public static String generateSecureURL(String url) {
		EBSContext context = EBSContext.getInstance();
		//HMAC hmac = context.getHmac();

		if ((url == null)// || (hmac == null)
				) {
			return url;
		}

		int i = url.indexOf("?");

		if (i == -1) {
			i = url.length();
		}

		int j = url.lastIndexOf("/", i);

		String str1 = url.substring(j + 1);

		int k = str1.lastIndexOf(AND_MAC_EQ);
		if (k == -1) {
			k = str1.lastIndexOf(QUES_MAC_EQ);
		}

		if (k != -1) {
			char c = str1.charAt(k);
			int n = MAC_EQ.length() + 1;
			String str2 = str1.substring(k + n);
			str1 = str1.substring(0, k);
			int i1 = str2.indexOf("&");

			if (i1 != -1) {
				str1 = str1 + c + str2.substring(i1 + 1);
			}

		}

		int m = j + str1.length() + MAC_EQ.length() + 26;
		StringBuffer localStringBuffer = new StringBuffer(m);

		String str2 = decode(str1, "UFT-8");
		if (str2 == null)
			str2 = str1;
		url = url.substring(0, j + 1) + str1
				+ (str1.indexOf('?') == -1 ? QUES_MAC_EQ : AND_MAC_EQ)
				//+ hmac.getHMAC(str2, 2)
		;

		return url;
	}

	public static String decode(String paramString, String encoding) {
		String str1 = encoding;

		if (str1 == null) {
			str1 = System.getProperty("file.encoding");
			if (str1 == null) {
				return null;
			}
		}

		StringBuffer localStringBuffer = null;

		for (int i = 0; i < paramString.length(); i++) {
			char c = paramString.charAt(i);
			switch (c) {
			case '+':
				if (localStringBuffer == null) {
					localStringBuffer = new StringBuffer(
							1 * paramString.length());

					localStringBuffer.append(paramString.substring(0, i));
				}
				localStringBuffer.append(' ');
				break;
			case '%':
				try {
					if (paramString.charAt(i + 1) == 'u') {
						try {
							String str3 = ""
									+ (char) Integer
											.parseInt(paramString.substring(
													i + 2, i + 6), 16);

							byte[] arrayOfByte2 = str3.getBytes(str1);

							String str4 = new String(arrayOfByte2, "8859_1");
							if (localStringBuffer == null) {
								localStringBuffer = new StringBuffer(
										1 * paramString.length());
								localStringBuffer.append(paramString.substring(
										0, i));
							}
							localStringBuffer.append(str4);
							i += 5;
						} catch (Exception localException2) {

							return null;
						}
					} else {
						if (localStringBuffer == null) {
							localStringBuffer = new StringBuffer(
									1 * paramString.length());
							localStringBuffer.append(paramString
									.substring(0, i));
						}

						localStringBuffer.append((char) Integer.parseInt(
								paramString.substring(i + 1, i + 3), 16));

						i += 2;
					}
				} catch (NumberFormatException localNumberFormatException) {
					return null;
				}

			default:
				if (localStringBuffer == null)
					continue;
				localStringBuffer.append(c);
			}

		}

		String str2 = null;
		try {
			byte[] arrayOfByte1 = null;

			if (localStringBuffer == null)
				arrayOfByte1 = paramString.getBytes("8859_1");
			else {
				arrayOfByte1 = localStringBuffer.toString().getBytes("8859_1");
			}
			str2 = new String(arrayOfByte1, str1);
		} catch (Exception localException1) {
			return null;
		}
		return str2;
	}
}
