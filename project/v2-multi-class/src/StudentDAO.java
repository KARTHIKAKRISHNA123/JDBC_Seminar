import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * StudentDAO — Data Access Object.
 *
 * Every line of SQL in the application lives in this class. Main never writes SQL;
 * it just asks:  dao.insert(student),  dao.findById(3),  dao.delete(5) ...
 *
 * Benefits: SQL is in one place, Main stays readable, and you could swap MySQL for
 * another database by rewriting only this class.
 */
public class StudentDAO {

    private static final String COLUMNS = "id, name, email, phone, age, department";

    /** INSERT — returns the id MySQL generated. */
    public int insert(Student s) throws SQLException {
        String sql = "INSERT INTO students (name, email, phone, age, department) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            if (s.getAge() == null) ps.setNull(4, Types.INTEGER);
            else                    ps.setInt(4, s.getAge());
            ps.setString(5, s.getDepartment());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                int id = keys.getInt(1);
                s.setId(id);
                return id;
            }
        }
    }

    /** SELECT all */
    public List<Student> findAll() throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM students ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return mapAll(rs);
        }
    }

    /** SELECT one — Optional.empty() when the id does not exist (no null checks in Main). */
    public Optional<Student> findById(int id) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM students WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    /** SELECT ... WHERE name LIKE ? */
    public List<Student> findByName(String part) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM students WHERE name LIKE ? ORDER BY name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + part + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return mapAll(rs);
            }
        }
    }

    /** UPDATE email + phone — true if a row was changed. */
    public boolean updateContact(int id, String email, String phone) throws SQLException {
        String sql = "UPDATE students SET email = ?, phone = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, phone);
            ps.setInt(3, id);
            return ps.executeUpdate() == 1;
        }
    }

    /** DELETE — true if a row was removed. */
    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // ---------- ResultSet row -> Student object ----------
    private Student map(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getObject("age", Integer.class),     // keeps SQL NULL as Java null
                rs.getString("department"));
    }

    private List<Student> mapAll(ResultSet rs) throws SQLException {
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }
}
