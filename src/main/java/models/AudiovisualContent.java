package models;

import java.util.Random;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public abstract class AudiovisualContent implements RecordIdentifiable {

	@CsvBindByName
	private final String id = generateId();
	@CsvBindByName
	private String title;
	@CsvBindByName
	private int minutesDuration;
	@CsvBindByName
	private String genre;
	private static int counter = 0;

	private static final Random random = new Random();

	private static synchronized String generateId() {
		int randomNumber = random.nextInt(900) + 100;
		return "AV" + randomNumber + "-" + ++counter;
	}

	protected void printCommonDetails() {
		System.out.println("------------------------------------");
		System.out.println("ID: " + getId());
		System.out.println("Título: " + getTitle());
		System.out.println("Duración en minutos: " + getMinutesDuration());
		System.out.println("Género: " + getGenre());
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException("Unimplemented method 'getName'");

	}

	@Override
	public String getContact() {
		return "No aplica información de contacto";
	}

	@Override
	public String getLinkedInUser() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getLinkedInUser'");
	}

	public abstract void showDetails();
}
