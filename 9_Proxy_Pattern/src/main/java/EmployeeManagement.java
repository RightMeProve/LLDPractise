/**
 * Client code for Proxy Pattern demo.
 *
 * Client depends on EmployeeDao abstraction, not on concrete implementation.
 * By switching object creation from EmployeeDaoImpl to EmployeeDaoProxy,
 * we inject authorization logic transparently.
 */
public class EmployeeManagement {
    public static void main(String[] args){
        System.out.println("========= Proxy Design Pattern ==========");

        // USER role: read allowed, write denied (as per proxy rules).
        EmployeeDao userProxyObj = new EmployeeDaoProxy("USER");
        userProxyObj.getEmployeeInfo(1); // Access granted.
        userProxyObj.createEmployee(new EmployeeDo()); // Throws RuntimeException: Access Denied!
    }
}
