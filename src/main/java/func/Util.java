package pac1.func;

public class Util {
	public static String sanitizing(String str) {
		return str.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;")
				.replaceAll("\'", "&apos;");
	}

}
