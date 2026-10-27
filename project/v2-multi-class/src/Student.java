/**
 * Student — the MODEL: one object = one row of the students table.
 * It only holds data. It knows nothing about MySQL or the console.
 */
public class Student {

    private Integer id;          // Integer (not int) so it can be null before the DB assigns it
    private String name;
    private String email;
    private String phone;
    private Integer age;         // null = unknown, matches SQL NULL
    private String department;

    public Student(Integer id, String name, String email, String phone, Integer age, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.department = department;
    }

    public Integer getId()          { return id; }
    public String getName()         { return name; }
    public String getEmail()        { return email; }
    public String getPhone()        { return phone; }
    public Integer getAge()         { return age; }
    public String getDepartment()   { return department; }

    public void setId(Integer id)        { this.id = id; }
    public void setEmail(String email)   { this.email = email; }
    public void setPhone(String phone)   { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("%-4d %-14s %-24s %-11s %-4s %s",
                id, name, email,
                phone == null ? "-" : phone,
                age == null ? "-" : age,
                department);
    }
}
