# Design Patterns — Java

A collection of commonly used **Object-Oriented Design Patterns** implemented and explained in Java.

The notes focus on understanding the **problem**, **why the pattern is required**, **how it solves the problem**, and the key relationships involved.

---

## Patterns Covered

- [Strategy Pattern](#1-strategy-pattern)
- [Observer Pattern](#2-observer-pattern)
- [Decorator Pattern](#3-decorator-pattern)
- [Builder Pattern](#4-builder-pattern)
- [Builder vs Decorator](#builder-vs-decorator)

---

# 1. Strategy Pattern

## Problem

Suppose we have a parent `Vehicle` class with a `drive()` method and multiple child classes:

- `SportsVehicle`
- `FamilyVehicle`
- `OffRoadVehicle`
- etc.

If multiple child classes have the same implementation of `drive()`, we may end up duplicating the same code.

For example:

```java
class SportsVehicle extends Vehicle {
    public void drive() {
        // Normal driving
    }
}

class FamilyVehicle extends Vehicle {
    public void drive() {
        // Same normal driving logic
    }
}
```

If the driving logic changes, we have to modify it in multiple classes.

This leads to:

- Code duplication
- Difficult maintenance
- Tight coupling between the vehicle and its behavior

## Solution

Extract the changing behavior into a separate **Strategy**.

Create a `DrivingStrategy` interface:

```java
interface DrivingStrategy {
    void drive();
}
```

Then create different implementations:

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

The `Vehicle` now uses a `DrivingStrategy`:

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

The required strategy can be injected through the constructor:

```java
Vehicle familyVehicle =
        new Vehicle(new NormalDrivingStrategy());

Vehicle sportsVehicle =
        new Vehicle(new SportsDrivingStrategy());
```

## Key Idea

> Encapsulate behavior that can vary and make that behavior interchangeable.

Strategy follows the principle:

> **Favor composition over inheritance.**

Instead of inheriting different implementations of `drive()`, the vehicle **has-a** `DrivingStrategy`.

---

# 2. Observer Pattern

## Problem

Suppose we have a weather system.

Whenever the weather changes, multiple devices need to be notified:

- Mobile App
- Monitor
- Weather Station
- Digital Billboard

If the weather system directly manages all these devices, it becomes tightly coupled to them.

```text
Weather
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

Example:

```text
WeatherObservable
```

It maintains a list of observers and provides operations such as:

```text
addObserver()
removeObserver()
notifyObservers()
```

### Observer

Objects that want to receive updates.

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
             -----------------------
             |          |          |
          Mobile     Monitor    Billboard
         Observer    Observer    Observer
```

When the weather changes:

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

An observer generally maintains a reference to the observable:

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
MonitorObserver
      |
      | HAS-A
      ↓
WeatherObservable
```

And:

```text
MonitorObserver IS-A Observer
WeatherObservable IS-A Observable
```

## Key Idea

> When the state of one object changes, automatically notify all interested objects without tightly coupling the subject to its observers.

---

# 3. Decorator Pattern

## Problem

Suppose we have a pizza and want to customize it with toppings:

- Onion
- Capsicum
- Cheese
- Mushroom
- Extra Cheese
- etc.

One approach would be creating a class for every possible combination:

```text
Pizza
PizzaWithOnion
PizzaWithCheese
PizzaWithOnionAndCheese
PizzaWithOnionCheeseAndCapsicum
PizzaWithOnionCapsicum
...
```

This results in **class explosion** because the number of classes grows with the possible combinations.

## Solution

Use the **Decorator Pattern**.

Create a common `Pizza` component:

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

Now create decorators.

A decorator:

1. **IS-A Pizza**
2. **HAS-A Pizza**

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

We can now combine decorators dynamically:

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

> Add responsibilities or behavior to an object dynamically without modifying its original class.

## Important Relationship

```text
          Decorator
          /       \
       IS-A       HAS-A
        /           \
     Pizza  ←────── Pizza
```

This is one of the most important things to remember about the Decorator Pattern.

---

# 4. Builder Pattern

## Problem

Suppose we have a `Student` class with many fields:

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

Some of these fields may be optional.

We could create multiple constructors:

```java
Student(name)

Student(name, age)

Student(name, age, email)

Student(name, age, email, phone)

...
```

This causes several problems:

- Too many constructors
- Long parameter lists
- Poor readability
- Easy to make mistakes while passing arguments
- Difficult constructor overloading

For example:

```java
Student(String name, String email)

Student(String email, String name)
```

These cannot coexist in Java because Java identifies overloaded methods/constructors using their **parameter types**, not parameter names.

## Solution

Use the **Builder Pattern**.

Create a builder that stores the values required to construct the object.

The builder provides methods such as:

```java
name()
age()
email()
phone()
```

Each method sets the value and returns the builder itself.

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

For example:

```text
Director
   ↓
Builder
   ↓
Student
```

The Director may define predefined construction flows:

```text
createEngineeringStudent()
createMedicalStudent()
createMBAStudent()
```

The Director decides **how the object should be constructed**, while the Builder performs the actual construction.

For simple Builder implementations, a Director is often unnecessary.

## Key Idea

> Separate the construction of a complex object from the object itself, allowing the object to be created with different configurations.

---

# Builder vs Decorator

| Builder | Decorator |
|---|---|
| Creational Design Pattern | Structural Design Pattern |
| Focuses on creating an object | Focuses on adding behavior/responsibility |
| Configures object properties | Combines additional behaviors |
| Used during object construction | Can be applied dynamically at runtime |
| Example: `Student.Builder()` | Example: `new CheeseDecorator(pizza)` |

## Easy Way to Remember

### Builder

> **"How do I create this object?"**

```text
Builder → Object
```

Example:

```java
Student student = new StudentBuilder()
        .name("Satyam")
        .age(25)
        .build();
```

### Decorator

> **"I already have an object. How can I add more behavior to it?"**

```text
Decorator → Decorator → Object
```

Example:

```java
Pizza pizza = new BasicPizza();

if (wantCheese) {
    pizza = new CheeseDecorator(pizza);
}

if (wantOnion) {
    pizza = new OnionDecorator(pizza);
}
```

The decorators can be selected and combined **at runtime**, which is what is meant by **dynamic composition**.

---

# Quick Revision

| Pattern | Main Problem | Main Solution |
|---|---|---|
| **Strategy** | Duplicate/variable behavior across classes | Extract behavior into interchangeable strategies |
| **Observer** | Multiple objects need updates when state changes | Maintain observers and notify them |
| **Decorator** | Too many classes for combinations of features | Wrap objects and add behavior dynamically |
| **Builder** | Too many constructors / complex object creation | Build objects step-by-step |

## One-Line Definitions

**Strategy**

> Encapsulates interchangeable behaviors and allows them to be selected independently of the class using them.

**Observer**

> Establishes a one-to-many relationship where changes in one object notify its dependents.

**Decorator**

> Dynamically adds responsibilities or behavior to an existing object without modifying its class.

**Builder**

> Separates complex object construction from its representation and allows step-by-step configuration.