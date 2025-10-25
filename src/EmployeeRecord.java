//Importing local date for formatting
import java.time.LocalDate;
//Declaring primary class along with class attributes.
public class EmployeeRecord {
    private final int employID;
    private String employName;
    private String employGen;
    private String employRole;
    private String employStat;
    private LocalDate hireDate;
    public EmployeeRecord(int employID, String employName, String employGen,
                          String employRole, String employStat, LocalDate hireDate) {
        //Parameterized contractors
        this.employID = employID;
        this.employName = employName;
        this.employGen = employGen;
        this.employRole = employRole;
        this.employStat = employStat;
        this.hireDate = hireDate;
    }

    // Getters
    public int getEmployID() {
        return employID;
    }
    public String getEmployName() {
        return employName;
    }
    public String getEmployGen() {
        return employGen;
    }
    public String getEmployRole() {
        return employRole;
    }
    public String getEmployStat() {
        return employStat;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }

    // Setters
    public void setEmployName(String employName) {
        this.employName = employName;
    }
    public void setEmployGen(String employGen) {
        this.employGen = employGen;
    }
    public void setEmployRole(String employRole) {
        this.employRole = employRole;
    }
    public void setEmployStat(String employStat) {
        this.employStat = employStat;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    @Override //Wired to employee record GUI file
    public String toString() {
        return String.format("ID: %d | Name: %s | Gender: %s | Role: %s | Status: %s | Hire Date: %s",
                employID, employName, employGen, employRole, employStat, hireDate.toString());
    }
}
