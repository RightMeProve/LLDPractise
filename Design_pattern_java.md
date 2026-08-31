# Design Patterns

A collection of commonly used **Object-Oriented Design Patterns** implemented and explained in Java.

## Patterns Covered

- [Strategy Pattern](#1-strategy-pattern)
- [Observer Pattern](#2-observer-pattern)
- [Decorator Pattern](#3-decorator-pattern)
- [Builder Pattern](#4-builder-pattern)
- [Factory Pattern](#5-factory-pattern)
- [Abstract Factory Pattern](#6-abstract-factory-pattern)
- [Builder vs Decorator](#builder-vs-decorator)
- [Factory vs Abstract Factory vs Builder](#factory-vs-abstract-factory-vs-builder)

---

# 1. Strategy Pattern

## Problem Statement

Suppose we have a parent `Vehicle` class with a `drive()` method, and multiple types of vehicles extend it:

- `SportsVehicle`
- `FamilyVehicle`
- `OffRoadVehicle`
- etc.

The problem occurs when multiple child classes have the **same implementation** of `drive()`.

For example, suppose `SportsVehicle` and `FamilyVehicle` both use the same normal driving behavior.

We might end up writing the same code in both classes.

If the driving behavior needs to change, we have to modify it in multiple classes.

This leads to:

- Code duplication
- Difficult maintenance
- Tight coupling between the vehicle and its driving behavior

## Solution

Instead of implementing `drive()` directly inside every vehicle class, we extract the behavior into a separate **Strategy**.

```java
interface DrivingStrategy {
    void drive();
}
```

Different strategies can then be implemented:

```java
class NormalDrivingStrategy implements DrivingStrategy {

    @Override
    public void drive() {
        System.out.println("Normal driving");
    }
}

class SportsDrivingStrategy implements DrivingStrategy {

    @Override
    public void drive() {
        System.out.println("Sports driving");
    }
}
```

The `Vehicle` can have a `DrivingStrategy`:

```java
class Vehicle {

    private DrivingStrategy drivingStrategy;

    Vehicle(DrivingStrategy drivingStrategy) {
        this.drivingStrategy = drivingStrategy;
    }

    public void drive() {
        drivingStrategy.drive();
    }
}
```

The required strategy can be passed using **constructor injection**:

```java
Vehicle familyVehicle =
        new Vehicle(new NormalDrivingStrategy());

Vehicle sportsVehicle =
        new Vehicle(new SportsDrivingStrategy());
```

Now, if the implementation of `NormalDrivingStrategy` changes, we only need to change it in one place.

## Key Idea

> Encapsulate a behavior that can vary and make it interchangeable.

Instead of inheriting behavior from a parent class, we **compose** the required behavior into the object.

This follows:

> **Favor composition over inheritance.**

---

# 2. Observer Pattern

## Problem Statement

Suppose we have a weather system.

Whenever the weather changes, multiple devices need to be updated:

- Mobile App
- Monitor
- Weather Station
- Digital Billboard

If the weather system directly manages every device, it becomes tightly coupled to them.

```text
Weather
   |
   ├── Mobile
   ├── Monitor
   ├── Weather Station
   └── Billboard
```

Adding or removing a device would require modifying the weather system.

## Solution

The **Observer Pattern** establishes a **one-to-many relationship** between an object and its dependents.

There are two main components:

### Observable / Subject

The object whose state changes.

In our example:

```text
WeatherObservable
```

It maintains a list of observers and provides methods such as:

```text
addObserver()
removeObserver()
notifyObservers()
```

### Observer

The objects that want to receive updates.

For example:

```text
MobileObserver
MonitorObserver
BillboardObserver
```

The relationship becomes:

```text
              WeatherObservable
                     |
          -------------------------
          |           |           |
       Mobile      Monitor    Billboard
      Observer     Observer    Observer
```

Whenever the weather changes:

```text
WeatherObservable
       |
       ↓
notifyObservers()
       |
       ↓
All registered observers are updated
```

## Relationships

An observer can maintain a reference to the observable:

```java
class MonitorObserver implements Observer {

    private WeatherObservable weather;

    MonitorObserver(WeatherObservable weather) {
        this.weather = weather;
    }
}
```

Therefore:

```text
MonitorObserver IS-A Observer
WeatherObservable IS-A Observable

MonitorObserver HAS-A WeatherObservable
```

The `HAS-A` relationship allows the observer to register itself with the particular observable it wants to observe.

## Key Idea

> When the state of one object changes, automatically notify all interested objects without tightly coupling the subject to its observers.

---

# 3. Decorator Pattern

## Problem Statement

Suppose we have a pizza and want to customize it with different toppings:

- Capsicum
- Onion
- Extra Cheese
- Mushroom
- Hand Tossed
- Fresh Pan
- etc.

If we create a separate class for every possible combination, we can end up with a huge number of classes.

For example:

```text
Pizza
PizzaWithCheese
PizzaWithOnion
PizzaWithCheeseAndOnion
PizzaWithCheeseOnionAndCapsicum
PizzaWithOnionAndCapsicum
...
```

This leads to **class explosion** because of the permutations and combinations of customizations.

## Solution

Instead of creating a class for every combination, we use the **Decorator Pattern**.

We create a common component:

```java
interface Pizza {
    int getCost();
}
```

The base pizza implements it:

```java
class BasicPizza implements Pizza {

    @Override
    public int getCost() {
        return 100;
    }
}
```

Now we create decorators.

A decorator has both:

- **IS-A relationship** with the component
- **HAS-A relationship** with the component

For example:

```java
class CheeseDecorator implements Pizza {

    private Pizza pizza;

    CheeseDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 30;
    }
}
```

We can now combine decorators:

```java
Pizza pizza = new BasicPizza();

pizza = new CheeseDecorator(pizza);
pizza = new OnionDecorator(pizza);
pizza = new CapsicumDecorator(pizza);
```

Conceptually:

```text
CapsicumDecorator
        ↓
 OnionDecorator
        ↓
CheeseDecorator
        ↓
   BasicPizza
```

Each decorator wraps the previous object and adds its own behavior.

## Key Idea

> Dynamically add responsibilities or behavior to an existing object without modifying its original class.

---

# 4. Builder Pattern

## Problem Statement

Suppose we have a class with many fields, and many of them are optional:

```java
class Student {

    String name;
    int age;
    String email;
    String phone;
    String address;
    String college;
    String branch;
}
```

One approach would be to create multiple constructors:

```text
Student(name)

Student(name, age)

Student(name, age, email)

Student(name, age, email, phone)

...
```

This creates several problems:

- Too many constructors
- Long parameter lists
- Poor readability
- Easy to pass arguments in the wrong order
- Difficult to maintain

Also, Java does not consider parameter names while overloading.

For example:

```java
Student(String name, String email)
Student(String email, String name)
```

These cannot coexist because they have the same parameter types:

```text
(String, String)
```

## Solution

Use the **Builder Pattern**.

Create a builder that contains the values required to construct the `Student`.

The builder provides methods such as:

```text
name()
age()
email()
phone()
```

Each method sets the corresponding value and returns the builder itself.

This enables method chaining:

```java
Student student = new StudentBuilder()
        .name("Satyam")
        .age(25)
        .email("abc@gmail.com")
        .phone("9999999999")
        .build();
```

The `build()` method finally creates and returns the actual `Student` object.

## Director

A `Director` is **optional**.

It is useful when we have predefined construction processes or business rules.

```text
Director
   ↓
Builder
   ↓
Student
```

The Director can define predefined construction flows such as:

```text
createEngineeringStudent()
createMedicalStudent()
createMBAStudent()
```

The Director decides the **sequence/procedure of construction**, while the Builder performs the construction.

For simple Builder implementations, a Director is often unnecessary.

## Key Idea

> Separate the construction of a complex object from the object itself, allowing the object to be created step-by-step with different configurations.

---

# Builder vs Decorator

| Builder | Decorator |
|---|---|
| Creational Design Pattern | Structural Design Pattern |
| Focuses on creating an object | Focuses on adding behavior/responsibility |
| Configures object properties | Combines additional behaviors |
| Used during object construction | Can be applied dynamically at runtime |
| Example: `StudentBuilder` | Example: `CheeseDecorator` |

### Easy Way to Remember

**Builder:**

> "How do I create/configure this object?"

```text
Builder → Object
```

**Decorator:**

> "I already have an object. How can I add more behavior to it?"

```text
Decorator → Decorator → Object
```

---

# 5. Factory Pattern

## Problem Statement

Suppose we have different implementations of a `Notification`:

```text
EmailNotification
SMSNotification
PushNotification
```

All of them implement:

```java
interface Notification {
    void send();
}
```

Now imagine the client receives the notification type from a request:

```java
String type = request.getNotificationType();
```

The client needs a `Notification`, but it should not be responsible for deciding which concrete class to create.

Without a Factory, the client may contain:

```java
Notification notification;

if (type.equals("EMAIL")) {
    notification = new EmailNotification();
}
else if (type.equals("SMS")) {
    notification = new SMSNotification();
}
else if (type.equals("PUSH")) {
    notification = new PushNotification();
}
```

Now the client has **two responsibilities**:

1. Decide which concrete object to create
2. Use that object

This also couples the client directly to all concrete notification classes.

## Solution

Delegate the object-creation responsibility to a **Factory**.

Create:

```text
NotificationFactory
```

which is responsible for deciding which concrete notification should be created.

```java
class NotificationFactory {

    public static Notification create(String type) {

        if (type.equals("EMAIL")) {
            return new EmailNotification();
        }

        if (type.equals("SMS")) {
            return new SMSNotification();
        }

        if (type.equals("PUSH")) {
            return new PushNotification();
        }

        throw new IllegalArgumentException("Unknown type");
    }
}
```

Now the client simply asks for the required abstraction:

```java
Notification notification =
        NotificationFactory.create(type);

notification.send();
```

The client doesn't need to know which concrete implementation is being created.

## Key Idea

> Encapsulate the decision of which concrete object to create and separate object creation from object usage.

### Important Point

Factory is **not needed simply because we want to avoid writing `new`**.

If we always know that we need a `Car`, this is perfectly fine:

```java
Vehicle vehicle = new Car();
```

A Factory becomes useful when the **concrete type needs to be selected dynamically** or when object-creation logic is complex/repeated and should be centralized.

---

# 6. Abstract Factory Pattern

## Problem Statement

Suppose our application needs a group of related objects.

For example, a GUI application supports:

- Windows
- Mac

Each platform has its own family of UI components.

### Windows Family

```text
WindowsButton
WindowsCheckbox
WindowsTextField
```

### Mac Family

```text
MacButton
MacCheckbox
MacTextField
```

We want to ensure that objects from the same family are created together.

For example:

```text
WindowsButton + WindowsCheckbox      ✅

MacButton + MacCheckbox              ✅

WindowsButton + MacCheckbox          ❌
```

The problem is that the client should not have to worry about:

> "Which Windows/Mac implementation should I create for every component?"

It should simply say:

> "I want the Windows UI family."

## Solution

Create an **Abstract Factory** that defines methods for creating the entire family of related objects.

```java
interface GUIFactory {

    Button createButton();

    Checkbox createCheckbox();

    TextField createTextField();
}
```

Now create concrete factories for each family.

### Windows Factory

```java
class WindowsFactory implements GUIFactory {

    public Button createButton() {
        return new WindowsButton();
    }

    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }

    public TextField createTextField() {
        return new WindowsTextField();
    }
}
```

### Mac Factory

```java
class MacFactory implements GUIFactory {

    public Button createButton() {
        return new MacButton();
    }

    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }

    public TextField createTextField() {
        return new MacTextField();
    }
}
```

Now the client chooses the factory/family:

```java
GUIFactory factory = new WindowsFactory();

Button button = factory.createButton();
Checkbox checkbox = factory.createCheckbox();
TextField textField = factory.createTextField();
```

The client gets a consistent family of objects without knowing their concrete classes.

## Key Idea

> Provide an interface for creating a family of related objects without specifying their concrete classes.

---

# Factory vs Abstract Factory

## Factory

Factory generally focuses on creating **one type/category of product**.

```text
VehicleFactory
      |
      ├── Car
      ├── Bike
      └── Truck
```

The question is:

> **"Which Vehicle should I create?"**

Example:

```java
Vehicle vehicle =
        VehicleFactory.createVehicle(type);
```

---

## Abstract Factory

Abstract Factory focuses on creating a **family of related products**.

```text
                 GUIFactory
                  /       \
                 /         \
       WindowsFactory     MacFactory
             |                 |
       -------------       -------------
       |     |     |       |     |     |
    Button Checkbox Text  Button Checkbox Text
```

The question is:

> **"Which family of related objects should I create?"**

Example:

```java
GUIFactory factory = new WindowsFactory();

Button button = factory.createButton();
Checkbox checkbox = factory.createCheckbox();
```

Both objects belong to the Windows family.

---

# Factory vs Abstract Factory vs Builder

| Pattern | Main Question |
|---|---|
| **Factory** | Which object should I create? |
| **Abstract Factory** | Which family of related objects should I create? |
| **Builder** | How should I construct/configure this complex object? |

### Factory

```text
Choose the concrete type
          ↓
     Create object
```

### Abstract Factory

```text
Choose the product family
          ↓
Create related objects from that family
```

### Builder

```text
Choose properties step-by-step
          ↓
     Build object
```

---

# Quick Revision

### Strategy

> Encapsulates interchangeable behaviors and allows them to be selected independently of the class using them.

### Observer

> Establishes a one-to-many relationship where a change in one object notifies its dependents.

### Decorator

> Dynamically adds responsibilities or behavior to an existing object without modifying its original class.

### Builder

> Separates complex object construction from its representation and allows step-by-step configuration.

### Factory

> Encapsulates the decision of which concrete object to create and separates object creation from object usage.

### Abstract Factory

> Creates families of related objects without exposing their concrete implementations.