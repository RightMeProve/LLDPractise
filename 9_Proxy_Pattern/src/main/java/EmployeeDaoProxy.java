/**
 * Proxy (Protection Proxy) for EmployeeDao.
 *
 * Goal:
 * - Intercept client calls
 * - Check authorization based on role
 * - Delegate to real object only if access is allowed
 *
 * Benefits:
 * - Centralized access-control logic
 * - Client still depends on EmployeeDao abstraction
 * - Real Subject stays focused on business operations
 */
public class EmployeeDaoProxy implements EmployeeDao{
    // Real Subject reference: all allowed calls are forwarded here.
    private EmployeeDao employeeDaoObj;

    // Role of current client/session making the request.
    private String clientRole;

    // Proxy decides when/how to instantiate the real object.
    public EmployeeDaoProxy(String clientRole){
        employeeDaoObj = new EmployeeDaoImpl();
        this.clientRole = clientRole;
    }

    @Override
    public void getEmployeeInfo(int empID) {
        // Read access policy:
        // ADMIN and USER are allowed to read.
        if(clientRole.equals("ADMIN") || clientRole.equals("USER")){
            employeeDaoObj.getEmployeeInfo(empID);
        }else {
            // Any role outside allowed set is denied.
            throw new RuntimeException("Access Denied!");
        }
    }

    @Override
    public void createEmployee(EmployeeDo obj) {
        // Write access policy:
        // Only ADMIN is allowed to create employees.
        if(clientRole.equals("ADMIN")){
            employeeDaoObj.createEmployee(obj);
        }
        else {
            throw new RuntimeException("Access Denied!");
        }
    }
}
