/**
 * Real Subject in Proxy Pattern.
 *
 * This class contains the core business behavior (actual work).
 * It does NOT handle authorization checks; that responsibility is
 * intentionally kept in the proxy so business logic remains clean.
 */
public class EmployeeDaoImpl implements EmployeeDao{
    @Override
    public void getEmployeeInfo(int empID) {
        // Simulates fetching employee data from DB/service.
        System.out.println("Fetching employee info for ID: " + empID);
    }

    @Override
    public void createEmployee(EmployeeDo obj) {
        // Simulates creating employee data in DB/service.
        System.out.println("Creating employee: " + obj);
    }
}
