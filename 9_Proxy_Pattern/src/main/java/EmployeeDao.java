/**
 * Subject interface in Proxy Pattern.
 *
 * Both:
 * - Real Subject (EmployeeDaoImpl)
 * - Proxy (EmployeeDaoProxy)
 *
 * implement this same contract so client code can work with either one
 * without knowing whether access control is being applied.
 */
public interface EmployeeDao {
    /**
     * Read operation: fetch/display employee information by ID.
     * In this example we keep the method void and print to console for simplicity.
     */
    void getEmployeeInfo(int empID);

    /**
     * Write operation: create a new employee record.
     * Typically this should be allowed only for privileged roles.
     */
    void createEmployee(EmployeeDo obj);
}
