package main.java.cubeGame.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import main.java.cubeGame.model.Die;

import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java.util.List;
/*
 * Utility Class used to 
 */
public class SheetManager {

	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static final String SPREADSHEET_ID = "1WGQgmEM0d8eYutIDekvzvsNEpAWS50jq0auys3wC9MY"; 
	private static final String DICESHEET_ID = "Story Game Records";
	private static final String TOTALS_ID = "Totals";
	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved credentials/ folder.
	 */
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final String CREDENTIALS_FILE_PATH = "/main/resources/credentials.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = SheetManager.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	public static void writeDiceToSheet(Die[] dice) throws GeneralSecurityException, IOException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		final String firstColumn = "'" + DICESHEET_ID + "'!A:A";
		Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
		ValueRange readResponse = service.spreadsheets().values().get(SPREADSHEET_ID, firstColumn).execute();
		List<List<Object>> values = readResponse.getValues();
		if (values == null || values.isEmpty()) {
			System.out.println("No data found.");
			return;
		}
		int size = values.get(0).size() + 1; // next empty row
		// Write the values to the next row
		List<List<Object>> valueMatrix = new ArrayList<>(); // 2D List that will only have its first row filled
		List<Object> row = new ArrayList<>(); // Row containing
		valueMatrix.add(row);
		row.add(new SimpleDateFormat("MM/dd/YY 'at' hh:mm:ss").format(new Date())); // Add Timestamp
		for (Die d : dice)
			row.add(d.getName()); // Add the dice variable

		ValueRange body = new ValueRange().setValues(valueMatrix);
		String nextRow = String.format("'" + DICESHEET_ID + "'!A%s:F%s", size, size);
		// Create a Spreadsheet request to append our data to the next empty row
		Sheets.Spreadsheets.Values.Append request = service.spreadsheets().values()
				.append(SPREADSHEET_ID, nextRow, body).setValueInputOption("RAW").setInsertDataOption("INSERT_ROWS");
		request.execute();
	}
}
