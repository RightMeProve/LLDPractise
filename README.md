# LLD Practice: Design Patterns and System Design 🚀

This repository tracks my hands-on **Low-Level Design (LLD)** journey.  
Each directory contains a focused implementation of a design pattern or a complete LLD project with interview-style explanations.

## 📌 Progress Tracker

| Status | # | Pattern / Topic | Implementation Project | Type |
| :---: | :---: | :--- | :--- | :--- |
| ✅ | 1 | **Strategy Pattern** | Duck Simulator | Behavioral |
| ✅ | 2 | **Observer Pattern** | Weather-O-Rama | Behavioral |
| ✅ | 3 | **Decorator Pattern** | Starbuzz Coffee | Structural |
| ✅ | 4 | **Simple Factory Pattern** | PizzaStore (Idiom) | Creational |
| ✅ | 5 | **Factory Method Pattern** | PizzaStore (Regional) | Creational |
| ✅ | 6 | **Abstract Factory Pattern** | PizzaStore (Ingredients) | Creational |
| ✅ | 7 | **Singleton Pattern** | Singleton Variations | Creational |
| ✅ | 8 | **Chain of Responsibility Pattern** | Logger Chain | Behavioral |
| ✅ | 9 | **Proxy Pattern** | Employee Access Control (`9_Proxy_Pattern`) | Structural |
| ✅ | 10 | **LLD Project** | Notification System | System Design |
| ✅ | 11 | **LLD Project** | Parking Lot | System Design |
| ✅ | 12 | **LLD Project** | Tic Tac Toe | System Design |
| ✅ | 13 | **LLD Project** | Elevator System | System Design |
| ✅ | 14 | **LLD Project** | Snake and Ladder Game | System Design |
| ✅ | 15 | **LLD Project** | Car Rental System | System Design |
| ✅ | 16 | **Data Structure Design** | HashMap Implementation (`Design-HashMap`) | Core DS |



## 🏗 Repository Philosophy

The objective is to learn LLD by building and documenting:
- **Design patterns** with clean abstractions and practical examples
- **System design modules** with SOLID principles and extensibility in mind
- **Interview-oriented explanations** (flow, trade-offs, and design decisions)

Core principles followed:
- **Encapsulate what varies**
- **Open/Closed Principle**
- **Dependency Inversion Principle**

---

## 📂 Project Structure

Each folder is self-contained and can be explored independently.

```text
.
├── 1_Strategy_Pattern
├── 2_Observer_Pattern
├── 3_Decorator_Pattern
├── 4_SimpleFactory_Pattern
├── 5_Factory_Pattern
├── 6_Abstract_Factory
├── 7_Singleton_Pattern
├── 8_Chain_of_Responsibility
├── 9_Proxy_Pattern
├── Design-HashMap
├── Design-Car-Rental-System
├── Design-Notification-System
├── Design-Parking-Lot
├── Design-Tic-Tac-Toe
├── Design-Elevator-System
├── Design-Snake-And-Ladder-Game
└── ...
```

---

## ▶ How to Run a Module

From repository root:

```bash
mvn -f "<module-folder>/pom.xml" clean package
```

Example:

```bash
mvn -f "9_Proxy_Pattern/pom.xml" clean package
```