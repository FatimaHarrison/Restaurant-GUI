import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//Declaring primary class along with class attributes.
public class Record {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private Integer partySize;
    private String date;
    private String time;
    private String notes;
//Setter methods
    public Record(Integer id, String name, String email, String phoneNumber,
                  Integer partySize, String date, String time, String notes) {
        setId(id);
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPartySize(partySize);
        setDate(date);
        setTime(time);
        setNotes(notes);
    }
//Getter methods
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Integer getPartySize() { return partySize; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getNotes() { return notes; }
//Exceptions validations for ID number
    public void setId(Integer id) {
        if (id == null || id < 1000000 || id > 9999999) {
            throw new IllegalArgumentException("ID must be a 7-digit number.");
        }
        this.id = id;
    }
//Exceptions validations for customer name
    public void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 25) {
            throw new IllegalArgumentException("Name must be 1–25 characters.");
        }
        this.name = name.trim();
    }
    //Exceptions validations for customer email
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email.trim();
    }
    //Exceptions validations for customer phone number
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;
    }
    //Exceptions validations for customer reservation party size
    public void setPartySize(Integer partySize) {
        if (partySize == null || partySize < 1 || partySize > 13) {
            throw new IllegalArgumentException("Party size must be between 1 and 13.");
        }
        this.partySize = partySize;
    }
    //Exceptions validations for entering reservation date
    public void setDate(String date) {
        if (date == null || !date.matches("\\d{4}/\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Date must be in YYYY/MM/DD format.");
        }
        this.date = date;
    }
    //Exceptions validations for entering reservation time
    public void setTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty.");
        }
        try {
            time = time.trim().toUpperCase().replaceAll("(?<=\\d)(AM|PM)", " $1");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime parsedTime = LocalTime.parse(time, formatter);
            this.time = parsedTime.format(formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Time must be in hh:mm AM/PM format (e.g., 02:30 PM).");
        }
    }
    //Exceptions validations for entering reservation notes
    public void setNotes(String notes) {
        if (notes == null || notes.matches(".*[<>\"].*")) {
            throw new IllegalArgumentException("Notes contain unsafe characters.");
        }
        this.notes = notes.trim();
    }

    @Override //Wired to Host GUI
    public String toString() {
        return String.format("ID: %d | Name: %s | Email: %s | Phone: %s | Party Size: %d | Date: %s | Time: %s | Notes: %s",
                id, name, email, phoneNumber, partySize, date, time, notes);
    }
}
