package de.bwm.owm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.JSONObject;

public class JSONReader {

	public static JSONObject readFile(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			StringBuffer buf = new StringBuffer();
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				buf.append(sCurrentLine);
			}
			if (!buf.toString().equals(Strings.EMPTY)) {
				return new JSONObject(buf.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String readFileInString(String filename) {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "CP1252"));) {

			StringBuffer buf = new StringBuffer();
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				buf.append(sCurrentLine);
			}
			return buf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readResourceFileInString(String filename) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(filename);
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static String readURIToString(URL url) {
		try {
			File resourceFile = new File(url.toURI());
			BufferedReader br = new BufferedReader(new FileReader(resourceFile));

			StringBuffer buf = new StringBuffer();
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				buf.append(sCurrentLine);
			}
			return buf.toString();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	 * 
	 * @param is
	 * @return
	 */
	public static String readInputStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String readZipInString(String fileName, String entryName) {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(fileName);
			ZipEntry entry = zipFile.getEntry(entryName);
			InputStream is = zipFile.getInputStream(entry);
			Scanner s = new Scanner(is).useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
